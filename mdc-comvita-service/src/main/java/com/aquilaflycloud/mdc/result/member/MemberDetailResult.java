package com.aquilaflycloud.mdc.result.member;

import com.aquilaflycloud.mdc.model.member.MemberInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * MemberDetailResult
 *
 * @author star
 * @date 2019-12-17
 */
@Data
public class MemberDetailResult implements Serializable {
    @ApiModelProperty(value = "会员详情信息")
    private List<MemberDetail> memberDetailList;

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class MemberDetail extends MemberInfo implements Serializable {
        @ApiModelProperty(value = "微信授权号名称")
        private String appName;

        @ApiModelProperty(value = "支付宝授权号名称")
        private String aliAppName;

        @ApiModelProperty(value = "会员兴趣标签")
        private String tagNames;

        @ApiModelProperty(value = "会员手机型号")
        private String model;

        @ApiModelProperty(value = "会员成长值")
        private Integer growthValue;

        @ApiModelProperty(value = "会员等级")
        private String gradeTitle;
    }
}
