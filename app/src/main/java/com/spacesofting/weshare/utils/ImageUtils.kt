package com.spacesofting.weshare.utils

import com.spacesofting.weshare.api.Api
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.Settings
import com.spacesofting.weshare.mvp.model.Photo
import com.pawegio.kandroid.d
import com.spacesofting.weshare.utils.default
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


object ImageUtils {

    val CACHE = ApplicationWrapper.INSTANCE.cacheDir.path

    private var baseImageUrl = ""

    fun saveBaseImageUrl() {
     /*   if (baseImageUrl.isEmpty() && Settings.IsAuthorized) {
            Api.Pictures.getImagePath()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ url ->
                        baseImageUrl = url.imagePath
                    }, { err ->
                        d("Base_url_error", err.message.toString())
                    })
        }*/
    }

    fun getImagePath(imageName: String) = baseImageUrl + imageName

    fun send(img: File): Observable<Photo> {
        //get extension for mimetype
        var extension = img.extension
        if (extension.trim().isEmpty()) {
            extension = "*"
        }

        //prepare body
        val file = RequestBody.create(MediaType.parse("image/${extension}"), img)
        val body = MultipartBody.Part.createFormData("pictureFile", img.name, file)

        val observable = Api.Pictures.addPicture(body).default().share()

        //remove file on success if it stored in cache directory
        if (img.path != img.path) {
            observable.subscribe({ img.delete() })
        }

        if (img.path.contains(CACHE)) {
            observable.subscribe({ AddPictureResponse ->
                img.delete()
            }, { e ->
                //  Log.d(e.message)
            })
        }
        return observable
    }
}