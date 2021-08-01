package com.dejavu.nunu.core.utils;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.hutool.json.JSONUtil;

import java.nio.charset.StandardCharsets;

public class SecurityUtil {


    /**
     * 生成Key
     * @return
     */
    public static String generateKey(){
        return IdUtil.simpleUUID();
    }


    /**
     * 加密
     * @param key
     * @param obj
     * @return
     */
    public static String encrypt(String key,Object obj){

        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key.getBytes(StandardCharsets.UTF_8));

        String jsonStr = JSONUtil.toJsonStr(obj);

        return aes.encryptHex(jsonStr);

    }

    /**
     * 解密
     * @return
     */
    public static String decrypt(String key,String context){
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key.getBytes(StandardCharsets.UTF_8));
        return aes.decryptStr(context);
    }


}
