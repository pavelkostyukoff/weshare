package com.spacesofting.weshare.presentation.ui.adapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.spacesofting.weshare.data.api.RespouncePublish
import com.spacesofting.weshare.presentation.ui.fragment.IAmShooltGoodsFragment
import com.spacesofting.weshare.presentation.ui.fragment.IAmRentGoodsFragment

class InventoryPagerAdapter(
    fm: FragmentManager,
    advert: RespouncePublish?
) : androidx.fragment.app.FragmentPagerAdapter(fm) {
    val advert = advert

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                IAmShooltGoodsFragment(advert)
            }
            1 -> {
                IAmRentGoodsFragment()
            }
            else -> IAmRentGoodsFragment()
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