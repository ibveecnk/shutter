package de.ibveecnk.rolladensteuerung_kotlin.models

import com.google.gson.annotations.SerializedName


data class ActionsStats (

  @SerializedName("skipped" ) var skipped : Int? = null

)