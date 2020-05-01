package com.spacesofting.weshare.utils

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.pawegio.kandroid.d
import retrofit2.HttpException

/**
 * Created by bender on 28/06/2017.
 */
class ErrorUtils {

    companion object {
        fun parseError(error: Throwable): ServerException {
            var result = ServerException()

            if (error is HttpException) {
                val responseString = error.response()?.errorBody()?.string()
                if (!responseString.isNullOrEmpty()) {
                    try {
                        val parsedError = GsonBuilder().create().fromJson(responseString, ServerException::class.java)
                        if (parsedError != null) result = parsedError
                    } catch (e: JsonSyntaxException) { }
                }
            }
            return result
        }

        inline fun <reified T>parseErrorFields(fields: JsonElement): T {
            d("fields: $fields")
            return GsonBuilder().create().fromJson(fields, object: TypeToken<T>() {}.type)
        }

    }
}