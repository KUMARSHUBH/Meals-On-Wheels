package com.krshubham.mealsonwheels

import com.krshubham.mealsonwheels.response.SearchResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


const val API_KEY = "AIzaSyC1s20ONtors2iUHx9k89QavKG3lzOo-f0"

interface YouTubeService {


    @GET("search")
    fun getVideos(

        @Query("part") part:String = "snippet",
        @Query("q") q: String = "recipe",
        @Query("order") order:String = "relevance",
        @Query("maxResults") maxResults:Int = 50,
        @Query("safeSearch") safeSearch: String = "strict",
        @Query("type") type: String = "video"
    ): Call<SearchResponse>


    companion object {
        operator fun invoke(): YouTubeService {

            val requestInterceptor = Interceptor { chain ->

                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("key", API_KEY)
                    .build()

                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/youtube/v3/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(YouTubeService::class.java)
        }
    }
}