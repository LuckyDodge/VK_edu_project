package com.example.tosh.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tosh.R
import com.example.tosh.data.AppsRepositoryImpl
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
    val viewModel: AppsViewModel = viewModel {
        AppsViewModel(AppsRepositoryImpl())
    }
    val apps by viewModel.apps.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Routes.Apps,
        modifier = modifier,
    ) {
        composable(Routes.Apps) {
            val toolbarMessage = stringResource(R.string.toolbar_snackbar)
            AppsScreen(
                apps = apps,
                onAppClick = { app ->
                    navController.navigate(Routes.appDetails(app.id))
                },
                onToolbarClick = { viewModel.onToolbarClick(toolbarMessage) },
                snackbarMessages = viewModel.snackbarMessages,
            )
        }
        composable(
            route = Routes.AppDetails,
            arguments = listOf(
                navArgument("appId") { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            val appId = backStackEntry.arguments?.getString("appId")
            val app = apps.firstOrNull { it.id == appId } ?: apps.firstOrNull() ?: return@composable

            AppDetailsScreen(
                app = app,
                showcaseApps = apps,
                onBackClick = { navController.popBackStack() },
                onShowcaseAppClick = {
                    navController.popBackStack(Routes.Apps, inclusive = false)
                },
            )
        }
    }
}
