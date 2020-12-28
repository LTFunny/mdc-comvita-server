package com.aquilaflycloud.mdc.param.ticket;

import com.aquilaflycloud.mdc.enums.ticket.ProductInfoRecommendEnum;
import com.aquilaflycloud.mdc.enums.ticket.ProductInfoTopEnum;
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
public class ProductInfoUpdateTopParam implements Serializable {
    private static final long serialVersionUID = 8201359840412341208L;
    @ApiModelProperty(value = "产品id", required = true)
    @NotNull(message = "产品id不能为空")
    private Long id;

    @ApiModelProperty(value = "是否推荐(ticket.ProductInfoTopEnum)", required = true)
    @NotNull(message = "是否置顶枚举不能为空")
    private ProductInfoTopEnum isTop;

    @ApiModelProperty(value = "置顶时间", hidden = true)
    private Date topTime;
}
