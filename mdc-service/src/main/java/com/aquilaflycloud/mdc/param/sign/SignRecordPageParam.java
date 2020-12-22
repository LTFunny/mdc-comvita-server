package com.aquilaflycloud.mdc.param.sign;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.model.sign.OfflineSignMemberRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * SignRecordPageParam
 *
 * @author star
 * @date 2020-05-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SignRecordPageParam extends PageParam<OfflineSignMemberRecord> {
    @ApiModelProperty(value = "打卡活动id", required = true)
    @NotNull(message = "打卡活动id不能为空")
    private Long signId;
}


