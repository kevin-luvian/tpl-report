package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database.api

import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.inferred.CartApiModel
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.model.inferred.CartApiModelPost
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.*


private const val APIKEY = "06aa19f10bc1f4fd574f93f35529e0a679e12"
private const val BASE_URL = "https://mobileprogramming-0e17.restdb.io/rest/"

object ApiClient {
    private val cartQuery = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getCartMinderApi(): CartMinderAPI = cartQuery.create(CartMinderAPI::class.java)
}

interface CartMinderAPI {
    @GET("cartminder")
    suspend fun getCartApis(@Query("apikey") key: String = APIKEY): List<CartApiModel?>

    @GET("cartminder")
    suspend fun getCartApisAsModelPost(@Query("apikey") key: String = APIKEY): List<CartApiModelPost?>

    @POST("cartminder")
    suspend fun postCartApis(
        @Body c: CartApiModelPost,
        @Query("apikey") key: String = APIKEY
    ): CartApiModel

    @DELETE("cartminder/{id}")
    suspend fun deleteCartApi(
        @Path("id") id: String,
        @Query("apikey") key: String = APIKEY
    ): Response<DeleteResult>

    @DELETE("cartminder/*")
    suspend fun deleteAllCartApi(
        @Query("q") q: Map<String, Objects> = mapOf(),
        @Query("apikey") key: String = APIKEY
    ): Response<DeleteResult>
}

data class DeleteResult(var result: Any?)