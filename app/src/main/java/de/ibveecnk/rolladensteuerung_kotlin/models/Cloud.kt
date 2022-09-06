package de.ibveecnk.rolladensteuerung_kotlin.models

import com.google.gson.annotations.SerializedName


data class Cloud (

  @SerializedName("enabled"   ) var enabled   : Boolean? = null,
  @SerializedName("connected" ) var connected : Boolean? = null

)