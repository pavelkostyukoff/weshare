package com.spacesofting.weshare.mvp.ui.adapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.spacesofting.weshare.mvp.ui.fragment.IRentFragment
import com.spacesofting.weshare.mvp.ui.fragment.IShootItFragment

class InventoryPagerAdapter (fm: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
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