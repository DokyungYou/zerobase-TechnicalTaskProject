package com.zerobase.shopreservation.service;

import com.zerobase.shopreservation.common.ResponseMessage;
import com.zerobase.shopreservation.common.ServiceResult;
import com.zerobase.shopreservation.common.exception.BizException;
import com.zerobase.shopreservation.dto.input.ResponseReservationInput;
import com.zerobase.shopreservation.dto.input.partner.LoginPartnerInput;
import com.zerobase.shopreservation.dto.input.partner.ShopRegisterInput;
import com.zerobase.shopreservation.dto.input.partner.SignUpPartnerInput;
import com.zerobase.shopreservation.entity.Reservation;
import com.zerobase.shopreservation.entity.Shop;
import com.zerobase.shopreservation.entity.UserPartner;
import com.zerobase.shopreservation.repository.ReservationRepository;
import com.zerobase.shopreservation.repository.ShopRepository;
import com.zerobase.shopreservation.repository.UserPartnerRepository;
//import com.zerobase.shopreservation.util.PasswordUtils;
import com.zerobase.shopreservation.type.ReservationStatus;
import com.zerobase.shopreservation.util.JWTUtils;
import com.zerobase.shopreservation.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PartnerServiceImpl implements PartnerService{

    private final UserPartnerRepository userPartnerRepository;
    private final ShopRepository shopRepository;
    private final ReservationRepository reservationRepository;

    // 파트너 회원가입
    @Override
    public ServiceResult signUp(SignUpPartnerInput signUpPartnerInput) {

        //사업자등록번호, 아이디, 이메일, 연락처 중복체크
        Optional<UserPartner> byBusinessRegistrationNumber
                = userPartnerRepository.findByBusinessRegistrationNumber(signUpPartnerInput.getBusinessRegistrationNumber());
        if(byBusinessRegistrationNumber.isPresent()){
            return ServiceResult.fail("입력하신 사업자등록번호는 이미 사용중입니다.");
        }

        Optional<UserPartner> byEmail = userPartnerRepository.findByEmail(signUpPartnerInput.getEmail());
        if(byEmail.isPresent()){
            return ServiceResult.fail("입력하신 이메일은 이미 사용중입니다.");
        }
        Optional<UserPartner> byPartnerId = userPartnerRepository.findByPartnerId(signUpPartnerInput.getPartnerId());
        if(byPartnerId.isPresent()){
            return ServiceResult.fail("입력하신 아이디는 이미 사용중입니다.");
        }


        Optional<UserPartner> byPhoneNumber = userPartnerRepository.findByPhoneNumber(signUpPartnerInput.getPhoneNumber());
        if(byPhoneNumber.isPresent()){
            return ServiceResult.fail("입력하신 휴대전화번호는 이미 사용중입니다.");
        }


        //입력한 비밀번호 암호화
        String encryptPassword = PasswordUtils.getEncryptPassword(signUpPartnerInput.getPassword());

        
        UserPartner partner = UserPartner.builder()
                    .businessRegistrationNumber(signUpPartnerInput.getBusinessRegistrationNumber())
                    .partnerName(signUpPartnerInput.getName())
                    .phoneNumber(signUpPartnerInput.getPhoneNumber())
                    .email(signUpPartnerInput.getEmail())
                    .partnerId(signUpPartnerInput.getPartnerId())
                    .password(encryptPassword)
                    .signUpDate(LocalDateTime.now())
                    .build();

        userPartnerRepository.save(partner);
        return ServiceResult.success();
    }


    //파트너 로그인 (1시간짜리 토큰 생성)
    @Override
    public ResponseMessage login(LoginPartnerInput loginPartnerInput) {
        Optional<UserPartner> byBusinessRegistrationNumber
                = userPartnerRepository.findByBusinessRegistrationNumber(loginPartnerInput.getBusinessRegistrationNumber());

        if(!byBusinessRegistrationNumber.isPresent()){
            return ResponseMessage.fail("로그인에 실패했습니다.");
        }

        UserPartner partner = byBusinessRegistrationNumber.get();
        if(!PasswordUtils.equalPassword(loginPartnerInput.getPassword(),partner.getPassword())){
            return ResponseMessage.fail("로그인에 실패했습니다.");
        }

        //로그인 성공 (토큰 부여) 메소드 수정필요
        String partnerToken = JWTUtils.createToken(partner);


        return ResponseMessage.success(partnerToken);
    }


    // 상점 신규 등록
    @Override
    public ServiceResult registerShop(ShopRegisterInput shopRegisterInput, String email) {

        System.out.println(email);

        //같은데 왜 자꾸 안되냐
        Optional<UserPartner> optionalUserPartner
                = userPartnerRepository.findByEmail(email);
        if(!optionalUserPartner.isPresent()){
            return ServiceResult.fail("해당 이메일없.");
        }

        UserPartner partner = optionalUserPartner.get();

        //이거 Shop 클래스의 메소드로 따로 뺼까...
        Shop shop = Shop.builder()
                        .takeOut(shopRegisterInput.isTakeOut())
                        .wifi(shopRegisterInput.isWifi())
                        .parking(shopRegisterInput.isParking())
                        .facilitiesForDisabled(shopRegisterInput.isFacilitiesForDisabled())
                        .seats(shopRegisterInput.getSeats())
                        .reservation(shopRegisterInput.isReservation())
                        .reservationType(shopRegisterInput.getReservationType())
                        .operatingHours(shopRegisterInput.getOperatingHours())
                        .dayOff(shopRegisterInput.getDayOff())
                        .shopAddress(shopRegisterInput.getShopAddress())
                        .longitude(shopRegisterInput.getLongitude())
                        .latitude(shopRegisterInput.getLatitude())
                        .contactNumber(shopRegisterInput.getContactNumber())
                        .shopIntroduction(shopRegisterInput.getShopIntroduction())
                        .shopType(shopRegisterInput.getShopType())
                        .shopName(shopRegisterInput.getShopName())

                        .reviewCount(0L)
                        .averageShopRating(0.0)


                        .businessRegistrationNumber(partner.getBusinessRegistrationNumber())
                        .regDate(LocalDateTime.now())
                        .userPartner(partner)
                        .build();

        shopRepository.save(shop);
        return ServiceResult.success();
    }



    // 전체 예약목록 확인(날짜순 정렬)
    @Override
    public List<Reservation> getReservation(String email, Long shopId) {
       //일단 이메일로 들여왔는데, 나중에 다른걸로 바꿀것
        
        // shopId로 shop 불러오고 그 shop의 파트너의 email(혹은 사업자등록번호 등의 데이터)의 데이터 일치하는지 확인
        // 일치하면 그 shop 객체가 조인돼있는 예약정보 불러오기
        Optional<Shop> optionalShop = shopRepository.findById(shopId);
        if(optionalShop.isEmpty()){
            //여기서는 예외를 던져야함, 리턴타입이 List 라서
            throw new BizException("존재하지 않는 상점입니다.");
        }

        Shop shop = optionalShop.get();
       if(!shop.getUserPartner().getEmail().equals(email)){
           throw new BizException("니 가게 아님.");
       }

       return reservationRepository.findByShopOrderByReservationDateTime(shop);
    }


    //예약 승인 및 거절
    @Override
    public ServiceResult responseReservation(Long shopId,String email, ResponseReservationInput responseReservationInput) {
        Optional<Shop> optionalShop = shopRepository.findById(shopId);
        if(optionalShop.isEmpty()){
            return ServiceResult.fail("존재하지 않는 가게입니다.");
        }

        Shop shop = optionalShop.get();
        if(!shop.getUserPartner().getEmail().equals(email)){
            return ServiceResult.fail("니 가게 아님.");
        }

        //상점 , 예약 아이디, 예약 상태
        Optional<Reservation> optionalReservation = reservationRepository.findByIdAndShop(responseReservationInput.getReservationId(), shop);
        if(optionalReservation.isEmpty()){
            return ServiceResult.fail("존재하지 않는 예약입니다.");
        }

        if(responseReservationInput.getReservationStatus() == null){
            responseReservationInput.setReservationStatus(ReservationStatus.WAITING);
        }


        Reservation reservation = optionalReservation.get();
        reservation.setReservationStatus(responseReservationInput.getReservationStatus());


        reservationRepository.save(reservation);
        return ServiceResult.success();
    }
}
