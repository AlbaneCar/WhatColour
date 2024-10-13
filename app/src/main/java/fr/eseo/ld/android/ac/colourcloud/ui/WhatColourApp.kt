package fr.eseo.ld.android.ac.colourcloud.ui

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import fr.eseo.ld.android.ac.colourcloud.R
import fr.eseo.ld.android.ac.colourcloud.repositories.FirebaseRepository
import fr.eseo.ld.android.ac.colourcloud.ui.navigation.WhatColourScreens
import fr.eseo.ld.android.ac.colourcloud.ui.screens.GameScreen
import fr.eseo.ld.android.ac.colourcloud.ui.screens.HighScoreScreen
import fr.eseo.ld.android.ac.colourcloud.ui.screens.WelcomeScreen
import fr.eseo.ld.android.ac.colourcloud.ui.viewmodels.GameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhatColourAppBar(
    currentScreen: WhatColourScreens,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.app_name))
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(
                            id = R.string.back_button
                        )
                    )
                }
            }
        }
    )
}

@Composable
fun WhatColourApp(navController: NavHostController = rememberNavController(), context: Context) {
    val viewModel: GameViewModel = hiltViewModel()
    val firebaseRepository = FirebaseRepository()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen: WhatColourScreens = WhatColourScreens.valueOf(
        backStackEntry?.destination?.route ?: WhatColourScreens.WELCOME.name
    )

    Scaffold(
        topBar = {
            WhatColourAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        },
        content = { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = WhatColourScreens.WELCOME.name,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                composable(
                    route = WhatColourScreens.WELCOME.name
                ) {
                    WelcomeScreen(
                        context = context,
                        playGameClick = {
                            viewModel.startGame()
                            navController.navigate(WhatColourScreens.GAME.name)
                        },
                        highScoreClick = {
                            navController.navigate(WhatColourScreens.GLOBAL.name)
                        }
                    )
                }
                composable(
                    route = WhatColourScreens.GAME.name
                ) {
                    GameScreen(
                        context = context,
                        navController = navController
                    )
                }
                composable(
                    route = WhatColourScreens.GLOBAL.name
                ) {
                    HighScoreScreen(
                        firebaseRepository = firebaseRepository
                    )
                }
            }
        }
    )
}
