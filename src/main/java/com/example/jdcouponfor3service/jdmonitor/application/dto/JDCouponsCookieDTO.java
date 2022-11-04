package com.example.jdcouponforotherservice.jdmonitor.application.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



@Data
@ApiModel("修改京东自动领券信息参数")
public class JDCouponsCookieDTO {
    @ApiModelProperty(example = "username")
    private String username;
    @ApiModelProperty(example = "cookies")
    private String cookies;
    @ApiModelProperty(example = "wxpush")
    private String wxpush;
    @ApiModelProperty(example = "phone")
    private String phone;
    @ApiModelProperty(example = "usedate")
    private Integer usedate;
    @ApiModelProperty(example = "needsuccesspush")
    private String needsuccesspush;
}
