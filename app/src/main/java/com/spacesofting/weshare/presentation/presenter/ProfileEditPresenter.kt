package com.spacesofting.weshare.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.spacesofting.weshare.api.Api
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.mvp.UpdateProfile
import com.spacesofting.weshare.presentation.view.ProfileEditView
import io.reactivex.android.schedulers.AndroidSchedulers

@InjectViewState
class ProfileEditPresenter : MvpPresenter<ProfileEditView>() {
    val router = ApplicationWrapper.INSTANCE.getRouter()


    //todo функция на запрос нового профиля
    //todo получаем ответ 200 - показваем тост что все ок и вызываем функцию во вью и закрываем окно

    fun chengeMyProfile(updProfile: UpdateProfile) {
        viewState.showProgress(true)

        Api.Users.updateProfile(updProfile)
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { viewState.showProgress(false) }
            .subscribe ({
                it
                //todo проходим в основной экран
                //todo тут может отправить во вью и показать тост
                router.exit()

            }){
                it
                router.exit()

                //todo пользователь уже зарегистрирован проходим в аторизацию или диалог
                //router.navigateTo(ScreenPool.BASE_FRAGMENT)
            }

    }

}
