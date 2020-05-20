package com.zj.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.blankj.utilcode.util.SPUtils;
import com.zj.core.util.Preference;

/**
 * 全局的API接口。
 *
 * @author guolin
 * @since 17/2/15
 */
public class Play {

    public static boolean isDebug = false;
    private static final String USERNAME = "username";
    private static final String NICE_NAME = "nickname";
    private static final String IS_LOGIN = "isLogin";

    @SuppressLint("StaticFieldLeak")
    private static Context context;


    /**
     * 初始化接口。这里会进行应用程序的初始化操作，一定要在代码执行的最开始调用。
     *
     * @param c Context参数，注意这里要传入的是Application的Context，千万不能传入Activity或者Service的Context。
     */
    public static void initialize(Context c) {
        context = c;
        Preference.Companion.setContext(context);
    }

    /**
     * 获取全局Context，在代码的任意位置都可以调用，随时都能获取到全局Context对象。
     *
     * @return 全局Context对象。
     */
    public static Context getContext() {
        return context;
    }


    /**
     * 判断用户是否已登录。
     *
     * @return 已登录返回true，未登录返回false。
     */
    public static boolean isLogin() {
        return SPUtils.getInstance().getBoolean(IS_LOGIN, false);
    }

    /**
     * 返回当前应用的包名。
     */
    public static String getPackageName() {
        return context.getPackageName();
    }

    /**
     * 注销用户登录。
     */
    public static void logout() {
        SPUtils.getInstance().clear();
    }


    public static void setUserInfo(String nickname, String username) {
        SPUtils.getInstance().put(NICE_NAME, nickname);
        SPUtils.getInstance().put(USERNAME, username);
    }

    public static String getNickName() {
        return SPUtils.getInstance().getString(NICE_NAME, "");
    }

    public static String getUsername() {
        return SPUtils.getInstance().getString(USERNAME, "");
    }


    public static void setLogin(boolean b) {
        SPUtils.getInstance().put(IS_LOGIN, b);
    }
}