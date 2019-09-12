package com.example.phone;


/**
 * @author Mr.老王
 * @desc 手机联系人的数据bean
 * @email wkz123011@gmail.com
 */

public class ContactInfo {
    public String id;

    public String name;
    public String phone;

    public ContactInfo(String id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "name='" + name + '\'' +
                ", phone='" + phone ;
    }
}
