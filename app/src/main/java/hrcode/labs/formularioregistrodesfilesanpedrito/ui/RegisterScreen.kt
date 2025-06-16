package hrcode.labs.formularioregistrodesfilesanpedrito.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import hrcode.labs.formularioregistrodesfilesanpedrito.data.database.DbHelper
import hrcode.labs.formularioregistrodesfilesanpedrito.data.student.*
import hrcode.labs.formularioregistrodesfilesanpedrito.domain.Person
import hrcode.labs.formularioregistrodesfilesanpedrito.domain.TypePerson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navigateToList: () -> Unit,
    viewModel: PersonViewModel = viewModel(
        factory = PersonViewModelFactory(
            PersonRepository(LocalContext.current)
        )
    )
) {
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var typeSelected by remember { mutableStateOf(TypePerson.STUDENT) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Registro para el Desfile San Pedrito",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 24.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Apellidos") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            singleLine = true
        )

        Text(
            "Tipo de Participante:",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp),
            fontSize = 16.sp
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = typeSelected == TypePerson.STUDENT,
                onClick = { typeSelected = TypePerson.STUDENT }
            )
            Text(
                text = "Estudiante",
                modifier = Modifier.clickable { typeSelected = TypePerson.STUDENT }
            )

            Spacer(modifier = Modifier.width(16.dp))

            RadioButton(
                selected = typeSelected == TypePerson.PROFESSOR,
                onClick = { typeSelected = TypePerson.PROFESSOR }
            )
            Text(
                text = "Profesor",
                modifier = Modifier.clickable { typeSelected = TypePerson.PROFESSOR }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                val person = Person(
                    name = name,
                    lastName = lastName,
                    email = email,
                    type = typeSelected
                )
                viewModel.insertPerson(person)
                showSuccessDialog = true
                name = ""
                lastName = ""
                email = ""
                typeSelected = TypePerson.STUDENT
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = name.isNotBlank() && lastName.isNotBlank() && email.isNotBlank()
        ) {
            Text("Registrar Participante")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("Registro Exitoso") },
            text = { Text("El participante ha sido registrado correctamente.") },
            confirmButton = {
                Button(onClick = { showSuccessDialog = false }) {
                    Text("Aceptar")
                }
            }
        )
    }
}