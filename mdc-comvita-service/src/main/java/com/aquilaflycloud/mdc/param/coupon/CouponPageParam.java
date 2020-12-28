package com.aquilaflycloud.mdc.param.coupon;

import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.coupon.CouponStateEnum;
import com.aquilaflycloud.mdc.enums.coupon.CouponTypeEnum;
import com.aquilaflycloud.mdc.model.coupon.CouponInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * CouponPageParam
 *
 * @author star
 * @date 2020-03-10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CouponPageParam extends PageAuthParam<CouponInfo> implements Serializable {
    @ApiModelProperty(value = "优惠券编码")
    private String couponCode;

    @ApiModelProperty(value = "优惠券名称")
    private String couponName;

    @ApiModelProperty(value = "优惠券类型(coupon.CouponTypeEnum)")
    private CouponTypeEnum couponType;

    @ApiModelProperty(value = "创建开始时间")
    private Date createTimeStart;

    @ApiModelProperty(value = "创建结束时间")
    private Date createTimeEnd;

    @ApiModelProperty(value = "创建人名称")
    private String creatorName;

    @ApiModelProperty(value = "指定用户部门名称")
    private String designateOrgNames;

    @ApiModelProperty(value = "状态(coupon.CouponStatusEnum)")
    private CouponStateEnum state;

    @ApiModelProperty(value = "分类id")
    private Long catalogId;

    @ApiModelProperty(value = "是否查询有效状态(common.WhetherEnum)(默认否, 为是时state无效)")
    private WhetherEnum valid = WhetherEnum.NO;

    @ApiModelProperty(value = "商铺关联id")
    private Long relationId;
}
