package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.dataAuth.common.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author pengyongliang
 * @Date 2020/12/31 11:52
 * @Version 1.0
 */
@Data
public class PreOrderGoodsPageResult extends PageParam {

    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品描述")
    private String goodsDescription;

    @ApiModelProperty(value = "商品照片")
    private String goodsPicture;

    @ApiModelProperty(value = "预约门店")
    private String reserveShop;

    @ApiModelProperty(value = "预约开始时间")
    private Date reserveStartTime;

    @ApiModelProperty(value = "预约结束时间")
    private Date reserveEndTime;
}
