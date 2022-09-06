package de.ibveecnk.rolladensteuerung_kotlin.models

import com.google.gson.annotations.SerializedName


data class WifiSta (

  @SerializedName("connected" ) var connected : Boolean? = null,
  @SerializedName("ssid"      ) var ssid      : String?  = null,
  @SerializedName("ip"        ) var ip        : String?  = null,
  @SerializedName("rssi"      ) var rssi      : Int?     = null

)