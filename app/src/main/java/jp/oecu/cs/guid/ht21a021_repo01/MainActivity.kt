package jp.oecu.cs.guid.ht21a021_repo01

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private lateinit var dbIntent: Intent
    private lateinit var db: SQLiteDatabase
    private lateinit var listviewdb: ListView

    // SQL
    private val WHERE_ID_SQL: String = GUIDDBOpenHelper.COLUMN_ID + " = ?"

    companion object {
        const val EXTRA_ID: String = "id"
        const val EXTRA_TITLE: String = "title"
        const val EXTRA_MEMO = "memo"

    }

    private val mStartForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                reloadCursor()
            }
        }

    private fun reloadCursor() {
        val helper: GUIDDBOpenHelper = GUIDDBOpenHelper(this)
        val db: SQLiteDatabase = helper.readableDatabase
        val c: Cursor = db.query(GUIDDBOpenHelper.DB_TABLE, null, null, null, null, null, null)
        val adapter: SimpleCursorAdapter = listviewdb.adapter as SimpleCursorAdapter
        adapter.swapCursor(c)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var result = false
        if (item.itemId == R.id.add) {
            // 「追加」メニューの処理
            dbIntent = Intent()
            dbIntent!!.setClass(applicationContext, EditorActivity::class.java)
            mStartForResult.launch(dbIntent)
        } else if (item.itemId == R.id.del) {
            // 削除メニューの処理
            var adapter = listviewdb.adapter
            if (adapter.count > 0) {
                var id: Long = adapter.getItemId(adapter.count - 1)
                var whereargs = arrayOf(id.toString())
                reloadCursor()
            }
            result = true
        }
        return result
    }


    // 実行時
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // DB準備
        val helper = GUIDDBOpenHelper(this)
        // dbを読み込みようで使用することを設定
        db = helper.readableDatabase
        // クエリ発行
        val c = db.query(GUIDDBOpenHelper.DB_TABLE, null, null, null, null, null, null)
        // 表示する値の設定
        val from = arrayOf(GUIDDBOpenHelper.COLUMN_TITLE, GUIDDBOpenHelper.COLUMN_MEMO)
        // 表示場所のid設定（simple_list_item_2用）
        val to = intArrayOf(android.R.id.text1, android.R.id.text2)
        // ListView用のアダプタの作成（表示様式；simple_list_item_2）
        val adapter =
            SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, c, from, to, 0)

        // Get id of ListView
        listviewdb = findViewById(R.id.lstDB)
        listviewdb.adapter = adapter
        listviewdb.setOnItemClickListener { parent, view, pos, id ->
            val columns = arrayOf(GUIDDBOpenHelper.COLUMN_TITLE, GUIDDBOpenHelper.COLUMN_MEMO)
            val whereargs = arrayOf(id.toString())
            val c = db.query(
                GUIDDBOpenHelper.DB_TABLE,
                columns,
                WHERE_ID_SQL,
                whereargs,
                null,
                null,
                null
            )
            c.moveToFirst()


            val intent = Intent()
            intent.setClass(applicationContext, EditorActivity::class.java)
            intent.putExtra(EXTRA_ID, id)
            intent.putExtra(EXTRA_TITLE, c.getString(0))
            intent.putExtra(EXTRA_MEMO, c.getString(1))
            mStartForResult.launch(intent)
        }
    }


}