package com.aquilaflycloud.mdc.param.shop;

import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import com.aquilaflycloud.mdc.enums.shop.ShopCommentStateEnum;
import com.aquilaflycloud.mdc.enums.shop.ShopCommentScoreEnum;
import com.aquilaflycloud.mdc.model.shop.ShopCommentInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
public class ShopCommentInfoListParam extends PageAuthParam<ShopCommentInfo> implements Serializable {
    private static final long serialVersionUID = 964760282570096374L;

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
