package de.ibveecnk.rolladensteuerung_kotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import de.ibveecnk.rolladensteuerung_kotlin.helpers.Logger
import de.ibveecnk.rolladensteuerung_kotlin.helpers.NetworkClient
import de.ibveecnk.rolladensteuerung_kotlin.helpers.TransitionManager
import de.ibveecnk.rolladensteuerung_kotlin.room.RollerDatabase
import de.ibveecnk.rolladensteuerung_kotlin.room.schemas.RollerSchema
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    private val networkClient: NetworkClient = NetworkClient()
    private var mainThreadRunning: AtomicBoolean = AtomicBoolean(false)
    private var terminate: AtomicBoolean = AtomicBoolean(false)
    private var rollerList: List<RollerSchema?>? = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        refreshList()
        launchThread()
    }


    override fun onResume() {
        super.onResume()
        this.terminate.set(false)
        launchThread()
    }

    override fun onPause() {
        super.onPause()
        this.terminate.set(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        this.terminate.set(true)
    }

    private fun refreshList() {
        Thread {
            val listView = findViewById<ListView>(R.id.listView)

            val rollerDao = RollerDatabase.getInstance(applicationContext).rollerDao()

            rollerList =  rollerDao.loadAll()

            if(rollerList?.isEmpty() == true) {
                Thread.sleep(200)
                refreshList()
            }

            val rollerNames = rollerList?.map { it?.name }

            val arrayAdapter: ArrayAdapter<String> =
                ArrayAdapter<String>(this, R.layout.activity_list_view, R.id.textView, rollerNames!!)

            runOnUiThread {
                listView.adapter = arrayAdapter

                listView.onItemClickListener = OnItemClickListener { parent, _, position, _ ->
                    Thread {
                        val btn = parent.getItemAtPosition(position)

                        val rollerData = rollerDao.getInfo(btn.toString())?.map { it?.ip }

                        val i = Intent(this@MainActivity, RollerActivity::class.java)
                            .putExtra("title", btn.toString())
                            .putExtra("endpoint", rollerData?.get(0))

                        // Is on index 0 in history frame anyways
                        TransitionManager.default(this, i, false)
                    }.start()
                }

                listView.onItemLongClickListener =
                    AdapterView.OnItemLongClickListener { parent, _, position, _ ->
                        val btn = parent.getItemAtPosition(position)

                        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                            .setCancelable(true)
                            .setTitle("Confirm")
                            .setMessage("Delete Roller '${btn}'?")
                            .setPositiveButton("Yes") { _, _ ->
                                Thread {
                                    rollerDao.deleteByName(btn.toString())
                                    runOnUiThread { refreshList() }
                                }.start()
                            }
                        builder.setNegativeButton(android.R.string.cancel) { _, _ ->

                        }

                        val dialog: AlertDialog = builder.create()

                        runOnUiThread {
                            dialog.show()
                        }

                        return@OnItemLongClickListener true
                    }
            }
        }.start()
    }

    private fun launchThread() {
        thread {
            if (this.mainThreadRunning.get()) return@thread
            this.mainThreadRunning.set(true)

            Logger.info(
                "Started thread"
            )

            while (!this.terminate.get()) {
                rollerList?.forEach {
                    val current = it
                    Thread {
                        if(it != null) {
                            val enabled = networkClient.ping(it.ip)
                            val button = getListViewItemByContent(current?.name.toString())

                            runOnUiThread {
                                button?.isEnabled = enabled
                            }
                        }

                    }.start()
                }
                Thread.sleep(2000)
            }
        }
    }

    private fun getListViewItemByContent(text: String) : Button? {
        val listView = findViewById<ListView>(R.id.listView)

        listView.children.forEach {
            val linearLayout = it as LinearLayout
            val button = linearLayout.children.first() as Button

            if (button.text.toString() == text) {
                return button
            }
        }
        return null
    }

    fun buttonPressed(view: View) {
        val id = view.id

        val btn = findViewById<Button>(id)

        val i = Intent(this@MainActivity, RollerActivity::class.java).putExtra("title", btn.text)

        // Is on index 0 in history frame anyways
        TransitionManager.default(this, i, false)
    }

    fun addRoller(view: View) {
        val i = Intent(this@MainActivity, AddRollerActivity::class.java)
        TransitionManager.default(this, i, false)
    }

    fun editRoller(view: View) {
        Logger.error("Not implemented")
    }
}
