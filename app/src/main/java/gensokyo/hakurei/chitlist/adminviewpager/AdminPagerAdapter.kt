package gensokyo.hakurei.chitlist.adminviewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import gensokyo.hakurei.chitlist.accountslist.AccountsListFragment
import gensokyo.hakurei.chitlist.adminsettings.AdminSettingsFragment
import gensokyo.hakurei.chitlist.itemslist.ItemsListFragment
import gensokyo.hakurei.chitlist.transactionslist.TransactionsListFragment

const val ADMIN_SETTINGS_PAGE_INDEX = 0
const val ACCOUNTS_LIST_PAGE_INDEX = 1
const val ITEMS_LIST_PAGE_INDEX = 2
const val TRANSACTIONS_LIST_PAGE_INDEX = 3

class AdminPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    /**
     * Mapping of the ViewPager page indexes to their respective Fragments.
     */
    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        ADMIN_SETTINGS_PAGE_INDEX to { AdminSettingsFragment() },
        ACCOUNTS_LIST_PAGE_INDEX to { AccountsListFragment() },
        ITEMS_LIST_PAGE_INDEX to { ItemsListFragment() },
        TRANSACTIONS_LIST_PAGE_INDEX to { TransactionsListFragment() }
    )

    override fun getItemCount() = tabFragmentsCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}