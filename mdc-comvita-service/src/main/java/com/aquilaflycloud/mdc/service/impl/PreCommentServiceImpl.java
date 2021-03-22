package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aquilaflycloud.auth.bean.User;
import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.member.BusinessTypeEnum;
import com.aquilaflycloud.mdc.enums.member.InteractionBusinessTypeEnum;
import com.aquilaflycloud.mdc.enums.member.InteractionTypeEnum;
import com.aquilaflycloud.mdc.enums.pre.ActivityCommentStateEnum;
import com.aquilaflycloud.mdc.enums.pre.ActivityCommentViewStateEnum;
import com.aquilaflycloud.mdc.mapper.*;
import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyBusinessRel;
import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyInfo;
import com.aquilaflycloud.mdc.model.member.MemberInteraction;
import com.aquilaflycloud.mdc.model.pre.PreActivityInfo;
import com.aquilaflycloud.mdc.model.pre.PreCommentInfo;
import com.aquilaflycloud.mdc.param.member.MemberInteractionParam;
import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.member.MemberInfoResult;
import com.aquilaflycloud.mdc.result.pre.PreCommentInfoResult;
import com.aquilaflycloud.mdc.result.pre.PreCommentPageResult;
import com.aquilaflycloud.mdc.service.FolksonomyService;
import com.aquilaflycloud.mdc.service.MemberInteractionService;
import com.aquilaflycloud.mdc.service.PreCommentService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author zly
 */
