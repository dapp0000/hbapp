package com.uart.entitylib;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.uart.entitylib.dao.DaoMaster;
import com.uart.entitylib.dao.DaoSession;

public class DbManage {
    public static DaoSession setupDatabase(Context context,String dbName) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, dbName, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        return daoMaster.newSession();
    }
}
