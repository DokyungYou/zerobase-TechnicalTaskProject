package com.zerobase.shopreservation.service;

import com.zerobase.shopreservation.common.ResponseMessage;
import com.zerobase.shopreservation.common.ServiceResult;
import com.zerobase.shopreservation.common.exception.BizException;
import com.zerobase.shopreservation.dto.request.LoginInput;
import com.zerobase.shopreservation.dto.request.customer.ArriveCheckInput;
import com.zerobase.shopreservation.dto.request.customer.ReservationShopInput;
import com.zerobase.shopreservation.dto.request.customer.ReviewInput;
import com.zerobase.shopreservation.dto.request.customer.SignUpCustomerInput;
import com.zerobase.shopreservation.dto.response.ReviewResponse;
import com.zerobase.shopreservation.dto.type.ReservationStatus;
import com.zerobase.shopreservation.entity.Reservation;
import com.zerobase.shopreservation.entity.Review;
import com.zerobase.shopreservation.entity.Shop;
import com.zerobase.shopreservation.entity.UserCustomer;
import com.zerobase.shopreservation.repository.ReservationRepository;
import com.zerobase.shopreservation.repository.ReviewRepository;
import com.zerobase.shopreservation.repository.ShopRepository;
import com.zerobase.shopreservation.repository.UserCustomerRepository;
import com.zerobase.shopreservation.util.JwtUtils;
import com.zerobase.shopreservation.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{

    private final UserCustomerRepository userCustomerRepository;
    private final ShopRepository shopRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;


    // 이용자 회원가입
    @Override
    public ServiceResult signUp(SignUpCustomerInput signUpCustomerInput) {

        //아이디, 이메일, 연락처 중복체크
        if(userCustomerRepository.existsByEmail(signUpCustomerInput.getEmail())){
            return ServiceResult.fail("입력하신 이메일은 이미 사용중입니다.");
        }
        if(userCustomerRepository.existsByUserId(signUpCustomerInput.getCustomerId())){
            return ServiceResult.fail("입력하신 아이디는 이미 사용중입니다.");
        }
       if(userCustomerRepository.existsByPhoneNumber(signUpCustomerInput.getPhoneNumber())) {
            return ServiceResult.fail("입력하신 휴대전화번호는 이미 사용중입니다.");
        }


        if(signUpCustomerInput.getNickname() == null){
            signUpCustomerInput.setNickname("customer" + userCustomerRepository.countAll());
        }
        if(userCustomerRepository.existsByNickName(signUpCustomerInput.getNickname())){
            return ServiceResult.fail("입력하신 닉네임은 이미 사용중입니다.");
        }


        UserCustomer customer = UserCustomer.createUserCustomer(signUpCustomerInput);
        userCustomerRepository.save(customer);
        return ServiceResult.success();
    }


    // 이용자 로그인
    @Override
    public ResponseMessage login(LoginInput loginInput) {
        Optional<UserCustomer> optionalCustomer = userCustomerRepository.findByUserId(loginInput.getId());
        if(!optionalCustomer.isPresent()){
            return ResponseMessage.fail("로그인에 실패했습니다!");
        }
        UserCustomer userCustomer = optionalCustomer.get();

        if(!PasswordUtils.equalPassword(loginInput.getPassword(),userCustomer.getPassword())){
            return ResponseMessage.fail("로그인에 실패했습니다!");
        }

        String customerToken = JwtUtils.createToken(userCustomer);
        return  ResponseMessage.success(customerToken);
    }



    // 예약 요청
    @Override
    public ServiceResult reservationShop(String email, ReservationShopInput reservationShopInput) {
            //이용자
            Optional<UserCustomer> optionalCustomer = userCustomerRepository.findByEmail(email);
            if(optionalCustomer.isEmpty()){
                return ServiceResult.fail("존재하지 않는 이용자입니다.");
            }
            UserCustomer userCustomer = optionalCustomer.get();


        // 가게
            Optional<Shop> optionalShop = shopRepository.findById(reservationShopInput.getShopId());
            if(optionalShop.isEmpty()){
                return ServiceResult.fail("해당 가게가 존재하지 않습니다.");
            }
            Shop shop = optionalShop.get();


            if(!shop.isBookable()){
                return ServiceResult.fail("예약이 불가능한 곳입니다.");
            }



            Reservation reservation
                    = Reservation.reservationShop(reservationShopInput, shop, userCustomer);

            reservationRepository.save(reservation);
            return ServiceResult.success();
            
    }
    
    //내가 예약한 목록 가져오기  (이것도 shop다 나오는거 수정)
    @Override
    public List<Reservation> getMyReservationList(String email) {
        //이용자
        Optional<UserCustomer> optionalCustomer = userCustomerRepository.findByEmail(email);
        if(optionalCustomer.isEmpty()){
            throw new BizException("존재하지 않는 이용자입니다.");
        }
        UserCustomer userCustomer = optionalCustomer.get();

        return reservationRepository.findByUserCustomer(userCustomer);
    }

    //예약 취소
    @Override
    public ServiceResult cancelReservation(Long reservationId, String email) {
        Optional<UserCustomer> optionalCustomer = userCustomerRepository.findByEmail(email);
        if(optionalCustomer.isEmpty()){
            return ServiceResult.fail("존재하지 않는 이용자입니다.");
        }
        UserCustomer userCustomer = optionalCustomer.get();

        Optional<Reservation> optionalReservation = reservationRepository.findByIdAndUserCustomer(reservationId, userCustomer);
        if(optionalReservation.isEmpty()){
            return ServiceResult.fail("예약 건이 존재하지않습니다.");
        }
        Reservation reservation = optionalReservation.get();

        if(LocalDateTime.now().isAfter(reservation.getReservationDateTime().minusDays(7))){
            return ServiceResult.fail("예약취소 가능한 날이 지났습니다. (매장 측에 문의해주세요.)");
        }

        reservation.setReservationStatus(ReservationStatus.CANCELLATION);
        reservation.setStatusUpdate(LocalDateTime.now());
        reservationRepository.save(reservation);
        return ServiceResult.success();
    }


    // 도착 확인 (키오스크)
   /*본래는 1분마다 스케줄러를 실행하여 실시간으로 예약시간을 확인하여, 예약시간 10분까지 도착체크를 하지 안했다면 자동으로 "도착실패"라는 값으로 변경하게끔 하려 했으나
    대부분 가게에서 조금 늦었다고 주문을 무조건 안 받을 것 같지는 않고 도착실패로 처리되어도 결국은 예약했던 건의 주문을 받을 상황이 더 많을 것 같아서
    어차피 가게측에서 직접 처리를 하게될 상황이 많다면 굳이 실시간으로 자동체크할 필요가 없다고 느꼈기에
    이용자가 키오스크에서 체크할 때의 시간만 10분전인지 체크하게끔 하였음
   * */
    @Override
    public ServiceResult arriveCheck(long shopId, ArriveCheckInput arriveCheckInput) {

        Optional<Shop> optionalShop = shopRepository.findById(shopId);
        if(optionalShop.isEmpty()){
            return ServiceResult.fail("잘못된 접근입니다. (가게 아이디 불일치)");
        }
        Shop shop = optionalShop.get();

        // 가게아이디 + 휴대폰번호 로 찾기
        Optional<Reservation> optionalReservation
                = reservationRepository.findByShopAndPhoneNumber(shop,arriveCheckInput.getPhoneNumber());

        if(optionalReservation.isEmpty()){
            return ServiceResult.fail("예약 정보가 존재하지 않습니다.");
        }

        Reservation reservation = optionalReservation.get();
        
        LocalDateTime currentTime = LocalDateTime.now();

        // 키오스크 체크는 예약시간 10분전 까지만 가능 ( 그 외의 상황은 직원과 협의 -> 직원이 직접 체크)
        if(currentTime.isAfter(reservation.getReservationDateTime().minusMinutes(10))){
            return ServiceResult.fail("도착체크가 가능한 시간이 지났습니다. (매장직원에게 문의해주세요)");
        }

        if(currentTime.isBefore(reservation.getReservationDateTime().minusMinutes(30))){
            return ServiceResult.fail("아직 도착체크가 가능한 시간이 아닙니다. (매장직원에게 문의해주세요)");
        }

        reservation.setReservationStatus(ReservationStatus.ARRIVED);
        reservationRepository.save(reservation);

        return ServiceResult.success();
    }


    // 리뷰 남기기 + 이미 작성한 리뷰를 또 새로 작성할수는 없게 해야함 (근데 해당가게를 여러번 이용할 수 있으니, 찾을때는 예약pk, 유저 아이디, shop으로 찾기)
    @Override
    public ServiceResult reviewShop(Long reservedId, String email, ReviewInput reviewInput) {
        //이용자
        Optional<UserCustomer> optionalCustomer = userCustomerRepository.findByEmail(email);
        if(optionalCustomer.isEmpty()){
            return ServiceResult.fail("존재하지 않는 이용자입니다.");
        }
        UserCustomer userCustomer = optionalCustomer.get();

        //상점
        Optional<Shop> optionalShop = shopRepository.findById(reviewInput.getShopId());
        if(optionalShop.isEmpty()){
            return ServiceResult.fail("존재하지 않는 상점입니다.");
        }
        Shop shop = optionalShop.get();


        Optional<Reservation> optionalReservation = reservationRepository.findById(reservedId);
        if(optionalReservation.isEmpty()){
            return ServiceResult.fail("예약정보가 존재하지 않습니다.");
        }
        Reservation reservation = optionalReservation.get();


        if(reservation.getShop().getId() != reviewInput.getShopId()){
            return ServiceResult.fail("잘못된 접근입니다.(예약정보 불일치)");
        }
        if(!Objects.equals(reservation.getUserCustomer().getId(), userCustomer.getId())){
            return ServiceResult.fail("잘못된 접근입니다.(예약정보 불일치)");
        }
       if(reviewRepository.existsByUserIdAndShopAndReservedId(userCustomer.getId(), shop, reservation.getId())){
           return ServiceResult.fail("리뷰는 한건당 한번만 작성할 수 있습니다.");
       }

       
        // 가지고 온 과거 예약 데이터의 상태를 따져야함 (결제완료여부, 예약날짜의 3일안에만 작성가능)
        if(reservation.getReservationStatus() != ReservationStatus.COMPLETED_PAYMENT){
            return ServiceResult.fail("이용한 예약 건의 리뷰만 작성할 수 있습니다.");
        }
        if(LocalDateTime.now().isBefore(reservation.getReservationDateTime().plusHours(1))){
            return ServiceResult.fail("아직 리뷰를 작성할 수 없습니다.");
        }
        if(LocalDateTime.now().isAfter(reservation.getReservationDateTime().plusDays(3))){
            return ServiceResult.fail("리뷰를 작성할 수 있는 기간이 지났습니다.");
        }


        reviewRepository.save(Review.writeReview(reservedId, reviewInput, shop, userCustomer));


        //해당 가게의 리뷰수, 평균 별점 갱신
        shop.updateShopRating(shop, reviewInput.getRating());
        shopRepository.save(shop);

        return ServiceResult.success();
    }


    // 내가 작성한 리뷰목록 가져오기
    @Override
    public List<ReviewResponse> getMyReviewList(String email) {
        //이용자
        Optional<UserCustomer> optionalCustomer = userCustomerRepository.findByEmail(email);
        if(optionalCustomer.isEmpty()){
            throw new BizException("존재하지 않는 사용자입니다.");
        }

        return ReviewResponse.getReviews(reviewRepository.findByUserId(optionalCustomer.get().getId()));
    }


    // 회원탈퇴 (이용자가 남겼던 리뷰는 그대로 남게끔 하였음)
    @Override
    public ServiceResult unregisterCustomer(String email) {

        Optional<UserCustomer> optionalCustomer = userCustomerRepository.findByEmail(email);
        if(optionalCustomer.isEmpty()){
            return ServiceResult.fail("존재하지 않는 사용자입니다.");
        }
        UserCustomer userCustomer = optionalCustomer.get();

        userCustomerRepository.delete(userCustomer);
        return ServiceResult.success();
    }
}
