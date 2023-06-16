package com.example.capstoneharvesthub.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.capstoneharvesthub.R
import com.example.capstoneharvesthub.data.utils.Product

@Composable
fun ProductItem(product: Product, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        AsyncImage(
            model = product.imageUrl,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .width(500.dp)
                .height(200.dp)

        )
        Row(
            modifier = Modifier
                .padding(10.dp, 10.dp, 0.dp, 10.dp)
        ) {
            Column {
                Text(
                    text = product.productName,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.subtitle1.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = stringResource(R.string.harga, product.price),
                    style = MaterialTheme.typography.subtitle2,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}