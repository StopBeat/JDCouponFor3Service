package com.example.jdcouponfor3service.jdmonitor.infrastructure.repository;

import com.example.jdcouponfor3service.jdmonitor.infrastructure.dataobject.JdCouponsCookie3;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JdCouponsCookieRepository extends CrudRepository<JdCouponsCookie3,Long> {
    @Query(value = "select * from jd_cookie_second where phone = ?1",nativeQuery=true)
    JdCouponsCookie3 findByPhone(String phone);


}
