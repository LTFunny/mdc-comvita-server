package com.aquilaflycloud.mdc.param.ticket;

import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import com.aquilaflycloud.mdc.enums.ticket.ProductInfoStateEnum;
import com.aquilaflycloud.mdc.model.ticket.TicketProductInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ProductInfoActivityListParam extends PageAuthParam<TicketProductInfo> implements Serializable {
    @ApiModelProperty(value = "产品id或产品名称模糊搜索")
    private String keyWord;

    @ApiModelProperty(value = "状态(ticket.ProductInfoStateEnum)", hidden = true)
    private ProductInfoStateEnum state;
}
