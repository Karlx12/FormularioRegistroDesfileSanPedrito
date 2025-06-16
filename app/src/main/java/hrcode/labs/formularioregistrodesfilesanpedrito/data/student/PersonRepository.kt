package hrcode.labs.formularioregistrodesfilesanpedrito.data.student

import android.content.Context
import hrcode.labs.formularioregistrodesfilesanpedrito.data.database.DbHelper
import hrcode.labs.formularioregistrodesfilesanpedrito.domain.Person

class PersonRepository(private val context: Context) {

    private val dbHelper = DbHelper(context)
    private val dao by lazy { PersonDao(dbHelper.writableDatabase) }

    fun add(student: Person): Long{
        val studentSearched= dao.getAll().find { it.type == student.type }
        if (studentSearched != null) {
            return -1
        }
        return dao.insert(student)
    }
    fun getAll(): List<Person> = dao.getAll()
    fun remove(id:Int): Int = dao.delete(id)

    fun close() {
        dbHelper.close()
    }

}