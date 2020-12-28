package com.aquilaflycloud.mdc.result.exchange;

import com.aquilaflycloud.mdc.enums.exchange.ShowThumbnailEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * GoodsSpecValueInfoResult
 *
 * @author zengqingjie
 * @date 2020-07-04
 */
@Data
public class GoodsSpecValueInfoResult implements Serializable {
    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "规格id")
    private Long specId;

    @ApiModelProperty(value = "规格名称")
    private String name;

    @ApiModelProperty(value = "排序")
    private String sortNo;

    @ApiModelProperty(value = "是否展示缩略图(exchange.ShowThumbnailEnum)")
    private ShowThumbnailEnum showThumbnail;

    @ApiModelProperty(value = "配置json串")
    private String configureJson;

    /*@ApiModelProperty(value = "缩略图Url")
    private String thumbnailUrl;*/

    @ApiModelProperty(value = "规格值信息")
    private List<GoodsValueInfoResult> goodsValueInfoResults;


}
