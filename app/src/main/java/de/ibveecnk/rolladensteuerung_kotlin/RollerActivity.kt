package de.ibveecnk.rolladensteuerung_kotlin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import de.ibveecnk.rolladensteuerung_kotlin.helpers.Logger
import de.ibveecnk.rolladensteuerung_kotlin.helpers.NetworkClient
import de.ibveecnk.rolladensteuerung_kotlin.helpers.TransitionManager
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread

class RollerActivity : AppCompatActivity() {
    private var networkClient: NetworkClient = NetworkClient()
    private var terminate: AtomicBoolean = AtomicBoolean(false)
    private var destroyThreadRunning: AtomicBoolean = AtomicBoolean(false)
    private var mainThreadRunning: AtomicBoolean = AtomicBoolean(false)
    private var baseurl: String = ""
    private val formatter: NumberFormat = DecimalFormat("#0.00")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roller)

        if (!intent.hasExtra("title") || !intent.hasExtra("title")) {
            throw IllegalArgumentException("Parameters are missing.")
        }

        baseurl = intent.getStringExtra("endpoint").toString()
        findViewById<TextView>(R.id.t_title).text = intent.getStringExtra("title").toString()

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
        launchDestroyThread()
    }

    override fun onDestroy() {
        super.onDestroy()
        this.terminate.set(true)
        launchDestroyThread()
    }

    override fun onBackPressed() {
        // super.onBackPressed()
        launchDestroyThread()

        val i = Intent(this@RollerActivity, MainActivity::class.java)
        TransitionManager.default(this, i, false)
    }

    private fun launchDestroyThread() {
        thread {
            if (this.destroyThreadRunning.get()) return@thread

            Logger.info("Started DestroyThread")
            this.destroyThreadRunning.set(true)

            var lowPowerCounter = 0
            var i = 0

            while (i < 60 && networkClient.ping(baseurl)) {
                i++

                try {
                    val obj = networkClient.fetchJSON("http://$baseurl/status")
                    val mainRoller = obj?.rollers?.get(0)

                    val d1double = mainRoller?.power
                    val movement = mainRoller?.state.toString()

                    if (movement == "stop") {
                        Thread.sleep(500)
                        break
                    } else if (d1double != null && d1double < 5.0) {
                        lowPowerCounter++
                        if (lowPowerCounter > 1) {
                            lowPowerCounter = 0
                            networkClient.sendGet("http://$baseurl/roller/0?go=stop")
                        }
                    }
                } catch (e: Exception) {
                    Logger.error(e.toString())
                    continue
                }
                Thread.sleep(1000)
            }
            this.destroyThreadRunning.set(false)
            Logger.info("Killed DestroyThread")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun launchThread() {
        thread {
            if (this.mainThreadRunning.get()) return@thread

            this.mainThreadRunning.set(true)
            var lowPowerCounter = 0
            Logger.info("Started thread")

            while (!this.terminate.get()) {
                val detail1 = findViewById<TextView>(R.id.t_detail1)
                val detail2 = findViewById<TextView>(R.id.t_detail2)

                val up = findViewById<Button>(R.id.t_up)
                val stop = findViewById<Button>(R.id.t_stop)
                val down = findViewById<Button>(R.id.t_down)

                if (networkClient.ping(baseurl)) {
                    try {
                        val obj = networkClient.fetchJSON("http://$baseurl/status")
                        val mainRoller = obj?.rollers?.get(0)

                        val d1double = mainRoller?.power.toString().toDouble()
                        val d1 = formatter.format(d1double)
                        val movement = mainRoller?.state.toString()

                        // Necessary to not immediately stop on start
                        // of movement due to bad Thread timing
                        if (d1double < 5.0 && movement != "stop") {
                            lowPowerCounter++
                            if (lowPowerCounter > 1) {
                                lowPowerCounter = 0
                                networkClient.sendGet("http://$baseurl/roller/0?go=stop")
                            }
                        }

                        this.runOnUiThread {
                            detail1.text =
                                "IP: $baseurl"
                            detail2.text =
                                "Power $d1 W"

                            up.isEnabled = movement != "open"
                            stop.isEnabled = movement != "stop"
                            down.isEnabled = movement != "close"
                        }
                    } catch (err: Exception) {
                        Logger.error(err)
                    }
                } else {
                    this.runOnUiThread {
                        detail1.text = "IP: $baseurl"
                        detail2.text = "keine Verbindung"
                        up.isEnabled = false
                        stop.isEnabled = false
                        down.isEnabled = false
                    }
                }
                Thread.sleep(1000)
            }
            this.mainThreadRunning.set(false)
            Logger.info(
                "Killed thread"
            )
        }
    }

    fun buttonPressed(view: View) {
        when (view.id) {
            R.id.t_up -> {
                networkClient.sendGet("http://$baseurl/roller/0?go=open")
            }
            R.id.t_stop -> {
                networkClient.sendGet("http://$baseurl/roller/0?go=stop")
            }
            R.id.t_down -> {
                networkClient.sendGet("http://$baseurl/roller/0?go=close")
            }
        }
    }
}
