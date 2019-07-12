package com.spacesofting.weshare.ui.fragment.ui.fragment.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spacesofting.weshare.ui.fragment.ui.fragment.presentation.view.FeedCompilationsView
import com.spacesofting.weshare.ui.fragment.ui.fragment.presentation.presenter.FeedCompilationsPresenter

import com.arellomobile.mvp.MvpFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.paginate.Paginate
import com.pawegio.kandroid.runDelayed
import com.pawegio.kandroid.visible
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.FragmentWrapper
import com.spacesofting.weshare.common.Settings
import com.spacesofting.weshare.ui.adapter.FeedCompilationsAdapter

class FeedCompilationsFragment :
    FragmentWrapper(),
    FeedCompilationsView,
    Paginate.Callbacks{
    override fun getFragmentLayout(): Int {
        return R.layout.fragment_feed_compilations
    }

    var feedAdapter: FeedCompilationsAdapter? = null
    var isAuthenticated = true //Settings.isAuthenticated()

    @InjectPresenter
    lateinit var mPresenter: FeedCompilationsPresenter

    companion object {
        fun getInstance(): FeedCompilationsFragment = FeedCompilationsFragment()
    }


    private fun initCompilationsList() {
        feedAdapter = FeedCompilationsAdapter(activity as Context, mPresenter)
        compilationsList.adapter = feedAdapter
        compilationsList.layoutManager = LinearLayoutManager(activity)
        Paginate.with(compilationsList, this).build()
    }

    fun reload() {
        mPresenter.paginateLoading = false
        mPresenter.lastLoadedCount = 0
        mPresenter.page = 0
        initCompilationsList()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCompilationsList()
        setProgressAnimation(true)

        swipe.setOnRefreshListener {
            swipe.isRefreshing = false
            reload()
        }
    }

    override fun onResumeFragment() {
        runDelayed(300, {
            feedAdapter?.updateCompilationView()

            if (isAuthenticated) {
                if (ApplicationWrapper.instance.isNewUserProfile()) {
                    mPresenter.subscribeCompilations(Settings.getListInt())
                    //reload()
                }
            }
        })
    }

    override fun onPauseFragment() {

    }

    /**
     * view staste functions
     */
    override fun onLoadMore() {
        mPresenter.paginateLoading = true
        mPresenter.loadCompilations()
    }

    override fun isLoading() = mPresenter.paginateLoading

    override fun hasLoadedAllItems(): Boolean {
        if (mPresenter.page == 0 || mPresenter.lastLoadedCount == mPresenter.ITEMS_PER_PAGE) {
            return false
        }
        return true
    }

    override fun onLoadCompilations(list: List<Compilation>) {
        mPresenter.paginateLoading = false
        mPresenter.page++
        mPresenter.lastLoadedCount = list.size

        feedAdapter?.let {
            val arry = Settings.getListInt()
            if (ApplicationWrapper.instance.profile == null && !arry.isEmpty()) {
                arry.forEach {
                    onSubscribe(it)
                }
            }
            it.dataset.addAll(list)
            it.notifyDataSetChanged()
        }
    }

    override fun goToCompilation(compilation: Compilation) {
        val intent = CompilationActivity.getIntent(context, compilation)
        startActivityForResult(intent, CompilationActivity.RESULT_OK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            if (requestCode == CompilationActivity.RESULT_OK) {
                val compilation = it.getSerializableExtra(CompilationActivity.COMPILATION) as Compilation?

                if (compilation!!.isFavorite) {
                    onSubscribe(compilation.id)
                } else {
                    onUnsubscribe(compilation.id)
                }
            }
        }
    }

    override fun goToEditWish(wish: Wish) {
        startActivity(WishEditActivity.getIntent(activity, wish))
    }

    override fun goToWish(wish: Wish, compilation: Compilation) {
        startActivity(WishActivity.getIntent(activity, wish, compilation))
    }

    override fun onSubscribe(compilationId: Int) {
        logEvent("compilation_add", compilationId)
        //TODO: Use the same mechanism as in Unsubscribe
        feedAdapter?.let {
            for (i in 0 until it.dataset.size) {
                val element = it.dataset[i]
                if (element.id == compilationId && !element.isFavorite) {
                    element.isFavorite = true
                    it.notifyItemChanged(i)
                    break
                }
            }
        }
    }

    override fun onUnsubscribe(compilationId: Int) {
        logEvent("compilation_remove", compilationId)
        //TODO: Use the same mechanism as in Subscribe
        feedAdapter?.let {
            for (i in 0 until it.dataset.size) {
                val element = it.dataset[i]
                if (element.id == compilationId && element.isFavorite) {
                    element.isFavorite = false
                    it.notifyItemChanged(i)
                    break
                }
            }
        }
    }

    fun logEvent(event: String, compilationId: Int) {

        val mapCompilation = HashMap<String, Any>()
        mapCompilation.put("compilation_id", compilationId)

        ApplicationWrapper.trackEvent(activity, event, mapCompilation);
    }

    override fun hideAddAnimation(wish: Wish, compilation: Compilation?, adapter: CompilationsWishAdapter?) {
        if (adapter == null) {
            feedAdapter?.let {
                for (i in 0 until it.dataset.size) {
                    val element = it.dataset[i]
                    if (element.id == compilation!!.id && element.isLoading()) {
                        element.setLoading(false)
                        it.notifyItemChanged(i)
                        break
                    }
                }
            }
        } else {
            for (i in 0 until adapter.dataset.size) {
                val element = adapter.dataset[i] as Wish
                if (element.id == wish.id && element.isLoading()) {
                    element.setLoading(false)
                    adapter.notifyItemChanged(i)
                    break
                }
            }
        }
    }

    override fun onAdded(wish: Wish) {
        wishSaveSuccess(wish)

        ApplicationWrapper.trackEvent(activity, "wish_added", mapOf("from" to "compilations"))
    }

    override fun onErrorAdded(wish: Wish) {
        wishSaveFailure(wish)
    }

    override fun scrollOnTop() {
        compilationsList.scrollToPosition(0)
    }

    override fun setProgressAnimation(isEnable: Boolean) {
        (activity as NavigationActivity).progressBar.visible = isEnable
    }
}