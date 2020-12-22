package com.aquilaflycloud.mdc.result.ticket;

import com.aquilaflycloud.mdc.enums.ticket.ScenicSpotTypeEnum;
import com.aquilaflycloud.mdc.model.ticket.TicketProductInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * ProductInfoResult
 *
 * @author Zengqingjie
 * @date 2019-10-29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProductInfoByIdResult extends TicketProductInfo {
    @ApiModelProperty(value = "联票设置景区枚举集合")
    private List<ScenicSpotTypeEnum> scenicSpotTypeEnums;

    @ApiModelProperty(value = "产品类型拼接字符串")
    private String typeStr;
}
