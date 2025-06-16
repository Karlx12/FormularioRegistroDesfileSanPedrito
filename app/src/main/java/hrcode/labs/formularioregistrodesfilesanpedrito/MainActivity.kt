package hrcode.labs.formularioregistrodesfilesanpedrito

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hrcode.labs.formularioregistrodesfilesanpedrito.ui.ListScreen
import hrcode.labs.formularioregistrodesfilesanpedrito.ui.RegisterScreen
import hrcode.labs.formularioregistrodesfilesanpedrito.ui.composables.BottomNavigationBar
import hrcode.labs.formularioregistrodesfilesanpedrito.ui.theme.FormularioDesfileAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FormularioDesfileAppTheme {
                FormularioDesfileApp()
            }
        }
    }
}

sealed class Screen(
    val route: String,
    val label: String? = null,
    val icon: ImageVector? = null,
    val iconResourceId: Int? = null
) {
    object RegisterRoute : Screen("register", "Registro", iconResourceId = R.drawable.checkbook_24dp_e3e3e3_fill0_wght400_grad0_opsz24)
    object ListRoute : Screen("list", "Lista", iconResourceId = R.drawable.list_24dp_e3e3e3_fill0_wght400_grad0_opsz24)
}

@Composable
fun FormularioDesfileApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.RegisterRoute.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.RegisterRoute.route) {
                RegisterScreen(
                    navigateToList = {
                        navController.navigate(Screen.ListRoute.route)
                    }
                )
            }

            composable(Screen.ListRoute.route) {
                ListScreen(
                    navigateToRegister = {
                        navController.navigate(Screen.RegisterRoute.route) {
                            popUpTo(Screen.RegisterRoute.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
