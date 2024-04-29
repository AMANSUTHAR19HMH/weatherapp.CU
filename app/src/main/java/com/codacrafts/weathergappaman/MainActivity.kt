package com.codacrafts.weathergappaman


import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.codacrafts.weathergappaman.databinding.ActivityMainBinding


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        fetchWeatherData("Delhi")
        SearchCity()
        val calnederbt: Button = findViewById(R.id.calanderbtn)
        calnederbt.setOnClickListener{
            val intent = Intent(this , calneder::class.java)
            startActivity(intent)
        }


       
    }

    fun SearchCity() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                else {
                    Toast.makeText(
                        applicationContext,
                        "Invalid city: $query",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun kelvinToCelsius(kelvin: Double): Double {
        return kelvin - 273.15
    }

    private fun fetchWeatherData(location: String) {
        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/").build()
            .create(ApiInterface::class.java)
        val response =
            retrofit.getweatherData(location, "2256af0cbd5e9207e761d4139c2fd180", "matric")
        response.enqueue(object : Callback<weatherapp> {
            override fun onResponse(call: Call<weatherapp>, response: Response<weatherapp>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    val temperatureKelvin = responseBody.main.temp
                    val temperatureCelsius = kelvinToCelsius(temperatureKelvin)
                    val humidity = responseBody.main.humidity
                    val windSpeed = responseBody.wind.speed
                    val sunRise = responseBody.sys.sunrise.toLong()
                    val sunSet = responseBody.sys.sunset.toLong()
                    val seaLevel = responseBody.main.pressure
                    val condition = responseBody.weather.firstOrNull()?.main ?: "unknown"
                    val maxTemp = responseBody.main.temp_max
                    val minTemp = responseBody.main.temp_min

                    binding.temoprature.text = "${temperatureCelsius.roundToInt()} °C"
                    binding.weather.text = condition
                    binding.maxTemp.text = "Max Temp: ${kelvinToCelsius(maxTemp).roundToInt()} °C"
                    binding.minTemp.text = "Min Temp: ${kelvinToCelsius(minTemp).roundToInt()} °C"
                    binding.humidity.text = "$humidity %"
                    binding.windspeed.text = "$windSpeed m/s"
                    binding.sunrise.text = "${time(sunRise)}"
                    binding.sunset.text = "${time(sunSet)}"
                    binding.sealevel.text = "$seaLevel hPa"
                    binding.condition.text = condition
                    binding.day.text = dayname(System.currentTimeMillis())
                    binding.date.text = date()
                    binding.location.text = "$location"
                    changeImageAccordingtoWeather(condition)
                }
                else {
                    // Handle invalid city search
                    Toast.makeText(this@MainActivity, "Invalid city or location", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<weatherapp>, t: Throwable) {
                // Handle failure
            Toast.makeText(this@MainActivity, "Failed to fetch weather data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun changeImageAccordingtoWeather(conditions: String) {
        when (conditions) {


            "Heavy Rain" -> {
                binding.root.setBackgroundResource(R.drawable.heavyrain)
                binding.lottieAnimationView.setAnimation(R.raw.rainningani)
                binding.date.setTextColor(ContextCompat.getColor(this, R.color.black))
            }

            "Light Rain" -> {
                binding.root.setBackgroundResource(R.drawable.lightrain)
                binding.lottieAnimationView.setAnimation(R.raw.rainningani)
                binding.date.setTextColor(ContextCompat.getColor(this, R.color.black))
            }

            "Smoke" -> {
//                binding.root.setBackgroundResource(R.drawable.heavyrain)
                binding.root.setBackgroundResource(R.drawable.cloudy_background)
                binding.lottieAnimationView.setAnimation(R.raw.rainningani)
                binding.date.setTextColor(ContextCompat.getColor(this, R.color.black))
            }

            "Drizzle" -> {
                binding.root.setBackgroundResource(R.drawable.drizzleback)
                binding.lottieAnimationView.setAnimation(R.raw.rainningani)
                binding.date.setTextColor(ContextCompat.getColor(this, R.color.black))
            }

            "Moderate Rain" -> {
                binding.root.setBackgroundResource(R.drawable.raining_background)
                binding.lottieAnimationView.setAnimation(R.raw.rainningani)
                binding.date.setTextColor(ContextCompat.getColor(this, R.color.black))
            }

            "Showers" -> {
                binding.root.setBackgroundResource(R.drawable.raining_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
                binding.date.setTextColor(ContextCompat.getColor(this, R.color.black))
            }

            "Light Snow" -> {
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
                binding.date.setTextColor(ContextCompat.getColor(this, R.color.black))
            }

            "Moderate Snow" -> {
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
                binding.date.setTextColor(ContextCompat.getColor(this, R.color.black))
            }

            "Heavy Snow" -> {
                binding.root.setBackgroundResource(R.drawable.heavysbowback)
                binding.date.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }

            "Blizzard" -> {
                binding.root.setBackgroundResource(R.drawable.blizzerdback)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }

            "Clear" -> {
                binding.root.setBackgroundResource(R.drawable.clearback)
                binding.lottieAnimationView.setAnimation(R.raw.sunn)
                binding.date.setTextColor(ContextCompat.getColor(this, R.color.black))
            }

            "Sunny" -> {
                binding.root.setBackgroundResource(R.drawable.sunnybackgorund)
                binding.lottieAnimationView.setAnimation(R.raw.sunn)
                binding.date.setTextColor(ContextCompat.getColor(this, R.color.black))

            }

            "Cloudy" -> {
                binding.root.setBackgroundResource(R.drawable.cloudyback)
                binding.date.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.lottieAnimationView.setAnimation(R.raw.cloudyhaze)
            }

            "Mist" -> {
                binding.root.setBackgroundResource(R.drawable.mistback)
                binding.lottieAnimationView.setAnimation(R.raw.cloudyhaze)
            }

            "Clouds" -> {
                binding.root.setBackgroundResource(R.drawable.cloudy_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloudyhaze)
                binding.date.setTextColor(ContextCompat.getColor(this, R.color.black))
            }

            "Haze" -> {
                binding.root.setBackgroundResource(R.drawable.cloudy_background)
                binding.lottieAnimationView.setAnimation(R.raw.animationhaze)
                binding.date.setTextColor(ContextCompat.getColor(this, R.color.black))
            }

            else -> {
                binding.root.setBackgroundResource(R.drawable.sunnybackgorund)
                binding.lottieAnimationView.setAnimation(R.raw.sunn)
                binding.date.setTextColor(ContextCompat.getColor(this, R.color.black))
            }

        }
        binding.lottieAnimationView.playAnimation()
    }

 /* private fun date(): String {
          val currentDate = Date()
          val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
          val formattedDate = sdf.format(currentDate)

          val dayOfYear = Calendar.getInstance().apply {
              time = currentDate
          }.get(Calendar.DAY_OF_YEAR)

          val dayOfYearText = "Day $dayOfYear"

          // Find the TextView
          val dayOfYearTextView = findViewById<TextView>(R.id.dayOfYearTextView)
          // Set the text
          dayOfYearTextView.text = dayOfYearText

          return formattedDate
      }*/


    private fun date(): String {
        val currentDate = Date()
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val formattedDate = sdf.format(currentDate)

        val dayOfYear = Calendar.getInstance().apply {
            time = currentDate
        }.get(Calendar.DAY_OF_YEAR)

        return "$formattedDate, Day $dayOfYear"
    }

    private fun time(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format((Date(timestamp * 1000)))
    }

    fun dayname(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))
    }
}




/*




package com.amn.weatherappaman

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.WindowManager
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.amn.weatherappaman.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        } else {
            getLocation()
        }

        fetchWeatherData(28.7041, 77.1025)
        SearchCity()

        binding.calanderbtn.setOnClickListener {
            val intent = Intent(this, calanderActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener(this, OnSuccessListener<Location> { location ->
                location?.let {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    fetchWeatherData(latitude, longitude) // Pass both latitude and longitude
                }
            })
    }
    private fun fetchWeatherData(latitude: Double, longitude: Double) {
        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/").build()
            .create(ApiInterface::class.java)
        val response =
            retrofit.getweatherData("$latitude,$longitude", "2256af0cbd5e9207e761d4139c2fd180", "metric")
        response.enqueue(object : Callback<weatherapp> {
            override fun onResponse(call: Call<weatherapp>, response: Response<weatherapp>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    val temperatureKelvin = responseBody.main.temp
                    val temperatureCelsius = kelvinToCelsius(temperatureKelvin)
                    val humidity = responseBody.main.humidity
                    val windSpeed = responseBody.wind.speed
                    val sunRise = responseBody.sys.sunrise.toLong()
                    val sunSet = responseBody.sys.sunset.toLong()
                    val seaLevel = responseBody.main.pressure
                    val condition = responseBody.weather.firstOrNull()?.main ?: "unknown"
                    val maxTemp = responseBody.main.temp_max
                    val minTemp = responseBody.main.temp_min

                    binding.temoprature.text = "${temperatureCelsius.roundToInt()} °C"
                    binding.weather.text = condition
                    binding.maxTemp.text = "Max Temp: ${kelvinToCelsius(maxTemp).roundToInt()} °C"
                    binding.minTemp.text = "Min Temp: ${kelvinToCelsius(minTemp).roundToInt()} °C"
                    binding.humidity.text = "$humidity %"
                    binding.windspeed.text = "$windSpeed m/s"
                    binding.sunrise.text = "${time(sunRise)}"
                    binding.sunset.text = "${time(sunSet)}"
                    binding.sealevel.text = "$seaLevel hPa"
                    binding.condition.text = condition
                    binding.day.text = dayname(System.currentTimeMillis())
                    binding.date.text = date()
                    binding.location.text = "$latitude, $longitude"
                    changeImageAccordingtoWeather(condition)
                }
            }

            override fun onFailure(call: Call<weatherapp>, t: Throwable) {
                // Handle failure
            }
        })
    }

    private fun changeImageAccordingtoWeather(conditions: String) {
        // Your implementation here
    }

    private fun kelvinToCelsius(kelvin: Double): Double {
        return kelvin - 273.15
    }

    private fun date(): String {
        val currentDate = Date()
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val formattedDate = sdf.format(currentDate)

        val dayOfYear = Calendar.getInstance().apply {
            time = currentDate
        }.get(Calendar.DAY_OF_YEAR)

        return "$formattedDate, Day $dayOfYear"
    }

    private fun time(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format((Date(timestamp * 1000)))
    }

    fun dayname(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))
    }

    private fun SearchCity() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                   */
/* fetchWeatherData(query)*//*

                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            }
        }
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 100
    }
}








*/


