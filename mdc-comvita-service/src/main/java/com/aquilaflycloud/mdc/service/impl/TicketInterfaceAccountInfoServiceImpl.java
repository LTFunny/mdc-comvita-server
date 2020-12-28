package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aquilaflycloud.mdc.mapper.TicketInterfaceAccountInfoMapper;
import com.aquilaflycloud.mdc.mapper.TicketScenicSpotInfoMapper;
import com.aquilaflycloud.mdc.model.ticket.TicketInterfaceAccountInfo;
import com.aquilaflycloud.mdc.model.ticket.TicketScenicSpotInfo;
import com.aquilaflycloud.mdc.param.ticket.InterfaceAccountInfoAddParam;
import com.aquilaflycloud.mdc.param.ticket.InterfaceAccountInfoEditParam;
import com.aquilaflycloud.mdc.param.ticket.InterfaceAccountInfoListParam;
import com.aquilaflycloud.mdc.result.ticket.TicketInterfaceAccountInfoRelationResult;
import com.aquilaflycloud.mdc.result.ticket.TicketInterfaceAccountInfoResult;
import com.aquilaflycloud.mdc.service.TicketInterfaceAccountInfoService;
import com.aquilaflycloud.mdc.service.TicketScenicSpotInfoService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * <p>
 * 第三方接口(道控)账号信息服务实现类
 * </p>
 *
 * @author Zengqingjie
 * @since 2019-11-19
 */
@Service
public class TicketInterfaceAccountInfoServiceImpl implements TicketInterfaceAccountInfoService {
    @Resource
    private TicketInterfaceAccountInfoMapper ticketInterfaceAccountInfoMapper;

    @Resource
    private TicketScenicSpotInfoMapper ticketScenicSpotInfoMapper;

    @Resource
    private TicketScenicSpotInfoService ticketScenicSpotInfoService;

    @Override
    public List<TicketInterfaceAccountInfoResult> getInterfaceAccountInfo() {
        //查询所有账号信息
        List<TicketInterfaceAccountInfo> ticketInterfaceAccountInfos = ticketInterfaceAccountInfoMapper.selectList(Wrappers.<TicketInterfaceAccountInfo>lambdaQuery()
                .isNotNull(TicketInterfaceAccountInfo::getMerchantCode)
                .isNotNull(TicketInterfaceAccountInfo::getInterfaceAccount)
                .isNotNull(TicketInterfaceAccountInfo::getSecret)
                .isNotNull(TicketInterfaceAccountInfo::getBaseUrl)
                .isNotNull(TicketInterfaceAccountInfo::getDesignateOrgIds));

        //获取所有景区的信息
        List<TicketScenicSpotInfo> list = ticketScenicSpotInfoService.list();

        List<TicketInterfaceAccountInfoResult> result = new ArrayList<>();

        //账号中的id已经配置到景区中
        if ((ObjectUtil.isNotNull(ticketInterfaceAccountInfos) && ticketInterfaceAccountInfos.size() > 0)
                && (ObjectUtil.isNotNull(list) && list.size() > 0)) {
            for (int i = 0; i < ticketInterfaceAccountInfos.size(); i++) {
                TicketInterfaceAccountInfo ticketInterfaceAccountInfo = ticketInterfaceAccountInfos.get(i);
                for (int j = 0; j < list.size(); j++) {
                    TicketScenicSpotInfo ticketScenicSpotInfo = list.get(j);

                    if (ticketScenicSpotInfo.getAccountId().longValue() == (ticketInterfaceAccountInfo.getId().longValue())) {
                        TicketInterfaceAccountInfoResult item = new TicketInterfaceAccountInfoResult();
                        BeanUtil.copyProperties(ticketInterfaceAccountInfo, item);
                        //设置景区id和景区类型
                        item.setScenicSpotId(ticketScenicSpotInfo.getId());
                        item.setType(ticketScenicSpotInfo.getType());
                        item.setScenicSpotName(ticketScenicSpotInfo.getName());
                        result.add(item);
                    }
                }
            }
        }

        return result;
    }

