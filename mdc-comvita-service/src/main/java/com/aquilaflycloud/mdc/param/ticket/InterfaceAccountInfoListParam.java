package com.aquilaflycloud.mdc.param.ticket;

import com.aquilaflycloud.dataAuth.common.AuthParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class InterfaceAccountInfoListParam extends AuthParam implements Serializable {
}
