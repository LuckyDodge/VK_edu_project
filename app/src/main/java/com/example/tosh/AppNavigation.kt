package com.example.tosh

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

private object Routes {
    const val Apps = "apps"
    const val AppDetails = "app/{appId}"

    fun appDetails(appId: String) = "app/$appId"
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Apps,
        modifier = modifier,
    ) {
        composable(Routes.Apps) {
            AppsScreen(
                apps = sampleApps,
                onAppClick = { app ->
                    navController.navigate(Routes.appDetails(app.id))
                },
            )
        }
        composable(
            route = Routes.AppDetails,
            arguments = listOf(
                navArgument("appId") { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            val appId = backStackEntry.arguments?.getString("appId")
            val app = sampleApps.firstOrNull { it.id == appId } ?: sampleApps.first()

            AppDetailsScreen(
                app = app,
                showcaseApps = sampleApps,
                onBackClick = { navController.popBackStack() },
                onShowcaseAppClick = {
                    navController.popBackStack(Routes.Apps, inclusive = false)
                },
            )
        }
    }
}
