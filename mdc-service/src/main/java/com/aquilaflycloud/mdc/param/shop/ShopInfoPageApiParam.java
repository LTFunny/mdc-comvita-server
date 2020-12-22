package com.aquilaflycloud.mdc.param.shop;

import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import com.aquilaflycloud.mdc.enums.shop.ShopCenturyShopEnum;
import com.aquilaflycloud.mdc.enums.shop.ShopInfoSortEnum;
import com.aquilaflycloud.mdc.enums.shop.ShopRecommendEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * AlipayShopInfoListParam
 *
 * @author zengqingjie
 * @date 2020-04-07
 */
@Data
@Accessors(chain = true)
public class ShopInfoPageApiParam extends PageAuthParam implements Serializable {
    @ApiModelProperty(value = "分类id")
    private Long categoryId;

    @ApiModelProperty(value = "店铺全称")
    private String shopFullName;

    @ApiModelProperty(value = "店铺简称")
    private String shopName;

    @ApiModelProperty(value = "是否推荐(shop.ShopRecommendEnum)")
    private ShopRecommendEnum isRecommend;

    @ApiModelProperty(value = "是否百年老店(shop.ShopCenturyShopEnum)")
    private ShopCenturyShopEnum isCenturyShop;

    @ApiModelProperty(value = "排序枚举(shop.ShopInfoSortEnum)")
    private ShopInfoSortEnum sortEnum;

}
