package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.helper

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast

class NetworkHelper(private var context: Context) {
    private var connectivityManager: ConnectivityManager =
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)

    private fun isNetworkActive() = connectivityManager.activeNetwork != null

    fun checkConnection(lmbd: () -> Any?) {
        if (isNetworkActive()) lmbd()
        else Toast.makeText(context, "connection is required", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private var instance: NetworkHelper? = null
        fun getInstance(context: Context) =
            instance ?: NetworkHelper(context).also { instance = it }
    }
}