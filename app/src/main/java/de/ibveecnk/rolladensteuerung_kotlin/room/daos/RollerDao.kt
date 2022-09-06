package de.ibveecnk.rolladensteuerung_kotlin.room.daos

import androidx.room.*
import de.ibveecnk.rolladensteuerung_kotlin.room.schemas.RollerSchema

@Dao
interface RollerDao {
    @Insert
    fun insert(roller: RollerSchema)

    @Update
    fun update(roller: RollerSchema)

    @Delete
    fun delete(roller: RollerSchema)

    @Query("DELETE FROM roller_table WHERE name = :name")
    fun deleteByName(name: String)

    @Query("SELECT * FROM roller_table")
    fun loadAll(): List<RollerSchema?>?

    @Query("SELECT * FROM roller_table WHERE name like :name")
    fun getInfo(name: String): List<RollerSchema?>?
}
