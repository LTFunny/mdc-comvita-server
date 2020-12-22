package com.aquilaflycloud.mdc.param.shop;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class ShopComplaintInfoGetParam implements Serializable {
    private static final long serialVersionUID = -3645018465352635859L;

    @ApiModelProperty(value = "投诉id", required = true)
    @NotNull(message = "投诉id不能为空")
    private Long id;
}
