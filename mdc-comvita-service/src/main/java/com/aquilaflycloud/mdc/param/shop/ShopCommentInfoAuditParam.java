package com.aquilaflycloud.mdc.param.shop;

import com.aquilaflycloud.mdc.enums.shop.ShopCommentStateEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class ShopCommentInfoAuditParam implements Serializable {
    private static final long serialVersionUID = 964760282570096374L;

    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "状态(shop.ShopCommentStateEnum)")
    @NotNull(message = "状态不能为空")
    private ShopCommentStateEnum state;

    @ApiModelProperty(value = "处理备注")
    private String remark;

}
