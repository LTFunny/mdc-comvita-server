package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.enums.member.BusinessTypeEnum;
import com.aquilaflycloud.mdc.enums.shop.*;
import com.aquilaflycloud.mdc.extra.wechat.service.WechatMiniService;
import com.aquilaflycloud.mdc.feign.consumer.org.ShopConsumer;
import com.aquilaflycloud.mdc.mapper.ShopCommentInfoMapper;
import com.aquilaflycloud.mdc.mapper.ShopInfoMapper;
import com.aquilaflycloud.mdc.message.ShopInfoErrorEnum;
import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyInfo;
import com.aquilaflycloud.mdc.model.shop.ShopCommentInfo;
import com.aquilaflycloud.mdc.model.shop.ShopInfo;
import com.aquilaflycloud.mdc.param.shop.*;
import com.aquilaflycloud.mdc.param.wechat.MiniProgramQrCodeUnLimitGetParam;
import com.aquilaflycloud.mdc.result.shop.*;
import com.aquilaflycloud.mdc.service.FolksonomyService;
import com.aquilaflycloud.mdc.service.ShopCommentInfoService;
import com.aquilaflycloud.mdc.service.ShopInfoService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.result.OssResult;
import com.aquilaflycloud.util.AliOssUtil;
import com.aquilaflycloud.util.RedisUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.sop.servercommon.exception.ServiceException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * SystemShopInfoServiceImpl
 *
 * @author zengqingjie
 * @date 2020-04-07
 */
@Service
public class ShopInfoServiceImpl implements ShopInfoService {

    @Resource
    private ShopInfoMapper shopInfoMapper;

    @Resource
    private FolksonomyService folksonomyService;

    @Resource
    private ShopCommentInfoService shopCommentInfoService;

    @Resource
    private WechatMiniService wechatMiniService;

    @Resource
    private ShopCommentInfoMapper shopCommentInfoMapper;

    @Resource
    private ShopConsumer shopConsumer;

    @Override
    public IPage<ShopInfo> page(ShopInfoListParam param) {
        return shopInfoMapper.selectPage(param.page(), Wrappers.<ShopInfo>lambdaQuery()
                .eq(ObjectUtil.isNotNull(param.getState()), ShopInfo::getState, param.getState())
                .eq(ObjectUtil.isNotNull(param.getTenantType()), ShopInfo::getTenantType, param.getTenantType())
                .like(StrUtil.isNotBlank(param.getShopName()), ShopInfo::getShopName, param.getShopName())
                .orderByDesc(ShopInfo::getCreateTime)
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        );
    }

    @Override
    public ShopInfoDetailResult getShopInfo(ShopInfoGetParam param) {
        ShopInfo info = shopInfoMapper.selectById(param.getId());
        ShopInfoDetailResult result = Convert.convert(ShopInfoDetailResult.class, info);

        List<FolksonomyInfo> folksonomyInfoList = folksonomyService.getFolksonomyBusinessList(BusinessTypeEnum.SHOP, info.getId());
        result.setFolksonomyInfoList(folksonomyInfoList);
        return result;
    }

    @Override
    public void editShopInfo(ShopInfoEditParam param) {
        ShopInfo info = new ShopInfo();
        BeanUtil.copyProperties(param, info);
        int count = shopInfoMapper.updateById(info);

        if (count > 0) {
            //保存业务功能标签
            folksonomyService.saveFolksonomyBusinessRel(BusinessTypeEnum.SHOP, param.getId(), param.getFolksonomyIds());
            return;
        }
        throw new ServiceException("保存商户信息失败，请重试");
    }

    @Override
    public ShopOperateInfoResult getShopOperateInfo(ShopOperateInfoGetParam param) {
        ShopStarRatingCalculateResult shopOperateInfo = shopCommentInfoService.getShopOperateInfo(param);
        IPage<ShopCommentInfo> shopOperateCommentInfoPage = shopCommentInfoService.getShopOperateCommentInfoPage(param);

        ShopOperateInfoResult result = new ShopOperateInfoResult(shopOperateInfo, shopOperateCommentInfoPage);

        return result;
    }

