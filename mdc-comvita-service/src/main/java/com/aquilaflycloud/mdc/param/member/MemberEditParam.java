package com.aquilaflycloud.mdc.param.member;

import com.aquilaflycloud.common.NoneBlank;
import com.aquilaflycloud.mdc.enums.member.SexEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@NoneBlank(fieldNames = {"idCard", "realName", "nickName", "sex", "country", "province", "city", "county", "address", "memberHeadUrl", "birthday"}, message = "更新参数不能全都为空")
@Data
@Accessors(chain = true)
public class MemberEditParam implements Serializable {
    @ApiModelProperty(value = "身份证号码")
    private String idCard;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "性别(member.SexEnum)")
    private SexEnum sex;

    @ApiModelProperty(value = "国家")
    private String country;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "区域")
    private String county;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "会员头像")
    private String memberHeadUrl;

    @ApiModelProperty(value = "出生日期")
    private String birthday;

}