    @Override
    public List<TicketInterfaceAccountInfoResult> normalGetInterfaceAccountInfo() {
        //查询所有账号信息
        List<TicketInterfaceAccountInfo> ticketInterfaceAccountInfos = ticketInterfaceAccountInfoMapper.normalSelectList(Wrappers.<TicketInterfaceAccountInfo>lambdaQuery()
                .isNotNull(TicketInterfaceAccountInfo::getMerchantCode)
                .isNotNull(TicketInterfaceAccountInfo::getInterfaceAccount)
                .isNotNull(TicketInterfaceAccountInfo::getSecret)
                .isNotNull(TicketInterfaceAccountInfo::getBaseUrl)
                .isNotNull(TicketInterfaceAccountInfo::getDesignateOrgIds));

        //获取所有景区的信息
        List<TicketScenicSpotInfo> list = ticketScenicSpotInfoMapper.normalSelectList(Wrappers.<TicketScenicSpotInfo> lambdaQuery()
                .isNotNull(TicketScenicSpotInfo::getAccountId)
                .isNotNull(TicketScenicSpotInfo::getType)
        );

        List<TicketInterfaceAccountInfoResult> result = new ArrayList<>();

        //账号中的id已经配置到景区中
        if ((ObjectUtil.isNotNull(ticketInterfaceAccountInfos) && ticketInterfaceAccountInfos.size() > 0)
                && (ObjectUtil.isNotNull(list) && list.size() > 0)) {
            for (int i = 0; i < ticketInterfaceAccountInfos.size(); i++) {
                TicketInterfaceAccountInfo ticketInterfaceAccountInfo = ticketInterfaceAccountInfos.get(i);
                for (int j = 0; j < list.size(); j++) {
                    TicketScenicSpotInfo ticketScenicSpotInfo = list.get(j);
                    if (ticketScenicSpotInfo.getAccountId().longValue() == (ticketInterfaceAccountInfo.getId().longValue())) {
                        TicketInterfaceAccountInfoResult item = new TicketInterfaceAccountInfoResult();
                        BeanUtil.copyProperties(ticketInterfaceAccountInfo, item);
                        //设置景区id和景区类型
                        item.setScenicSpotId(ticketScenicSpotInfo.getId());
                        item.setType(ticketScenicSpotInfo.getType());
                        item.setScenicSpotName(ticketScenicSpotInfo.getName());
                        result.add(item);
                    }
                }
            }
        }

        return result;
    }

