package com.gloiot.hygooilstation.server.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gloiot.hygooilstation.utils.ConstantUtlis;


public class DBHelper extends SQLiteOpenHelper {

    private SharedPreferences.Editor editor;

    public DBHelper(Context context) {
//        super(context, "zyydb", null, 20160928);//第三个参数允许在查询数据库的时候返回一个自定义的cursor,一般传入null。第四个参数表示当前数据库的版本号可用于数据库的升级。
        super(context, "Hygo_OilstationMessagedb", null, 1);//第三个参数允许在查询数据库的时候返回一个自定义的cursor,一般传入null。第四个参数表示当前数据库的版本号可用于数据库的升级。
    }

    @Override
    public void onCreate(SQLiteDatabase arg0) {
        arg0.execSQL("CREATE TABLE " + ConstantUtlis.DB_MESSAGE + "(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                ConstantUtlis.DB_MESSAGE_MESSAGEID + " TEXT DEFAULT \"\"," +
                ConstantUtlis.DB_MESSAGE_SENDID + " TEXT DEFAULT \"\"," +
                ConstantUtlis.DB_MESSAGE_RECEIVEID + " TEXT DEFAULT \"\"," +
                ConstantUtlis.DB_MESSAGE_MESSAGETYPE + " TEXT DEFAULT \"\"," +
                ConstantUtlis.DB_MESSAGE_STATE + " TEXT DEFAULT \"\"," +
                ConstantUtlis.DB_MESSAGE_CONTENT + " TEXT DEFAULT \"\"," +
                ConstantUtlis.DB_MESSAGE_SENDTIME + " TEXT DEFAULT \"\"," +
                ConstantUtlis.DB_MESSAGE_RECEIVEIDTIME + " TEXT DEFAULT \"\"," +
                ConstantUtlis.DB_MESSAGE_REPLYID + " TEXT DEFAULT \"\"," +
                ConstantUtlis.DB_MESSAGE_REMARK + " TEXT DEFAULT \"\")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
        if (arg1 != arg2) {
            arg0.execSQL("DROP TABLE DB_Friends");
            arg0.execSQL("DROP TABLE DB_Groups");
            arg0.execSQL("DROP TABLE DB_Strangers");
            arg0.execSQL("DROP TABLE DB_Kefus");
            arg0.execSQL("DROP TABLE DB_Image");
            arg0.execSQL("DROP TABLE Phone");
            onCreate(arg0);

//            editor = SharedPreferencesUtlis.getInstance().getSharedPreferences().edit();
//            editor.putString("", "退出");
//            editor.commit();
        }
    }

}
