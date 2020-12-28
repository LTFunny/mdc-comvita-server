package com.aquilaflycloud.mdc.param.shop;

import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import com.aquilaflycloud.mdc.model.shop.ShopCommentInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * AlipayShopCommentInfoPageParam
 *
 * @author zengqingjie
 * @date 2020-04-16
 */
@Data
@Accessors(chain = true)
public class ShopCommentInfoPageApiParam extends PageAuthParam<ShopCommentInfo> implements Serializable {
    @ApiModelProperty(value = "店铺id", required = true)
    @NotNull(message = "店铺id不能为空")
    private Long shopId;
}
