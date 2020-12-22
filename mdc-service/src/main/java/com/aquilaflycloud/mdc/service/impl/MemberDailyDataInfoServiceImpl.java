package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.date.DateUtil;
import com.aquilaflycloud.mdc.mapper.AlipayMemberDailyDataInfoMapper;
import com.aquilaflycloud.mdc.model.alipay.AlipayMemberDailyDataInfo;
import com.aquilaflycloud.mdc.param.member.MemberDailyDataListParam;
import com.aquilaflycloud.mdc.param.member.MemberGrowParam;
import com.aquilaflycloud.mdc.param.member.MemberVisitTimesParam;
import com.aquilaflycloud.mdc.result.member.MemberGrowResult;
import com.aquilaflycloud.mdc.result.member.MemberVisitTimesResult;
import com.aquilaflycloud.mdc.service.MemberDailyDataInfoService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * AlipayMemberDailyDataInfoServiceImpl
 *
 * @author Zengqingjie
 * @date 2020-05-25
 */
@Service
public class MemberDailyDataInfoServiceImpl implements MemberDailyDataInfoService {
    @Resource
    private AlipayMemberDailyDataInfoMapper alipayMemberDailyDataInfoMapper;


    @Override
    public List<AlipayMemberDailyDataInfo> listInfos(MemberDailyDataListParam param) {
        return alipayMemberDailyDataInfoMapper.normalSelectList(Wrappers.<AlipayMemberDailyDataInfo>lambdaQuery()
                .eq(AlipayMemberDailyDataInfo::getDate, param.getDate())
                .eq(AlipayMemberDailyDataInfo::getAppId, param.getAppId())
        );
    }

    @Override
    public int handleInfos(AlipayMemberDailyDataInfo info, String type) {
        int count = 0;

        if ("1".equals(type)) {
            //保存数据
            count = alipayMemberDailyDataInfoMapper.normalInsert(info);
        } else if ("2".equals(type)) {
            //更新数据
            count = alipayMemberDailyDataInfoMapper.normalUpdateById(info);
        }

        return count;
    }

    @Override
    public MemberGrowResult memberGrow(MemberGrowParam param) {
        Date start = DateUtil.parse(param.getStartDate().toString(), "yyyy-MM-dd");
        Date end = DateUtil.parse(param.getEndDate().toString(), "yyyy-MM-dd");
        List<AlipayMemberDailyDataInfo> dataInfos = alipayMemberDailyDataInfoMapper.normalSelectList(Wrappers.<AlipayMemberDailyDataInfo>lambdaQuery()
                .ge(AlipayMemberDailyDataInfo::getDate, DateUtil.format(param.getStartDate(), "yyyy-MM-dd"))
                .le(AlipayMemberDailyDataInfo::getDate, DateUtil.format(param.getEndDate(), "yyyy-MM-dd"))
                .eq(AlipayMemberDailyDataInfo::getAppId, param.getAppId())
                .orderByAsc(AlipayMemberDailyDataInfo::getDate)
        );

        //查询当日实时数据
        AlipayMemberDailyDataInfo currentInfo = alipayMemberDailyDataInfoMapper.normalSelectOne(Wrappers.<AlipayMemberDailyDataInfo>lambdaQuery()
                .eq(AlipayMemberDailyDataInfo::getDate, DateUtil.date().toString("yyyy-MM-dd"))
                .eq(AlipayMemberDailyDataInfo::getAppId, param.getAppId())
        );

        //未有实时数据，创建默认对象
        if (null == currentInfo) {
            currentInfo = AlipayMemberDailyDataInfo.getDefaultInfo(param.getAppId());
        }

        //获取日期列表
        List<String> dates = findDates(start, end);

        //将数据封装到map，key=日期;value=信息
        Map<String, AlipayMemberDailyDataInfo> infoMap = new HashMap<>();
        if (null != dataInfos && dataInfos.size() > 0) {
            for (AlipayMemberDailyDataInfo item : dataInfos) {
                infoMap.put(item.getDate(), item);
            }
        }

        //封装折线图数据
        List<Long> activeUserList = new ArrayList<>();
        List<Long> totalUserList = new ArrayList<>();
        List<Long> visitTimesList = new ArrayList<>();
        List<Long> launchList = new ArrayList<>();
        List<Long> newUserList = new ArrayList<>();
        List<String> dailyDurationList = new ArrayList<>();
        List<String> onceDurationList = new ArrayList<>();
        for (String date : dates) {
            Long activeUser = 0L;
            Long totalUser = 0L;
            Long visitTimes = 0L;
            Long launch = 0L;
            Long newUser = 0L;
            String dailyDuration = "00:00:00";
            String onceDuration = "00:00:00";

            AlipayMemberDailyDataInfo info = infoMap.get(date);
            if (null != info) {
                activeUser = (null == info.getActiveUser() ? 0L : info.getActiveUser());
                totalUser = (null == info.getTotalUser() ? 0L : info.getTotalUser());
                visitTimes = (null == info.getVisitTimes() ? 0L : info.getVisitTimes());
                launch = (null == info.getLaunch() ? 0L : info.getLaunch());
                newUser = (null == info.getNewUser() ? 0L : info.getNewUser());
                dailyDuration = (null == info.getDailyDuration() ? "00:00:00" : info.getDailyDuration());
                onceDuration = (null == info.getOnceDuration() ? "00:00:00" : info.getOnceDuration());
            }

            activeUserList.add(activeUser);
            totalUserList.add(totalUser);
            visitTimesList.add(visitTimes);
            launchList.add(launch);
            newUserList.add(newUser);
            dailyDurationList.add(dailyDuration);
            onceDurationList.add(onceDuration);
        }

        return new MemberGrowResult(currentInfo, dates, activeUserList, totalUserList, visitTimesList, launchList, newUserList, dailyDurationList, onceDurationList);
    }

