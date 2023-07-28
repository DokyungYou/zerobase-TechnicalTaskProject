package com.zerobase.shopreservation.dto.input;

import com.zerobase.shopreservation.entity.Shop;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationShopInput {



    @NotNull(message = "상점 아이디는 필수 입력값입니다.")
    private Long shopId;

    // 예약인원, 특이사항 등의 내용
    @NotBlank(message = "예약내용은 필수 입력값입니다.")
    private String reservationContents;



    @NotBlank(message = "예약날짜는 필수 입력값입니다.")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$", message = "올바른 형식의 날짜와 시간을 입력해주세요. (yyyy-MM-dd HH:mm:ss)")
//    @Future(message = "예약할 수 없는 날짜입니다."
    private String reservationDateTime;


//    private DateTime dateTime;





//    이용자 아이디, 휴대전화번호 등의 정보는 토큰으로 해결되는 부분인가?
    // 근데 보통 키오스크에서 할때 아이디, 비밀번호 로그인까지 할 필요가 있을까? 간단하게 휴대전화번호만 입력해도 할 수 있게끔 해도 되지않나?
    
//    @NotBlank(message = "휴대전화번호는 필수 입력값입니다.")
//    private String phoneNumber;

}
