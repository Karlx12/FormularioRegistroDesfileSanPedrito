package hrcode.labs.formularioregistrodesfilesanpedrito.domain

enum class TypePerson(val value: String) {
    PROFESSOR("Professor"),
    STUDENT("Student");

    companion object {
        fun fromString(value: String): TypePerson {
            return values().find { it.value == value } ?: STUDENT
        }
    }
}
