package com.ztl.gym.weixin.controller;

import cn.hutool.core.util.StrUtil;
import com.ztl.gym.code.domain.Code;
import com.ztl.gym.code.domain.CodeRecord;
import com.ztl.gym.code.service.ICodeRecordService;
import com.ztl.gym.code.service.ICodeService;
import com.ztl.gym.common.core.domain.AjaxResult;
import com.ztl.gym.common.utils.CodeRuleUtils;
import com.ztl.gym.common.utils.StringUtils;
import com.ztl.gym.product.domain.Product;
import com.ztl.gym.product.service.IProductService;
import com.ztl.gym.storage.service.IStorageService;
import com.ztl.gym.weixin.common.AjaxJson;
import com.ztl.gym.weixin.service.WxService;
import com.ztl.gym.weixin.utils.WxUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/weixin")
public class WxInitController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WxService wxService;

    @Autowired
    private IStorageService storageService;
    @Autowired
    private ICodeService codeService;
    @Autowired
    private ICodeRecordService codeRecordService;
    @Autowired
    private IProductService tProductService;


    private static String AppId;
    @Value("${wx.appId}")
    public void setAppId(String appId) {
        AppId = appId;
    }

    protected static String AppSecret;
    @Value("${wx.appSecret}")
    public void setAppSecret(String appSecret) {
        AppSecret = appSecret;
    }

//    @Bean
//    @LoadBalanced
//    RestTemplate restTemplate(){
//        return new RestTemplate();
//    }

    /**
     *@Author: shenz
     *@param:  shareUrl 分享url地址
     *@Description: 初始化微信JSSDK Config信息
    1.先通过appId和appSecret参数请求指定微信地址 获取AccessToken
    2.在通过第一步中的AccessToken作为参数请求微信地址 获取jsapi_ticket临时票据(此处不考虑调用频率，使用者根据情况放入缓存或定时任务)
    3.通过第二步的JssdkGetticket和timestamp,nonceStr,url作为参数请求微信地址，获取签名signature
    4.将第三步获得的signature和jsapi_ticket,nonceStr,timestamp,url返回给前端，作为Config初始化验证的信息
     */
    @RequestMapping("/initWXJSSDKConfigInfo")
    public AjaxJson initWXJSConfig (@RequestParam(required = false) String url) throws Exception{
        logger.info("url=" + url);
        String json = "";
        try {
            Map map = wxService.initJSSDKConfig(url);
            json = WxUtil.mapToJson(map);
        }catch (Exception e){
            AjaxJson.fail(e.getMessage());
        }
        return AjaxJson.ok(json);
    }

    /**
     * 用于微信验证
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "getwx", method = {RequestMethod.GET,RequestMethod.POST})
    public void getwx(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /**
         * signature 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。 timestamp
         * 时间戳 nonce 随机数 echostr 随机字符串
         */
        System.out.println("this is getwx");
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        PrintWriter out = null;
        //接入验证
        if (wxService.check(timestamp, nonce, signature)) {
            System.out.println("接入成功");
            out = response.getWriter();
            // 原样返回echostr参数
            out.print(echostr);
            out.flush();
            out.close();
            return;
        } else {
            System.out.println("接入失败");
        }
    }



    /**
     * 获取微信用户信息
     * @param code
     * @param request
     * @param response
     * @param session
     * @return
     * @throws IOException
     */
    @RequestMapping("/getWeiXinUserInfo")
    public String getWeiXinUserInfo(String code, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException{
        //第一步：用户同意授权，获取code
        if (code == null) {
            String url = URLEncoder.encode(request.getRequestURL().toString());
            String authorizeUrl = wxService.buildAuthorizeURL(url);
            response.sendRedirect(authorizeUrl);
            return null;
        }
        //第二步：通过code换取网页授权access_token
        String htmlInfo = "";
        Map<String, Object> accesstokenInfo = wxService.getACCESSTOKEN(code);
        String errcode = (String)accesstokenInfo.get("errcode");
        if(StringUtils.isEmpty(errcode)){
            //第四步：拉取用户信息(需scope为 snsapi_userinfo)
            Map<String, Object> weiXinUserInfo = wxService.getWeiXinUserInfo(accesstokenInfo);
            String userInfohtml = createUserInfoHtml(weiXinUserInfo);
            return userInfohtml;
        }
        return htmlInfo;
    }

    private String createUserInfoHtml(Map<String, Object> weiXinUserInfo) {
        return  null;
    }


    @RequestMapping("getuserInfo")
    @ResponseBody
    @CrossOrigin
    public Map<String,Object> getuserInfo(HttpServletRequest request, HttpServletResponse response)  throws Exception   {
        String result="";
        Map<String,Object> resMap = new HashMap<String, Object>();
        String page = request.getParameter("page");
        try {
            //获取code
            String code = request.getParameter("code");
//			String
            //换取accesstoken的地址
            String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
            url = url.replace("APPID", AppId).replace("SECRET", AppSecret)
                    .replace("CODE", code);
            result = WxUtil.get(url);
            System.out.println(result);
            String at = JSONObject.fromObject(result).getString("access_token");
            String openid = JSONObject.fromObject(result).getString("openid");
            //拉取用户的基本信息  看看是否已关注
//			url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
//			url = url.replace("ACCESS_TOKEN",wxService.getAccessToken() ).replace("OPENID", openid);
//			result = WxUtil.get(url);
//			System.out.println(result);
            //拉取用户的基本信息
            url = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
            url = url.replace("ACCESS_TOKEN", at).replace("OPENID", openid);
            result = WxUtil.get(url);
//			final String resultString = result;
//			pool.execute(new Runnable() {
//				public void run() {
            if(StringUtils.isNotEmpty(result)) {
                System.out.println("result:"+result);
                JSONObject object = JSONObject.fromObject(result) ;
                System.out.println("user:"+object);
                System.out.println("openid:"+object.getString("openid"));
            }
//				}
//			});

        } catch (Exception e) {
            System.out.println("getuserInfo is error"+e.getMessage());
            e.printStackTrace();
        }
//		finally {
//			pool.shutdown();
//		}
        System.out.println(result);

        resMap.put("page",page);
        return resMap;
    }





    /**
     * 根据码号查询相关产品和码信息
     */
    @RequestMapping(value = "cxspxqBycode", method = {RequestMethod.GET,RequestMethod.POST})
    public AjaxResult cxspxqBycode(HttpServletRequest request, HttpServletResponse response) {
        String code = request.getParameter("code");
        System.out.println("扫码详情进入成功  code=="+code);
        String temp ="";
        if(StrUtil.isNotEmpty(code)){
            long companyId = CodeRuleUtils.getCompanyIdByCode(code.trim());
            Code codequery = new Code();
            codequery.setCompanyId(companyId);
            codequery.setCode(code.trim());
            Code codeEntity = codeService.selectCode(codequery);
            if(codeEntity!=null){
                long codeIndex = codeEntity.getCodeIndex();
                CodeRecord codeRecord = codeRecordService.selectCodeRecordByIndex(codeIndex);
                if(codeRecord!=null){
                    long productId = codeRecord.getProductId();
                    Product product = tProductService.selectTProductById(productId);
                    if(product!=null){
                        String productDetailPc = product.getProductDetailPc();
                        if(StrUtil.isNotEmpty(productDetailPc)){
                            temp = productDetailPc;
                        }
                    }
                }
            }
        }
        return AjaxResult.success(temp);
    }

}
