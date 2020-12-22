package com.aquilaflycloud.mdc.param.wechat;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.wechat.CodeAuditStateEnum;
import com.aquilaflycloud.mdc.enums.wechat.CodeReleaseStateEnum;
import com.aquilaflycloud.mdc.enums.wechat.TemplateConfigTypeEnum;
import com.aquilaflycloud.mdc.model.wechat.WechatMiniProgramCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class CodeListParam extends PageParam<WechatMiniProgramCode> {

    @ApiModelProperty(value = "小程序appId")
    private String appId;

    @ApiModelProperty(value = "代码版本号")
    private String codeVersion;

    @ApiModelProperty(value = "代码描述")
    private String codeDesc;

    @ApiModelProperty(value = "模板类型(wechat.TemplateConfigTypeEnum)")
    private TemplateConfigTypeEnum templateType;

    @ApiModelProperty(value = "审核状态(wechat.CodeAuditStateEnum)")
    private CodeAuditStateEnum auditState;

    @ApiModelProperty(value = "发布状态(wechat.CodeReleaseStateEnum)")
    private CodeReleaseStateEnum releaseState;
}
