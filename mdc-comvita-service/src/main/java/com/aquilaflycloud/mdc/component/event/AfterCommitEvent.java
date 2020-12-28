package com.aquilaflycloud.mdc.component.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class AfterCommitEvent extends ApplicationEvent {
    @Getter
    private Runnable runnable;

    private AfterCommitEvent(Object source, Runnable runnable) {
        super(source);
        this.runnable = runnable;
    }

    public static AfterCommitEvent build(Object source, Runnable runnable) {
        return new AfterCommitEvent(source, runnable);
    }
}