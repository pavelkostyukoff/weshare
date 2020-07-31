package com.spacesofting.weshare.mvp.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.digitalhorizon.eve.ui.widget.IOSSheetDialog
import com.google.android.gms.location.LocationListener
import com.pawegio.kandroid.toast
import com.pawegio.kandroid.visible
import com.spacesofting.weshare.R
import com.spacesofting.weshare.api.Api
import com.spacesofting.weshare.api.Entity
import com.spacesofting.weshare.api.Entitys
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.FragmentWrapper
import com.spacesofting.weshare.mvp.RentItem
import com.spacesofting.weshare.mvp.presentation.presenter.MapPresenter
import com.spacesofting.weshare.mvp.presentation.view.MapViewMaps
import com.spacesofting.weshare.mvp.ui.adapter.CategoryAdapter
import com.spacesofting.weshare.utils.applySchedulers
import com.tbruyelle.rxpermissions2.RxPermissions
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.runtime.image.ImageProvider
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.fragment_map.categoryCycleView
import kotlinx.android.synthetic.main.fragment_map.custom_caption
import kotlinx.android.synthetic.main.fragment_map.custom_sub_category
import kotlinx.android.synthetic.main.fragment_map.subCategoryCycleView
import moxy.presenter.InjectPresenter
import org.imaginativeworld.whynotimagecarousel.CarouselItem
import org.imaginativeworld.whynotimagecarousel.CarouselOnScrollListener
import java.util.concurrent.TimeUnit


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
    private val category = ApplicationWrapper.category
    private var latMain = String()

    private var lngMain = String()
    private val MAPKIT_API_KEY = "42e20f72-1a03-4a0d-9a60-155947e01546"
    private val TARGET_LOCATION: Point? =
        Point(59.945933, 30.320045)

    val ITEMS_PER_PAGE = 15
    var page = 0
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
        initAdapterCategory(category)
        initListItems()
        initCategoryList()


        filter.setOnClickListener {
            openFilter()
        }



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
    //filter
    private fun openFilter() {
        filterLayout.visible = !filterLayout.visible
        val cs = ConstraintSet()
        val targetVisibility = if (filterLayout.visible) View.GONE else View.VISIBLE
        val colorId = if (filterLayout.visible) R.color.perano else R.color.white
        cs.clone(rootLayout)
        cs.setVisibility(R.id.filterLayout, targetVisibility)
        TransitionManager.beginDelayedTransition(rootLayout, AutoTransition().setDuration(500))
        cs.applyTo(rootLayout)
        context?.let { ContextCompat.getColor(it, colorId) }?.let { filter.setColorFilter(it) }
        /*  if (!filterLayout.visible) {
              //todo запрашивает фильтра с сервера и утсанавливает через презентер
              setFilters()
          }
          //todo устанавливает число выбранных фильтров
          setIndicatorView()
          //todo сохранение состояния фильров и их порядок
          context?.let { Settings.SaveFilterListState(FILTER_LIST, getFiltersState, it) }
          context?.let { Settings.SaveFilterListPriority(FILTER_LIST_PRIORITY, getFiltersPriority, it) }*/
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
            showDialogMore()
            return true
        }
    }

    private fun showDialogMore(/*guestCard: GuestCard*/) {
        context?.let {
            val dialog = IOSSheetDialog(it)
                .setTitle(R.string.dialog_chose_action)
                .setOnSelectedListener(object: IOSSheetDialog.OnSelectedItemListener{
                    override fun onSelectedItem(btnName: String) {
                        when(btnName){
                            getString(R.string.escalate) -> {
                               // presenter.sendForApprovalGuestCard(guestCard)
                            }
                            getString(R.string.change_request) -> {
                               // showChangeRequestDialog(guestCard)
                            }
                            getString(R.string.edit) -> {
                              //  router.navigateTo(ScreenPool.EDIT_GUEST_CARD_FRAGMENT, guestCard)
                            }
                            getString(R.string.remove) -> {
                              //  presenter.deleteGuestCard(guestCard)
                            }
                        }
                    }
                })

            /*if (guestCard.guestCardOperations.isEscalate){
                dialog.addButton(R.string.escalate)
            }

            if (guestCard.guestCardOperations.isChangeRequest){
                dialog.addButton(R.string.change_request)
            }

            if (guestCard.guestCardOperations.isEdit){
                dialog.addButton(R.string.edit)
            }

            if (guestCard.guestCardOperations.isDelete){
                dialog.addButton(R.string.remove, R.color.red_orange)
            }*/

            dialog.show()
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

/*
        category.adapter = catAdapter

        category.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(
                activity,
                androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
                false
            )
*/
    }
    private fun initAdapterCategory(it: Entitys?) {
        var resourceId: Int? = null
        // if (it?.entities == null) {
        it?.entities?.map {
            when (it.code) {
                "kids" -> {
                    resourceId = R.drawable.kids
                }
                "realty" -> {
                    resourceId = R.drawable.nedviga
                }
                "equipment" -> {
                    resourceId = R.drawable.oborudovanie_stroyka
                }
                "clothes" -> {
                    resourceId = R.drawable.clouse
                }
                "relaxation" -> {
                    resourceId = R.drawable.rest_otdih
                }
                "other" -> {
                    resourceId = R.drawable.sports
                }
                "transport" -> {
                    resourceId = R.drawable.transport
                }
                "hobby" -> {
                    resourceId = R.drawable.sports
                }
                "electronics" -> {
                    resourceId = R.drawable.electronix
                }
                else -> resourceId = R.drawable.wish_default_img
            }
            val uri: Uri =
                Uri.parse("android.resource://" + activity?.packageName.toString() + "/" + resourceId)
            it.categoryImg = uri.toString()
        }
    }
    private fun initCategoryList() {
        val listFour = mutableListOf<CarouselItem>()
        categoryCycleView.captionTextSize = 0
        var pos = 0
        var count = 0
        category?.entities?.map {
            count++
           // if (it.id == advert.categoryId) {
                pos = count
          //  }

            it.categoryImg?.let { url ->
                CarouselItem(
                    imageUrl = url,
                    caption = it.name
                )
            }?.let { item ->
                listFour.add(
                    item
                )
                categoryCycleView.addData(listFour)
            }
        }

        categoryCycleView.onScrollListener = object : CarouselOnScrollListener {
            var positionNew = pos
            @SuppressLint("CheckResult")
            override fun onScrollStateChanged(
                recyclerView: RecyclerView,
                newState: Int,
                position: Int,
                carouselItem: CarouselItem?
            ) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE/* && positionNew != position*/) {
                    carouselItem?.apply {
                        custom_caption.text = caption
                    }
                    //todo идем в презентер что бы он сделал нам новую выгрузку если есть подкатегории
                    Single.just(position)
                        .delay(1000, TimeUnit.MILLISECONDS)
                        .applySchedulers()
                        .subscribe({
                            getSubCategory(category?.entities?.get(position))
                            //todo вот тут мы и будем делать запрос - получать шмотки и обновлять карту
                            val test = category?.entities?.get(position)?.name
                        }, {
                            it
                            //  loadingViewModel.errorMessage = it.nonNullMessage()
                            // loadingViewModel.isError = true
                        })

                }
                positionNew = position
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                // todo меняем категорию - получаем номер саб категориий делаем запрос на сервер через призентер
                // todo презентер отображает initSabCategoryList ()
            }
        }
        //  categoryCycleView.setIndicator(custom_indicator)
        categoryCycleView.currentPosition = 3

    }

    //todo 2--------------------2
    private fun initSubCategoryList(entitys: Entitys?) {
        var position = 0
        var count = 0
        val listFour = mutableListOf<CarouselItem>()

        subCategoryCycleView.captionTextSize = 0
        entitys?.entities?.map {
            count++
           // if (it.id == advert.categoryId) {
                position = count
          //  }
            it.categoryImg?.let { it1 ->
                CarouselItem(
                    imageUrl = it1,
                    caption = it.name
                )
            }?.let { it2 -> listFour.add(it2) }
        }
        subCategoryCycleView.currentPosition = 0

        subCategoryCycleView.onScrollListener = object : CarouselOnScrollListener {
            var positionNew = position

            override fun onScrollStateChanged(
                recyclerView: RecyclerView,
                newState: Int,
                position: Int,
                carouselItem: CarouselItem?
            ) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    carouselItem?.apply {
                        custom_sub_category.text = caption
                    }
                    //todo идем в презентер что бы он сделал нам новую выгрузку если есть подкатегории
                    if (entitys?.entities != null) {
                        if (entitys.entities!!.isNotEmpty()) {
                            //todo вот тут мы и будем делать запрос - получать шмотки и обновлять карту
                           // advert.categoryId = entitys.entities!![position].id
                            val test = entitys.entities!![position].name
                        }
                    }
                }
                positionNew = position
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                // todo меняем категорию - получаем номер саб категориий делаем запрос на сервер через призентер
                // todo презентер отображает initSabCategoryList ()
            }
        }

        subCategoryCycleView.addData(listFour)
        subCategoryCycleView.scrollTo(0, 0)
        if (entitys?.entities?.isEmpty()!!) {
            subCategoryCycleView.visibility = View.GONE
            custom_sub_category.visibility = View.GONE
        } else {
            subCategoryCycleView.visibility = View.GONE
            custom_sub_category.visibility = View.GONE
            subCategoryCycleView.visibility = View.VISIBLE
            custom_sub_category.visibility = View.VISIBLE
            // subCategoryCycleView.setIndicator(custom_indicator)
        }
    }

    fun getSubCategory(ent: Entity?) {
        with(Api) {
            Tags.getCategories(ent?.id, ITEMS_PER_PAGE, page * ITEMS_PER_PAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.entities?.isNotEmpty()!!)
                    {
                        val arr = java.util.ArrayList<Entity>()
                        ent?.name = "Все"
                        ent?.let { it1 -> arr.add(it1) }
                        it.entities
                        it.entities?.let { it1 -> arr.addAll(it1) }
                        it.entities = arr
                    }
                    setNewSubCategory(it)

                }) {
                  //  showToast(R.string.error_load_ok_delete)
                }
        }
    }
     fun setNewSubCategory(it: Entitys) {
        //
        val test = it
        Single.just(initAdapterCategory(it))
            .applySchedulers()
            .subscribe({
                initSubCategoryList(test)
            }, {
                it
                //  loadingViewModel.errorMessage = it.nonNullMessage()
                // loadingViewModel.isError = true
            })
    }
}
