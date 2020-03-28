package com.example.dubei.activity.view;

import com.example.dubei.activity.api.AccessTokenApi;
import com.example.dubei.activity.api.IdAndSecretApi;
import com.example.dubei.activity.api.MenuApi;
import com.example.dubei.activity.ui.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.dubei.activity.bean.AccessToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MenuManager {

    public static void createMenu() throws IOException {

//        ViewButton viewButton = new ViewButton();
//        viewButton.setName("北京PM2.5");
//        viewButton.setType("view");
//        viewButton.setUrl("http://fznyqj.natappfree.cc/upload/1.html");
//        ComplexButton complexButton = new ComplexButton();
//        complexButton.setName("小工具");
//        complexButton.setSub_button(new Button[] {viewButton});
//
//        ClickButton clickButton2 = new ClickButton();
//        clickButton2.setName("获取书单");
//        clickButton2.setType("click");
//        clickButton2.setKey("button1");

        ViewButton viewButton1 = new ViewButton();
        viewButton1.setName("免费领礼物");
        viewButton1.setType("view");
        viewButton1.setUrl("https://www.yuanlaiyouni.vip/dist");
//        ComplexButton complexButton1 = new ComplexButton();
//        complexButton1.setName("领取礼品");
//        complexButton1.setSub_button(new Button[] {viewButton1});
        Menu menu = new Menu();

//        ViewButton viewButton2 = new ViewButton();
//        viewButton2.setName("今日特惠");
//        viewButton2.setType("view");
//        viewButton2.setUrl("https://www.yuanlaiyouni.vip/dist");

        ViewButton viewButton3 = new ViewButton();
        viewButton3.setName("商务合作");
        viewButton3.setType("view");
        viewButton3.setUrl("https://www.yuanlaiyouni.vip/dist/?code=071Dh24v18pqDh02eK4v1LW14v1Dh245&state=STATE#/suhz");

        menu.setButton(new Button[] {viewButton1,viewButton3});
//        menu.setButton(new Button[] {complexButton, clickButton2, complexButton1});
        AccessToken accessToken = null;
        accessToken = AccessTokenApi.getAccessToken(IdAndSecretApi.appID, IdAndSecretApi.appSecret);
        String token = accessToken.getAccessToken();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(menu);
        MenuApi.createMenu(token, json);
    }
}
