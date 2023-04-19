package net.yuuzu.expenseapp.main_feature

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import net.yuuzu.expenseapp.main_feature.presentation.addedit_screen.AddEditScreen
import net.yuuzu.expenseapp.main_feature.presentation.details_screen.DetailScreen
import net.yuuzu.expenseapp.main_feature.presentation.main_screen.ExpenseScreen
import net.yuuzu.expenseapp.main_feature.presentation.util.Screen
import net.yuuzu.expenseapp.ui.theme.ExpenseAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExpenseAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.ExpenseScreen.route
                    ) {
                        composable(route = Screen.ExpenseScreen.route) {
                            ExpenseScreen(navController = navController)
                        }
                        composable(
                            route = Screen.AddEditScreen.route +
                                    "?expenseId={expenseId}",
                            arguments = listOf(
                                navArgument(
                                    name = "expenseId"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ) {
                            AddEditScreen(navController = navController)
                        }
                        composable(route = Screen.DetailScreen.route) {
                            DetailScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}



