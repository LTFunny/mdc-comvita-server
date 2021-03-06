package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.DynaBean;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.aquilaflycloud.dataAuth.bean.DataAuthParam;
import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.dataAuth.util.DataAuthUtil;
import com.aquilaflycloud.mdc.enums.member.UploadBusinessTypeEnum;
import com.aquilaflycloud.mdc.enums.system.ExportStateEnum;
import com.aquilaflycloud.mdc.enums.system.ExportTypeEnum;
import com.aquilaflycloud.mdc.mapper.MemberFileUploadRecordMapper;
import com.aquilaflycloud.mdc.mapper.SystemExportLogMapper;
import com.aquilaflycloud.mdc.model.member.MemberFileUploadRecord;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.aquilaflycloud.mdc.model.system.SystemExportLog;
import com.aquilaflycloud.mdc.param.coupon.CouponRelPageParam;
import com.aquilaflycloud.mdc.param.exchange.OrderPageParam;
import com.aquilaflycloud.mdc.param.member.MemberPageParam;
import com.aquilaflycloud.mdc.param.member.RewardRecordPageParam;
import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.param.system.*;
import com.aquilaflycloud.mdc.result.system.SqlResult;
import com.aquilaflycloud.mdc.service.*;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.result.OssResult;
import com.aquilaflycloud.util.AliOssUtil;
import com.aquilaflycloud.util.RedisUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import com.gitee.sop.servercommon.param.ParamValidator;
import com.gitee.sop.servercommon.param.ServiceParamValidator;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * SystemFileServiceImpl
 *
 * @author star
 * @date 2019-12-09
 */
@Slf4j
@Service
public class SystemFileServiceImpl implements SystemFileService {
    private final ParamValidator paramValidator = new ServiceParamValidator();
    @Resource
    private SystemExportLogMapper systemExportLogMapper;
    @Resource
    private MemberFileUploadRecordMapper memberFileUploadRecordMapper;
    @Resource
    private SystemSqlInjectorService systemSqlInjectorService;
    @Resource
    private MemberService memberService;
    @Resource
    private ExchangeService exchangeService;
    @Resource
    private CouponInfoService couponInfoService;
    @Resource
    private MemberRewardService memberRewardService;
    @Resource
    private PrePickingCardService prePickingCardService;
    @Resource
    private PreOrderAdministrationService preOrderAdministrationService;
    @Resource
    private PreActivityService preActivityService;


