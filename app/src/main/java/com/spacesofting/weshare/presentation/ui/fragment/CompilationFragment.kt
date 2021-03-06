package com.spacesofting.weshare.presentation.ui.fragment

import android.os.Bundle
import com.google.android.material.appbar.AppBarLayout
import android.view.View
import com.spacesofting.weshare.presentation.view.CompilationView
import com.spacesofting.weshare.presentation.presenter.CompilationPresenter

import moxy.presenter.InjectPresenter
import com.paginate.Paginate
import com.pawegio.kandroid.visible
import com.spacesofting.weshare.R
import com.spacesofting.weshare.presentation.common.FragmentWrapper
import com.spacesofting.weshare.presentation.mvp.Compilation
import com.spacesofting.weshare.presentation.mvp.Wish
import com.spacesofting.weshare.presentation.ui.adapter.CompilationAdapter
import com.spacesofting.weshare.presentation.ui.adapter.ListWishElement
import com.spacesofting.weshare.presentation.common.utils.ImageUtils
import com.spacesofting.weshare.presentation.common.utils.alphaAnimation
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_compilation.*
import kotlinx.android.synthetic.main.list_item_category.goToCategory

class CompilationFragment : FragmentWrapper(),
    CompilationView, AppBarLayout.OnOffsetChangedListener, Paginate.Callbacks {
    override fun getFragmentLayout(): Int {
        return R.layout.fragment_compilation
    }

    private val PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR: Float = 0.6f
    private val PERCENTAGE_TO_HIDE_TITLE_DETAILS: Float = 0.5f
    private val PERCENTAGE_TO_HIDE_SUBTITLE: Float = 0.3f
    private val PERCENTAGE_TO_HIDE_COUNT: Float = 0.4f
    private val ALPHA_ANIMATIONS_DURATION: Long = 200

    private var isTheTitleVisible: Boolean = false
    private var isTheTitleContainerVisible: Boolean = true
    private var isSubtitleVisible: Boolean = true
    private var isCountVisisble: Boolean = true

    var paginateLoading = false
    var isSubscribeStatusChanged = false
    var adapter: CompilationAdapter? = null

    @InjectPresenter
    lateinit var mPresenter: CompilationPresenter

    companion object {
        val COMPILATION = "compilation"
        val RESULT_OK = 100
        fun getInstance(): CompilationFragment =
            CompilationFragment()
/*
        fun getIntent(context: Context, compilation: Compilation) : Intent {
            val intent = Intent(context, CompilationActivity::class.java)
            intent.putExtra(COMPILATION, compilation)
            return intent
        }*/
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showToolbar(TOOLBAR_EMBEDDED, R.layout.view_fragment_toolbar)
        setHomeAsUpIndicator(TOOLBAR_INDICATOR_HAMBURGER, R.color.black)


        //  mPresenter.compilation = intent.getSerializableExtra(COMPILATION) as Compilation?
        //   initToolbar()
        //hideToolbar()
        //   mPresenter.compilation?.title?.let { setTitle(it) }
        //   compilationCount.text = getString(R.string.compilation_count_idea, mPresenter.compilation?.count)
//        goToCategory.visible = !mPresenter.compilation!!.isFavorite
        //    setAvatarCompilation()
        // compilationTitle.alphaAnimation(0, View.INVISIBLE)
        //   initWishList()

        swipe.setOnRefreshListener {
            swipe.isRefreshing = false
            reload()
        }

        buttonBack.setOnClickListener { onBackPressed() }
//        goToCategory.setOnClickListener { mPresenter.subscribeCompilations() }
//        logEvent("compilation_view", mPresenter.compilation!!.id)
    }


    private fun onBackPressed() {
        if (isSubscribeStatusChanged) {
            /* intent.putExtra(COMPILATION, mPresenter.compilation)
             setResult(RESULT_OK, intent)
             finish()*/
        }

        activity?.onBackPressed()
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, offset: Int) {
        val maxScroll: Float = appBarLayout!!.totalScrollRange.toFloat()
        val percentage: Float = (Math.abs(offset) / maxScroll)

        handleAlphaOnTitle(percentage)
        handleToolbarTitleVisibility(percentage)
        handleSubtitleBlockHide(percentage)
        handleContVisible(percentage)
    }

    private fun handleToolbarTitleVisibility(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!isTheTitleVisible) {
                compilationTitle.alphaAnimation(ALPHA_ANIMATIONS_DURATION, View.VISIBLE)
                isTheTitleVisible = true
            }
        } else {
            if (isTheTitleVisible) {
                compilationTitle.alphaAnimation(ALPHA_ANIMATIONS_DURATION, View.INVISIBLE)
                isTheTitleVisible = false
            }
        }
    }

    private fun handleSubtitleBlockHide(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_HIDE_SUBTITLE) {
            if (isSubtitleVisible) {
                isSubtitleVisible = false
            }
        } else {
            if (!isSubtitleVisible) {
                isSubtitleVisible = true
            }
        }
    }

    private fun handleContVisible(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_HIDE_COUNT) {
            if (isCountVisisble) {
                isCountVisisble = false
            }
        } else {
            if (!isCountVisisble) {
                isCountVisisble = true
            }
        }
    }

    private fun handleAlphaOnTitle(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (isTheTitleContainerVisible) {
                isTheTitleContainerVisible = false
            }
        } else {
            if (!isTheTitleContainerVisible) {
                isTheTitleContainerVisible = true
            }
        }
    }

    private fun initToolbar() {
        /*super.setSupportActionBar(compilationToolbar)
        initAppBar(compilationToolbar)
        supportActionBar?.title = ""*/
    }

    /* private fun setTitle(title: String?){
         compilationTitle.text = title
         compilationSubTitle.text = title
     }*/

    private fun setAvatarCompilation() {
        val imgName = mPresenter.compilation?.photo?.url
        if (imgName == null) {
            avatarCompilation.setImageResource(R.drawable.ic_avatar_placeholder)
        } else {
            Picasso.with(context).load(ImageUtils.resolveImagePath(imgName)).into(avatarCompilation)
        }
    }

    override fun loadWish(list: List<Wish>) {
        paginateLoading = false

        mPresenter.lastLoadedCount = list.size
        mPresenter.paginatePage++

        adapter?.let {
            it.dataset.addAll(list)
            it.notifyDataSetChanged()
        }
    }

    override fun addProgress(item: ListWishElement, isActive: Boolean) {
        adapter?.let {
            val position = it.indexOf(item)
            it.notifyItemChanged(position)
        }
    }

    override fun onLoadMore() {
        paginateLoading = true
        mPresenter.loadWishes()
    }

    override fun isLoading() = paginateLoading

    override fun hasLoadedAllItems(): Boolean {
        if (mPresenter.lastLoadedCount == mPresenter.ITEMS_PER_PAGE || mPresenter.paginatePage == 0) {
            return false
        }
        return true
    }

    override fun wishAdded(wish: Wish) {
        // wishSaveSuccess(wish)
    }

    private fun initWishList() {
        adapter = context?.let { CompilationAdapter(it, mPresenter) }
        wishesList.adapter = adapter
        wishesList.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(context)
        Paginate.with(wishesList, this).build()
    }

    fun reload() {
        paginateLoading = false
        mPresenter.lastLoadedCount = 0
        mPresenter.paginatePage = 0
        initWishList()
    }

    override fun onSubscribe(compilationId: Int) {
        goToCategory.visible = false
        mPresenter.compilation!!.isFavorite = true
        isSubscribeStatusChanged = true

        logEvent("compilation_add", compilationId)
    }

    override fun onUnsubscribe(compilationId: Int) {
        goToCategory.visible = true
        mPresenter.compilation!!.isFavorite = false
        isSubscribeStatusChanged = true

        logEvent("compilation_remove", compilationId)
    }

    fun logEvent(event: String, compilationId: Int) {

        val mapCompilation = HashMap<String, Any>()
        mapCompilation.put("compilation_id", compilationId)

        //  ApplicationWrapper.trackEvent(this, event, mapCompilation);
    }

    override fun hideAddAnimation(wish: Wish) {
        adapter?.let {
            for (i in 0 until it.dataset.size) {
                val element = it.dataset[i]
                if (element.id == wish.id && element.isLoading()) {
                    element.setLoading(false)
                    it.notifyItemChanged(i)
                    break
                }
            }
        }
    }

    override fun onAdded(wish: Wish) {
        // wishSaveSuccess(wish)
        //    ApplicationWrapper.trackEvent(this, "wish_added", mapOf("from" to "compilation"))
    }

    override fun onErrorAdded(wish: Wish) {
        // wishSaveFailure(wish)
    }

    override fun goToWish(wish: Wish, compilation: Compilation) {
        // startActivity(WishActivity.getIntent(this, wish, compilation))
    }
}