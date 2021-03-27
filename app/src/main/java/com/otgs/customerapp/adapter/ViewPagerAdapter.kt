package com.otgs.customerapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ViewPagerAdapter(supportFragmentManager: FragmentManager) :
        FragmentStatePagerAdapter(supportFragmentManager) {

        val mFragmentList = ArrayList<Fragment>()
//        val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList.get(position)
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

//    override fun getPageTitle(position: Int): CharSequence? {
//        return mFragmentTitleList[position]
//    }

        fun addFragment(fragment: Fragment/*, mFragmentTitleList1: String*/) {
            mFragmentList.add(fragment)
//            mFragmentTitleList.add(mFragmentTitleList1)
        }
    }