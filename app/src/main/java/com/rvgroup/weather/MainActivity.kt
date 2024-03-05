package com.rvgroup.weather

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    val City: String = "India"
    val API: String = "7e3a075e0feaa3fedeb613906b52ad62"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        weatherTask().execute()

    }
    inner class weatherTask(): AsyncTask<String, Void, String>(){
        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.prog_b).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
            findViewById<TextView>(R.id.text_error).visibility = View.GONE

        }

        override fun doInBackground(vararg p0: String?): String? {
            var response:String?
            try {
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$City&units=metric&appid=$API")
//                response = URL("https://api.openweathermap.org/data/2.5/weather?q=City&units=metric&appid=$API")
//                response = URL("https://api.openweathermap.org/data/3.0/weather?q=$City&units=metric&appid=$API")
                    .readText(Charsets.UTF_8)
            }
            catch (e :Exception)
            {
                response = null
            }
            return response

        }
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val updateAt: Long = jsonObj.getLong("dt")
                val updateAtText =
                    "Updated at: " + SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
                        Date(updateAt * 1000)
                    )
                val temp = main.getString("temp")+"C"
                val tempMin = "Min Temp: " + main.getString("temp_min") + "C"
                val tempMax = "Max Temp: " + main.getString("temp_max") + "C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")
                val sunrise: Long = sys.getLong("sunrise")
                val sunset: Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescription = weather.getString("description")
                val address = jsonObj.getString("name") + ", " + sys.getString("country")

                findViewById<TextView>(R.id.address).text = address
                findViewById<TextView>(R.id.update_at).text = updateAtText
                findViewById<TextView>(R.id.status).text = weatherDescription.capitalize()
                findViewById<TextView>(R.id.temp).text = temp
                findViewById<TextView>(R.id.min_temp).text = tempMin
                findViewById<TextView>(R.id.max_temp).text = tempMax
                findViewById<TextView>(R.id.sunrise_time).text =
                    SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise * 1000))
                findViewById<TextView>(R.id.sunset_time).text =
                    SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset * 1000))

                findViewById<TextView>(R.id.wind_time).text = windSpeed
                findViewById<TextView>(R.id.pressure_time).text = pressure
                findViewById<TextView>(R.id.humidity_time).text = humidity

                findViewById<ProgressBar>(R.id.prog_b).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE

            }
            catch (e : Exception)
            {
               findViewById<ProgressBar>(R.id.prog_b).visibility = View.GONE
                findViewById<TextView>(R.id.text_error).visibility = View.VISIBLE
            }
        }

    }
}