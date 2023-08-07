package com.zerobase.shopreservation.service;

import com.zerobase.shopreservation.common.ResponseMessage;
import com.zerobase.shopreservation.common.ServiceResult;
import com.zerobase.shopreservation.common.exception.BizException;
import com.zerobase.shopreservation.dto.request.partner.UpdateReservationDateTimeInput;
import com.zerobase.shopreservation.dto.response.ReservationResponse;
import com.zerobase.shopreservation.dto.request.partner.ResponseReservationInput;
import com.zerobase.shopreservation.dto.request.LoginInput;
import com.zerobase.shopreservation.dto.request.partner.ShopInput;
import com.zerobase.shopreservation.dto.request.partner.SignUpPartnerInput;
import com.zerobase.shopreservation.entity.Reservation;
import com.zerobase.shopreservation.entity.Shop;
import com.zerobase.shopreservation.entity.UserPartner;
import com.zerobase.shopreservation.repository.ReservationRepository;
import com.zerobase.shopreservation.repository.ShopRepository;
import com.zerobase.shopreservation.repository.UserPartnerRepository;

import com.zerobase.shopreservation.dto.type.ReservationStatus;
import com.zerobase.shopreservation.util.JwtUtils;
import com.zerobase.shopreservation.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
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
    public ResponseMessage login(LoginInput loginInput) {

        Optional<UserPartner> byBusinessRegistrationNumber
                = userPartnerRepository.findByUserId(loginInput.getId());

        if(byBusinessRegistrationNumber.isEmpty()){
            return ResponseMessage.fail("로그인에 실패했습니다.");
        }

        UserPartner partner = byBusinessRegistrationNumber.get();
        if(!PasswordUtils.equalPassword(loginInput.getPassword(),partner.getPassword())){
            return ResponseMessage.fail("로그인에 실패했습니다.");
        }


        String partnerToken = JwtUtils.createToken(partner);
        return ResponseMessage.success(partnerToken);
    }


    // 상점 신규 등록
    @Override
    public ServiceResult registerShop(ShopInput shopInput, String businessRegistrationNumber) {

        Optional<UserPartner> optionalUserPartner
                = userPartnerRepository.findByBusinessRegistrationNumber(businessRegistrationNumber);
        if(optionalUserPartner.isEmpty()){
            return ServiceResult.fail("존재하지 않는 파트너입니다.");
        }

        UserPartner partner = optionalUserPartner.get();

        shopRepository.save(Shop.registerShop(shopInput,partner));
        return ServiceResult.success();
    }



    @Override
    public ServiceResult updateShop(Long shopId, String businessRegistrationNumber, ShopInput updateShopInput) {

        Optional<UserPartner> optionalUserPartner
                = userPartnerRepository.findByBusinessRegistrationNumber(businessRegistrationNumber);
        if(optionalUserPartner.isEmpty()){
            return ServiceResult.fail("존재하지 않는 파트너입니다.");
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
    public List<ReservationResponse> getReservation(String businessRegistrationNumber, Long shopId) {


        Optional<Shop> optionalShop = shopRepository.findById(shopId);
        if(optionalShop.isEmpty()){
            throw new BizException("존재하지 않는 상점입니다.");
        }

        Shop shop = optionalShop.get();
       if(!shop.getUserPartner().getBusinessRegistrationNumber().equals(businessRegistrationNumber)){
           throw new BizException("파트너의 상점이 아닙니다.");
       }

        return ReservationResponse.getReservationList(reservationRepository.findByShopOrderByReservationDateTime(shop));
    }


    // 예약 상태 변경 (거절, 승인, 취소, 도착,결제완료)
    // 이미 결제완료 상태로 넣었으면 더이상 변경 불가능
    @Override
    public ServiceResult updateReservationStatus(Long shopId,String businessRegistrationNumber, ResponseReservationInput responseReservationInput) {
        Optional<Shop> optionalShop = shopRepository.findById(shopId);
        if(optionalShop.isEmpty()){
            return ServiceResult.fail("존재하지 않는 가게입니다.");
        }

        Shop shop = optionalShop.get();
        if(!shop.getUserPartner().getBusinessRegistrationNumber().equals(businessRegistrationNumber)){
            return ServiceResult.fail("해당 가게의 파트너가 아닙니다.");
        }

        // 지정한 가게의 특정 예약데이터 가져오기
        Optional<Reservation> optionalReservation = reservationRepository.findByIdAndShop(responseReservationInput.getReservationId(), shop);
        if(optionalReservation.isEmpty()){
            return ServiceResult.fail("존재하지 않는 예약입니다.");
        }
        Reservation reservation = optionalReservation.get();

        if(reservation.getReservationStatus() == ReservationStatus.COMPLETED_PAYMENT){
            return ServiceResult.fail("이미 결제완료한 예약 건은 상태변경이 불가합니다.");
        }


        reservation.setReservationStatus(responseReservationInput.getReservationStatus());
        reservation.setStatusUpdate(LocalDateTime.now());

        reservationRepository.save(reservation);
        return ServiceResult.success();
    }


    // 예약날짜 변경
    @Override
    public ServiceResult updateReservationDateTime(Long shopId, String businessRegistrationNumber, UpdateReservationDateTimeInput updateReservationDateTimeInput) {
        Optional<Shop> optionalShop = shopRepository.findById(shopId);
        if(optionalShop.isEmpty()){
            return ServiceResult.fail("존재하지 않는 가게입니다.");
        }

        Shop shop = optionalShop.get();
        if(!shop.getUserPartner().getBusinessRegistrationNumber().equals(businessRegistrationNumber)){
            return ServiceResult.fail("해당 가게의 파트너가 아닙니다.");
        }

        // 지정한 가게의 특정 예약데이터 가져오기
        Optional<Reservation> optionalReservation = reservationRepository.findByIdAndShop(updateReservationDateTimeInput.getReservationId(), shop);
        if(optionalReservation.isEmpty()){
            return ServiceResult.fail("존재하지 않는 예약입니다.");
        }
        Reservation reservation = optionalReservation.get();


        // 가게에서 직접 예약날짜, 시간 변경시엔 당일도 가능 (과거만 불가능)
        LocalDateTime updateDateTime = Reservation.FromDateString(updateReservationDateTimeInput.getReservationDateTime());
        if(updateDateTime.isBefore(LocalDateTime.now())){
            return ServiceResult.fail("예약이 가능한 날짜가 아닙니다. (과거의 날짜 및 시간)");
        }
        
        reservation.setReservationDateTime(updateDateTime);
        reservationRepository.save(reservation);
        return ServiceResult.success();
    }


    @Override
    public ServiceResult deleteShop(Long shopId, String businessRegistrationNumber) {

        Optional<Shop> optionalShop = shopRepository.findById(shopId);
        if(optionalShop.isEmpty()){
            throw new BizException("존재하지 않는 상점입니다.");
        }

        Shop shop = optionalShop.get();
        if(!shop.getUserPartner().getBusinessRegistrationNumber().equals(businessRegistrationNumber)){
            throw new BizException("파트너의 상점이 아닙니다.");
        }


        shopRepository.delete(shop);
        return ServiceResult.success();
    }


    @Override
    public ServiceResult deletePartner(String businessRegistrationNumber){
        Optional<UserPartner> optionalUserPartner
                = userPartnerRepository.findByBusinessRegistrationNumber(businessRegistrationNumber);
        if(optionalUserPartner.isEmpty()){
            return ServiceResult.fail("존재하지 않는 파트너입니다.");
        }

        UserPartner partner = optionalUserPartner.get();


        try{userPartnerRepository.delete(partner);
        }catch (Exception e){ //본래는 SQLIntegrityConstraintViolationException 이 발생하나 예외처리가 안돼서 일단 상위클래스인 Exception 로 처리하였음
            return  ServiceResult.fail("회원탈퇴 실패 (해당 파트너계정으로 등록된 상점이 존재합니다.)");
        }
        return ServiceResult.success();
    }
}
