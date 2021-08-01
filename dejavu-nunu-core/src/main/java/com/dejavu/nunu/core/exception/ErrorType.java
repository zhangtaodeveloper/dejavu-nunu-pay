package com.dejavu.nunu.core.exception;

/**
 * 说明：
 *
 * @author zt
 * @date 2017/11/21 15:21.
 */
public interface ErrorType {

    /**
     * 返回code
     *
     * @return
     */
    String getCode();

    /**
     * 返回meg
     *
     * @return
     */
    String getMsg();
}
