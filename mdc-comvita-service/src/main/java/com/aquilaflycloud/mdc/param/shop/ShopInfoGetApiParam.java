package com.aquilaflycloud.mdc.param.shop;

import com.aquilaflycloud.dataAuth.common.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * AlipayShopInfoGetParam
 *
 * @author zengqingjie
 * @date 2020-04-15
 */
@Data
@Accessors(chain = true)
public class ShopInfoGetApiParam extends PageParam implements Serializable {
    @ApiModelProperty(value = "店铺id")
    private Long id;

    @ApiModelProperty(value = "关系id")
    private Long relationId;
}
