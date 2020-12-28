package com.aquilaflycloud.mdc.param.exchange;

import com.aquilaflycloud.dataAuth.common.AuthParam;
import com.aquilaflycloud.mdc.enums.exchange.ShowThumbnailEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * ExchangeGoodsSpecValueAddInfo
 *
 * @author zengqingjie
 * @date 2020-07-01
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExchangeGoodsSpecValueAddInfo extends AuthParam implements Serializable {
    @ApiModelProperty(value = "规格id", required = true)
    @NotNull(message = "规格id不能为空")
    private Long specId;

    @ApiModelProperty(value = "规格值ids", required = true)
    @NotBlank(message = "规格值ids不能为空")
    private String specValueIds;

    @ApiModelProperty(value = "配置json串", required = true)
    @NotBlank(message = "配置json串不能为空")
    private String configureJson;

    @ApiModelProperty(value = "是否展示缩略图(exchange.ShowThumbnailEnum)")
    private ShowThumbnailEnum showThumbnail;

    @ApiModelProperty(value = "排序", required = true)
    @NotNull(message = "排序不能为空")
    private String sortNo;

}
