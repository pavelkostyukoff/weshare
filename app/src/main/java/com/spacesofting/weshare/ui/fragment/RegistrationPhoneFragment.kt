package com.spacesofting.weshare.ui.fragment

import android.content.Context
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.WindowManager
import com.spacesofting.weshare.mvp.view.RegistrationView

import com.arellomobile.mvp.presenter.InjectPresenter
import com.pawegio.kandroid.toast
import com.pawegio.kandroid.visible
import com.spacesofting.weshare.R
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.FragmentWrapper
import com.spacesofting.weshare.mvp.Login
import com.spacesofting.weshare.mvp.Profile
import com.spacesofting.weshare.mvp.device.DeviceInfo
import com.spacesofting.weshare.mvp.device.OS
import com.spacesofting.weshare.mvp.presentation.RegistrationPresenter
import com.spacesofting.weshare.utils.TimerUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_registration_phone.*
import kotlinx.android.synthetic.main.view_bag_my_info.*

class RegistrationPhoneFragment : FragmentWrapper(), RegistrationView {


    override fun toastError(s: String) {
        toast("Пароль должен состоять из 8 симовлов , цифр и букв латинского алфавита")
    }

    val COUNT_DOWN = 1000L

    companion object {
        fun getInstance() = RegistrationPhoneFragment()
    }

    @InjectPresenter
    lateinit var presenter: RegistrationPresenter

    private var countDownTimer: CountDownTimer? = null

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_registration_phone
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showToolbar(TOOLBAR_HIDE)

        presenter.deviceInfo = getDeviceInfo()
        autorize.setOnClickListener {
            //  presenter.onPhoneConfirm(countryCodePicker.fullNumberWithPlus)

           // if (login.text.isNotEmpty() && pass.text.isNotEmpty())
          //  {
                val mail = Login()
            mail.email = myMail.text.toString()
            mail.password = pass.text.toString()
                //val profile = Profile("+79776554741", login.text.toString(),"K1o2I2P412345","test","test","2010-01-01")
                //   val profile = Login(login.text.toString(), pass.text.toString())

                // presenter.autorize(mail,false)
                presenter.autorize(mail,false)
          //  }

        }


        toRegister.setOnClickListener {
          //  presenter.onPhoneConfirm(countryCodePicker.fullNumberWithPlus)

            if (myMail.text.isNotEmpty() && pass.text.isNotEmpty())
            {
                val profile = Profile()
                profile.firstName = nickName.text.toString()
                profile.password = pass.text.toString()
                profile.email = myMail.text.toString()
                presenter.registration(profile,false)
               // presenter.refreshed()
            }
            else {
                toast(R.string.auth_incorrect_all)
            }

        }

     //   keyboard.attachEditText(inputNumber)

        //todo botton presenter.registration(Login)



    }

    override fun onCheckedPhone(isValid: Boolean) {
        toRegister.isEnabled = isValid
        showErrorMessage(!isValid, R.string.auth_incorrect_phone)
    }

    override fun onDetach() {
        super.onDetach()
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

            myMail?.setTextColor(color)
            pass?.setTextColor(color)
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