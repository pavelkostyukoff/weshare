package com.spacesofting.weshare.common

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.text.TextUtils
import com.google.gson.Gson
import com.spacesofting.weshare.BuildConfig
import com.spacesofting.weshare.mvp.RoleEnum
import com.spacesofting.weshare.mvp.User
import com.spacesofting.weshare.mvp.model.guestcard.GuestCardPriority
import com.spacesofting.weshare.mvp.model.guestcard.GuestCardState
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.URLConnection
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet
import kotlin.collections.LinkedHashMap


object Settings {

    private val PREFERENCES = PreferenceManager.getDefaultSharedPreferences(ApplicationWrapper.instance)
    private val KEY_API_NAME = "key_api_name"
    private val KEY_ACCESS_TOKEN = "key_access_token"
    private val KEY_VALIDATION_TOKEN = "key_validation_token"
    private val SAVE_COMPILATIONS = "save_compilations"
    private val PROFILE = "profile"
    private val KEY_ACCESS_TOKEN_ANONYMOUS = "key_access_token_anon"
    private val IS_A_ANONYMOUS = "is_anonymous_user"
    private val KEY_ROLE = "key_role"
    val LIMIT_IMAGE_SIZE = 1
    val THE_SIZE_OF_A_MEGABYTE = 1024//920///1024

    private var isAuthenticated = false
    var isAnonymousUser: Boolean
        get() = PREFERENCES.getBoolean(IS_A_ANONYMOUS, false)
        set(value) {
            PREFERENCES.edit().putBoolean(IS_A_ANONYMOUS, value).apply()
        }
/*
    init {
        isAuthorized = AccessToken != null

    }*/
/*var myObject =  User()
    var profile: String
        get() = PREFERENCES.getString(PROFILE, "")!!

        set(value) {
            PREFERENCES.edit().putString(PROFILE, value).apply()
        }*/

    /*fun getProfile(): User? {
       *//* val gson = Gson()
        val json = PREFERENCES.getString("MyObject", "")
        return gson.fromJson(json, User::class.java)*//*
    }*/



    fun setProfile(pr : User) {
      /*  val gson =  Gson();
        val json = gson.toJson(pr);
        PREFERENCES.edit().putString("MyObject", json);
        PREFERENCES.edit().commit();*/
    }

    var apiPathName: String
        get() = PREFERENCES.getString(KEY_API_NAME, "PROD")!!
        set(value) {
            PREFERENCES.edit().putString(KEY_API_NAME, value).apply()
        }

    fun get(): User? {
        val gson = Gson()
        val json = PREFERENCES.getString(PROFILE, "")
        return gson.fromJson(json, User::class.java)
    }

    fun set(user : User) {
        val gson = Gson()
        val json = gson.toJson(user);
        profi = json
    }

    var profi: String
        get() = PREFERENCES.getString(PROFILE, "PROD")!!
        set(value) {
            PREFERENCES.edit().putString(PROFILE, value).apply()
        }

    fun saveData(
    )
    {

    }


    fun getData(
        con: Context?,
        variable: String?,
        defaultValue: String?
    ): String? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(con)
        return prefs.getString(variable, defaultValue)
    }
    var ApiPath: String
        get() {
            val path = BuildConfig.API_PATHS[apiPathName]
            if (path != null) {
                return path
            } else {
                return BuildConfig.API_PATHS["test"]!!
            }
        }
        set(value) {
            BuildConfig.API_PATHS[apiPathName] = value
        }


/*
    var PicPath: String = ""
        get() {
            val path = BuildConfig.API_PATHS[apiPathName]
            if (path != null) {
                return path
            } else {
                return BuildConfig.API_PATHS[BuildConfig.DEFAULT_API_NAME]!!
            }
        }*/
//use for check authorization status

    fun checkAuthorization() {
        isAuthenticated = AccessToken != null /*|| AccessTokenAnonymous != null*/
    }

    private fun decrypt(key: String): String? {
        val encrypted = PREFERENCES.getString(key, null)
       // val decrypted = SecureUtils.decrypt(key)

        return encrypted
    }

    //use for check authorization status
    fun isAuthenticated() = isAuthenticated

    var AccessToken: String?
        get() = decrypt(KEY_ACCESS_TOKEN)
        set(value) {
            encrypt(KEY_ACCESS_TOKEN, value)
          //  IsAuthorized = true
        }

    var ValidationToken: String?
        get() = decrypt(KEY_VALIDATION_TOKEN)
        set(value) {
            encrypt(KEY_VALIDATION_TOKEN, value)
        }

    private fun encrypt(key: String, value: String?) {
      //  val encrypted: String? = SecureUtils.encrypt(value)
        PREFERENCES.edit().putString(key, value).apply()
    }


    var Role: RoleEnum
        set(value) {
            PREFERENCES.edit().putString(KEY_ROLE, value.name).apply()
        }
        get() {
            return when(PREFERENCES.getString(KEY_ROLE, RoleEnum.NONE.name)) {
                RoleEnum.ADMINISTRATOR.name -> RoleEnum.ADMINISTRATOR
                RoleEnum.GUEST.name -> RoleEnum.GUEST
                RoleEnum.GUEST_MANAGER.name -> RoleEnum.GUEST_MANAGER
                RoleEnum.EVENT_MANAGER.name -> RoleEnum.EVENT_MANAGER
                RoleEnum.GUEST_RESPONSIBLE.name -> RoleEnum.GUEST_RESPONSIBLE
                RoleEnum.STRUCTURE_UNIT_OFFICER.name -> RoleEnum.STRUCTURE_UNIT_OFFICER
                RoleEnum.SECURITY.name -> RoleEnum.SECURITY
                else -> RoleEnum.NONE
            }
        }

    fun logout() {
        AccessToken = null
        ValidationToken = null
        isAuthenticated = false
    }
 /*   private var apiPathName: String
        get() = PREFERENCES.getString(KEY_API_NAME, BuildConfig.DEFAULT_API_NAME)
        set(value) {
            PREFERENCES.edit().putString(KEY_API_NAME, value).apply()
        }*/
