package hrcode.labs.formularioregistrodesfilesanpedrito.data.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hrcode.labs.formularioregistrodesfilesanpedrito.domain.Person
import hrcode.labs.formularioregistrodesfilesanpedrito.domain.TypePerson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PersonViewModel(
    private val repository: PersonRepository
): ViewModel() {
    private val _allPersons = MutableStateFlow<List<Person>>(emptyList())
    val allPersons: StateFlow<List<Person>> = _allPersons

    // Lista original sin filtrar para mantener los datos completos
    private var originalPersons: List<Person> = emptyList()

    // Estado para el filtro actual
    private val _currentFilter = MutableStateFlow<TypePerson?>(null)
    val currentFilter: StateFlow<TypePerson?> = _currentFilter

    init {
        loadAllPersons()
    }

    fun loadAllPersons() {
        viewModelScope.launch {
            originalPersons = repository.getAll()
            _allPersons.value = originalPersons
        }
    }

    fun insertPerson(person: Person) {
        viewModelScope.launch {
            repository.add(person)
            loadAllPersons()
        }
    }

    fun deletePerson(id: Int) {
        viewModelScope.launch {
            repository.remove(id)
            loadAllPersons()
        }
    }

    override fun onCleared() {
        repository.close()
        super.onCleared()
    }

    fun sortPersons(sortOption: Pair<String, Boolean>) {
        val (field, ascending) = sortOption
        _allPersons.value = when (field) {
            "name" -> {
                if (ascending) _allPersons.value.sortedBy { it.name }
                else _allPersons.value.sortedByDescending { it.name }
            }
            "lastName" -> {
                if (ascending) _allPersons.value.sortedBy { it.lastName }
                else _allPersons.value.sortedByDescending { it.lastName }
            }
            "type" -> {
                if (ascending) _allPersons.value.sortedBy { it.type.value }
                else _allPersons.value.sortedByDescending { it.type.value }
            }
            else -> _allPersons.value
        }
    }

    // Nueva función para filtrar por tipo de persona
    fun filterByType(type: TypePerson?) {
        _currentFilter.value = type
        _allPersons.value = if (type == null) {
            originalPersons
        } else {
            originalPersons.filter { it.type == type }
        }
    }

    // Aplica el filtro actual después de recargar los datos
    fun applyCurrentFilter() {
        filterByType(_currentFilter.value)
    }
}