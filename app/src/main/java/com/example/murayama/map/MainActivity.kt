package com.example.murayama.map

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.TextView
import com.esri.arcgisruntime.geometry.GeometryEngine
import com.esri.arcgisruntime.geometry.Point
import com.esri.arcgisruntime.geometry.SpatialReference
import com.esri.arcgisruntime.geometry.SpatialReferences
import com.esri.arcgisruntime.geometry.SpatialReferences.getWgs84

import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.Basemap
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener
import com.esri.arcgisruntime.mapping.view.MapView


class MainActivity : AppCompatActivity() {

    private var mMapView : MapView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // MapView の作成
        mMapView = findViewById(R.id.mapView) as MapView

        // Basemap の作成
        val map = ArcGISMap(Basemap.Type.TOPOGRAPHIC, 35.3312442,139.6202471, 8)

        // MapViewへBasemapを追加する
        mMapView!!.map = map

        // タッチイベント
        mMapView!!.setOnTouchListener(object : DefaultMapViewOnTouchListener(this,mMapView) {
            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {

                val screenPoint = android.graphics.Point(Math.round(e!!.getX()),Math.round((e!!.getY())))
                val arcgisPoint = mMapView!!.screenToLocation(screenPoint)
                // WGS84(緯度経度)へ変換
                val arcgis84p = GeometryEngine.project(arcgisPoint, SpatialReferences.getWgs84()) as Point

                // 表示するテキストを作成する
                val calloutText = TextView(applicationContext)
                calloutText.setText("Lat:" + String.format("%.4f",arcgis84p.getY()) + "Lon" + String.format("%.4f", arcgis84p.getX()))
                calloutText.setTextColor(Color.BLACK)

                // 表示オブジェクトに設定する
                val callout = mMapView!!.callout
                callout.location = arcgisPoint
                callout.content = calloutText
                callout.show()
                return true
            }
        })
    }

    override fun onPause() {
        mMapView!!.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        mMapView!!.resume()
    }
}