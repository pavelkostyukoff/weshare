package com.spacesofting.weshare.domain.usecases

import com.spacesofting.weshare.data.api.Api
import com.spacesofting.weshare.mvp.Autorize
import com.spacesofting.weshare.mvp.Login
import com.spacesofting.weshare.mvp.Profile
import com.spacesofting.weshare.mvp.ResponceProfile
import com.spacesofting.weshare.mvp.model.Mail
import com.spacesofting.weshare.mvp.model.MailComfirm
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import retrofit2.Response

class RegisterNewUserUseCase() {
    fun register(profile: Profile): Observable<ResponceProfile>? {

        // вот это поменять на корутину
        return        Api.Users.register(profile)
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun verifyMail(mail: Mail): Observable<Response<Void>>? {

        // вот это поменять на корутину
        return          Api.Users.verifyMailRequest(mail)
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun confirmMail(mail: MailComfirm?): Observable<Response<Void>>? {

        // вот это поменять на корутину
        return         Api.Users.comfirmMeil(mail)
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun autorize(login: Login): Observable<Autorize>? {

        // вот это поменять на корутину
        return              Api.Auth.autorize(login)
            .observeOn(AndroidSchedulers.mainThread())
    }
}