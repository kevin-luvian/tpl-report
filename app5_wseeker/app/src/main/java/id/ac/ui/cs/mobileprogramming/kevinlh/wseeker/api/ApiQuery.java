package id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.api;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.model.NetworkInfo;
import id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.model.NetworkInfoPost;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiQuery {
    @GET("networks")
    Call<List<NetworkInfo>> getNetworks(@Query("apikey") String key);

    @POST("networks")
    Call<NetworkInfoPost> postNetwork(@Query("apikey") String key, @Body NetworkInfoPost networkInfoPost);

    @PUT("networks/{id}")
    Call<NetworkInfoPost> putNetwork(@Path("id") String id, @Query("apikey") String key, @Body NetworkInfoPost networkInfoPost);

    @DELETE("networks/{id}")
    Call<NetworkInfo> deleteNetwork(@Path("id") String id, @Query("apikey") String key);
}
