package id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.viewmodel;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.api.ApiClient;
import id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.helper.NetworkScanHelper;
import id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.model.NetworkInfo;

public class NetworksViewModel extends ViewModel {
    private final NetworkScanHelper networkHelper;
    private final MutableLiveData<List<NetworkInfo>> liveNetworks;
    private final ApiClient api;

    public NetworksViewModel(Context context) {
        networkHelper = new NetworkScanHelper(context);
        liveNetworks = new MutableLiveData<>();
        api = new ApiClient();
        resetNetworks();
    }

    public MutableLiveData<List<NetworkInfo>> getLiveNetworks() {
        return liveNetworks;
    }

    public void resetNetworks() {
        liveNetworks.setValue(new ArrayList<>());
    }

    public void scanNetworks() {
        updateLiveNetworks(networkHelper.getNetworks());
    }

    public void updateLiveNetworks(List<NetworkInfo> networks) {
        new UpdateLiveNetworksAsyncTask(networks, liveNetworks.getValue()).execute();
    }

    public void fetchNetworks() {
        new FetchNetworksAsyncTask().execute();
    }

    public void uploadNetworks() {
        api.uploadNetworks(liveNetworks.getValue());
    }

    public void deleteNetworks() {
        api.deleteAllNetworks();
    }

    public class FetchNetworksAsyncTask extends AsyncTask<Void, Void, List<NetworkInfo>> {

        @Override
        protected List<NetworkInfo> doInBackground(Void... voids) {
            return api.fetchNetworks();
        }

        @Override
        protected void onPostExecute(List<NetworkInfo> networkInfos) {
            updateLiveNetworks(networkInfos);
        }
    }

    public class UpdateLiveNetworksAsyncTask extends AsyncTask<Void, Void, List<NetworkInfo>> {
        List<NetworkInfo> newNetworks;
        List<NetworkInfo> oldNetworks;

        public UpdateLiveNetworksAsyncTask(List<NetworkInfo> newNetworks,
                                           List<NetworkInfo> oldNetworks) {
            this.newNetworks = newNetworks;
            this.oldNetworks = oldNetworks;
        }

        @Override
        protected List<NetworkInfo> doInBackground(Void... voids) {
            List<NetworkInfo> networkInfos = ApiClient.convergeList(newNetworks, oldNetworks);
            networkInfos.sort((o1, o2) -> {
                int level = o2.getLevel() - o1.getLevel();
                if (level != 0) return level;
                return o1.getSsid().compareTo(o2.getSsid());
            });
            return networkInfos;
        }

        @Override
        protected void onPostExecute(List<NetworkInfo> networkInfos) {
            liveNetworks.setValue(networkInfos);
        }
    }
}
