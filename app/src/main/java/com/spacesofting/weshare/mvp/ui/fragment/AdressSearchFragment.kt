package com.spacesofting.weshare.mvp.ui.fragment

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.jakewharton.rxbinding2.widget.RxTextView
import com.spacesofting.weshare.R
import com.spacesofting.weshare.api.model.place.Place
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.FragmentWrapper
import com.spacesofting.weshare.common.ScreenPool
import com.spacesofting.weshare.mvp.presentation.presenter.AddressSearchPresenter
import com.spacesofting.weshare.mvp.presentation.view.AddressSearchView
import com.spacesofting.weshare.mvp.ui.adapter.AddressSearchAdapter
import com.spacesofting.weshare.mvp.ui.adapter.SelectableAdapter
import com.spacesofting.weshare.utils.Consts
import com.spacesofting.weshare.utils.LogUtil
import com.spacesofting.weshare.utils.WebAppInterface
import com.spacesofting.weshare.yandex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_address_search.*
import kotlinx.android.synthetic.main.fragment_address_search.progressBar
import kotlinx.android.synthetic.main.fragment_authorization.*
import moxy.presenter.InjectPresenter
import org.apache.commons.collections4.IterableUtils
import org.apache.commons.collections4.Predicate
import java.io.UnsupportedEncodingException
import java.lang.Exception
import java.net.URLDecoder
import java.util.*
import java.util.concurrent.TimeUnit

