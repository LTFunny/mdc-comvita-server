package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.mdc.enums.pre.ActivityStateEnum;
import com.aquilaflycloud.mdc.enums.pre.RuleDefaultEnum;
import com.aquilaflycloud.mdc.enums.pre.RuleStateEnum;
import com.aquilaflycloud.mdc.enums.pre.RuleTypeEnum;
import com.aquilaflycloud.mdc.mapper.PreActivityInfoMapper;
import com.aquilaflycloud.mdc.mapper.PreRuleInfoMapper;
import com.aquilaflycloud.mdc.model.pre.PreActivityInfo;
import com.aquilaflycloud.mdc.model.pre.PreRuleInfo;
import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.pre.PreEnableRuleResult;
import com.aquilaflycloud.mdc.result.pre.PreRuleDetailResult;
import com.aquilaflycloud.mdc.service.PreRuleService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * PreRuleServiceImpl
 * @author linkq
 */
@Slf4j
@Service
public class PreRuleServiceImpl implements PreRuleService {

    @Resource
    private PreRuleInfoMapper preRuleInfoMapper;

    @Resource
    private PreActivityInfoMapper preActivityInfoMapper;

    @Override
    public IPage<PreRuleDetailResult> page(PreRulePageParam param) {
        return preRuleInfoMapper.selectPage(param.page(), Wrappers.<PreRuleInfo>lambdaQuery()
                .like( param.getRuleName()!=null,PreRuleInfo::getRuleName, param.getRuleName())
                .eq( param.getRuleState()!=null,PreRuleInfo::getRuleState, param.getRuleState())
                .eq( param.getRuleType()!=null,PreRuleInfo::getRuleType, param.getRuleType())
                .orderByDesc(PreRuleInfo::getIsDefault,PreRuleInfo::getCreateTime)
        ).convert(this::dataConvert);
    }

    @Override
    public void add(PreRuleAddParam param) {
        checkNameParam(param.getRuleName());
        PreRuleInfo info = new PreRuleInfo();
        BeanUtil.copyProperties(param, info);
        info.setId(MdcUtil.getSnowflakeId());
        if(param.getRuleType() == RuleTypeEnum.ORDER_DISCOUNT){
            info.setTypeDetail(JSONUtil.toJsonStr(param.getOrderDiscount()));
        }
        if(param.getRuleType() == RuleTypeEnum.ORDER_FULL_REDUCE){
            info.setTypeDetail(JSONUtil.toJsonStr(param.getOrderFullReduce()));
        }
        if(param.getRuleType() == RuleTypeEnum.ORDER_GIFTS){
            info.setTypeDetail(JSONUtil.toJsonStr(param.getRefGoods()));
        }
        int count = preRuleInfoMapper.insert(info);
        if (count == 1) {
            log.info("新增营销规则成功");
        } else {
            throw new ServiceException("新增营销规则失败");
        }
    }

    private PreRuleDetailResult dataConvert(PreRuleInfo info){
        if(null != info){
            PreRuleDetailResult p = new PreRuleDetailResult();
            BeanUtil.copyProperties(info, p);
            if(info.getRuleType() == RuleTypeEnum.ORDER_DISCOUNT){
                p.setOrderDiscount(JSONUtil.toBean(info.getTypeDetail(),PreRuleOrderDiscountParam.class));
            }
            if(info.getRuleType() == RuleTypeEnum.ORDER_FULL_REDUCE){
                p.setOrderFullReduce(JSONUtil.toBean(info.getTypeDetail(),PreRuleOrderFullReduceParam.class));
            }
            if(info.getRuleType() == RuleTypeEnum.ORDER_GIFTS){
                p.setRefGoods(JSONUtil.toList(JSONUtil.parseArray(info.getTypeDetail()), PreRuleGoodsParam.class));
            }
            return p;
        }else {
            return null;
        }
    }

    @Override
    public PreRuleDetailResult get(PreRuleIdParam param) {
        PreRuleInfo info =  preRuleInfoMapper.selectById(param.getId());
        return dataConvert(info);
    }

    private void checkNameParam(String name) {
        PreRuleInfo info =  preRuleInfoMapper.selectOne(Wrappers.<PreRuleInfo>lambdaQuery()
                .eq(PreRuleInfo::getRuleName,name));
        if(null != info){
            throw new ServiceException("已存在相同名称的规则,名称为:" + info.getRuleName());
        }
    }

