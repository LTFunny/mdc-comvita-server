package com.aquilaflycloud.mdc.component.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class AfterRollbackEvent extends ApplicationEvent {
    @Getter
    private Runnable runnable;

    private AfterRollbackEvent(Object source, Runnable runnable) {
        super(source);
        this.runnable = runnable;
    }

    public static AfterRollbackEvent build(Object source, Runnable runnable) {
        return new AfterRollbackEvent(source, runnable);
    }
}