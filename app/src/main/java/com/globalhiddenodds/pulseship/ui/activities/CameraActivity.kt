package com.globalhiddenodds.pulseship.ui.activities

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.globalhiddenodds.pulseship.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CameraActivity: AppCompatActivity(){
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.activity_camera)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.camera_nav_host_fragment) as NavHostFragment? ?: return

        navController = host.navController

        /*navController.addOnDestinationChangedListener { _, destination, _ ->
            val dest: String = try {
                resources.getResourceName(destination.id)
            } catch (e: Resources.NotFoundException) {
                destination.id.toString()
            }

            Toast.makeText(this@CameraActivity, "Navigated to $dest",
                Toast.LENGTH_SHORT).show()
            Log.d("NavigationCamera", "Navigated to $dest")
        }*/

    }
}