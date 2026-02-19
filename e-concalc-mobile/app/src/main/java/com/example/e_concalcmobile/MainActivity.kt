package com.example.e_concalcmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.e_concalcmobile.navigation.Screen
import com.example.e_concalcmobile.ui.screens.*
import com.example.e_concalcmobile.ui.theme.EConcalcMobileTheme

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

@Composable
fun MainApp() {
    val navController = rememberNavController()

    val bottomNavItems = listOf(
        BottomNavItem(Screen.Calculator.route, Icons.Default.Calculate, "Calculator"),
        BottomNavItem(Screen.Converter.route, Icons.Default.SwapHoriz, "Converter"),
        BottomNavItem(Screen.Currency.route, Icons.Default.AttachMoney, "Currency")
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(navController, bottomNavItems)
        },
        containerColor = Color(0xFF1a1a2e)
    ) { innerPadding ->
        NavigationGraph(
            navController = navController,
            modifier = Modifier
                .padding(innerPadding)
                .background(Color(0xFF1a1a2e))
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
        containerColor = Color(0xFF16213e),
        contentColor = Color.White
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label, maxLines = 1) },
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
                    selectedIconColor = Color(0xFF00D4FF),
                    selectedTextColor = Color(0xFF00D4FF),
                    unselectedIconColor = Color(0xFF8892b0),
                    unselectedTextColor = Color(0xFF8892b0),
                    indicatorColor = Color(0xFF0f3460)
                )
            )
        }
    }
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Calculator.route,
        modifier = modifier
    ) {
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
