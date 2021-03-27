package com.spacesofting.weshare.mvp.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import moxy.presenter.InjectPresenter
import com.paginate.Paginate
import com.pawegio.kandroid.visible
import com.spacesofting.weshare.R
import com.spacesofting.weshare.common.FragmentWrapper
import com.spacesofting.weshare.common.ScreenPool
import com.spacesofting.weshare.mvp.Datum
import com.spacesofting.weshare.mvp.Wish
import com.spacesofting.weshare.mvp.presentation.presenter.FeedChaosPresenter
import com.spacesofting.weshare.mvp.presentation.view.FeedChaosView
import com.spacesofting.weshare.mvp.ui.adapter.FeedChaosAdapter
import com.spacesofting.weshare.mvp.ui.adapter.FeedCompilationsAdapter
import kotlinx.android.synthetic.main.activity_wrapper.*
import kotlinx.android.synthetic.main.fragment_feed_compilations.*

class FeedChaosFragment : FragmentWrapper(),
    FeedChaosView, Paginate.Callbacks {


    override fun getFragmentLayout(): Int {
        return R.layout.fragment_feed_chaos
    }

    var feedAdapter: FeedChaosAdapter? = null
    var isAuthenticated = true //Settings.isAuthenticated()

    @InjectPresenter
    lateinit var mPresenter: FeedChaosPresenter

    companion object {
        fun getInstance(): FeedCompilationsFragment =
            FeedCompilationsFragment()
    }

    private fun initCompilationsList() {
        feedAdapter = FeedChaosAdapter(activity as Context, mPresenter)
        compilationsList.adapter = feedAdapter
        compilationsList.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(activity)
        Paginate.with(compilationsList, this).build()
    }

    fun reload() {
        mPresenter.paginateLoading = false
        mPresenter.lastLoadedCount = 0
        mPresenter.page = 0
        initCompilationsList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.let { super.onViewCreated(it, savedInstanceState) }
        setToolbarBackgroundDrawable(R.drawable.bg_vertical_gradient)
        setTitleColor(R.color.white)
        showToolbar(TOOLBAR_EMBEDDED, R.layout.view_toolbar_with_search_filter)
        showAvatar()
        initCompilationsList()
        activity?.scan?.visible = true
        //reload()
        setProgressAnimation(true)
        swipe.setOnRefreshListener {
            swipe.isRefreshing = false
            reload()
        }
    }

    /*   override fun onResumeFragment() {
           runDelayed(300, {
               feedAdapter?.updateCompilationView()

               if (isAuthenticated) {
                   //todo тут при подсоединениее фрагмента после авторизации смотрим был ли подписан наш человечек к подборкам
                   // todo и если да то метод дает список подборок после чего мы их красим что подписаны все
                   if (ApplicationWrapper.instance.isNewUserProfile()) {
                       mPresenter.subscribeCompilations(Settings.getListInt())
                       //reload()
                   }
               }
           })
       }*/

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

    //todo подборка категорий с сервера
    override fun onLoadCompilations(list: List<Datum>) {
        mPresenter.paginateLoading = false
        mPresenter.page++
        mPresenter.lastLoadedCount = list.size
        feedAdapter?.let {
            //todo в коде ниже происходит понимание подписки
            /*val arry = Settings.getListInt()
            if (ApplicationWrapper.instance.profile == null && !arry.isEmpty()) {
                arry.forEach {
                    onSubscribe(it)
                }
            }*/
            it.dataset.addAll(list)
            it.notifyDataSetChanged()
        }
    }

    //todo тут мы переходим в понравившуюся нам категорию
    override fun goToCompilation(compilation: Datum) {
        router.navigateTo(ScreenPool.SHOW_CATEGORY)
        /*val intent = CompilationActivity.getIntent(context, compilation)
        startActivityForResult(intent, CompilationActivity.RESULT_OK)*/
    }

    //todo после перехода нам что то понравилось мы чекнули и по возвращении говорим что да это моя категория подписаться
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /* data?.let {
             if (requestCode == CompilationActivity.RESULT_OK) {
                 val compilation = it.getSerializableExtra(CompilationActivity.COMPILATION) as Compilation?

                 if (compilation!!.isFavorite) {
                     onSubscribe(compilation.id)
                 } else {
                     onUnsubscribe(compilation.id)
                 }
             }
         }*/
    }

    //todo редактирования вещи
    override fun goToEditWish(wish: Wish) {
        // startActivity(WishEditActivity.getIntent(activity, wish))
    }

    //todo открыть вещь
    override fun goToWish(wish: Wish, compilation: Datum) {
        // startActivity(WishActivity.getIntent(activity, wish, compilation))
    }

    //todo подписаться на категорию она всегда висит в топе
    override fun onSubscribe(compilationId: Int) {
        logEvent("compilation_add", compilationId)
        //TODO: Use the same mechanism as in Unsubscribe
        feedAdapter?.let {
            for (i in 0 until it.dataset.size) {
                val element = it.dataset[i]
                /*    if (element.id == compilationId && !element.isFavorite) {
                        element.isFavorite = true
                        it.notifyItemChanged(i)
                        break
                    }*/
            }
        }
    }

    override fun onUnsubscribe(compilationId: Int) {
        logEvent("compilation_remove", compilationId)
        //TODO: Use the same mechanism as in Subscribe
        feedAdapter?.let {
            for (i in 0 until it.dataset.size) {
                val element = it.dataset[i]
                /*  if (element.id == compilationId && element.isFavorite) {
                      element.isFavorite = false
                      it.notifyItemChanged(i)
                      break
                  }*/
            }
        }
    }

    fun logEvent(event: String, compilationId: Int) {

        val mapCompilation = HashMap<String, Any>()
        mapCompilation.put("compilation_id", compilationId)
        // ApplicationWrapper.trackEvent(activity, event, mapCompilation)
    }

    override fun hideAddAnimation(
        wish: Wish,
        compilation: Datum?,
        adapter: FeedCompilationsAdapter?
    ) {
        if (adapter == null) {
            feedAdapter?.let {
                for (i in 0 until it.dataset.size) {
                    val element = it.dataset[i]
                    if (!(element.id != compilation!!.id)) {
                        // element.setLoading(false)
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

    //todo добавить вещь к себе - как это может пригодитсья нам? возможно позже
    override fun onAdded(wish: Wish) {
        /*   wishSaveSuccess(wish)

           ApplicationWrapper.trackEvent(activity, "wish_added", mapOf("from" to "compilations"))*/
    }

    //todo ошибка при добавлении
    override fun onErrorAdded(wish: Wish) {
        // wishSaveFailure(wish)
    }

    override fun scrollOnTop() {
        compilationsList.scrollToPosition(0)
    }

    //todo крутилка крутилочка крутиться крутит
    override fun setProgressAnimation(isEnable: Boolean) {
        //  (activity as NavigationActivity).progressBar.visible = isEnable
    }

    fun showAvatar() {
       /* if (ApplicationWrapper.user.avatar != null) {
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
}