package com.aquilaflycloud.mdc.result.wechat;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MiniMemberInfo {
    private String appId;
    private String openId;
}
