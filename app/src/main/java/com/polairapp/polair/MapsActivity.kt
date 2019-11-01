package com.polairapp.polair

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.heatmaps.Gradient
import com.google.maps.android.heatmaps.HeatmapTileProvider
import kotlinx.android.synthetic.main.activity_maps.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private val ALT_HEATMAP_GRADIENT_COLORS = intArrayOf(
        Color.argb(0, 0, 255, 255), // transparent
        Color.argb(255 / 3 * 2, 0, 255, 255),
        Color.rgb(0, 255, 255),
        Color.rgb(0, 255, 0),
        Color.rgb(255, 255, 0),
        Color.rgb(255, 0, 0)
    )

    val ALT_HEATMAP_GRADIENT_START_POINTS = floatArrayOf(0.0f, 0.10f, 0.20f, 0.30f, 0.40f, 1.0f)

    val ALT_HEATMAP_GRADIENT = Gradient(
        ALT_HEATMAP_GRADIENT_COLORS,
        ALT_HEATMAP_GRADIENT_START_POINTS
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        initButtonsListeners()
    }

    private fun initButtonsListeners() {
        btnWorkshop.setOnClickListener {
            mMap.addMarker(MarkerOptions().apply {
                position(LatLng(4.63819,-74.08623))
                title("Marker in Sydney")
                icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_tools))
                flat(true)
            })
        }
        btnPrediction.setOnClickListener {
            val intent = Intent(this, PredictActivity::class.java)
            startActivity(intent)
            finish()
        }
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
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val sydney = LatLng(4.63819,-74.08623)

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15f))

        //Experimental call - HeatMap
        addHeatMap()
    }

    private fun addHeatMap() {
        val defaultPoints : ArrayList<LatLng> = ArrayList()
        //Points, should be read from a json dataset
        defaultPoints.add(LatLng(4.63811,-74.08610))
        defaultPoints.add(LatLng(4.63611,-74.08410))
        defaultPoints.add(LatLng(4.63511,-74.08910))
        //HeatmapTileProvider must be declared here in order to modify radius
        val mProvider: HeatmapTileProvider = HeatmapTileProvider.Builder().data(defaultPoints).build()
        //Setups
        mProvider.setRadius(150)
        mProvider.setGradient(ALT_HEATMAP_GRADIENT)
        mProvider.setOpacity(0.3)
        val mOverlay : TileOverlay = mMap.addTileOverlay(TileOverlayOptions().tileProvider(mProvider))
        mOverlay.clearTileCache()
    }
}