@Slf4j
@Service
public class PreCommentServiceImpl implements PreCommentService {
    @Resource
    private PreCommentInfoMapper preCommentInfoMapper;
    @Resource
    private MemberInteractionMapper memberInteractionMapper;
    @Resource
    private MemberInteractionService memberInteractionService;
    @Resource
    private FolksonomyService folksonomyService;
    @Resource
    private FolksonomyBusinessRelMapper folksonomyBusinessRelMapper;
    @Resource
    private PreActivityInfoMapper preActivityInfoMapper;
    @Override
    @Transactional
    public void commentInfo(CommentParam param) {
        MemberInfoResult infoResult = MdcUtil.getRequireCurrentMember();
     /*   PreCommentInfo preCommentInfo=preCommentInfoMapper.selectOne(Wrappers.<PreCommentInfo>lambdaQuery()
                .eq(PreCommentInfo::getActivityId,param.getActivityId())
                .eq(PreCommentInfo::getCommentatorId,infoResult.getId()));

        if(preCommentInfo!=null){
            throw new ServiceException("已点评该活动");
        }*/
        PreCommentInfo  preCommentInfo =new PreCommentInfo();
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
                //回复记录的父记录id 为空表示开始的第一条点评 否则即回复内容
                .eq(PreCommentInfo::getParentId, null)
        );
    }

    @Override
    public PreCommentInfoResult detailsComment(CommentDetailsParam param) {
        PreCommentInfo info = preCommentInfoMapper.selectById(param.getId());
        PreCommentInfoResult result = BeanUtil.copyProperties(info, PreCommentInfoResult.class);
        result.setLikeNum(memberInteractionService.getInteractionNum(new MemberInteractionParam().setBusinessId(result.getId())
                .setBusinessType(InteractionBusinessTypeEnum.COMMENT).setInteractionType(InteractionTypeEnum.LIKE)));
        PreActivityInfo preActivityInfo=preActivityInfoMapper.selectById(info.getActivityId());
        if(preActivityInfo!=null){
            result.setActivityPicture(preActivityInfo.getActivityPicture());
        }else{
            throw new ServiceException("活动信息查询有误");
        }
        return result;
    }

    @Override
    public IPage<PreCommentInfo> pageLikeComment(PageParam<MemberInteraction> param) {
        Long memberId = MdcUtil.getRequireCurrentMemberId();
        return memberInteractionMapper.selectInteractionCommentPage(param.page(), Wrappers.<MemberInteraction>lambdaQuery()
                .eq(MemberInteraction::getBusinessType, InteractionBusinessTypeEnum.COMMENT)
                .eq(MemberInteraction::getInteractionType, InteractionTypeEnum.LIKE)
                .eq(MemberInteraction::getIsCancel, WhetherEnum.NO)
                .eq(MemberInteraction::getMemberId, memberId)
                .orderByDesc(MemberInteraction::getInteractionTime)
        );
    }

    @Override
    public void changeViewState(PreCommentChangViewStateParam param) {
        if(param.getId()==null) {
            throw new ServiceException("活动点评主键id为空" );
        }
        PreCommentInfo preCommentInfo =  preCommentInfoMapper.selectById(param.getId());
        if(ActivityCommentViewStateEnum.HIDE == preCommentInfo.getComViewState()){
            preCommentInfo.setComViewState(ActivityCommentViewStateEnum.OPEN);
        }else if(ActivityCommentViewStateEnum.OPEN == preCommentInfo.getComViewState()){
            preCommentInfo.setComViewState(ActivityCommentViewStateEnum.HIDE);
        }
        int count = preCommentInfoMapper.updateById(preCommentInfo);
        if (count <= 0) {
            throw new ServiceException("改变展示状态(隐藏/公开)失败");
        }
        log.info("改变展示状态完成");
    }

    /**
     * 审核
     * 通过：记录点评的标签信息，点评内容公开，所有会员均可查看
     * 不通过：点评内容隐藏，点评者可查看，其他会员不可查看
     * 审核反馈：若审核结果为不通过，则该项为必填
     * @param param
     */
    @Transactional
    @Override
    public void audit(PreCommentAuditParam param) {
        if(param.getId()==null) {
            throw new ServiceException("活动点评主键id为空" );
        }
        folksonomyService.saveFolksonomyBusinessRel(BusinessTypeEnum.ACTIVITYCOMMENT, param.getId(), param.getFolksonomyIds());
        log.info("处理标签完成");
        PreCommentInfo preCommentInfo =  preCommentInfoMapper.selectById(param.getId());
        User user = MdcUtil.getCurrentUser();
        preCommentInfo.setAuditor(user.getUsername());
        preCommentInfo.setAuditorId(user.getId());
        if(ActivityCommentStateEnum.PASS == param.getAuditOperateType()){
            preCommentInfo.setAuditRemark(param.getAuditFeedback());
            preCommentInfo.setComViewState(ActivityCommentViewStateEnum.OPEN);
            preCommentInfo.setComState(ActivityCommentStateEnum.PASS);
        }else if(ActivityCommentStateEnum.NO_PASS == param.getAuditOperateType()){
            if(StrUtil.isBlank(param.getAuditFeedback())){
                throw new ServiceException("请填写审核反馈");
            }
            preCommentInfo.setAuditRemark(param.getAuditFeedback());
            preCommentInfo.setComViewState(ActivityCommentViewStateEnum.HIDE);
            preCommentInfo.setComState(ActivityCommentStateEnum.NO_PASS);
        }else{
            throw new ServiceException("无效的参数" );
        }

        int count = preCommentInfoMapper.updateById(preCommentInfo);
        if (count <= 0) {
            throw new ServiceException("处理审核点评失败");
        }
        log.info("处理审核点评完成");
    }

    @Override
    public PreCommentPageResult get(PreCommentGetParam param) {
        if(param.getId()==null) {
            throw new ServiceException("活动点评主键id为空" );
        }
        PreCommentInfo preCommentInfo =  preCommentInfoMapper.selectById(param.getId());
        return dataConvertResult(preCommentInfo);
    }

    /**
     * 数据转换
     * @param preCommentInfo
     * @return
     */
    private PreCommentPageResult dataConvertResult(PreCommentInfo preCommentInfo) {
        if (null != preCommentInfo) {
            PreCommentPageResult result = new PreCommentPageResult();
            BeanUtil.copyProperties(preCommentInfo, result);
            List<FolksonomyInfo> folksonomys = folksonomyService.getFolksonomyBusinessList(BusinessTypeEnum.ACTIVITYCOMMENT, preCommentInfo.getId());
            StringBuffer sb = new StringBuffer();
            if(CollUtil.isNotEmpty(folksonomys)){
                for(int i = 0 ; i < folksonomys.size() ; i++){
                    sb.append(folksonomys.get(i).getName());
                    if(i != folksonomys.size() - 1 ){
                        sb.append(",");
                    }
                }
            }
            result.setFolksonomyNames(sb.toString());
            result.setComLikeCount(memberInteractionService.getInteractionNum(new MemberInteractionParam().setBusinessId(result.getId())
                    .setBusinessType(InteractionBusinessTypeEnum.COMMENT).setInteractionType(InteractionTypeEnum.LIKE)));
            result.setCommentReplyList(getCommentReply(result.getId()));
            return result;
        } else {
            return null;
        }
    }

    /**
     * 获取点评回复
     * @param commentId
     * @return
     */
    private List<PreCommentPageResult.CommentReply> getCommentReply(Long commentId) {
        List<PreCommentPageResult.CommentReply> result = new ArrayList<>();
        QueryWrapper<PreCommentInfo> qw = new QueryWrapper<>();
        qw.eq("parent_id", commentId);
        qw.orderByDesc("create_time");
        List<PreCommentInfo> preCommentInfos = preCommentInfoMapper.selectList(qw);
        if(CollUtil.isNotEmpty(preCommentInfos)){
            preCommentInfos.forEach(f -> {
                PreCommentPageResult.CommentReply commentReply = new PreCommentPageResult.CommentReply();
                commentReply.setReplyId(f.getId());
                commentReply.setContent(f.getComContent());
                commentReply.setPicture(f.getComPicture());
                commentReply.setReplier(f.getCommentator());
                commentReply.setReplyTime(f.getCreateTime());
                result.add(commentReply);
            });
        }
        return result;
    }

    @Override
    public IPage<PreCommentPageResult> page(PreCommentPageParam param) {
        List<Long> businessIds = getFolksonomyBusinessRels(param.getFolksonomyIds());
        //选中标签 但是没有找到关联的活动点评时 返回空
        if(CollUtil.isNotEmpty(param.getFolksonomyIds()) && CollUtil.isEmpty(businessIds)){
            return null;
        }
        return preCommentInfoMapper.selectPage(param.page(), Wrappers.<PreCommentInfo>lambdaQuery()
                .in(CollUtil.isNotEmpty(businessIds),PreCommentInfo::getId,businessIds)
                .eq(param.getComState() != null, PreCommentInfo::getComState, param.getComState())
                .like(StrUtil.isNotBlank(param.getCommentator()), PreCommentInfo::getCommentator, param.getCommentator())
                .like(StrUtil.isNotBlank(param.getAuditor()), PreCommentInfo::getAuditor, param.getAuditor())
                .like(StrUtil.isNotBlank(param.getActivityName()), PreCommentInfo::getActivityName, param.getActivityName())
                .eq(param.getComViewState() != null, PreCommentInfo::getComViewState, param.getComViewState())
                .ge(param.getCommentStartTime() != null, PreCommentInfo::getCreateTime, param.getCommentStartTime())
                .le(param.getCommentEndTime() != null, PreCommentInfo::getCreateTime, param.getCommentEndTime())
                .ge(param.getAuditStartTime() != null, PreCommentInfo::getLastUpdateTime, param.getAuditStartTime())
                .le(param.getAuditEndTime() != null, PreCommentInfo::getLastUpdateTime, param.getAuditEndTime())
                //回复记录的父记录id为空表示开始的第一条点评 有值即为回复
                .eq(PreCommentInfo::getParentId,null)
                .orderByDesc(PreCommentInfo::getCreateTime)
        ).convert(this::dataConvertResult);
    }

    @Override
    public void reply(PreCommentReplyParam param) {
        if(param.getCommentId()==null) {
            throw new ServiceException("活动点评主键id为空" );
        }
        PreCommentInfo  preCommentInfo =new PreCommentInfo();
        preCommentInfo.setParentId(param.getCommentId());
        User user = MdcUtil.getCurrentUser();
        preCommentInfo.setCommentatorId(user.getId());
        preCommentInfo.setCommentator(user.getUsername());
        preCommentInfo.setActivityId(param.getActivityId());
        preCommentInfo.setActivityName(param.getActivityName());
        preCommentInfo.setComContent(param.getContent());
        preCommentInfo.setComPicture(param.getPicture());
        int preComment =  preCommentInfoMapper.insert(preCommentInfo);
        if(preComment < 0){
            throw new ServiceException("点评回复失败");
        }
        log.info("回复点评完成");
    }

    private List<Long> getFolksonomyBusinessRels(List<Long> folksonomyIds) {
        if(CollUtil.isEmpty(folksonomyIds)){
            return null;
        }
        Set<Long> businessIds = new HashSet<>();
        //先获取标签关联的业务id
        if(CollUtil.isNotEmpty(folksonomyIds)){
            QueryWrapper<FolksonomyBusinessRel> qw = new QueryWrapper<>();
            qw.in("folksonomy_id", folksonomyIds);
            qw.eq("business_type",BusinessTypeEnum.ACTIVITYCOMMENT.getType());
            List<FolksonomyBusinessRel> folksonomyBusinessRels = folksonomyBusinessRelMapper.selectList(qw);
            if(CollUtil.isNotEmpty(folksonomyBusinessRels)){
                folksonomyBusinessRels.forEach(f -> {
                    businessIds.add(f.getBusinessId());
                });
            }
        }
        return new ArrayList<>(businessIds);
    }
}
