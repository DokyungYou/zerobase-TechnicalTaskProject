package com.zerobase.shopreservation.service;

import com.zerobase.shopreservation.common.ResponseMessage;
import com.zerobase.shopreservation.common.ServiceResult;
import com.zerobase.shopreservation.common.exception.BizException;
import com.zerobase.shopreservation.dto.input.partner.ResponseReservationInput;
import com.zerobase.shopreservation.dto.input.partner.LoginPartnerInput;
import com.zerobase.shopreservation.dto.input.partner.ShopInput;
import com.zerobase.shopreservation.dto.input.partner.SignUpPartnerInput;
import com.zerobase.shopreservation.entity.Reservation;
import com.zerobase.shopreservation.entity.Shop;
import com.zerobase.shopreservation.entity.UserPartner;
import com.zerobase.shopreservation.repository.ReservationRepository;
import com.zerobase.shopreservation.repository.ShopRepository;
import com.zerobase.shopreservation.repository.UserPartnerRepository;

import com.zerobase.shopreservation.dto.type.ReservationStatus;
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
        if(userPartnerRepository.existsByBusinessRegistrationNumber(signUpPartnerInput.getBusinessRegistrationNumber())){
            return ServiceResult.fail("입력하신 사업자등록번호는 이미 사용중입니다.");
        }
        if(userPartnerRepository.existsByEmail(signUpPartnerInput.getEmail())){
            return ServiceResult.fail("입력하신 이메일은 이미 사용중입니다.");
        }
        if(userPartnerRepository.existsByUserId(signUpPartnerInput.getPartnerId())){
            return ServiceResult.fail("입력하신 아이디는 이미 사용중입니다.");
        }
        if(userPartnerRepository.existsByPhoneNumber(signUpPartnerInput.getPhoneNumber())){
            return ServiceResult.fail("입력하신 휴대전화번호는 이미 사용중입니다.");
        }


        UserPartner partner = UserPartner.createUserPartner(signUpPartnerInput);
        userPartnerRepository.save(partner);
        return ServiceResult.success();
    }


    //파트너 로그인 (1시간짜리 토큰 생성)
    @Override
    public ResponseMessage login(LoginPartnerInput loginPartnerInput) {

        Optional<UserPartner> byBusinessRegistrationNumber
                = userPartnerRepository.findByPartnerId(loginPartnerInput.getPartnerId());

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
    public ServiceResult registerShop(ShopInput shopInput, String email) {

        Optional<UserPartner> optionalUserPartner
                = userPartnerRepository.findByEmail(email);
        if(!optionalUserPartner.isPresent()){
            return ServiceResult.fail("해당 이메일없.");
        }

        UserPartner partner = optionalUserPartner.get();

        shopRepository.save(Shop.registerShop(shopInput,partner));
        return ServiceResult.success();
    }




    // 엔티티로 받아야하나 싶기도하고
    @Override
    public ServiceResult updateShop(Long shopId, String email, ShopInput updateShopInput) {

        Optional<UserPartner> optionalUserPartner
                = userPartnerRepository.findByEmail(email);
        if(!optionalUserPartner.isPresent()){
            return ServiceResult.fail("해당 이메일없.");
        }

        UserPartner partner = optionalUserPartner.get();

        Optional<Shop> optionalShop = shopRepository.findByUserPartnerAndId(partner, shopId);
        if(optionalShop.isEmpty()){
            return ServiceResult.fail("가게 정보가 존재하지 않습니다.");
        }

        Shop shop = optionalShop.get();
        Shop.updateShop(updateShopInput, shop);
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
            throw new BizException("존재하지 않는 상점입니다.");
        }

        Shop shop = optionalShop.get();
       if(!shop.getUserPartner().getEmail().equals(email)){
           throw new BizException("파트너의 상점이 아닙니다.");
       }

       return reservationRepository.findByShopOrderByReservationDateTime(shop);
    }


    //예약 상태 변경 (거절, 승인, 취소, 도착)
    @Override
    public ServiceResult responseReservation(Long shopId,String email, ResponseReservationInput responseReservationInput) {
        Optional<Shop> optionalShop = shopRepository.findById(shopId);
        if(optionalShop.isEmpty()){
            return ServiceResult.fail("존재하지 않는 가게입니다.");
        }

        Shop shop = optionalShop.get();
        if(!shop.getUserPartner().getEmail().equals(email)){
            return ServiceResult.fail("해당 가게의 파트너가 아닙니다.");
        }

        // 지정한 가게의 특정 예약데이터 가져오기
        Optional<Reservation> optionalReservation = reservationRepository.findByIdAndShop(responseReservationInput.getReservationId(), shop);
        if(optionalReservation.isEmpty()){
            return ServiceResult.fail("존재하지 않는 예약입니다.");
        }


        // 인풋에 이 데이터 notnull 붙여놨긴했는데, 되는지 안되는지 몰라서 일단 넣어놓은 되면 이거는 삭제해도 됨
        if(responseReservationInput.getReservationStatus() == null){
            responseReservationInput.setReservationStatus(ReservationStatus.WAITING);
        }


        Reservation reservation = optionalReservation.get();
        reservation.setReservationStatus(responseReservationInput.getReservationStatus());

        reservationRepository.save(reservation);
        return ServiceResult.success();
    }
}
