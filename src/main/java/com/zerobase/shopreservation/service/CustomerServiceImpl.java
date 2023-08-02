package com.zerobase.shopreservation.service;

import com.zerobase.shopreservation.common.ResponseMessage;
import com.zerobase.shopreservation.common.ServiceResult;
import com.zerobase.shopreservation.common.exception.BizException;
import com.zerobase.shopreservation.dto.GetShopList;
import com.zerobase.shopreservation.dto.input.*;
import com.zerobase.shopreservation.entity.Reservation;
import com.zerobase.shopreservation.entity.Review;
import com.zerobase.shopreservation.entity.Shop;
import com.zerobase.shopreservation.entity.UserCustomer;

import com.zerobase.shopreservation.repository.*;
import com.zerobase.shopreservation.dto.type.OrderByColum;
import com.zerobase.shopreservation.dto.type.ReservationStatus;
import com.zerobase.shopreservation.util.JWTUtils;
import com.zerobase.shopreservation.util.PasswordUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{

    private final UserCustomerRepository userCustomerRepository;
    private final ShopRepository shopRepository;
    private final ShopCustomRepository shopCustomRepository;

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
    public ResponseMessage login(LoginCustomerInput loginCustomerInput) {
        Optional<UserCustomer> optionalCustomer = userCustomerRepository.findByCustomerId(loginCustomerInput.getCustomerId());
        if(!optionalCustomer.isPresent()){
            return ResponseMessage.fail("로그인에 실패했습니다!");
        }
        UserCustomer userCustomer = optionalCustomer.get();

        if(!PasswordUtils.equalPassword(loginCustomerInput.getPassword(),userCustomer.getPassword())){
            return ResponseMessage.fail("로그인에 실패했습니다!");
        }

        String customerToken = JWTUtils.createCustomerToken(userCustomer);
        return  ResponseMessage.success(customerToken);
    }




    // 상점목록 가져오기
    @Override
    public List<GetShopList> getShopList(GetShopListInput getShopListInput) {

        // 위치정보가 들어오지 않았을경우
        if(getShopListInput.getCoordinate() == null){
            if(getShopListInput.getOrderByColum().equals(OrderByColum.DISTANCE)) {
                throw new BizException("거리순 정렬은 위치정보가 입력되어야 적용할 수 있습니다.");
            }
            if(getShopListInput.getMaxDistance()!= null){
                throw new BizException("거리정보는 위치정보가 입력되어야 적용할 수 있습니다.");
            }
        }

        // 위치 정보는 들어왔으나 검색반경을 지정해주지 않을때는 기본값(3km)으로 초기화
        if(getShopListInput.getMaxDistance() == null){
            getShopListInput.setMaxDistance(3.0);
        }

        if(getShopListInput.getLimit() == null){
            getShopListInput.setLimit(20);
        }

        return shopCustomRepository.getShopList(getShopListInput);
    }


    // 특정 상점 상세정보 가져오기
    @Override
    public Shop getShopDetail(Long shopId) {
        Optional<Shop> optionalShop = shopRepository.findById(shopId);
        if(optionalShop.isEmpty()){
            throw new BizException("가게 정보가 존재하지 않습니다.");
        }

        return optionalShop.get();
    }


    // 특정 상점의 리뷰 가져오기
    @Override
    public List<Review> getShopReviews(Long shopId) {
        Optional<Shop> optionalShop = shopRepository.findById(shopId);
        if(optionalShop.isEmpty()){
            throw new BizException("가게 정보가 존재하지 않습니다.");
        }
        Shop shop = optionalShop.get();
        return reviewRepository.findByShop(shop);
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

            // 예외처리 할 거 많을 것 같은데
            String dateString = reservationShopInput.getReservationDateTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime localDateTime = LocalDateTime.parse(dateString,formatter);


            Reservation reservation
                    = Reservation.builder()
                                .shop(shop)
                                .userCustomer(userCustomer)
                                .reservationContents(reservationShopInput.getReservationContents())
                                .reservationDateTime(localDateTime)
                                .phoneNumber(userCustomer.getPhoneNumber())
                                .regDate(LocalDateTime.now())
                                .reservationStatus(ReservationStatus.WAITING)
                                .build();


            reservationRepository.save(reservation);
            return ServiceResult.success();
            
    }
    
    //내가 예약한 목록 가져오기
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


    // 도착 확인 (키오스크)
    @Override
    public ServiceResult arriveCheck(long shopId,ArriveCheckInput arriveCheckInput) {

        Optional<Reservation> byPhoneNumber
                = reservationRepository.findByPhoneNumber(arriveCheckInput.getPhoneNumber());

        if(byPhoneNumber.isEmpty()){
            return ServiceResult.fail("예약 정보가 존재하지 않습니다.");
        }

        Reservation reservation = byPhoneNumber.get();

        if(!PasswordUtils.equalPassword(arriveCheckInput.getPassword(),reservation.getUserCustomer().getPassword())){
            return ServiceResult.fail("비밀번호 불일치.");
        }

        LocalDateTime currentTime = LocalDateTime.now();

        // 키오스크 체크는 예약시간 10분전 까지만 가능 ( 그 외의 상황은 가게주인과 협의 -> 가게주인이 직접 체크)
        if(currentTime.isAfter(reservation.getReservationDateTime().minusMinutes(10))){
            return ServiceResult.fail("도착체크 시간이 지났습니다. (매장직원에게 문의해주세요)");
        }

        if(currentTime.isBefore(reservation.getReservationDateTime().minusMinutes(30))){
            return ServiceResult.fail("아직 도착체크 가능한 시간이 아닙니다. (매장직원에게 문의해주세요)");
        }

        reservation.setReservationStatus(ReservationStatus.CARRIED_OUT);
        reservationRepository.save(reservation);

        return ServiceResult.success();
    }


    // 리뷰 남기기
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

        // 예약 아이디로 찾은  예약 자체는 존재하는데, 자신의 예약내역이 아니거나 가게가 일치하지 않을때
        // wrapper 타입 아닌 기본타입으로 바꿨는데도 != 에 주의가 뜨네
        if(reservation.getShop().getId() != reviewInput.getShopId()){
            return ServiceResult.fail("뭐라고 쓰지");
        }
        if(reservation.getUserCustomer().getId() != userCustomer.getId() ){
            return ServiceResult.fail("뭐라고 쓰지");
        }

        // 가지고 온 과거 예약 데이터의 상태를 따져야함 (예약약속 지켰는지의 여부, 예약날짜의 3일안에만 작성가능)
        // 근데 enum타입 을 스택메모리 비교하는 연산자로 써도 되나? (테스트 필요)
        if(reservation.getReservationStatus() != ReservationStatus.CARRIED_OUT){
            return ServiceResult.fail("이용한 예약 건의 리뷰만 작성할 수 있습니다.");
        }

        if(LocalDateTime.now().isBefore(reservation.getReservationDateTime().plusHours(1))){
            return ServiceResult.fail("아직 리뷰를 작성할 수 없습니다.");
        }

        if(LocalDateTime.now().isAfter(reservation.getReservationDateTime().plusDays(3))){
            return ServiceResult.fail("리뷰를 작성할 수 있는 기간이 지났습니다.");
        }


        // 근데 이 조건의 로직이 먼저 돌아가고 그 결과에 따라 별점, 텍스트를 넣을 수 있게 해야할것 같은데...
        // input 으로 처음부터 텍스트, 별점을 넣게 하지 말고 ( 고민 좀 해봅시다..)

        // 리뷰를 쓸 자격이 될 때
        Review review = Review.builder()
                                .content(reviewInput.getContents())
                                // shop 아이디만 넣어야할지 고민
                                .shop(shop)

                                // 아이디(Customer pk나 이메일  or 아이디)만 넣을지 UserCustomer 로 넣을지 고민
                                .userCustomer(userCustomer)
                                .regDate(LocalDateTime.now())
                                .star(reviewInput.getRating())
                                .build();

        reviewRepository.save(review);


        shop.updateShopRating(shop, reviewInput.getRating());
        shopRepository.save(shop); //리뷰수, 평균 별점 갱신

        return ServiceResult.success();
    }


    // 내가 작성한 리뷰목록 가져오기
    @Override
    public List<Review> getMyReviewList(String email) {
        //이용자
        Optional<UserCustomer> optionalCustomer = userCustomerRepository.findByEmail(email);
        if(optionalCustomer.isEmpty()){
            throw new BizException("존재하지 않는 사용자입니다.");
        }
        
        // 리뷰 컬럼 수정할수도 있음
        UserCustomer userCustomer = optionalCustomer.get();
        return reviewRepository.findByUserCustomer(userCustomer);
    }
}
