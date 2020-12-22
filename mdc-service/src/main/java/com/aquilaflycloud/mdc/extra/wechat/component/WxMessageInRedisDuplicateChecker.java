package com.aquilaflycloud.mdc.extra.wechat.component;

import com.aquilaflycloud.util.RedisUtil;
import me.chanjar.weixin.common.api.WxMessageDuplicateChecker;

import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * 基于redis实现消息重复检查器.
 * 将每个消息id保存在内存里，每隔5秒清理已经过期的消息id，每个消息id的过期时间是15秒
 * </pre>
 *
 * @author star
 */
public class WxMessageInRedisDuplicateChecker implements WxMessageDuplicateChecker {

    /**
     * 一个消息ID在内存的过期时间：15秒.
     */
    private final int timeToLive;

    /**
     * 无参构造方法.
     * <pre>
     * 一个消息ID在内存的过期时间：15秒
     * </pre>
     */
    public WxMessageInRedisDuplicateChecker() {
        this.timeToLive = 15;
    }

    /**
     * 构造方法.
     *
     * @param timeToLive 一个消息ID在内存的过期时间：秒
     */
    public WxMessageInRedisDuplicateChecker(int timeToLive) {
        this.timeToLive = timeToLive;
    }

    @Override
    public boolean isDuplicate(String messageId) {
        if (messageId == null) {
            return false;
        }
        long time = System.currentTimeMillis();
        return RedisUtil.syncLoad("DuplicateChecker", () -> {
            Boolean result = RedisUtil.redis().hasKey(messageId);
            assert result != null;
            if (!result) {
                RedisUtil.redis().opsForValue().set(messageId, time, timeToLive, TimeUnit.SECONDS);
            }
            return result;
        });
    }
}
