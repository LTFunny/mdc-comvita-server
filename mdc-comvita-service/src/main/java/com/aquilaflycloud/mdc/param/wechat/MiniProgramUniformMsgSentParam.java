package com.aquilaflycloud.mdc.param.wechat;

import cn.binarywang.wx.miniapp.bean.WxMaTemplateData;
import com.aquilaflycloud.common.AnotherFieldHasValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@AnotherFieldHasValue.List({
        @AnotherFieldHasValue(fieldName = "sendMpMessage", fieldValue = "true", notNullFieldName = "mpAppId", message = "微信公众号appId不能为空"),
        @AnotherFieldHasValue(fieldName = "sendMpMessage", fieldValue = "true", notNullFieldName = "url", message = "消息跳转url不能为空"),
        @AnotherFieldHasValue(fieldName = "sendMpMessage", fieldValue = "false", notNullFieldName = "pagePath", message = "消息跳转小程序页面路径不能为空"),
        @AnotherFieldHasValue(fieldName = "sendMpMessage", fieldValue = "false", notNullFieldName = "formId", message = "小程序模板消息formId不能为空")
})
@Data
@Accessors(chain = true)
public class MiniProgramUniformMsgSentParam {

    @ApiModelProperty(value = "微信模板消息id", required = true)
    @NotBlank(message = "微信模板消息id不能为空")
    private String templateId;

    @ApiModelProperty(value = "微信openId", required = true)
    @NotBlank(message = "微信openId不能为空")
    private String openId;

    @ApiModelProperty(value = "微信小程序AppId", required = true)
    @NotBlank(message = "微信小程序AppId不能为空")
    private String appId;

    @ApiModelProperty(value = "微信模板内容", required = true)
    @NotEmpty(message = "微信模板内容不能为空")
    private List<WxMaTemplateData> data;

    @ApiModelProperty(value = "发送消息类型(是否发送公众号模板消息)")
    private Boolean sendMpMessage = true;

    @ApiModelProperty(value = "消息跳转小程序页面路径")
    private String pagePath;

    @ApiModelProperty(value = "消息跳转url")
    private String url;

    @ApiModelProperty(value = "微信公众号appId")
    private String mpAppId;

    @ApiModelProperty(value = "小程序模板消息formId")
    private String formId;

    @ApiModelProperty(value = "模板需要放大的关键词,不填则默认无放大")
    private String emphasisKeyword;

}
