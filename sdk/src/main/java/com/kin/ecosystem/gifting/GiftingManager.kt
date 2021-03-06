package com.kin.ecosystem.gifting

import android.app.Activity
import android.content.Context
import com.kin.ecosystem.common.Observer
import com.kin.ecosystem.common.Subscription
import com.kin.ecosystem.common.model.OrderConfirmation

interface GiftingManager {

	fun addOrderConfirmationObserver(observer: Observer<OrderConfirmation>) : Subscription<OrderConfirmation>

	fun showDialog(activity: Activity, recipientUserID: String)
}