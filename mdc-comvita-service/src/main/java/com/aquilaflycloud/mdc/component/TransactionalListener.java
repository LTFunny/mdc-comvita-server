package com.aquilaflycloud.mdc.component;

import cn.hutool.core.util.StrUtil;
import com.aquilaflycloud.mdc.component.event.AfterCommitEvent;
import com.aquilaflycloud.mdc.component.event.AfterRollbackEvent;
import com.aquilaflycloud.mdc.util.MdcUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class TransactionalListener {
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void afterCommitEvent(AfterCommitEvent event) {
        Object source = event.getSource();
        log.info(StrUtil.toString(source));
        MdcUtil.getTtlExecutorService().submit(event.getRunnable());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void afterRollbackEvent(AfterRollbackEvent event) {
        Object source = event.getSource();
        log.info(StrUtil.toString(source));
        event.getRunnable().run();
    }
}