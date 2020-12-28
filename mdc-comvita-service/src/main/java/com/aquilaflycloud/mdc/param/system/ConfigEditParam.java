package com.aquilaflycloud.mdc.param.system;

import com.aquilaflycloud.mdc.enums.member.SourceEnum;
import com.aquilaflycloud.mdc.enums.member.UnifiedColNameEnum;
import com.aquilaflycloud.mdc.enums.member.UnifiedJudgeColEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * ConfigEditParam
 *
 * @author star
 * @date 2020-04-09
 */
@Data
public class ConfigEditParam {
    @ApiModelProperty(value = "配置id", required = true)
    @NotNull(message = "配置id不能为空")
    private Long id;

    @ApiModelProperty(value = "配置值")
    private String configValue;

    @ApiModelProperty(value = "微信直联支付配置")
    private WechatDirectPayConfigParam wechatDirectPayConfig;

    @ApiModelProperty(value = "通用授权应用配置")
    private AuthorUniversalConfigParam authorUniversalConfig;

    @ApiModelProperty(value = "统一会员配置")
    private UnifiedMemberConfigParam unifiedMemberConfig;

    @ApiModelProperty(value = "配置说明")
    private String remark;

    @Data
    public class WechatDirectPayConfigParam {
        @ApiModelProperty(value = "微信直联支付是否生效")
        private Boolean effective;
    }

    @Data
    public class AuthorUniversalConfigParam {
        @ApiModelProperty(value = "是否显示通用应用")
        private Boolean visible;
    }

    @Data
    public class UnifiedMemberConfigParam {
        @ApiModelProperty(value = "是否使用统一会员")
        private Boolean unified;

        @ApiModelProperty(value = "统一会员判断字段")
        private UnifiedJudgeColEnum unifiedJudgeCol;

        @ApiModelProperty(value = "统一会员来源")
        private SourceEnum unifiedSource;

        @ApiModelProperty(value = "统一会员字段")
        private UnifiedColNameEnum unifiedCol;

        @ApiModelProperty(value = "统一会员字段值")
        private String unifiedValue;

        @ApiModelProperty(value = "被统一会员来源范围")
        @Size(min = 1, message = "被统一会员来源范围不能为空")
        private Set<SourceEnum> includeSource;

        @ApiModelProperty(value = "被统一会员字段")
        private UnifiedColNameEnum includeCol;

        @ApiModelProperty(value = "被统一会员字段值")
        @Size(min = 1, message = "被统一会员字段值不能为空")
        private Set<String> includeValue;
    }
}
