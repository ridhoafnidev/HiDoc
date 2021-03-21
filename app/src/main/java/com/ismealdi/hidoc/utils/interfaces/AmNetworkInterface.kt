package com.ismealdi.hidoc.utils.interfaces

/**
 * Created by Al
 * on 08/04/19 | 11:54
 */
interface AmNetworkInterface {
    
    fun onSuccess(message: String)

    fun onError(message: String)

    fun onInfo(message: String)

    fun onAlert(message: String, actionText: String, actionListener: Runnable)

    fun progress(isShow: Boolean = true)
    
}