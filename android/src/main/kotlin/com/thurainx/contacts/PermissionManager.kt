package com.thurainx.contacts

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugin.common.MethodChannel.Result


class PermissionManager(val context: Context) : PluginRegistry.ActivityResultListener, PluginRegistry.RequestPermissionsResultListener {

    private var activity: Activity? = null
    private var result: Result? = null

    fun initActivity(myActivity: Activity?) {
        activity = myActivity
    }

    fun initResult(myResult: Result) {
        result = myResult
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ): Boolean {
        if (activity == null) {
            Log.d("permission_manager", "activity cannot be null.")
        }
        val permissionIndex = permissions.indexOf(Manifest.permission.READ_CONTACTS)
        when (requestCode) {
            100 -> {
                Log.d(
                    "permission_manager",
                    "is Granted: ${grantResults[permissionIndex] == PackageManager.PERMISSION_GRANTED}"
                )
                if (grantResults[permissionIndex] == PackageManager.PERMISSION_GRANTED) {
                    result?.success(PermissionConstants.PERMISSION_STATUS_GRANTED)
                } else if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        activity!!,
                        Manifest.permission.READ_CONTACTS
                    )
                ) {
                    result?.success(PermissionConstants.PERMISSION_STATUS_NEVER_ASK_AGAIN)
                } else {
                    result?.success(PermissionConstants.PERMISSION_STATUS_DENIED)
                }

                return true
            }

            else -> {
                return false
            }
        }
    }

    fun requestContactPermission() {
        if(!determineContactPermission()){
            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.READ_CONTACTS), 100)
        }else{
            result?.success(PermissionConstants.PERMISSION_STATUS_GRANTED)
        }
    }

    fun determineContactPermission() : Boolean {
        val permissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)
        return permissionStatus == PackageManager.PERMISSION_GRANTED
    }


}