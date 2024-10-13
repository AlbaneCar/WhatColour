package fr.eseo.ld.android.ac.colourcloud.ui.screens

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import fr.eseo.ld.android.ac.colourcloud.R
import fr.eseo.ld.android.ac.colourcloud.ui.state.GameUiState
import fr.eseo.ld.android.ac.colourcloud.ui.viewmodels.GameViewModel

@Composable
fun WelcomeTitle() {
    Text(
        text = stringResource(id = R.string.game_description),
        modifier = Modifier.padding(8.dp),
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 32.sp,
        textAlign = TextAlign.Center,
    )
}

@Composable
fun WelcomeButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = text,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(8.dp)
        )
    }
}

@Composable
fun WelcomeScores(gameUiState: GameUiState) {
    Text(
        text = stringResource(R.string.last_score, gameUiState.lastScore),
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(8.dp)
    )
    Text(
        text = stringResource(R.string.local_high_score, gameUiState.localHighScore),
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(8.dp)
    )
}

@Composable
fun WelcomeScreen(context: Context, playGameClick: () -> Unit, highScoreClick: () -> Unit) {
    val viewModel: GameViewModel = hiltViewModel()
    val gameUiState by viewModel.uiState.collectAsState()
    val orientation = LocalConfiguration.current.orientation

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
        PortraitWelcomeScreen(
            gameUiState = gameUiState,
            playGameClick = playGameClick,
            highScoreClick = highScoreClick
        )
    } else {
        LandscapeWelcomeScreen(
            gameUiState = gameUiState,
            playGameClick = playGameClick,
            highScoreClick = highScoreClick
        )
    }
}

@Composable
fun PortraitWelcomeScreen(
    gameUiState: GameUiState,
    playGameClick: () -> Unit,
    highScoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.safeDrawingPadding(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        WelcomeTitle()
        WelcomeButton(
            text = stringResource(R.string.play_game_button, gameUiState.lastScore),
            onClick = playGameClick
        )
        WelcomeScores(gameUiState)
        WelcomeButton(
            text = stringResource(R.string.show_global_scores_button, gameUiState.localHighScore),
            onClick = highScoreClick
        )
    }
}

@Composable
fun LandscapeWelcomeScreen(
    gameUiState: GameUiState,
    playGameClick: () -> Unit,
    highScoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        WelcomeTitle()
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                WelcomeScores(gameUiState)
            }
            Column(
                modifier = Modifier
                    .weight(0.4f)
                    .padding(start = 8.dp)
            ) {
                WelcomeButton(
                    text = stringResource(id = R.string.play_game_button),
                    onClick =
                    playGameClick
                )
                WelcomeButton(
                    text = stringResource(R.string.show_global_scores_button, gameUiState.localHighScore),
                    onClick = highScoreClick
                )
            }
        }
    }
}
