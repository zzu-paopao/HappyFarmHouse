package com.zzu.luanchuan.utils;

import android.net.Uri;

import com.zzu.luanchuan.constant.Constants;

import java.io.File;

@SuppressWarnings("all")
public class StorageUtils {
    /**
     * 这个方法用来在应用启动的时候，检查并创建必要的文件夹
     */
    public static void initial_storage() {
        File f = new File(Constants.ROOT);
        if (!f.exists()) {
            f.mkdir();
        }
        f = new File(Constants.ROOT + "temp/");
        if (!f.exists()) {
            f.mkdir();
        }

    }

    /**
     * 此方法用于创建一个临时的Uri用于存储临时照片
     *
     * @param 临时文件名
     * @return 临时的Uri
     */
    public static Uri get_temp_photo_uri() {
        //先检查
        initial_storage();
        String file_name = System.currentTimeMillis() + ".jpg";
        File f = new File(Constants.ROOT + "temp/", file_name);
        return Uri.fromFile(f);
    }

}