    @Override
    public void update(PreRuleUpdateParam param) {
        PreRuleInfo info =  preRuleInfoMapper.selectById(param.getId());
        PreRuleInfo newInfo = new PreRuleInfo();
        BeanUtil.copyProperties(param,newInfo);
        if(info.getRuleType() == RuleTypeEnum.ORDER_DISCOUNT){
            String jsonStr = JSONUtil.toJsonStr(param.getOrderDiscount());
            if(!info.getTypeDetail().equals(jsonStr)){
                newInfo.setTypeDetail(JSONUtil.toJsonStr(param.getOrderDiscount()));
            }
        }
        if(info.getRuleType() == RuleTypeEnum.ORDER_FULL_REDUCE){
            String jsonStr = JSONUtil.toJsonStr(param.getOrderFullReduce());
            if(!info.getTypeDetail().equals(jsonStr)){
                newInfo.setTypeDetail(JSONUtil.toJsonStr(param.getOrderFullReduce()));
            }
        }
        if(info.getRuleType() == RuleTypeEnum.ORDER_GIFTS){
            String jsonStr = JSONUtil.toJsonStr(param.getRefGoods());
            if(!info.getTypeDetail().equals(jsonStr)){
                newInfo.setTypeDetail(JSONUtil.toJsonStr(param.getRefGoods()));
            }
        }
        int count = preRuleInfoMapper.updateById(newInfo);
        if (count <= 0) {
            throw new ServiceException("更新规则失败");
        }
        log.info("更新规则完成");
    }

    @Override
    public void cancelStart(PreRuleIdParam param) {
        if(param.getId()==null) {
            throw new ServiceException("规则主键id为空" );
        }
        PreRuleInfo info =  preRuleInfoMapper.selectById(param.getId());
        PreRuleInfo newInfo = new PreRuleInfo();
        BeanUtil.copyProperties(param,newInfo);
        if(RuleStateEnum.DISABLE == info.getRuleState()){
            newInfo.setRuleState(RuleStateEnum.ENABLE);
        }else if(RuleStateEnum.ENABLE == info.getRuleState()){
            checkRef(param.getId());
            newInfo.setRuleState(RuleStateEnum.DISABLE);
        }

        int count = preRuleInfoMapper.updateById(newInfo);
        if (count <= 0) {
            throw new ServiceException("更新规则状态失败");
        }
        log.info("更新规则状态完成");
    }

    /**
     * 检查规则关联的活动
     * 销售规则有关联的正在进行的活动时，不允许停用，浮层提示异常信息“该销售规则有关联的正在进行/未开始的活动，无法停用”
     * @param id
     */
    private void checkRef(Long id) {
        QueryWrapper<PreActivityInfo> qw = new QueryWrapper<>();
        qw.eq("ref_rule", id);
        List<PreActivityInfo> infos = preActivityInfoMapper.selectList(qw);
        if(null != infos && infos.size() > 0){
            boolean notStartActivityExist = false;
            boolean runningActivityExist = false;
            DateTime now = DateTime.now();
            for(PreActivityInfo info : infos){
                if(ActivityStateEnum.CANCELED != info.getActivityState()){
                    if (now.isAfterOrEquals(info.getBeginTime()) && now.isBeforeOrEquals(info.getEndTime())) {
                        runningActivityExist = true;
                        break;
                    } else if (now.isBefore(info.getBeginTime())) {
                        notStartActivityExist = true;
                        break;
                    }
                }
            }
            if(notStartActivityExist){
                throw new ServiceException("该销售规则有关联的未开始的活动，无法停用" );
            }
            if(runningActivityExist){
                throw new ServiceException("该销售规则有关联的正在进行的活动，无法停用" );
            }
        }
    }

    @Override
    public void setDefault(PreRuleIdParam param) {
        if(param.getId()==null) {
            throw new ServiceException("规则主键id为空" );
        }
        PreRuleInfo oldInfo =  preRuleInfoMapper.selectOne(Wrappers.<PreRuleInfo>lambdaQuery()
                .eq(PreRuleInfo::getIsDefault,RuleDefaultEnum.DEFAULT));
        if(null != oldInfo){
            PreRuleInfo newInfo = new PreRuleInfo();
            BeanUtil.copyProperties(param,newInfo);
            newInfo.setIsDefault(RuleDefaultEnum.NOT_DEFAULT);
            int count = preRuleInfoMapper.updateById(newInfo);
            if (count <= 0) {
                throw new ServiceException("默认撤销失败");
            }
            log.info("默认撤销成功!");
        }

        PreRuleInfo newInfo = new PreRuleInfo();
        BeanUtil.copyProperties(param,newInfo);
        newInfo.setIsDefault(RuleDefaultEnum.DEFAULT);
        int count = preRuleInfoMapper.updateById(newInfo);
        if (count <= 0) {
            throw new ServiceException("默认设置失败");
        }
        log.info("默认设置成功!");
    }

    @Override
    public List<PreEnableRuleResult> getEnableRules() {
        List<PreRuleInfo> infos = preRuleInfoMapper.selectList(Wrappers.<PreRuleInfo>lambdaQuery()
                .eq(PreRuleInfo::getRuleState, RuleStateEnum.ENABLE)
        );
        if(CollUtil.isNotEmpty(infos)){
            List<PreEnableRuleResult> results = new ArrayList<>();
            infos.forEach(r -> {
                PreEnableRuleResult ruleResult = new  PreEnableRuleResult();
                ruleResult.setRuleId(r.getId());
                ruleResult.setRuleName(r.getRuleName());
                results.add(ruleResult);
            });
            return results;
        }else{
            return null;
        }
    }
}
