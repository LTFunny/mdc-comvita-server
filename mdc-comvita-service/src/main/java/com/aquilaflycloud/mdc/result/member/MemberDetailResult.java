package com.aquilaflycloud.mdc.result.member;

import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyInfo;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * MemberInfoResult
 *
 * @author star
 * @date 2021/1/11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MemberDetailResult extends MemberInfo implements Serializable {
    @ApiModelProperty(value = "微信授权号名称")
    private String appName;

    @ApiModelProperty(value = "会员兴趣标签")
    private List<FolksonomyInfo> folksonomyInfoList;

    @ApiModelProperty(value = "会员手机型号")
    private String model;

    @ApiModelProperty(value = "会员成长值")
    private Integer growthValue;

    @ApiModelProperty(value = "会员积分值")
    private Integer scoreValue;

    @ApiModelProperty(value = "会员等级")
    private String gradeTitle;
}
