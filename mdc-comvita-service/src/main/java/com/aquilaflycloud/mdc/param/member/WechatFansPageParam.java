package com.aquilaflycloud.mdc.param.member;

import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.member.SexEnum;
import com.aquilaflycloud.mdc.enums.member.SubscribeSceneEnum;
import com.aquilaflycloud.mdc.model.wechat.WechatFansInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class WechatFansPageParam extends PageAuthParam<WechatFansInfo> {
    @ApiModelProperty(value = "微信appId")
    private String appId;

    @ApiModelProperty(value = "微信昵称")
    private String nickName;

    @ApiModelProperty(value = "微信性别")
    private SexEnum gender;

    @ApiModelProperty(value = "微信国家")
    private String country;

    @ApiModelProperty(value = "微信省份")
    private String province;

    @ApiModelProperty(value = "微信城市")
    private String city;

    @ApiModelProperty(value = "取关时间开始")
    private Date unsubscribeTimeStart;

    @ApiModelProperty(value = "取关时间结束")
    private Date unsubscribeTimeEnd;

    @ApiModelProperty(value = "关注时间开始")
    private Date subscribeTimeStart;

    @ApiModelProperty(value = "关注时间结束")
    private Date subscribeTimeEnd;

    @ApiModelProperty(value = "关注渠道")
    private SubscribeSceneEnum subscribeScene;

    @ApiModelProperty(value = "是否关注")
    private WhetherEnum subscribeState;

}