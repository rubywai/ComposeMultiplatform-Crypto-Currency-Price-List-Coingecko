package com.rubylearner.ktorclient.services

import com.rubylearner.ktorclient.models.PriceModels
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url

class ApiService(private val httpClient: HttpClient) {
    private val coinBaseURl = "https://api.coingecko.com/api/v3/coins/markets"
    suspend fun getPriceApi(
        vsCurrency: String = "usd",
        perPage: Int = 20,
        page: Int = 1,
    ): List<PriceModels> {
        return httpClient.get {
            url(coinBaseURl)
            url {
                parameters.append("vs_currency", vsCurrency)
                parameters.append("per_page", perPage.toString())
                parameters.append("page", page.toString())
            }
        }.body<List<PriceModels>>()
    }

}