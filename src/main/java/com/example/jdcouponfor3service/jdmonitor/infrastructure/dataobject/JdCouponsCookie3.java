package com.example.jdcouponfor3service.jdmonitor.infrastructure.dataobject;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class JdCouponsCookieSecond {
    @Id
    private int id;
    @TableField("username")
    private String username;
    @TableField("cookies")
    private String cookies;
    @TableField("wxpush")
    private String wxpush;
    @TableField("phone")
    private String phone;
    @TableField("usedate")
    private Integer usedate;
    @TableField("needsuccesspush")
    private String needsuccesspush;
    @TableField("onlyus")
    private String onlyus;
}
