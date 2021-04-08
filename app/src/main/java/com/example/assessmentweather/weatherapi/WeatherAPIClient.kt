package com.example.assessmentweather.weatherapi

import com.example.assessmentweather.models.CityDailyResponse
import com.example.assessmentweather.models.ForecastResponse
import com.example.assessmentweather.models.WeatherResponse
import com.example.assessmentweather.utils.Constants
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class WeatherAPIClient {

    private val api = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(getOkHttpClient())
        .build()
        .create(WeatherAPI::class.java)

    fun getDataFromGps(
        latitude: String,
        longitude: String,
        units: String
    ): Single<WeatherResponse> {
        return api.getWeatherByGPS(latitude, longitude, units)
    }

    fun getForecastFromGps(
        latitude: String,
        longitude: String,
        units: String
    ): Single<ForecastResponse> {
        return api.getForecastByGPS(latitude, longitude, units)
    }

    fun getCityDailyWeatherFromGps(
        latitude: String,
        longitude: String,
        cnt: String,
        units: String
    ): Single<CityDailyResponse> {
        return api.getCityDailyWeatherByGPS(latitude, longitude, cnt, units)
    }

    private fun getOkHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.addInterceptor(RequestInterceptor())
        return client.build()
    }

}