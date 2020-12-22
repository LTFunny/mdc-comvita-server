package com.aquilaflycloud.mdc.param.ticket;

import com.aquilaflycloud.mdc.enums.ticket.ProductInfoTopEnum;
import com.aquilaflycloud.mdc.enums.ticket.ProductInfoTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * ProductInfoUpdateParam 更新产品信息是否置顶
 *
 * @author Zengqingjie
 * @date 2019-11-18
 */

@Data
@Accessors(chain = true)
public class ProductInfoUpdateCompositeParam implements Serializable {
    private static final long serialVersionUID = 8201359840412341208L;
    @ApiModelProperty(value = "产品id", required = true)
    @NotNull(message = "产品id不能为空")
    private Long id;

    @ApiModelProperty(value = "产品类型(ticket.ProductInfoTypeEnum)", required = true)
    @NotNull(message = "产品类型不能为空")
    private ProductInfoTypeEnum type;

    @ApiModelProperty(value = "联票设置景区枚举集合(type逗号分割)")
    private String scenicSpotTypes;
}
