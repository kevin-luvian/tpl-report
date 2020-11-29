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

//    public void pingAll() {
//        ScanWifiAyncTask task = new ScanWifiAsyncTask();
//        try {
//            task.execute().get();
//        } catch (ExecutionException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

//    public void printString() {
//        String currentDesc = String.format("Rssi %s\n", wifiManager.getConnectionInfo().getRssi());
//        currentDesc += String.format("Link Speed %s\n", wifiManager.getConnectionInfo().getLinkSpeed());
//        System.out.println(String.format("Current Wifi Desc :: \n%s\n", currentDesc));
//
//        wifiManager.startScan();
//        List<ScanResult> scanResults = wifiManager.getScanResults();
//        for (ScanResult scan : scanResults) {
//            String desc = String.format("ssid %s\n", scan.SSID);
//            desc += String.format("bssid %s\n", scan.BSSID);
//            desc += String.format("frequency %s\n", scan.frequency);
//            desc += String.format("level %s\n", scan.level);
//            desc += String.format("capabilities %s\n", scan.capabilities);
//            desc += String.format("venue %s\n", scan.venueName.toString());
//            System.out.println(String.format("Wifi Net :: \n%s\n", desc));
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                for (ScanResult.InformationElement elem : scan.getInformationElements()) {
//                    System.out.println(String.format("Elem id:%s idExt:%s", elem.getId(), elem.getIdExt()));
//                }
//            }
//        }
//    }

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

//    private class ReadARPAsyncTask extends AsyncTask<Void, Void, HashMap<String, String>> {
//        @Override
//        protected HashMap<String, String> doInBackground(Void... voids) {
//            pingHosts();
//            HashMap<String, String> map = new HashMap<String, String>();
//            BufferedReader br;
//            try {
//                br = new BufferedReader(new FileReader("/proc/net/arp"));
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//                return null;
//            }
//            String currentLine;
//            String[] splittedLine;
//            String ip;
//            String mac;
//            int counter = 0;
//            try {
//                while ((currentLine = br.readLine()) != null) {
//                    splittedLine = currentLine.split(" +");
////                    Log.d("ARP LINE", String.format(Locale.ROOT, "Line[%d]: %s", ++counter, Arrays.toString(splittedLine)));
////                    Log.d("ARP LINE", String.format(Locale.ROOT, "Line[%d]: %s", ++counter, currentLine));
//                    if (splittedLine != null && splittedLine.length >= 4) {
//                        ip = splittedLine[0];
//                        mac = splittedLine[3];
//                        if (!mac.equals(EMPTY_MAC)) {
//                            map.put(mac, ip);
//                        }
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return map;
//        }
//
//        protected void pingHosts() {
//            for (int i = 1; i < 255; i++) {
//                String host = String.format(Locale.ROOT, "%s.%d", subnet, i);
//                try {
//                    if (InetAddress.getByName(host).isReachable(TIMEOUT))
//                        Log.i("SCANNED HOST", String.format("Host: %s is reachable", host));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
}
