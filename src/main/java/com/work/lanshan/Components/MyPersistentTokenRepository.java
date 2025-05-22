package com.work.lanshan.Components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class MyPersistentTokenRepository implements PersistentTokenRepository {

    @Autowired
    private RedisTemplate redisTemplate;

    //令牌过期时间
    private final static long TOKEM_VALID_DAYS = 14;

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        String key = token.getUsername();
        String usernamekey=token.getUsername();
        redisTemplate.opsForValue().set(usernamekey,token.getSeries());
        redisTemplate.expire(usernamekey,TOKEM_VALID_DAYS, TimeUnit.DAYS);

        Map<String,String> map=new HashMap<>();
        map.put("username",token.getUsername());
        map.put("series",token.getSeries());
        map.put("tokenValue",token.getTokenValue());
        map.put("data",String.valueOf(token.getDate().getTime()));

        redisTemplate.opsForValue().set(key,map);
        redisTemplate.expire(key,TOKEM_VALID_DAYS,TimeUnit.DAYS);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date data) {
        if(redisTemplate.hasKey(series)){
            redisTemplate.opsForHash().put(series,"tokenValue",tokenValue);
            redisTemplate.opsForHash().put(series,"data",String.valueOf(data.getTime()));
        }
    }


    //获取Token通过series
    @Override
    public PersistentRememberMeToken getTokenForSeries(String series) {

        //创建一个ArrayList用来获取多个value
        List<String> hashKeys = new ArrayList<>();
        hashKeys.add("username");
        hashKeys.add("tokenValue");
        hashKeys.add("date");
        List<String> hashValues = redisTemplate.opsForHash().multiGet(series, hashKeys);


        String username =  hashValues.get(0);
        String tokenValue = hashValues.get(1);
        String date = hashValues.get(2);

        if (null == username || null == tokenValue || null == date) {
            return null;
        }
        long timestamp = Long.valueOf(date);
        Date time = new Date(timestamp);

        return new PersistentRememberMeToken(username, series, tokenValue, time);
    }

    //移除对应的Token
    @Override
    public void removeUserTokens(String username) {
        //因为传入的是username,所以我们先用刚才创建的usernamekey获取到series的值
        Object o = redisTemplate.opsForValue().get(username);

        String key=String.valueOf(o);
        if (o!=null){
            redisTemplate.delete(username);
            redisTemplate.delete(key);
        }

    }
}
