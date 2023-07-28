package com.zerobase.shopreservation.service;

import com.zerobase.shopreservation.common.ResponseMessage;
import com.zerobase.shopreservation.common.ServiceResult;
import com.zerobase.shopreservation.dto.input.LoginCustomerInput;
import com.zerobase.shopreservation.dto.input.ReservationShopInput;
import com.zerobase.shopreservation.dto.input.ReviewInput;
import com.zerobase.shopreservation.dto.input.SignUpCustomerInput;
import com.zerobase.shopreservation.entity.Reservation;
import com.zerobase.shopreservation.entity.Review;
import com.zerobase.shopreservation.entity.Shop;
import com.zerobase.shopreservation.entity.UserCustomer;

import com.zerobase.shopreservation.repository.ReservationRepository;
import com.zerobase.shopreservation.repository.ReviewRepository;
import com.zerobase.shopreservation.repository.ShopRepository;
import com.zerobase.shopreservation.repository.UserCustomerRepository;
import com.zerobase.shopreservation.type.ReservationStatus;
import com.zerobase.shopreservation.util.JWTUtils;
import com.zerobase.shopreservation.util.PasswordUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

            if(!shop.isReservation()){
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

        // 가지고 온 과거 예약 데이터의 상태를 따져야함 (예약약속 지켰는지의 여부, 예약날짜의 일주일 이내에만 작성가능)
        // 일단 냅두고 나중에 쓸 것


        // 리뷰를 쓸 자격이 될 때
        Review review = Review.builder()
                                .content(reviewInput.getContents())
                                .shop(shop)
                                .userCustomer(userCustomer)
                                .regDate(LocalDateTime.now())
                                .star(reviewInput.getRating())
                                .build();

        reviewRepository.save(review);


//        // 그냥 처음 상점등록할때, 0 값을 넣는걸로 바꿀까싶기도하고
//        if(shop.getReviewCount() == null){
//            shop.setReviewCount(0L);
//        }
//        if(shop.getAverageShopRating() == null){
//            shop.setAverageShopRating(0.0);
//        }


        // 상점 리뷰수, 점수 갱신
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
