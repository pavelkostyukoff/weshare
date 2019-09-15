package com.spacesofting.weshare.ui.adapter
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.spacesofting.weshare.ui.fragment.IRentFragment
import com.spacesofting.weshare.ui.fragment.IShootItFragment

class InventoryPagerAdapter (fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                IRentFragment()
                return IRentFragment()
            }
            1 -> {
                IShootItFragment()
                return IShootItFragment()
            }
            else -> return IShootItFragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Сдаю"
            else -> {
                return "Арендую"
            }
        }
    }
}