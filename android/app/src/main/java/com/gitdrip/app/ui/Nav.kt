package com.gitdrip.app.ui

import android.provider.Settings
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gitdrip.app.MainViewModel
import com.gitdrip.app.NewProject
import com.gitdrip.app.ProjectDetail
import com.gitdrip.app.ProjectList
import com.gitdrip.app.RunDetailScreen
import com.gitdrip.app.SettingsScreen
import com.gitdrip.app.SetupScreen
import com.gitdrip.app.ui.screens.DashboardScreen

private const val ENTER_MS = 280
private const val EXIT_MS = 200

/** Route table + 200-300 ms fade/slide transitions (off when the system animation scale is 0). Route names are unchanged since U1. */
@Composable
fun GitDripNav(vm: MainViewModel = viewModel()) {
    val nav = rememberNavController()
    val resolver = LocalContext.current.contentResolver
    val animate = remember { DashboardLogic.animationsEnabled(Settings.Global.getFloat(resolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f)) }
    val enter = if (animate) fadeIn(tween(ENTER_MS)) + slideInHorizontally(tween(ENTER_MS)) { it / 12 } else EnterTransition.None
    val exit = if (animate) fadeOut(tween(EXIT_MS)) else ExitTransition.None
    val popEnter = if (animate) fadeIn(tween(ENTER_MS)) + slideInHorizontally(tween(ENTER_MS)) { -it / 12 } else EnterTransition.None
    val popExit = if (animate) fadeOut(tween(EXIT_MS)) + slideOutHorizontally(tween(EXIT_MS)) { it / 12 } else ExitTransition.None
    NavHost(
        nav, "dashboard",
        enterTransition = { enter }, exitTransition = { exit }, popEnterTransition = { popEnter }, popExitTransition = { popExit },
    ) {
        composable("dashboard") {
            DashboardScreen(
                vm,
                onProjects = { nav.navigate("projects") }, onSetup = { nav.navigate("setup") }, onSettings = { nav.navigate("settings") },
                onRun = { nav.navigate("run/$it") },
            )
        }
        composable("run/{id}", listOf(navArgument("id") { type = NavType.LongType })) { e ->
            RunDetailScreen(vm, e.arguments!!.getLong("id")) { nav.popBackStack() }
        }
        composable("setup") { SetupScreen(vm) { nav.popBackStack() } }
        composable("settings") { SettingsScreen(vm) { nav.popBackStack() } }
        composable("projects") {
            ProjectList(vm, { nav.popBackStack() }, { nav.navigate("new") }, { nav.navigate("project/$it") })
        }
        composable("new") { NewProject(vm) { nav.popBackStack() } }
        composable("project/{id}", listOf(navArgument("id") { type = NavType.LongType })) { e ->
            ProjectDetail(vm, e.arguments!!.getLong("id"), { nav.navigate("run/$it") }) { nav.popBackStack() }
        }
    }
}
