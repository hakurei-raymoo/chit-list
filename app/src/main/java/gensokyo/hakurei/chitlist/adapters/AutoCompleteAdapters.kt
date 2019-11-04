package gensokyo.hakurei.chitlist.adapters

import android.util.Log
import gensokyo.hakurei.chitlist.database.BareAccount
import gensokyo.hakurei.chitlist.database.BareItem

private const val TAG = "AutoCompleteAdapters"

/* Convert List<BareAccount> to List<String> for AutoCompeteTextView. */
fun formatAccounts(accounts: List<BareAccount>): List<String> {
    val mutableList = accounts.toMutableList()
    val stringList = mutableListOf<String>()
    mutableList.sortWith(compareBy(String.CASE_INSENSITIVE_ORDER) {
        String.format(
            "%04d: %s %s",
            it.accountId,
            it.firstName,
            it.lastName
        )
    })
    Log.i(TAG, "mutableList=$mutableList")

    mutableList.forEach {
        stringList.add(String.format("%04d: %s %s", it.accountId, it.firstName, it.lastName))
    }
    Log.i(TAG, "stringList=$stringList")
    return stringList
}

/* Convert List<BareItem> to List<String> for AutoCompeteTextView. */
fun formatItems(items: List<BareItem>): List<String> {
    val mutableList = items.toMutableList()
    val stringList = mutableListOf<String>()
    mutableList.sortWith(compareBy(String.CASE_INSENSITIVE_ORDER) {
        String.format(
            "%04d: %s",
            it.itemId,
            it.name
        )
    })
    Log.i(TAG, "mutableList=$mutableList")

    mutableList.forEach {
        stringList.add(String.format("%04d: %s", it.itemId, it.name))
    }
    Log.i(TAG, "stringList=$stringList")
    return stringList
}
