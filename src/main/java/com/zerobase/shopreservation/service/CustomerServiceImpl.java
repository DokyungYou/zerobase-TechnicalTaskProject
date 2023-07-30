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
import com.zerobase.shopreservation.type.OrderByColum;
import com.zerobase.shopreservation.type.ReservationStatus;
import com.zerobase.shopreservation.util.JWTUtils;
import com.zerobase.shopreservation.util.PasswordUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.LocalTime;
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
        Optional<UserCustomer> byEmail = userCustomerRepository.findByEmail(signUpCustomerInput.getEmail());
        if(byEmail.isPresent()){
            return ServiceResult.fail("입력하신 이메일은 이미 사용중입니다.");
        }
        Optional<UserCustomer> byPartnerId = userCustomerRepository.findByCustomerId(signUpCustomerInput.getCustomerId());
        if(byPartnerId.isPresent()){
            return ServiceResult.fail("입력하신 아이디는 이미 사용중입니다.");
        }


        Optional<UserCustomer> byPhoneNumber = userCustomerRepository.findByPhoneNumber(signUpCustomerInput.getPhoneNumber());
        if(byPhoneNumber.isPresent()){
            return ServiceResult.fail("입력하신 휴대전화번호는 이미 사용중입니다.");
        }

        Optional<UserCustomer> byNickName = userCustomerRepository.findByNickName(signUpCustomerInput.getNickname());
        if(byNickName.isPresent()){
            return ServiceResult.fail("입력하신 닉네임은 이미 사용중입니다.");
        }


        //입력한 비밀번호 암호화
        String encryptPassword = PasswordUtils.getEncryptPassword(signUpCustomerInput.getPassword());



        UserCustomer customer = UserCustomer.builder()
                            .customerName(signUpCustomerInput.getName())
                            .phoneNumber(signUpCustomerInput.getPhoneNumber())
                            .email(signUpCustomerInput.getEmail())
                            .customerId(signUpCustomerInput.getCustomerId())
                            .password(encryptPassword)
                            .nickName(signUpCustomerInput.getNickname())
                            .signUpDate(LocalDateTime.now())
                            .build();

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



    // 갑자기 생각하면 할수록 복잡해는데..?
    // 상점목록 가져오기
    // 단순히 상점들 조회하는건데도 로그인(토큰)이 꼭 필요하게 해야할까? 좀 더 고민해보자
    @Override
    public List<GetShopList> getShopList(GetShopListInput getShopListInput) {

        // 위치정보가 들어오지 않았을경우
        if(getShopListInput.getCoordinate() == null){
            if(getShopListInput.getOrderByColum().equals(OrderByColum.DISTANCE)) {
                throw new BizException("거리순 정렬은 위치정보가 입력되어야 적용할 수 있습니다.");
            }
            if(getShopListInput.getMaxDistance()!= null){
                throw new BizException("검색반경은 위치정보가 입력되어야 적용할 수 있습니다.");
            }
        }

        // 위치 정보는 들어왔으나 검색반경을 지정해주지 않을때는 기본값으로 초기화
        if(getShopListInput.getMaxDistance() == null){
            getShopListInput.setMaxDistance(3.0);
        }

        if(getShopListInput.getLimit() == null){
            getShopListInput.setLimit(20);
        }

        return shopCustomRepository.getShopList(getShopListInput);
    }


    // 핸들러 여기에 넣어도 안 돌아가네?
    @ExceptionHandler(BizException.class)
    public ResponseEntity<?> handlerBizException(BizException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
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


            Reservation reservation = Reservation.builder()
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

        // 키오스크 체크는 예약시간 10분전 까지만 가능 ( 그 후에는 가게주인과 협의 -> 가게주인이 직접 체크)
        if(currentTime.isAfter(reservation.getReservationDateTime().minusMinutes(10))){
            return ServiceResult.fail("일찍 좀 다녀라");
        };

        if(currentTime.isBefore(reservation.getReservationDateTime().minusMinutes(30))){
            return ServiceResult.fail("너무 일찍왔다 ");
        }

        reservation.setArrivedReservationTime(true);
        reservationRepository.save(reservation);
        // 리뷰를 쓸 수 있는 권한(3일짜리 토큰 주기)
        // 도착했다고 바로 리뷰 쓸 수 있게 하지말고
        // 예약 시간보다 한시간 후에 적을 수 있게 만들자.
        // 아니다 토큰도 필요 없을듯, true이면서 예약시간 1~2시간 뒤인 시간이면 작성 가능하게 만들기.
        return ServiceResult.success();
    }

    // 리뷰 작성
    // 근데 메소드 하나에 두가지 이상의 역할을 넣으면 안되지않나? 근데 연결되는 역할이라 같이 넣었는데
    // (예약약속 지켰는지의 여부, 예약날짜의 일주일 이내에만 작성가능 /shop 의 리뷰,점수 갱신 /  /
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
            return ServiceResult.fail("해당 예약내역이 존재하지 않습니다.");
        }
        Reservation reservation = optionalReservation.get();

        // 예약 아이디로 찾은  예약 내역자체는 존재하는데, 자신의 예약내역이 아니거나 가게가 일치하지 않을때
        // wrapper 타입 아닌 기본타입으로 바꿨는데도 != 에 주의가 뜨네
        if(reservation.getShop().getId() != reviewInput.getShopId()){
            return ServiceResult.fail("뭐라고 쓰지");
        }
        if(reservation.getUserCustomer().getId() != userCustomer.getId() ){
            return ServiceResult.fail("뭐라고 쓰지");
        }

        // 가지고 온 과거 예약 데이터의 상태를 따져야함 (예약약속 지켰는지의 여부, 예약날짜의 3일안에만 작성가능)
        if(!reservation.isArrivedReservationTime()){
            return ServiceResult.fail("리뷰를 작성할 수 없습니다.");
        }

        if(LocalDateTime.now().isAfter(reservation.getReservationDateTime().plusDays(3))){
            return ServiceResult.fail("리뷰를 작성할 수 있는 기간이 지났습니다.");
        }


        // 근데 이 조건의 로직이 먼저 돌아가고 그 결과에 따라 별점, 텍스트를 넣을 수 있게 해야할것 같은데...
        // input 으로 처음부터 텍스트, 별점을 넣게 하지 말고 ( 고민 좀 해봅시다..)

        // 리뷰를 쓸 자격이 될 때
        Review review = Review.builder()
                                .content(reviewInput.getContents())
                                .shop(shop)
                                .userCustomer(userCustomer)
                                .regDate(LocalDateTime.now())
                                .star(reviewInput.getRating())
                                .build();

        reviewRepository.save(review);


        // 상점 리뷰수, 점수 갱신 (메소드를 따로 구현할까 생각중)
        long beforeReviewCount = shop.getReviewCount();
        long newReviewCount = beforeReviewCount + 1;

        double beforeRating = shop.getAverageShopRating();
        double newRating = ((beforeRating * beforeReviewCount) + reviewInput.getRating().getValue()) / newReviewCount;

        shop.setReviewCount(newReviewCount);
        shop.setAverageShopRating(newRating);

        shopRepository.save(shop); //리뷰수, 평균 별점 갱신

        return ServiceResult.success();
    }

}
