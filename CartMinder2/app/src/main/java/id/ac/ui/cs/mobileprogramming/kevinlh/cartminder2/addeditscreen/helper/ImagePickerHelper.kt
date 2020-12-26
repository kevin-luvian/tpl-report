package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.addeditscreen.helper

import android.Manifest
import android.app.Activity
import androidx.appcompat.app.AlertDialog
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.R
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.generalhelper.RuntimePermissionHelper


object ImagePickerHelper {
    interface PickerOptionListener {
        fun onTakeCameraSelected()
        fun onChooseGallerySelected()
    }

    fun showImagePickerOptions(context: Activity, listener: PickerOptionListener) {
        // setup the alert builder
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.button_choose_image))

        // add a list
        val labels = arrayOf(
            context.getString(R.string.label_take_camera),
            context.getString(R.string.label_choose_gallery)
        )
        builder.setItems(labels) { _, which ->
            when (which) {
                0 -> {
                    if (RuntimePermissionHelper.checkPermissions(
                            context,
                            arrayOf(
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            )
                        )
                    ) listener.onTakeCameraSelected()
                }
                1 -> {
                    if (RuntimePermissionHelper.checkPermissions(
                            context,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                        )
                    ) listener.onChooseGallerySelected()
                }
            }
        }

        // create and show the alert dialog
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}