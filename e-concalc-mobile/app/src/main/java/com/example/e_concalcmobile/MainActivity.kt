package com.example.e_concalcmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.e_concalcmobile.api.ApiClient
import com.example.e_concalcmobile.navigation.Screen
import com.example.e_concalcmobile.ui.screens.*
import com.example.e_concalcmobile.ui.theme.EConcalcMobileTheme
import com.example.e_concalcmobile.utils.HistoryManager
import com.example.e_concalcmobile.utils.TokenManager
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EConcalcMobileTheme {
                MainApp()
            }
        }
    }
}

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    val historyManager = remember { HistoryManager(context) }
    val scope = rememberCoroutineScope()

    val startDestination = Screen.Splash.route

    val bottomNavItems = listOf(
        BottomNavItem(Screen.Calculator.route, Icons.Default.Calculate, "Calculator"),
        BottomNavItem(Screen.Converter.route, Icons.Default.SwapHoriz, "Converter"),
        BottomNavItem(Screen.Currency.route, Icons.Default.AttachMoney, "Currency")
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute != Screen.Login.route &&
            currentRoute != Screen.Register.route &&
            currentRoute != Screen.Splash.route

    var showProfileMenu by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Logout confirmation dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout", color = Color.White, fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to logout?", color = Color(0xFF94A3B8)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        // Call API logout
                        scope.launch {
                            try {
                                val token = tokenManager.getBearerToken()
                                if (token != null) {
                                    try {
                                        ApiClient.instance.logout(token)
                                    } catch (_: Exception) {}
                                }
                            } catch (_: Exception) {}
                        }
                        // Clear local data
                        tokenManager.clearAuth()
                        historyManager.clearAllUserHistory()
                        // Navigate to login
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                ) {
                    Text("Logout", color = Color(0xFFEF4444), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel", color = Color(0xFF38BDF8))
                }
            },
            containerColor = Color(0xFF1E293B),
            shape = RoundedCornerShape(20.dp)
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (showBottomBar) {
                TopAppBar(
                    title = {
                        Text(
                            text = "E-Concalc",
                            color = Color(0xFF38BDF8),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    },
                    actions = {
                        // User profile/logout
                        Box {
                            IconButton(onClick = { showProfileMenu = true }) {
                                Surface(
                                    shape = CircleShape,
                                    color = if (tokenManager.isLoggedIn()) Color(0xFF1E3A5F) else Color(0xFF334155),
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                        Icon(
                                            if (tokenManager.isLoggedIn()) Icons.Default.Person else Icons.Default.PersonOutline,
                                            contentDescription = "Profile",
                                            tint = if (tokenManager.isLoggedIn()) Color(0xFF38BDF8) else Color(0xFF64748B),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }

                            DropdownMenu(
                                expanded = showProfileMenu,
                                onDismissRequest = { showProfileMenu = false },
                                containerColor = Color(0xFF1E293B)
                            ) {
                                if (tokenManager.isLoggedIn()) {
                                    // Show user info
                                    DropdownMenuItem(
                                        text = {
                                            Column {
                                                Text(
                                                    tokenManager.getUserName() ?: "User",
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 15.sp
                                                )
                                                Text(
                                                    tokenManager.getUserEmail() ?: "",
                                                    color = Color(0xFF64748B),
                                                    fontSize = 12.sp
                                                )
                                            }
                                        },
                                        onClick = {},
                                        leadingIcon = {
                                            Icon(Icons.Default.AccountCircle, null, tint = Color(0xFF38BDF8))
                                        }
                                    )
                                    HorizontalDivider(color = Color(0xFF334155))
                                    DropdownMenuItem(
                                        text = { Text("Logout", color = Color(0xFFEF4444)) },
                                        onClick = {
                                            showProfileMenu = false
                                            showLogoutDialog = true
                                        },
                                        leadingIcon = {
                                            Icon(Icons.Default.Logout, null, tint = Color(0xFFEF4444))
                                        }
                                    )
                                } else {
                                    DropdownMenuItem(
                                        text = {
                                            Column {
                                                Text("Guest Mode", color = Color.White, fontWeight = FontWeight.Bold)
                                                Text("History saved locally", color = Color(0xFF64748B), fontSize = 12.sp)
                                            }
                                        },
                                        onClick = {},
                                        leadingIcon = {
                                            Icon(Icons.Default.PersonOutline, null, tint = Color(0xFF64748B))
                                        }
                                    )
                                    HorizontalDivider(color = Color(0xFF334155))
                                    DropdownMenuItem(
                                        text = { Text("Login", color = Color(0xFF38BDF8)) },
                                        onClick = {
                                            showProfileMenu = false
                                            navController.navigate(Screen.Login.route)
                                        },
                                        leadingIcon = {
                                            Icon(Icons.Default.Login, null, tint = Color(0xFF38BDF8))
                                        }
                                    )
                                }
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF0F172A)
                    )
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController, bottomNavItems)
            }
        },
        containerColor = Color(0xFF0F172A)
    ) { innerPadding ->
        NavigationGraph(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .padding(innerPadding)
                .background(Color(0xFF0F172A))
        )
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    items: List<BottomNavItem>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color(0xFF1E293B),
        contentColor = Color.White,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label, maxLines = 1, fontSize = 11.sp) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF38BDF8),
                    selectedTextColor = Color(0xFF38BDF8),
                    unselectedIconColor = Color(0xFF64748B),
                    unselectedTextColor = Color(0xFF64748B),
                    indicatorColor = Color(0xFF1E3A5F)
                )
            )
        }
    }
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            val ctx = LocalContext.current
            SplashScreen(
                onSplashFinished = {
                    val dest = if (TokenManager(ctx).isLoggedIn()) {
                        Screen.Calculator.route
                    } else {
                        Screen.Login.route
                    }
                    navController.navigate(dest) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Calculator.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onSkipLogin = {
                    navController.navigate(Screen.Calculator.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Calculator.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onSkipLogin = {
                    navController.navigate(Screen.Calculator.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Calculator.route) {
            CalculatorScreen()
        }
        composable(Screen.Converter.route) {
            ConverterScreen()
        }
        composable(Screen.Currency.route) {
            CurrencyScreen()
        }
    }
}
