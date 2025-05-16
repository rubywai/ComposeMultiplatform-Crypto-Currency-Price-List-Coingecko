package com.rubylearner.ktorclient.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.rubylearner.ktorclient.models.PriceModels
import com.rubylearner.ktorclient.services.PriceUiState
import com.rubylearner.ktorclient.viewmodels.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.pow
import kotlin.math.round

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(){
    val viewModel: HomeViewModel = koinViewModel()
    val state = viewModel.cryptoStateList.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.fetchPriceList(page = 1)
    }


    Column {
        println(state.value)
        when (state.value) {

            is PriceUiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,      // centers vertically
                    horizontalAlignment = Alignment.CenterHorizontally  // centers horizontally

                ) {
                    Text("Failed to Load")
                    ElevatedButton({
                        viewModel.fetchPriceList(page = 1)
                    }){
                        Text("Reload")
                    }
                }
            }
            PriceUiState.Loading -> {
                ProgressLoader(isLoading = true)
            }
            is PriceUiState.Success -> {
                (state.value as PriceUiState.Success).priceList  .let {
                    PullToRefreshBox(
                        isRefreshing = false,
                        onRefresh = {
                            viewModel.fetchPriceList(1)
                        },
                        contentAlignment = Alignment.TopStart,


                    ) {
                        LazyColumn {
                            items(it) { item ->
                                CryptoPriceItem(item)
                            }
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun ProgressLoader(isLoading: Boolean) {
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}


@Composable
fun CryptoPriceItem(coin: PriceModels) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .background(Color(0xFFF0F0F0), RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // KamelImage to load network image

        AsyncImage(
            model = coin.image,
            contentDescription = coin.name,
            modifier = Modifier.size(40.dp),
            contentScale = ContentScale.Fit,

        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = coin.name ?: "",
                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = coin.symbol?.uppercase() ?: "",
                style = MaterialTheme.typography.body2,
                color = Color.Gray
            )
        }

        Text(
            text = "$${(coin.currentPrice ?: 0.0).toDouble().format(2)}",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(end = 16.dp)
        )

        val changeColor = if ((coin.priceChangePercentage24h ?: 0.0) >= 0.0) Color(0xFF4CAF50) else Color(0xFFF44336)
        Text(
            text = "${coin.priceChangePercentage24h?.format(2)}%",
            color = changeColor,
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.Bold
        )
    }

}
fun Double.format(digits: Int = 2): String {
    val multiplier = 10.0.pow(digits)
    return (round(this * multiplier) / multiplier).toString()
}
