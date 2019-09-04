package io.github.amanshuraikwar.howmuch.base.data

import io.github.amanshuraikwar.howmuch.protocol.*
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Implementation of the Data Manager for the app.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 07/03/18.
 */
class DataManagerImpl
@Inject constructor(private val transactionDataManager: TransactionDataManager,
                    private val categoriesDataManager: CategoriesDataManager,
                    private val userDataManager: UserDataManager,
                    private val walletDataManager: WalletDataManager) : DataManager {

    override fun getAllTransactions()
            = transactionDataManager.getAllTransactions()

    override fun getTransactionById(id: String)
            = transactionDataManager.getTransactionById(id)

    override fun addTransaction(transaction: Transaction)
            = transactionDataManager.addTransaction(transaction)

    override fun updateTransaction(transaction: Transaction)
            = transactionDataManager.updateTransaction(transaction)

    override fun deleteTransaction(transaction: Transaction)
            = transactionDataManager.deleteTransaction(transaction)

    override fun getAllCategories()
            = categoriesDataManager.getAllCategories()

    override fun getCategoryById(id: String)
            = categoriesDataManager.getCategoryById(id)

    override fun addCategory(category: Category)
            = categoriesDataManager.addCategory(category)

    override fun updateCategory(category: Category)
            = categoriesDataManager.updateCategory(category)

    override fun deleteCategory(category: Category)
            = categoriesDataManager.deleteCategory(category)

    override fun signIn(user: User)
            = userDataManager.signIn(user)

    override fun signOut()
            = userDataManager.signOut()

    override fun isSignedIn()
            = userDataManager.isSignedIn()

    override fun getSignedInUser()
            = userDataManager.getSignedInUser()

    override fun getMonthlyExpenseLimit()
            = userDataManager.getMonthlyExpenseLimit()

    override fun setMonthlyExpenseLimit(limit: Double)
            = userDataManager.setMonthlyExpenseLimit(limit)

    override fun getAllWallets()
            = walletDataManager.getAllWallets()

    override fun getWalletById(id: String)
            = walletDataManager.getWalletById(id)

    override fun addWallet(wallet: Wallet)
            = walletDataManager.addWallet(wallet)

    override fun updateWallet(wallet: Wallet)
            = walletDataManager.updateWallet(wallet)

    override fun deleteWallet(wallet: Wallet)
            = walletDataManager.deleteWallet(wallet)

    override fun getSpreadSheetId()
            = userDataManager.getSpreadSheetId()
}