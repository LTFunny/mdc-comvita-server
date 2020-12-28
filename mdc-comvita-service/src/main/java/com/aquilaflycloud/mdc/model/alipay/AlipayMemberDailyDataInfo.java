package com.aquilaflycloud.mdc.model.alipay;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * 支付宝小程序友盟统计活跃用户数、新增用户数、总用户数、启动数
 * @author Zengqingjie
 * @date 2020-05-19
 */
@Data
@TableName(value = "alipay_member_daily_data_info")
public class AlipayMemberDailyDataInfo implements Serializable {
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    @TableField(value = "date")
    @ApiModelProperty(value = "统计日期")
    private String date;

    @TableField(value = "active_user")
    @ApiModelProperty(value = "活跃用户数")
    private Long activeUser;

    @TableField(value = "total_user")
    @ApiModelProperty(value = "累加总用户数")
    private Long totalUser;

    @TableField(value = "visit_times")
    @ApiModelProperty(value = "访次")
    private Long visitTimes;

    @TableField(value = "launch")
    @ApiModelProperty(value = "启动数")
    private Long launch;

    @TableField(value = "new_user")
    @ApiModelProperty(value = "新增用户数")
    private Long newUser;

    @TableField(value = "daily_duration")
    @ApiModelProperty(value = "人均停留时长")
    private String dailyDuration;

    @TableField(value = "once_duration")
    @ApiModelProperty(value = "次均停留时长")
    private String onceDuration;

    @TableField(value = "appId")
    @ApiModelProperty(value = "appId")
    private String appId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 最后更新时间
     */
    @TableField(value = "last_update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "最后更新时间")
    private Date lastUpdateTime;

    public AlipayMemberDailyDataInfo(String date, Long activeUser, Long totalUser, Long visitTimes, Long launch, Long newUser, String dailyDuration, String onceDuration, String appId, Date lastUpdateTime) {
        this.date = date;
        this.activeUser = activeUser;
        this.totalUser = totalUser;
        this.visitTimes = visitTimes;
        this.launch = launch;
        this.newUser = newUser;
        this.dailyDuration = dailyDuration;
        this.onceDuration = onceDuration;
        this.appId = appId;
        this.lastUpdateTime = lastUpdateTime;
    }

    public AlipayMemberDailyDataInfo() {
    }

    public static AlipayMemberDailyDataInfo getDefaultInfo(String appId) {
        AlipayMemberDailyDataInfo info = new AlipayMemberDailyDataInfo(DateUtil.date().toString("yyyy-MM-dd"), 0L, 0L, 0L, 0L, 0L, "00:00:00", "00:00:00", appId, DateUtil.date());
        return info;
    }
}