open class AddressSearchFragment : FragmentWrapper(),
    AddressSearchView, SelectableAdapter.OnItemClickListener, YandexMapsListener, MapListener,
    LoaderManager.LoaderCallbacks<MutableList<Place>> {
    private var mRecyclerViewScrollListener: RecyclerView.OnScrollListener? = null
    private var mAdapter: AddressSearchAdapter<YandexMapsAddress>? = null
    val MOSCOW_CENTER_LATITUDE = 55.752377
    val MOSCOW_CENTER_LONGITUDE = 37.619588
    val ADDRESSES = "addresses"
    val ADDRESS = "address"
    private var requestCode = 0
    val ORDER_PARAM = "orderparam"
    val ORDER_ID_PARAM = "orderidparam"
    val REQUEST_CODE = "request_code"
    val ADDRESS_FROM = "address_from"
    val ADDRESS_TO = "address_to"
    val TIME = "time"
    val PAYMENT = "payment"
    val CHILD_SEAT = "child_seat"
    val COMMENTS = "description"
    private var mWebAppInterface: WebAppInterface? = null

    // списки вокзалов
    private val metroStations: List<Place>? = null
    private val airports: List<Place>? = null
    private val trainStations: List<Place>? = null
    private val predefined: List<Place>? = null

    val MOSCOW_CITY_NAME = "Россия, Москва"
    private val AIRPORT = "аэропорт"
    private val METRO = "метро"
    private val MOSCOW_AREA_NAME = "Россия, Московская область"
    private val mWebViewClient: WebViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            LogUtil.Log(url)
            if (url.startsWith(Consts.MAP_INIT_SCHEME)) {
                positionMapToCity(MOSCOW_CITY_NAME)
                return true
            } else if (url.startsWith(Consts.SEARCH_SCHEME)) {
                onSearchScheme(url)
                return true
            } else if (url.startsWith(Consts.MAP_MOVED_SCHEME)) {
                return true
            } else if (url.startsWith(Consts.MOUSE_ENTER_SCHEME)) {
                return true
            } else if (url.startsWith(Consts.MOUSE_LEAVE_SCHEME)) {
                return true
            } else if (url.startsWith("http://") || url.startsWith("intent://")) {
                return true
            }
            return super.shouldOverrideUrlLoading(view, url)
        }

        override fun onReceivedError(
            view: WebView,
            errorCode: Int,
            description: String,
            failingUrl: String
        ) {
            super.onReceivedError(view, errorCode, description, failingUrl)
           LogUtil.Loge(description)
        }

        @TargetApi(Build.VERSION_CODES.M)
        override fun onReceivedError(
            view: WebView,
            request: WebResourceRequest,
            error: WebResourceError
        ) {
            LogUtil.Loge(error.toString())
            super.onReceivedError(view, request, error)
        }
    }

    fun findPredefined(name: String?): List<YandexMapsAddress>? {
        val found = ArrayList<YandexMapsAddress>()
        IterableUtils.find(this.predefined, object : Predicate<Place?> {
            override fun evaluate(place: Place?): Boolean {
                var getAddress: String? = null
                if (place != null) {
                    getAddress = place.address
                    if (!TextUtils.isEmpty(getAddress)) getAddress =
                        getAddress.toLowerCase()
                }
                if (place != null && getAddress != null && getAddress.startsWith(name!!)) {
                    val yPlace = YandexMapsAddress()
                    if (airports!!.contains(place)) {
                        yPlace.displayName =
                            AIRPORT + " " + place.address
                        yPlace.value = MOSCOW_AREA_NAME
                    } else if (metroStations!!.contains(place)) {
                        yPlace.displayName =
                            METRO + " " + place.address
                        yPlace.value = MOSCOW_CITY_NAME
                    } else {
                        yPlace.displayName = place.address
                        yPlace.value = MOSCOW_CITY_NAME
                    }
                    yPlace.latitude = place.location.latitude
                    yPlace.longitude = place.location.longitude
                    found.add(yPlace)
                }
                return false
            }
        })
        return found
    }

    private fun onSearchScheme(url: String) {
        val address: Int = url.indexOf(ADDRESS)
        if (address != -1) {
            val addresses: Int = url.indexOf(ADDRESSES)
            if (addresses != -1) {
                val allPlaces: MutableList<YandexMapsAddress> = ArrayList()
                var name: String? = url.substring(address + ADDRESS.length + 1, addresses - 1)
                try {
                    name = URLDecoder.decode(name, "UTF-8")
                    if (!TextUtils.isEmpty(name)) {
                        val predefinedPlaces: List<YandexMapsAddress>? =
                            this.findPredefined(name)
                        allPlaces.run {
                            predefinedPlaces?.let { addAll(it) }
                        }
                    }
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                }
                var query: String? =
                    url.substring(addresses + ADDRESSES.length + 1)
                try {
                    query = URLDecoder.decode(query, "UTF-8")
                    val gson = GsonBuilder().create()
                    val collectionType =
                        object : TypeToken<List<YandexMapsAddress?>?>() {}.type
                    val adrss =
                        gson.fromJson<List<YandexMapsAddress>>(
                            query,
                            collectionType
                        )
                    if (adrss != null) allPlaces.addAll(adrss)
                    mAdapter!!.refill(allPlaces)
                } catch (e: JsonSyntaxException) {
                    e.printStackTrace()
                    LogUtil.Loge(e)
                } catch (e: JsonParseException) {
                    e.printStackTrace()
                    LogUtil.Loge(e)
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                    LogUtil.Loge(e)
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                    LogUtil.Loge(e)
                }
                LogUtil.Log(query)
                hideKeyboard(context)
            }
        }
    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_address_search
    }

    companion object {
        const val TAG = "AddressSearch"

        private const val DATA_KEY = "data"
        fun newInstance(searchText: String?): AddressSearchFragment {
            val fragment = AddressSearchFragment()

            searchText?.let {
                val argument = Bundle()
                argument.putSerializable(DATA_KEY, it)
                fragment.arguments = argument
            }
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: AddressSearchPresenter


    @SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showToolbar(TOOLBAR_HIDE)
        mAdapter = AddressSearchAdapter(this)
        RxTextView.afterTextChangeEvents(searchEditText)
            .debounce(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                /*searchEditText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }
                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        if (s.isNotEmpty()) { // position the text type in the left top corner
                            searchEditText.gravity = Gravity.LEFT or Gravity.TOP
                        } else { // no text entered. Center the hint text.
                            searchEditText.gravity = Gravity.CENTER
                        }
                    }

                    override fun afterTextChanged(s: Editable) {*/

                        searchForAddress(searchEditText.text.toString())
                //    }
            //    })
            }

        /*  RxTextView.afterTextChangeEvents(searchEditText)
                   .debounce(500, TimeUnit.MILLISECONDS)
                   .observeOn(AndroidSchedulers.mainThread())
                   .subscribe {
                       searchForAddress(it.toString())
                   }*/


        searchEditText.gravity = Gravity.CENTER
        searchEditText.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                onLocationSearch()
                return@OnEditorActionListener true
            }
            false
        })
        searchEditText.isEnabled = false

        mRecyclerViewScrollListener = object : RecyclerView.OnScrollListener() {
            override
            fun onScrollStateChanged(addresList: RecyclerView, scrollState: Int) {
                if (scrollState == RecyclerView.SCROLL_STATE_DRAGGING) {
                     hideKeyboard(context)
                }
            }
        }

        // val advert = activity?.intent?.getSerializableExtra(DATA_KEY) as Advert?
        webView!!.settings.javaScriptEnabled = true

        mWebAppInterface = WebAppInterface(context)
        mWebAppInterface!!.setOnInitListener(this)
        webView.addJavascriptInterface(mWebAppInterface, WebAppInterface.NEXI)

        webView.webViewClient = mWebViewClient

        addresList.layoutManager = LinearLayoutManager(context)
        addresList.adapter = mAdapter
