package com.example.dubei.activity;

import com.example.dubei.activity.view.MenuManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class WeChatDemoApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(WeChatDemoApplication.class, args);
		MenuManager.createMenu();
	}
}
