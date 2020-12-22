package com.aquilaflycloud.mdc.result.member;

import com.aquilaflycloud.mdc.model.member.MemberEventLog;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * MemberEventLogResult
 *
 * @author star
 * @date 2019-11-19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MemberEventLogResult extends MemberEventLog implements Serializable {
    private Integer countNum;
}
