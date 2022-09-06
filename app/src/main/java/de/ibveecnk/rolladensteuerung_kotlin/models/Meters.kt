package de.ibveecnk.rolladensteuerung_kotlin.models

import com.google.gson.annotations.SerializedName


data class Meters (

  @SerializedName("power"     ) var power     : Double?           = null,
  @SerializedName("overpower" ) var overpower : Int?           = null,
  @SerializedName("is_valid"  ) var isValid   : Boolean?       = null,
  @SerializedName("timestamp" ) var timestamp : Int?           = null,
  @SerializedName("counters"  ) var counters  : ArrayList<Double> = arrayListOf(),
  @SerializedName("total"     ) var total     : Int?           = null

)
