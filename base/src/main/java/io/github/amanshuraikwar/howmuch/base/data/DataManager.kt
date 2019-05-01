package io.github.amanshuraikwar.howmuch.base.data

import io.github.amanshuraikwar.howmuch.protocol.CategoriesDataManager
import io.github.amanshuraikwar.howmuch.protocol.TransactionDataManager
import io.github.amanshuraikwar.howmuch.protocol.UserDataManager
import io.github.amanshuraikwar.howmuch.protocol.WalletDataManager

/**
 * Data Manager abstraction for the whole app.
 * This is a single entry point to access/modify any kind of data the app uses.
 *
 * @author Amanshu Raikwar
 * Created by Amanshu Raikwar on 06/03/18.
 */
interface DataManager : TransactionDataManager, CategoriesDataManager, UserDataManager, WalletDataManager