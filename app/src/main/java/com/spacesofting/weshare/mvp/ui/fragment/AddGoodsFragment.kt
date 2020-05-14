package com.spacesofting.weshare.mvp.ui.fragment

import android.app.FragmentTransaction
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.afollestad.materialdialogs.MaterialDialog
import com.gpbdigital.wishninja.ui.watcher.WishNameToDescriptionWatcher
import com.pawegio.kandroid.visible
import com.pawegio.kandroid.w
import com.spacesofting.weshare.R
import com.spacesofting.weshare.api.Entity
import com.spacesofting.weshare.api.Entitys
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.FragmentWrapper
import com.spacesofting.weshare.common.ScreenPool
import com.spacesofting.weshare.mvp.model.Advert
import com.spacesofting.weshare.mvp.presentation.presenter.AddGoodsPresenter
import com.spacesofting.weshare.mvp.presentation.view.AddGoodsView
import com.spacesofting.weshare.mvp.ui.WishEditPresenterReporterWatcher
import com.spacesofting.weshare.mvp.ui.adapter.MyCyclePagerAdapter
import com.spacesofting.weshare.utils.ImageUtils
import com.spacesofting.weshare.utils.RealFilePath
import com.spacesofting.weshare.utils.hideKeyboard
import com.tbruyelle.rxpermissions2.RxPermissions
import com.wangpeiyuan.cycleviewpager2.CycleViewPager2Helper
import com.wangpeiyuan.cycleviewpager2.indicator.DotsIndicator
import kotlinx.android.synthetic.main.fragment_add_goods.*
import moxy.presenter.InjectPresenter
import org.imaginativeworld.whynotimagecarousel.CarouselItem
import org.imaginativeworld.whynotimagecarousel.CarouselOnScrollListener
import java.io.File


