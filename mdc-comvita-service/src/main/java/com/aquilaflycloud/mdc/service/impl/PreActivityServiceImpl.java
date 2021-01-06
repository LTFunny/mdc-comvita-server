package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import com.aquilaflycloud.mdc.enums.member.BusinessTypeEnum;
import com.aquilaflycloud.mdc.mapper.FolksonomyBusinessRelMapper;
import com.aquilaflycloud.mdc.mapper.PreActivityInfoMapper;
import com.aquilaflycloud.mdc.mapper.PreGoodsInfoMapper;
import com.aquilaflycloud.mdc.mapper.PreRuleInfoMapper;
import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyBusinessRel;
import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyInfo;
import com.aquilaflycloud.mdc.model.pre.PreActivityInfo;
import com.aquilaflycloud.mdc.model.pre.PreGoodsInfo;
import com.aquilaflycloud.mdc.model.pre.PreRuleInfo;
import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.pre.PreActivityAnalysisResult;
import com.aquilaflycloud.mdc.result.pre.PreActivityDetailResult;
import com.aquilaflycloud.mdc.service.FolksonomyService;
import com.aquilaflycloud.mdc.service.PreActivityService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * PreActivityServiceImpl
 * @author linkq
 */
@Slf4j
@Service
public class PreActivityServiceImpl implements PreActivityService {

    @Resource
    private PreActivityInfoMapper preActivityInfoMapper;

    @Resource
    private PreRuleInfoMapper preRuleInfoMapper;

    @Resource
    private PreGoodsInfoMapper preGoodsInfoMapper;

    @Resource
    private FolksonomyBusinessRelMapper folksonomyBusinessRelMapper;

    @Resource
    private FolksonomyService folksonomyService;

    @Override
    public IPage<PreActivityInfo> page(PreActivityPageParam param) {
        List<Long> folksonomyIds = param.getFolksonomyIds();
        List<Long> businessIds = new ArrayList<>();
        if(CollUtil.isNotEmpty(folksonomyIds)){
            QueryWrapper<FolksonomyBusinessRel> qw = new QueryWrapper<>();
            qw.in("folksonomy_id", folksonomyIds);
            List<FolksonomyBusinessRel> folksonomyBusinessRels = folksonomyBusinessRelMapper.selectList(qw);
            if(CollUtil.isNotEmpty(folksonomyBusinessRels)){
                folksonomyBusinessRels.forEach(f -> {
                    businessIds.add(f.getBusinessId());
                });
            }
        }

        IPage<PreActivityInfo> list = preActivityInfoMapper.selectPage(param.page(), Wrappers.<PreActivityInfo>lambdaQuery()
                .like( param.getActivityName()!=null,PreActivityInfo::getActivityName, param.getActivityName())
                .in(CollUtil.isNotEmpty(businessIds),PreActivityInfo::getId,businessIds)
                .eq( param.getActivityState()!=null,PreActivityInfo::getActivityState, param.getActivityState().getType())
                .eq( param.getActivityType()!=null,PreActivityInfo::getActivityType, param.getActivityType().getType())
                .apply(param.getCreateTimeStart() != null,"date_format (optime,'%Y-%m-%d') >= date_format('" + param.getCreateTimeStart() + "','%Y-%m-%d')")
                .apply(param.getCreateTimeEnd() != null,"date_format (optime,'%Y-%m-%d') <= date_format('" + param.getCreateTimeEnd() + "','%Y-%m-%d')")
        );

        return list;
    }

    @Override
    public void add(PreActivityAddParam param) {
        checkNameParam(param.getActivityName());
        checkTimeParam(param.getBeginTime(),param.getEndTime());

        PreActivityInfo activityInfo = new PreActivityInfo();
        BeanUtil.copyProperties(param, activityInfo);
        activityInfo.setId(MdcUtil.getSnowflakeId());
        int count = preActivityInfoMapper.insert(activityInfo);
        if (count == 1) {
            folksonomyService.saveFolksonomyBusinessRel(BusinessTypeEnum.PREACTIVITY, activityInfo.getId(), param.getFolksonomyIds());
            log.info("新增活动成功");
        } else {
            throw new ServiceException("新增活动失败");
        }
    }

