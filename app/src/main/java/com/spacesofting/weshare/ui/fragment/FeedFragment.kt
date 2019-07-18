package com.spacesofting.weshare.ui.fragment

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spacesofting.weshare.mvp.view.FeedView
import com.spacesofting.weshare.mvp.presentation.FeedPresenter

import com.arellomobile.mvp.presenter.InjectPresenter
import com.paginate.Paginate
import com.spacesofting.weshare.R
import com.spacesofting.weshare.common.FragmentWrapper
import kotlinx.android.synthetic.main.fragment_feed.*

class FeedFragment : FragmentWrapper(), FeedView, Paginate.Callbacks {
    private val EDIT_WISH_REQUEST = 100

    companion object {
        fun getInstance(): FeedFragment = FeedFragment()
    }

    var mainAdapter: FeedAdapter? = null
    var paginateLoading = false
    var paginatePage = 0
    var lastLoadedCount = 0
    var currentListLoadingMode = ListLoadingMode.FEED

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

        //make new adapter
        val elementWidth = (activity.screenWidth - 2 * 6.dp - 4 * 4.dp) / 2
        mainAdapter = FeedAdapter(feedPresenter, elementWidth)
        wishesList.adapter = mainAdapter
        wishesList.init()
        paginatePage = 0
        lastLoadedCount = 0

        //do pagging
        Paginate.with(wishesList, this).build()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!(activity as NavigationActivity).isMyProfile()){
            return
        }

        if (ApplicationWrapper.instance.isDesireToAuthorize == true) {
            feedPresenter.saveWish()
        }

        reload()
        wishesList.topPadding(0)

        wishesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, scrollState: Int) {
                super.onScrollStateChanged(recyclerView, scrollState)
                if (scrollState == RecyclerView.SCROLL_STATE_IDLE) {
                    mainAdapter?.resumeImgDownload(activity)
                } else {
                    mainAdapter?.pauseImgDownload(activity)
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
        if(!(activity as NavigationActivity).isMyProfile()){
            return
        }

        feedPresenter.handleWishToAdd()
        currentListLoadingMode = ListLoadingMode.FEED
        wishesList.visible()
        errorLayout.gone()
    }




    override fun addTemplateWishes(templates: List<ListWishElement>) {

        hidePageLoadingError()

    }


}