package com.aquilaflycloud.mdc.param.member;

import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ClientConfigSaveParam {
    @ApiModelProperty(value = "微信或支付宝appId")
    private String appId;

    @ApiModelProperty(value = "等级奖励类型(member.RewardTypeEnum)")
    private RewardTypeEnum gradeType;

    @ApiModelProperty(value = "等级占比奖励类型(member.RewardTypeEnum)")
    private RewardTypeEnum gradeRateType;

    @ApiModelProperty(value = "排名奖励类型(member.RewardTypeEnum)")
    private RewardTypeEnum rankType;

    @ApiModelProperty(value = "总值奖励类型(member.RewardTypeEnum)")
    private RewardTypeEnum totalType;

    @ApiModelProperty(value = "推荐商品图片")
    private String recommendGoodsImg;

    @ApiModelProperty(value = "默认等级名称")
    private String defaultGradeTitle;

    @ApiModelProperty(value = "兑换订单超时支付时间(分钟)")
    private Integer exchangeOrderOvertimeToPay;

    @ApiModelProperty(value = "兑换订单超时确认时间(天)")
    private Integer exchangeOrderOverConfirmDay;

    @ApiModelProperty(value = "兑换订单超时退款时间(天)")
    private Integer exchangeOrderOverRefundDay;

    @ApiModelProperty(value = "兑换订单退款原因类型")
    private String[] exchangeOrderReasonTypes;

    @ApiModelProperty(value = "店铺详情路由")
    private String shopDetailPage;

    @ApiModelProperty(value = "地图中心点")
    private String mapCenter;
}
