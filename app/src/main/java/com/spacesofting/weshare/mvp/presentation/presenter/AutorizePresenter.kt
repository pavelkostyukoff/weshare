package com.spacesofting.weshare.mvp.presentation.presenter

import android.annotation.SuppressLint
import com.pawegio.kandroid.d
import com.spacesofting.weshare.api.EnumErrorType
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.ScreenPool
import com.spacesofting.weshare.common.Settings
import com.spacesofting.weshare.mvp.Login
import com.spacesofting.weshare.mvp.Profile
import com.spacesofting.weshare.mvp.device.DeviceInfo
import com.spacesofting.weshare.mvp.model.Mail
import com.spacesofting.weshare.mvp.model.MailComfirm
import com.spacesofting.weshare.mvp.model.PasswordResetComfirm
import com.spacesofting.weshare.mvp.presentation.usecases.*
import com.spacesofting.weshare.mvp.presentation.view.AutorizeView
import com.spacesofting.weshare.utils.ErrorUtils
import com.spacesofting.weshare.utils.ServerException
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class AutorizePresenter : MvpPresenter<AutorizeView>() {
    lateinit var deviceInfo: DeviceInfo
    val router = ApplicationWrapper.instance.getRouter()

    val useCaseRegister = RegisterNewUserUseCase()
    val useCaseGetMyProfile = GetMyProfileUseCase()
    val useCaseCheck = RequestMailCheckUseCase()
    val useCaseConfirm = RequestMailConfirmUseCase()
    val useCaseRequest = RequestMailUseCase()

    @SuppressLint("CheckResult")
    fun autorize(mail: Login, isRetryIn: Boolean = false) {
        viewState.showProgress(true)
        useCaseRegister.autorize(mail)
            ?.doFinally { viewState.showProgress(false) }
            ?.subscribe ({
                Settings.accessToken = it.accessToken
                Settings.refreshToken = it.refreshToken
             //   ApplicationWrapper.user = it.user!!
                getProfile()
                //todo тут кладем токен в сохранялки Settings
            }){
              //  it.message?.let { it1 -> viewState.toastError(it1) }
                val error = ErrorUtils.parseError(it)
                parseError(error)
            }
    }

    @SuppressLint("CheckResult")
    fun loginMailRequest(mail: Mail) {
        useCaseRequest.execute(mail)
            ?.subscribe({
                    profile ->
                viewState.showComfiermRequstMailDialog()
              //  router.newRootScreen(ScreenPool.FEED_FRAGMENT)
            }, { e ->
                e
            })
    }

    @SuppressLint("CheckResult")

    fun comfirmRequest(mailComf: MailComfirm) {
        useCaseCheck.execute(mailComf)
            ?.subscribe({
                    profile ->
                viewState.shownewPasswordCreate()
            }, { e ->
                e
            })
    }

    @SuppressLint("CheckResult")
    fun passComfirm(passComf: PasswordResetComfirm) {
        useCaseConfirm.execute(passComf)
            ?.subscribe({
                    profile ->
              //  viewState.showComfiermRequstMailDialog()
             //   router.newRootScreen(ScreenPool.FEED_FRAGMENT)
            }, { e ->
                e
            })
    }
    @SuppressLint("CheckResult")
    private fun getProfile()
        {
            //todo userME
            useCaseGetMyProfile.getAccount()
                        ?.subscribe({
                                profile ->

                            Settings.set(profile)
                            router.newRootScreen(ScreenPool.FEED_FRAGMENT)
                        }, { e ->
                            e.message?.let { viewState.toastError(it) }
                        })
        }
    //todo ТУТТУТ



    @SuppressLint("CheckResult")
    fun registration(profile: Profile, isRetryIn: Boolean = false) {
        viewState.showProgress(true)
        useCaseRegister.register(profile)
            ?.doFinally { viewState.showProgress(false) }
            ?.subscribe ({
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

  /*  fun sendAgainSmsCode(autorize: Autorize) {
        Settings.refreshToken = null
       // autorize(autorize, true)
    }
*/
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
   private fun parseError(error: ServerException) {
       when (error.type) {
           EnumErrorType.REQUIRED_CONFIRMATION -> {
               //  val timeout = TimerUtils().timeToMillis(error.extraFields?.timeout?.elapsed)
             //  Settings.refreshToken = error.extraFields?.validationToken
               //  router.navigateTo(ScreenPool.SMS_CONFIRMATION_FRAGMENT, SmsRegistration(registration, timeout))
           }
           EnumErrorType.MODEL_VALIDATION -> {
               d("ERROR_TYPE", "validation model")
           }
           EnumErrorType.TIMEOUT -> {
               //   val timeout = TimerUtils().timeToMillis(error.extraFields?.timeout?.elapsed)
               //   viewState.showErrorMessage(true, R.string.auth_user_blocked, timeout, true)
           }
           EnumErrorType.ERRORS -> {
               val reasons = error.extraFields?.errors?.get(0)?.reasons?.get(0)
               viewState.toastError(error.message)
               when(error.message)
               {
                   "Too many request to verify email. Try again later."->{
                       viewState.errorDlg("Слишком много запросов проверки mail, попробуйте позже")
                   }
                   "User already exists"-> {
                     //  verifyMailRequest(mail)
                   }
                   "Invalid credentials" -> {
                       viewState.errorDlg("Не верные учетные данные")
                   }
               }
               /*      when(reasons) {
                //   EnumReasons.WRONG_SMS_CODE -> {
               //        viewState.showErrorMessage(true, R.string.auth_wrong_sms_code)
                   }

                   else -> {
                 //      viewState.showErrorMessage(true, R.string.error_general)
                   }*/
           }
       }
   }
}
