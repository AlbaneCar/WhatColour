package fr.eseo.ld.android.ac.colourcloud.model

data class PreviousGuess(
    val correctColourData: ColourData,
    val guessedColourData: ColourData
){
    val result : Boolean
    init {
        result = correctColourData == guessedColourData
    }
}
