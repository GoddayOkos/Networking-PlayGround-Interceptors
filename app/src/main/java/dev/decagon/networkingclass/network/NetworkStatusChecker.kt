package dev.decagon.networkingclass.network

import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class NetworkStatusChecker(
    private val connectivityManager: ConnectivityManager?,
    val onNoConnection: () -> Unit
) {

    /**
     * Checks the Internet connection and performs an action if it's active.
     */
    inline fun performIfConnectedToInternet(action: () -> Unit) {
        if (hasInternetConnection()) {
            action()
        } else {
            onNoConnection()
        }
    }

    fun hasInternetConnection(): Boolean {
        val network = connectivityManager?.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
    }
}