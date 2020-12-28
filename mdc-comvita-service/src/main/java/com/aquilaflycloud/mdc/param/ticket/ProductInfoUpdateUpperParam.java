package com.aquilaflycloud.mdc.param.ticket;

import com.aquilaflycloud.mdc.enums.ticket.ProductInfoUpperEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * ProductInfoUpdateUpperParam 更新产品信息是否上架
 *
 * @author Zengqingjie
 * @date 2020-2-7
 */

@Data
@Accessors(chain = true)
public class ProductInfoUpdateUpperParam implements Serializable {
    private static final long serialVersionUID = 8201359840412341208L;

    @ApiModelProperty(value = "产品id", required = true)
    @NotNull(message = "产品id不能为空")
    private Long id;

    @ApiModelProperty(value = "是否上架", required = true)
    @NotNull(message = "是否上架不能为空")
    private ProductInfoUpperEnum isUpper;
}
