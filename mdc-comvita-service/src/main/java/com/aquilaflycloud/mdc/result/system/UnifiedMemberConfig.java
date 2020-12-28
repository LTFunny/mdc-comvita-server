package com.aquilaflycloud.mdc.result.system;

import com.aquilaflycloud.mdc.enums.member.SourceEnum;
import com.aquilaflycloud.mdc.enums.member.UnifiedColNameEnum;
import com.aquilaflycloud.mdc.enums.member.UnifiedJudgeColEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

/**
 * UnifiedMemberConfig
 *
 * @author star
 * @date 2020/11/5
 */
@Data
public class UnifiedMemberConfig {
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
    private Set<SourceEnum> includeSource;

    @ApiModelProperty(value = "被统一会员字段")
    private UnifiedColNameEnum includeCol;

    @ApiModelProperty(value = "被统一会员字段值")
    private Set<String> includeValue;
}
