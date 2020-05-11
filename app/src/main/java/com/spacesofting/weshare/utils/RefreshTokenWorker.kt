package com.spacesofting.weshare.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.spacesofting.weshare.R
import com.spacesofting.weshare.api.Api
import com.spacesofting.weshare.common.Settings
import com.spacesofting.weshare.mvp.Refrash
import io.reactivex.android.schedulers.AndroidSchedulers


class RefreshTokenWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    private val TAG: String = RefreshTokenWorker::class.java.simpleName

    override fun doWork(): Result {

        sendNotification("Title", "Details")
        return Result.success()

    }


    private fun sendNotification(title: String, message: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //If on Oreo then notification required a notification channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, "default")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher)

        notificationManager.notify(1, notification.build())
    }

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
}