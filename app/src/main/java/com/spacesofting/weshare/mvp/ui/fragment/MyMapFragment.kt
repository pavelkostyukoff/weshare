package com.spacesofting.weshare.mvp.ui.fragment

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import com.google.android.gms.location.LocationListener
import com.pawegio.kandroid.toast
import com.spacesofting.weshare.R
import com.spacesofting.weshare.common.FragmentWrapper
import com.spacesofting.weshare.mvp.RentItem
import com.spacesofting.weshare.mvp.presentation.presenter.MapPresenter
import com.spacesofting.weshare.mvp.presentation.view.MapViewMaps
import com.spacesofting.weshare.mvp.ui.adapter.CategoryAdapter
import com.tbruyelle.rxpermissions2.RxPermissions
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.runtime.image.ImageProvider
import kotlinx.android.synthetic.main.fragment_map.*
import moxy.presenter.InjectPresenter


class MyMapFragment : FragmentWrapper(),
    MapViewMaps, LocationListener {
    override fun onLocationChanged(p0: Location?) {
        //  lng = p0?.longitude.toString()
        //  lat= p0?.latitude.toString()

        val lat = (p0?.latitude)
        val lng = (p0?.longitude)
        latMain = lat.toString()
        lngMain = lng.toString()
    }

    private var latMain = String()
    private var lngMain = String()
    private val MAPKIT_API_KEY = "42e20f72-1a03-4a0d-9a60-155947e01546"
    private val TARGET_LOCATION: Point? =
        Point(59.945933, 30.320045)

    //private val TARGET_LOCATION = Point(59.956, 30.313)
    private val ANIMATED_RECTANGLE_CENTER = Point(59.956, 30.313)
    private val DRAGGABLE_PLACEMARK_CENTER = Point(59.948, 30.323)
    var catAdapter: CategoryAdapter? = null
    private var lat = String()
    private var loe = String()

    // //  private var lat = String
    private var mapObjects: MapObjectCollection? = null


    override fun getFragmentLayout(): Int {
        return R.layout.fragment_map
    }

    companion object {
        const val TAG = "MyMapFragment"

        fun newInstance(): MyMapFragment {
            val fragment: MyMapFragment =
                MyMapFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var mMapPresenter: MapPresenter

    override fun onAttach(context: Context) {
        MapKitFactory.setApiKey(MAPKIT_API_KEY)
        MapKitFactory.initialize(activity)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showToolbar(TOOLBAR_HIDE)
        initListItems()


        activity?.let {
            RxPermissions(it)
                .request(android.Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe {
                    val locationManager =
                        context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
                    try {

                        val location =
                            locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)//GPS_PROVIDER
                        val one = location?.latitude
                        val two = location?.longitude
                        lat = one.toString()
                        loe = two.toString()
                        // Укажите имя activity вместо map.
                        mapview?.map?.move(
                            CameraPosition(Point(one!!, two!!), 14.0f, 0.0f, 0.0f),
                            Animation(Animation.Type.LINEAR, 4f),
                            null
                        )
                    } catch (e: SecurityException) {
                        //  dialogGPS(this.context) // lets the user know there is a problem with the gps
                        e
                    }
                    createMapObjects()
                }
        }
    }

    override fun onStop() {
        super.onStop()
        mapview?.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onStart() {
        super.onStart()
        mapview?.onStart()
        MapKitFactory.getInstance().onStart()
    }

    private fun createMapObjects() {

        mapObjects = mapview.map.mapObjects.addCollection()
        val marks = ArrayList<Point>()
        marks.add(Point(lat.toDouble(), loe.toDouble()))
        marks.add(Point(lat.toDouble(), loe.toDouble() + 0.05))
        marks.add(Point(lat.toDouble(), loe.toDouble() + 0.06))
        marks.add(Point(lat.toDouble(), loe.toDouble() + 0.07))

        for (i in 0 until marks.size) {
            val mark = mapObjects?.addPlacemark(Point(marks[i].latitude, marks[i].longitude))
            mark?.opacity = 2.8f
            mark?.setIcon(ImageProvider.fromResource(activity, R.drawable.tools_instruments))
            mark?.addTapListener(YandexMapObjectTapListener())
        }

        val mark = mapObjects?.addPlacemark(Point(lat.toDouble(), loe.toDouble()))
        mark?.opacity = 2.8f
        mark?.setIcon(ImageProvider.fromResource(activity, R.drawable.transport))
        mark?.isDraggable = false
        mark?.addTapListener(YandexMapObjectTapListener())

        val mark2 = mapObjects?.addPlacemark(Point(lat.toDouble(), loe.toDouble() + 1))
        mark2?.opacity = 2.8f
        mark2?.setIcon(ImageProvider.fromResource(activity, R.drawable.clouse))
        //   mark?.isDraggable = false
        mark2?.addTapListener(YandexMapObjectTapListener())
    }

    private inner class YandexMapObjectTapListener : MapObjectTapListener {
        override fun onMapObjectTap(mapObject: MapObject, point: Point): Boolean {
            toast("something")
            return true
        }
    }

    private fun initListItems() {
        if (catAdapter == null) {
            catAdapter = activity?.let { context?.let { it1 -> CategoryAdapter(mMapPresenter) } }
        }

        val one = RentItem("9", "2", resources.getDrawable(R.drawable.dress, null))
        val one1 = RentItem("12", "2", resources.getDrawable(R.drawable.ic_big_car, null))
        val one2 = RentItem("14", "2", resources.getDrawable(R.drawable.ic_kids, null))

        val filterList = ArrayList<RentItem>()

        filterList.add(one)
        filterList.add(one1)
        filterList.add(one2)
        filterList.add(one2)
        filterList.add(one2)

        catAdapter?.dataset?.addAll(filterList)

        category.adapter = catAdapter

        category.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(
                activity,
                androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
                false
            )

    }
}