    @Override
    public void synchronization(ShopSynchronizationInfoGetParam param) {
        Long tenantId = MdcUtil.getCurrentTenantId();
        Serializable value = RedisUtil.redis().opsForValue().get("synchronizationShopInfo" + tenantId);
        if (null != value) {
            throw ShopInfoErrorEnum.SHOP_INFO_SYNCHRONIZATION_ERROR_10801.getErrorMeta().getException();
        }
        RedisUtil.redis().opsForValue().set("synchronizationShopInfo" + tenantId, tenantId, 5, TimeUnit.MINUTES);

        JSONArray infos = shopConsumer.listShopInfo();
        //异步处理商户信息
        MdcUtil.getTtlExecutorService().submit(() -> {
            //获取B0商户数据
            if (null == infos) {
                return;
            }

            //转化类型
            List<ShopInfo> shopInfos = infos.toList(ShopInfo.class);

            //循环设置当前租户id相关信息
            for (int i = 0; i < shopInfos.size(); i++) {
                ShopInfo item = shopInfos.get(i);
                item.setTenantId(tenantId);
            }

            //获取B0商户ids
            List<Long> ids = shopInfos.stream().map(item -> item.getRelationId()).collect(Collectors.toList());

            //获取数据库中存在的数据
            List<ShopInfo> dataInfos = shopInfoMapper.selectList(Wrappers.<ShopInfo>lambdaQuery()
//                    .in(ShopInfo::getRelationId, ids)
                    .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
            );

            //获取数据库ids
            List<Long> dataIds = dataInfos.stream().map(item -> item.getRelationId()).collect(Collectors.toList());

            //数据库中存在，形成更新集合
            List<Long> deleteIds = dataInfos.stream().filter(item -> !ids.contains(item.getRelationId())).map(item->item.getId()).collect(Collectors.toList());

            //将B0获取的商户信息分为更新集合和保存集合
            //数据库中存在，形成更新集合
            List<ShopInfo> updateList = shopInfos.stream().filter(item -> dataIds.contains(item.getRelationId())).collect(Collectors.toList());

            //将更新id设置到更新对象中
            if (null != updateList && updateList.size() > 0) {
                for (int i = 0; i < updateList.size(); i++) {
                    //判断数据库是否有数据
                    if (null == dataInfos || dataInfos.size() <= 0) {
                        break;
                    }

                    //获取当前更新数据
                    ShopInfo updateItem = updateList.get(i);

                    //与数据库数据对比获取id
                    for (int j = 0; j < dataInfos.size(); j++) {
                        ShopInfo dataItem = dataInfos.get(j);
                        //商户id相等，设置id
                        if (ObjectUtil.equal(updateItem.getRelationId(), dataItem.getRelationId())) {
                            updateItem.setId(dataItem.getId());
                            updateItem.setQrCode(dataItem.getQrCode());
                            break;
                        }
                    }
                }
            }

            //保存的商户信息
            List<ShopInfo> insertList = shopInfos.stream().filter(item -> !(dataIds.contains(item.getRelationId()))).collect(Collectors.toList());

            //批量保存商户信息
            if (null != insertList && insertList.size() > 0) {
                for (int i = 0; i < insertList.size(); i++) {
                    //设置id和默认值
                    ShopInfo item = insertList.get(i);
                    item.setId(IdWorker.getId());
                    item.setIsCenturyShop(ShopCenturyShopEnum.NOCENTURYSHOP);
                    item.setIsRecommend(ShopRecommendEnum.NORECOMMEND);
                    item.setShowIntroductionDesc(ShopShowIntroductionDescEnum.NOUSE);
                    item.setShowIntroductionPic(ShopShowIntroductionPicEnum.NOUSE);
                    item.setShowListPic(ShopShowListPicEnum.NOUSE);

                    //区分微信和支付宝
                    if (StrUtil.startWith(param.getAppId(), "wx")) {
                        String url = wechatMiniService.miniCodeUnLimitGet(new MiniProgramQrCodeUnLimitGetParam().setAppId(param.getAppId()).setScene("id=" + item.getId().toString()).setPagePath(param.getUrlParam()));
                        if (null != url && StringUtils.isNotBlank(url)) {
                            item.setQrCode(url);
                        }
                    }
                }
                int insertCount = shopInfoMapper.normalInsertAllBatch(insertList);
            }

            //更新商户信息
            if (null != updateList && updateList.size() > 0) {
                for (int i = 0; i < updateList.size(); i++) {
                    ShopInfo item = updateList.get(i);

                    //判断是否有商户二维码，没有则生成
                    if (StringUtils.isBlank(item.getQrCode())) {
                        //区分微信和支付宝
                        if (StrUtil.startWith(param.getAppId(), "wx")) {
                            String url = wechatMiniService.miniCodeUnLimitGet(new MiniProgramQrCodeUnLimitGetParam().setAppId(param.getAppId()).setScene("id=" + item.getId().toString()).setPagePath(param.getUrlParam()));
                            if (null != url && StringUtils.isNotBlank(url)) {
                                item.setQrCode(url);
                            }
                        }
                    }

                    int updateCount = shopInfoMapper.normalUpdateById(item);
                }
            }

            //批量逻辑删除店铺
            if (null != deleteIds && deleteIds.size() > 0) {
                int deleteCount = shopInfoMapper.deleteBatchIds(deleteIds);
            }
        });

    }

