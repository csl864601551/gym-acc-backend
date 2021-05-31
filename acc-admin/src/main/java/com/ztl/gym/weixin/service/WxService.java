package com.ztl.gym.weixin.service;


import com.ztl.gym.weixin.utils.WxUtil;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class WxService {

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

}
