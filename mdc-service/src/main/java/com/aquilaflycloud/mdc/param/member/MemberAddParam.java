package com.aquilaflycloud.mdc.param.member;

import com.aquilaflycloud.dataAuth.common.AuthParam;
import com.aquilaflycloud.mdc.enums.member.SexEnum;
import com.aquilaflycloud.mdc.enums.member.SourceEnum;
import com.aquilaflycloud.mdc.enums.member.SubSourceEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@Accessors(chain = true)
public class MemberAddParam extends AuthParam {
    /**
     * 会员编码
     */
    @ApiModelProperty(value = "会员编码")
    private String memberCode;

    /**
     * 会员名称
     */
    @ApiModelProperty(value = "会员名称", required = true)
    @NotBlank(message = "会员名称不能为空")
    private String memberName;

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码", required = true)
    @NotBlank(message = "手机号码不能为空")
    private String phoneNumber;

    /**
     * 身份证号码
     */
    @ApiModelProperty(value = "身份证号码")
    private String idCard;

    /**
     * 真实姓名
     */
    @ApiModelProperty(value = "真实姓名")
    private String realName;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickName;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    private SexEnum sex;

    /**
     * 国家
     */
    @ApiModelProperty(value = "国家")
    private String country;

    /**
     * 省份
     */
    @ApiModelProperty(value = "省份")
    private String province;

    /**
     * 城市
     */
    @ApiModelProperty(value = "城市")
    private String city;

    /**
     * 区域
     */
    @ApiModelProperty(value = "区域")
    private String county;

    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址")
    private String address;

    /**
     * 会员头像
     */
    @ApiModelProperty(value = "会员头像")
    private String avatarUrl;

    /**
     * 出生日期
     */
    @ApiModelProperty(value = "出生日期")
    private Date birthday;

    /**
     * 微信appId
     */
    @ApiModelProperty(value = "微信appId")
    private String wxAppId;

    /**
     * 微信openId
     */
    @ApiModelProperty(value = "微信openId")
    private String openId;

    /**
     * 微信unionId
     */
    @ApiModelProperty(value = "微信unionId")
    private String unionId;

    /**
     * 支付宝appId
     */
    @ApiModelProperty(value = "支付宝appId")
    private String aliAppId;

    /**
     * 支付宝userId
     */
    @ApiModelProperty(value = "支付宝userId")
    private String userId;

    /**
     * 会员类型
     */
    @ApiModelProperty(value = "会员类型", hidden = true)
    private SourceEnum source = SourceEnum.MANUAL;

    /**
     * 会员子类型
     */
    @ApiModelProperty(value = "会员子类型")
    private SubSourceEnum subSource;
}
