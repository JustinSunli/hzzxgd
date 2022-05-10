package com.scwl.hzzxgd;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scwl.hzzxgd.entity.InitializationInformationEntity;
import com.scwl.hzzxgd.mapper.InitializationInformationMapper;
import com.scwl.hzzxgd.service.impl.InitializationInformationServiceImpl;
import com.scwl.hzzxgd.utils.HttpHelper;
import com.scwl.hzzxgd.utils.JWTUtils;
import com.scwl.hzzxgd.utils.WeChatUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class HzzxgdApplicationTests {
    @Resource
    RedisTemplate<String,String> redisTemplate;
    @Resource
    InitializationInformationMapper initializationInformationMapper;
    @Resource
    InitializationInformationServiceImpl initializationInformationService;
    @Test
    void contextLoads() {
//        redisTemplate.opsForValue().set("suitAccessToken","2JDXPqlW",2, TimeUnit.MINUTES);
//        System.out.println("ok");
        String accessToken =redisTemplate.opsForValue().get("ww53c8160388d016ee"+"permanentCode");
        System.out.println(accessToken);
//        String suiteAccessToken = redisTemplate.opsForValue().get("suiteAccessToken");
//        System.out.println(suiteAccessToken);
//        String providerAccessToken = redisTemplate.opsForValue().get("provider_access_token");
//        System.out.println("providerAccessToken = " + providerAccessToken);
//        String a = redisTemplate.opsForValue().get("a");
//        System.out.println("a = " + a);
//        redisTemplate.delete("ww53c8160388d016ee"+"accessToken");
//        redisTemplate.delete("ww53c8160388d016ee"+"permanentCode");
        System.out.println("ok");
    }

    @Test
    void rides(){
//        List<InitializationInformationEntity> list =
//                initializationInformationService.list(new QueryWrapper<InitializationInformationEntity>().lambda()
//                        .eq(InitializationInformationEntity::getPId,0L)
//                        .eq(InitializationInformationEntity::getRole,"2")
//                        .eq(InitializationInformationEntity::getIsActive,"1"));
//        redisTemplate.opsForValue().set("token", String.valueOf(list),2, TimeUnit.MINUTES);
//        System.out.println("ok");

        String token =redisTemplate.opsForValue().get("token");
        System.out.println(token);
    }

    @Test
    void initializationInformation(){
        InitializationInformationEntity initializationInformationEntity = new InitializationInformationEntity();
        initializationInformationEntity.setPId(null)
                .setMenuUrl("/aaaaaaaaaaa")
                .setMenuName("aaaaaaaaaaaaa")
                .setIcon(null)
                .setIsSingle(null)
                .setRole("2")
                .setIsActive("1")
                .setCreateTime(new Date());
        initializationInformationMapper.insert(initializationInformationEntity);
    }

    @Test
    void get(){
        String role = "1";
        List<InitializationInformationEntity> entityList = new ArrayList<>();
        List<InitializationInformationEntity> list =
                initializationInformationService.list(new QueryWrapper<InitializationInformationEntity>().lambda()
                        .eq(InitializationInformationEntity::getPId,0L)
                        .eq(InitializationInformationEntity::getRole,role)
                        .eq(InitializationInformationEntity::getIsActive,"1"));
        for (InitializationInformationEntity entity : list) {
            String id = entity.getId();
            List<InitializationInformationEntity> entities = initializationInformationService.list(new QueryWrapper<InitializationInformationEntity>().lambda()
                    .eq(InitializationInformationEntity::getPId, id)
                    .eq(InitializationInformationEntity::getRole, role)
                    .eq(InitializationInformationEntity::getIsActive, "1"));
            InitializationInformationEntity initializationInformationEntity = new InitializationInformationEntity();
            initializationInformationEntity.setMenuUrl(entity.getMenuUrl())
                    .setMenuName(entity.getMenuName()).setIcon(entity.getIcon())
                    .setChildren(entities);
            entityList.add(initializationInformationEntity);
        }
        System.out.println(entityList);
    }

    @Test
    void bcrypt(){
//        String str = UUID.randomUUID().toString();
//        String token = JWTUtils.createToken(str);
//        System.out.println("str = " + str);
//        System.out.println("token = " + token);
//        ea4c2514-fc7d-4ac5-9b46-4832de21fa27
//        String token = "Yzy eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlYTRjMjUxNC1mYzdkLTRhYzUtOWI0Ni00ODMyZGUyMWZhMjciLCJleHAiOjE2NTAwMjA5Nzd9.J3irD7JkZ4G5aE9WdtvASqpCDZK1pAj365SGVwYNRvxOWzcjnhEzgNnIHwUF5StX9kPip2GRQKrEwEglll7fkw";
        String token = "Yzy eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0NTQ1NDU0dzVzZDRzNGQsNHNkNGE0ZWR3ZTR3ZSIsImV4cCI6MTY1MDAyMjE4NH0.mQSlDS5Gq9Jv4ghafjKXnfLGqda8ll9S40NTKiQGYCV9DjjNrgm7JZv4PxHmzJk2PSE5LcuU23D2w7gud1GNMQ";
        String s = JWTUtils.validateToken(token);
        System.out.println("s = " + s);
    }

    @Test
    void test(){
        String sCorpID = "ww53c8160388d016ee";
        String sProviderSecret = "fhcBs_K_-la7NtH5C0utzRjYowyeXJlHaVXD0bePowPnb2yeBEWl74CWHA4Uwg4Z";
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("corpid", sCorpID);
            jsonParams.put("provider_secret", sProviderSecret);
            JSONObject jsonObj = HttpHelper.doPost(WeChatUtils.THIRD_BUS_WECHAT_GET_PROVIDER_TOKEN, jsonParams);
            String providerAccessToken = jsonObj.getString("provider_access_token");
            Integer expiresIn = Integer.parseInt(jsonObj.getString("expires_in"));
            redisTemplate.opsForValue().set("providerAccessToken", providerAccessToken, expiresIn, TimeUnit.SECONDS);
            System.out.println("providerAccessToken = " + providerAccessToken);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
