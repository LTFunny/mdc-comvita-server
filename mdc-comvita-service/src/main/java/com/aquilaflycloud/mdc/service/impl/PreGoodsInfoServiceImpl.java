package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.aquilaflycloud.mdc.enums.member.BusinessTypeEnum;
import com.aquilaflycloud.mdc.enums.pre.ActivityStateEnum;
import com.aquilaflycloud.mdc.enums.pre.GoodsStateEnum;
import com.aquilaflycloud.mdc.enums.pre.GoodsTypeEnum;
import com.aquilaflycloud.mdc.enums.pre.RuleStateEnum;
import com.aquilaflycloud.mdc.mapper.PreActivityInfoMapper;
import com.aquilaflycloud.mdc.mapper.PreGoodsInfoMapper;
import com.aquilaflycloud.mdc.mapper.PreOrderInfoMapper;
import com.aquilaflycloud.mdc.mapper.PreRuleInfoMapper;
import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyInfo;
import com.aquilaflycloud.mdc.model.pre.PreActivityInfo;
import com.aquilaflycloud.mdc.model.pre.PreGoodsInfo;
import com.aquilaflycloud.mdc.model.pre.PreRuleInfo;
import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.pre.GoodsSalesVolumeResult;
import com.aquilaflycloud.mdc.result.pre.PreGoodsInfoResult;
import com.aquilaflycloud.mdc.service.FolksonomyService;
import com.aquilaflycloud.mdc.service.PreGoodsInfoService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zouliyong
 */
@Service
@Slf4j
public class PreGoodsInfoServiceImpl implements PreGoodsInfoService {
    @Resource
    private PreGoodsInfoMapper preGoodsInfoMapper;
    @Resource
    private PreOrderInfoMapper preOrderInfoMapper;
    @Resource
    private PreActivityInfoMapper preActivityInfoMapper;
    @Resource
    private PreRuleInfoMapper preRuleInfoMapper;
    @Resource
    private FolksonomyService folksonomyService;

    @Override
    public IPage<PreGoodsInfo> pagePreGoodsInfoList(PreGoodsInfoListParam param) {
        return preGoodsInfoMapper.selectPage(param.page(), Wrappers.<PreGoodsInfo>lambdaQuery()
                .like(StrUtil.isNotBlank(param.getGoodsName()), PreGoodsInfo::getGoodsName, param.getGoodsName())
                .like(StrUtil.isNotBlank(param.getFolksonomyId()), PreGoodsInfo::getFolksonomyId, param.getFolksonomyId())
                .eq(param.getGoodsState() != null, PreGoodsInfo::getGoodsState, param.getGoodsState())
                .eq(param.getGoodsType() != null, PreGoodsInfo::getGoodsType, param.getGoodsType())
                .like(StrUtil.isNotBlank(param.getGoodsCode()), PreGoodsInfo::getGoodsCode, param.getGoodsCode())
                .notIn(param.getMiniSymbol() == 1,PreGoodsInfo::getGoodsType,GoodsTypeEnum.GIFTS)
                .orderByDesc(PreGoodsInfo::getCreateTime)
        );
    }

    private void checkDuplicate(Long id, String code, String name) {
        if (StrUtil.isAllBlank(code, name)) {
            throw new ServiceException("?????????????????????????????????");
        }
        int count = preGoodsInfoMapper.selectCount(Wrappers.<PreGoodsInfo>lambdaQuery()
                .ne(id != null, PreGoodsInfo::getId, id)
                .eq(StrUtil.isNotBlank(code), PreGoodsInfo::getGoodsCode, code)
                .eq(StrUtil.isNotBlank(name), PreGoodsInfo::getGoodsName, name)
        );
        if (count > 0) {
            if (StrUtil.isNotBlank(code)) {
                throw new ServiceException("????????????????????????");
            }
            if (StrUtil.isNotBlank(name)) {
                throw new ServiceException("????????????????????????");
            }
        }
    }

    @Override
    @Transactional
    public void addPreGoodsInfo(GoodsInfoAddParam param) {
        checkDuplicate(null, param.getGoodsCode(), null);
        checkDuplicate(null, null, param.getGoodsName());
        PreGoodsInfo preGoodsInfo = BeanUtil.copyProperties(param, PreGoodsInfo.class);
        Long goodsId = MdcUtil.getSnowflakeId();
        preGoodsInfo.setId(goodsId);
        preGoodsInfo.setGoodsState(GoodsStateEnum.ONSALE);
        Map<Long, String> folksonomyMap = folksonomyService.saveFolksonomyBusinessRel(BusinessTypeEnum.PREGOODS, preGoodsInfo.getId(), param.getFolksonomyIds());
        if (CollUtil.isNotEmpty(folksonomyMap)) {
            preGoodsInfo.setFolksonomyId(CollUtil.join(folksonomyMap.keySet(), ","));
            preGoodsInfo.setFolksonomyName(CollUtil.join(folksonomyMap.values(), ","));
        }
        int count = preGoodsInfoMapper.insert(preGoodsInfo);
        if (count <= 0) {
            throw new ServiceException("????????????????????????");
        }
    }