/*
    private fun decrypt(key: String): String? {
        val encrypted = PREFERENCES.getString(key, null)
        val decrypted = SecureUtils.decrypt(encrypted)

        return decrypted
    }*/

   /* private fun encrypt(key: String, value: String?) {
        val encrypted: String? = SecureUtils.encrypt(value)
        PREFERENCES.edit().putString(key, encrypted).apply()
    }*/

    fun SaveLinkedHashMapToInternalStorage(SavedData: String, linkedHashMapList: LinkedHashMap<GuestCardState, Boolean>, context:Context) {
        try {
            val fos = context.openFileOutput(SavedData, Context.MODE_PRIVATE)
            val s = ObjectOutputStream(fos)
            s.writeObject(linkedHashMapList)
            s.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun LoadLinkedHashMapFromInternalStorage(savedData: String, context:Context): LinkedHashMap<GuestCardState, Boolean> {
        var linkedHashMapLIST = LinkedHashMap<GuestCardState, Boolean>()
        try {
            val fileInputStream = context.openFileInput(savedData)
            val objectInputStream = ObjectInputStream(fileInputStream)
            linkedHashMapLIST = objectInputStream.readObject() as LinkedHashMap<GuestCardState, Boolean>
            objectInputStream.close()
            fileInputStream?.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return linkedHashMapLIST
    }

    fun SaveFilterListState(SavedData: String, filterList: ArrayList<GuestCardState>, context:Context) {
        try {
            val fos = context.openFileOutput(SavedData, Context.MODE_PRIVATE)
            val s = ObjectOutputStream(fos)
            s.writeObject(filterList)
            s.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun LoadFilterListState(savedData: String, context:Context): ArrayList<GuestCardState> {
        var filterList = ArrayList<GuestCardState>()
        try {
            val fileInputStream = context.openFileInput(savedData)
            val objectInputStream = ObjectInputStream(fileInputStream)
            filterList = objectInputStream.readObject() as ArrayList<GuestCardState>
            objectInputStream.close()
            fileInputStream?.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return filterList
    }

    fun SaveFilterListPriority(SavedData: String, filterList: ArrayList<GuestCardPriority>, context:Context) {
        try {
            val fos = context.openFileOutput(SavedData, Context.MODE_PRIVATE)
            val s = ObjectOutputStream(fos)
            s.writeObject(filterList)
            s.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun LoadFilterListPriority(savedData: String, context:Context): ArrayList<GuestCardPriority> {
        var filterList = ArrayList<GuestCardPriority>()
        try {
            val fileInputStream = context.openFileInput(savedData)
            val objectInputStream = ObjectInputStream(fileInputStream)
            filterList = objectInputStream.readObject() as ArrayList<GuestCardPriority>
            objectInputStream.close()
            fileInputStream?.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return filterList
    }

    fun SaveLinkedHashMapToInternalStoragePriority(SavedData: String, linkedHashMapList: LinkedHashMap<GuestCardPriority, Boolean>, context:Context) {
        try {
            val fos = context.openFileOutput(SavedData, Context.MODE_PRIVATE)
            val s = ObjectOutputStream(fos)
            s.writeObject(linkedHashMapList)
            s.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun LoadLinkedHashMapFromInternalStoragePriority(savedData: String, context:Context): LinkedHashMap<GuestCardPriority, Boolean> {
        var linkedHashMapLIST = LinkedHashMap<GuestCardPriority, Boolean>()
        try {
            val fileInputStream = context.openFileInput(savedData)
            val objectInputStream = ObjectInputStream(fileInputStream)
            linkedHashMapLIST = objectInputStream.readObject() as LinkedHashMap<GuestCardPriority, Boolean>
            objectInputStream.close()
            fileInputStream?.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return linkedHashMapLIST
    }

    fun prepareFilePart( partName: String, file: File): MultipartBody.Part {

        val mimeType = URLConnection.guessContentTypeFromName(file.name)
        val requestFile = RequestBody.create(MediaType.parse(mimeType), file)

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }
    //TODO: Why it has so abstract name instead of concrete one like compilationSubscriptions()...
    fun getListInt(): HashSet<Int> {
        //TODO: Use standart Serialization mechanism here
        val myList = TextUtils.split(PREFERENCES.getString(SAVE_COMPILATIONS, ""), "‚‗‚")
        val arrayToList = java.util.ArrayList(Arrays.asList(*myList))
        val newList = HashSet<Int>()

        for (item in arrayToList) {
            newList.add(Integer.parseInt(item))
        }

        return newList
    }
}