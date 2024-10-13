package fr.eseo.ld.android.ac.colourcloud.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import fr.eseo.ld.android.ac.colourcloud.model.HighScore
import fr.eseo.ld.android.ac.colourcloud.repositories.FirebaseRepository
import kotlinx.coroutines.launch

@Composable
fun HighScoreScreen(firebaseRepository: FirebaseRepository) {
    val scores = remember { mutableStateListOf<HighScore>() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val topScores = firebaseRepository.getTopScores()
                Log.d("HighScoreScreen", "Top scores fetched: $topScores")
                scores.clear()
                scores.addAll(topScores)
            } catch (e: Exception) {
                Log.e("HighScoreScreen", "Error fetching top scores: ", e)
            }
        }
        firebaseRepository.scoresRef
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        val newScores = mutableListOf<HighScore>()
                        snapshot.children.mapNotNullTo(newScores) {
                            it.getValue(HighScore::class.java)
                        }
                        val sortedScores = newScores.sortedByDescending {
                            it.score
                        }
                        Log.d("HighScoreScreen", "Scores updated: $sortedScores")
                        scores.clear()
                        scores.addAll(sortedScores)
                    } catch (e: Exception) {
                        Log.e("HighScoreScreen", "Error processing scores: ${e.message}")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("HighScoreScreen", "Error fetching scores: ${error.message}")
                }
            })
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        itemsIndexed(scores) { index, score ->
            ScoreItem(position = index + 1, score = score)
        }
    }
}


@Composable
fun ScoreItem(position: Int, score: HighScore) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "$position",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = score.name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Score: ${score.score}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
