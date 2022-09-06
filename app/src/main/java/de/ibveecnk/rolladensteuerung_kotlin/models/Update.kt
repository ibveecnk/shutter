package de.ibveecnk.rolladensteuerung_kotlin.models

import com.google.gson.annotations.SerializedName


data class Update (

  @SerializedName("status"      ) var status     : String?  = null,
  @SerializedName("has_update"  ) var hasUpdate  : Boolean? = null,
  @SerializedName("new_version" ) var newVersion : String?  = null,
  @SerializedName("old_version" ) var oldVersion : String?  = null

)