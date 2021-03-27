package com.kuber.vpn.Utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.otgs.customerapp.Constants
import com.otgs.customerapp.Constants.Companion.IS_FIRST_ATEMPT
import com.otgs.customerapp.Constants.Companion.KEY_EMAIL
import com.otgs.customerapp.Constants.Companion.KEY_MOBILE_NO
import com.otgs.customerapp.Constants.Companion.KEY_NAME
import com.otgs.customerapp.Constants.Companion.KEY_PROFILE_PHOTO
import com.otgs.customerapp.Constants.Companion.KEY_TOKEN
import com.otgs.customerapp.Constants.Companion.KEY_UNIQUENO
import com.otgs.customerapp.Constants.Companion.KEY_USERID
import com.otgs.customerapp.Constants.Companion.KEY_USERTYPE
import com.otgs.customerapp.activities.LoginActivity
import com.otgs.customerapp.activities.MainActivity


object sessionManager : Constants {
    val TAG = sessionManager::class.java.simpleName
    const val NAME_SHARPREFS = "app_prefs"
    fun saveSetting(
        mContext: Context?,
        mKey: String?,
        mValue: String?
    ) {
        try {
            if (mContext != null) {
                val mSharedPreferences = mContext.getSharedPreferences(
                    NAME_SHARPREFS,
                    Context.MODE_PRIVATE
                )
                if (mSharedPreferences != null) {
                    val editor = mSharedPreferences.edit()
                    editor.putString(mKey, mValue)
                    editor.commit()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getSetting(
        mContext: Context?,
        mKey: String?,
        mDefValue: String?
    ): String? {
        try {
            if (mContext != null) {
                val mSharedPreferences = mContext.getSharedPreferences(
                    NAME_SHARPREFS,
                    Context.MODE_PRIVATE
                )
                if (mSharedPreferences != null) {
                    return mSharedPreferences.getString(mKey, mDefValue)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mDefValue
    }

    fun removeSetting(mContext: Context, mKey: String?) {
        val mSharedPreferences = mContext.getSharedPreferences(
            NAME_SHARPREFS,
            Context.MODE_PRIVATE
        )
        val editor = mSharedPreferences.edit()
        editor.remove(mKey)
        editor.commit()
    }

    fun setToken(mContext: Context?, mValue: String?) {
        saveSetting(mContext, Constants.KEY_TOKEN, mValue)
    }

    fun getToken(mContext: Context?): String? {
        return getSetting(mContext, KEY_TOKEN, "")
    }

    fun setUser_ID(mContext: Context?, mValue: String?) {
        saveSetting(mContext, Constants.KEY_USERID, mValue)
    }

    fun getUser_ID(mContext: Context?): String? {
        return getSetting(mContext, KEY_USERID, "")
    }

    fun setUserType(mContext: Context?, mValue: String?) {
        saveSetting(mContext, Constants.KEY_USERTYPE, mValue)
    }

    fun getUserType(mContext: Context?): String? {
        return getSetting(mContext, KEY_USERTYPE, "")
    }

    fun setEmail(mContext: Context?, mValue: String?) {
        saveSetting(mContext, Constants.KEY_EMAIL, mValue)
    }

    fun getEmail(mContext: Context?): String? {
        return getSetting(mContext, KEY_EMAIL, "")
    }

    fun setName(mContext: Context?, mValue: String?) {
        saveSetting(mContext, Constants.KEY_NAME, mValue)
    }

    fun getName(mContext: Context?): String? {
        return getSetting(mContext, KEY_NAME, "")
    }

    fun setUnique_No(mContext: Context?, mValue: String?) {
        saveSetting(mContext, Constants.KEY_UNIQUENO, mValue)
    }

    fun getUnique_No(mContext: Context?): String? {
        return getSetting(mContext, KEY_UNIQUENO, "")
    }

    fun setMobile_No(mContext: Context?, mValue: String?) {
        saveSetting(mContext, Constants.KEY_MOBILE_NO, mValue)
    }

    fun getMobile_No(mContext: Context?): String? {
        return getSetting(mContext, KEY_MOBILE_NO, "")
    }

    fun setPofile_photo(mContext: Context?, mValue: String?) {
        saveSetting(mContext, Constants.KEY_PROFILE_PHOTO, mValue)
    }

    fun getPofile_photo(mContext: Context?): String? {
        return getSetting(mContext, KEY_PROFILE_PHOTO, "")
    }

    fun setis_first_atempt(mContext: Context?, mValue: Int) {
        saveSetting(mContext, IS_FIRST_ATEMPT, mValue.toString())
    }

    fun getis_first_atempt(mContext: Context?): Int {
        return getSetting(mContext, IS_FIRST_ATEMPT, "0")!!.toInt()
    }

//    fun getLocalReward(mContext: Context?): Long {
//        return getSetting(mContext, KEY_LOCAL_REWARD, "0")!!.toLong()
//    }
//
//    fun setLocalReward(mContext: Context?, mValue: Long) {
//        saveSetting(mContext, KEY_LOCAL_REWARD, mValue.toString())
//    }

//    fun setChangePassword(mContext: Context?, mValue: Boolean) {
//        saveSetting(mContext, KEY_CHNAGE_PWD, mValue.toString())
//    }
//
//    fun getChangePassword(mContext: Context?): Boolean {
//        return java.lang.Boolean.parseBoolean(
//            getSetting(mContext, KEY_CHNAGE_PWD, "false")
//        )
//    }

//    fun saveArrayList(
//        mContext: Context?,
//        list: List<Long?>?
//    ) {
//        val gson = Gson()
//        val key = gson.toJson(list)
//        saveSetting(mContext, KEY_DAILY_BONUS, key)
//    }
//
//    fun getArrayList(mContext: Context?): List<Long> {
//        val gson = Gson()
//        val json = getSetting(mContext, KEY_DAILY_BONUS, "")
//        val type =
//            object : TypeToken<List<Long?>?>() {}.type
//        return gson.fromJson(json, type)
//    }

    fun clearData(mContext: Context) {
        removeSetting(mContext, KEY_USERID);

        val i = Intent(mContext, MainActivity::class.java)
// set the new task and clear flags
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        mContext.startActivity(i)
    }
}