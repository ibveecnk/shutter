package de.ibveecnk.rolladensteuerung_kotlin.models

import com.google.gson.annotations.SerializedName


data class Mqtt (

  @SerializedName("connected" ) var connected : Boolean? = null

)