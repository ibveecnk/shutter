package de.ibveecnk.rolladensteuerung_kotlin.models

import com.google.gson.annotations.SerializedName


data class Inputs (

  @SerializedName("input"     ) var input    : Int?    = null,
  @SerializedName("event"     ) var event    : String? = null,
  @SerializedName("event_cnt" ) var eventCnt : Int?    = null

)