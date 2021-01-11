package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.mdc.enums.member.BusinessTypeEnum;
import com.aquilaflycloud.mdc.enums.pre.ActivityTypeEnum;
import com.aquilaflycloud.mdc.mapper.*;
import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyBusinessRel;
import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyInfo;
import com.aquilaflycloud.mdc.model.pre.PreActivityInfo;
import com.aquilaflycloud.mdc.model.pre.PreGoodsInfo;
import com.aquilaflycloud.mdc.model.pre.PreOrderInfo;
import com.aquilaflycloud.mdc.model.pre.PreRuleInfo;
import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.pre.*;
import com.aquilaflycloud.mdc.service.FolksonomyService;
import com.aquilaflycloud.mdc.service.PreActivityService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

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
    private PreOrderInfoMapper preOrderInfoMapper;

    @Resource
    private FolksonomyBusinessRelMapper folksonomyBusinessRelMapper;

    @Resource
    private FolksonomyService folksonomyService;

    @Override
    public IPage<PreActivityPageResult> pagePreActivity(PreActivityPageParam param) {
        return preActivityInfoMapper.selectPage(param.page(), Wrappers.<PreActivityInfo>lambdaQuery()
                .eq( PreActivityInfo::getActivityType, ActivityTypeEnum.PRE_SALES)
        ).convert(apply -> {
            PreActivityPageResult result = new PreActivityPageResult();
            BeanUtil.copyProperties(apply, result);
            result.setRefGoodsCode(getGoodsCode(apply.getRefGoods()));
            result.setFolksonomyIds(getFolksonomys(apply.getId()));
            if(StrUtil.isNotBlank(apply.getRewardRuleContent())){
                result.setRewardRuleList(JSONUtil.toList(JSONUtil.parseArray(apply.getRewardRuleContent()), PreActivityRewardParam.class));
            }
            return result;
        });
    }

    @Override
    public IPage<PreActivityPageResult> page(PreActivityPageParam param) {
        List<Long> businessIds = getFolksonomyBusinessRels(param.getFolksonomyIds());
        return preActivityInfoMapper.selectPage(param.page(), Wrappers.<PreActivityInfo>lambdaQuery()
                .like( param.getActivityName()!=null,PreActivityInfo::getActivityName, param.getActivityName())
                .in(CollUtil.isNotEmpty(businessIds),PreActivityInfo::getId,businessIds)
                .eq( param.getActivityState()!=null,
                        PreActivityInfo::getActivityState,
                        param.getActivityState())
                .eq( param.getActivityType()!=null,
                        PreActivityInfo::getActivityType,
                        param.getActivityType())
                .apply(param.getCreateTimeStart() != null,
                        "date_format (optime,'%Y-%m-%d') >= date_format('" + param.getCreateTimeStart() + "','%Y-%m-%d')")
                .apply(param.getCreateTimeEnd() != null,
                        "date_format (optime,'%Y-%m-%d') <= date_format('" + param.getCreateTimeEnd() + "','%Y-%m-%d')")
        ).convert(apply -> {
            PreActivityPageResult result = new PreActivityPageResult();
            BeanUtil.copyProperties(apply, result);
            result.setRefGoodsCode(getGoodsCode(apply.getRefGoods()));
            result.setFolksonomyIds(getFolksonomys(apply.getId()));
            if(StrUtil.isNotBlank(apply.getRewardRuleContent())){
                result.setRewardRuleList(JSONUtil.toList(JSONUtil.parseArray(apply.getRewardRuleContent()), PreActivityRewardParam.class));
            }
            return result;
        });
    }

    private List<PreActivityFolksonomyResult> getFolksonomys(Long business_id) {
        List<PreActivityFolksonomyResult> results = new ArrayList<>();
        List<Map<String, Object>> list = preActivityInfoMapper.getFolksonomy(business_id);
        if(CollUtil.isNotEmpty(list)){
            list.forEach(l -> {
                Long id = (Long) l.get("id");
                String name = (String) l.get("name");
                PreActivityFolksonomyResult p = new PreActivityFolksonomyResult();
                p.setFolksonomyId(id);
                p.setFolksonomyName(name);
                results.add(p);
            });
        }
        return results;
    }

    private String getGoodsCode(Long refGoods) {
        String code = "";
        PreGoodsInfo goods = preGoodsInfoMapper.selectById(refGoods);
        if(null != goods){
            code = goods.getGoodsCode();
        }
        return code;
    }

    /**
     * 获取关联的标签
     * @param folksonomyIds
     * @return
     */
    private List<Long> getFolksonomyBusinessRels(List<Long> folksonomyIds) {
        if(CollUtil.isEmpty(folksonomyIds)){
            return null;
        }
        List<Long> businessIds = new ArrayList<>();
        //先获取标签关联的业务id
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
        return businessIds;
    }

    @Transactional
    @Override
    public void add(PreActivityAddParam param) {
        checkNameParam(param.getActivityName());
        checkTimeParam(param.getBeginTime(),param.getEndTime());

        PreActivityInfo activityInfo = new PreActivityInfo();
        BeanUtil.copyProperties(param, activityInfo);
        activityInfo.setId(MdcUtil.getSnowflakeId());
        activityInfo.setRewardRuleContent(JSONUtil.toJsonStr(param.getRewardRuleList()));
        int count = preActivityInfoMapper.insert(activityInfo);
        if (count == 1) {
            log.info("新增活动成功");
            folksonomyService.saveFolksonomyBusinessRel(BusinessTypeEnum.PREACTIVITY, activityInfo.getId(), param.getFolksonomyIds());
            log.info("处理标签成功");
        } else {
            throw new ServiceException("新增活动失败");
        }
    }

    /**
     * 时间有效性校验
     * 活动时间不得早于当前时间 开始时间不得迟于结束时间
     * @param beginTime
     * @param endTime
     */
    private void checkTimeParam(Date beginTime, Date endTime) {
//        Date now = DateTime.now();
//        if(beginTime.getTime() < now.getTime()){
//            throw new ServiceException("活动开始时间不得早于当前时间");
//        }
//        if(endTime.getTime() < now.getTime()){
//            throw new ServiceException("活动结束时间不得早于当前时间");
//        }
        if(endTime.getTime() < beginTime.getTime()){
            throw new ServiceException("活动结束时间不得早于活动开始时间");
        }
    }

    /**
     * 重名校验
     * 活动名称不允许重复
     * @param activityName
     */
    private void checkNameParam(String activityName) {
        PreActivityInfo info =  preActivityInfoMapper.selectOne(Wrappers.<PreActivityInfo>lambdaQuery()
                .eq(PreActivityInfo::getActivityName,activityName));
        if(null != info){
            throw new ServiceException("已存在相同名称的活动,名称为:" + info.getActivityName());
        }
    }

    @Transactional
    @Override
    public void update(PreActivityUpdateParam param) {
        if(param.getId()==null) {
            throw new ServiceException("编辑的活动主键id为空" );
        }
        checkNameParam(param.getActivityName());
        checkTimeParam(param.getBeginTime(),param.getEndTime());
        PreActivityInfo activityInfo =  preActivityInfoMapper.selectById(param.getId());
        BeanUtil.copyProperties(param, activityInfo,"id");
        if (CollUtil.isNotEmpty(param.getRewardRuleList())) {
            activityInfo.setRewardRuleContent(JSONUtil.toJsonStr(param.getRewardRuleList()));
        }
        preActivityInfoMapper.updateById(activityInfo);
        log.info("编辑活动信息成功");
        Set<Long> oldIds = new HashSet<>();
        QueryWrapper<FolksonomyBusinessRel> qw = new QueryWrapper<>();
        qw.in("business_id", param.getId());
        List<FolksonomyBusinessRel> folksonomyBusinessRels = folksonomyBusinessRelMapper.selectList(qw);
        if(CollUtil.isNotEmpty(folksonomyBusinessRels)){
            folksonomyBusinessRels.forEach(f -> {
                oldIds.add(f.getFolksonomyId());
            });
        }
        Set<Long> newIds = new HashSet<>();
        if(CollUtil.isNotEmpty(param.getFolksonomyIds())){
            newIds = new HashSet(Arrays.asList(param.getFolksonomyIds()));
        }
        List<Long> addIds = new ArrayList<>();
        List<Long> deleteIds = new ArrayList<>();
        for(Long n : newIds){
            if(!oldIds.contains(n)){
                addIds.add(n);
            }
        }
        for(Long o : oldIds){
            if(!newIds.contains(o)){
                deleteIds.add(o);
            }
        }
        if(CollUtil.isNotEmpty(addIds)){
            folksonomyService.saveFolksonomyBusinessRel(BusinessTypeEnum.PREACTIVITY, activityInfo.getId(), addIds);
        }

        if(CollUtil.isNotEmpty(deleteIds)){
            deleteIds.forEach(i -> {
                folksonomyBusinessRelMapper.delete(new QueryWrapper<FolksonomyBusinessRel>()
                        .eq("folksonomy_id", i)
                        .eq("business_id",param.getId()));

            });
        }
        log.info("处理标签成功");
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
        //rewardRuleList
        if (StrUtil.isNotBlank(info.getRewardRuleContent())) {
            preActivityDetailResult.setRewardRuleList(JSONUtil.toList(JSONUtil.parseArray(info.getRewardRuleContent()), PreActivityRewardResult.class));
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
        activityInfo.setActivityState(param.getActivityState());
        preActivityInfoMapper.updateById(activityInfo);
    }

    @Override
    public PreActivityAnalysisResult analyse(PreActivityAnalysisParam param) {
        QueryWrapper<PreOrderInfo> qw = new QueryWrapper<>();
        qw.select("DISTINCT member_id","sum(total_price) as total")
                .eq("activity_info_id",param.getId())
                .groupBy("member_id");
        List<Map<String, Object>> maps = preOrderInfoMapper.selectMaps(qw);
        PreActivityAnalysisResult result = new  PreActivityAnalysisResult();
        if(CollUtil.isNotEmpty(maps)){
            result.setParticipantsCount(Convert.toLong(maps.size()));
            final BigDecimal total = new BigDecimal(0.00);
            maps.forEach(l ->{
                BigDecimal bigDecimal = (BigDecimal) l.get("total");
                total.add(bigDecimal);
            });
            result.setExchangePrice(total);
            if(maps.size() > 0){
                BigDecimal ppc = total.divide(new BigDecimal(maps.size()));
                result.setPricePerCustomer(ppc);
            }else{
                result.setPricePerCustomer(new BigDecimal(0.00));
            }
        }else{
            result.setParticipantsCount(0L);
            result.setExchangePrice(new BigDecimal(0.00));
            result.setPricePerCustomer(new BigDecimal(0.00));
        }
        return result;
    }
}
