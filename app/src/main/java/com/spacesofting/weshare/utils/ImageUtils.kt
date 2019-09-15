package com.spacesofting.weshare.utils

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.Drawable
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import com.spacesofting.weshare.api.Api
import com.spacesofting.weshare.common.ApplicationWrapper
import com.spacesofting.weshare.common.Settings
import com.spacesofting.weshare.mvp.Image
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL


class ImageUtils {

    companion object {
        val CACHE = ApplicationWrapper.INSTANCE.cacheDir.path

        fun convertToGrayscale(drawable: Drawable): Drawable {
            val matrix = ColorMatrix()
            matrix.setSaturation(0f)
            drawable.colorFilter = ColorMatrixColorFilter(matrix)
            return drawable
        }

        fun compressPhoto(file: File): File {
            val compressedPhoto = File("$CACHE/${file.name}")
            val imageSize       = file.length().toDouble()/(Settings.THE_SIZE_OF_A_MEGABYTE * Settings.THE_SIZE_OF_A_MEGABYTE).toDouble()
            val scaleFactor     = Math.sqrt((Settings.LIMIT_IMAGE_SIZE / imageSize))
            val bitmap: Bitmap  = BitmapFactory.decodeFile(file.path)
            val outWidthB       = (bitmap.width * scaleFactor).toInt()
            val outHeightB      = (bitmap.height * scaleFactor).toInt()
            val outputStream    = ByteArrayOutputStream()
            val newBitmap       = Bitmap.createScaledBitmap(bitmap, outWidthB, outHeightB,false)
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            val bitmapData      = outputStream.toByteArray()
            val fos             = FileOutputStream(compressedPhoto)
            fos.write(bitmapData)
            fos.flush()
            fos.close()

            return compressedPhoto
        }

        fun resolveImagePath(imageName: String) = "${Settings.ApiPath}$imageName"

        /**
         * Generates Path to image in the cache
         */
        fun savePhotoFile(): File {
            val file = File("$CACHE/${System.currentTimeMillis()}.png") //TODO: check image format
            return file
        }
        fun savePhotoFile(files :String): File {
            val file = File("$files")
            return file
        }

        /**
         * Build File which can be used to save image from camera com.spacesofting.weshare
         */
        fun takePhotoIntent(file: File): Intent {

            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(ApplicationWrapper.INSTANCE.getPackageManager()) != null) {
                // Continue only if the File was successfully created
                //  val photoURI = FileProvider.getUriForFile(ApplicationWrapper.INSTANCE, ApplicationWrapper.INSTANCE.packageName, file)
                val photoURI =
                    FileProvider.getUriForFile(ApplicationWrapper.context, "com.spacesofting.weshare", file)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            }

            return takePictureIntent
        }



        /**
         * Downloads image and saves it into cache
         */
        fun download(url: String): Single<File> {
            val segments = URL(url).file.split("/")
            val fileName = segments[segments.size - 1]
            val file = File("$CACHE/$fileName")
            val observable = Single.create<File>({ emitter ->
                run {
                    val builder = Picasso.Builder(ApplicationWrapper.INSTANCE)
                    builder.listener { picasso, uri, exception ->
                        run {
                            emitter.onError(Exception("Could not load image"))
                            file.delete()
                        }
                    }
                    builder.build()
                        .load(url)
                        .into(object : Target {
                            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
                            override fun onBitmapFailed(errorDrawable: Drawable?) {
                                emitter.onError(Exception("Could not load image"))
                            }

                            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                                try {
                                    if (file.exists()) file.delete()
                                    file.createNewFile()
                                    val ostream = FileOutputStream(file)
                                    bitmap?.compress(Bitmap.CompressFormat.PNG, 80, ostream) //TODO: check image format
                                    ostream.flush()
                                    ostream.close()
                                    emitter.onSuccess(file)
                                } catch (e: Exception) {
                                    emitter.onError(Exception("Could not load image"))
                                    file.delete()
                                }
                            }
                        })
                }
            })
            return observable
        }

        /**
         * Sends image to server. If image from cache then delete it
         */
        fun send(img: File): Observable<Image> {
            //get extension for mimetype
            var extension = img.extension
            if( extension.trim().isEmpty()){
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
                },{
                        e ->
                    //  Log.d(e.message)
                })
            }

            return observable
        }
    }
}
