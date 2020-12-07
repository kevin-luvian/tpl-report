package id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.helper;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.model.NetworkInfo;

public class NetworkScanHelper {
    WifiManager wifiManager;

    public NetworkScanHelper(Context context) {
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    public List<NetworkInfo> scanLocalNetworks() {
        try {
            return new ScanWifiAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private class ScanWifiAsyncTask extends AsyncTask<Void, Void, List<NetworkInfo>> {
        @Override
        protected List<NetworkInfo> doInBackground(Void... voids) {
            List<NetworkInfo> networks = new ArrayList<NetworkInfo>();
            for (ScanResult scan : getScanResults()) {
                NetworkInfo newNetwork = createNetworkFromScanResult(scan);
                networks.add(newNetwork);
            }
            return networks;
        }

        private List<ScanResult> getScanResults() {
            wifiManager.startScan();
            return wifiManager.getScanResults();
        }

        private NetworkInfo createNetworkFromScanResult(ScanResult scanResult) {
            NetworkInfo ni = new NetworkInfo();
            ni.setSsid(scanResult.SSID);
            ni.setMac(scanResult.BSSID);
            ni.setFrequency(scanResult.frequency);
            ni.setLevel(scanResult.level);
            ni.setCapabilities(scanResult.capabilities);
            Log.d("NETWORK", ni.toString());
            return ni;
        }
    }
}
