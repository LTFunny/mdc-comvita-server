package com.aquilaflycloud.mdc.result.member;

import com.aquilaflycloud.mdc.model.member.MemberInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * MemberInfoResult
 *
 * @author star
 * @date 2019-10-29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MemberInfoResult extends MemberInfo implements Serializable {
    private String sessionKey;
}
