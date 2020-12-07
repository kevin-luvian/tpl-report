package id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.helper;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.model.NetworkInfo;

public class NetworkScanHelper {
    static final int TIMEOUT = 5;
    static final String EMPTY_MAC = "00:00:00:00:00:00";
    ExecutorService executor;
    WifiManager wifiManager;
    String subnet;

    public NetworkScanHelper(Context context) {
        executor = new ThreadPoolExecutor(1, 1, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        subnet = getSubnetAddress(wifiManager.getDhcpInfo().gateway);
    }

    public String getSubnetAddress(int address) {
        return String.format(Locale.ROOT, "%d.%d.%d",
                (address & 0xff),
                (address >> 8 & 0xff),
                (address >> 16 & 0xff));
    }

    public List<NetworkInfo> getNetworks() {
        ScanWifiAsyncTask task = new ScanWifiAsyncTask();
        try {
            return task.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private class ScanWifiAsyncTask extends AsyncTask<Void, Void, List<NetworkInfo>> {
        @Override
        protected List<NetworkInfo> doInBackground(Void... voids) {
            wifiManager.startScan();
            List<NetworkInfo> networks = new ArrayList<NetworkInfo>();
            List<ScanResult> scanResults = wifiManager.getScanResults();
            for (ScanResult scan : scanResults) {
                NetworkInfo ni = new NetworkInfo();
                ni.setSsid(scan.SSID);
                ni.setMac(scan.BSSID);
                ni.setFrequency(scan.frequency);
                ni.setLevel(scan.level);
                ni.setCapabilities(scan.capabilities);
                Log.d("NETWORK", ni.toString());
                networks.add(ni);
            }
            return networks;
        }
    }
}
