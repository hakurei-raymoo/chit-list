package gensokyo.hakurei.chitlist.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import gensokyo.hakurei.chitlist.checkout.CheckoutFragment
import gensokyo.hakurei.chitlist.history.HistoryFragment
import gensokyo.hakurei.chitlist.shop.ShopFragment

const val SHOP_PAGE_INDEX = 0
const val CHECKOUT_PAGE_INDEX = 1
const val HISTORY_PAGE_INDEX = 2

class HomePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    /**
     * Mapping of the ViewPager page indexes to their respective Fragments.
     */
    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        SHOP_PAGE_INDEX to { ShopFragment() },
        CHECKOUT_PAGE_INDEX to { CheckoutFragment() },
        HISTORY_PAGE_INDEX to { HistoryFragment() }
    )

    override fun getItemCount() = tabFragmentsCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}