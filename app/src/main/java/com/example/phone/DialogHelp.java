package com.example.phone;

import android.app.Activity;
import android.content.DialogInterface;

/**
 * @author Mr.老王
 * @desc 权限帮助
 * @email wkz123011@gmail.com
 */

public class DialogHelp {

    public static void showNormalDialog(final Activity context) {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        android.app.AlertDialog.Builder normalDialog =
                new android.app.AlertDialog.Builder(context);
        normalDialog.setTitle("没有权限无法继续借款");
        normalDialog.setMessage("前往系统设置开启权限");
        normalDialog.setPositiveButton("去授权",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        JumpPermissionManagement.GoToSetting(context);
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        // 显示
        normalDialog.show();
    }
}