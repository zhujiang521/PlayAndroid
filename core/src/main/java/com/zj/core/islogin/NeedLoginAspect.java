package com.zj.core.islogin;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;

import com.blankj.utilcode.util.ToastUtils;
import com.zj.core.Play;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 判断是否登录
 *
 * @author jiang zhu on 2019/4/19
 */
@Aspect
public class NeedLoginAspect {

    @Pointcut("execution(" +//执行语句

            "@com.zj.core.islogin.NeedLogin" +//注解筛选

            " * " + //类路径,*为任意路径

            "*" +  //方法名,*为任意方法名

            "(..)" +//方法参数,'..'为任意个任意类型参数

            ")" +

            " && " +//并集

            "@annotation(needLogin)"//注解筛选,这里主要用于下面方法的'NeedLogin'参数获取

    )

    public void pointcutNeedLogin(NeedLogin needLogin) {

    }

    @Around("pointcutNeedLogin(needLogin)")
    public void aroundNeedLogin(ProceedingJoinPoint joinPoint, final NeedLogin needLogin) throws Throwable {
        if (Play.isLogin()) {
            //方法执行
            joinPoint.proceed();
        } else {
            final Context context = Play.getContext();
            switch (needLogin.tipType()) {
                case NeedLogin.SHOW_TOAST:
                    ToastUtils.showShort(needLogin.tipToast());
                    break;
                case NeedLogin.SHOW_DIALOG:
                    /*
                        判断context是否为Activity
                        如果不是 , 使用Dialog会crash
                    */
                    if (context instanceof Activity) {
                        final AlertDialog dialog = new AlertDialog.Builder(context).create();
                        dialog.setTitle("登录提示");
                        dialog.setMessage("当前尚未登录，是否前往登录");
                        dialog.setButton(1, "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialog.setButton(2, "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(context, needLogin.LoginActivity());
                                context.startActivity(intent);
                            }
                        });
                        dialog.show();
                    } else {
                        // 这里处理直接跳到登录界面
                        ToastUtils.showShort(needLogin.tipToast());
                        Intent intent = new Intent(context, needLogin.LoginActivity());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                    break;
                //无响应类型
                case NeedLogin.NO_RESPONSE:
                default:
                    break;
            }
        }
    }

}
