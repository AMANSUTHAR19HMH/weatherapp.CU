package com.amn.weatherappaman


import android.os.Bundle
import android.view.WindowManager
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.amn.weatherappaman.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(binding.root)
        fetchWeatherData("hanumangarh")
        SearchCity()
    }

    private fun SearchCity() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
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
            }

            override fun onFailure(call: Call<weatherapp>, t: Throwable) {
                // Handle failure
            }
        })


    }

    private fun changeImageAccordingtoWeather(conditions: String) {
        when (conditions) {


            "Heavy Rain" -> {
                binding.root.setBackgroundResource(R.drawable.heavyrain)
                binding.lottieAnimationView.setAnimation(R.raw.rainningani)
            }

            "Light Rain" -> {
                binding.root.setBackgroundResource(R.drawable.lightrain)
                binding.lottieAnimationView.setAnimation(R.raw.rainningani)
            }

            "Smoke" -> {
                binding.root.setBackgroundResource(R.drawable.heavyrain)
                binding.lottieAnimationView.setAnimation(R.raw.rainningani)
            }

            "Drizzle" -> {
                binding.root.setBackgroundResource(R.drawable.drizzleback)
                binding.lottieAnimationView.setAnimation(R.raw.rainningani)
            }

            "Moderate Rain" -> {
                binding.root.setBackgroundResource(R.drawable.raining_background)
                binding.lottieAnimationView.setAnimation(R.raw.rainningani)
            }

            "Showers" -> {
                binding.root.setBackgroundResource(R.drawable.raining_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }

            "Light Snow" -> {
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }

            "Moderate Snow" -> {
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }

            "Heavy Snow" -> {
                binding.root.setBackgroundResource(R.drawable.heavysbowback)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }

            "Blizzard" -> {
                binding.root.setBackgroundResource(R.drawable.blizzerdback)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }

            "Clear" -> {
                binding.root.setBackgroundResource(R.drawable.clearback)
                binding.lottieAnimationView.setAnimation(R.raw.sunn)
            }

            "Sunny" -> {
                binding.root.setBackgroundResource(R.drawable.sunnybackgorund)
                binding.lottieAnimationView.setAnimation(R.raw.sunn)
            }

            "Cloudy" -> {
                binding.root.setBackgroundResource(R.drawable.cloudyback)
                binding.lottieAnimationView.setAnimation(R.raw.cloudyhaze)
            }

            "Mist" -> {
                binding.root.setBackgroundResource(R.drawable.mistback)
                binding.lottieAnimationView.setAnimation(R.raw.cloudyhaze)
            }

            "Clouds" -> {
                binding.root.setBackgroundResource(R.drawable.cloudy_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloudyhaze)
            }

            "Haze" -> {
                binding.root.setBackgroundResource(R.drawable.hazeback)
                binding.lottieAnimationView.setAnimation(R.raw.animationhaze)
            }

            else -> {
                binding.root.setBackgroundResource(R.drawable.sunnybackgorund)
                binding.lottieAnimationView.setAnimation(R.raw.sunn)
            }

        }
        binding.lottieAnimationView.playAnimation()
    }

    private fun date(): String {
        val sdf = SimpleDateFormat("DD MMMM yyyy", Locale.getDefault())
        return sdf.format((Date()))
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


