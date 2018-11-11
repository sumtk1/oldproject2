package com.gloiot.hygooilstation.server.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gloiot.hygooilstation.bean.Message;

import java.util.List;


public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }


    /**
     * 消息--增
     *
     * @param messages
     */
    public void addMessage(List<Message> messages) {
        Log.i("---DBManager---消息", "数量：" + messages.size());
        db.beginTransaction();
        try {
            for (Message message : messages) {
                db.execSQL("INSERT INTO DB_message VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        new Object[]{
                                message.getMessageId(), message.getSendId(),
                                message.getReceiveId(), message.getMessageType(),
                                message.getState(), message.getContent(),
                                message.getSendTime(), message.getReceiveTime(),
                                message.getReplyId(), message.getRemark()
                        });
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }


    /**
     * 数据查找
     *
     * @param table     表名
     * @param condition 条件
     * @return
     */
    public Cursor queryTables(String table, String condition) {
        Log.i("---DBManager---数据查找", "表：" + table + "条件：" + condition);
        Cursor c = db.rawQuery("SELECT * FROM " + table + " where " + condition, null);
        return c;
    }

    /**
     * 数据删除
     *
     * @param table     表名
     * @param condition 条件
     */
    public void deleteTables(String table, String condition) {
        Log.i("---DBManager---删除表", "表：" + table + "条件：" + condition);
        db.execSQL("delete from " + table + " where " + condition);
    }

    /**
     * 更新数据
     *
     * @param table
     * @param values
     * @param whereClause
     * @param whereArgs
     */
    public void updateDB(String table, ContentValues values, String whereClause, String[] whereArgs) {
        Log.i("---DBManager---数据更新", "表：" + table + "位置：" + whereClause);
        db.update(table, values, whereClause, whereArgs);
    }
}
