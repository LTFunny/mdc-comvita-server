package com.aquilaflycloud.mdc.result.sign;

import com.aquilaflycloud.mdc.model.sign.OfflineSignActivity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * OfflineSignResult
 *
 * @author star
 * @date 2020-05-07
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OfflineSignResult extends OfflineSignActivity {
    @ApiModelProperty(value = "打卡次数")
    private Integer signCount;

    @ApiModelProperty(value = "打卡人数")
    private Integer signMember;
}
