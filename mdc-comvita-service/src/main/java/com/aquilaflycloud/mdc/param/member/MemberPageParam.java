package com.aquilaflycloud.mdc.param.member;

import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.member.*;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class MemberPageParam extends PageAuthParam<MemberInfo> {
    @ApiModelProperty(value = "会员码")
    private String memberCode;

    @ApiModelProperty(value = "是否有手机号码(common.WhetherEnum)")
    private WhetherEnum hasPhoneNumber;

    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;

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

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "国家(模糊查询)")
    private String countryLike;

    @ApiModelProperty(value = "省份(模糊查询)")
    private String provinceLike;

    @ApiModelProperty(value = "城市(模糊查询)")
    private String cityLike;

    @ApiModelProperty(value = "区域(模糊查询)")
    private String countyLike;

    @ApiModelProperty(value = "地址(模糊查询)")
    private String addressLike;

    @ApiModelProperty(value = "生日开始时间")
    private Date birthdayStart;

    @ApiModelProperty(value = "生日结束时间")
    private Date birthdayEnd;

    @ApiModelProperty(value = "是否授权(common.WhetherEnum)")
    private WhetherEnum isAuth;

    @ApiModelProperty(value = "是否中国(common.WhetherEnum)")
    private WhetherEnum isChina;

    @ApiModelProperty(value = "微信或支付宝appId")
    private String appId;

    @ApiModelProperty(value = "会员类型(member.SourceEnum)")
    private SourceEnum source;

    @ApiModelProperty(value = "会员子类型(member.SubSourceEnum)")
    private SubSourceEnum subSource;

    @ApiModelProperty(value = "创建开始时间")
    private Date createTimeStart;

    @ApiModelProperty(value = "创建结束时间")
    private Date createTimeEnd;
}