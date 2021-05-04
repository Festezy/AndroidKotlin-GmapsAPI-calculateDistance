package com.ariqandrean.myMapsApps2

import android.annotation.SuppressLint
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.layout_findlocation.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var currentLocation : LatLng
    private lateinit var currentAddress: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("SetTextI18n")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        var locationList = mutableMapOf<String, Double>("Llatitude1" to 0.0, "Llongtitude1" to 0.0,
                "Llatitude2" to 0.0, "Llongtitude2" to 0.0)
        var addressList = mutableMapOf<String, String>("address1" to "", "address2" to "")

        // Add a marker in Sydney and move the camera
        val blk = LatLng(-6.234532061960018, 106.99041027874719)
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(blk, 15f))

        mMap.setOnCameraIdleListener {
            currentLocation = mMap.cameraPosition.target
            val geocoder = Geocoder(this)
            mMap.clear()
            var geocoderResult = geocoder.getFromLocation(currentLocation.latitude, currentLocation.longitude, 1)
            currentAddress = geocoderResult[0].getAddressLine(0)
//            geocoder.getFromLocation(currentLocation.latitude, currentLocation.longitude, 1)
            mMap.addMarker(MarkerOptions().position(currentLocation).title("position"))
        }
        btnSet.setOnClickListener {
            if (locationList["Llatitude1"] == 0.0){
                locationList.put("Llatitude1", currentLocation.latitude)
                locationList["Llongtitude1"] = currentLocation.longitude
                addressList["address1"] = currentAddress
            } else{
                locationList.put("Llatitude2", currentLocation.latitude)
                locationList["Llongtitude2"] = currentLocation.longitude
                addressList["address2"] = currentAddress
            }
            tvLocation1.text = "${locationList.get("Llatitude1")} - ${locationList.get("Llongtitude1")}"
            tvLocation2.text = "${locationList["Llatitude2"]} - ${locationList["Llongtitude2"]}"
            tvLocation1.text = addressList.get("address1").toString()
            tvLocation2.text = addressList["address2"].toString()

        }
        btnHitungJarak.setOnClickListener {
            val location1 = LatLng(locationList.get("Llatitude1")!!, locationList["Llongtitude1"]!!)
            val locationsProv1 = Location("")
            locationsProv1.latitude = locationList.get("Llatitude1")!!
            locationsProv1.longitude = locationList["Llongtitude1"]!!

            val location2 = LatLng(locationList.get("Llatitude2")!!, locationList["Llongtitude2"]!!)
            val locationsProv2 = Location("")
            locationsProv2.latitude = locationList.get("Llatitude2")!!
            locationsProv2.longitude = locationList["Llongtitude2"]!!


            mMap.addMarker(MarkerOptions().position(location1).title("Position 1"))
            mMap.addMarker(MarkerOptions().position(location2).title("Position 2"))

            mMap.addPolyline(PolylineOptions().clickable(true)
                    .add(location1, location2).color(R.color.teal_200).width(16f))
            val loc1ToLoc2 = locationsProv1.distanceTo(locationsProv2)
            tvJarakHitungLokasi.text = "${loc1ToLoc2 / 1000} KM"
        }
        btnReset.setOnClickListener {
            locationList.putAll(setOf("Llatitude1" to 0.0, "Llongtitude1" to 0.0,
                    "Llatitude2" to 0.0, "Llongtitude2" to 0.0))
            tvLocation1.text = ""
            tvLocation2.text = ""
            mMap.clear()
        }

    }
}