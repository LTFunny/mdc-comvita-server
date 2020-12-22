package com.aquilaflycloud.mdc.param.member;

import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import com.aquilaflycloud.mdc.model.member.MemberScanRecord;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class MemberConsumptionTicketPageInfoParam extends PageAuthParam<MemberScanRecord> {
}
