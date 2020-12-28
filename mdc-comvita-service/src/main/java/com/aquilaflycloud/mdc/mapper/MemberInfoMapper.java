package com.aquilaflycloud.mdc.mapper;

import com.aquilaflycloud.dataAuth.component.AfcBaseMapper;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface MemberInfoMapper extends AfcBaseMapper<MemberInfo> {

    @InterceptorIgnore(tenantLine = "true")
    @Select("select * from (select * from member_info group by create_time desc) a " +
            "where phone_number is not null and app_id is null and ali_app_id is null group by phone_number")
    List<MemberInfo> selectPhoneMember();

    @InterceptorIgnore(tenantLine = "true")
    @Select("select * from (select * from member_info group by create_time desc) a " +
            "where phone_number is not null and app_id is not null and union_id is not null and ali_app_id is null group by phone_number")
    List<MemberInfo> selectPhoneUnionIdMember();

    @InterceptorIgnore(tenantLine = "true")
    @Select("select * from (select * from member_info group by create_time desc) a " +
            "where phone_number is not null and app_id is null and ali_app_id is not null group by phone_number")
    List<MemberInfo> selectPhoneUserIdMember();

    @InterceptorIgnore(tenantLine = "true")
    @Select("select * from (select * from member_info group by create_time desc) a " +
            "where phone_number is null and app_id is not null and union_id is not null and ali_app_id is null group by union_id")
    List<MemberInfo> selectUnionIdMember();

    @InterceptorIgnore(tenantLine = "true")
    @Select("select * from (select * from member_info group by create_time desc) a " +
            "where phone_number is null and app_id is null and ali_app_id is not null group by user_id")
    List<MemberInfo> selectUserIdMember();

    @Select("SELECT ifnull(sum(case when ISNULL(birthday) then 1 else 0 end),0) as '未知', ifnull(sum(case when TIMESTAMPDIFF(YEAR, birthday, CURDATE())<17 then 1 else 0 end),0) as '17岁以下', ifnull(sum(case when TIMESTAMPDIFF(YEAR, birthday, CURDATE()) BETWEEN 17 and 24 then 1 else 0 end),0) as '18-24岁', ifnull(sum(case when TIMESTAMPDIFF(YEAR, birthday, CURDATE()) BETWEEN 25 and 29 then 1 else 0 end),0) as '25-29岁', ifnull(sum(case when TIMESTAMPDIFF(YEAR, birthday, CURDATE()) BETWEEN 30 and 39 then 1 else 0 end),0) as '30-39岁', ifnull(sum(case when TIMESTAMPDIFF(YEAR, birthday, CURDATE()) BETWEEN 40 and 49 then 1 else 0 end),0) as '40-49岁', ifnull(sum(case when TIMESTAMPDIFF(YEAR, birthday, CURDATE())>50 then 1 else 0 end),0) as '50岁以上' FROM member_info where app_id = #{appId} or ali_app_id=#{appId} AND is_auth='1' ")
    Map<String, BigDecimal> selectAgeCount(String appId);
}
