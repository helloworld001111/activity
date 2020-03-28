package com.example.dubei.activity.util;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class TokenUtilTest {

    @Test
    public void decryptSalt() throws Exception {
        String s = TokenUtil.decryptSalt("admin-token");
        String[] arr = s.split("_");
        System.out.println(Arrays.toString(arr));
    }
}