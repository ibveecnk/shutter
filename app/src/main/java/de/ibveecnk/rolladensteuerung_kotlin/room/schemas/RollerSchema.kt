package de.ibveecnk.rolladensteuerung_kotlin.room.schemas

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "roller_table")
data class RollerSchema(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val ip: String,
)
