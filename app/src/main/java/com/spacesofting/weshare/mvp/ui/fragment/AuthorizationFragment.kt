package com.spacesofting.weshare.mvp.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.jakewharton.rxbinding2.widget.RxTextView
import com.pawegio.kandroid.toast
import com.pawegio.kandroid.visible
import com.spacesofting.weshare.R
import com.spacesofting.weshare.common.ApplicationWrapper.Companion.mailAutorize
import com.spacesofting.weshare.common.FragmentWrapper
import com.spacesofting.weshare.common.ScreenPool
import com.spacesofting.weshare.mvp.Login
import com.spacesofting.weshare.mvp.device.DeviceInfo
import com.spacesofting.weshare.mvp.device.OS
import com.spacesofting.weshare.mvp.model.Mail
import com.spacesofting.weshare.mvp.model.MailComfirm
import com.spacesofting.weshare.mvp.model.PasswordResetComfirm
import com.spacesofting.weshare.mvp.presentation.presenter.AutorizePresenter
import com.spacesofting.weshare.mvp.presentation.view.AutorizeView
import com.spacesofting.weshare.utils.TimerUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_wrapper.*
import kotlinx.android.synthetic.main.fragment_authorization.*
import moxy.presenter.InjectPresenter
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class AuthorizationFragment : FragmentWrapper(),
    AutorizeView {
    val mail = Mail()
    val mailComfirm = MailComfirm()

    // val REPLACEMENT = Regex("[^a-zA-Zа-яА-Я0-9-._]")
    val PATTERN = Pattern.compile("[a-zA-Z0-9а-яА-Я_.$%*)(!@:|]{4,32}")

    private var comfirmDlg: AlertDialog? = null

    override fun toastError(s: String) {
        toast(s)
    }


    val COUNT_DOWN = 1000L

    companion object {
        fun getInstance() =
            AuthorizationFragment()
    }
    //todo псевдокод

    @InjectPresenter
    lateinit var presenter: AutorizePresenter
    private var countDownTimer: CountDownTimer? = null

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_authorization
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showToolbar(TOOLBAR_HIDE)
        activity?.scan?.visible = false

        RxTextView.afterTextChangeEvents(loginText)
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { tvChangeEvent ->
                //remove unacceptable symbols
                /* val newValue = loginText.text.toString()?.replace(REPLACEMENT, "")
                 if (newValue != null && newValue != loginText.text.toString()) {*/
                //   loginText.text.replace(0, loginText.text.length, newValue)
                //   }
            }

        RxTextView.afterTextChangeEvents(password)
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { tvChangeEvent ->
                //remove unacceptable symbols
                //  val newValue = password.text.toString()?.replace(REPLACEMENT, "")
                //   if (newValue != null && newValue != password.text.toString()) {
                //    password.text.replace(0, password.text.length, newValue)
                //   }
            }

        password.setText("password") //=  "password"

        presenter.deviceInfo = getDeviceInfo()
        //todo тут происхо
        autorize.setOnClickListener {
            //  presenter.onPhoneConfirm(countryCodePicker.fullNumberWithPlus)
            // if (login.text.isNotEmpty() && pass.text.isNotEmpty())
            //  {
            if (loginText.text.isEmpty() || password.text.isEmpty()) {
                toast("Введите логин и пароль")
            } else {
                val login = Login()
                login.login = /* "Soprano61" +*/
                    loginText.text.toString() //+"@yandex.ru"//myMail.text.toString() //
                login.password = password.text.toString()//

                mailAutorize = login
                presenter.autorize(login, false)
                //  com.spacesofting.weshare.common.Settings.checkAuthorization()
                //todo hide keyboard
            }

            //  }
        }
        restorePassword.setOnClickListener {
            setMailtoSendReset(true)
            //todo open dialog set mail.login
            //todo достаем из профиля - профиль храним открываем диалог с почтой вводим почту - верификация - отправляем запрос
            //todo auth​/reset-password​/request c почтой Mail()
            //todo вызываем диалог с 2 окнами - поле с 4 фицрами - по кнопке ок - отправляем запрос на проверку /auth/reset-password/check
            //todo если ок -> открываем 3й диалог для ввода пароля 2 раза с верификацией и по кнопке ок - отправляем /auth/reset-password/confirm
        }

        toRegister.setOnClickListener {
            router.navigateTo(ScreenPool.REGISTRATION_FRAGMENT)
            //  presenter.onPhoneConfirm(countryCodePicker.fullNumberWithPlus)
            //   if (phone.text.isNotEmpty() && login.text.isNotEmpty())
            //  {
            // presenter.refreshed()
        }
        //     else {
        //        toast(R.string.auth_incorrect_all)
        //    }
        //    }
        //keyboard.attachEditText(inputNumber)
        //todo botton presenter.registration(Login)
    }

    override fun onCheckedPhone(isValid: Boolean) {
        toRegister.isEnabled = isValid
        showErrorMessage(!isValid, R.string.auth_incorrect_phone)
    }

    override fun errorDlg(message: String) {
        activity?.let {
            MaterialDialog.Builder(it)
                .title(R.string.error_general)
                .content(message)
                .positiveText(R.string.ok)
                .onPositive { dialog, which ->
                    //todo   close()
                }
                .neutralText(R.string.edit)
                .show()
        }
    }

    override fun onStop() {
        super.onStop()
        countDownTimer?.cancel()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        countDownTimer?.cancel()
    }

    override fun onResume() {
        super.onResume()
        //fix emerging keyboard after recovery state app
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    /**
     * view state functions
     */

    override fun showProgress(isVisible: Boolean) {
        progressBar.visible = isVisible
        toRegister.isEnabled = !isVisible
    }

    override fun showCheckPhoneProgress(isVisible: Boolean) {
        progressBar.visible = isVisible
    }

    override fun showErrorMessage(
        isVisible: Boolean,
        msg: Int,
        countDown: Long,
        isBlocked: Boolean
    ) {
        errorMsg?.visible = isVisible

        context?.let {
            var color: Int? = null
            if (isVisible) {
                color = ContextCompat.getColor(it, R.color.monza)
            } else {
                color = ContextCompat.getColor(it, R.color.colorPrimaryDark)
            }
            loginText?.setTextColor(color)
            // firstName?.setTextColor(color)
        }

        if (isBlocked) {
            countDownTimer?.cancel()

            val listener = object : TimerUtils.OnTimerFinishListener {
                override fun onFinish() {
                    showErrorMessage(false)
                }
            }

            countDownTimer = TimerUtils().getTimer(
                context,
                countDown,
                COUNT_DOWN,
                msg,
                listener,
                errorMsg
            ).start()
        } else {
            countDownTimer?.cancel()
            errorMsg?.text = if (msg == -1) "" else getText(msg)
        }
    }

    override fun showCountDownRetrySms(countDown: Long) {}

    /**
     * other functions
     */

    private fun getDeviceInfo(): DeviceInfo {
        val deviceInfo = DeviceInfo()

        activity?.let {
            deviceInfo.id =
                Settings.Secure.getString(it.contentResolver, Settings.Secure.ANDROID_ID)
            deviceInfo.name = android.os.Build.PRODUCT
            deviceInfo.model = android.os.Build.MODEL
            deviceInfo.vendor = android.os.Build.MANUFACTURER
            deviceInfo.os = OS()

            val wifiManager =
                it.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInfo = wifiManager.connectionInfo as WifiInfo
            deviceInfo.macs.add(wifiInfo.macAddress)
        }

        return deviceInfo
    }

    fun isValid(): Boolean {
        /*    if(profileNew.firstName != null) {
            return PATTERN.matcher(profileNew.firstName).matches()
        }else{
            return false
        }*/
        return false
    }

    override fun showComfiermRequstMailDialog() {
        setCODECOMFIRMTOSERVER(true)
    }

    override fun shownewPasswordCreate() {
        mewPasswordCreate(true)
    }

    private fun setMailtoSendReset(isVisible: Boolean) {
        if (isVisible) {
            val view = LayoutInflater.from(activity).inflate(R.layout.confirm_dialog, null)
            view.findViewById<EditText>(R.id.code)?.hint = "Введите свой email"
            view.findViewById<Button>(R.id.ok).setOnClickListener {
                mail.login = view.findViewById<EditText>(R.id.code)?.text.toString()
                presenter.loginMailRequest(mail)
                comfirmDlg?.dismiss()
            }

            comfirmDlg = activity?.let {
                AlertDialog.Builder(it)
                    .setView(view)
                    //  .setCancelable(false)
                    .show()
            }
        } else {
            comfirmDlg?.dismiss()
        }
    }

    fun setCODECOMFIRMTOSERVER(isVisible: Boolean) {
        if (isVisible) {
            val view = LayoutInflater.from(activity).inflate(R.layout.confirm_dialog, null)
            view.findViewById<EditText>(R.id.code)?.hint = "Введите код подтверждения четыре цифры"
            view.findViewById<Button>(R.id.ok).setOnClickListener {
                //mail.login = view.findViewById<EditText>(R.id.code)?.text.toString()
                mailComfirm.code = view.findViewById<EditText>(R.id.code)?.text.toString()
                mailComfirm.login = mail.login
                presenter.comfirmRequest(mailComfirm)
                comfirmDlg?.dismiss()
            }

            comfirmDlg = activity?.let {
                AlertDialog.Builder(it)
                    .setView(view)
                    //    .setCancelable(false)
                    .show()
            }
        } else {
            comfirmDlg?.dismiss()
        }
    }

    fun mewPasswordCreate(isVisible: Boolean) {
        if (isVisible) {
            val view = LayoutInflater.from(activity).inflate(R.layout.new_password_create, null)
            val test1 = view.findViewById<EditText>(R.id.code)?.text.toString()
            val test2 = view.findViewById<EditText>(R.id.code4)?.text.toString()




            view.findViewById<Button>(R.id.ok).setOnClickListener {

                if (test1.isNotEmpty() || test2.isNotEmpty()) {
                    test1
                    test2
                } else {
                    toast(R.string.password_error)
                }


                val req = PasswordResetComfirm()
                req.login = mail.login
                req.code = mailComfirm.code





                req.password = view.findViewById<EditText>(R.id.code4)?.text.toString()

                presenter.passComfirm(req)
                comfirmDlg?.dismiss()

            }

            comfirmDlg = activity?.let {
                AlertDialog.Builder(it)
                    .setView(view)
                    //  .setCancelable(false)
                    .show()
            }
        } else {
            comfirmDlg?.dismiss()
        }

    }
}