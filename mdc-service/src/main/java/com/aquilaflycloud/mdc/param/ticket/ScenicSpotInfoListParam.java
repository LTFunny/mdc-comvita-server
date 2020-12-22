package com.aquilaflycloud.mdc.param.ticket;

import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import com.aquilaflycloud.mdc.model.ticket.TicketScenicSpotInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * ScenicSpotInfoListParam 获取列表数据
 *
 * @author Zengqingjie
 * @date 2019-11-18
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ScenicSpotInfoListParam extends PageAuthParam<TicketScenicSpotInfo> implements Serializable {

    private static final long serialVersionUID = 4707615718557432450L;
}
