package com.intgod.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Evan on 2016/3/18.
 */
public class DBManager {

    private Context context;
    private SQLiteDatabase db;
    public static String PACKAGE_NAME = "com.intgod.coolweather";
    private static String DB_NAME = "location.db";
    public static final String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + PACKAGE_NAME + "/databases/";


    public DBManager(Context context) {
        this.context = context;
    }

    public SQLiteDatabase openDatabase() {
        String dbFile = DB_PATH + DB_NAME;
        // 检查数据库文件存不存在
        File f = new File(DB_PATH);
        //如果database目录不存在，则新建该目录


        try {
            if (!new File(dbFile).exists()) {
                if (!f.exists()) {
                    f.mkdir();
                }
                // 得到 assets 目录下事先准备好的 SQLite 数据库作为输入流
                InputStream is = this.context.getAssets().open(DB_NAME);
                FileOutputStream fos = new FileOutputStream(dbFile);
                //文件写入
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
                //关闭文件流
                fos.close();
                is.close();
            }
            db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
            return db;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return db;
    }


}

