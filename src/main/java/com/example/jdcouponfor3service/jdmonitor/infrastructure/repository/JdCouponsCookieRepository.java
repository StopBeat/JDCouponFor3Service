package com.example.jdcouponforotherservice.jdmonitor.infrastructure.repository;

import com.example.jdcouponforotherservice.jdmonitor.infrastructure.dataobject.JdCouponsCookieSecond;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JdCouponsCookieRepository extends CrudRepository<JdCouponsCookieSecond,Long> {
    @Query(value = "select * from jd_cookie_second where phone = ?1",nativeQuery=true)
    JdCouponsCookieSecond findByPhone(String phone);


}
