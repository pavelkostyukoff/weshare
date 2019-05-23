package com.spacesofting.weshare.common


import com.digitalhorizon.eve.api.Api
import com.digitalhorizon.eve.mvp.model.guestcard.GuestCardPriority
import com.spacesofting.weshare.mvp.RoleEnum
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers


object AccountManager {
    var userCard: Any? = null
    private var priorities: ArrayList<GuestCardPriority> = arrayListOf()

    fun getPriorities(): Observable<ArrayList<GuestCardPriority>> {
        val observable = if (!priorities.isEmpty()) {
            Observable.create { emitter ->
                emitter.onNext(priorities)
            }
        } else {
            Api.Users.getGuestCardsPriorities()
                    .map {
                        Observable.create<ArrayList<GuestCardPriority>> { subscriber ->
                            priorities = it
                            subscriber.onComplete()
                        }.subscribe()
                        return@map it
                    }
        }

        return observable.observeOn(AndroidSchedulers.mainThread())
    }

    var role: RoleEnum
        set(value) {
            Settings.Role = value
        }
        get() = Settings.Role
}