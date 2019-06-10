package com.spacesofting.weshare.ui.fragment.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.pawegio.kandroid.d
import com.spacesofting.weshare.R
import com.spacesofting.weshare.api.Api
import com.spacesofting.weshare.api.HttpStatusCode
import com.spacesofting.weshare.common.AccountManager
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.ScreenPool
import com.spacesofting.weshare.common.Settings
import com.spacesofting.weshare.mvp.Mail
import com.spacesofting.weshare.mvp.Registration
import com.spacesofting.weshare.mvp.device.DeviceInfo
import com.spacesofting.weshare.ui.fragment.presentation.view.RegistrationView
import com.spacesofting.weshare.utils.ErrorUtils
import com.spacesofting.weshare.utils.TimerUtils
import io.reactivex.android.schedulers.AndroidSchedulers

@InjectViewState
class RegistrationPresenter : MvpPresenter<RegistrationView>() {
    lateinit var deviceInfo: DeviceInfo
    val router = ApplicationWrapper.INSTANCE.getRouter()



    fun registration(mail: Mail, isRetryIn: Boolean = false) {
        viewState.showProgress(true)

        Api.Auth.register(mail)
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { viewState.showProgress(false) }
            .subscribe ({
                it

            }){
                it
            }

            }

    fun sendAgainSmsCode(registration: Registration) {
        Settings.ValidationToken = null
       // registration(registration, true)
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

   /* fun confirmSmsCode(registration: Registration, smsCode: String) {
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

   /* private fun parseError(error: ServerException, registration: Registration) {
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