    @Override
    public BaseResult<String> downloadShopCode(ShopDownloadMiniCodeParam param) {
        List<ShopInfo> systemShopInfos = shopInfoMapper.selectList(Wrappers.<ShopInfo>lambdaQuery()
                .eq(ObjectUtil.isNotNull(param.getState()), ShopInfo::getState, param.getState())
                .eq(ObjectUtil.isNotNull(param.getTenantType()), ShopInfo::getTenantType, param.getTenantType())
                .like(StrUtil.isNotBlank(param.getShopName()), ShopInfo::getShopName, param.getShopName())
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        );

        if (null == systemShopInfos || systemShopInfos.size() <= 0) {
            return null;
        }

        List<InputStream> isList = new ArrayList<>();
        List<String> pathList = new ArrayList<>();
        try {
            for (ShopInfo item : systemShopInfos) {
                if (StrUtil.isBlank(item.getQrCode())) {
                    continue;
                }

                //网络请求获取图片数据
                InputStream inputStream = HttpUtil.createGet(item.getQrCode()).execute().bodyStream();

                if (null != inputStream) {
                    isList.add(inputStream);
                    pathList.add(StrUtil.join("-", item.getShopFullName(), item.getStoreCode(), DateUtil.format(DateUtil.date(), "yyyyMMddHHmm")) + item.getId() + ".png");
                }
            }

            File tmpDirFile = Files.createTempDirectory("shop-codes-temp").toFile();
            File resultFile = File.createTempFile("codes", ".zip", tmpDirFile);
            File zipFile = ZipUtil.zip(resultFile, pathList.toArray(new String[0]), isList.toArray(new InputStream[0]));
            String url = AliOssUtil.uploadFile("shopCodeZip.zip", new FileInputStream(zipFile));
            return new BaseResult<String>().setResult(url);
        } catch (Exception e) {
            throw new ServiceException("批量下载二维码失败");
        }
    }

    @Override
    public IPage<ShopInfoResult> pageShopInfo(ShopInfoPageApiParam param) {
        //如果没有传参数，设置默认值
        if (null == param.getSortEnum()) {
            param.setSortEnum(ShopInfoSortEnum.SMART_SORT);
        }

        String authSql = param.getDataAuthParam().getCompleteSql();
        //查询分页信息
        IPage infos = shopInfoMapper.selectPageInfo(param.page(), authSql, param);

        //如果查询无分页数据，直接返回
        if (null == infos || null == infos.getRecords() || infos.getRecords().size() <= 0) {
            return infos;
        }

        //分页数据id集合
        List<ShopInfo> records = infos.getRecords();
        List<Long> ids = records.stream().map(ShopInfo::getId).collect(Collectors.toList());

        //转换类型
        List<ShopInfoResult> resultList = records.stream().map(item -> Convert.convert(ShopInfoResult.class, item)).collect(Collectors.toList());

        //获取分页数据集合的评论平均分
        List<ShopCommentStarRatingAvgResult> avgResults = shopCommentInfoService.getShopInfoStarRatingAvg(ids);
        Map<Long, BigDecimal> avgMap = new HashMap<>();
        //设置平均分到map
        if (null != avgResults && avgResults.size() > 0) {
            for (int i = 0; i < avgResults.size(); i++) {
                ShopCommentStarRatingAvgResult item = avgResults.get(i);
                avgMap.put(item.getShopId(), item.getAverage());
            }
        }

        for (int i = 0; i < resultList.size(); i++) {
            ShopInfoResult item = resultList.get(i);

            //有值设置，无值默认值
            BigDecimal avg = avgMap.get(item.getId());
            if (null != avg) {
                item.setAverage(avg);
            }

            //查询标签列表
            List<FolksonomyInfo> folksonomyInfoList = folksonomyService.getFolksonomyBusinessList(BusinessTypeEnum.SHOP, item.getId());
            if (null != folksonomyInfoList && folksonomyInfoList.size() > 0) {
                item.setFolksonomyInfoList(folksonomyInfoList);
            }
        }

        //封装数据
        IPage<ShopInfoResult> result = new Page<>(infos.getCurrent(), infos.getSize(), infos.getTotal());
        result.setPages(infos.getPages());
        result.setRecords(resultList);
        return result;
    }

