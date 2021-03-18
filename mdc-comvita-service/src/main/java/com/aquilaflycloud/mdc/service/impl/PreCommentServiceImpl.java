package com.aquilaflycloud.mdc.service.impl;

import com.aquilaflycloud.mdc.enums.pre.ActivityCommentStateEnum;
import com.aquilaflycloud.mdc.enums.pre.ActivityCommentViewStateEnum;
import com.aquilaflycloud.mdc.mapper.PreCommentInfoMapper;
import com.aquilaflycloud.mdc.model.pre.PreCommentInfo;
import com.aquilaflycloud.mdc.model.pre.PreFlashOrderInfo;
import com.aquilaflycloud.mdc.param.pre.CommentDetailsParam;
import com.aquilaflycloud.mdc.param.pre.CommentPageParam;
import com.aquilaflycloud.mdc.param.pre.CommentParam;
import com.aquilaflycloud.mdc.result.member.MemberInfoResult;
import com.aquilaflycloud.mdc.service.PreCommentService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @Author zly
 */
@Slf4j
@Service
public class PreCommentServiceImpl implements PreCommentService {
    @Resource
    private PreCommentInfoMapper preCommentInfoMapper;
    @Override
    @Transactional
    public void commentInfo(CommentParam param) {
        MemberInfoResult infoResult = MdcUtil.getRequireCurrentMember();
        PreCommentInfo preCommentInfo=preCommentInfoMapper.selectOne(Wrappers.<PreCommentInfo>lambdaQuery()
                .eq(PreCommentInfo::getActivityId,param.getActivityId())
                .eq(PreCommentInfo::getCommentatorId,infoResult.getId()));

        if(preCommentInfo!=null){
            throw new ServiceException("已点评该活动");
        }
        preCommentInfo =new PreCommentInfo();
        preCommentInfo.setCommentatorId(infoResult.getId());
        preCommentInfo.setCommentator(infoResult.getRealName());
        preCommentInfo.setActivityId(param.getActivityId());
        preCommentInfo.setActivityName(param.getActivityName());
        preCommentInfo.setComContent(param.getComContent());
        preCommentInfo.setComPicture(param.getComPicture());
        preCommentInfo.setComState(ActivityCommentStateEnum.WAITING);
        preCommentInfo.setComViewState(ActivityCommentViewStateEnum.HIDE);
        int preComment =  preCommentInfoMapper.insert(preCommentInfo);
        if(preComment < 0){
            throw new ServiceException("生成点评失败。");
        }
    }

    @Override
    public IPage<PreCommentInfo> pageComment(CommentPageParam param) {
        MemberInfoResult infoResult = MdcUtil.getRequireCurrentMember();
        return preCommentInfoMapper.selectPage(param.page(), Wrappers.<PreCommentInfo>lambdaQuery()
                .eq(PreCommentInfo::getCommentatorId, infoResult.getId())
                .eq(PreCommentInfo::getComState, param.getComState())
        );
    }

    @Override
    public PreCommentInfo detailsComment(CommentDetailsParam param) {
        return preCommentInfoMapper.selectById(param.getId());
    }
}
