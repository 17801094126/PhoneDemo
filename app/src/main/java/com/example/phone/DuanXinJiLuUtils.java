package com.example.phone;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * @author Mr.老王
 * @desc 获取短信
 * @email wkz123011@gmail.com
 */
public class DuanXinJiLuUtils {


    public static String[] getSms(Activity activity) {
        Uri SMS_INBOX = Uri.parse("content://sms/");
        ContentResolver cr = activity.getContentResolver();
        String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
        Cursor cur = cr.query(SMS_INBOX, projection, null, null, "date desc");
        String[] arr=new String[cur.getCount()];
        if (null == cur) {
            Log.i("ooc", "************cur == null");
            return null;
        }
        int i=0;
        while (cur.moveToNext()) {
            String number = cur.getString(cur.getColumnIndex("address"));//手机号
            String name = cur.getString(cur.getColumnIndex("person"));//联系人姓名列表
            String body = cur.getString(cur.getColumnIndex("body"));//短信内容
            Long date = cur.getLong(cur.getColumnIndex("date"));//短信内容
            String yearTime = TimeUtils.getYearTime(date);
            Log.e("Msg",date+"");
            //至此就获得了短信的相关的内容, 以下是把短信加入map中，构建listview,非必要。
             arr[i]="手机号："+number+"   ，   短信内容："+body+"  ，  时间: "+yearTime+"\n";
            Log.e("Msg",arr[i]);
            i++;
        }
        return arr;
    }

}
