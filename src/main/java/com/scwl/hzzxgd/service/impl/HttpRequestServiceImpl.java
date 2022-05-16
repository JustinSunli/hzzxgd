package com.scwl.hzzxgd.service.impl;

import com.scwl.hzzxgd.encryptionAndDecryption.AesException;
import com.scwl.hzzxgd.encryptionAndDecryption.WXBizJsonMsgCrypt;
import com.scwl.hzzxgd.entity.DepartmentListInformationEntity;
import com.scwl.hzzxgd.entity.MemberInformationEntity;
import com.scwl.hzzxgd.exception.SandException;
import com.scwl.hzzxgd.service.*;
import com.scwl.hzzxgd.utils.HttpHelper;
import com.scwl.hzzxgd.utils.JWTUtils;
import com.scwl.hzzxgd.utils.PubUtil;
import com.scwl.hzzxgd.utils.WeChatUtils;
import com.scwl.hzzxgd.vo.LoginForUserAndEnterpriseInfo;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

@Service
@Transactional(rollbackFor = Exception.class)
public class HttpRequestServiceImpl implements HttpRequestService {
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private EnterpriseInformationService enterpriseInformationService;
    @Resource
    private DepartmentListInformationService departmentListInformationService;
    @Resource
    private MemberInformationService memberInformationService;
    @Resource
    private InitializationInformationService initializationInformationService;


    private static final String S_TOKEN = "OY0Q7Ny7ktHl54";
    private static final String S_ENCODING_AES_KEY = "2KiraEkiexJv0k8h5Jr5uwonGxmEmqmMu9HHkTNSBAj";
    private static final String S_SUITE_SECRET = "ERU5Pq8lA4PfbVASK08iuewvo6O5ZxrP9M9reUdVj6w";
    private static final String S_CORP_ID = "ww53c8160388d016ee";
    private static final String S_CORPSECRET = "OZtKSpjMBg-hkqUMiExrc6pzzWTnbk5VHh3iAIHP4KY";
    private static final String S_PROVIDER_SECRET = "fhcBs_K_-la7NtH5C0utzRjYowyeXJlHaVXD0bePowPnb2yeBEWl74CWHA4Uwg4Z";
    private WXBizJsonMsgCrypt wxcpt = null;

//    String sToken = "mHqIqa3fBH6T";
//    String sEncodingAESKey = "6qvGgBXxvyOXzGQUleNtVXRYdCQEFw5fOm9NLMASrtD";
//    String suiteSecret = "TAcyzVZb3oTbaNaD9gPgTmA8UE9Orih_Xi33G6g_OQc";





