package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.pre.ActivityCommentStateEnum;
import com.aquilaflycloud.mdc.enums.pre.FlashOrderInfoStateEnum;
import com.aquilaflycloud.mdc.model.pre.PreActivityInfo;
import com.aquilaflycloud.mdc.model.pre.PreCommentInfo;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * MemberFlashPageParam
 *
 * @author star
 * @date 2021/3/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommentPageParam extends PageParam<PreCommentInfo> {
    /**
     * 状态 1-待审核 2-审核通过 3-审核不通过
     */
    @ApiModelProperty(value = "点评状态(pre.ActivityCommentStateEnum)")
    private String comState;
}