    @Override
    public MemberVisitTimesResult memberVisitTimes(MemberVisitTimesParam param) {
        Date start = DateUtil.parse(param.getStartDate().toString(), "yyyy-MM-dd");
        Date end = DateUtil.parse(param.getEndDate().toString(), "yyyy-MM-dd");

        List<AlipayMemberDailyDataInfo> dataInfos = alipayMemberDailyDataInfoMapper.normalSelectList(Wrappers.<AlipayMemberDailyDataInfo>lambdaQuery()
                .ge(AlipayMemberDailyDataInfo::getDate, DateUtil.format(param.getStartDate(), "yyyy-MM-dd"))
                .le(AlipayMemberDailyDataInfo::getDate, DateUtil.format(param.getEndDate(), "yyyy-MM-dd"))
                .eq(AlipayMemberDailyDataInfo::getAppId, param.getAppId())
                .orderByAsc(AlipayMemberDailyDataInfo::getDate)
        );

        //获取日期列表
        List<String> dates = findDates(start, end);

        //将数据封装到map，key=日期;value=信息
        Map<String, AlipayMemberDailyDataInfo> infoMap = new HashMap<>();
        if (null != dataInfos && dataInfos.size() > 0) {
            for (AlipayMemberDailyDataInfo item : dataInfos) {
                infoMap.put(item.getDate(), item);
            }
        }

        List<Long> visitTimesList = new ArrayList<>();
        List<Long> launchList = new ArrayList<>();

        for (String date : dates) {
            Long visitTimes = 0L;
            Long launch = 0L;

            AlipayMemberDailyDataInfo info = infoMap.get(date);
            if (null != info) {
                visitTimes = (null == info.getVisitTimes() ? 0L : info.getVisitTimes());
                launch = (null == info.getLaunch() ? 0L : info.getLaunch());
            }

            visitTimesList.add(visitTimes);
            launchList.add(launch);
        }

        return new MemberVisitTimesResult(dates, visitTimesList, launchList);
    }


    public static List<String> findDates(Date dBegin, Date dEnd) {
        List<String> lDate = new ArrayList<String>();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        lDate.add(sd.format(dBegin));
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(sd.format(calBegin.getTime()));
        }
        return lDate;
    }
}
