package io.github.amanshuraikwar.howmuch.ui.spinner

import android.content.Context
import io.github.amanshuraikwar.howmuch.protocol.Wallet

class WalletAdapter
constructor(context: Context, resourceId: Int, objects: List<Wallet>)
    : SpinnerAdapter<Wallet>(context, resourceId, objects) {

    override fun Wallet.getTitle() = this.name
}