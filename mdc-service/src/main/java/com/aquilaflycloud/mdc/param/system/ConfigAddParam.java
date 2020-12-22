package com.aquilaflycloud.mdc.param.system;

import com.aquilaflycloud.common.AnotherFieldHasValue;
import com.aquilaflycloud.common.NoneBlank;
import com.aquilaflycloud.mdc.enums.member.SourceEnum;
import com.aquilaflycloud.mdc.enums.member.UnifiedColNameEnum;
import com.aquilaflycloud.mdc.enums.member.UnifiedJudgeColEnum;
import com.aquilaflycloud.mdc.enums.system.TenantConfigTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * ConfigAddParam
 *
 * @author star
 * @date 2020-04-09
 */
@NoneBlank(fieldNames = {"alipayDirectPayConfig", "wechatDirectPayConfig", "authorUniversalConfig", "unifiedMemberConfig"}, message = "配置参数不能都为空")
@AnotherFieldHasValue.List({
        @AnotherFieldHasValue(fieldName = "configType", fieldValue = "ALIPAYDIRECTPAY", notNullFieldName = "alipayDirectPayConfig", message = "支付宝直联支付配置不能为空"),
        @AnotherFieldHasValue(fieldName = "configType", fieldValue = "WECHATDIRECTPAY", notNullFieldName = "wechatDirectPayConfig", message = "微信直联支付配置不能为空"),
        @AnotherFieldHasValue(fieldName = "configType", fieldValue = "AUTHORUNIVERSAL", notNullFieldName = "authorUniversalConfig", message = "通用授权应用配置不能为空"),
        @AnotherFieldHasValue(fieldName = "configType", fieldValue = "UNIFIEDMEMBER", notNullFieldName = "unifiedMemberConfig", message = "统一会员配置不能为空"),
})
@Data
public class ConfigAddParam {
    @ApiModelProperty(value = "配置类型(system.TenantConfigTypeEnum)", required = true)
    @NotNull(message = "配置类型不能为空")
    private TenantConfigTypeEnum configType;

    @ApiModelProperty(value = "支付宝直联支付配置")
    private AlipayDirectPayConfigParam alipayDirectPayConfig;

    @ApiModelProperty(value = "微信直联支付配置")
    private WechatDirectPayConfigParam wechatDirectPayConfig;

    @ApiModelProperty(value = "通用授权应用配置")
    private AuthorUniversalConfigParam authorUniversalConfig;

    @ApiModelProperty(value = "统一会员配置")
    private UnifiedMemberConfigParam unifiedMemberConfig;

    @ApiModelProperty(value = "配置说明")
    private String remark;

    @Data
    private class AlipayDirectPayConfigParam {
        @ApiModelProperty(value = "支付宝直联支付是否生效", required = true)
        @NotNull(message = "是否生效不能为空")
        private Boolean effective;

        @ApiModelProperty(value = "支付宝服务商id")
        private String sysServiceProviderId;
    }

    @Data
    public class WechatDirectPayConfigParam {
        @ApiModelProperty(value = "微信直联支付是否生效", required = true)
        @NotNull(message = "是否生效不能为空")
        private Boolean effective;
    }

    @Data
    public class AuthorUniversalConfigParam {
        @ApiModelProperty(value = "是否显示通用应用", required = true)
        @NotNull(message = "是否显示不能为空")
        private Boolean visible;
    }

    @Data
    public class UnifiedMemberConfigParam {
        @ApiModelProperty(value = "是否使用统一会员", required = true)
        @NotNull(message = "是否使用不能为空")
        private Boolean unified;

        @ApiModelProperty(value = "统一会员判断字段", required = true)
        @NotNull(message = "统一会员判断字段不能为空")
        private UnifiedJudgeColEnum unifiedJudgeCol;

        @ApiModelProperty(value = "统一会员来源", required = true)
        @NotNull(message = "统一会员来源不能为空")
        private SourceEnum unifiedSource;

        @ApiModelProperty(value = "统一会员字段", required = true)
        @NotNull(message = "统一会员字段不能为空")
        private UnifiedColNameEnum unifiedCol;

        @ApiModelProperty(value = "统一会员字段值", required = true)
        @NotBlank(message = "统一会员字段值不能为空")
        private String unifiedValue;

        @ApiModelProperty(value = "被统一会员来源范围", required = true)
        @NotEmpty(message = "被统一会员来源范围不能为空")
        private Set<SourceEnum> includeSource;

        @ApiModelProperty(value = "被统一会员字段", required = true)
        @NotNull(message = "被统一会员字段不能为空")
        private UnifiedColNameEnum includeCol;

        @ApiModelProperty(value = "被统一会员字段值", required = true)
        @NotEmpty(message = "被统一会员字段值不能为空")
        private Set<String> includeValue;
    }
}
