package com.spacesofting.weshare.mvp.presentation

import android.annotation.SuppressLint
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.spacesofting.weshare.api.Api
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.mvp.UpdateProfile
import com.spacesofting.weshare.mvp.view.ProfileEditView
import com.spacesofting.weshare.ui.fragment.ProfileEditFragment.Companion.SCANNER_REQUEST_CODE
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File

@InjectViewState
class ProfileEditPresenter : MvpPresenter<ProfileEditView>() {
    val router = ApplicationWrapper.INSTANCE.getRouter()
    var photoFile: File? = null

    @SuppressLint("CheckResult")
    fun savePhoto() {
        viewState.showProgress(true)
        photoFile?.let {
            Api.Users.updateAvatar(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewState.showProgress(false)
                    viewState.setPhotoForPicasso(it)
                    // router.newRootScreen(ScreenPool.APPROVE_LIST_FRAGMENT)
                }, { e ->
                    viewState.showProgress(false)
                })
        }

        /*photoFile?.let { photoFile ->
            ImageUtils.send(photoFile)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ photo ->
                    viewState.showProgress(false)
                   // cardRequest?.photo?.pictureId = photo.pictureId
                   // card?.photo = photo
                  //  viewState.getCardFields()
                  //  saveCard()
                }, {
                    viewState.showProgress(false)
                    ////viewState.showToast(R.string.error_general)
                })
        }*/
    }

    /*fun saveCard() {
        viewState.showProgress(true)
        Api.Users.sendGuestCard(card?.id!!, cardRequest, card?.rowVersion)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewState.showProgress(false)
                card = it
                viewState.showToast(R.string.edit_guest_card_successfully_saved)
                router.newRootScreen(ScreenPool.APPROVE_LIST_FRAGMENT)
            }, { e ->
                viewState.showProgress(false)
                viewState.showToast(R.string.error_general)
            })
    }*/

    //todo функция на запрос нового профиля
    //todo получаем ответ 200 - показваем тост что все ок и вызываем функцию во вью и закрываем окно

    fun chengeMyProfile(updProfile: UpdateProfile) {
        viewState.showProgress(true)

        Api.Users.updateProfile(updProfile)
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { viewState.showProgress(false) }
            .subscribe ({
                it
                ApplicationWrapper.user = it
                viewState.showNewInfo(it)
                //todo проходим в основной экран
                //todo тут может отправить во вью и показать тост
                router.exitWithResult(SCANNER_REQUEST_CODE, it)
            }){
                it
                router.exit()
                //todo пользователь уже зарегистрирован проходим в аторизацию или диалог
                //router.navigateTo(ScreenPool.BASE_FRAGMENT)
            }

    }

}
