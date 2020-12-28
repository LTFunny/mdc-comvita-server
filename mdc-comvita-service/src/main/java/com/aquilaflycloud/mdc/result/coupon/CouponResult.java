package com.aquilaflycloud.mdc.result.coupon;

import com.aquilaflycloud.mdc.model.catalog.CatalogInfo;
import com.aquilaflycloud.mdc.model.coupon.CouponInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * CouponResult
 *
 * @author star
 * @date 2020-03-08
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CouponResult extends CouponInfo implements Serializable {
    @ApiModelProperty(value = "未领取数量")
    private Integer unReceivedCount;

    @ApiModelProperty(value = "已领过期数量")
    private Integer expiredCount;

    @ApiModelProperty(value = "分类列表")
    private List<CatalogInfo> catalogList;
}
