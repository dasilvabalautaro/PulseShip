package com.globalhiddenodds.pulseship.ui.fragments.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

private val PERMISSIONS_REQUIRED = arrayOf(
    Manifest.permission.CAMERA
)

class PermissionsFragment : Fragment() {

    private val getCameraPermission = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach {
            Log.e(tag, "${it.key} = ${it.value}")
        }

        if (hasPermissions(requireContext())) {
            val action = PermissionsFragmentDirections.actionPermissionsToSelector()
            findNavController().navigate(action)
        } else {
            Toast.makeText(context, "Permission request denied", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (hasPermissions(requireContext())) {
            // If permissions have already been granted, proceed

            val action = PermissionsFragmentDirections.actionPermissionsToSelector()
            findNavController().navigate(action)


        } else {
            // Request camera-related permissions
            getCameraPermission.launch(PERMISSIONS_REQUIRED)
        }
    }

    companion object {
        /** Convenience method used to check if all permissions required by this app are granted */
        fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}
