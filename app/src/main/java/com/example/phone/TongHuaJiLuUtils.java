package com.example.phone;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Mr.老王
 * @desc 权通话记录
 * @email wkz123011@gmail.com
 */

public class TongHuaJiLuUtils {

    /**
     * 利用系统CallLog获取通话历史记录
     */
    public static String[] getCallHistoryList(Activity activity,int num) {
        Cursor cs;
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_CALL_LOG}, 1000);
        }
        cs = activity.getContentResolver().query(CallLog.Calls.CONTENT_URI, //系统方式获取通讯录存储地址
                new String[]{
                        CallLog.Calls.CACHED_NAME,  //姓名
                        CallLog.Calls.NUMBER,    //号码
                        CallLog.Calls.TYPE,  //呼入/呼出(2)/未接
                        CallLog.Calls.DATE,  //拨打时间
                        CallLog.Calls.DURATION,   //通话时长
                        CallLog.Calls.GEOCODED_LOCATION//运营商地址
                }, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        String[]  str=new String[cs.getCount()];
        if (cs != null && cs.getCount() > 0) {
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date_today = simpleDateFormat.format(date);
            int i = 0;
            for (cs.moveToFirst(); (!cs.isAfterLast()) && i < num; cs.moveToNext(), i++) {
                String callName = cs.getString(0);  //名称
                String callNumber = cs.getString(1);  //号码
                String callAddress=cs.getString(5);
                //如果名字为空，在通讯录查询一次有没有对应联系人
                if (callName == null || callName.equals("")){
                    String[] cols = {ContactsContract.PhoneLookup.DISPLAY_NAME};
                    //设置查询条件
                    String selection = ContactsContract.CommonDataKinds.Phone.NUMBER + "='"+callNumber+"'";
                    Cursor cursor = activity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            cols, selection, null, null);
                    int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
                    if (cursor.getCount()>0){
                        cursor.moveToFirst();
                        callName = cursor.getString(nameFieldColumnIndex);
                    }
                    cursor.close();
                }
                //通话类型
                int callType = Integer.parseInt(cs.getString(2));
                String callTypeStr = "";
                switch (callType) {
                    case CallLog.Calls.INCOMING_TYPE:
                        callTypeStr = "呼入";
                        break;
                    case CallLog.Calls.OUTGOING_TYPE:
                        callTypeStr = "呼出";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        callTypeStr = "未接";
                        break;
                    default:
                        //其他类型的，例如新增号码等记录不算进通话记录里，直接跳过
                        Log.i("ssss",""+callType);
                        i--;
                        continue;
                }
                //拨打时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date callDate = new Date(Long.parseLong(cs.getString(3)));
                String callDateStr = sdf.format(callDate);
                if (callDateStr.equals(date_today)) { //判断是否为今天
                    sdf = new SimpleDateFormat("HH:mm");
                    callDateStr = sdf.format(callDate);
                } else if (date_today.contains(callDateStr.substring(0, 7))) { //判断是否为当月
                    sdf = new SimpleDateFormat("dd");
                    int callDay = Integer.valueOf(sdf.format(callDate));

                    int day = Integer.valueOf(sdf.format(date));
                    if (day - callDay == 1) {
                        callDateStr = "昨天";
                    } else {
                        sdf = new SimpleDateFormat("MM-dd");
                        callDateStr = sdf.format(callDate);
                    }
                } else if (date_today.contains(callDateStr.substring(0, 4))) { //判断是否为当年
                    sdf = new SimpleDateFormat("MM-dd");
                    callDateStr = sdf.format(callDate);
                }

                //通话时长
                int callDuration = Integer.parseInt(cs.getString(4));
                int min = callDuration / 60;
                int sec = callDuration % 60;
                String callDurationStr = "";
                if (sec > 0) {
                    if (min > 0) {
                        callDurationStr = min + "分" + sec + "秒";
                    } else {
                        callDurationStr = sec + "秒";
                    }
                }

                /**
                 * callName 名字
                 * callNumber 号码
                 * callTypeStr 通话类型
                 * callDateStr 通话日期
                 * callDurationStr 通话时长
                 * callAddress 运营商地址
                 * 请在此处执行相关UI或存储操作，之后会查询下一条通话记录
                 */
                if (callName!=null&&!callName.equals("null")){
                    str[i]="名字: 未接来电"+" , 号码:"+callNumber+" , 通话类型:"+callTypeStr
                            +" , 通话日期："+callDateStr+"  , 通话时长: "+callDurationStr+" , 运营商地址"+callAddress+"\n";
                    Log.e("Msg","名字:"+callName+" , 号码:"+callNumber+" , 通话类型:"+callTypeStr
                            +" , 通话日期："+callDateStr+"  , 通话时长: "+callDurationStr+" , 运营商地址"+callAddress+"\n");
                }else{
                    str[i]="名字: 未接来电"+" , 号码:"+callNumber+" , 通话类型:"+callTypeStr
                            +" , 通话日期："+callDateStr+"  , 通话时长: "+callDurationStr+" , 运营商地址"+callAddress+"\n";
                    Log.e("Msg","名字:"+callName+" , 号码:"+callNumber+" , 通话类型:"+callTypeStr
                            +" , 通话日期："+callDateStr+"  , 通话时长: "+callDurationStr+" , 运营商地址"+callAddress+"\n");
                }
                i++;
            }
        }
        return str;
    }
}
