package com.spacesofting.weshare.mvp.presentation.presenter

import com.pawegio.kandroid.e
import com.spacesofting.weshare.api.Api
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.mvp.Image
import com.spacesofting.weshare.mvp.Template
import com.spacesofting.weshare.mvp.Wish
import com.spacesofting.weshare.mvp.model.Advert
import com.spacesofting.weshare.mvp.model.dto.WishList
import com.spacesofting.weshare.mvp.presentation.view.FeedView
import com.spacesofting.weshare.mvp.ui.adapter.ListWishElement
import com.spacesofting.weshare.mvp.ui.fragment.FeedFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class FeedPresenter : MvpPresenter<FeedView>() {

    val templatesPerPage = 20
    var defaultList: WishList? = null
    var wishToAdd: ListWishElement? = null
    val templatesList = ArrayList<ListWishElement>()

    private val LIMIT = 0.toDouble()
    var limit: Double = LIMIT
    var wish: Advert? = null
        set(value) {
       /*     field = value
            newWish = field?.clone() as Advert
            //wish is not mine, we cannot save foreign wish. So it will be a new wish after saving
            if (!newWish.isMy()) {
                newWish.id = 0
            }*/
        }

    private var newWish = Advert()
    var photoPath = ApplicationWrapper.instance.getImagePathWish()
    var imageFile = photoPath
    var imageChanged = false

    var lists: List<WishList>? = null


    init {
        if (ApplicationWrapper.instance.isDesireToAuthorize) {

            imageChanged = true
            ApplicationWrapper.instance.isDesireToAuthorize = false
            newWish = ApplicationWrapper.instance.getAuthorityWish()!!
            saveWish()

           // newWish.price = Money(0)

        /*    Api.Wishes.lists()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ lists ->
                    this.lists = lists
                })*/
        }
    }

    fun tryToAddWish(item: ListWishElement) {
        viewState.showWishAddingProgress(item)

        if (defaultList != null) {
            val listId = (defaultList as WishList).id
            listId?.let {
              /*  Api.Wishes.wishes(item!!.id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ list ->

                        if (list.isEmpty() || item !is Template) {
                            var template: Template? = null
                            var wish: Wish? = null
                            if (item is Template) {
                                template = item as Template
                                wish = Wish(template.id, it)
                            } else if (item is Wish) {
                                wish = item as Wish
                            }

                            addWish(template, wish!!)
                        } else {
                            viewState.wishExists(item, list.size)
                        }
                    }, { error ->
                        viewState.wishAddError(error.message)
                    })*/
            }
        } else {
            try {
                loadLists(item)
            } catch (e: java.lang.NullPointerException) {
                e("${e.message}")
            }
        }
    }

    fun confirmAddingWish(template: Template) {
        val wish = Wish(template.id, defaultList!!.id!!)
        addWish(template, wish)
    }

    fun loadLists(template: ListWishElement) {
/*        Api.Wishes.lists()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { list ->
                    run {
                        //or use existing one
                        defaultList = list[0]
                        tryToAddWish(template)
                    }
                },
                { ex -> throw NullPointerException(ex.message) })*/
    }

    private fun addWish(template: Template?, wish: Wish) {
        if (template != null) {/*
            Api.Wishes.wishAddFromTemplate(template.id, wish)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ wishNew ->
                    viewState.wishAdded(wishNew, template.id)
                    wishToAdd = null
                },
                    { error ->
                        //hide progress
                        viewState.setLoadingProgressVisibility(false)
                        viewState.hideWishAddingProgress(template.id)
                        if ((error as HttpException).code() == HttpStatusCode.HTTP_BANK_CARD_REGISTRATION_REQUIRED) {
                            wishToAdd = template
                        }
                    })*/
        } else {
           /* Api.Wishes.wishAdd(wish)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ wishNew ->
                    viewState.wishAdded(wishNew, wish.id)
                    wishToAdd = null
                },
                    { error ->
                        //hide progress
                        viewState.setLoadingProgressVisibility(false)
                        viewState.hideWishAddingProgress(wish.id)

                        // in case of registration required we remember item that was chosen by user
                        // and deliver save operation to handleWishToAdd function
                        if ((error as HttpException).code() == HttpStatusCode.HTTP_BANK_CARD_REGISTRATION_REQUIRED) {
                            wishToAdd = wish
                        }
                    })*/
        }
    }

    fun handleWishToAdd() {
      /*  if (ApplicationWrapper.instance.profile == null)
        {
            Api.Users.getAccount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ profile ->
                    ApplicationWrapper.instance.profile = profile
                }, { e ->
                })
        }
        wishToAdd?.let {
            if (it is Wish) {
                viewState.editWish(it)
            } else if (it is Template) {
                viewState.editWish(Wish(it, defaultList!!.id!!))
            }
            wishToAdd = null
        }*/
    }

    fun onNewTemplatesRequired(mode: FeedFragment.ListLoadingMode, page: Int) {
        when (mode) {
            FeedFragment.ListLoadingMode.FEED -> {
             /*   Api.Wishes.feed(templatesPerPage, page * templatesPerPage)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext { templatesList.addAll(it) }
                    .subscribe({ list -> viewState.addTemplateWishes(list) },
                        { error -> viewState.showPageLoadingError() })*/
            }
            FeedFragment.ListLoadingMode.TEMPLATES -> {
         /*       Api.Wishes.templates(templatesPerPage, page * templatesPerPage)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext { templatesList.addAll(it) }
                    .subscribe({ list -> viewState.addTemplateWishes(list) },
                        { error -> viewState.showPageLoadingError() })*/
            }
        }
    }

    fun showProfile(id: Int?) {
        if (id != null) {
            viewState.goToProfile(id)
        }
    }

    fun goToRegister() {
        viewState.goToRegister()
    }

    fun showWish(wish: ListWishElement) {
        viewState.goToWish(wish)
    }

    fun pay(wish: Wish) {
        wish.profile?.let {
            viewState.pay(wish)
        }
    }

    fun pull2refresh() {
        viewState.reload()
    }

    fun saveWish() {
     /*   ImageUtils.send(this?.imageFile!!, goodId)?.subscribe({ img ->
           // save(img)
            imageChanged = false
        }, { error ->
        })*/
    }

    private fun save(img: Image) {
        //setup new image before saving
        if (img != null) {
         //   newWish.images = arrayOf(img)
        }

 /*       Api.Wishes.wishAdd(newWish)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ })*/
    }

    fun scrollOnTop(){
        viewState.scrollOnTop()
    }
}