    @Override
    public ShopInfoGetResult getShopInfos(ShopInfoGetApiParam param) {
        if (ObjectUtil.isNull(param.getId()) && ObjectUtil.isNull(param.getRelationId())) {
            throw new ServiceException("id不能为空");
        }

        ShopInfo info = new ShopInfo();
        if (ObjectUtil.isNotNull(param.getId())){
            info = shopInfoMapper.selectById(param.getId());
        } else if(ObjectUtil.isNotNull(param.getRelationId())){
            info = shopInfoMapper.selectOne(Wrappers.<ShopInfo> lambdaQuery()
                    .eq(ShopInfo::getRelationId, param.getRelationId())
            );
        }

        ShopInfoGetResult result = Convert.convert(ShopInfoGetResult.class, info);

        //查询标签
        List<FolksonomyInfo> folksonomyInfoList = folksonomyService.getFolksonomyBusinessList(BusinessTypeEnum.SHOP, info.getId());
        result.setFolksonomyInfoList(folksonomyInfoList);

        //查询评分
        List<ShopCommentInfo> shopCommentInfos = shopCommentInfoMapper.selectList(Wrappers.<ShopCommentInfo>lambdaQuery()
                .eq(ShopCommentInfo::getShopId, param.getId())
                .eq(ShopCommentInfo::getIsDelete, 0)
                .eq(ShopCommentInfo::getState, ShopCommentStateEnum.PASS)
        );

        //求和评论分
        if (null != shopCommentInfos && shopCommentInfos.size() > 0) {
            BigDecimal sum = new BigDecimal(0);
            for (ShopCommentInfo item : shopCommentInfos) {
                sum = sum.add(new BigDecimal(ObjectUtil.isNull(item.getAllScore())?0 : item.getAllScore()));
            }
            BigDecimal average = sum.divide(new BigDecimal(shopCommentInfos.size()),  RoundingMode.HALF_UP);
            result.setAverage(average);
            result.setCount(new Long(shopCommentInfos.size()));
        }

        //查询详情，增加一次点击
        ShopInfo updateInfo = new ShopInfo();
        updateInfo.setId(result.getId());
        if (null == result.getDetailClickCount()) {
            updateInfo.setDetailClickCount(1L);
        } else {
            updateInfo.setDetailClickCount(result.getDetailClickCount() + 1);
        }

        shopInfoMapper.updateById(updateInfo);
        return result;
    }

    @Override
    public BaseResult<String> downloadCode(ShopDownloadCodeParam param) {
        //网络请求获取图片数据
        InputStream in = HttpUtil.createGet(param.getQrCode()).execute().bodyStream();
        if (null == in) {
            throw new ServiceException("下载店铺二维码失败，请重试");
        }
        String name = StrUtil.nullToDefault("店铺二维码", MdcUtil.getSnowflakeIdStr()) + DateUtil.format(DateUtil.date(), "yyyyMMddHHmm").toString();
        String fileName = StrUtil.appendIfMissing(name, ".png");

        String path = "ShopInfoCode/";
        OssResult result = AliOssUtil.uploadFileReturn(path, fileName, in, null);
        return new BaseResult<String>().setResult(result.getUrl());
    }
}
