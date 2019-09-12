package com.example.phone;

import android.Manifest;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yanzhenjie.alertdialog.AlertDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bt;
    private TextView lv;
    private Button bt1;
    private Button bt2;

    private String[]  phonePermission=new String[]{Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.GET_ACCOUNTS, Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.USE_SIP,
            Manifest.permission.PROCESS_OUTGOING_CALLS};

    private String[] smsPermission=new String[]{
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_WAP_PUSH,
            Manifest.permission.RECEIVE_MMS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.GET_ACCOUNTS, Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.USE_SIP,
            Manifest.permission.PROCESS_OUTGOING_CALLS
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView() {
        bt = (Button) findViewById(R.id.bt);
        lv = (TextView) findViewById(R.id.lv);
        bt.setOnClickListener(this);
        bt1 = (Button) findViewById(R.id.bt1);
        bt1.setOnClickListener(this);
        bt2 = (Button) findViewById(R.id.bt2);
        bt2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt:
                if (!AndPermission.hasPermission(this,phonePermission)){
                    AndPermission.with(this)
                            .requestCode(200)
                            .permission(Permission.CONTACTS, Permission.PHONE)
                            .rationale(rationaleListener)
                            .callback(listener)
                            .start();
                }else{
                    getPerson();
                }
                break;
            case R.id.bt1:
                if (!AndPermission.hasPermission(this,phonePermission)){
                    AndPermission.with(this)
                            .requestCode(200)
                            .permission(Permission.CONTACTS, Permission.PHONE)
                            .rationale(rationaleListener)
                            .callback(tonghuaListener)
                            .start();
                }else{
                    getTongHuaj();
                }
                break;
            case R.id.bt2:
                if (!AndPermission.hasPermission(this,smsPermission)){
                    AndPermission.with(this)
                            .requestCode(200)
                            .permission(Permission.SMS,Permission.CONTACTS, Permission.PHONE)
                            .rationale(rationaleListener)
                            .callback(duanxinListener)
                            .start();
                }else{
                    getDuanXin();
                }
                break;
        }
    }

    /**
     * Rationale支持，这里自定义对话框。
     */
    private RationaleListener rationaleListener = new RationaleListener() {
        @Override
        public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
            AlertDialog.newBuilder(MainActivity.this)
                    .setTitle("友好提醒")
                    .setMessage("你已拒绝过通讯录，沒有通讯录权限无法为你添加紧急联系人！")
                    .setPositiveButton("好，给你", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            rationale.resume();
                        }
                    })
                    .setNegativeButton("我拒绝", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            rationale.cancel();
                        }
                    }).show();
        }
    };
    /**
     * 获取手机短信记录
     * @return
     */
    private void getDuanXin() {

        if (MiuiUtils.isMIUI()) {
            MiuiUtils.goPermissionSettings(this);
        }else{
            String[] sms = DuanXinJiLuUtils.getSms(this);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < sms.length; i++) {
                sb.append(sms[i]).append("\n").append("\n");
            }
            lv.setText(sb.toString());
        }

    }
    /**
     * 获取手机通话记录
     * @return
     */
    private void getTongHuaj() {
        String[] callHistoryList = TongHuaJiLuUtils.getCallHistoryList(this,100);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < callHistoryList.length; i++) {
            sb.append(callHistoryList[i]).append("\n");
        }
        lv.setText(sb.toString());

    }
    /**
     * 获取手机联系人
     * @return
     */
    private void getPerson() {
        String[] contacts = getContacts();
        StringBuffer sb = new StringBuffer();
        for (int i = contacts.length - 1; i >= 0; i--) {
            sb.append(contacts[i]).append("\n");
        }
        lv.setText(sb.toString());
    }

    /**
     * 获取手机联系人
     * @return
     */
    private String[] getContacts() {
        //联系人的Uri，也就是content://com.android.contacts/contacts
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        //指定获取_id和display_name两列数据，display_name即为姓名
        String[] projection = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };
        //根据Uri查询相应的ContentProvider，cursor为获取到的数据集
        Cursor cursor = this.getContentResolver().query(uri, projection, null, null, null);
        String[] arr = new String[cursor.getCount()];
        int i = 0;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Long id = cursor.getLong(0);
                //获取姓名
                String name = cursor.getString(1);
                //指定获取NUMBER这一列数据
                String[] phoneProjection = new String[]{
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                };
                arr[i] = "姓名：" + name;

                //根据联系人的ID获取此人的电话号码
                Cursor phonesCusor = this.getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        phoneProjection,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id,
                        null,
                        null);

                //因为每个联系人可能有多个电话号码，所以需要遍历
                if (phonesCusor != null && phonesCusor.moveToFirst()) {
                    do {
                        String num = phonesCusor.getString(0);
                        arr[i] += " , 电话号码：" + num;
                    } while (phonesCusor.moveToNext());
                }
                i++;
            } while (cursor.moveToNext());
        }
        return arr;
    }

    /**
     * 手机联系人权限回调
     */
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。

            // 这里的requestCode就是申请时设置的requestCode。
            // 和onActivityResult()的requestCode一样，用来区分多个不同的请求。
            if (requestCode == 200) {
                if (AndPermission.hasPermission(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE})) {
                    getPerson();
                } else {
                    // 使用AndPermission提供的默认设置dialog，用户点击确定后会打开App的设置页面让用户授权。
                    DialogHelp.showNormalDialog(MainActivity.this);
                }
            }

        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            if (requestCode == 200) {
                if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, deniedPermissions)) {
                    if (AndPermission.hasPermission(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE})) {
                        getPerson();
                    }
                } else {
                    // 使用AndPermission提供的默认设置dialog，用户点击确定后会打开App的设置页面让用户授权。
                    DialogHelp.showNormalDialog(MainActivity.this);
                }

            }
        }
    };

    /**
     * 通话记录权限回调
     */
    private PermissionListener tonghuaListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。

            // 这里的requestCode就是申请时设置的requestCode。
            // 和onActivityResult()的requestCode一样，用来区分多个不同的请求。
            if (requestCode == 200) {
                if (AndPermission.hasPermission(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CALL_LOG})) {
                    getTongHuaj();
                } else {
                    // 使用AndPermission提供的默认设置dialog，用户点击确定后会打开App的设置页面让用户授权。
                    DialogHelp.showNormalDialog(MainActivity.this);
                }
            }

        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            if (requestCode == 200) {
                if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, deniedPermissions)) {
                    if (AndPermission.hasPermission(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CALL_LOG})) {
                        getTongHuaj();
                    }
                } else {
                    // 使用AndPermission提供的默认设置dialog，用户点击确定后会打开App的设置页面让用户授权。
                    DialogHelp.showNormalDialog(MainActivity.this);
                }

            }
        }
    };


    /**
     * 短信权限回调
     */
    private PermissionListener duanxinListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。

            // 这里的requestCode就是申请时设置的requestCode。
            // 和onActivityResult()的requestCode一样，用来区分多个不同的请求。
            if (requestCode == 200) {
                if (AndPermission.hasPermission(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CALL_LOG})) {
                    getDuanXin();
                } else {
                    // 使用AndPermission提供的默认设置dialog，用户点击确定后会打开App的设置页面让用户授权。
                    DialogHelp.showNormalDialog(MainActivity.this);
                }
            }

        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            if (requestCode == 200) {
                if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, deniedPermissions)) {
                    if (AndPermission.hasPermission(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CALL_LOG})) {
                        getDuanXin();
                    }
                } else {
                    // 使用AndPermission提供的默认设置dialog，用户点击确定后会打开App的设置页面让用户授权。
                    DialogHelp.showNormalDialog(MainActivity.this);
                }

            }
        }
    };
}
