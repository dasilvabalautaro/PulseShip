package com.globalhiddenodds.pulseship.ui.activities

import android.app.SearchManager
import android.content.Context
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.globalhiddenodds.pulseship.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.floor

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration : AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.navigation_activity)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        //setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.pulseship_nav_host_fragment) as NavHostFragment? ?: return

        navController = host.navController

        val drawerLayout : DrawerLayout? = findViewById(R.id.drawer_layout)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.home_dest, R.id.contacts_dest),
            drawerLayout)

        setupActionBar(navController, appBarConfiguration)

        setupNavigationMenu(navController)

        setupBottomNavMenu(navController)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val retValue = super.onCreateOptionsMenu(menu)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        // The NavigationView already has these same navigation items, so we only add
        // navigation items to the menu here if there isn't a NavigationView
        if (navigationView == null) {
            menuInflater.inflate(R.menu.overflow_menu, menu)
            return true
        }

        /*val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu!!.findItem(R.id.search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }*/

        return retValue
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Have the NavigationUI look for an action or destination matching the menu
        // item id and navigate there if found.
        // Otherwise, bubble up to the parent.
        /*val id = item.itemId
        if (R.id.search == id){

        }*/

        return item.onNavDestinationSelected(findNavController(R.id.pulseship_nav_host_fragment))
                || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        // Allows NavigationUI to support proper up navigation or the drawer layout
        // drawer menu, depending on the situation
        return findNavController(R.id.pulseship_nav_host_fragment).navigateUp(appBarConfiguration)
    }

    private fun setupActionBar(navController: NavController,
                               appBarConfig : AppBarConfiguration) {

        // This allows NavigationUI to decide what label to show in the action bar
        // By using appBarConfig, it will also determine whether to
        // show the up arrow or drawer menu icon
        setupActionBarWithNavController(navController, appBarConfig)

    }

    private fun setupNavigationMenu(navController: NavController) {
        // In split screen mode, you can drag this view out from the left
        // This does NOT modify the actionbar
        val sideNavView = findViewById<NavigationView>(R.id.nav_view)
        sideNavView?.setupWithNavController(navController)

    }

    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav?.setupWithNavController(navController)

    }

    fun setBottomNavigationVisibility(visibility: Int){
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav?.visibility = visibility
    }
}
