package com.spacesofting.weshare.mvp.presentation

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.spacesofting.weshare.api.Api
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.ScreenPool
import com.spacesofting.weshare.common.Settings
import com.spacesofting.weshare.mvp.Autorize
import com.spacesofting.weshare.mvp.Login
import com.spacesofting.weshare.mvp.Profile
import com.spacesofting.weshare.mvp.Refrash
import com.spacesofting.weshare.mvp.device.DeviceInfo
import com.spacesofting.weshare.mvp.model.Mail
import com.spacesofting.weshare.mvp.model.MailComfirm
import com.spacesofting.weshare.mvp.model.PasswordResetComfirm
import com.spacesofting.weshare.mvp.view.AutorizeView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class AutorizePresenter : MvpPresenter<AutorizeView>() {
    lateinit var deviceInfo: DeviceInfo
    val router = ApplicationWrapper.INSTANCE.getRouter()


    fun autorize(mail: Login, isRetryIn: Boolean = false) {
        viewState.showProgress(true)
        Api.Auth.autorize(mail)
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { viewState.showProgress(false) }
            .subscribe ({
                it->
                Settings.AccessToken = it.accessToken
                Settings.ValidationToken = it.rowrefreshTokenVersion
             //   ApplicationWrapper.user = it.user!!
                getProfile()
                //todo тут кладем токен в сохранялки Settings
            }){
                it
                viewState.toastError("Такой пользователь не найден")
            }
    }

    fun loginMailRequest(mail: Mail) {
        Api.Users.reqestMeil(mail)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                    profile ->
                viewState.showComfiermRequstMailDialog()
              //  router.newRootScreen(ScreenPool.FEED_FRAGMENT)
            }, { e ->
                e
            })
    }

    fun comfirmRequest(mailComf: MailComfirm) {
        Api.Users.reqestMailCHeck(mailComf)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                    profile ->
                viewState.shownewPasswordCreate()
            }, { e ->
                e
            })
    }

    fun passComfirm(passComf: PasswordResetComfirm) {
        Api.Users.reqestMailComfirm(passComf)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                    profile ->
              //  viewState.showComfiermRequstMailDialog()
             //   router.newRootScreen(ScreenPool.FEED_FRAGMENT)
            }, { e ->
                e
            })
    }






fun getProfile()
{
  /*  //todo userME
    Api.Users.verifyMailRequest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
                profile ->
          //  ApplicationWrapper.user = profile
            router.newRootScreen(ScreenPool.FEED_FRAGMENT)
        }, { e ->
e
        })*/
}
    //todo ТУТТУТ
    fun refreshed()
    {
        val validationToken = Settings.ValidationToken
        //todo autorize
        val token = validationToken?.let { Refrash(it) }
        token?.let {
            Api.Auth.getNewToken(it)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({ it->
                    Settings.AccessToken = it.accessToken
                    Settings.ValidationToken = it.rowrefreshTokenVersion
                  //  ApplicationWrapper.user = it.user!!
                    //todo тут кладем токен в сохранялки Settings

                }){
                    it
                    //todo 403

                  //  autorize(mail)
                }
        }
    }


    fun registration(profile: Profile, isRetryIn: Boolean = false) {
        viewState.showProgress(true)
        Api.Users.register(profile)
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { viewState.showProgress(false) }
            .subscribe ({
                it
                //todo проходим в основной экран
                router.newRootScreen(ScreenPool.FEED_FRAGMENT)
            }){
                it
              //  it.response().errorBody()
                viewState.toastError("Пароль должен состоять из 8 симовлов , цифр и букв латинского алфавита")

             /*   if (it.cause.  response().errorBody() != null)
                {
                    viewState.showErrorText(ReadUtils.readContent(error.response().errorBody()))
                    error.printStackTrace()
                    viewState.goSpinner(false)
                }*/
                //todo пользователь уже зарегистрирован проходим в аторизацию или диалог
                //router.navigateTo(ScreenPool.BASE_FRAGMENT)
            }

            }

    fun sendAgainSmsCode(autorize: Autorize) {
        Settings.ValidationToken = null
       // autorize(autorize, true)
    }

//todo  чекает есть ли в базе уже этот телефон
   /* fun checkPhoneNumber(phoneNumber: String) {
        viewState.showCheckPhoneProgress(true)
        //TODO: refactoring, set completable
        Api.Auth.checkPhone(Phone(phoneNumber))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when (it.code()) {
                    HttpStatusCode.NO_CONTENT,
                    HttpStatusCode.OK -> {
                        viewState.onCheckedPhone(true)
                        viewState.showCheckPhoneProgress(false)
                    }
                    HttpStatusCode.NOT_FOUND -> {
                        viewState.onCheckedPhone(false)
                        viewState.showCheckPhoneProgress(false)
                    }
                }
            }, { e:Exception ->
                viewState.showErrorMessage(true)
            })
    }*/

   /* fun confirmSmsCode(registration: Autorize, smsCode: String) {
        viewState.showProgress(true)

        Api.Auth.register(registration, smsCode)
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { viewState.showProgress(false) }
            .subscribe({ result ->
                Settings.AccessToken = result.accessToken
                Settings.ValidationToken = null
               // AccountManager.role = result.roles.first()
                router.newRootScreen(ScreenPool.MAIN_FRAGMENT)
            }, { e:Exception ->
                val error = ErrorUtils.parseError(e)
                parseError(error, registration)
            })
    }*/

   /* private fun parseError(error: ServerException, registration: Autorize) {
        when(error.type) {
            EnumErrorType.REQUIRED_CONFIRMATION -> {
                val timeout = TimerUtils().timeToMillis(error.extraFields?.timeout?.elapsed)
                Settings.ValidationToken = error.extraFields?.validationToken
               // router.navigateTo(ScreenPool.SMS_CONFIRMATION_FRAGMENT, SmsRegistration(registration, timeout))
            }
            EnumErrorType.MODEL_VALIDATION -> {
                d("ERROR_TYPE", "validation model")
            }
            EnumErrorType.TIMEOUT -> {
                val timeout = TimerUtils().timeToMillis(error.extraFields?.timeout?.elapsed)
                viewState.showErrorMessage(true, R.string.auth_user_blocked, timeout, true)
            }
            EnumErrorType.ERRORS -> {
                val reasons = error.extraFields?.errors?.get(0)?.reasons?.get(0)

                when(reasons) {
                    EnumReasons.WRONG_SMS_CODE -> {
                        viewState.showErrorMessage(true, R.string.auth_wrong_sms_code)
                    }

                    else -> {
                        viewState.showErrorMessage(true, R.string.error_general)
                    }
                }
            }
        }
    }*/
}
