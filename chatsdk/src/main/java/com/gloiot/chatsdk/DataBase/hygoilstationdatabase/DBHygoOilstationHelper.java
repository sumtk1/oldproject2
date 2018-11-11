package com.gloiot.chatsdk.DataBase.hygoilstationdatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gloiot.chatsdk.utils.Constant;

/**
 * Created by Dlt on 2017/6/16 13:50
 */
public class DBHygoOilstationHelper  extends SQLiteOpenHelper {

    public DBHygoOilstationHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);//第三个参数允许在查询数据库的时候返回一个自定义的cursor,一般传入null。
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE if not exists" + Constant.DB_MESSAGE + "(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                Constant.DB_MESSAGE_MESSAGEID + " TEXT DEFAULT \"\"," +
                Constant.DB_MESSAGE_SENDID + " TEXT DEFAULT \"\"," +
                Constant.DB_MESSAGE_RECEIVEID + " TEXT DEFAULT \"\"," +
                Constant.DB_MESSAGE_MESSAGETYPE + " TEXT DEFAULT \"\"," +
                Constant.DB_MESSAGE_STATE + " TEXT DEFAULT \"\"," +
                Constant.DB_MESSAGE_CONTENT + " TEXT DEFAULT \"\"," +
                Constant.DB_MESSAGE_SENDTIME + " TEXT DEFAULT \"\"," +
                Constant.DB_MESSAGE_RECEIVEIDTIME + " TEXT DEFAULT \"\"," +
                Constant.DB_MESSAGE_REPLYID + " TEXT DEFAULT \"\"," +
                Constant.DB_MESSAGE_REMARK + " TEXT DEFAULT \"\")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
