package id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.api;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.model.NetworkInfo;
import id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.model.NetworkInfoPost;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String LOG_API = "API";
    private static final String APIKEY = "06aa19f10bc1f4fd574f93f35529e0a679e12";
    static final String BASE_URL = "https://mobileprogramming-0e17.restdb.io/rest/";
    private final ExecutorService executor;
    ApiQuery apiQuery;

    public ApiClient() {
        executor = new ThreadPoolExecutor(3, 5, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiQuery = retrofit.create(ApiQuery.class);
    }

    public List<NetworkInfo> fetchNetworks() {
        try {
            Response<List<NetworkInfo>> response = apiQuery.getNetworks(APIKEY).execute();
            if (response.isSuccessful()) {
                List<NetworkInfo> networks = response.body();
                for (NetworkInfo network : networks) {
                    network.setScanLocation(NetworkInfo.FROM_API);
                }
                return networks;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void deleteAllNetworks() {
        executor.execute(() -> {
            List<NetworkInfo> fetchedNetworks = fetchNetworks();
            for (NetworkInfo network : fetchedNetworks) {
                executor.execute(() -> {
                    Log.i(LOG_API, String.format(
                            "trying to query: [delete] ssid: %s id: %s",
                            network.getSsid(),
                            network.getId()));
                    try {
                        apiQuery.deleteNetwork(network.getId(), APIKEY).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.i(LOG_API, String.format(
                            "ssid: %s id: %s query: [ deleted ]",
                            network.getSsid(),
                            network.getId()));
                });
            }
        });
    }

    public void uploadNetworks(List<NetworkInfo> networks) {
        executor.execute(() -> {
            List<NetworkInfo> fetchedNetworks = fetchNetworks();
            List<NetworkInfo> convergedList = convergeNetworkInfoList(networks, fetchedNetworks);
            for (NetworkInfo network : convergedList) {
                NetworkInfoPost post = new NetworkInfoPost(network);
                executor.execute(() -> {
                    Log.i(LOG_API, String.format(
                            "trying to query: [upload] ssid: %s id: %s",
                            network.getSsid(),
                            network.getId()));
                    try {
                        if (network.getId().isEmpty())
                            apiQuery.postNetwork(APIKEY, post).execute();
                        else
                            apiQuery.putNetwork(network.getId(), APIKEY, post).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.i(LOG_API, String.format(
                            "ssid: %s id: %s query: [ uploaded ]",
                            network.getSsid(),
                            network.getId()));
                });
            }
        });
    }

    public static List<NetworkInfo> convergeNetworkInfoList(
            List<NetworkInfo> newList,
            List<NetworkInfo> oldList) {
        HashMap<String, IdPos> map = parseToIdPos(oldList);
        for (NetworkInfo newNetwork : newList) {
            IdPos idPos = map.get(newNetwork.getMac());
            if (idPos != null) {
                newNetwork.setId(idPos.id);
                oldList.set(idPos.position, newNetwork);
            } else {
                oldList.add(newNetwork);
            }
        }
        return oldList;
    }

    public static HashMap<String, IdPos> parseToIdPos(List<NetworkInfo> networks) {
        HashMap<String, IdPos> map = new HashMap<>();
        NetworkInfo net;
        for (int i = 0; i < networks.size(); i++) {
            net = networks.get(i);
            map.put(net.getMac(), new IdPos(net.getId(), i));
        }
        return map;
    }

    public static class IdPos {
        int position;
        String id;

        IdPos(String id, int position) {
            this.id = id;
            this.position = position;
        }
    }
}
