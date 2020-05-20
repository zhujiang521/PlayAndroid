package com.zj.core.islogin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 判断是否登录
 * @author jiang zhu on 2019/4/19
 */
//注解类型:方法注解
@Target(ElementType.METHOD)
//注解声明期:运行时注解
@Retention(RetentionPolicy.RUNTIME)

public @interface NeedLogin {

    /**

     * 展示Dialog提示登录

     */

    int SHOW_DIALOG = 0;

    /**

     * 弹出吐司提示登录

     */

    int SHOW_TOAST = 1;

    /**

     * 没有响应

     */

    int NO_RESPONSE = 2;

    /**

     *  提示类型

     */

    int tipType() default SHOW_TOAST;

    /**

     * 登录Activity

     */

    Class LoginActivity();

    String tipToast() default "当前尚未登录，请先登录";

    String tipDialog() default "当前尚未登录，是否前往登录？";

}
