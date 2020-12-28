package com.aquilaflycloud.mdc.param.ticket;

import com.aquilaflycloud.dataAuth.common.AuthParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ChannelInfoListParam extends AuthParam implements Serializable {
    private static final long serialVersionUID = -152819013022367781L;
}
