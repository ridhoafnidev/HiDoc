package com.ismealdi.hidoc.utils.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import com.ismealdi.hidoc.utils.commons.Logs
import com.ismealdi.hidoc.utils.interfaces.AmConnectionInterface

/**
 * Created by Al
 * on 06/04/19 | 02:20
 */
class ConnectionReceiver : BroadcastReceiver() {

	private var callback: AmConnectionInterface? = null

	override fun onReceive(context: Context, arg1: Intent) {
		val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

		Logs.e("Internet Check")

		val builder = NetworkRequest.Builder()
		
		connMgr.registerNetworkCallback(builder.build(), 
			object : ConnectivityManager.NetworkCallback() {
				override fun onAvailable(network: Network?) {
					super.onAvailable(network)

					showMessage("Internet Connected")
				}

				override fun onLost(network: Network?) {
					super.onLost(network)

					showMessage("No Internet Connection")
				}
			}
		)
	}

	private fun showMessage(message: String) {
		this.callback?.let {
			it.onConnectionChange(message)
			Logs.e(message)
		} ?: run {
			Logs.e("No Callback for Internet State")
		}
	}

	fun registerReceiver(receiver: AmConnectionInterface) {
		this.callback = receiver
	}

}