package com.pascal.dansandroid.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pascal.dansandroid.data.prefs.PreferencesLogin
import com.pascal.dansandroid.ui.screen.account.AccountScreen
import com.pascal.dansandroid.ui.screen.detail.DetailScreen
import com.pascal.dansandroid.ui.screen.home.HomeScreen
import com.pascal.dansandroid.ui.screen.login.LoginScreen
import com.pascal.dansandroid.ui.screen.splash.SplashScreen

@Composable
fun RouteScreen(
    navController: NavHostController = rememberNavController(),
) {
    val context = LocalContext.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in listOf(
                    Screen.HomeScreen.route,
                    Screen.AccountScreen.route
            )) {
                BottomBar(navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.SplashScreen.route,
            enterTransition = {
                fadeIn(animationSpec = tween(700))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(700))
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(700))
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(700))
            }
        ) {
            composable(route = Screen.SplashScreen.route) {
                SplashScreen(
                    paddingValues = paddingValues
                ) {
                    navController.popBackStack()
                    navController.navigate(
                        if (PreferencesLogin.getIsLogin(context)) Screen.HomeScreen.route
                        else Screen.LoginScreen.route
                    ) {
                        popUpTo(Screen.SplashScreen.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            }
            composable(route = Screen.LoginScreen.route) {
                LoginScreen(
                    paddingValues = paddingValues,
                    onLogin = {
                        navController.popBackStack()
                        navController.navigate( Screen.HomeScreen.route) {
                            popUpTo(Screen.LoginScreen.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(route = Screen.HomeScreen.route) {
                HomeScreen(
                    paddingValues = paddingValues,
                    onDetail = {
                        navController.popBackStack()
                        navController.navigate(Screen.DetailScreen.createRoute(it))
                    }
                )
            }
            composable(route = Screen.DetailScreen.route) {
                DetailScreen(
                    paddingValues = paddingValues,
                    id = it.arguments?.getString("id") ?: "",
                    onNavBack = {
                        navController.popBackStack()
                        navController.navigate(Screen.HomeScreen.route)
                    }
                )
            }
            composable(route = Screen.AccountScreen.route) {
                AccountScreen(
                    paddingValues = paddingValues,
                    onLogout = {
                        navController.popBackStack()
                        navController.navigate( Screen.LoginScreen.route) {
                            popUpTo(Screen.HomeScreen.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}
