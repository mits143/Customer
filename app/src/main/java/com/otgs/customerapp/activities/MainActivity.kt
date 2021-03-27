package com.otgs.customerapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.otgs.customerapp.R
import com.otgs.customerapp.adapter.ViewPagerAdapter
import com.otgs.customerapp.fragment.FragementSetting
import com.otgs.customerapp.fragment.FragmentBooking
import com.otgs.customerapp.fragment.FragmentHome
import com.otgs.customerapp.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var viewpageradapter: ViewPagerAdapter? = null
    private var editVehicle = false
    private var addVehicle = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        init()
    }

    fun init() {
        viewpageradapter = ViewPagerAdapter(supportFragmentManager)
        viewpageradapter!!.addFragment(FragmentBooking())
        viewpageradapter!!.addFragment(FragmentHome())
        viewpageradapter!!.addFragment(FragementSetting())
        viewPager.adapter = viewpageradapter
        viewPager.setCurrentItem(1)
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        viewPager!!.offscreenPageLimit = 3

        viewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                when (position) {
                    0 -> toolbar.title = "Booking"
                    1 -> toolbar.title = "Home"
                    2 -> toolbar.title = "Setting"
                }
            }

        })

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            if (item.itemId == android.R.id.home) {
                finish()
            } else {
                when (item.itemId) {
                    R.id.booking -> {
                        viewPager.setCurrentItem(0)
                        toolbar.title = "Booking"
                    }
                    R.id.home -> {
                        viewPager.setCurrentItem(1)
                        toolbar.title = "Home"
                    }
                    R.id.setting -> {
                        viewPager.setCurrentItem(2)
                        toolbar.title = "Setting"
                    }
                }
            }

            true
        }

        Utils.hideFirstFab(llEditVehicle)
        Utils.hideFirstFab(llAddVehicle)

        fab.setOnClickListener { v ->

            editVehicle = Utils.twistFab(v, !editVehicle)
            addVehicle = Utils.twistFab(v, !addVehicle)

            if (editVehicle) {
                Utils.showFab(llEditVehicle)
            } else {
                Utils.hideFab(llEditVehicle)
            }

            if (addVehicle) {
                Utils.showFab(llAddVehicle)
            } else {
                Utils.hideFab(llAddVehicle)
            }
        }

        llEditVehicle.setOnClickListener {
            startActivity(Intent(this, VehicleList::class.java))
        }

        llAddVehicle.setOnClickListener {
            startActivity(Intent(this, AddVehicle::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.notification_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_notification -> {
                startActivity(Intent(this, NotificationActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}