    /**
     * 不同请求处理
     *
     * @param request
     * @return
     */
    @Override
    @Transactional
    public String RequestHttpRequestService(HttpServletRequest request) {
        try {
            wxcpt = new WXBizJsonMsgCrypt(S_TOKEN, S_ENCODING_AES_KEY);
        } catch (AesException e) {
            e.printStackTrace();
        }
        String context;
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            context = getHttpRequest(request);
        } else if ("POST".equalsIgnoreCase(request.getMethod())) {
            context = postHttpRequest(request);
        } else {
            throw new RuntimeException("请求方式错误");
        }
        return context;
    }


    /**
     * 企业发出get请求解密处理
     *
     * @param request
     * @return
     */
    private String getHttpRequest(HttpServletRequest request) {
        // 解析出url上的参数值如下：
        String msgSignature = request.getParameter("msg_signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echoStr = request.getParameter("echostr");
        String sEchoStr = null; //需要返回的明文
        try {
            sEchoStr = wxcpt.VerifyURL(msgSignature, timestamp,
                    nonce, echoStr);
            System.out.println("verifyurl echostr: " + sEchoStr);
        } catch (Exception e) {
            //验证URL失败，错误原因请查看异常
            e.printStackTrace();
        }
        return sEchoStr;
    }

    /**
     * 企业发出post请求解密处理
     *
     * @param request
     * @return
     */
    private String postHttpRequest(HttpServletRequest request) {
        String msgSignature = request.getParameter("msg_signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        try {
            //获得正文内容
            String messageBody = getMessageBody(request);
            //解析明文XML
            String sMsg = wxcpt.DecryptMsgXML(msgSignature, timestamp, nonce, messageBody);
//            System.out.println("after decrypt msg: " + sMsg);
            // TODO: 解析出明文xml标签的内容进行处理
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            StringReader sr = new StringReader(sMsg);
            InputSource is = new InputSource(sr);
            Document document = db.parse(is);

            Element root = document.getDocumentElement();
            NodeList nodeList1 = root.getElementsByTagName("InfoType");
            Node item = nodeList1.item(0);
            if (PubUtil.isNotEmpty(item)){
                String infoType = item.getTextContent();
//                System.out.println("infoType：" + infoType);
                if ("suite_ticket".equals(infoType)) {
                    if (PubUtil.isEmpty(redisTemplate.opsForValue().get("suiteAccessToken"))){//如果过期或不存在,则存入rides
                        NodeList nodeList2 = root.getElementsByTagName("SuiteTicket");
                        String suiteTicket = nodeList2.item(0).getTextContent();
                        NodeList nodeList3 = root.getElementsByTagName("SuiteId");
                        String suiteId = nodeList3.item(0).getTextContent();
                        JSONObject jsonParams = new JSONObject();
                        jsonParams.put("suite_id", suiteId);
                        jsonParams.put("suite_secret", S_SUITE_SECRET);
                        jsonParams.put("suite_ticket", suiteTicket);
                        JSONObject jsonObj = HttpHelper.doPost(WeChatUtils.THIRD_BUS_WECHAT_SUITE_TOKEN, jsonParams);
                        String suiteAccessToken = jsonObj.getString("suite_access_token");
                        Integer expiresIn = Integer.parseInt(jsonObj.getString("expires_in"));
                        redisTemplate.opsForValue().set("suiteAccessToken", suiteAccessToken, expiresIn, TimeUnit.SECONDS);
                        System.out.println("suiteAccessToken:" + suiteAccessToken);
                        return "success";
                    }else{
                        System.out.println("suiteAccessToken已存在");
                        return "success";
                    }
                } else if ("create_auth".equals(infoType)) {
                    //授权成功
                    NodeList nodeList4 = root.getElementsByTagName("AuthCode");
                    String authCode = nodeList4.item(0).getTextContent();
                    //存入企业信息表
                    String url = WeChatUtils.THIRD_BUS_WECHAT_ACCESS_TOKEN + redisTemplate.opsForValue().get("suiteAccessToken");
                    JSONObject jsonParams = new JSONObject();
                    jsonParams.put("auth_code", authCode);
                    JSONObject jsonObj1 = HttpHelper.doPost(url, jsonParams);
                    System.err.println("jsonObj1 = " + jsonObj1);
                    String corpid = jsonObj1.getJSONObject("auth_corp_info").getString("corpid");
                    System.err.println("corpid = " + corpid);
                    String accessToken = jsonObj1.getString("access_token");
                    String permanentCode = jsonObj1.getString("permanent_code");
                    Integer expiresIn = Integer.parseInt(jsonObj1.getString("expires_in"));
                    redisTemplate.opsForValue().set(corpid + "accessToken", accessToken, expiresIn, TimeUnit.SECONDS);
                    redisTemplate.opsForValue().set(corpid + "permanentCode", permanentCode);
                    enterpriseInformationService.insertAll(jsonObj1);
                    //存入部门信息表
                    String url2 = WeChatUtils.THIRD_BUS_WECHAT_DEPART_LIST + redisTemplate.opsForValue().get(corpid + "accessToken");
                    JSONObject jsonObj2 = HttpHelper.doGet(url2);
                    departmentListInformationService.insertAll(jsonObj2, corpid);
                    //存入部门成员表
                    memberInformationService.insertAll(corpid);
                    return "success";
                } else if ("change_auth".equals(infoType)){
                    //变更授权
                    System.out.println("change_auth");
                    return "success";
                } else if ("cancel_auth".equals(infoType)){
                    //取消授权
                    System.out.println("cancel_auth");
                    return "success";
                } else if("change_contact".equals(infoType)){
                    //成员通知事件 部门通知事件 标签通知事件
                    System.out.println("change_contact");
                    return "success";
                } else{
                    System.out.println("else");
                    return "success";
                }
            }else{
                System.out.println("not have infoType");
                return "success";
            }
        } catch (Exception e) {
            // TODO
            // 解密失败，失败原因请查看异常
            e.printStackTrace();
        }
        return "success";
    }

    /**
     * 登陆
     *
     * @param request
     */
    @Override
    @Transactional
    public LoginForUserAndEnterpriseInfo login(HttpServletRequest request) {
        LoginForUserAndEnterpriseInfo loginForUserAndEnterpriseInfo = new LoginForUserAndEnterpriseInfo();
        if (PubUtil.isEmpty(request.getParameter("code")))
            throw new SandException("登陆失败");
        String code = request.getParameter("code");
//        String corpid = request.getParameter("corpid");
        try {
            //获取访问用户身份
            String url = WeChatUtils.THIRD_BUS_WECHAT_GET_USER_INFO + redisTemplate.opsForValue().get("suiteAccessToken") + "&code=" + code;
            JSONObject jsonObj = HttpHelper.doGet(url);
            if (jsonObj.getInt("errcode") != 0){
                throw new SandException("登陆失败");
            }
            if (PubUtil.isEmpty(jsonObj.getString("UserId"))) {
                throw new SandException("不属于任何企业");
            }else {
                String corpid = jsonObj.getString("CorpId");
                String openUserid = jsonObj.getString("open_userid");
                MemberInformationEntity userInfoByOpenUserId = memberInformationService.getUserInfoByOpenUserId(corpid, openUserid);
                String userid = userInfoByOpenUserId.getId();
                /* 新建token 存储 userid 和 corpid */
                String Authorization = JWTUtils.createToken(corpid+","+userid);

                String userTicket = jsonObj.getString("user_ticket");

                //获取用户敏感信息
                String url2 = WeChatUtils.THIRD_BUS_WECHAT_GET_USER_DETAIL3RD + redisTemplate.opsForValue().get("suiteAccessToken");
                JSONObject jsonParams = new JSONObject();
                jsonParams.put("user_ticket", userTicket);
                JSONObject jsonObj2 = HttpHelper.doPost(url2, jsonParams);

                //修改用户信息 返回
                MemberInformationEntity memberInformationEntity = memberInformationService.updateAndGetUserInfo(jsonObj2);

//                //根据corpid获取企业信息
//                EnterpriseInformationEntity enterpriseInformationEntity = enterpriseInformationService.getEnterpriseInfoByCorpid(corpid);
//
//                /* 根据用户判断角色返回初始化信息 */
//                List<InitializationInformationEntity> informationListVos = initializationInformationService.initialization(corpid,openUserid);

                loginForUserAndEnterpriseInfo.setAuthorization(Authorization)
                        .setMemberInformationEntity(memberInformationEntity);
//                        .setEnterpriseInformationEntity(enterpriseInformationEntity)
//                        .setInitializationInformationListVos(informationListVos);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loginForUserAndEnterpriseInfo;
    }


    /**
     * 手动存入,获取部门列表
     */
    @Override
    @Transactional
    public List<DepartmentListInformationEntity> department(String corpid) {
        String accessToken = redisTemplate.opsForValue().get(corpid + "accessToken");
        if (PubUtil.isEmpty(accessToken))
            accessToken = getAccessToken(corpid);
        String url = WeChatUtils.THIRD_BUS_WECHAT_DEPART_LIST + accessToken;
        JSONObject jsonObj = HttpHelper.doGet(url);
        //存入部门信息表
        List<DepartmentListInformationEntity> list = departmentListInformationService.insertAll(jsonObj, corpid);
        return list;
    }

    /**
     * 手动存入,获取部门成员信息
     */
    @Override
    @Transactional
    public List<MemberInformationEntity> member(String corpid) {
        List<MemberInformationEntity> list = memberInformationService.insertAll(corpid);
        return list;
    }

    /**
     * 手动获取accessToken
     */
    @Override
    @Transactional
    public String getAccessToken(String corpid) {
        String accessToken = redisTemplate.opsForValue().get(corpid + "accessToken");
        if (PubUtil.isEmpty(accessToken)){
            try {
                String url2 = WeChatUtils.THIRD_BUS_WECHAT_GET_CORP_TOKEN + redisTemplate.opsForValue().get("suiteAccessToken");
                JSONObject jsonParams2 = new JSONObject();
                jsonParams2.put("auth_corpid", corpid);
                jsonParams2.put("permanent_code", redisTemplate.opsForValue().get(corpid+"permanentCode"));
                JSONObject jsonObject = HttpHelper.doPost(url2, jsonParams2);
                accessToken = jsonObject.getString("access_token");
                Integer expiresIn = Integer.parseInt(jsonObject.getString("expires_in"));
                redisTemplate.opsForValue().set(corpid + "accessToken", accessToken, expiresIn, TimeUnit.SECONDS);
            }catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return accessToken;
    }


    /**
     * 手动清除现在存在rides中的suiteAccessToken
     */
    @Override
    @Transactional
    public void deleteSuiteAccessToken() {
        redisTemplate.delete("suiteAccessToken");
    }

    /**
     * 获取签名
     * @param url
     * @return
     */
    @Override
    public String autograph(String corpid, String url) {
        String accessToken = redisTemplate.opsForValue().get(corpid + "accessToken");
        if (PubUtil.isEmpty(accessToken))
            accessToken = getAccessToken(corpid);
        try {
            String url2 = WeChatUtils.THIRD_BUS_GET_JSAPI_TICKET + accessToken + "&type=agent_config";
            JSONObject jsonObject = HttpHelper.doGet(url2);
            String ticket = jsonObject.getString("ticket");

            String nonceStr = genNonce();
            Long timestamp = createTimestamp();
            String sign = sign(url, nonceStr, timestamp, ticket);
            return sign;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    @Override
//    public JSONObject a() {
//        String url = "https://qyapi.weixin.qq.com/cgi-bin/service/contact/search?provider_access_token=gCtN5lahIDJYcsb0qt4H860P7bWIGMpI-w2DH7wfCk3wi5X_E2GLgAEl6oGauR1C7ppLyzy3uucd0JQ_juwjft1hkXP08VMfGd0gO8wncy20QDp2EfLyyFb2F89Y1Urj";
//        JSONObject jsonParams = new JSONObject();
//        JSONObject jsonObj = null;
//        try {
//            jsonParams.put("auth_corpid", "ww53c8160388d016ee");
//            jsonParams.put("query_word", "wxid");
//            jsonParams.put("full_match_field", 1);
//            jsonObj = HttpHelper.doPost(url, jsonParams);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return jsonObj;
//    }


    /**
     * 获得消息正文XML
     *
     * @param request
     * @return
     * @throws IOException
     */
    private String getMessageBody(HttpServletRequest request) throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        }
        byte[] buffer = new byte[contentLength];
        for (int i = 0; i < contentLength; ) {
            int readlen = request.getInputStream().read(buffer, i,
                    contentLength - i);
            if (readlen == -1) {
                break;
            }
            i += readlen;
        }

        String charEncoding = request.getCharacterEncoding();
        if (charEncoding == null) {
            charEncoding = "UTF-8";
        }
        request.getInputStream();
        String messageBody = new String(buffer, charEncoding);
        System.out.println("messageBody = " + messageBody);
        return messageBody;
    }

    /**
     * 获取随机字符串
     * @return
     */
    private static String createNonceStr(){
        return UUID.randomUUID().toString().replaceAll("-","").substring(0,32);
    }
    public static String genNonce() {
        return bytesToHex(Long.toString(System.nanoTime())
                .getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 创建时间戳
     * @return
     */
    private static Long createTimestamp(){
        return System.currentTimeMillis() / 1000;
    }

    public static String bytesToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    /**
     * SHA-1签名
     * @param url
     * @param nonce
     * @param timestamp
     * @param ticket
     * @return
     * @throws Exception
     */
    private static String sign(String url, String nonce, Long timestamp, String ticket) throws Exception {
        String plain = String.format(WeChatUtils.SIGN_RULE, ticket, nonce, timestamp, url);
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            sha1.reset();
            sha1.update(plain.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(sha1.digest());
        } catch (NoSuchAlgorithmException e) {
            // throw new LeysenException("jsapi_ticket计算签名错误");
            throw new SandException("jsapi_ticket计算签名错误");
        }
    }

}
