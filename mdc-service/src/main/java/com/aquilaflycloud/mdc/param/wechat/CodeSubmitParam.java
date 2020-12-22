package com.aquilaflycloud.mdc.param.wechat;

import com.aquilaflycloud.common.NoneBlank;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import me.chanjar.weixin.open.bean.ma.WxOpenMaSubmitAudit;

import java.util.List;

@NoneBlank(fieldNames = {"appId", "id"}, message = "appId和id不能同时为空")
@Data
@Accessors(chain = true)
public class CodeSubmitParam {

    @ApiModelProperty(value = "小程序appId(id为空时必填)")
    private String appId;

    @ApiModelProperty(value = "小程序代码id(appId为空时必填)")
    private Long id;

    private List<WxOpenMaSubmitAudit> itemList;

    private String feedbackInfo;

    private String feedbackStuff;

}
