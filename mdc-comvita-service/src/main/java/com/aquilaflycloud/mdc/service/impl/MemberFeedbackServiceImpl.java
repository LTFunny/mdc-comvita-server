package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.aquilaflycloud.mdc.mapper.MemberFeedbackMapper;
import com.aquilaflycloud.mdc.model.member.MemberFeedbackInfo;
import com.aquilaflycloud.mdc.param.member.MemberFeedbackAddParam;
import com.aquilaflycloud.mdc.param.member.MemberFeedbackEditParam;
import com.aquilaflycloud.mdc.param.member.MemberFeedbackGetParam;
import com.aquilaflycloud.mdc.param.member.MemberFeedbackPageParam;
import com.aquilaflycloud.mdc.result.member.MemberInfoResult;
import com.aquilaflycloud.mdc.service.MemberFeedbackService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * MemberFeedbackServiceImpl
 *
 * @author zengqingjie
 * @date 2020-06-28
 */
@Service
public class MemberFeedbackServiceImpl implements MemberFeedbackService {
    @Resource
    private MemberFeedbackMapper memberFeedbackMapper;

    @Override
    public IPage<MemberFeedbackInfo> page(MemberFeedbackPageParam param) {
        return memberFeedbackMapper.selectPage(param.page(), Wrappers.<MemberFeedbackInfo> lambdaQuery()
                .like(StrUtil.isNotBlank(param.getNickName()), MemberFeedbackInfo::getNickName, param.getNickName())
                .ge(ObjectUtil.isNotNull(param.getCreateStartTime()), MemberFeedbackInfo::getCreateTime, param.getCreateStartTime())
                .le(ObjectUtil.isNotNull(param.getCreateEndTime()), MemberFeedbackInfo::getCreateTime, param.getCreateEndTime())
                .eq(ObjectUtil.isNotNull(param.getScore()), MemberFeedbackInfo::getScore, param.getScore())
                .orderByDesc(MemberFeedbackInfo::getCreateTime)
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        );

    }

    @Override
    public MemberFeedbackInfo get(MemberFeedbackGetParam param) {
        return memberFeedbackMapper.selectById(param.getId());
    }

    @Override
    public void edit(MemberFeedbackEditParam param) {
        MemberFeedbackInfo info = new MemberFeedbackInfo();
        BeanUtil.copyProperties(param, info);

        int count = memberFeedbackMapper.updateById(info);

        if (count <= 0) {
            throw new ServiceException("更新失败，请重试");
        }
    }

    @Override
    public void add(MemberFeedbackAddParam param) {
        MemberInfoResult memberInfo = MdcUtil.getRequireCurrentMember();
        MemberFeedbackInfo info = new MemberFeedbackInfo();

        //设置会员信息
        BeanUtil.copyProperties(param, info);
        MdcUtil.setMemberInfo(info, memberInfo);

        int count = memberFeedbackMapper.insert(info);

        if (count <= 0) {
            throw new ServiceException("保存失败，请重试");
        }
    }
}
