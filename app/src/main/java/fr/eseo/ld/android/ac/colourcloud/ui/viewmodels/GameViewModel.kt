package fr.eseo.ld.android.ac.colourcloud.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.eseo.ld.android.ac.colourcloud.model.ColourData
import fr.eseo.ld.android.ac.colourcloud.model.Colours
import fr.eseo.ld.android.ac.colourcloud.model.HighScore
import fr.eseo.ld.android.ac.colourcloud.model.PreviousGuess
import fr.eseo.ld.android.ac.colourcloud.model.WhatColourDataStore
import fr.eseo.ld.android.ac.colourcloud.repositories.FirebaseRepository
import fr.eseo.ld.android.ac.colourcloud.ui.state.GameUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class GameViewModel @Inject constructor(private val dataStore: WhatColourDataStore,  private val firebaseRepository : FirebaseRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()
    private var userGuess by mutableStateOf(-1)
        private set
    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            dataStore.uiStateFlow.collect { state ->
                _uiState.value = state
            }
        }
    }

    private fun selectRandomColour(exemptColour: ColourData? = null): ColourData {
        val filteredList = if (exemptColour != null) {
            Colours.colours.filter { it != exemptColour }
        } else {
            Colours.colours
        }
        val randomIndex = Random.nextInt(filteredList.size)
        return filteredList[randomIndex]
    }

    private fun updateGameState(score: Int) {
        val correctColour = selectRandomColour()
        val incorrectColour = selectRandomColour(correctColour)
        _uiState.update { currentState ->
            currentState.copy(
                currentCorrectColour = correctColour,
                currentIncorrectColour = incorrectColour,
                currentScore = score
            )
        }
    }

    private fun updateGameState(time: Long) {
        _uiState.update { currentState ->
            currentState.copy(
                timeLeft = time
            )
        }
    }

    private fun updateGameState(lastScore: Int, highScore: Int) {
        viewModelScope.launch {
            dataStore.saveScores(lastScore, highScore)
            _uiState.value =
                _uiState.value.copy(localHighScore = highScore, lastScore = lastScore)
        }
    }

    private fun updateGameState(previousGuess: PreviousGuess) {
        _uiState.update { currentState ->
            currentState.copy(
                previousGuesses = _uiState.value.previousGuesses + previousGuess
            )
        }
    }

    private fun startTimer() {
        timerJob?.cancel()

        // Launch a new timerJob in the viewModelScope
        timerJob = viewModelScope.launch {
            while (_uiState.value.timeLeft > 0) {
                val delayTime: Long = if (_uiState.value.timeLeft > 10000) {
                    1000
                } else {
                    100
                }
                delay(delayTime)
                val timeLeftAfterTick = _uiState.value.timeLeft - delayTime
                updateGameState(timeLeftAfterTick)
            }
        }
    }

    fun stopGame() {
        timerJob?.cancel()
    }

    fun startGame() {
        val correctColour = selectRandomColour()
        val incorrectColour = selectRandomColour(correctColour)
        _uiState.update { currentState ->
            currentState.copy(
                currentCorrectColour = correctColour,
                currentIncorrectColour = incorrectColour,
                currentScore = 0,
                timeLeft = 60000,
                guessedColourIndex = -1,
                previousGuesses = emptyList()
            )
        }
        startTimer()
    }

    fun checkUserGuess(guessedColourId: Int) {
        userGuess = guessedColourId
        val previousGuess: PreviousGuess
        if (userGuess.equals(_uiState.value.currentCorrectColour.nameId)) {
            previousGuess = PreviousGuess(
                _uiState.value.currentCorrectColour, _uiState.value.currentCorrectColour
            )
            val updatedScore = _uiState.value.currentScore.plus(1)
            updateGameState(score = updatedScore)
        } else {
            previousGuess = PreviousGuess(
                _uiState.value.currentCorrectColour, _uiState.value.currentIncorrectColour
            )
            val timeLeftAfterPenalty = _uiState.value.timeLeft.minus(2000)
            updateGameState(time = timeLeftAfterPenalty)
        }
        updateGameState(previousGuess)
        userGuess = -1
    }

    fun recordScore() {
        val lastScore = _uiState.value.currentScore
        val highScore =
            if (_uiState.value.currentScore > _uiState.value.localHighScore)
                _uiState.value.currentScore
            else
                _uiState.value.localHighScore
        updateGameState(lastScore, highScore)
    }

    fun addScoreToFirebase(score : HighScore) {
        viewModelScope.launch {
            firebaseRepository.addScore(score)
        }
    }
}