    @Override
    @Transactional
    public void editPreGoodsInfo(GoodsInfoEditParam param) {
        PreGoodsInfo goods = preGoodsInfoMapper.selectById(param.getId());
        if (goods == null) {
            throw new ServiceException("???????????????");
        }
        //?????????????????????????????????
        checkDuplicate(goods.getId(), param.getGoodsCode(), null);
        checkDuplicate(goods.getId(), null, param.getGoodsName());
        PreGoodsInfo update = new PreGoodsInfo();
        BeanUtil.copyProperties(param, update);
        update.setId(goods.getId());
        //????????????????????????
        Map<Long, String> folksonomyMap = folksonomyService.saveFolksonomyBusinessRel(BusinessTypeEnum.PREGOODS, update.getId(), param.getFolksonomyIds());
        if (CollUtil.isNotEmpty(folksonomyMap)) {
            update.setFolksonomyId(CollUtil.join(folksonomyMap.keySet(), ","));
            update.setFolksonomyName(CollUtil.join(folksonomyMap.values(), ","));
        }
        int count = preGoodsInfoMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("????????????????????????");
        }
    }

    @Override
    @Transactional
    public void changeGoodsState(ChangeGoodsStateParam param) {
        PreGoodsInfo goods = preGoodsInfoMapper.selectById(param.getId());
        if (goods.getGoodsState() == GoodsStateEnum.ONSALE) {
            //????????????????????????????????????
            DateTime now = DateTime.now();
            int count = preActivityInfoMapper.selectCount(Wrappers.<PreActivityInfo>lambdaQuery()
//                    .eq(PreActivityInfo::getRefGoods, goods.getId())
                    .like(PreActivityInfo::getRefGoods, goods.getId())
                    .ge(PreActivityInfo::getEndTime, now)
                    .ne(PreActivityInfo::getActivityState, ActivityStateEnum.CANCELED)
            );
            if (count > 0) {
                throw new ServiceException("?????????????????????????????????,????????????");
            }
            //?????????????????????
            if (GoodsTypeEnum.GIFTS.equals(goods.getGoodsType())) {
                //??????????????????????????????????????????????????????
                int count2 = preRuleInfoMapper.selectCount(Wrappers.<PreRuleInfo>lambdaQuery()
                        .like(PreRuleInfo::getTypeDetail, goods.getId())
                        .eq(PreRuleInfo::getRuleState, RuleStateEnum.ENABLE)
                );
                if (count2 > 0) {
                    throw new ServiceException("?????????????????????????????????,????????????");
                }
            }

        }
        PreGoodsInfo update = new PreGoodsInfo();
        update.setId(param.getId());
        update.setGoodsState(goods.getGoodsState() == GoodsStateEnum.ONSALE ? GoodsStateEnum.OFFTHESHELF : GoodsStateEnum.ONSALE);
        int count = preGoodsInfoMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("????????????");
        }
    }

    @Override
    public PreGoodsInfoResult getGoodsInfo(GoodsInfoGetParam param) {
        PreGoodsInfo goods = preGoodsInfoMapper.selectById(param.getId());
        if (goods == null) {
            throw new ServiceException("???????????????");
        }
        PreGoodsInfoResult preGoodsInfoResult = BeanUtil.copyProperties(goods, PreGoodsInfoResult.class);
        List<FolksonomyInfo> folksonomyInfoList = folksonomyService.getFolksonomyBusinessList(BusinessTypeEnum.PREGOODS, goods.getId());
        preGoodsInfoResult.setFolksonomyInfoList(folksonomyInfoList);
        return preGoodsInfoResult;
    }

    @Override
    public GoodsSalesVolumeResult goodsVolume(GoodsSaleNumParam param) {
        try {
            param.setGoodsSevenTime(getBeforOrAfterDate(new Date(), -7));
            param.setGoodsFifteenTime(getBeforOrAfterDate(new Date(), -15));
            param.setGoodsThirtyTime(getBeforOrAfterDate(new Date(), -30));
            GoodsSalesVolumeResult info = preOrderInfoMapper.getNum(param);
            return info;
        } catch (ParseException e) {
            throw new ServiceException("????????????????????????");
        }

    }

    /**
     * ????????????????????????????????????count?????????
     *
     * @param selectDate
     * @param count
     * @return
     */
    public Date getBeforOrAfterDate(Date selectDate, int count) throws ParseException {
        Calendar c = Calendar.getInstance();
        c.setTime(selectDate);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + count);
        return c.getTime();
    }
}