    private void checkTimeParam(Date beginTime, Date endTime) {
        //活动时间不得早于当前时间 开始时间不得迟于结束时间
        Date now = DateTime.now();
        if(beginTime.getTime() < now.getTime()){
            throw new ServiceException("活动开始时间不得早于当前时间");
        }
        if(endTime.getTime() < now.getTime()){
            throw new ServiceException("活动结束时间不得早于当前时间");
        }
        if(endTime.getTime() < beginTime.getTime()){
            throw new ServiceException("活动结束时间不得早于活动开始时间");
        }
    }

    private void checkNameParam(String activityName) {
        //活动名称不允许重复
        PreActivityInfo info =  preActivityInfoMapper.selectOne(Wrappers.<PreActivityInfo>lambdaQuery()
                .eq(PreActivityInfo::getActivityName,activityName));
        if(null != info){
            throw new ServiceException("已存在相同名称的活动,名称为:" + info.getActivityName());
        }
    }

    @Override
    public void update(PreActivityUpdateParam param) {
        if(param.getId()==null) {
            throw new ServiceException("编辑的活动主键id为空" );
        }
        checkNameParam(param.getActivityName());
        checkTimeParam(param.getBeginTime(),param.getEndTime());
        PreActivityInfo activityInfo =  preActivityInfoMapper.selectById(param.getId());
        BeanUtil.copyProperties(param, activityInfo,"id");
        //@TODO
//        if(CollectionUtils.isEmpty(param.getFolksonomyIds())) {
//            String tagId=param.getFolksonomyIds().toString();
//            activityInfo.setFolksonomyId(tagId);
//        }
        preActivityInfoMapper.updateById(activityInfo);
        log.info("编辑活动信息成功");
        //保存业务功能标签
        folksonomyService.saveFolksonomyBusinessRel(BusinessTypeEnum.PREACTIVITY, activityInfo.getId(), param.getFolksonomyIds());
    }

    /**
     * 获取活动详情
     * @param param
     * @return
     */
    @Override
    public PreActivityDetailResult get(PreActivityGetParam param) {
        if(param.getId()==null) {
            throw new ServiceException("下架的活动主键id为空" );
        }
        PreActivityInfo info=  preActivityInfoMapper.selectById(param.getId());
        PreActivityDetailResult preActivityDetailResult = new PreActivityDetailResult();
        BeanUtil.copyProperties(info, preActivityDetailResult);
        //refGoodsCode
        PreGoodsInfo goods = preGoodsInfoMapper.selectById(info.getRefGoods());
        if(null != goods){
            preActivityDetailResult.setRefGoodsCode(goods.getGoodsCode());
        }
        //refRuleName
        PreRuleInfo rule = preRuleInfoMapper.selectById(info.getRefRule());
        if(null != rule){
            preActivityDetailResult.setRefRuleName(rule.getRuleName());
        }
        // ksonomyIds
        List<FolksonomyInfo> folksonomyInfos = folksonomyService.getFolksonomyBusinessList(BusinessTypeEnum.PREACTIVITY, info.getId());
        if(CollUtil.isNotEmpty(folksonomyInfos)){
            List<String> names = new ArrayList<>();
            folksonomyInfos.forEach(s -> {
                names.add(s.getName());
            });
            preActivityDetailResult.setFolksonomyNames(names);
        }
        return preActivityDetailResult;
    }

    @Override
    public void cancel(PreActivityCancelParam param) {
        if(param.getId()==null) {
            throw new ServiceException("下架的活动主键id为空" );
        }
        PreActivityInfo activityInfo =  preActivityInfoMapper.selectById(param.getId());
        activityInfo.setActivityState(param.getActivityState().getType());
        preActivityInfoMapper.updateById(activityInfo);
    }

    @Override
    public PreActivityAnalysisResult analyse(PreActivityAnalysisParam param) {
        return null;
    }
}
