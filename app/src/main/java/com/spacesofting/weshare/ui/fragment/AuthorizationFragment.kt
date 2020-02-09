package com.spacesofting.weshare.ui.fragment

import android.content.Context
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.arellomobile.mvp.presenter.InjectPresenter
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
import com.spacesofting.weshare.mvp.presentation.AutorizePresenter
import com.spacesofting.weshare.mvp.view.AutorizeView
import com.spacesofting.weshare.utils.TimerUtils
import kotlinx.android.synthetic.main.activity_wrapper.*
import kotlinx.android.synthetic.main.fragment_authorization.*

class AuthorizationFragment : FragmentWrapper(), AutorizeView {
    val mail = Mail()
    val mailComfirm = MailComfirm()


    private var comfirmDlg: AlertDialog? = null

    override fun toastError(s: String) {
        toast(s)
    }

    override fun showComfiermRequstMailDialog() {
        setCODECOMFIRMTOSERVER(true)    }

    override fun shownewPasswordCreate() {
        mewPasswordCreate(true)
    }

    fun setMailtoSendReset(isVisible: Boolean) {
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
                    .setCancelable(false)
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
                    .setCancelable(false)
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
                }
                else {
                    toast(R.string.password_error)
                }


                    val req = PasswordResetComfirm()
                req.login = mail.login
                req.code = mailComfirm.code





                req.password = view.findViewById<EditText>(R.id.code4)?.text.toString()

                presenter.passCOmfirm(req)
                comfirmDlg?.dismiss()

            }

            comfirmDlg = activity?.let {
                AlertDialog.Builder(it)
                    .setView(view)
                    .setCancelable(false)
                    .show()
            }
        } else {
            comfirmDlg?.dismiss()
        }

    }

    val COUNT_DOWN = 1000L

    companion object {
        fun getInstance() = AuthorizationFragment()
    }

    @InjectPresenter
    lateinit var presenter: AutorizePresenter

    private var countDownTimer: CountDownTimer? = null

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_authorization
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showToolbar(TOOLBAR_HIDE)
        activity?.scan?.visible = false
        password.setText("password") //=  "password"

        presenter.deviceInfo = getDeviceInfo()
        autorize.setOnClickListener {
            //  presenter.onPhoneConfirm(countryCodePicker.fullNumberWithPlus)
           // if (login.text.isNotEmpty() && pass.text.isNotEmpty())
          //  {
                val login = Login()
            login.login = /* "Soprano61" +*/ loginText.text.toString() //+"@yandex.ru"//myMail.text.toString() //
            login.password = password.text.toString()//

            mailAutorize = login
                presenter.autorize(login,false)
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
     //   keyboard.attachEditText(inputNumber)
        //todo botton presenter.registration(Login)
    }

    override fun onCheckedPhone(isValid: Boolean) {
        toRegister.isEnabled = isValid
        showErrorMessage(!isValid, R.string.auth_incorrect_phone)
    }

    override fun onStop() {
        super.onStop()

        countDownTimer?.cancel()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        countDownTimer?.cancel()

    }

    override fun onResume() {
        super.onResume()

        //fix emerging keyboard after recovery state app
      //  activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
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

    override fun showErrorMessage(isVisible: Boolean, msg: Int, countDown: Long, isBlocked: Boolean) {
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

            countDownTimer = TimerUtils().getTimer(context,
                countDown,
                COUNT_DOWN,
                msg,
                listener,
                errorMsg).start()
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
            deviceInfo.id = Settings.Secure.getString(it.contentResolver, Settings.Secure.ANDROID_ID)
            deviceInfo.name = android.os.Build.PRODUCT
            deviceInfo.model = android.os.Build.MODEL
            deviceInfo.vendor = android.os.Build.MANUFACTURER
            deviceInfo.os = OS()

            val wifiManager = it.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInfo = wifiManager.connectionInfo as WifiInfo
            deviceInfo.macs.add(wifiInfo.macAddress)
        }

        return deviceInfo
    }
}