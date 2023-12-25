package jp.oecu.cs.guid.ht21a021_repo01

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EditorActivity : AppCompatActivity(), View.OnClickListener {

    // 編集中の項目IDです。
    private var editorId: Long = 0

    // 編集項目を修正するために必要な変数（編集データのIDを検索）
    private val WHERE_ID_SQL = GUIDDBOpenHelper.COLUMN_ID + " = ?"
    private lateinit var db: SQLiteDatabase

    // インテント
    private lateinit var intent: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        var helper = GUIDDBOpenHelper(this)
        db = helper.writableDatabase

        // event handler settings
        var oprt_save: Button = findViewById(
            R.id.btnSave
        )
        oprt_save.setOnClickListener(this)

        // Extraが指定（既存データの編集）された場合、その設定を反映する
        intent = getIntent()
        if (intent.hasExtra(MainActivity.EXTRA_ID)) {
            val editor_title: EditText = findViewById(R.id.edtTitle)
            val editor_memo: EditText = findViewById(R.id.edtMemo)

            editorId = intent.getLongExtra(MainActivity.EXTRA_ID, 0)

            editor_title.setText(intent.getStringExtra(MainActivity.EXTRA_TITLE))
            editor_memo.setText(intent.getStringExtra(MainActivity.EXTRA_MEMO))
        } else {
            editorId = -1
        }
    }

    override fun onClick(v: View) {
        val editor_title: EditText = findViewById(R.id.edtTitle)
        val editor_memo: EditText = findViewById(R.id.edtMemo)

        if (v.id == R.id.btnSave) {
            val title = editor_title.text.toString()
            val memo = editor_memo.text.toString()
            val strdate = SimpleDateFormat("yyyy/mm/dd", Locale.JAPAN).format(Date())

            val insertValues = ContentValues().apply {
                put(GUIDDBOpenHelper.COLUMN_TITLE, title)
                put(GUIDDBOpenHelper.COLUMN_MEMO, memo)
                put(GUIDDBOpenHelper.COLUMN_DATE, strdate)
            }

            if (editorId != -1L) {
                var whereArgs = arrayOf(editorId.toString())
                db.update(GUIDDBOpenHelper.DB_TABLE, insertValues, WHERE_ID_SQL, whereArgs)
            } else {
                db.insert(GUIDDBOpenHelper.DB_TABLE, null, insertValues)
            }

            setResult(Activity.RESULT_OK, intent) // 処理が正しく終わったことをintentに入れておく
            finish() // 終了（前の画面の戻る）
        }
    }
}