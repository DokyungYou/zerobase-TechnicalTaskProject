package com.zerobase.shopreservation.repository;

import com.zerobase.shopreservation.dto.Coordinate;
import com.zerobase.shopreservation.dto.GetShopList;
import com.zerobase.shopreservation.dto.input.GetShopListInput;
import com.zerobase.shopreservation.entity.Shop;
import com.zerobase.shopreservation.type.ShopType;
import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ShopCustomRepository {

    private final EntityManager entityManager;
    private final ShopRepository shopRepository;

    // 필요한 컬럼)
    //pk, 가게이름, 평균별점,총 리뷰수, 가게유형, 예약가능여부
    public List<GetShopList> getShopList(GetShopListInput getShopListInput) {

        String bookable = "";
        if (getShopListInput.getBookable() == null) {
            bookable = "true or false";
        } else {
            bookable = getShopListInput.getBookable() + "";
        }
        String distanceColum = getDistanceColum(getShopListInput);


        String sql = "select s.id ,s.shop_name ,s.shop_type ,s.average_shop_rating ,s.review_count ,s.bookable, "
                + distanceColum +
                " from shop s " +
                " where s.shop_type " + whereShopType(getShopListInput) +
                " and s.bookable =" + bookable +
                " HAVING distance < " + getShopListInput.getMaxDistance() +
                " order by " + getShopListInput.getOrderByColum()+
                " limit "+ getShopListInput.getLimit();

        Query nativeQuery = entityManager.createNativeQuery(sql);
        JpaResultMapper jpaResultMapper = new JpaResultMapper();


        System.out.println(sql); // 임시 확인용
        return jpaResultMapper.list(nativeQuery, GetShopList.class);
    }



    // 가게유형 Input값에 따른 조건문
    String whereShopType(GetShopListInput getShopListInput) {
        List<ShopType> shopTypes = getShopListInput.getShopTypes();
        StringBuilder sb = new StringBuilder();

        if (shopTypes == null || shopTypes.size() == 0) {
                sb.append("like ");
                sb.append("'%'");
                return sb.toString();
            }

        if (shopTypes.size() == 1) {
            sb.append("like ");
            sb.append("'%");
            sb.append(shopTypes.get(0).getTypeName());
            sb.append("%'");
            return sb.toString();
        }


            sb.append("REGEXP '");
            for (int i = 0; i <= shopTypes.size() - 1; i++) {
                if (i == shopTypes.size() - 1) {
                    sb.append(shopTypes.get(i).getTypeName());
                    sb.append("'");
                } else {
                    sb.append(shopTypes.get(i).getTypeName());
                    sb.append("|");
                }
            }
            return sb.toString();
        }
        

    String getDistanceColum(GetShopListInput getShopListInput){
        if(getShopListInput.getCoordinate() == null){
            return "(0) AS distance";
        }

       double myLongitude = getShopListInput.getCoordinate().getLongitude();
       double myLatitude = getShopListInput.getCoordinate().getLatitude();

        return
                "( 6371 * acos ( cos ( radians("+myLongitude+") ) " +
                " * cos( radians( s.latitude ) ) " +
                " * cos( radians( s.longitude ) - radians("+myLatitude+") ) " +
                " + sin ( radians("+myLongitude+") ) * sin( radians( s.latitude ) ) " +
                " )" +
                ") AS distance "
                ;
    }
        
        


    }
