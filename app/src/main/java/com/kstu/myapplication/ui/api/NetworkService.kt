package com.kstu.myapplication.ui.api

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*
import com.google.gson.GsonBuilder
import com.google.gson.Gson




class NetworkService private constructor() {
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

    init {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        mRetrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getUnsafeOkHttpClient().build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    private fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf() //To change body of created functions use File | Settings | File Templates.
                }

                @Throws(CertificateException::class)
                override fun checkClientTrusted(
                    chain: Array<X509Certificate>,
                    authType: String
                ) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(
                    chain: Array<X509Certificate>,
                    authType: String
                ) {
                }
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.getSocketFactory()

            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier { _, _ -> true }
            return builder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

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