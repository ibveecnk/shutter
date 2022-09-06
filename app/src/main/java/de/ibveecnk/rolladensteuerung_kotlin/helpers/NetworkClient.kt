package de.ibveecnk.rolladensteuerung_kotlin.helpers

import com.google.gson.Gson
import com.google.gson.JsonObject
import de.ibveecnk.rolladensteuerung_kotlin.models.RollerResponse
import de.ibveecnk.rolladensteuerung_kotlin.models.Shelly
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.InetAddress
import java.net.URL
import kotlin.concurrent.thread


class NetworkClient {
    private fun getJSONString(endpoint: String): String {
        return try {
            val url = URL(endpoint)
            val response: String
            with(url.openConnection() as HttpURLConnection) {
                response = inputStream.bufferedReader().use(BufferedReader::readText)
            }
            response
        } catch (ex: Exception) {
            Logger.error(ex.toString())
            ""
        }


    }

    private fun jsonStringToJSONObject(jsonString: String): RollerResponse? {
        return try {
            val result = Gson().fromJson(jsonString, RollerResponse::class.java)
            result
        } catch (ex: Exception) {
            Logger.error(ex)
            null
        }
    }

    fun fetchJSON(endpoint: String): RollerResponse? {
        return jsonStringToJSONObject(getJSONString(endpoint))
    }


    fun ping(ip: String): Boolean {
        Logger.info("Pinging $ip")
        return try {
            val url = URL("http://$ip/shelly")
            val response: String;
            with(url.openConnection() as HttpURLConnection) {
                connectTimeout = 500
                readTimeout = 500
                response = inputStream.bufferedReader().use(BufferedReader::readText)
                disconnect()
            }
            return response != "";
        } catch (ex: java.io.IOException) {
            // Sensible to ignore
            false
        } catch (ex: Exception) {
            Logger.error(ex)
            false
        }
    }

    fun sendGet(endpoint: String) {
        thread {
            try {
                val url = URL(endpoint)
                val connection = url.openConnection() as HttpURLConnection
                connection.responseMessage
            } catch (ex: Exception) {
                Logger.error(ex)
                return@thread
            }
        }
    }
}
