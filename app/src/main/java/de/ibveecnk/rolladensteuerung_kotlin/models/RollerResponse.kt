package de.ibveecnk.rolladensteuerung_kotlin.models

import com.google.gson.annotations.SerializedName


data class RollerResponse (

  @SerializedName("wifi_sta"        ) var wifiSta       : WifiSta?           = WifiSta(),
  @SerializedName("cloud"           ) var cloud         : Cloud?             = Cloud(),
  @SerializedName("mqtt"            ) var mqtt          : Mqtt?              = Mqtt(),
  @SerializedName("time"            ) var time          : String?            = null,
  @SerializedName("unixtime"        ) var unixtime      : Int?               = null,
  @SerializedName("serial"          ) var serial        : Int?               = null,
  @SerializedName("has_update"      ) var hasUpdate     : Boolean?           = null,
  @SerializedName("mac"             ) var mac           : String?            = null,
  @SerializedName("cfg_changed_cnt" ) var cfgChangedCnt : Int?               = null,
  @SerializedName("actions_stats"   ) var actionsStats  : ActionsStats?      = ActionsStats(),
  @SerializedName("rollers"         ) var rollers       : ArrayList<Rollers> = arrayListOf(),
  @SerializedName("meters"          ) var meters        : ArrayList<Meters>  = arrayListOf(),
  @SerializedName("inputs"          ) var inputs        : ArrayList<Inputs>  = arrayListOf(),
  @SerializedName("update"          ) var update        : Update?            = Update(),
  @SerializedName("ram_total"       ) var ramTotal      : Int?               = null,
  @SerializedName("ram_free"        ) var ramFree       : Int?               = null,
  @SerializedName("fs_size"         ) var fsSize        : Int?               = null,
  @SerializedName("fs_free"         ) var fsFree        : Int?               = null,
  @SerializedName("voltage"         ) var voltage       : Double?            = null,
  @SerializedName("uptime"          ) var uptime        : Int?               = null

)