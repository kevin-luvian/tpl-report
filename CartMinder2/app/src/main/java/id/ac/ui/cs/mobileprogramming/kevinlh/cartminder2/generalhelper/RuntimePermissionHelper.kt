package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.generalhelper

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log

object RuntimePermissionHelper {
    const val REQUEST_PERMISSIONS_CODE = 177
    private val requiredPermissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    fun hasPermission(activity: Activity, permission: String): Boolean {
        return activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermissions(activity: Activity, permission: Array<String>, requestCode: Int) {
        activity.requestPermissions(permission, requestCode)
    }

    fun checkPermissions(
        activity: Activity,
        permissions: Array<String>,
        requestCode: Int = REQUEST_PERMISSIONS_CODE
    ): Boolean {
        val listPermissionsNeeded = arrayListOf<String>()
        permissions.forEach {
            if (!hasPermission(activity, it)) listPermissionsNeeded.add(it)
        }
        Log.d("PERMISSION_CHECK", "permissions needed: ${listPermissionsNeeded.toArray()}")
        if (listPermissionsNeeded.isNotEmpty()) {
            requestPermissions(activity, listPermissionsNeeded.toTypedArray(), requestCode);
            return false
        }
        return true
    }
}