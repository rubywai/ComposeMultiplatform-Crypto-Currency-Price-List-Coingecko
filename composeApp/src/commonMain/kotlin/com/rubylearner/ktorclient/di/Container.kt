package com.rubylearner.ktorclient.di

import com.rubylearner.ktorclient.services.ApiService
import com.rubylearner.ktorclient.services.Repository
import com.rubylearner.ktorclient.viewmodels.HomeViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val providehttpClientModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(json = Json { ignoreUnknownKeys = true }, contentType = ContentType.Any)
            }
        }
    }
}

val provideapiServiceModule = module{
    single { ApiService(get()) }
}

val provideRepositoryModule = module {
    single<Repository> { Repository(get()) }
}

val provideviewModelModule = module {
    viewModel<HomeViewModel> {
        HomeViewModel()
    }
}

fun appModule() = listOf(providehttpClientModule,
    provideapiServiceModule, provideRepositoryModule, provideviewModelModule)