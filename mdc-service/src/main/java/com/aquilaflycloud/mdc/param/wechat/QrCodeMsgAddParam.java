package com.aquilaflycloud.mdc.param.wechat;

import com.aquilaflycloud.common.AnotherFieldHasValue;
import com.aquilaflycloud.mdc.enums.wechat.ExpireTypeEnum;
import com.aquilaflycloud.mdc.enums.wechat.QrcodeMsgTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@AnotherFieldHasValue.List({
        @AnotherFieldHasValue(fieldName = "expireType", fieldValue = "TEMPORARY", notNullFieldName = "expireSeconds", message = "二维码有效时间不能为空"),
        @AnotherFieldHasValue(fieldName = "msgType", fieldValue = "TEXT", notNullFieldName = "msgText", message = "文本消息不能为空"),
        @AnotherFieldHasValue(fieldName = "msgType", fieldValue = "IMAGE", notNullFieldName = "msgImage", message = "图片消息不能为空"),
        @AnotherFieldHasValue(fieldName = "msgType", fieldValue = "VOICE", notNullFieldName = "msgVoice", message = "语音消息不能为空"),
        @AnotherFieldHasValue(fieldName = "msgType", fieldValue = "VIDEO", notNullFieldName = "msgVideo", message = "视频消息不能为空"),
        @AnotherFieldHasValue(fieldName = "msgType", fieldValue = "MUSIC", notNullFieldName = "msgMusic", message = "音乐消息不能为空"),
        @AnotherFieldHasValue(fieldName = "msgType", fieldValue = "NEWS", notNullFieldName = "msgNewsList", message = "图文消息不能为空"),
})
@Data
@Accessors(chain = true)
public class QrCodeMsgAddParam {
    @ApiModelProperty(value = "微信appId", required = true)
    @NotBlank(message = "微信appId不能为空")
    private String appId;

    @ApiModelProperty(value = "二维码自定义参数", required = true)
    @NotBlank(message = "自定义参数不能为空")
    private String sceneString;

    @ApiModelProperty(value = "二维码时效类型(wechat.ExpireTypeEnum)", required = true)
    @NotNull(message = "时效类型不能为空")
    private ExpireTypeEnum expireType;

    @ApiModelProperty(value = "二维码有效时间(秒)")
    private Integer expireSeconds;

    @ApiModelProperty(value = "发送消息类型(wechat.QrcodeMsgTypeEnum)", required = true)
    @NotNull(message = "消息类型不能为空")
    private QrcodeMsgTypeEnum msgType;

    @ApiModelProperty(value = "文本消息")
    @Valid
    private QrcodeMsgTextParam msgText;

    @ApiModelProperty(value = "图片消息")
    @Valid
    private QrcodeMsgImageParam msgImage;

    @ApiModelProperty(value = "语音消息")
    @Valid
    private QrcodeMsgVoiceParam msgVoice;

    @ApiModelProperty(value = "视频消息")
    @Valid
    private QrcodeMsgVideoParam msgVideo;

    @ApiModelProperty(value = "音乐消息")
    @Valid
    private QrcodeMsgMusicParam msgMusic;

    @ApiModelProperty(value = "图文消息")
    @Valid
    private List<QrcodeMsgNewsParam> msgNewsList;
}
