package com.aquilaflycloud.mdc.extra.wechat.component;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.redis.WxRedisOps;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@RequiredArgsConstructor
public class RedisWxRedisOps implements WxRedisOps {

  private final RedisTemplate<String, String> redisTemplate;

  private final RedissonClient redissonClient;

  @Override
  public String getValue(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  @Override
  public void setValue(String key, String value, int expire, TimeUnit timeUnit) {
    if (expire <= 0) {
      redisTemplate.opsForValue().set(key, value);
    } else {
      redisTemplate.opsForValue().set(key, value, expire, timeUnit);
    }
  }

  @Override
  public Long getExpire(String key) {
    return redisTemplate.getExpire(key);
  }

  @Override
  public void expire(String key, int expire, TimeUnit timeUnit) {
    redisTemplate.expire(key, expire, timeUnit);
  }

  @Override
  public Lock getLock(@NonNull String key) {
    return redissonClient.getLock(key);
  }
}
