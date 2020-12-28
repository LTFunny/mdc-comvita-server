package com.aquilaflycloud.mdc.result.member;

import com.aquilaflycloud.mdc.model.member.MemberGrade;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * MemberGradeResult
 *
 * @author star
 * @date 2020-03-05
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MemberGradeResult extends MemberGrade implements Serializable {
    private Long memberCount;
}