    @Override
    public List<TicketInterfaceAccountInfoRelationResult> list(InterfaceAccountInfoListParam param) {
        //获取已经配置了的账号id
        List<TicketScenicSpotInfo> ticketScenicSpotInfos = ticketScenicSpotInfoMapper.selectList(Wrappers.<TicketScenicSpotInfo>lambdaQuery()
                .isNotNull(TicketScenicSpotInfo::getAccountId)
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        );

        //如果为空，则返回空
        if (ObjectUtil.isNull(ticketScenicSpotInfos) || ticketScenicSpotInfos.size() <= 0) {
            return null;
        }

        List<Long> accountIds = new ArrayList<>();
        Map<Long, TicketScenicSpotInfo> scenicSpotInfoMap = new HashMap<>();
        for (int i = 0; i < ticketScenicSpotInfos.size(); i++) {
            TicketScenicSpotInfo ticketScenicSpotInfo = ticketScenicSpotInfos.get(i);
            accountIds.add(ticketScenicSpotInfo.getAccountId());
            scenicSpotInfoMap.put(ticketScenicSpotInfo.getAccountId(), ticketScenicSpotInfo);
        }


        //查询账号信息
        List<TicketInterfaceAccountInfo> ticketInterfaceAccountInfos = ticketInterfaceAccountInfoMapper.selectList(Wrappers.<TicketInterfaceAccountInfo>lambdaQuery()
                .in(TicketInterfaceAccountInfo::getId, accountIds)
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        );

        //如果为空，则返回空
        if (ObjectUtil.isNull(ticketInterfaceAccountInfos) || ticketInterfaceAccountInfos.size() <= 0) {
            throw new ServiceException("未找到账号的信息");
        }

        List<TicketInterfaceAccountInfoRelationResult> result = new ArrayList<>();
        for (int i = 0; i < ticketInterfaceAccountInfos.size(); i++) {
            TicketInterfaceAccountInfo accountInfo = ticketInterfaceAccountInfos.get(i);
            TicketInterfaceAccountInfoRelationResult item = new TicketInterfaceAccountInfoRelationResult();
            BeanUtil.copyProperties(accountInfo, item);
            item.setScenicSpotTypeEnum(scenicSpotInfoMap.get(accountInfo.getId()).getType());
            result.add(item);
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TicketInterfaceAccountInfoRelationResult add(InterfaceAccountInfoAddParam param) {
        //判断账号信息(商户编码或OTA账户或秘钥)是否重复
        List<TicketInterfaceAccountInfo> ticketInterfaceAccountInfos = ticketInterfaceAccountInfoMapper.selectList(Wrappers.<TicketInterfaceAccountInfo>lambdaQuery()
                .eq(TicketInterfaceAccountInfo::getMerchantCode, param.getMerchantCode())
                .or().eq(TicketInterfaceAccountInfo::getInterfaceAccount, param.getInterfaceAccount())
                .or().eq(TicketInterfaceAccountInfo::getSecret, param.getSecret())
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        );

        if (ObjectUtil.isNotNull(ticketInterfaceAccountInfos) && ticketInterfaceAccountInfos.size() > 0) {
            throw new ServiceException("商户编码或OTA账户或秘钥存在重复信息");
        }

        //判断对应景区是否设置了账号信息
        List<TicketScenicSpotInfo> ticketScenicSpotInfos = ticketScenicSpotInfoMapper.selectList(Wrappers.<TicketScenicSpotInfo>lambdaQuery()
                .eq(TicketScenicSpotInfo::getType, param.getScenicSpotType())
                .isNotNull(TicketScenicSpotInfo::getAccountId)
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        );

        if (ObjectUtil.isNotNull(ticketScenicSpotInfos) && ticketScenicSpotInfos.size() > 0) {
            throw new ServiceException("选中的景区已配置账号信息");
        }

        //保存账号信息
        TicketInterfaceAccountInfo accountInfo = new TicketInterfaceAccountInfo();
        BeanUtil.copyProperties(param, accountInfo);
        accountInfo.setId(IdWorker.getId());

        int insertCount = ticketInterfaceAccountInfoMapper.insert(accountInfo);

        //更新景区信息
        TicketScenicSpotInfo ticketScenicSpotInfo = new TicketScenicSpotInfo();
        ticketScenicSpotInfo.setAccountId(accountInfo.getId());
        ticketScenicSpotInfo.setDesignateOrgIds(accountInfo.getDesignateOrgIds());
        ticketScenicSpotInfo.setDesignateOrgNames(accountInfo.getDesignateOrgNames());
        int updateCount = ticketScenicSpotInfoMapper.update(ticketScenicSpotInfo, Wrappers.<TicketScenicSpotInfo>lambdaQuery()
                .eq(TicketScenicSpotInfo::getType, param.getScenicSpotType())
        );

        if (insertCount <=0 || updateCount <= 0) {
            throw new ServiceException("保存账号信息失败");
        }

        TicketInterfaceAccountInfoRelationResult result = new TicketInterfaceAccountInfoRelationResult();
        BeanUtil.copyProperties(accountInfo, result);
        result.setScenicSpotTypeEnum(param.getScenicSpotType());

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TicketInterfaceAccountInfoRelationResult edit(InterfaceAccountInfoEditParam param) {
        TicketInterfaceAccountInfoRelationResult result = new TicketInterfaceAccountInfoRelationResult();
        //判断账号信息(商户编码或OTA账户或秘钥)是否重复(过滤本身)
        List<TicketInterfaceAccountInfo> ticketInterfaceAccountInfos = ticketInterfaceAccountInfoMapper.selectList(Wrappers.<TicketInterfaceAccountInfo>lambdaQuery()
                .eq(TicketInterfaceAccountInfo::getMerchantCode, param.getMerchantCode())
                .or().eq(TicketInterfaceAccountInfo::getInterfaceAccount, param.getInterfaceAccount())
                .or().eq(TicketInterfaceAccountInfo::getSecret, param.getSecret())
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        ).stream().filter(item->ObjectUtil.notEqual(item.getId(), param.getId())).collect(toList());

        if (ObjectUtil.isNotNull(ticketInterfaceAccountInfos) && ticketInterfaceAccountInfos.size() > 0) {
            throw new ServiceException("商户编码或OTA账户或秘钥存在重复信息");
        }

        TicketInterfaceAccountInfo interfaceAccountInfo = ticketInterfaceAccountInfoMapper.selectById(param.getId());
        if (ObjectUtil.isNull(interfaceAccountInfo)) {
            throw new ServiceException("未找到对应账号信息");
        }


        List<TicketScenicSpotInfo> scenicSpotInfos = ticketScenicSpotInfoMapper.selectList(Wrappers.<TicketScenicSpotInfo>lambdaQuery()
                .eq(TicketScenicSpotInfo::getAccountId, interfaceAccountInfo.getId())
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        );

        if (ObjectUtil.isNull(scenicSpotInfos)) {
            throw new ServiceException("该账号未关联到景区或景区被删除");
        } else if (ObjectUtil.isNotNull(scenicSpotInfos) && scenicSpotInfos.size() >=2) {
            throw new ServiceException("该账号关联到多个景区");
        }


        TicketScenicSpotInfo oldScenicSpotInfo = scenicSpotInfos.get(0);
        if (ObjectUtil.notEqual(oldScenicSpotInfo.getType(), param.getScenicSpotType())) {
            //账号关联的景区与之前不同，将原先关联的账号id设置为null，更新关联的景区
            //获取关联景区，是否已关联账号
            List<TicketScenicSpotInfo> newScenicSpotInfos = ticketScenicSpotInfoMapper.selectList(Wrappers.<TicketScenicSpotInfo>lambdaQuery()
                    .eq(TicketScenicSpotInfo::getType, param.getScenicSpotType())
                    .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
            );

            if (ObjectUtil.isNull(newScenicSpotInfos)) {
                throw new ServiceException("选择的景区信息不存在");
            } else if (ObjectUtil.isNotNull(newScenicSpotInfos) && newScenicSpotInfos.size() >=2) {
                throw new ServiceException("选择的景区对应多个信息");
            }

            TicketScenicSpotInfo newScenicSpotInfo = newScenicSpotInfos.get(0);
            if (ObjectUtil.isNotNull(newScenicSpotInfo.getAccountId())) {
                throw new ServiceException("选择的景区已关联到账号");
            }

            //更新旧的景区关联账号
            TicketScenicSpotInfo oldItem = new TicketScenicSpotInfo();
            oldItem.setId(oldScenicSpotInfo.getId());
            oldItem.setAccountId(null);

            int oldCount = ticketScenicSpotInfoMapper.updateById(oldItem);


            //更新新的景区关联账号
            TicketScenicSpotInfo newItem = new TicketScenicSpotInfo();
            newItem.setId(newScenicSpotInfo.getId());
            newItem.setAccountId(param.getId());

            int newCount = ticketScenicSpotInfoMapper.updateById(newItem);

            if (oldCount <= 0 || newCount<=0) {
                throw new ServiceException("更新信息失败");
            }
        } else if (ObjectUtil.equal(oldScenicSpotInfo.getType(), param.getScenicSpotType())) {
            //更新景区的指定部门ids
            TicketScenicSpotInfo updateItem = new TicketScenicSpotInfo();
            updateItem.setId(oldScenicSpotInfo.getId());
            updateItem.setDesignateOrgIds(param.getDesignateOrgIds());
            MdcUtil.setOrganInfo(updateItem);
            updateItem.setAccountId(param.getId());
            int updateCount = ticketScenicSpotInfoMapper.updateById(updateItem);

            if (updateCount <=0) {
                throw new ServiceException("更新信息失败");
            }
        }


        TicketInterfaceAccountInfo accountUpdateItem = new TicketInterfaceAccountInfo();
        BeanUtil.copyProperties(param, accountUpdateItem);
        int count = ticketInterfaceAccountInfoMapper.updateById(accountUpdateItem);

        if (count <= 0) {
            throw new ServiceException("更新账号信息失败");
        }

        BeanUtil.copyProperties(accountUpdateItem, result);
        result.setScenicSpotTypeEnum(param.getScenicSpotType());
        return result;
    }
}
