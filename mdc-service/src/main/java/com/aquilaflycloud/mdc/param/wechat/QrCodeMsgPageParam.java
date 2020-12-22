package com.aquilaflycloud.mdc.param.wechat;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.wechat.ExpireTypeEnum;
import com.aquilaflycloud.mdc.enums.wechat.QrcodeMsgTypeEnum;
import com.aquilaflycloud.mdc.model.wechat.WechatAuthorSiteQrcodeMsg;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class QrCodeMsgPageParam extends PageParam<WechatAuthorSiteQrcodeMsg> {
    @ApiModelProperty(value = "微信appId")
    private String appId;

    @ApiModelProperty(value = "二维码自定义参数")
    private String sceneString;

    @ApiModelProperty(value = "二维码时效类型(wechat.ExpireTypeEnum)")
    private ExpireTypeEnum expireType;

    @ApiModelProperty(value = "发送消息类型(wechat.QrcodeMsgTypeEnum)")
    private QrcodeMsgTypeEnum msgType;
}
