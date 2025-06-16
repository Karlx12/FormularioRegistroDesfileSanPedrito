package hrcode.labs.formularioregistrodesfilesanpedrito.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import hrcode.labs.formularioregistrodesfilesanpedrito.data.student.PersonContract

class DbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "StudentsRegister.db"
        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE = """
            CREATE TABLE ${PersonContract.TABLE_NAME} (
                ${PersonContract.Columns.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${PersonContract.Columns.NAME} TEXT NOT NULL,
                ${PersonContract.Columns.LASTNAME} TEXT NOT NULL,
                ${PersonContract.Columns.TYPE} TEXT NOT NULL,
                ${PersonContract.Columns.EMAIL} TEXT NOT NULL
            )
        """
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${PersonContract.TABLE_NAME}")
        onCreate(db)
    }
}