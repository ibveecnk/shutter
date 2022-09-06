package de.ibveecnk.rolladensteuerung_kotlin

import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import de.ibveecnk.rolladensteuerung_kotlin.helpers.Logger
import de.ibveecnk.rolladensteuerung_kotlin.helpers.TransitionManager
import de.ibveecnk.rolladensteuerung_kotlin.room.RollerDatabase
import de.ibveecnk.rolladensteuerung_kotlin.room.schemas.RollerSchema

class AddRollerActivity : AppCompatActivity() {
    private val Name = "";
    private val IP = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_roller)
    }

    fun saveRoller(view: View) {
        val id = view.id;

        val name = findViewById<EditText>(R.id.input_name).text
        val ip = findViewById<EditText>(R.id.input_ip).text

        Thread {
            val rollerDao = RollerDatabase.getInstance(applicationContext).rollerDao()
            try {
                rollerDao.insert(RollerSchema(name.toString(), ip.toString()))
                Logger.info("Added $name, $ip");
            } catch (ex: SQLiteConstraintException){
                // Duplicate name
                runOnUiThread {
                    Toast.makeText(this, "Name bereits vorhanden.", Toast.LENGTH_LONG).show()
                    name.clear()
                }
                return@Thread
            }


            val i = Intent(this@AddRollerActivity, MainActivity::class.java)
            TransitionManager.default(this, i, false)
        }.start()
    }

    override fun onBackPressed() {
        val i = Intent(this@AddRollerActivity, MainActivity::class.java)
        TransitionManager.default(this, i, false)
    }
}
