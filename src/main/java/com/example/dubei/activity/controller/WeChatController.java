package com.example.dubei.activity.controller;

import com.example.dubei.activity.api.IdAndSecretApi;
import com.example.dubei.activity.base.pojo.Result;
import com.example.dubei.activity.constant.Constant;
import com.example.dubei.activity.handler.DefaultHandler;
import com.example.dubei.activity.util.ActionHelperUtils;
import com.example.dubei.activity.util.XMLUtil;
import com.example.dubei.activity.util.CheckUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.*;

@Controller
@RequestMapping("wechat")
@Slf4j
public class WeChatController {

    /*
     * 微信公众号服务器验证
     */
    @GetMapping("verify")
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        //获取参数值
        String signature = request.getParameter("signature");
        String timeStamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        log.info("signature:"+signature+",timestamp:"+timeStamp+",nonce:"+nonce+",echostr:"+echostr);
        PrintWriter out = null;
        try {
            out = response.getWriter();
            //开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
            if (CheckUtil.checkSignature(IdAndSecretApi.TOKEN, timeStamp, nonce).equals(signature)) {
                //作出响应，即原样返回随机字符串
                out.println(echostr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    @PostMapping("verify")
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException {
        Map<String, String> map = XMLUtil.xml2Map(request);
        log.info("request xml data" + map);
        PrintWriter out = response.getWriter();
        DefaultHandler defaultHandler = new DefaultHandler();
        String msg = defaultHandler.handler(map);
        out.println(msg);
        out.close();
    }

    //返回给jssdk使用 config接口注入权限验证
    @RequestMapping("valiadateInfo")
    public void valiadateInfo(String wxurl,HttpServletResponse response) {
        String appid = IdAndSecretApi.appID;
        String secret = IdAndSecretApi.appSecret;
        log.info("appid:"+appid+",secret:"+secret);
        try {
            String accessToken = getAccessToken(appid, secret);
            //2、获取Ticket
            String jsapi_ticket = getTicket(accessToken);

            //3、时间戳和随机字符串
            String noncestr = UUID.randomUUID().toString().replace("-", "").substring(0, 16);//随机字符串
            String timestamp = String.valueOf(System.currentTimeMillis() / 1000);//时间戳
            log.info("accessToken:" + accessToken + "\njsapi_ticket:" + jsapi_ticket + "\n时间戳：" + timestamp + "\n随机字符串：" + noncestr);
            //5、将参数排序并拼接字符串
            String str = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url=" + wxurl;
            //6、将字符串进行sha1加密
            String signature = SHA1(str);
            log.info("参数：" + str + "\n签名：" + signature);
            Map<String,Object> result = new HashMap<String,Object>();
            result.put("timestamp",timestamp);
            result.put("nonceStr",noncestr);
            result.put("signature",signature);
            result.put("appId",appid);
            ActionHelperUtils.generateResult(Result.success(result),response);
        } catch (Exception e) {
            log.error("",e);
        }
    }

    public static String getAccessToken(String appid, String secret) {
        String access_token = "";
        String grant_type = "client_credential";//获取access_token填写client_credential
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=" + grant_type + "&appid=" + appid + "&secret=" + secret;
        //这个url链接地址和参数皆不能变
        try {
            //获取code后，请求以下链接获取access_token
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
            http.connect();
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            String message = new String(jsonBytes, "UTF-8");
            JSONObject demoJson = JSONObject.fromObject(message);
            log.info("JSON字符串：" + demoJson);
            access_token = demoJson.getString("access_token");
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return access_token;
    }

    //获取ticket
    public static String getTicket(String access_token) {
        String ticket = null;
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + access_token + "&type=jsapi";//这个url链接和参数不能变
        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
            http.connect();
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            String message = new String(jsonBytes, "UTF-8");
            JSONObject demoJson = JSONObject.fromObject(message);
            log.info("JSON字符串：" + demoJson);
            ticket = demoJson.getString("ticket");
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ticket;
    }


    public static String SHA1(String decript) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (Exception e) {
            log.error("",e);
        }
        return "";
    }
}
