package hrcode.labs.formularioregistrodesfilesanpedrito.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import hrcode.labs.formularioregistrodesfilesanpedrito.data.student.*
import hrcode.labs.formularioregistrodesfilesanpedrito.domain.Person
import hrcode.labs.formularioregistrodesfilesanpedrito.domain.TypePerson

@Composable
fun ListScreen(
    navigateToRegister: () -> Unit,
    viewModel: PersonViewModel = viewModel(
        factory = PersonViewModelFactory(
            PersonRepository(LocalContext.current)
        )
    )
) {
    val persons by viewModel.allPersons.collectAsState()
    val currentFilter by viewModel.currentFilter.collectAsState()
    var selectedPerson by remember { mutableStateOf<Person?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }
    var showSortDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        viewModel.loadAllPersons()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Participantes Registrados",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            // Botones de filtro y ordenamiento
            Row {
                // Botón de filtrado
                FilledTonalIconButton(
                    onClick = { showFilterDialog = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filtrar por tipo"
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Botón de ordenamiento
                FilledTonalIconButton(
                    onClick = { showSortDialog = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.Sort,
                        contentDescription = "Ordenar"
                    )
                }
            }
        }

        // Mostrar filtro actual si está aplicado
        currentFilter?.let { filter ->
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Filtrado por: ${filter.value}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    FilledTonalButton(
                        onClick = { viewModel.filterByType(null) },
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text("Borrar filtro")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (persons.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No hay participantes registrados",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(persons) { person ->
                    PersonItem(
                        person = person,
                        onDelete = {
                            selectedPerson = person
                            showDeleteDialog = true
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = navigateToRegister,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar Nuevo Participante")
        }
    }

    // Diálogo de eliminación
    if (showDeleteDialog && selectedPerson != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Está seguro que desea eliminar a ${selectedPerson?.name} ${selectedPerson?.lastName}?") },
            confirmButton = {
                Button(
                    onClick = {
                        selectedPerson?.id?.let { viewModel.deletePerson(it) }
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Diálogo de filtrado
    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            title = { Text("Filtrar por tipo") },
            text = {
                Column {
                    FilterOption(
                        text = "Todos",
                        selected = currentFilter == null,
                        onClick = {
                            viewModel.filterByType(null)
                            showFilterDialog = false
                        }
                    )
                    FilterOption(
                        text = TypePerson.STUDENT.value,
                        selected = currentFilter == TypePerson.STUDENT,
                        onClick = {
                            viewModel.filterByType(TypePerson.STUDENT)
                            showFilterDialog = false
                        }
                    )
                    FilterOption(
                        text = TypePerson.PROFESSOR.value,
                        selected = currentFilter == TypePerson.PROFESSOR,
                        onClick = {
                            viewModel.filterByType(TypePerson.PROFESSOR)
                            showFilterDialog = false
                        }
                    )
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showFilterDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Diálogo de ordenamiento
    if (showSortDialog) {
        AlertDialog(
            onDismissRequest = { showSortDialog = false },
            title = { Text("Ordenar por") },
            text = {
                Column {
                    SortOption(
                        text = "Apellido (A-Z)",
                        onClick = {
                            viewModel.sortPersons(Pair("lastName", true))
                            showSortDialog = false
                        }
                    )
                    SortOption(
                        text = "Apellido (Z-A)",
                        onClick = {
                            viewModel.sortPersons(Pair("lastName", false))
                            showSortDialog = false
                        }
                    )
                    SortOption(
                        text = "Nombre (A-Z)",
                        onClick = {
                            viewModel.sortPersons(Pair("name", true))
                            showSortDialog = false
                        }
                    )
                    SortOption(
                        text = "Nombre (Z-A)",
                        onClick = {
                            viewModel.sortPersons(Pair("name", false))
                            showSortDialog = false
                        }
                    )
                    SortOption(
                        text = "Tipo (A-Z)",
                        onClick = {
                            viewModel.sortPersons(Pair("type", true))
                            showSortDialog = false
                        }
                    )
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showSortDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun FilterOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text)
    }
}

@Composable
fun SortOption(
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Composable
fun PersonItem(
    person: Person,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    "${person.name} ${person.lastName}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    person.email,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))

                val typeColor = when (person.type) {
                    TypePerson.PROFESSOR -> MaterialTheme.colorScheme.tertiary
                    TypePerson.STUDENT -> MaterialTheme.colorScheme.primary
                }

                Box(
                    modifier = Modifier
                        .background(
                            color = typeColor.copy(alpha = 0.15f),
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        person.type.value,
                        color = typeColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}