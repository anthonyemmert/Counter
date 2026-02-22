package com.example.composeday3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.composeday3.data.CounterRepository
import com.example.composeday3.data.SettingsRepository
import com.example.composeday3.ui.screens.AboutScreen
import com.example.composeday3.ui.screens.CounterScreen
import com.example.composeday3.ui.screens.SettingsScreen
import com.example.composeday3.ui.theme.ComposeDay3Theme
import com.example.composeday3.viewmodel.CounterViewModel
import com.example.composeday3.viewmodel.SettingsViewModel
import com.example.composeday3.viewmodel.UiEvent
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController




class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberAnimatedNavController()
            val haptics = LocalHapticFeedback.current

            // --- Day 12: Counter repository + VM ---
            val counterRepository = remember { CounterRepository(applicationContext) }
            val counterVm: CounterViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return CounterViewModel(counterRepository) as T
                    }
                }
            )
            val counterState by counterVm.uiState.collectAsState()

            // --- Day 13: Settings repository + VM ---
            val settingsRepository = remember { SettingsRepository(applicationContext) }
            val settingsVm: SettingsViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return SettingsViewModel(settingsRepository) as T
                    }
                }
            )
            val settingsState by settingsVm.uiState.collectAsState()

            // Theme is now dynamic + persisted
            ComposeDay3Theme(darkTheme = settingsState.isDarkMode) {

                val snackbarHostState = remember { SnackbarHostState() }

                // Collect one-time events from CounterViewModel
                LaunchedEffect(Unit) {
                    counterVm.events.collect { event ->
                        when (event) {
                            is UiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
                        }
                    }
                }

                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = backStackEntry?.destination?.route
                val title = routeTitle(currentRoute)

                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    topBar = {
                        TopAppBar(
                            title = { Text(text = title) },
                            navigationIcon = {
                                if (currentRoute != Routes.COUNTER) {
                                    IconButton(
                                        onClick = {
                                            haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                            navController.popBackStack()
                                        }
                                    ) {
                                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                                    }
                                }
                            },
                            actions = {
                                if (currentRoute == Routes.COUNTER) {
                                    IconButton(
                                        onClick = {
                                            haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                            navController.navigate(Routes.ABOUT)
                                        }
                                    ) {
                                        Icon(Icons.Filled.Info, contentDescription = "About")
                                    }

                                    IconButton(
                                        onClick = {
                                            haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                            navController.navigate(Routes.SETTINGS)
                                        }
                                    ) {
                                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                                    }

                                    IconButton(
                                        onClick = {
                                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                            counterVm.reset()
                                        }
                                    ) {
                                        Icon(Icons.Filled.Refresh, contentDescription = "Reset Counter")
                                    }
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                ) { innerPadding ->
                    AnimatedNavHost(
                        navController = navController,
                        startDestination = Routes.COUNTER,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Routes.COUNTER) {
                            CounterScreen(
                                count = counterState.count,
                                step = counterState.step,
                                onIncrement = { counterVm.increment() },
                                onDecrement = { counterVm.decrement() },
                                onReset = { counterVm.reset() },
                                onStepUp = { counterVm.stepUp() },
                                onStepDown = { counterVm.stepDown() },
                                onResetStep = { counterVm.resetStep() }

                            )

                        }
                        composable(Routes.ABOUT) {
                            AboutScreen()
                        }
                        composable(Routes.SETTINGS) {
                            SettingsScreen(
                                isDarkMode = settingsState.isDarkMode,
                                onToggleDarkMode = { settingsVm.toggleDarkMode() }
                            )
                        }
                    }
                }
            }
        }
    }
}
