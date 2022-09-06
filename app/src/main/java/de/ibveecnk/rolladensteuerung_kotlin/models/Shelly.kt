package de.ibveecnk.rolladensteuerung_kotlin.models

import com.google.gson.annotations.SerializedName


data class Shelly (

    @SerializedName("type"         ) var type         : String?  = null,
    @SerializedName("mac"          ) var mac          : String?  = null,
    @SerializedName("auth"         ) var auth         : Boolean? = null,
    @SerializedName("fw"           ) var fw           : String?  = null,
    @SerializedName("discoverable" ) var discoverable : Boolean? = null,
    @SerializedName("num_outputs"  ) var numOutputs   : Int?     = null,
    @SerializedName("num_meters"   ) var numMeters    : Int?     = null,
    @SerializedName("num_rollers"  ) var numRollers   : Int?     = null

)
