package com.aquilaflycloud.mdc.param.shop;

import com.aquilaflycloud.common.AnotherFieldHasValue;
import com.aquilaflycloud.mdc.enums.shop.ShopComplaintStateEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@AnotherFieldHasValue.List({
        @AnotherFieldHasValue(fieldName = "state", fieldValue = "PASS", notNullFieldName = "passRemark", message = "处理备注不能为空"),
        @AnotherFieldHasValue(fieldName = "state", fieldValue = "NOPASS", notNullFieldName = "passRemark", message = "处理备注不能为空"),
        @AnotherFieldHasValue(fieldName = "state", fieldValue = "APPEAL_SUCCESS", notNullFieldName = "appealRemark", message = "申诉备注不能为空"),
        @AnotherFieldHasValue(fieldName = "state", fieldValue = "APPEAL_FAIL", notNullFieldName = "appealRemark", message = "申诉备注不能为空"),
})

@Data
@Accessors(chain = true)
public class ShopComplaintInfoEditParam implements Serializable {
    private static final long serialVersionUID = -3645018465352635859L;

    @ApiModelProperty(value = "投诉id", required = true)
    @NotNull(message = "投诉id不能为空")
    private Long id;

    @ApiModelProperty(value = "状态", required = true)
    @NotNull(message = "状态不能为空")
    private ShopComplaintStateEnum state;

    @ApiModelProperty(value = "通过备注")
    private String passRemark;

    @ApiModelProperty(value = "通过图片")
    private String passPicUrl;

    @ApiModelProperty(value = "申诉备注")
    private String appealRemark;

    @ApiModelProperty(value = "申诉图片")
    private String appealPicUrl;
}
