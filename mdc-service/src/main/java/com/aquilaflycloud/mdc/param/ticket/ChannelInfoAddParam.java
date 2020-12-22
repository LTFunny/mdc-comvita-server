package com.aquilaflycloud.mdc.param.ticket;

import com.aquilaflycloud.common.AnotherFieldHasValue;
import com.aquilaflycloud.dataAuth.common.AuthParam;
import com.aquilaflycloud.mdc.enums.ticket.ChannelInfoPageEnum;
import com.aquilaflycloud.mdc.enums.ticket.ChannelInfoPageTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@AnotherFieldHasValue.List({
        @AnotherFieldHasValue(fieldName = "showPageType", fieldValue = "SCENICSPOT", notNullFieldName = "showPage", message = "展示页面不能为空"),
        @AnotherFieldHasValue(fieldName = "showPageType", fieldValue = "SCENICSPOT", canNullFieldName = "refId", message = "关联数据id不能为空"),
})
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ChannelInfoAddParam extends AuthParam implements Serializable {

    private static final long serialVersionUID = 3221917251643852240L;
    @ApiModelProperty(value = "小程序appId", required = true)
    private String appId;

    @ApiModelProperty(value = "渠道名称", required = true)
    private String name;

    @ApiModelProperty(value = "渠道负责人", required = true)
    @NotNull(message = "渠道负责人不能为空")
    private String responsiblePerson;

    @ApiModelProperty(value = "联系电话", required = true)
    @NotNull(message = "联系电话不能为空")
    private String contactNumber;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "渠道二维码页面类型(ticket.ChannelInfoPageTypeEnum)", required = true)
    @NotNull(message = "页面类型不能为空")
    private ChannelInfoPageTypeEnum showPageType;

    @ApiModelProperty(value = "扫码渠道二维码成功后展示的页面(ticket.ChannelInfoPageEnum)")
    private ChannelInfoPageEnum showPage;

    @ApiModelProperty(value = "关联数据id")
    private Long refId;
}
