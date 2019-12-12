package com.kstu.myapplication.ui.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class  RetroServer() {

    private var tok = ""
    constructor(token:String) : this(){
        tok=token
    }

    private val mRetrofit: Retrofit

    val teacherApi: TeacherApi
        get() = mRetrofit.create(TeacherApi::class.java)

    val lextureApi: LectureApi
        get() = mRetrofit.create(LectureApi::class.java)

    val courseApi:CourseApi
        get() = mRetrofit.create(CourseApi::class.java)

    val themeApi: ThemeApi
        get() = mRetrofit.create(ThemeApi::class.java)
    val typeOfLessonApi:TypeOfLessonApi
        get() = mRetrofit.create(TypeOfLessonApi::class.java)
    val login :LoginApi
        get() = mRetrofit.create(LoginApi::class.java)

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        // call token by preferences, look example in daron labs
        val httpClient = OkHttpClient.Builder()
            .connectTimeout(24, TimeUnit.HOURS)
            .readTimeout(24, TimeUnit.HOURS)
            .writeTimeout(24, TimeUnit.HOURS)
            .addInterceptor(interceptor)
            .addInterceptor {
                val original = it.request()
                val builder = original.newBuilder()
                builder.addHeader("Authorization", "Bearer $tok")
                builder.addHeader("Accept", "application/json; charset=UTF-8")
                builder.addHeader("Content-Type", "application/json; charset=UTF-8")

                val request = builder.build()
                return@addInterceptor it.proceed(request)
            }
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()
        mRetrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000/")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }


    companion object {
        private var mInstance: NetworkService? = null
        private val BASE_URL = "http://10.0.2.2:5000/"

        val instance: NetworkService
            get() {
                if (mInstance == null) {
                    mInstance =
                        NetworkService()
                }
                return mInstance!!
            }
    }
}