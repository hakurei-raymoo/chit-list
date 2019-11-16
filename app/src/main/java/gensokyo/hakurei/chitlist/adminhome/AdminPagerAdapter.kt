package gensokyo.hakurei.chitlist.adminhome

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import gensokyo.hakurei.chitlist.accountslist.AccountsListFragment
import gensokyo.hakurei.chitlist.adminactions.AdminActionsFragment
import gensokyo.hakurei.chitlist.itemslist.ItemsListFragment
import gensokyo.hakurei.chitlist.transactionslist.TransactionsListFragment

const val ADMIN_ACTIONS_PAGE_INDEX = 0
const val TRANSACTIONS_LIST_PAGE_INDEX = 1
const val ITEMS_LIST_PAGE_INDEX = 2
const val ACCOUNTS_LIST_PAGE_INDEX = 3

class AdminPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    /**
     * Mapping of the ViewPager page indexes to their respective Fragments.
     */
    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        ADMIN_ACTIONS_PAGE_INDEX to { AdminActionsFragment() },
        TRANSACTIONS_LIST_PAGE_INDEX to { TransactionsListFragment() },
        ITEMS_LIST_PAGE_INDEX to { ItemsListFragment() },
        ACCOUNTS_LIST_PAGE_INDEX to { AccountsListFragment() }
    )

    override fun getItemCount() = tabFragmentsCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}