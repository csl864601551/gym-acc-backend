package com.ztl.gym.weixin.service;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ztl.gym.weixin.utils.WxUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class WxService {

    @Autowired
    private RestTemplate restTemplate;

    private static String AppId;
    @Value("${wx.appId}")
    public void setAppId(String appId) {
        AppId = appId;
    }

    protected static String Token;
    @Value("${wx.token}")
    public void setToken(String token) {
        Token = token;
    }

    protected static String AppSecret;
    @Value("${wx.appSecret}")
    public void setAppSecret(String appSecret) {
        AppSecret = appSecret;
    }

    private static final String GETWEIXINUSERINFO_AUTHORIZE="https://open.weixin.qq.com/connect/oauth2/authorize";
    private static final String GETWEIXINUSERINFO_GETACCESSTOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
    private static final String GETWEIXINUSERINFO_GETUSERINFO = "https://api.weixin.qq.com/sns/userinfo";
    private static final String JSSDK_ACCESSTOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    private static final String JSSDK_GETTICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";


    /**
     *@Author: hdx
     *@CreateTime: 20:46 2020/2/14
     *@param:  shareUrl 分享的url
     *@Description: 初始化JSSDKConfig
     */
    public Map initJSSDKConfig(String url) throws Exception {
        //获取AccessToken
        String accessToken = WxUtil.getJSSDKAccessToken();
        //获取JssdkGetticket
        String jsapiTicket = WxUtil.getJssdkGetticket(accessToken);
        String timestamp = Long.toString(System.currentTimeMillis() / 1000);
        String nonceStr = UUID.randomUUID().toString();
        String signature = WxUtil.buildJSSDKSignature(jsapiTicket,timestamp,nonceStr,url);
        Map<String,String> map = new HashMap<String,String>();
        map.put("url", url);
        map.put("jsapi_ticket", jsapiTicket);
        map.put("nonceStr", nonceStr);
        map.put("timestamp", timestamp);
        map.put("signature", signature);
        map.put("appid", AppId);
        return map;
    }


    /**
     * 向外暴露的获取token的方法 验证
     * @return
     */
    public  boolean check(String timestamp, String nonce, String signature) {
        //1）将token、timestamp、nonce三个参数进行字典序排序
        String[] strs = new String[] {Token,timestamp,nonce};
        Arrays.sort(strs);
        //2）将三个参数字符串拼接成一个字符串进行sha1加密
        String str = strs[0]+strs[1]+strs[2];
        String mysig = sha1(str);
        //3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
        return mysig.equalsIgnoreCase(signature);
    }


    /**
     * 进行sha1加密
     * @param
     * @return
     */
    private  String sha1(String src) {
        try {
            //获取一个加密对象
            MessageDigest md = MessageDigest.getInstance("sha1");
            //加密
            byte[] digest = md.digest(src.getBytes());
            char[] chars= {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
            StringBuilder sb = new StringBuilder();
            //处理加密结果
            for(byte b:digest) {
                sb.append(chars[(b>>4)&15]);
                sb.append(chars[b&15]);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 拼接用户授权重定向的URL
     * @param url
     * @return
     */
    public String buildAuthorizeURL(String url){

        return concatAuthorizeURL(url);
    }


    /**
     * 拼接引导用户授权重定向的URL
     * snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid）
     * snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且， 即使在未关注的情况下，只要用户授权，也能获取其信息 ）
     * @param url
     * @return
     */
    private String concatAuthorizeURL(String url) {
        StringBuilder authorizeUrl = new StringBuilder(GETWEIXINUSERINFO_AUTHORIZE);
        authorizeUrl.append("?appid=").append(AppId);
        authorizeUrl.append("&redirect_uri=").append(url);
        authorizeUrl.append("&response_type=code");
        authorizeUrl.append("&scope=snsapi_userinfo");
        authorizeUrl.append("&state=").append("STATE");
        authorizeUrl.append("#wechat_redirect");
        return authorizeUrl.toString();
    }


    /**
     * 获取微信用户的 accesstoken 和 openid
     * @param code
     * @return
     */
    public Map<String,Object> getACCESSTOKEN(String code){

        String getAccessTokenUrl = concatGetAccessTokenInfoURL(code);
        String json = postRequestForWeiXinService(getAccessTokenUrl);
        //String json = null;
        Map<String,Object> map = jsonToMap(json);

        return map;
    }


    /**
     * 拼接调用微信服务获取  accesstoken 和 openid URL
     * @param code
     * @return
     */
    private String concatGetAccessTokenInfoURL(String code) {
        StringBuilder getAccessTokenUrl = new StringBuilder(GETWEIXINUSERINFO_GETACCESSTOKEN);
        getAccessTokenUrl.append("?appid=").append(AppId);
        getAccessTokenUrl.append("&secret=").append(AppSecret);
        getAccessTokenUrl.append("&code=").append(code);
        getAccessTokenUrl.append("&grant_type=authorization_code");
        return getAccessTokenUrl.toString();
    }


    /**
     * 获取微信用户信息
     * @param map
     * @return
     */
    public Map getWeiXinUserInfo(Map<String, Object> map) {

        String getUserInfoUrl = concatGetWeiXinUserInfoURL(map);
        String json = getRequestForWeiXinService(getUserInfoUrl);
//        String json = null;
        Map userInfoMap = jsonToMap(json);

        return userInfoMap;
    }
    /**
     * 拼接调用微信服务获取用户信息的URL
     * @param map
     * @return
     */
    private String concatGetWeiXinUserInfoURL(Map<String, Object> map) {
        String openId = (String) map.get("openid");
        String access_token = (String) map.get("access_token");
        // 检验授权凭证（access_token）是否有效
        StringBuilder getUserInfoUrl = new StringBuilder(GETWEIXINUSERINFO_GETUSERINFO);
        getUserInfoUrl.append("?access_token=").append(access_token);
        getUserInfoUrl.append("&openId=").append(openId);
        getUserInfoUrl.append("&lang=zh_CN");

        return getUserInfoUrl.toString();
    }


    public String mapToJson(Map map){
        Gson gson = new Gson();
        String json = gson.toJson(map);
        return json;
    }
    private Map jsonToMap(String json) {
        Gson gons = new Gson();
        Map map = gons.fromJson(json, new TypeToken<Map>(){}.getType());
        return map;
    }

    private String postRequestForWeiXinService(String getAccessTokenUrl) {
        ResponseEntity<String> postForEntity = restTemplate.postForEntity(getAccessTokenUrl, null, String.class);
        String json = postForEntity.getBody();
        return json;
    }

    private String getRequestForWeiXinService(String getUserInfoUrl) {
        ResponseEntity<String> postForEntity = restTemplate.getForEntity(getUserInfoUrl.toString(), String.class);
        String json = postForEntity.getBody();
        return json;
    }

}
