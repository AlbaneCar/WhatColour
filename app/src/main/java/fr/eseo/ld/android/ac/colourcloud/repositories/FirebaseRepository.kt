package fr.eseo.ld.android.ac.colourcloud.repositories

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import fr.eseo.ld.android.ac.colourcloud.model.HighScore
import kotlinx.coroutines.tasks.await

class FirebaseRepository {
    private val database: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }
    val scoresRef: DatabaseReference = database.reference.child("scores")
    init {
        Log.d("HighScoreScreen","scoresRef: $scoresRef")
    }
    suspend fun getTopScores(): List<HighScore> {
        Log.d("HighScoreScreen","OK1")
        val snapshot = scoresRef.orderByChild("score").limitToLast(100).get().await()

        Log.d("HighScoreScreen","OK2")
        val scores = mutableListOf<HighScore>()

        Log.d("HighScoreScreen","OK3")
        snapshot.children.forEach {
            it.getValue(HighScore::class.java)?.let { score ->
                scores.add(score)

                Log.d("HighScoreScreen","OK4")
            }
        }
        return scores.sortedByDescending { it.score }
    }

    suspend fun addScore(newHighScore: HighScore) {
        val scores = getTopScores().toMutableList()
        if (scores.size < 100 || newHighScore.score > scores.last().score) {
            if (scores.size >= 100) {
                scores.removeAt(scores.size - 1)
            }
            scores.add(newHighScore)
            scores.sortedByDescending { it.score }
            scoresRef.setValue(scores).await()
        }
    }
}