class AddGoodsFragment : FragmentWrapper(), AddGoodsView, AdapterView.OnItemSelectedListener,
    MyCyclePagerAdapter.OnCardClickListener {
    private val CAMERA_REQUEST_CODE = 1
    private val GALLERY_REQUEST_CODE = 2

    private var adapterBaner: MyCyclePagerAdapter? = null
    private val category = ApplicationWrapper.category

    @InjectPresenter
    lateinit var mAddGoodsPresenter: AddGoodsPresenter
    private var picker: ImagePickerFragment? = null
    private var pathImg: String? = null
    private var progressDialog: ProgressDialog? = null
    private var bannerItems = ArrayList<File>()
    private var categoryItems: ArrayList<Entity>? = null
    private var subCategoryItems = ArrayList<Entity>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showToolbar(TOOLBAR_HIDE)

        //load wish to presenter - //todo мы нажали на кнопку карандаша в своих вещах на адаптер item забрали везь и прокинули сюда
        /* val advert = activity?.intent?.getSerializableExtra(WishEditActivity.ARG_WISH) as Advert?

         advert?.let {
             //todo //presenter.wish = wish
             // title = getString(R.string.wish_edit_wish_title)
             //todo в случае если мы получили вещь и она пошла грузитсья в поля
             setLoadedWish(it)
         }
 */
        wishEditNameEditText.addTextChangedListener(WishNameToDescriptionWatcher(
            mAddGoodsPresenter.nameMaxLength
        ) { s ->
            wishEditDescriptionEditText.text?.clear()
            wishEditDescriptionEditText.text?.append(s)
            wishEditDescriptionEditText.requestFocus()
        })

        wishEditNameEditText.addTextChangedListener(
            WishEditPresenterReporterWatcher(
                mAddGoodsPresenter,
                AddGoodsPresenter.Field.NAME
            )
        )
        wishEditDescriptionEditText.addTextChangedListener(
            WishEditPresenterReporterWatcher(
                mAddGoodsPresenter,
                AddGoodsPresenter.Field.DESCRIPTION
            )
        )
        //  wishUrlEditText.addTextChangedListener(WishEditPresenterReporterWatcher(presenter = presenter, field = WishEditPresenter.Field.WISH_URL))
        wishEditAmountEditText.addTextChangedListener(
            WishEditPresenterReporterWatcher(
                mAddGoodsPresenter,
                AddGoodsPresenter.Field.PRICE
            )
        )

        mAddGoodsPresenter.goodId = arguments?.getSerializable(DATA_KEY).toString()
        banner.viewPager2.apply {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
        setBannerData()
        view.viewTreeObserver.addOnWindowFocusChangeListener {
        }
//todo выбор валюты
        val adapter = ArrayAdapter.createFromResource(
            activity,
            R.array.wish_edit_cyrrency,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        privacyOptions.adapter = adapter
        privacyOptions.onItemSelectedListener = this
//todo выбор промежутка времени
        val adpaterPeriod = ArrayAdapter.createFromResource(
            activity,
            R.array.wish_edit_period,
            android.R.layout.simple_spinner_item
        )
        adpaterPeriod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        priceOptions.adapter = adpaterPeriod
        priceOptions.onItemSelectedListener = this
        banner.viewPager2.apply {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
        //todo слушатель даптера фотографий для ввода
        banner.viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.e("Selected_Page", position.toString())
            }
        })
        save.setOnClickListener { mAddGoodsPresenter.checkEditFieldsOrImage() }
        //todo слушаем ввод текста - открываем фрагмент с поиском адреса
        searchEditText.setOnTouchListener(
            object : OnTouchListener {
                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        router.navigateTo(ScreenPool.ADDRESS_SEARCH)
                        /*val i = Intent(activity, AddressSearchActivity::class.java)
                        i.putExtra(MainActivity.ORDER_PARAM, getOrderParam().ordinal())
                        if (getOrderParam() === Order.OrderParam.From) i.putExtra(
                            AddressSearchActivity.ADDRESS_TEXT,
                            NexiApplication.getActiveProfile().getCurrentOrder().getFrom().getAddress()
                        ) else i.putExtra(
                            AddressSearchActivity.ADDRESS_TEXT,
                            NexiApplication.getActiveProfile().getCurrentOrder().getTo().getAddress()
                        )
                        i.putExtra(
                            MainActivity.REQUEST_CODE,
                            ru.ddg.nexi.nexitaxi.fragments.PlaceFragment.ACTIVITY_SEARCH_CODE
                        )
                        startActivityForResult(
                            i,
                            ru.ddg.nexi.nexitaxi.fragments.PlaceFragment.ACTIVITY_SEARCH_CODE
                        )*/
                        return true
                    }
                    return false
                }
            })
    }

    //todo размещаем поля - если мы зашли в режим редактрования объявления
    private fun setLoadedWish(wish: Advert) {
        setConfirmButtonState(mAddGoodsPresenter.isValid(wish))
        //   wishUrlEditText.setText(wish.url)
        // presenter.newWish.images = wish.images
        wishEditNameEditText.setText(wish.title)
        wishEditDescriptionEditText.setText(wish.description)

        //for check edit fields
        mAddGoodsPresenter.editWishName = wish.title
        mAddGoodsPresenter.editWishDescription = wish.description

        //todo за место этого кода - мы получаем список url и заполняем список

        /*wish.images?.let {
            if (it.isNotEmpty()) {
                it[0].name?.let { name ->
                    presenter.imageChanged = true
                    pathImg = ImageUtils.resolveImagePath(name)
                    presenter.loadImageFromUrl(URL(pathImg))
                    Picasso.with(activity).load(pathImg!!).into(wishEditImageView)
                    addImgLayout.visibility = View.GONE
                    wishChangeImgBtn.visibility = View.VISIBLE

                    //for check edit image
                    val pathImgName = pathImg!!.substring(pathImg!!.lastIndexOf("/"))
                    val pathImgNameRemoveFirstChar = (pathImgName.subSequence(1, pathImgName.length)).toString()
                    presenter.editWishImage = pathImgNameRemoveFirstChar
                }
            }
        }*/
        //todo установка полей
        wish.rentPeriods?.let {
            wishEditAmountEditText.setText(it[0].amount.toString())
            //for check edit fields
            mAddGoodsPresenter.editWishAmount = it[0].amount.toString()
        } ?: run {
            wishEditAmountEditText.setText("0.0")
            //fix not calling "fieldChanged" from textWatcher
            mAddGoodsPresenter.fieldChanged("0.0", AddGoodsPresenter.Field.PRICE)

            //for check edit fields
            mAddGoodsPresenter.editWishAmount = "0.0"
        }
    }

    override fun showWishImage(file: File) {
        //todo  сюда складываем фото для адаптера - воти все вот и все
        /*     Picasso.with(context)
                 .load(file)
                 .centerCrop()
                 .resizeDimen(R.dimen.avatar_size_profile_edit, R.dimen.avatar_size_profile_edit)
                 .into(wishEditImageView)*/
        mAddGoodsPresenter.imageChanged = true
        mAddGoodsPresenter.imageFile = file
        // mAddGoodsPresenter.fieldChanged(file.path, AddGoodsPresenter.Field.IMAGE)
        //  addImgLayout.visibility = View.GONE
        // wishChangeImgBtn.visibility = View.VISIBLE
        // dellImage.visibility = View.VISIBLE
        //todo
        refrashAdapterBaner(file)
    }

    override fun setNewSubCategory(it: Entitys) {
        initAdapterCategory(it)
    }

    fun showSubCategory(id: String?) {
        mAddGoodsPresenter.getSubCategory(id)
    }

    override fun showToast(stringId: Int) {
        Toast.makeText(activity, stringId, Toast.LENGTH_LONG).show()
    }

    override fun showMaterialDialog(stringId: Int) {
        activity?.let {
            MaterialDialog.Builder(it)
                //  .title(title)
                .content(stringId)
                .positiveText(R.string.ok)
                .build()
                .show()
        }
    }

    override fun wishImageDelete() {
        //  Picasso.with(context).load(R.drawable.wish_default_img).into(wishEditImageView)
        mAddGoodsPresenter.imageChanged = true
        // mAddGoodsPresenter.fieldChanged(file.path, AddGoodsPresenter.Field.IMAGE)
        //   addImgLayout.visibility = View.VISIBLE
        //   wishChangeImgBtn.visibility = View.GONE
        //   dellImage.visibility = View.GONE
    }

    //todo тут нажимает на кнопку сохранить внизу - если прошли валидацию - она становится активна
    override fun save() {
        /*if (!mAddGoodsPresenter.isWishEdit()) {
            activity?.finish()
        } else {
            activity?.let {
                MaterialDialog.Builder(it)
                    .title(R.string.dialog_confirm_save)
                    .content(R.string.dialog_confirm_save)
                    .positiveText(R.string.save)
                    .onPositive { _, _ ->
                       // mAddGoodsPresenter.save()
                    }
                  //  .negativeText(R.string.reset)
                    .onNegative { _, _ ->
                        activity!!.finish()
                    }
                    .show()
            }
        }*/
    }

    override fun cancel() {
        activity?.let {
            MaterialDialog.Builder(it)
                .title(R.string.wish_edit_picker_title)
                .content(R.string.wish_edit_add_img_obligatorily)
                .positiveText(R.string.edit_guest_card_inn_count)
                .onPositive { _, _ ->
                    activity!!.finish()
                }
                .neutralText(R.string.edit)
                .show()
        }
    }

    override fun showProgress(isShowed: Boolean) {
        progressDialog?.let {
            if (isShowed) {
                it.setMessage(getString(R.string.wish_edit_progress))
                it.show()
            } else if (it.isShowing) {
                it.dismiss()
            }
        }
    }

    //todo валидация - ок - кнопка активна
    override fun setConfirmButtonState(isEnabled: Boolean) {
        save.isEnabled = isEnabled
    }

    override fun saved(isSuccess: Boolean) {
        /* if (isSuccess) {
             val wish = if (mAddGoodsPresenter.wish != null) mAddGoodsPresenter.wish else mAddGoodsPresenter.newWish
             mAddGoodsPresenter.wish?.isNew().let {
                 //   logEvent("wish_created")
             }

             wishSaveSuccess(wish)
             returnWish()
             goToAddCardFragment()
         }
         if (!Settings.isAuthenticated()) {
             goToRegister()
         }*/
    }

    override fun emptyPrice(isEmpty: Boolean) {
        if (isEmpty) {
            emptyMessage.visibility = View.VISIBLE
            connectBankCard.visibility = View.GONE
            ymLimitRedLine.visibility = View.GONE
        } else {
            emptyMessage.visibility = View.GONE
        }
    }

    //todo отображение фото на пикере перед подтверждением
    override fun setPreviewImg(file: File) {
        picker?.setImage(file)
    }

    //todo на экран регистрации - если хотим разместить вещь
    override fun goToRegistration() {
        //  goToRegister()
    }

    //todo  не используется пока что - ссылка парсить объвление из сети через сервер
    override fun showBrokenUrlMessage(isShowed: Boolean) {
        //brokenUrlMessage.visible = isShowed
    }

    override fun showCheckUrlProgress(isShowed: Boolean) {
        /* checkUrlProgress.visible = isShowed

         if (wishUrlEditText.text?.isNotEmpty()!!) {
             clearUrl.visible = !isShowed
         }*/
    }

    override fun showClearUrlBtn(isShowed: Boolean) {
        /*if (!checkUrlProgress.visible) {
            clearUrl.visible = isShowed
            getUrl.visible = !isShowed
        }*/
    }

    //todo задний фон экрана
    override fun showAutoCompleteProgress(isShowed: Boolean) {
        autoCompleteProgress.visible = isShowed
    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_add_goods
    }

    companion object {
        const val TAG = "AddGoodsFragment"
        private const val DATA_KEY = "data_key"

        fun getInstance(goodId: String?): AddGoodsFragment {
            val fragment =
                AddGoodsFragment()

            goodId?.let {
                val argument = Bundle()
                argument.putSerializable(DATA_KEY, it)
                fragment.arguments = argument
            }
            return fragment
        }
    }

    //todo инициализация банеров / категорий и прочее
    private fun setBannerData() {
        initAdapterCategory(null)
        initData()
        // initDataCategory()
        //todo добавляем фото
        btn_add.setOnClickListener {
            showPicker()
            //todo я вызываю пикер потом жду возврата фото и потом обновляю адаптер
            /* bannerItems.add(R.drawable.electronix)   //todo случайный элемент(resList.random())
             bannerItems.add(R.drawable.wish_default_img)   //todo случайный элемент(resList.random())
             bannerItems.add(R.drawable.wish_default_img)   //todo случайный элемент(resList.random())*/

            //    adapterBaner.notifyDataSetChanged()


        }
        //todo удаляем фото
        btn_remove.setOnClickListener {
            if (bannerItems.isNotEmpty()) {
                val index = bannerItems.size - 1
                bannerItems.removeAt(index)
                adapterBaner?.notifyDataSetChanged()

                if (bannerItems.isEmpty()) {
                    btn_remove.visible = false

                }
            }
        }

        val nextItemVisiblePx = resources.getDimension(R.dimen.dialog_corner_radius)
        val currentItemHorizontalMarginPx =
            resources.getDimension(R.dimen.oval_radius)
        val dotsRadius = resources.getDimension(R.dimen.rect_corner_radius)
        val dotsPadding = resources.getDimension(R.dimen.rect_corner_radius)
        val dotsBottomMargin = resources.getDimension(R.dimen.rect_corner_radius)
        //todo попытка сделать пейджер бзе прокруток
        /*     banner.viewPager2.overScrollMode = 2

             banner.viewPager2.apply {
                 orientation = ViewPager2.ORIENTATION_HORIZONTAL
                 (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER*/
        // }

        CycleViewPager2Helper(banner)
            .setAdapter(adapterBaner)
            .setMultiplePagerScaleInTransformer(
                nextItemVisiblePx.toInt(),
                currentItemHorizontalMarginPx.toInt(),
                0.1f
            )
            .setDotsIndicator(
                dotsRadius,
                Color.RED,
                Color.GRAY,
                dotsPadding,
                0,
                dotsBottomMargin.toInt(),
                0,
                DotsIndicator.Direction.CENTER
            )
            // .setAutoTurning(3000L)
            .build()

        banner.viewPager2.apply {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
        //todo category
    }

    private fun initAdapterCategory(it: Entitys?) {
        setPhotoAdapter(it)
    }

    //todo добавление фото по соответсвию наименованию к категориям
    private fun setPhotoAdapter(it: Entitys?) {
        var resourceId: Int? = null
        if (it == null) {
            category?.entities?.map {
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
                }
                val uri: Uri =
                    Uri.parse("android.resource://" + activity?.packageName.toString() + "/" + resourceId)
                it.categoryImg = uri.toString()
            }
        } else {

        }
    }


    private fun initData() {
        // подписываем нашу активити на события колбэка
        initBanners()
        initCategoryList()
    }

    private fun initBanners() {
        adapterBaner = MyCyclePagerAdapter()
        adapterBaner?.setOnCardClickListener(this)

        if (bannerItems.isEmpty()) {
            //   bannerItems.add(f)   //todo случайный элемент(resList.random())
            adapterBaner?.dataset = bannerItems
            adapterBaner?.notifyDataSetChanged()
        }
    }

    private fun refrashAdapterBaner(file: File) {
        // bannerItems.clear()
        bannerItems.add(file)
        adapterBaner?.dataset = bannerItems
        btn_remove.visible = bannerItems.isNotEmpty()
        adapterBaner?.notifyDataSetChanged()
    }

    private fun initCategoryList() {
        val listFour = mutableListOf<CarouselItem>()
        categoryCycleView.captionTextSize = 0

        category?.entities?.map {
            it.categoryImg?.let { it1 ->
                CarouselItem(
                    imageUrl = it1,
                    caption = it.name
                )
            }?.let { it2 ->
                listFour.add(
                    it2
                )
                categoryCycleView.addData(listFour)
            }
        }

        categoryCycleView.onScrollListener = object : CarouselOnScrollListener {

            override fun onScrollStateChanged(
                recyclerView: RecyclerView,
                newState: Int,
                position: Int,
                carouselItem: CarouselItem?
            ) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    carouselItem?.apply {
                        custom_caption.text = caption
                    }
                }

            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                // todo меняем категорию - получаем номер саб категориий делаем запрос на сервер через призентер
                // todo презентер отображает initSabCategoryList ()
            }
        }
        categoryCycleView.setIndicator(custom_indicator)
    }

    override fun openCamera(file: File) {
        activity?.let {
            RxPermissions(it)
                .request(android.Manifest.permission.CAMERA)
                .subscribe {
                    try {
                        // brokenUrlMessage.visible = false
                        val intent = ImageUtils.takePhotoIntent(file)
                        startActivityForResult(intent, CAMERA_REQUEST_CODE)
                    } catch (e: SecurityException) {
                        //  dialogGPS(this.context) // lets the user know there is a problem with the gps
                    }
                    //   val  location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                }
        }
    }

    override fun openGallery() {
        val getContentIntent = Intent(Intent.ACTION_GET_CONTENT)
        getContentIntent.type = "image/*"
        getContentIntent.addCategory(Intent.CATEGORY_OPENABLE)
        val intent = Intent.createChooser(getContentIntent, "Select image")
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> mAddGoodsPresenter.onCameraResult()
            GALLERY_REQUEST_CODE -> consumeGalleryResult(data)
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun consumeGalleryResult(data: Intent?) {
        data?.let {
            val uri = it.data as Uri
            val path = activity?.let { it1 -> RealFilePath.getPath(it1, uri) }
            if (path != null) {
                val file = File(path)
                mAddGoodsPresenter.onGalleryResult(file)
            } else {
                w("Could not load result from gallary")
            }
        }
    }

    private fun showPicker() {
        activity?.hideKeyboard()

        picker = ImagePickerFragment()
        picker?.listener = mAddGoodsPresenter
        picker?.isAvatarForm = false
        picker?.pathImage = pathImg
        picker?.file = mAddGoodsPresenter.imageFile

        activity?.let {
            RxPermissions(it)
                .request(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe { granted ->
                    picker?.galleryPermissionGranted = granted
                    fragmentManager?.beginTransaction()
                        ?.addToBackStack(null)
                        ?.add(R.id.container, picker!!)
                        ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        ?.commit()
                    ApplicationWrapper.context = this.context!!

                }
        }
    }

    override fun onDetach() {
        super.onDetach()
        categoryItems?.clear()
        subCategoryItems.clear()
        //todo если пользователь жмет отменить создаие вещи -
        // presenter.deletNewGoods
    }
    /*    fun onBackPressed() {
            val fragment = fragmentManager!!.findFragmentByTag("name")
            if (fragment != null && fragment.isVisible) {
                //  do something
            } else {
                super.onBackPressed()
            }
        }*/

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // presenter.fieldChanged(null, WishEditPresenter.Field.PRIVACY, Wish.PrivacyLevel.values()[p2])
        when (parent!!.id) {
            R.id.priceOptions -> Toast.makeText(
                activity,
                "hello,spinner1",
                Toast.LENGTH_SHORT
            ).show()
            R.id.privacyOptions -> Toast.makeText(
                activity,
                "hello,spinner2",
                Toast.LENGTH_SHORT
            ).show()
            else -> {
                Toast.makeText(
                    activity,
                    "hello,spinner3",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}
    override fun onCardClick() {
        showPicker()
    }

}
