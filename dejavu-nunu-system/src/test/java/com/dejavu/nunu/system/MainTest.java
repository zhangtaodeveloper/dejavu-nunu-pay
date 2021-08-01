package com.dejavu.nunu.system;

import com.dejavu.nunu.core.utils.SecurityUtil;

public class MainTest {


        public static void main(String[] args) {

            String key = SecurityUtil.generateKey();

            System.out.println("加密Key：" + key);

            String encrypt = SecurityUtil.encrypt(key, "{加密内容}");

            System.out.println("加密后：" + encrypt);

            String decrypt = SecurityUtil.decrypt(key, encrypt);

            System.out.println("解密后：" +decrypt);

        }

}
