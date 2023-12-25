package jp.oecu.cs.guid.ht21a021_repo01

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by ohnit_000 on 2022/12/9.
 * Convert to Kotlin by Yugo okamoto on 2023/12/25.
 */
class GUIDDBOpenHelper(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_SQL)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DROP_TABLE_SQL)
        onCreate(db)
    }

    companion object {
        const val DB_NAME = "GUID.db"
        const val DB_TABLE = "GUID_table"
        private const val DB_VERSION = 1
        const val COLUMN_ID = "_id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_MEMO = "memo"
        const val COLUMN_DATE = "date"
        private const val CREATE_TABLE_SQL =
            "create table " + DB_TABLE + "( " + COLUMN_ID + " integer primary key autoincrement," + COLUMN_TITLE + " text not null," + COLUMN_MEMO + " text null," + COLUMN_DATE + " text not null)"
        private const val DROP_TABLE_SQL = "drop table " + DB_TABLE
    }
}