package com.aquilaflycloud.mdc.result.member;

import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * MemberRewardRankResult
 *
 * @author star
 * @date 2019-12-30
 */
@Data
public class MemberRewardRankResult implements Serializable {
    @ApiModelProperty(value = "当前排名")
    private RankResult memberRank;

    @ApiModelProperty(value = "排名榜")
    private List<RankResult> memberRankList;

    @Data
    public static class RankResult implements Serializable {
        @ApiModelProperty(value = "排序")
        private Long rankNo;

        @ApiModelProperty(value = "奖励类型")
        private RewardTypeEnum rewardType;

        @ApiModelProperty(value = "奖励值")
        private Integer totalReward;

        @ApiModelProperty(value = "等级称号")
        private String gradeTitle;

        @ApiModelProperty(value = "微信或支付宝昵称")
        private String nickName;

        @ApiModelProperty(value = "微信或支付宝头像")
        private String avatarUrl;
    }

    private static final long serialVersionUID = 1L;
}