    private IPage pageData(ExcelDownloadParam param) {
        IPage page;
        switch (param.getExportType()) {
            case DATA_EXPORT: {
                SqlExecutePageParam exportParam = buildParam(param.getExportParam(), SqlExecutePageParam.class);
                page = systemSqlInjectorService.executePage(exportParam);
                break;
            }
            case MEMBER_INFO: {
                MemberPageParam exportParam = buildParam(param.getExportParam(), MemberPageParam.class, "mdc:member:list");
                page = memberService.pageMemberInfo(exportParam);
                break;
            }
            case MEMBER_REWARD: {
                RewardRecordPageParam exportParam = buildParam(param.getExportParam(), RewardRecordPageParam.class, "mdc:reward:list");
                page = memberRewardService.page(exportParam);
                break;
            }
            case EXCHANGE_ORDER: {
                OrderPageParam exportParam = buildParam(param.getExportParam(), OrderPageParam.class, "mdc:exchangeOrder:list");
                page = exchangeService.pageOrder(exportParam);
                break;
            }
            case COUPON_REL: {
                CouponRelPageParam exportParam = buildParam(param.getExportParam(), CouponRelPageParam.class, "mdc:couponRel:list");
                page = couponInfoService.pageCouponRel(exportParam);
                break;
            }
            case PRE_PICKING_CARD: {
                PrePickingCardPageParam exportParam = buildParam(param.getExportParam(), PrePickingCardPageParam.class, "mdc:pre:picking:card:page");
                page = prePickingCardService.page(exportParam);
                break;
            }
            case READY_INFO: {
                ReportFormParam exportParam = buildParam(param.getExportParam(), ReportFormParam.class);
                page = preOrderAdministrationService.pageOrderReportList(exportParam);
                break;
            }
            case GUIDE_INFO: {
                ReportFormParam exportParam = buildParam(param.getExportParam(), ReportFormParam.class);
                page = preOrderAdministrationService.achievementsGuide(exportParam);
                break;
            }
            case AFTER_SALES: {
                PreRefundOrderListParam exportParam = buildParam(param.getExportParam(), PreRefundOrderListParam.class);
                page = preOrderAdministrationService.pageOrderInfoList(exportParam);
                break;
            }
            case READY_GOODS: {
                ReadyListParam exportParam = buildParam(param.getExportParam(), ReadyListParam.class);
                page = preOrderAdministrationService.pagereadySalesList(exportParam);
                break;
            }
            case ORDER_INFO: {
                AdministrationListParam exportParam = buildParam(param.getExportParam(), AdministrationListParam.class);
                page = preOrderAdministrationService.pageOrderPageResultList(exportParam);
                break;
            }
            case SALES_VOUME: {
                AdministrationListParam exportParam = buildParam(param.getExportParam(), AdministrationListParam.class);
                page = preOrderAdministrationService.pageSalePageResultList(exportParam);
                break;
            }
            case PRE_ACTIVITY_INFO: {
                PreActivityExportParam exportParam = buildParam(param.getExportParam(), PreActivityExportParam.class);
                page = preActivityService.pageExportPreActivityPageResultList(exportParam);
                break;
            }
            case FLASH_ACTIVITY_INFO: {
                FlashExportParam flashExportParam = buildParam(param.getExportParam(), FlashExportParam.class);
                page = preActivityService.pageExportFlashActivityPageResultList(flashExportParam);
                break;
            }
            case FLASH_ORDER_INFO: {
                PreOrderPageParam flashExportParam = buildParam(param.getExportParam(), PreOrderPageParam.class);
                page = preOrderAdministrationService.pagePreOder(flashExportParam);
                break;
            }

            default:
                throw new ServiceException("??????????????????" + param.getExportType());
        }
        return page;
    }

    private Map<String, String> buildAliasMap(ExcelDownloadParam param) {
        Map<String, String> aliasMap = null;
        if (CollUtil.isNotEmpty(param.getExportAliasNames())) {
            aliasMap = param.getExportAliasNames().stream().collect(Collectors.toMap(ExcelDownloadParam.AliasName::getField, ExcelDownloadParam.AliasName::getAlias));
        }
        switch (param.getExportType()) {
            /*case MEMBER_CONSUMPTION_TICKET_INFO: {
                aliasMap = new HashMap<>();
                aliasMap.put("createTime", "????????????");
                aliasMap.put("nickName", "??????");
                aliasMap.put("phoneNumber", "?????????");
                aliasMap.put("shopName", "????????????");
                aliasMap.put("payMoney", "????????????(???)");
                aliasMap.put("rewardValueContent", "??????");
                aliasMap.put("state", "????????????");
                aliasMap.put("lastUpdateTime", "????????????");
                aliasMap.put("auditName", "?????????");
                break;
            }*/
            case READY_GOODS: {
                aliasMap = new HashMap<>();
                aliasMap.put("id", "id");
                aliasMap.put("orderCode", "????????????");
                aliasMap.put("goodsCode", "????????????");
                aliasMap.put("goodsName", "????????????");
                aliasMap.put("cardCode", "????????????");
                aliasMap.put("reserveName", "?????????");
                aliasMap.put("reservePhone", "???????????????");
                aliasMap.put("deliveryAddress", "????????????");
                aliasMap.put("guideName", "?????????");
                aliasMap.put("reserveShop", "????????????");
                aliasMap.put("reserveStartTime", "??????????????????");
                aliasMap.put("expressCode", "????????????");
                aliasMap.put("expressOrderCode", "????????????");
                aliasMap.put("expressName", "????????????");
                break;
            }
            default:
        }
        return aliasMap;
    }

