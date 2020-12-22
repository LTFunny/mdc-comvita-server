package com.aquilaflycloud.mdc.component;

import cn.hutool.core.date.DateTime;
import com.aquilaflycloud.auth.enums.UserTypeEnum;
import com.aquilaflycloud.mdc.enums.common.AuditStateEnum;
import com.aquilaflycloud.mdc.enums.common.CreateSourceEnum;
import com.aquilaflycloud.mdc.enums.common.StateEnum;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * AfcMetaObjectHandler 填充器
 *
 * @author star
 * @date 2019-09-24
 */
@Component
public class MdcMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        DateTime dateTime = DateTime.now();
        this.strictInsertFill(metaObject, "isDelete", Integer.class, 0);
        this.strictInsertFill(metaObject, "state", StateEnum.class, StateEnum.NORMAL);
        this.strictInsertFill(metaObject, "createSource", CreateSourceEnum.class, CreateSourceEnum.NORMAL);
        this.strictInsertFill(metaObject, "createTime", Date.class, dateTime);
        this.strictInsertFill(metaObject, "lastUpdateTime", Date.class, dateTime);
        this.strictInsertFill(metaObject, "tenantName", String.class, getTenantName());
        this.strictInsertFill(metaObject, "subTenantName", String.class, getSubTenantName());
        if (MdcUtil.getCurrentUser() != null) {
            if (MdcUtil.getCurrentUser().getType() == UserTypeEnum.SHOPEMPLOYEE) {
                this.strictInsertFill(metaObject, "auditState", AuditStateEnum.class, AuditStateEnum.PENDING);
            } else {
                this.strictInsertFill(metaObject, "auditState", AuditStateEnum.class, AuditStateEnum.APPROVE);
            }
            this.strictInsertFill(metaObject, "creatorId", Long.class, MdcUtil.getCurrentUserId());
            this.strictInsertFill(metaObject, "creatorName", String.class, MdcUtil.getCurrentUserName());
            this.strictInsertFill(metaObject, "lastOperatorId", Long.class, MdcUtil.getCurrentUserId());
            this.strictInsertFill(metaObject, "lastOperatorName", String.class, MdcUtil.getCurrentUserName());
            this.strictInsertFill(metaObject, "creatorOrgIds", String.class, MdcUtil.getCurrentOrgIds());
            this.strictInsertFill(metaObject, "creatorOrgNames", String.class, MdcUtil.getCurrentOrgNames());
        }
        this.strictInsertFill(metaObject, "appKey", String.class, MdcUtil.getAppKey());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "lastUpdateTime", Date.class, DateTime.now());
        if (MdcUtil.getCurrentUser() != null) {
            this.strictUpdateFill(metaObject, "lastOperatorId", Long.class, MdcUtil.getCurrentUserId());
            this.strictUpdateFill(metaObject, "lastOperatorName", String.class, MdcUtil.getCurrentUserName());
        }
    }

    private String getTenantName() {
        if (MdcUtil.getCurrentUserInfo() != null && MdcUtil.getCurrentUserInfo().getTenant() != null) {
            return MdcUtil.getCurrentUserInfo().getTenant().getOrgName();
        }
        return null;
    }

    private String getSubTenantName() {
        if (MdcUtil.getCurrentUserInfo() != null && MdcUtil.getCurrentUserInfo().getSubTenant() != null) {
            return MdcUtil.getCurrentUserInfo().getSubTenant().getOrgName();
        }
        return null;
    }
}
