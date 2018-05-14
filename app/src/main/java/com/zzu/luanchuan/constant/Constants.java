package com.zzu.luanchuan.constant;

import android.os.Environment;

/**
 * 这是所有的常量所在的类
 */
public class Constants {
    /**
     * 请求添加图片
     */
    public static final int REQUEST_ADD_IMAGE = 2;
    /**
     * 请求拍照
     */
    public static final int REQUEST_TAKE_PHOTO = 1;
    /**
     * 请求剪裁图片
     */
    public static final int REQUEST_CROP_PHOTO = 3;

    /*-------------------------------------------------------------------------------------------------*/
    /**
     * 请求读取外部存储权限
     */
    public static final int REQUEST_READ_EXTERNAL_STORAGE = 1;
    /**
     * 请求写入外部存储 相机 创建文件权限
     */
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE_CAMERA_MOUNT_UNMOUNT_FILESYSTEMS = 2;

    /*-------------------------------------------------------------------------------------------------*/
    /**
     * 这是应用在外部存储设备上的根目录
     */
    public static final String ROOT = Environment.getExternalStorageDirectory().toString() + "/农家乐/";
    /*-------------------------------------------------------------------------------------------------*/
    /**
     * 这是底部三个tab的名字
     */
    public static String[] NAMES_OF_BOTTOM_TABS = {"首页", "优惠", "我的"};


}
