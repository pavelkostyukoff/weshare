package com.spacesofting.weshare.utils

import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.spacesofting.weshare.api.Api
import com.spacesofting.weshare.common.Settings
import com.spacesofting.weshare.mvp.Refrash
import com.spacesofting.weshare.mvp.ui.fragment.AddGoodsFragment
import io.reactivex.android.schedulers.AndroidSchedulers


class RefreshTokenWorker(@NonNull context: Context?, @NonNull workerParams: WorkerParameters?) :
    Worker(context!!, workerParams!!) {
    @NonNull
    override fun doWork(): Result {
        Log.d(AddGoodsFragment.TAG, "onCreateViewHolder_1")

        refreshed()
    /*    val data = inputData
        val desc = data.getString(MainActivity.KEY_TASK_DESC)
        //displayNotification("Hey I am your work", desc)
        refrashTiken()
        val data1 = Data.Builder()
            .putString(KEY_TASK_OUTPUT, "Task Finished Successfully")
            .build()
        setOutputData(data1)*/
        return Result.success()
    }

    /*private fun displayNotification(task: String, desc: String?) {
        val manager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "simplifiedcoding",
                "simplifiedcoding",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }
        val builder: NotificationCompat.Builder =
            Builder(applicationContext, "simplifiedcoding")
                .setContentTitle(task)
                .setContentText(desc)
                .setSmallIcon(R.mipmap.ic_launcher)
        manager.notify(1, builder.build())
    }*/

   /* private fun refrashTiken() {
        Api.Users.getAccount()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it
                Log.d(AddGoodsFragment.TAG, "onCreateViewHolder_2")
            })
            {
                Log.d(AddGoodsFragment.TAG, "onCreateViewHolder_3")
            }

    }*/

    companion object {
        const val KEY_TASK_OUTPUT = "key_task_output"
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