package com.aquilaflycloud.mdc.param.ticket;

import com.aquilaflycloud.mdc.enums.ticket.ProductInfoPlayTimeType;
import com.aquilaflycloud.mdc.enums.ticket.ProductInfoSetPlayTimeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * ProductInfoUpdateParam 更新产品信息游玩时间
 *
 * @author Zengqingjie
 * @date 2019-11-18
 */

@Data
@Accessors(chain = true)
public class ProductInfoUpdatePlayTimeParam implements Serializable {
    private static final long serialVersionUID = 8201359840412341208L;
    @ApiModelProperty(value = "产品id", required = true)
    @NotNull(message = "产品id不能为空")
    private Long id;

    @ApiModelProperty(value = "是否设置游玩时间(ticket.ProductInfoSetPlayTimeEnum)", required = true)
    @NotNull(message = "是否设置游玩时间不能为空")
    private ProductInfoSetPlayTimeEnum isSetPlayTime;

    @ApiModelProperty(value = "可选日期类型(ticket.ProductInfoPlayTimeType)")
    private ProductInfoPlayTimeType playTimeType;

    @ApiModelProperty(value = "自定义游玩开始时间")
    private Date playStartTime;

    @ApiModelProperty(value = "自定义游玩结束时间")
    private Date playEndTime;

    @ApiModelProperty(value = "不可选日期集合")
    private String notPlayTime;
}
