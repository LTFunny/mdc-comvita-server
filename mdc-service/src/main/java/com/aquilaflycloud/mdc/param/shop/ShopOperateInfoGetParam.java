package com.aquilaflycloud.mdc.param.shop;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.shop.ShopCommentStateEnum;
import com.aquilaflycloud.mdc.enums.shop.ShopCommentScoreEnum;
import com.aquilaflycloud.mdc.model.shop.ShopCommentInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * ShopOperateInfoGetParam
 *
 * @author zengqingjie
 * @date 2020-04-07
 */
@Data
@Accessors(chain = true)
public class ShopOperateInfoGetParam extends PageParam<ShopCommentInfo> implements Serializable {

    private static final long serialVersionUID = 2953887925972694455L;

    @ApiModelProperty(value = "商户信息id", required = true)
    @NotNull(message = "商户信息id不能为空")
    private Long id;

    @ApiModelProperty(value = "评分枚举(shop.ShopCommentScoreEnum)")
    private ShopCommentScoreEnum scoreEnum;

    @ApiModelProperty(value = "评分")
    private BigDecimal score;

    @ApiModelProperty(value = "状态(shop.ShopCommentStateEnum)")
    private ShopCommentStateEnum state;

    @ApiModelProperty(value = "评论开始时间")
    private Date startTime;

    @ApiModelProperty(value = "评论结束时间")
    private Date endTime;
}
