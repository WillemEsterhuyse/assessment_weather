package com.example.assessmentweather.fragments

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.assessmentweather.R
import com.example.assessmentweather.databinding.FragmentLocationBinding
import com.example.assessmentweather.utils.Constants
import com.example.assessmentweather.utils.dateConverter
import com.example.assessmentweather.utils.timeConverter
import com.example.assessmentweather.viewmodels.LocationViewModel
import im.delight.android.location.SimpleLocation
import kotlinx.android.synthetic.main.fragment_location.*

class LocationFragment : Fragment() {

    private val REQUEST_CODE = 1

    private lateinit var viewModel: LocationViewModel
    private lateinit var dataBinding: FragmentLocationBinding

    var location: SimpleLocation? = null
    var latitude: String? = null
    var longitude: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_location, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)

        location = SimpleLocation(context)
        if (!location!!.hasLocationEnabled()) {
            SimpleLocation.openSettings(context)
        } else {
            if (ContextCompat.checkSelfPermission(
                    activity!!,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_CODE
                )
            } else {
                location = SimpleLocation(context)
                latitude = String.format("%.6f", location?.latitude)
                longitude = String.format("%.6f", location?.longitude)
                Log.e("Latitude", "" + latitude)
                Log.e("Longitude", "" + longitude)

            }
        }
        viewModel.getWeatherDataWithGPS(latitude!!, longitude!!, Constants.METRIC)

        viewModel.locationData.observe(viewLifecycleOwner, Observer { locationGps ->
            locationGps?.let {
                lytLocation.visibility = View.VISIBLE
                dataBinding.locationGPS = locationGps
                dataBinding.tvTemperature.text = locationGps.main!!.temp.toInt().toString()
                dataBinding.tvDate.text = dateConverter()
                dataBinding.tvSunrise.text = timeConverter((locationGps.sys!!.sunrise).toLong())
                dataBinding.tvSunset.text = timeConverter((locationGps.sys!!.sunset).toLong())
                dataBinding.imgState.setImageResource(resources.getIdentifier("ic_"+locationGps.weather?.get(0)?.icon, "drawable", view.context.packageName))

            }
        })

        viewModel.locationLoading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (it){
                    locationLoading.visibility = View.VISIBLE
                    lytLocation.visibility = View.GONE
                }else{
                    locationLoading.visibility = View.GONE
                }
            }
        })

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                location = SimpleLocation(context)
                latitude = String.format("%.6f", location?.latitude)
                longitude = String.format("%.6f", location?.longitude)
                Log.e("Latitude", "" + latitude)
                Log.e("Longitude", "" + longitude)

                viewModel.getWeatherDataWithGPS(latitude!!, longitude!!, Constants.METRIC)

            } else {
                Toast.makeText(context, "Check Location", Toast.LENGTH_LONG)
                    .show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
