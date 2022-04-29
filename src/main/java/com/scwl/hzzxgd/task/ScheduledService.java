package com.scwl.hzzxgd.task;

import com.scwl.hzzxgd.utils.HttpHelper;
import com.scwl.hzzxgd.utils.WeChatUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ScheduledService {
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    private static final String S_CORP_ID = "ww53c8160388d016ee";
    private static final String S_PROVIDER_SECRET = "fhcBs_K_-la7NtH5C0utzRjYowyeXJlHaVXD0bePowPnb2yeBEWl74CWHA4Uwg4Z";

    @Scheduled(cron = "0 0 0/2 * * *")
    public void providerAccessToken() throws JSONException, IOException {
        JSONObject jsonParams = new JSONObject();
        jsonParams.put("corpid", S_CORP_ID);
        jsonParams.put("provider_secret", S_PROVIDER_SECRET);
        JSONObject jsonObj = HttpHelper.doPost(WeChatUtils.THIRD_BUS_WECHAT_GET_PROVIDER_TOKEN, jsonParams);
        String providerAccessToken = jsonObj.getString("provider_access_token");
        Integer expiresIn = Integer.parseInt(jsonObj.getString("expires_in"));
        redisTemplate.opsForValue().set("providerAccessToken", providerAccessToken, expiresIn, TimeUnit.SECONDS);
        System.out.println("providerAccessToken--------------------------------------------");
    }

//    @Scheduled(cron = "0/50 * * * * *")
//    public void scheduled(){
//        redisTemplate.opsForValue().set("a", "a", 120, TimeUnit.SECONDS);
//        log.info("=====>>>>>使用cron  {}",System.currentTimeMillis());
//    }
//
//    @Scheduled(fixedRate = 5000)
//    public void scheduled1() {
//        log.info("=====>>>>>使用fixedRate{}", System.currentTimeMillis());
//    }
//
//    @Scheduled(fixedDelay = 5000)
//    public void scheduled2() {
//        log.info("=====>>>>>fixedDelay{}",System.currentTimeMillis());
//    }

}
