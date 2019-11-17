package com.spacesofting.weshare.ui.fragment

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.view.View
import com.spacesofting.weshare.mvp.view.MapViewMaps
import com.spacesofting.weshare.mvp.presentation.MapPresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.android.gms.location.LocationListener
import com.spacesofting.weshare.R
import com.spacesofting.weshare.common.FragmentWrapper
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.runtime.image.ImageProvider
import kotlinx.android.synthetic.main.fragment_map.*
import android.annotation.SuppressLint
import android.location.LocationManager
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import com.pawegio.kandroid.toast
import com.spacesofting.weshare.mvp.RentItem
import com.spacesofting.weshare.ui.adapter.CategoryAdapter
import com.spacesofting.weshare.ui.adapter.FeedAdapter
import com.spacesofting.weshare.ui.adapter.ItemThingRentAdapter
import com.tbruyelle.rxpermissions2.RxPermissions
import com.yandex.mapkit.geometry.*
import com.yandex.mapkit.map.*
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.fragment_irent.*
import java.util.*

class MyMapFragment : FragmentWrapper(), MapViewMaps , LocationListener {
    override fun onLocationChanged(p0: Location?) {
      //  lng = p0?.longitude.toString()
      //  lat= p0?.latitude.toString()

        val lat =  (p0?.latitude)
        val  lng = (p0?.longitude)
        latMain = lat.toString()
        lngMain = lng.toString()
    }
    private var latMain = String()
    private var lngMain = String()
    private val MAPKIT_API_KEY = "42e20f72-1a03-4a0d-9a60-155947e01546"
    private val TARGET_LOCATION = Point(59.956, 30.313)
    private val ANIMATED_RECTANGLE_CENTER = Point(59.956, 30.313)
    private val DRAGGABLE_PLACEMARK_CENTER = Point(59.948, 30.323)
    var catAdapter: CategoryAdapter? = null
    protected lateinit var mTariffsLayoutManager: org.solovyev.android.views.llm.LinearLayoutManager


    var lat = String()
    var loe = String()


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

    override fun onAttach(context: Context?) {
       MapKitFactory.setApiKey(MAPKIT_API_KEY)
        MapKitFactory.initialize(context)
        super.onAttach(context)
    }


    @SuppressLint("ServiceCast", "MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showToolbar(TOOLBAR_HIDE)
        initListItems()

        activity?.let {
            RxPermissions(it)
                .request(android.Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe { granted ->
                    val locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
                    val  location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    val one = location?.latitude
                    val two = location?.longitude

                    lat = one.toString()
                    loe = two.toString()
                    // Укажите имя activity вместо map.
                    mapview?.getMap()?.move(
                        CameraPosition(Point(one!!,two!!), 10.0f, 0.0f, 0.0f),
                        Animation(Animation.Type.LINEAR, 1f),
                        null
                    )
                }
        }
        createMapObjects()
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

        mapObjects = mapview.getMap().getMapObjects().addCollection()
        val marks = ArrayList<Point>()
        marks.add(Point(lat.toDouble(), loe.toDouble()))
        marks.add(Point(lat.toDouble(), loe.toDouble()+1))
        marks.add(Point(lat.toDouble(), loe.toDouble()+2))
        marks.add(Point(lat.toDouble(), loe.toDouble()+3))

        for (i in 0 until marks.size) {
            val mark = mapObjects?.addPlacemark(Point(marks.get(i).latitude, marks.get(i).longitude))
            mark?.opacity = 2.8f
            mark?.setIcon(ImageProvider.fromResource(activity, R.drawable.img1))
            mark?.addTapListener(YandexMapObjectTapListener())
        }

      //  val mark = mapObjects?.addPlacemark(Point(lat.toDouble(), loe.toDouble()))
      //  mark?.opacity = 2.8f
    //    mark?.setIcon(ImageProvider.fromResource(activity, R.drawable.img1))
            //   mark?.isDraggable = false
       // mark?.addTapListener(YandexMapObjectTapListener())

     /*   val mark2 = mapObjects?.addPlacemark(Point(lat.toDouble(), loe.toDouble()+1))
        mark2?.opacity = 2.8f
        mark2?.setIcon(ImageProvider.fromResource(activity, R.drawable.img12))
        //   mark?.isDraggable = false
        mark2?.addTapListener(YandexMapObjectTapListener())*/
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
        /*val imageFile: File?
      imageFile = R.drawable.img12*/

        val one = RentItem("9","2",3)
        val one1 = RentItem("12","2",3)
        val one2 = RentItem("14","2",3)
        val one3 = RentItem("1111","2",3)

        val filterList = ArrayList<RentItem>()

        filterList.add(one)
        filterList.add(one1)
        filterList.add(one2)
        filterList.add(one3)
        filterList.add(one2)
        filterList.add(one3)
        filterList.add(one3)
        filterList.add(one2)
        filterList.add(one3)

        catAdapter?.dataset?.addAll(filterList)


      //  catAdapter = CategoryAdapter(feedPresenter, elementWidth)


        category.adapter = catAdapter
       // mTariffsLayoutManager = org.solovyev.android.views.llm.LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        category.layoutManager =  LinearLayoutManager(activity) //mTariffsLayoutManager //LinearLayoutManager(activity)



    }
}
