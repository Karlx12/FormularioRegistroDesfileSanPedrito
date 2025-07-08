package hrcode.labs.formularioregistrodesfilesanpedrito

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hrcode.labs.formularioregistrodesfilesanpedrito.ui.ListScreen
import hrcode.labs.formularioregistrodesfilesanpedrito.ui.RegisterScreen
import hrcode.labs.formularioregistrodesfilesanpedrito.ui.WebViewScreen
import hrcode.labs.formularioregistrodesfilesanpedrito.ui.composables.BottomNavigationBar
import hrcode.labs.formularioregistrodesfilesanpedrito.ui.theme.FormularioDesfileAppTheme
import kotlinx.coroutines.launch

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
    object WebViewRoute : Screen("webview", "Feria San Pedrito", iconResourceId = null)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioDesfileApp() {
    val navController = rememberNavController()
    var darkTheme by remember { androidx.compose.runtime.mutableStateOf(false) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            val screenWidth = LocalConfiguration.current.screenWidthDp.dp
            ModalDrawerSheet(
                modifier = Modifier.width(screenWidth * 0.5f)
            ) {
                ListItem(
                    headlineContent = { Text("Cambiar tema") },
                    leadingContent = {
                        Icon(
                            painter = painterResource(id = if (darkTheme) R.drawable.brightness_7 else R.drawable.brightness_4),
                            contentDescription = "Tema"
                        )
                    },
                    trailingContent = {
                        Switch(
                            checked = darkTheme,
                            onCheckedChange = { darkTheme = it }
                        )
                    }
                )
                ListItem(
                    headlineContent = { Text("Feria San Pedrito 2025") },
                    leadingContent = {
                        Icon(
                            painter = painterResource(id = R.drawable.website),
                            contentDescription = "Web"
                        )
                    },
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.WebViewRoute.route)
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        FormularioDesfileAppTheme(darkTheme = darkTheme) {
            Scaffold(
//                modifier = Modifier.padding(WindowInsets.systemBars.asPaddingValues()),
                bottomBar = {
                    BottomNavigationBar(navController)
                },
                topBar = {
                    Box(
                        modifier = Modifier.padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                    ) {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                painter = painterResource(id = R.drawable.menu_open_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                                contentDescription = "Abrir menú"
                            )
                        }
                    }
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
                    composable(Screen.WebViewRoute.route) {
                        WebViewScreen(url = "https://andina.pe/agencia/noticia-san-pedro-y-san-pablo-que-lugares-del-peru-se-celebra-y-tradiciones-se-cumplen-990310.aspx")
                    }
                }
            }
        }
    }
}
