package fr.eseo.ld.android.ac.colourcloud.model

import androidx.annotation.ColorRes
import androidx.annotation.StringRes

data class ColourData(
    @StringRes val nameId : Int,
    @ColorRes val colourId : Int
)