    @Override
    public BaseResult<String> downloadExcel(ExcelDownloadParam param) {
        String key = getKey(param);
        List<Map<String, Object>> rows = searchData(param);
        String url = null;
        if (rows != null) {
            OssResult result = uploadExcel(param, rows);
            SystemExportLog log = new SystemExportLog();
            BeanUtil.copyProperties(result, log);
            log.setExportType(param.getExportType());
            log.setExportContent(JSONUtil.toJsonStr(param));
            log.setState(ExportStateEnum.SUCCESS);
            systemExportLogMapper.insert(log);
            url = result.getUrl();
        } else {
            Long id = RedisUtil.<Long>valueRedis().get(key);
            if (id == null) {
                throw new ServiceException("????????????");
            }
            SystemExportLog log = new SystemExportLog();
            log.setId(id);
            log.setExportType(param.getExportType());
            log.setExportContent(JSONUtil.toJsonStr(param));
            log.setState(ExportStateEnum.PROCESSING);
            systemExportLogMapper.insert(log);
        }
        return new BaseResult<String>().setResult(url);
    }

    private String getKey(ExcelDownloadParam param) {
        String params = JSONUtil.toJsonStr(param) + MdcUtil.getCurrentUserInfo().getMainDepartment();
        return SecureUtil.md5(params);
    }

