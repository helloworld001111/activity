package com.example.dubei.activity.util;

import com.example.dubei.activity.constant.Constant;
import org.junit.Test;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static org.junit.Assert.*;

public class URLUtilsTest {

    @Test
    public void downloadUrlAsPic() {
        String s = URLUtils.downloadUrlAsPic("http://thirdwx.qlogo.cn/mmopen/NicComq8XIlibiafLibdiaUicMPJSymOFReJG0per5N9aozIPmXnt6JxuSeScibAuAF4YbQNicvx1Qltyurk5V1nLV3osIOWAlVDmLWM/132",
                new File("/Users/duxiansen/Desktop/person/upload" + File.separator + Constant.WECHAT_PHOTO_FOLDER));
        System.out.println(s);
    }

    @Test
    public void fun1() throws UnsupportedEncodingException {
        String encode = URLEncoder.encode("http://www.yuanlaiyouni.vip","UTF-8");
        System.out.println(encode);
    }
}