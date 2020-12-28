package com.aquilaflycloud.mdc.param.coupon;

import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import com.aquilaflycloud.mdc.enums.common.CreateSourceEnum;
import com.aquilaflycloud.mdc.enums.coupon.CouponTypeEnum;
import com.aquilaflycloud.mdc.enums.coupon.VerificateModeEnum;
import com.aquilaflycloud.mdc.enums.coupon.VerificateStateEnum;
import com.aquilaflycloud.mdc.model.coupon.CouponMemberRel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * CouponMemberPageParam
 *
 * @author star
 * @date 2020-03-10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CouponRelPageParam extends PageAuthParam<CouponMemberRel> implements Serializable {
    @ApiModelProperty(value = "优惠券id")
    private Long couponId;

    @ApiModelProperty(value = "手机号")
    private String phoneNumber;

    @ApiModelProperty(value = "微信或支付宝昵称")
    private String nickName;

    @ApiModelProperty(value = "优惠券编码")
    private String couponCode;

    @ApiModelProperty(value = "优惠券名称")
    private String couponName;

    @ApiModelProperty(value = "优惠券类型(coupon.CouponTypeEnum)")
    private CouponTypeEnum couponType;

    @ApiModelProperty(value = "创建来源(common.CreateSourceEnum)")
    private CreateSourceEnum createSource;

    @ApiModelProperty(value = "领取开始时间")
    private Date receiveTimeStart;

    @ApiModelProperty(value = "领取结束时间")
    private Date receiveTimeEnd;

    @ApiModelProperty(value = "核销方式(coupon.VerificateModeEnum)")
    private VerificateModeEnum verificateMode;

    @ApiModelProperty(value = "核销状态(coupon.VerificateStateEnum)")
    private VerificateStateEnum verificateState;

    @ApiModelProperty(value = "核销开始时间")
    private Date verificateTimeStart;

    @ApiModelProperty(value = "核销结束时间")
    private Date verificateTimeEnd;

    @ApiModelProperty(value = "核销人名称")
    private String verificaterName;

    @ApiModelProperty(value = "核销人所属部门名称")
    private String verificaterOrgNames;

    @ApiModelProperty(value = "是否按核销时间排序(默认否,按照领取时间排序)")
    private Boolean orderByVerificateTime = false;
}