    private OssResult uploadExcel(ExcelDownloadParam param, List<Map<String, Object>> rows) {
        ExcelWriter writer = ExcelUtil.getBigWriter();
        ((SXSSFWorkbook) writer.getWorkbook()).setCompressTempFiles(true);
        writer.write(rows, true);
        //??????????????????
        XSSFDataFormat format = (XSSFDataFormat) writer.getWorkbook().createDataFormat();
        writer.getStyleSet().getCellStyleForDate().setDataFormat(format.getFormat(DatePattern.NORM_DATETIME_PATTERN));
        //???????????????????????????
        //writer.getStyleSet().getCellStyleForNumber().setDataFormat(HSSFDataFormat.getBuiltinFormat("TEXT"));
        final int columnCount = writer.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            SXSSFSheet sheet = ((SXSSFSheet) writer.getSheet());
            sheet.trackAllColumnsForAutoSizing();
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) * 17 / 10);
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writer.flush(out);
        String path = "ExcelExport/" + param.getExportType().name();
        String name = StrUtil.nullToDefault(param.getExportName(), MdcUtil.getSnowflakeIdStr());
        OssResult result = AliOssUtil.uploadFileReturn(path, StrUtil.appendIfMissing(name, ".xlsx"),
                new ByteArrayInputStream(out.toByteArray()), null);
        writer.close();
        return result;
    }

    private List<Map<String, Object>> searchData(ExcelDownloadParam param) {
        String key = getKey(param);
        Long id = RedisUtil.<Long>valueRedis().get(key);
        if (id != null) {
            throw new ServiceException("?????????????????????,??????????????????");
        }
        JSONObject params = JSONUtil.parseObj(param.getExportParam());
        Integer pageSize = Convert.toInt(params.getLong("pageSize"), Integer.MAX_VALUE);
        AtomicReference<IPage> page = new AtomicReference<>();
        if (pageSize > 1000) {
            params.set("pageSize", 1);
            param.setExportParam(params.toString());
            page.set(pageData(param));
            if (page.get().getTotal() > 1000) {
                Long logId = MdcUtil.getSnowflakeId();
                RedisUtil.valueRedis().set(key, logId, 5, TimeUnit.MINUTES);
                RequestContextHolder.setRequestAttributes(RequestContextHolder.getRequestAttributes(), true);
                MdcUtil.getTtlExecutorService().submit(() -> {
                    try {
                        params.set("pageSize", pageSize);
                        param.setExportParam(params.toString());
                        DateTime start = DateTime.now();
                        page.set(pageData(param));
                        log.info("sql????????????" + DateUtil.betweenMs(start, DateTime.now()));
                        RedisUtil.redis().expire(key, 5, TimeUnit.MINUTES);
                        Map<String, String> aliasMap = buildAliasMap(param);
                        start = DateTime.now();
                        if (param.getExportType() == ExportTypeEnum.DATA_EXPORT) {
                            SqlGetParam sqlGetParam = buildParam(param.getExportParam(), SqlGetParam.class);
                            SqlResult sqlResult = systemSqlInjectorService.get(sqlGetParam);
                            List<String> fields = sqlResult.getColumnList().stream().map(SqlResult.SqlColumn::getName).collect(Collectors.toList());
                            param.setExportFields(fields);
                            aliasMap = sqlResult.getColumnList().stream().collect(Collectors.toMap(SqlResult.SqlColumn::getName, SqlResult.SqlColumn::getAlias));
                        }
                        List<Map<String, Object>> rows = buildRows(page.get().getRecords(), param.getExportFields(), aliasMap);
                        log.info("?????????????????????" + DateUtil.betweenMs(start, DateTime.now()));
                        start = DateTime.now();
                        OssResult result = uploadExcel(param, rows);
                        log.info("??????Excel???????????????" + DateUtil.betweenMs(start, DateTime.now()));
                        SystemExportLog update = new SystemExportLog();
                        BeanUtil.copyProperties(result, update);
                        update.setId(logId);
                        update.setState(ExportStateEnum.SUCCESS);
                        systemExportLogMapper.updateById(update);
                    } catch (Exception e) {
                        SystemExportLog update = new SystemExportLog();
                        update.setId(logId);
                        update.setState(ExportStateEnum.FAILED);
                        systemExportLogMapper.updateById(update);
                        log.error("??????Excel??????", e);
                    } finally {
                        RedisUtil.redis().delete(key);
                    }
                });
                return null;
            } else {
                params.set("pageSize", pageSize);
                param.setExportParam(params.toString());
                page.set(pageData(param));
            }
        } else {
            page.set(pageData(param));
        }
        Map<String, String> aliasMap = buildAliasMap(param);
        if (param.getExportType() == ExportTypeEnum.DATA_EXPORT) {
            SqlGetParam sqlGetParam = buildParam(param.getExportParam(), SqlGetParam.class);
            SqlResult sqlResult = systemSqlInjectorService.get(sqlGetParam);
            List<String> fields = sqlResult.getColumnList().stream().map(SqlResult.SqlColumn::getName).collect(Collectors.toList());
            param.setExportFields(fields);
            aliasMap = sqlResult.getColumnList().stream().collect(Collectors.toMap(SqlResult.SqlColumn::getName, SqlResult.SqlColumn::getAlias));
        }
        return buildRows(page.get().getRecords(), param.getExportFields(), aliasMap);
    }

    private <T> T buildParam(String exportParam, Class<T> clazz, String... dataAuthFunction) {
        T param = JSON.parseObject(exportParam, clazz);
        if (param == null) {
            param = ReflectUtil.newInstance(clazz);
        }
        paramValidator.validateBizParam(param);
        if (ArrayUtils.isNotEmpty(dataAuthFunction)) {
            DynaBean bean = DynaBean.create(param);
            Map<String, DataAuthParam> dataAuth = DataAuthUtil.getDataAuth(dataAuthFunction, MdcUtil.getCurrentUserId());
            MdcUtil.dynaBeanSafeSet(bean, "dataAuthParamMap", dataAuth);
            if (dataAuth.size() > 0) {
                MdcUtil.dynaBeanSafeSet(bean, "dataAuthParam", dataAuth.entrySet().stream().findAny().get().getValue());
            }
        }
        return param;
    }

    private List<Map<String, Object>> buildRows(List resultList, List<String> exportFields, Map<String, String> aliasMap) {
        List<Map<String, Object>> rows = new ArrayList<>();
        for (Object object : resultList) {
            Map<String, Object> row = new LinkedHashMap<>();
            if (object instanceof Map) {
                Map objectMap = (Map) object;
                for (String name : exportFields) {
                    row.put(aliasMap.get(name), objectMap.get(name));
                }
            } else {
                for (Field field : ReflectUtil.getFields(object.getClass())) {
                    if (exportFields.size() > 0 && !exportFields.contains(field.getName())) {
                        continue;
                    }
                    ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
                    if (annotation == null) {
                        continue;
                    }
                    String alias = annotation.value();
                    if (aliasMap != null) {
                        if (aliasMap.containsKey(field.getName())) {
                            alias = aliasMap.get(field.getName());
                        }
                    }
                    Object value = null;
                    if (field.getType().isEnum()) {
                        Object enumField = ReflectUtil.getFieldValue(object, field);
                        if (enumField != null) {
                            value = ReflectUtil.getFieldValue(enumField, "name");
                        }
                    } else if ("Long".equals(field.getType().getSimpleName())) {
                        value = Convert.toStr(ReflectUtil.getFieldValue(object, field));
                    } else {
                        value = ReflectUtil.getFieldValue(object, field);
                    }
                    row.put(alias, value);
                }
            }
            rows.add(row);
        }
        return rows;
    }

    @Override
    public IPage<SystemExportLog> pageExcelLog(ExcelPageParam param) {
        return systemExportLogMapper.selectPage(param.page(), Wrappers.<SystemExportLog>lambdaQuery()
                .eq(param.getExportType() != null, SystemExportLog::getExportType, param.getExportType())
                .eq(param.getState() != null, SystemExportLog::getState, param.getState())
                .ge(param.getStartExportTime() != null, SystemExportLog::getCreateTime, param.getStartExportTime())
                .le(param.getEndExportTime() != null, SystemExportLog::getCreateTime, param.getEndExportTime())
                .orderByDesc(SystemExportLog::getCreateTime)
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        );
    }

    @Override
    public BaseResult<String> uploadFile(ClientFileUploadParam param) {
        MemberInfo memberInfo = MdcUtil.getRequireCurrentMember();
        String appId = MdcUtil.getMemberAppId(memberInfo);
        MultipartFile multipartFile = param.getFile();
        String fileName = multipartFile.getOriginalFilename();
        try {
            File tmpDirFile = Files.createTempDirectory("member-upload-temp").toFile();
            File file = File.createTempFile(StrUtil.subBefore(fileName, ".", true),
                    "." + StrUtil.subAfter(fileName, ".", true), tmpDirFile);
            if (param.getIsImg()) {
                BufferedImage image = ImgUtil.read(multipartFile.getInputStream());
                if (image != null) {
                    ImgUtil.write(image, file);
                } else {
                    throw new ServiceException("????????????????????????");
                }
            } else {
                if (param.getBusinessType() == UploadBusinessTypeEnum.FACE) {
                    throw new ServiceException("????????????????????????");
                }
                FileUtil.writeFromStream(multipartFile.getInputStream(), file);
            }
            OssResult result = AliOssUtil.uploadFileReturn(StrUtil.join("/", param.getBusinessType().name(), appId),
                    fileName, new FileInputStream(file), AliOssUtil.MEMBER_STYLE);
            MdcUtil.getTtlExecutorService().submit(() -> {
                MemberFileUploadRecord record = new MemberFileUploadRecord();
                BeanUtil.copyProperties(result, record);
                record.setBusinessType(param.getBusinessType());
                MdcUtil.setMemberInfo(record, memberInfo);
                memberFileUploadRecordMapper.insert(record);
            });
            return new BaseResult<String>().setResult(result.getUrl());
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException("??????????????????");
        }
    }
}
