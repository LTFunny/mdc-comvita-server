package com.aquilaflycloud.mdc.param.ticket;

import com.aquilaflycloud.mdc.enums.ticket.ProductInfoRecommendEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * ProductInfoUpdateParam 更新产品信息是否推荐
 *
 * @author Zengqingjie
 * @date 2019-11-18
 */

@Data
@Accessors(chain = true)
public class ProductInfoUpdateRecommendParam implements Serializable {
    private static final long serialVersionUID = 8201359840412341208L;
    @ApiModelProperty(value = "产品id", required = true)
    @NotNull(message = "产品id不能为空")
    private Long id;

    @ApiModelProperty(value = "是否推荐(ticket.ProductInfoRecommendEnum)", required = true)
    @NotNull(message = "是否推荐枚举不能为空")
    private ProductInfoRecommendEnum isRecommend;
}
