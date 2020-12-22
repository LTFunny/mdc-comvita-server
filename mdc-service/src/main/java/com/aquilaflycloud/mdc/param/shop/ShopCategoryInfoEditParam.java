package com.aquilaflycloud.mdc.param.shop;

import com.aquilaflycloud.dataAuth.common.AuthParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * ShopCategoryInfoEditParam
 *
 * @author zengqingjie
 * @date 2020-04-07
 */
@Data
@Accessors(chain = true)
public class ShopCategoryInfoEditParam extends AuthParam implements Serializable {

    private static final long serialVersionUID = 2953887925972694455L;

    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "描述")
    private String simpleDesc;

    @ApiModelProperty(value = "排序")
    private Integer catalogOrder;
}
