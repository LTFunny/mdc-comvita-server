package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.aquilaflycloud.mdc.enums.shop.ShopCommentAnonymousEnum;
import com.aquilaflycloud.mdc.enums.shop.ShopCommentStateEnum;
import com.aquilaflycloud.mdc.enums.shop.ShopCommentScoreEnum;
import com.aquilaflycloud.mdc.mapper.ShopCommentInfoMapper;
import com.aquilaflycloud.mdc.mapper.ShopInfoMapper;
import com.aquilaflycloud.mdc.model.shop.ShopCommentInfo;
import com.aquilaflycloud.mdc.model.shop.ShopInfo;
import com.aquilaflycloud.mdc.param.shop.*;
import com.aquilaflycloud.mdc.result.member.MemberInfoResult;
import com.aquilaflycloud.mdc.result.shop.*;
import com.aquilaflycloud.mdc.service.ShopCommentInfoService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class ShopCommentInfoServiceImpl implements ShopCommentInfoService {

    @Resource
    private ShopCommentInfoMapper shopCommentInfoMapper;

    @Resource
    private ShopInfoMapper shopInfoMapper;

    @Override
    public IPage<ShopCommentInfo> page(ShopCommentInfoListParam param) {
        LambdaQueryWrapper<ShopCommentInfo> wrapper = Wrappers.<ShopCommentInfo>lambdaQuery();

        if (ObjectUtil.isNotNull(param.getScoreEnum()) && ObjectUtil.equal(ShopCommentScoreEnum.ALL, param.getScoreEnum()) && ObjectUtil.isNotNull(param.getScore())) {
            wrapper.eq(ShopCommentInfo::getAllScore, param.getScore());
        } else if (ObjectUtil.isNotNull(param.getScoreEnum()) && ObjectUtil.equal(ShopCommentScoreEnum.TASTE, param.getScoreEnum()) && ObjectUtil.isNotNull(param.getScore())) {
            wrapper.eq(ShopCommentInfo::getTasteScore, param.getScore());
        } else if (ObjectUtil.isNotNull(param.getScoreEnum()) && ObjectUtil.equal(ShopCommentScoreEnum.ENVIRONMENT, param.getScoreEnum()) && ObjectUtil.isNotNull(param.getScore())) {
            wrapper.eq(ShopCommentInfo::getEnvironmentScore, param.getScore());
        } else if (ObjectUtil.isNotNull(param.getScoreEnum()) && ObjectUtil.equal(ShopCommentScoreEnum.SERVICE, param.getScoreEnum()) && ObjectUtil.isNotNull(param.getScore())) {
            wrapper.eq(ShopCommentInfo::getServiceScore, param.getScore());
        }

        return shopCommentInfoMapper.selectPage(param.page(), wrapper
                .eq(ObjectUtil.isNotNull(param.getState()), ShopCommentInfo::getState, param.getState())
                .ge(ObjectUtil.isNotNull(param.getStartTime()), ShopCommentInfo::getCreateTime, param.getStartTime())
                .le(ObjectUtil.isNotNull(param.getEndTime()), ShopCommentInfo::getCreateTime, param.getEndTime())
                .orderByDesc(ShopCommentInfo::getCreateTime)
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        );

    }

    @Override
    public void audit(ShopCommentInfoAuditParam param) {
        ShopCommentInfo info = new ShopCommentInfo();
        BeanUtil.copyProperties(param, info);

        int count = shopCommentInfoMapper.updateById(info);

        if (count <= 0) {
            throw new ServiceException("更新失败，请重试");
        }
    }

    @Override
    public void delete(ShopCommentInfoDeleteParam param) {
        int count = shopCommentInfoMapper.deleteById(param.getId());

        if (count <= 0) {
            throw new ServiceException("删除失败，请重试");
        }
    }

    @Override
    public ShopStarRatingCalculateResult getShopOperateInfo(ShopOperateInfoGetParam param) {
        ShopStarRatingCalculateResult result = new ShopStarRatingCalculateResult();

        LambdaQueryWrapper<ShopCommentInfo> wrapper = Wrappers.<ShopCommentInfo>lambdaQuery();

        if (ObjectUtil.isNotNull(param.getScoreEnum()) && ObjectUtil.equal(ShopCommentScoreEnum.ALL, param.getScoreEnum()) && ObjectUtil.isNotNull(param.getScore())) {
            wrapper.eq(ShopCommentInfo::getAllScore, param.getScore());
        } else if (ObjectUtil.isNotNull(param.getScoreEnum()) && ObjectUtil.equal(ShopCommentScoreEnum.TASTE, param.getScoreEnum()) && ObjectUtil.isNotNull(param.getScore())) {
            wrapper.eq(ShopCommentInfo::getTasteScore, param.getScore());
        } else if (ObjectUtil.isNotNull(param.getScoreEnum()) && ObjectUtil.equal(ShopCommentScoreEnum.ENVIRONMENT, param.getScoreEnum()) && ObjectUtil.isNotNull(param.getScore())) {
            wrapper.eq(ShopCommentInfo::getEnvironmentScore, param.getScore());
        } else if (ObjectUtil.isNotNull(param.getScoreEnum()) && ObjectUtil.equal(ShopCommentScoreEnum.SERVICE, param.getScoreEnum()) && ObjectUtil.isNotNull(param.getScore())) {
            wrapper.eq(ShopCommentInfo::getServiceScore, param.getScore());
        }

        List<ShopCommentInfo> shopCommentInfos = shopCommentInfoMapper.selectList(wrapper
                .eq(ShopCommentInfo::getShopId, param.getId())
                .eq(ShopCommentInfo::getIsDelete, 0)
                .eq(ShopCommentInfo::getState, ShopCommentStateEnum.PASS)
                .ge(ObjectUtil.isNotNull(param.getStartTime()), ShopCommentInfo::getCreateTime, param.getStartTime())
                .le(ObjectUtil.isNotNull(param.getEndTime()), ShopCommentInfo::getCreateTime, param.getEndTime())
        );

        //没有对应数据，返回默认值
        if (null == shopCommentInfos || shopCommentInfos.size() == 0) {
            return result;
        }

        BigDecimal sum = new BigDecimal(0);
        for (ShopCommentInfo item : shopCommentInfos) {
            sum = sum.add(new BigDecimal(ObjectUtil.isNull(item.getAllScore())?0 : item.getAllScore()));
            sum = sum.add(new BigDecimal(ObjectUtil.isNull(item.getTasteScore())? 0 : item.getAllScore()));
            sum = sum.add(new BigDecimal(ObjectUtil.isNull(item.getEnvironmentScore())?0 : item.getEnvironmentScore()));
            sum = sum.add(new BigDecimal(ObjectUtil.isNull(item.getServiceScore())? 0 : item.getServiceScore()));
        }

        //求和评论分
        double average = sum.divide(new BigDecimal(shopCommentInfos.size()).multiply(new BigDecimal(4)), 2, RoundingMode.HALF_UP).doubleValue();

        //统计好评数(>=4)
        Long goodCount = shopCommentInfos.stream().filter(item->item.getAllScore() >= 4).count();

        //统计差评数(<=2)
        Long badCount = shopCommentInfos.stream().filter(item->item.getAllScore() <= 2).count();

        result.setAverage(average);
        result.setGoodCount(goodCount);
        result.setBadCount(badCount);
        return result;
    }

    @Override
    public IPage<ShopCommentInfo> getShopOperateCommentInfoPage(ShopOperateInfoGetParam param) {
        LambdaQueryWrapper<ShopCommentInfo> wrapper = Wrappers.<ShopCommentInfo>lambdaQuery();

        if (ObjectUtil.isNotNull(param.getScoreEnum()) && ObjectUtil.equal(ShopCommentScoreEnum.ALL, param.getScoreEnum()) && ObjectUtil.isNotNull(param.getScore())) {
            wrapper.eq(ShopCommentInfo::getAllScore, param.getScore());
        } else if (ObjectUtil.isNotNull(param.getScoreEnum()) && ObjectUtil.equal(ShopCommentScoreEnum.TASTE, param.getScoreEnum()) && ObjectUtil.isNotNull(param.getScore())) {
            wrapper.eq(ShopCommentInfo::getTasteScore, param.getScore());
        } else if (ObjectUtil.isNotNull(param.getScoreEnum()) && ObjectUtil.equal(ShopCommentScoreEnum.ENVIRONMENT, param.getScoreEnum()) && ObjectUtil.isNotNull(param.getScore())) {
            wrapper.eq(ShopCommentInfo::getEnvironmentScore, param.getScore());
        } else if (ObjectUtil.isNotNull(param.getScoreEnum()) && ObjectUtil.equal(ShopCommentScoreEnum.SERVICE, param.getScoreEnum()) && ObjectUtil.isNotNull(param.getScore())) {
            wrapper.eq(ShopCommentInfo::getServiceScore, param.getScore());
        }

        return shopCommentInfoMapper.selectPage(param.page(), wrapper
                .eq(ShopCommentInfo::getShopId, param.getId())
                .eq(ObjectUtil.isNotNull(param.getState()), ShopCommentInfo::getState, param.getState())
                .ge(ObjectUtil.isNotNull(param.getStartTime()), ShopCommentInfo::getCreateTime, param.getStartTime())
                .le(ObjectUtil.isNotNull(param.getEndTime()), ShopCommentInfo::getCreateTime, param.getEndTime())
        );
    }

    @Override
    public ShopCommentStarRatingSortInfoResult getStarRatingSort(ShopCommentStarRatingSortInfoParam param) {
        String authSql = param.getDataAuthParam().getAliasCompleteSql("a");
        List<ShopCommentStarRatingSortInfo> topInfo = shopCommentInfoMapper.getStarRatingTopInfo(authSql);
        List<ShopCommentStarRatingSortInfo> lastInfo = shopCommentInfoMapper.getStarRatingLastInfo(authSql);

        ShopCommentStarRatingSortInfoResult result = new ShopCommentStarRatingSortInfoResult();

        if (null != topInfo && topInfo.size() > 0) {
            result.setTopInfo(topInfo);
        }

        if (null != lastInfo && lastInfo.size() > 0) {
            result.setLastInfo(lastInfo);
        }

        return result;
    }

    @Override
    public List<ShopCommentStarRatingAvgResult> getShopInfoStarRatingAvg(List<Long> ids) {
        return shopCommentInfoMapper.getShopInfoStarRatingAvg(ids);
    }

    @Override
    public IPage<ShopCommentInfo> getPageCommentInfo(ShopCommentInfoPageApiParam param) {
        //判断是否是登录用户
        MemberInfoResult member = MdcUtil.getCurrentMember();
        LambdaQueryWrapper<ShopCommentInfo> queryWrapper = Wrappers.<ShopCommentInfo>lambdaQuery()
                .eq(ShopCommentInfo::getShopId, param.getShopId())
                .eq(ShopCommentInfo::getIsDelete, 0)
                .orderByDesc(ShopCommentInfo::getCreateTime)
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams());

        //有会员登录信息
        if (null != member && ObjectUtil.isNotNull(member.getId())) {
            queryWrapper.and(j->j.eq(ShopCommentInfo::getState, ShopCommentStateEnum.PASS).or(k->k.ne(ShopCommentInfo::getState, ShopCommentStateEnum.PASS)
                    .eq(ShopCommentInfo::getMemberId, member.getId())));
        } else {
            queryWrapper.eq(ShopCommentInfo::getState, ShopCommentStateEnum.PASS);
        }

        return shopCommentInfoMapper.selectPage(param.page(), queryWrapper).convert(this::memberNameHandler);
    }

    @Override
    public void addCommentInfo(ShopCommentInfoAddApiParam param) {
        MemberInfoResult requireCurrentMember = MdcUtil.getRequireCurrentMember();

        ShopCommentInfo info = new ShopCommentInfo();
        BeanUtil.copyProperties(param, info);

        MdcUtil.setMemberInfo(info, requireCurrentMember);
        info.setTenantId(requireCurrentMember.getTenantId());
        info.setState(ShopCommentStateEnum.NOAUDIT);

        ShopInfo shopInfo = shopInfoMapper.selectById(param.getShopId());
        if (null != shopInfo) {
            info.setShopFullName(shopInfo.getShopName());
            info.setSubTenantId(shopInfo.getRelationId());
        }

        int count = shopCommentInfoMapper.normalInsert(info);
        if (count <= 0) {
            throw new ServiceException("保存失败，请重试");
        }
    }

    @Override
    public ShopCommentInfo get(ShopCommentInfoGetParam param) {
        return shopCommentInfoMapper.selectById(param.getId());
    }

    /**
     * 会员名称转化
     * @param info
     * @return
     */
    private ShopCommentInfo memberNameHandler(ShopCommentInfo info) {
        MemberInfoResult member = MdcUtil.getCurrentMember();
        //有会员登录信息且与当前信息的会员id相等，则设置为昵称为我
        if (null != member && ObjectUtil.isNotNull(member.getId()) && ObjectUtil.equal(member.getId(), info.getMemberId())) {
            info.setNickName("我");
        } else if (ObjectUtil.equal(info.getIsAnonymous(), ShopCommentAnonymousEnum.ANONYMOUS) || StrUtil.isBlank(info.getNickName())) {
            //当前用户未登录设置匿名
            info.setNickName("匿名用户");
        }

        return info;
    }
}
