package com.zerobase.shopreservation.dto.input;

import com.zerobase.shopreservation.dto.Coordinate;
import com.zerobase.shopreservation.type.OrderByColum;
import com.zerobase.shopreservation.type.ShopType;
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
  
  // 유형은 전혀 상관없이 거리만 고려하고 싶을 수 있으니 흠...
  // 입력한 위치와 가까운 가게 ?개 중에서 별점을 따지고싶을 수도 있고 애매한데
  // 혹은 거리 아예 신경안쓰고 등록된 가게 중에서 제일 인기 많은 곳 가고 싶을수도 있고


  // 일단 망고플레이트 어플을 참고해보니 가게 유형은 필수가 아니지만 정렬은 무조건 선택하게 되어있다.(직접 선택안해도 알아서 돼있음)
  // 거리순 정렬은 위치 기능이 켜져있을 때만 선택 가능


  // 가게 유형 ( 여러 종류를 동시에 찾고싶을수도 있으니까)
  private List<ShopType> shopTypes;

  // 정렬방식
  @NotNull(message = "정렬방식이 선택되어야합니다.")
  private OrderByColum orderByColum;


  //현재 자기 위치 혹은 원하는 위치(시작위치) 필드 넣어야함
  // 거리순 정렬방식을 선택했는데 이 값이 null이면  던져 예외
  private Coordinate coordinate;


  // 가져올 데이터 수 (null 이면 기본 20개만 가져오게 할까)
  private Integer limit;


  // 현재 위치와의 허용거리 (단위: Km)
  // null 이면  기본 3km 로 잡도록 하자
  private Double maxDistance;


  // 예약가능여부 (null 이면 상관없이 다 나오게하자)
  private Boolean bookable;
  
  



}
