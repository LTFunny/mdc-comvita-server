package com.aquilaflycloud.mdc.result.ticket;

import com.aquilaflycloud.mdc.model.ticket.TicketProductInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 微信小程序购票页面获取所有产品信息
 * </p>
 *
 * @author Zengqingjie
 * @since 2019-11-18
 */
@Data
public class WechatGetProductInfoResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "热门推荐产品集合")
    private List<TicketProductInfo> recommendList;

    @ApiModelProperty(value = "海洋馆产品集合")
    private List<TicketProductInfo> oceanList;

    @ApiModelProperty(value = "博物馆产品集合")
    private List<TicketProductInfo> museumList;

    @ApiModelProperty(value = "雨林馆产品集合")
    private List<TicketProductInfo> rainforestList;

    @ApiModelProperty(value = "套票产品集合")
    private List<TicketProductInfo> compositeList;

}
