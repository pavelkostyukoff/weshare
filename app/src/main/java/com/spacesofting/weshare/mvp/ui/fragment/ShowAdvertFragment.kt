package com.spacesofting.weshare.mvp.ui.fragment

import android.os.Bundle
import com.google.android.material.appbar.AppBarLayout
import android.view.View
import moxy.presenter.InjectPresenter
import com.paginate.Paginate
import com.spacesofting.weshare.R
import com.spacesofting.weshare.common.FragmentWrapper
import com.spacesofting.weshare.mvp.DatumRequast
import com.spacesofting.weshare.mvp.Wish
import com.spacesofting.weshare.mvp.model.dto.AdvertModifiredRespouns
import com.spacesofting.weshare.mvp.presentation.presenter.ShowAdvertPresenter
import com.spacesofting.weshare.mvp.presentation.view.ShowAdvertView
import com.spacesofting.weshare.mvp.ui.adapter.ListWishElement
import kotlinx.android.synthetic.main.fragment_show_advert.*

class ShowAdvertFragment : FragmentWrapper(),
    ShowAdvertView, AppBarLayout.OnOffsetChangedListener, Paginate.Callbacks {
    override fun getFragmentLayout(): Int {
        return R.layout.fragment_show_advert
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

    @InjectPresenter
    lateinit var mPresenter: ShowAdvertPresenter

    companion object {
        private const val DATA_KEY_STR = "string"
        fun getInstance(id: DatumRequast): ShowAdvertFragment {
            val fragment = ShowAdvertFragment()
            /*   smsRegistration?.let {
                   val argument = Bundle()
                   argument.putSerializable(DATA_KEY, it)
                   fragment.arguments = argument
               }*/
            //todo сюда мы принимаем вещь из редактирвоания
            id.let {
                val argument = Bundle()
                argument.putSerializable(DATA_KEY_STR, it)
                fragment.arguments = argument
            }
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showToolbar(TOOLBAR_EMBEDDED, R.layout.view_fragment_toolbar)
        setHomeAsUpIndicator(TOOLBAR_INDICATOR_HAMBURGER, R.color.black)
        val datum = arguments?.getSerializable(ShowAdvertFragment.DATA_KEY_STR) as DatumRequast
        datum.id?.let { mPresenter.showAdvertFolds(it) }

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
 /*       val imgName = mPresenter.compilation?.photo?.url
        if (imgName == null) {*/
            avatarCompilation.setImageResource(R.drawable.ic_avatar_placeholder)
     //   } else {
         //   Picasso.with(context).load(ImageUtils.resolveImagePath(imgName)).into(avatarCompilation)
      //  }
    }

    override fun loadWish(list: List<Wish>) {
        paginateLoading = false

        mPresenter.lastLoadedCount = list.size
        mPresenter.paginatePage++

    }

    override fun addProgress(item: ListWishElement, isActive: Boolean) {
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

    override fun setAdvertFold(advertRespouns: AdvertModifiredRespouns) {
        advertRespouns
    }

    fun reload() {
        paginateLoading = false
        mPresenter.lastLoadedCount = 0
        mPresenter.paginatePage = 0
    }

    fun logEvent(event: String, compilationId: Int) {

        val mapCompilation = HashMap<String, Any>()
        mapCompilation.put("compilation_id", compilationId)

        //  ApplicationWrapper.trackEvent(this, event, mapCompilation);
    }

    override fun hideAddAnimation(wish: Wish) {
    }

    override fun onAdded(wish: Wish) {
        // wishSaveSuccess(wish)
        //    ApplicationWrapper.trackEvent(this, "wish_added", mapOf("from" to "compilation"))
    }

    override fun onErrorAdded(wish: Wish) {
        // wishSaveFailure(wish)
    }
}