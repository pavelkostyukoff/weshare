package com.spacesofting.weshare.mvp.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import moxy.presenter.InjectPresenter
import com.paginate.Paginate
import com.pawegio.kandroid.toast
import com.pawegio.kandroid.visible
import com.spacesofting.weshare.R
import com.spacesofting.weshare.R.color.gray5555
import com.spacesofting.weshare.common.FragmentWrapper
import com.spacesofting.weshare.mvp.Template
import com.spacesofting.weshare.mvp.Wish
import com.spacesofting.weshare.mvp.presentation.presenter.FeedPresenter
import com.spacesofting.weshare.mvp.presentation.view.FeedView
import com.spacesofting.weshare.mvp.ui.adapter.FeedAdapter
import com.spacesofting.weshare.mvp.ui.adapter.ListWishElement
import com.spacesofting.weshare.utils.dp
import com.spacesofting.weshare.utils.gone
import com.spacesofting.weshare.utils.screenWidth
import kotlinx.android.synthetic.main.fragment_feed.*
import java.util.*

class FeedFragment : FragmentWrapper(),
    FeedView,  Paginate.Callbacks {
    override fun getFragmentLayout(): Int {
return R.layout.fragment_feed
    }

    private val EDIT_WISH_REQUEST = 100

    companion object {
        fun getInstance(): FeedFragment =
            FeedFragment()
    }

    var mainAdapter: FeedAdapter? = null
    var paginateLoading = false
    var paginatePage = 0
    var lastLoadedCount = 0
    var currentListLoadingMode =
        ListLoadingMode.FEED

    enum class ListLoadingMode {
        FEED, TEMPLATES
    }

    @InjectPresenter
    lateinit var feedPresenter: FeedPresenter

    /**
     * Make new adapter whcih have to redresh the list
     */
    override fun reload() {
        hidePageLoadingError()

        val t = Template()
        t.description = "SDkjasdmkjsnfmls"

       val tt =  ArrayList<ListWishElement>()
        tt.add(t)

        //make new adapter
        val elementWidth = (activity!!.screenWidth - 2 * 6.dp - 4 * 4.dp) / 2
        mainAdapter = FeedAdapter(feedPresenter, elementWidth,tt)
        wishesList.adapter = mainAdapter
        wishesList.init()
        paginatePage = 0
        lastLoadedCount = 0

        //do pagging
        Paginate.with(wishesList, this).build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*if(!(activity as NavigationActivity).isMyProfile()){
            return
        }

        if (ApplicationWrapper.instance.isDesireToAuthorize == true) {
            feedPresenter.saveWish()
        }*/

        reload()
        wishesList.topPadding(0)

        wishesList.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: androidx.recyclerview.widget.RecyclerView, scrollState: Int) {
                recyclerView?.let { super.onScrollStateChanged(it, scrollState) }
                if (scrollState == androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE) {
                    activity?.let { mainAdapter?.resumeImgDownload(it) }
                } else {
                    activity?.let { mainAdapter?.pauseImgDownload(it) }
                }
            }
        })

        //handle Pull2Refrsh
        swipe.setOnRefreshListener {
            swipe.isRefreshing = false
            feedPresenter.pull2refresh()
        }

        errorLayout.setOnRefreshListener {
            errorLayout.isRefreshing = false
            feedPresenter.pull2refresh()
        }
    }
    override fun onResume() {
        super.onResume()
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        /* if(!(activity as NavigationActivity).isMyProfile()){
             return
         }*/
        feedPresenter.handleWishToAdd()
        currentListLoadingMode =
            ListLoadingMode.FEED
        wishesList.visible
        errorLayout.gone()
    }

    fun showAvatar() {
      /*  if (ApplicationWrapper.user.avatar != null)
        {
            Picasso.with(activity)
                .load(ApplicationWrapper.user.avatar)
                .centerCrop()
                .resizeDimen(R.dimen.avatar_size_profile_edit, R.dimen.avatar_size_profile_edit)
                .into(titileAvatar,
                    object : Callback {
                        override fun onSuccess() {
                            // loadImageProgress.visibility = View.GONE
                        }
                        override fun onError() {
                            //  loadImageProgress.visibility = View.GONE
                        }
                    })
        }*/
    }


    override fun addTemplateWishes(templates: List<ListWishElement>) {
        hidePageLoadingError()
        mainAdapter?.let {
            val position = it.dataset.size
            if (position == 0) {
                wishesList.visible
                setLoadingProgressVisibility(false)
            }
            lastLoadedCount = templates.size

            if ((templates.size != feedPresenter.templatesPerPage) && (currentListLoadingMode == ListLoadingMode.FEED)) {
                currentListLoadingMode =
                    ListLoadingMode.TEMPLATES
                paginatePage = 0
                lastLoadedCount = 0
            } else {
                paginatePage++
            }
            paginateLoading = false

            it.dataset.addAll(templates)
            if (position == 0) {
                it.notifyDataSetChanged()
            } else {
                it.notifyItemRangeInserted(position, templates.size)
            }
        }
    }

    override fun onLoadMore() {
        paginateLoading = true
        feedPresenter.onNewTemplatesRequired(currentListLoadingMode, paginatePage)
    }

    override fun isLoading() = paginateLoading

    override fun hasLoadedAllItems(): Boolean {
        if (paginatePage == 0 || lastLoadedCount == feedPresenter.templatesPerPage) {
            return false
        }

        return true
    }

    override fun showPageLoadingError() {
        showLoadingError(null)
    }

    override fun hidePageLoadingError() {
        hideError()
    }

    override fun showLoadingError(errorMessage: String?) {
        showError(errorMessage)
    }

    override fun showLoadingError(errorMessageRes: Int) {
        showLoadingError(getString(errorMessageRes))
    }

    override fun showWishAddingProgress(item: ListWishElement?) {
        if (item != null) {
            item.setLoading(true)

            mainAdapter?.let {
                val position = it.dataset.indexOf(item)
                it.notifyItemChanged(position)
            }
        }
    }

    override fun setLoadingProgressVisibility(visible: Boolean) {
      //  progressBar.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun hideWishAddingProgress(sourceItemId: Int) {
        mainAdapter?.let {
            for (i in 0 until it.dataset.size) {
                val element = it.dataset[i]
                if (element.id == sourceItemId && element.isLoading()) {
                    element.setLoading(false)
                    it.notifyItemChanged(i)
                    break
                }
            }
        }
    }

    @SuppressLint("StringFormatInvalid")
    override fun wishAdded(wish: Wish, sourceId: Int) {
        hideWishAddingProgress(sourceId)
        toast(getString(R.string.save, wish.name))

      //  (activity as NavigationActivity).logEvent("wish_created")
    }

    @SuppressLint("ResourceAsColor")
    override fun wishExists(item: Template, count: Int) {
        activity?.let {
            MaterialDialog.Builder(it)
                .canceledOnTouchOutside(false)
                .content(getString(R.string.library_roundedimageview_licenseId,
                    resources.getQuantityString(R.plurals.add_wish_exist_plurals, count, count)))
                .positiveText("SDSADS")
                .negativeText("QQQ")
                .negativeColor(gray5555)
                .onPositive { _, _ -> feedPresenter.confirmAddingWish(item) }
                .cancelListener {
                    run {
                        item.setLoading(false)
                        mainAdapter?.let {
                            val position = it.dataset.indexOf(item)
                            it.notifyItemChanged(position)
                        }
                    }
                }
                .show()
        }
    }

    override fun wishAddError(errorMessage: String?) {
        mainAdapter?.notifyDataSetChanged()

        activity?.let {
            MaterialDialog.Builder(it)
                .content("WoW")
                .positiveText(R.string.ok)
                .onPositive { dialog, _ ->  }
                .show()
        }
    }

    override fun goToEditProfile() {
        //(activity as NavigationActivity).gotoEdit()
    }

    override fun goToRegister(){
      //  (activity as NavigationActivity).goToRegister()
    }

    override fun editWish(wish: Wish) {
        //Todo Создать или редактировать вещь/
        //val intent = WishEditActivity.getIntent(activity, wish)
       // startActivityForResult(intent, EDIT_WISH_REQUEST)
    }

    override fun goToProfile(id: Int) {
     //   startActivity(NavigationActivity.getIntent(activity, id, NavigationActivity.PROFILE_SLIDE))
    }

    override fun goToWish(wish: ListWishElement) {
        //todo Подробное отображение вещи ***
        //startActivity(WishActivity.getIntent(activity, wish))
    }

    private fun showError(errorMessage: String?) {
        wishesList.visibility = View.GONE

        val error = if (errorMessage.isNullOrBlank()) getString(R.string.error_general) else errorMessage
        errorLayout.visible
        errorLayout.findViewById<TextView>(R.id.errorText).text = error
    }

    private fun hideError() {
        wishesList.visibility = View.VISIBLE
        errorLayout.visibility = View.GONE
    }

    override fun pay(wish: Wish) {
       // (activity as NavigationActivity).pay(wish)
    }

    override fun scrollOnTop(){
        wishesList.scrollToPosition(0)
    }
}
