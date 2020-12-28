package com.aquilaflycloud.mdc.param.shop;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * ShopInfoGetParam
 *
 * @author zengqingjie
 * @date 2020-04-07
 */
@Data
@Accessors(chain = true)
public class ShopCategoryInfoGetParam implements Serializable {

    private static final long serialVersionUID = 2953887925972694455L;

    @ApiModelProperty(value = "商户分类信息id", required = true)
    @NotNull(message = "商户分类信息id不能为空")
    private Long id;
}
