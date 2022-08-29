package kim.young.fakestoreapp.android.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

val Context.currentConnectivityState: ConnectionState
    get() {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return getCurrentConnectivityState(connectivityManager)
    }

fun Context.observeConnectivityAsFlow() = callbackFlow {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val callback = networkCallback{ state->trySend(state)}
    val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()
    connectivityManager.registerNetworkCallback(networkRequest, callback)
    val currentState = getCurrentConnectivityState(connectivityManager)
    trySend(currentState)

    awaitClose{
        connectivityManager.unregisterNetworkCallback(callback)
    }
}

private fun getCurrentConnectivityState(connectivityManager: ConnectivityManager): ConnectionState {
    val connected = connectivityManager.allNetworks.any() { network ->
        connectivityManager.getNetworkCapabilities(network)?.hasCapability(
            android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
        ) ?: false
    }
    return if (connected) ConnectionState.Available else ConnectionState.Unavailable
}

fun networkCallback(callback: (ConnectionState) -> Unit): ConnectivityManager.NetworkCallback {
    return object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            callback(ConnectionState.Available)
        }
        override fun onLost(network: Network){
            callback(ConnectionState.Unavailable)
        }
    }
}

