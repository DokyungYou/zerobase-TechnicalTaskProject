package com.zerobase.shopreservation.dto.request.customer;

import com.zerobase.shopreservation.dto.Coordinate;
import com.zerobase.shopreservation.dto.type.OrderByColum;
import com.zerobase.shopreservation.dto.type.ShopType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetShopListInput {
  
 /**
  * 예전에 직접 사용해봤던 어플이 있는데
  * 가게 유형선택은 필수가 아니지만 정렬은 무조건 선택하게 되어있었다.
  * 거리순 정렬은 위치 기능이 켜져있을 때만 선택 가능
  * 했던 점을 참고
  * */


  // 가게 유형 (여러 종류를 동시에 찾고싶을 수 있으니 List타입 으로 받음)
  private List<ShopType> shopTypes;

  // 정렬방식
  @NotNull(message = "정렬방식이 선택되어야합니다.")
  private OrderByColum orderByColum;


  // 거리순 정렬방식을 선택했는데 이 값이 null 이면  예외처리
  private Coordinate coordinate;


  // 가져올 데이터 수 (null 이면 기본 20개)
  private Integer limit;


  // 현재 위치와의 허용거리 (단위: Km)
  // null 이면  기본 3km
  private Double maxDistance;


  // 예약가능여부 (null 이면 상관없이 나오게할 것)
  private Boolean bookable;
  
  



}
