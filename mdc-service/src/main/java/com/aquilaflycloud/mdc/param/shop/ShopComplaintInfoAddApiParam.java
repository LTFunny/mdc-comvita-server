package com.aquilaflycloud.mdc.param.shop;

import com.aquilaflycloud.dataAuth.common.AuthParam;
import com.aquilaflycloud.mdc.enums.shop.ShopCommentAnonymousEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * ShopComplaintInfoAddApiParam
 *
 * @author zengqingjie
 * @date 2020-04-23
 */
@Data
@Accessors(chain = true)
public class ShopComplaintInfoAddApiParam extends AuthParam implements Serializable {

    private static final long serialVersionUID = -6842437102056510839L;

    @ApiModelProperty(value = "投诉内容", required = true)
    @NotBlank(message = "投诉内容不能为空")
    private String content;

    @ApiModelProperty(value = "投诉图片", required = true)
    @NotBlank(message = "投诉图片不能为空")
    private String picUrl;

    @ApiModelProperty(value = "联系方式", required = true)
    @NotBlank(message = "联系方式不能为空")
    private String contactNumber;

    @ApiModelProperty(value = "是否匿名(shop.ShopCommentAnonymousEnum)", required = true)
    @NotNull(message = "是否匿名不能为空")
    private ShopCommentAnonymousEnum isAnonymous;

    @ApiModelProperty(value = "商户id", required = true)
    @NotNull(message = "商户id不能为空")
    private Long shopId;
}
