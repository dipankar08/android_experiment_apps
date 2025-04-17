package com.grodok.tflsimple

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import okhttp3.*
import org.json.JSONArray
import java.io.IOException
import java.util.concurrent.TimeUnit

class MainActivity : FragmentActivity() {

    private val stopId = "490007959N"
    private val appId = "YOUR_APP_ID"
    private val appKey = "YOUR_APP_KEY"

    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val fullList = mutableListOf<String>()
    private val currentPageList = mutableListOf<String>()

    private val httpClient = OkHttpClient()
    private val handler = Handler(Looper.getMainLooper())

    private val refreshIntervalMs = TimeUnit.SECONDS.toMillis(30)
    private val pageIntervalMs = TimeUnit.SECONDS.toMillis(10)

    private var currentPage = 0

    private val refreshRunnable = object : Runnable {
        override fun run() {
            fetchBusData()
            handler.postDelayed(this, refreshIntervalMs)
        }
    }

    private val pagingRunnable = object : Runnable {
        override fun run() {
            showNextPage()
            handler.postDelayed(this, pageIntervalMs)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listView = ListView(this)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, currentPageList)
        listView.adapter = adapter

        setContentView(listView)

        // Start auto-refresh and paging
        refreshRunnable.run()
        pagingRunnable.run()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(refreshRunnable)
        handler.removeCallbacks(pagingRunnable)
    }

    private fun fetchBusData() {
        val url = "https://api.tfl.gov.uk/StopPoint/$stopId/Arrivals"
        val request = Request.Builder().url(url).build()

        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { body ->
                    try {
                        val arrivals = JSONArray(body)
                        val updatedList = mutableListOf<String>()

                        for (i in 0 until arrivals.length()) {
                            val obj = arrivals.getJSONObject(i)
                            val line = obj.getString("lineName")
                            val dest = obj.getString("destinationName")
                            val timeToStation = obj.getInt("timeToStation") / 60
                            updatedList.add("$timeToStation m: $line to $dest")
                        }

                        updatedList.sortBy { it }

                        runOnUiThread {
                            fullList.clear()
                            fullList.addAll(updatedList)
                            currentPage = 0
                            showNextPage()
                        }
                    }catch (e:Exception){
                        //pass
                    }
                }
            }
        })
    }

    private fun showNextPage() {
        val pageSize = 5
        if (fullList.isEmpty()) return

        val totalPages = (fullList.size + pageSize - 1) / pageSize
        val startIndex = currentPage * pageSize
        val endIndex = minOf(startIndex + pageSize, fullList.size)

        currentPageList.clear()
        currentPageList.addAll(fullList.subList(startIndex, endIndex))
        adapter.notifyDataSetChanged()

        currentPage = (currentPage + 1) % totalPages
    }
}