//        String map = IOUtil.readAsset(getActivity().getAssets(), "map.html");
//        Uri path = Uri.parse("file:///android_res/drawable/car");
//        map = map.replace("mydot://dot.png", path.toString());
//        webView.loadDataWithBaseURL(null, map, "text/html", "utf-8", null);

        //        String map = IOUtil.readAsset(getActivity().getAssets(), "map.html");
//        Uri path = Uri.parse("file:///android_res/drawable/car");
//        map = map.replace("mydot://dot.png", path.toString());
//        webView.loadDataWithBaseURL(null, map, "text/html", "utf-8", null);
        webView.loadUrl("file:///android_asset/map.html")
/*
        fakeImageView.setOnClickListener {
            router.backTo(ScreenPool.ADD_GOODS)
        }*/
    }
    private fun positionMapToCity(name: String) {
        val mapCommand = String.format(
            Locale.ENGLISH,
            "javascript:positionMapToCity('%s'); ",
            name
        )
        webView!!.loadUrl(mapCommand)
    }

    open fun onLocationChanged(location: Location?) {
        location
    }

    private fun onLocationSearch() {
        var addr: String = searchEditText.text.toString()
        if (!TextUtils.isEmpty(addr)) {
            val loc = Location("loc")
            //loc.latitude = MOSCOW_CENTER_LATITUDE
            //loc.longitude = MOSCOW_CENTER_LONGITUDE
            progressBar.post {
                progressBar.visibility = View.VISIBLE
                addresList.isEnabled = false
                searchEditText.isEnabled = false
                hideKeyboard(context)
            }
            val ym = YandexMaps()
            ym.getAddressBy(addr, loc, this)
        }
    }

   /* private fun searchForAddress(address: String) {
        val mapCommand = String.format(
            Locale.ENGLISH,
            "javascript:searchForAddress('%s'); ",
            address
        )
        webView.loadUrl(mapCommand)
    }*/
   open fun searchForAddress(address: String): Unit {
       val mapCommand = String.format(
           Locale.ENGLISH,
           "javascript:searchForAddress('%s'); ",
           address
       )
       webView.loadUrl(mapCommand)
   }
   override fun onListItemClick(position: Int): Boolean {
       val place = mAdapter!!.getItem(position)
       searchForAddress(place.value)
       val text = place.displayName.toString() + " "
       searchEditText.setText(text)
       searchEditText.setSelection(text.length, text.length)
       onLocationSearch()
       //todo тут я могу забирать место и закрывать фрагмент передавая данные
       return false
   }

    fun hideKeyboard(context: Context?) {
        try {
            if (context == null) return
            val inputMethodManager =
                context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(searchEditText.windowToken, 0)
        }
        catch (e: Exception) {

        }

    }
    override fun onListItemLongClick(position: Int) {}
    // сюда возвращается из яндекс-карт адрес с координатами
    override fun onSuccessResponse(place: Place) {
        titleImageView.post {
            progressBar.visibility = View.GONE
         //   hideKeyboard(context)
            val airport: Boolean =
                isInAirportRange(place.location)
            place.setIsAirport(airport)
            //   val res = Intent()
            /*when (orderParam) {
                    From -> {
                      //  mTitleImageView.setImageResource(R.drawable.from_whence_blue)
                     //   setTitleText(getString(R.string.FROM), place.address)
                        res.putExtra(ADDRESS_FROM, place)
                    }
                    To -> {
                     //   titleImageView.setImageResource(R.drawable.where_blue)
                       // text(getString(R.string.TO), place.address)
                        res.putExtra(ADDRESS_TO, place)
                    }
                }*/
            //    res.putExtra(REQUEST_CODE, getRequestCode())
            /* val a: FragmentActivity? = activity
                if (a != null) {
                    a.setResult(Activity.RESULT_OK, res)
                    a.finish()
                }*/
            ApplicationWrapper.place = place
            router.navigateTo(ScreenPool.ADD_GOODS)
            //todo exitWithResult
        }
    }

    override fun onFailure(statusCode: Int) {
        statusCode
        titleImageView.post(Runnable {
            progressBar.visibility = View.GONE
            addresList.isEnabled = true
            searchEditText.
                isEnabled = true
            showKeyboard(context)
            Toast.makeText(context, "error", Toast.LENGTH_LONG)
                .show()
        })
    }

    override fun onSuccessResponse(places: ArrayList<Place?>?) {}

    override fun onMapInitialized(init: Boolean) {
        webView.post {
            if (!init) {
                webView.loadUrl("file:///android_asset/map.html")
            } else {
                searchEditText.post(Runnable {
                    searchEditText.isEnabled = true
                    progressBar.visibility = View.GONE
                    showKeyboard(context)
                })
            }
        }
    }
    private fun showKeyboard(context: Context?) {
        if (context == null) return
        val imm =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(
            searchEditText,
            InputMethodManager.SHOW_IMPLICIT
        )
    }

    fun setRequestCode(requestCode: Int) {
        this.requestCode = requestCode
    }

    fun getRequestCode(): Int {
        return requestCode
    }
    //todo это из активити
    fun setAddressText(text: String) {
        searchEditText.setText(text)
        if (!TextUtils.isEmpty(text)) searchEditText.setSelection(
            text.length,
            text.length
        )
    }


    override fun onCreateLoader(id: Int, args: Bundle?): Loader<MutableList<Place>> {
        return PlacesLoader(context, id)
    }

    // загрузка тарифов вокзалы , аэропорты


    override fun onLoadFinished(loader: Loader<MutableList<Place>>, data: MutableList<Place>?) {
        val placeParam: Place.PlaceParam = Place.PlaceParam.valueOf(loader.getId())
        placeParam
        when (placeParam) {

            /*Metro -> NexiApplication.getActiveProfile().setMetroStations(data)
            Airport -> NexiApplication.getActiveProfile().setAirports(data)
            Train -> NexiApplication.getActiveProfile().setTrainStations(data)*/
        }
    }
    private fun isInAirportRange(location: Location?): Boolean {
        if (location == null) return false
        if (airports != null) {
            for (item in airports) {
                val centerLocation = item.location ?: continue
                val distance = centerLocation.distanceTo(location)
                if (distance <= 1000) return true
            }
        }
        return false
    }

    override fun onLoaderReset(loader: Loader<MutableList<Place>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
