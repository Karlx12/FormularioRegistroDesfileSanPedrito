package hrcode.labs.formularioregistrodesfilesanpedrito.domain

data class Person (
    val id: Int=0,
    val name: String,
    val lastName: String,
    val type: TypePerson,
    val email: String
)
