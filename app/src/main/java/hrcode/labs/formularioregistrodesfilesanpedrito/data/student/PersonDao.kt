package hrcode.labs.formularioregistrodesfilesanpedrito.data.student

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import hrcode.labs.formularioregistrodesfilesanpedrito.domain.Person
import hrcode.labs.formularioregistrodesfilesanpedrito.domain.TypePerson

class PersonDao(private val db: SQLiteDatabase) {

    fun insert(person: Person): Long {
        val values = ContentValues().apply {
            put(PersonContract.Columns.NAME, person.name)
            put(PersonContract.Columns.LASTNAME, person.lastName)
            put(PersonContract.Columns.TYPE, person.type.value)
            put(PersonContract.Columns.EMAIL, person.email)
        }
        return db.insert(PersonContract.TABLE_NAME, null, values)
    }

    fun getAll(): List<Person> {
        val persons = mutableListOf<Person>()
        val cursor: Cursor = db.query(
            PersonContract.TABLE_NAME,
            null, null, null, null, null, null
        )

        cursor.use {
            while (it.moveToNext()) {
                persons.add(
                    Person(
                        id = it.getInt(it.getColumnIndexOrThrow(PersonContract.Columns.ID)),
                        name = it.getString(it.getColumnIndexOrThrow(PersonContract.Columns.NAME)),
                        lastName = it.getString(it.getColumnIndexOrThrow(PersonContract.Columns.LASTNAME)),
                        type = TypePerson.fromString(it.getString(it.getColumnIndexOrThrow(PersonContract.Columns.TYPE))),
                        email = it.getString(it.getColumnIndexOrThrow(PersonContract.Columns.EMAIL))
                    )
                )
            }
        }
        return persons
    }

    fun update(person: Person): Int {
        val values = ContentValues().apply {
            put(PersonContract.Columns.NAME, person.name)
            put(PersonContract.Columns.LASTNAME, person.lastName)
            put(PersonContract.Columns.TYPE, person.type.value)
            put(PersonContract.Columns.EMAIL, person.email)
        }
        return db.update(
            PersonContract.TABLE_NAME,
            values,
            "${PersonContract.Columns.ID} = ?",
            arrayOf(person.id.toString())
        )
    }

    fun delete(id: Int): Int {
        return db.delete(
            PersonContract.TABLE_NAME,
            "${PersonContract.Columns.ID} = ?",
            arrayOf(id.toString())
        )
    }
}
