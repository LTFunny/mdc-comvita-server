package com.aquilaflycloud.mdc.param.shop;

import com.aquilaflycloud.dataAuth.common.AuthParam;
import com.aquilaflycloud.mdc.enums.shop.ShopCommentAnonymousEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * AlipayShopCommentInfoAddParam
 *
 * @author zengqingjie
 * @date 2020-04-16
 */
@Data
@Accessors(chain = true)
public class ShopCommentInfoAddApiParam extends AuthParam implements Serializable {
    @ApiModelProperty(value = "评论内容", required = true)
    @NotBlank(message = "评论内容不能为空")
    private String content;

    @ApiModelProperty(value = "总体评分", required = true)
    @NotNull(message = "总体评分不能为空")
    private Integer allScore;

    @ApiModelProperty(value = "服务评分", required = true)
    @NotNull(message = "服务评分不能为空")
    private Integer serviceScore;

    @ApiModelProperty(value = "环境评分", required = true)
    @NotNull(message = "环境评分不能为空")
    private Integer environmentScore;

    @ApiModelProperty(value = "口味评分", required = true)
    @NotNull(message = "口味评分不能为空")
    private Integer tasteScore;

    @ApiModelProperty(value = "人均消费")
    private BigDecimal averageConsumption;

    @ApiModelProperty(value = "评论图片json")
    private String picUrl;

    @ApiModelProperty(value = "是否匿名(shop.ShopCommentAnonymousEnum)", required = true)
    @NotNull(message = "是否匿名不能为空")
    private ShopCommentAnonymousEnum isAnonymous;

    @ApiModelProperty(value = "商户id", required = true)
    @NotNull(message = "商户id不能为空")
    private Long shopId;
}
