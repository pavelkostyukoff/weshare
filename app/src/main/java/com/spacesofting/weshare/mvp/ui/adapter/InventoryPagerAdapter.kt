package com.spacesofting.weshare.mvp.ui.adapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.spacesofting.weshare.api.ResponcePublish
import com.spacesofting.weshare.mvp.ui.fragment.IRentFragment
import com.spacesofting.weshare.mvp.ui.fragment.IShootItFragment

class InventoryPagerAdapter(
    fm: FragmentManager,
    advert: ResponcePublish?
) : androidx.fragment.app.FragmentPagerAdapter(fm) {
    val advert = advert

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                IRentFragment(advert)
            }
            1 -> {
                IShootItFragment()
            }
            else -> IShootItFragment()
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