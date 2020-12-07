package id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.viewmodel;

import android.content.Context;
import android.os.AsyncTask;

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
        updateLiveNetworks(networkHelper.scanLocalNetworks());
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
        protected void onPostExecute(List<NetworkInfo> networkInfoList) {
            updateLiveNetworks(networkInfoList);
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
            List<NetworkInfo> networkInfoList = ApiClient.convergeNetworkInfoList(newNetworks, oldNetworks);
            networkInfoList.sort(NetworkInfo::compareNetworkInfo);
            return networkInfoList;
        }

        @Override
        protected void onPostExecute(List<NetworkInfo> networkInfoList) {
            liveNetworks.setValue(networkInfoList);
        }
    }
}
