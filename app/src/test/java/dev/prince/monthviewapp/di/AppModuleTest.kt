package dev.prince.monthviewapp.di

import dev.prince.monthviewapp.network.ApiService
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppModuleTest {

    private lateinit var retrofit: Retrofit
    private lateinit var apiService: ApiService

    @Before
    fun setup() {
        retrofit = Retrofit.Builder()
            .baseUrl("http://dev.frndapp.in:8085")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    @Test
    fun `verify retrofit provides correct base URL`() {
        val actualBaseUrl = retrofit.baseUrl().toString().removeSuffix("/")
        assertEquals("http://dev.frndapp.in:8085", actualBaseUrl)
    }

    @Test
    fun `verify ApiService is provided`() {
        assertNotNull(apiService)
    }
}
