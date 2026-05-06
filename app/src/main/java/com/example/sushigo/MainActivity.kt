package com.example.sushigo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.sushigo.ui.navigation.NavGraph
import com.example.sushigo.ui.navigation.Screen
import com.example.sushigo.ui.navigation.bottomNavItems
import com.example.sushigo.ui.theme.SushigoTheme
import com.example.sushigo.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val mainViewModel: MainViewModel = hiltViewModel()
            val isDarkModePref by mainViewModel.isDarkMode.collectAsState()
            val useDarkTheme = isDarkModePref ?: isSystemInDarkTheme()

            SushigoTheme(darkTheme = useDarkTheme) {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                val showBottomBar = bottomNavItems.any { it.route == currentDestination?.route }

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            NavigationBar(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 12.dp)
                                    .clip(RoundedCornerShape(32.dp)), // Делаем панель "плавающей"
                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                                tonalElevation = 8.dp
                            ) {
                                bottomNavItems.forEach { screen ->
                                    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                                    NavigationBarItem(
                                        icon = { 
                                            screen.icon?.let { 
                                                Icon(
                                                    it, 
                                                    contentDescription = screen.title,
                                                    tint = if (selected) MaterialTheme.colorScheme.primary else Color.Gray
                                                ) 
                                            } 
                                        },
                                        label = { 
                                            Text(
                                                screen.title, 
                                                style = MaterialTheme.typography.labelSmall,
                                                color = if (selected) MaterialTheme.colorScheme.primary else Color.Gray
                                            ) 
                                        },
                                        selected = selected,
                                        colors = NavigationBarItemDefaults.colors(
                                            indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                                        ),
                                        onClick = {
                                            navController.navigate(screen.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    NavGraph(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
