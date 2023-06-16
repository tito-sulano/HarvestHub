package com.example.capstoneharvesthub.detail

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.capstoneharvesthub.R
import com.example.capstoneharvesthub.data.Resource
import com.example.capstoneharvesthub.data.utils.Product
import com.example.capstoneharvesthub.data.utils.RatingBarView
import com.example.capstoneharvesthub.navigation.Routes
import com.example.capstoneharvesthub.ui.theme.auth.AuthViewModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun DetailScreenProductPembeli(viewModel: AuthViewModel, navController: NavHostController, productId: String?) {
    val productDetailState by viewModel.productDetailState.collectAsState()

    val context = LocalContext.current

    val userID = viewModel.currentUser?.uid

    LaunchedEffect(key1 = productId) {
        if (productId != null) {
            if (userID != null) {
                viewModel.getDataProductPetaniId(userID, productId, context) { productDetail ->
                    productDetail.id
                }
            }
        }
    }

    when (val resource = productDetailState) {
        is Resource.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is Resource.Success -> {
            val productDetail = resource.result
            Scaffold(topBar = {
                TopAppBar(
                    backgroundColor = MaterialTheme.colors.primary,
                    elevation = 10.dp
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        tint = Color.White,
                        contentDescription = stringResource(R.string.back),
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable { navController.navigate(Routes.ROUTE_HOME_PEMBELI.route) },
                    )
                    Text(
                        text = stringResource(R.string.menu_detail),
                        modifier = Modifier.padding(horizontal = 12.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }, content = {
                Modifier.padding(it)
                Column(Modifier) {
                    DetailItem(product = productDetail)
                    Spacer(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth()
                            .height(4.dp)
                            .background(Color.LightGray)
                    )
                    Row(modifier = Modifier
                        .padding(20.dp)){
                        Image(
                            painter = painterResource(R.drawable.dummy_avatar),
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .padding(4.dp)
                                .border(3.dp, MaterialTheme.colors.primary, CircleShape)
                                .clip(CircleShape)
                                .size(70.dp)
                        )
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "Dummy",
                                fontWeight = FontWeight.Bold,
                            )

                            RatingBarView(
                                modifier = Modifier.padding(0.dp,10.dp,0.dp,0.dp),
                                rating = mutableStateOf(4),
                                isRatingEditable = false,
                                isViewAnimated = true,
                                starIcon = painterResource(id = R.drawable.icon_star),
                                unRatedStarsColor = Color.LightGray,
                                starsPadding = 5.dp
                            )
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .background(Color.LightGray)
                    )
                }
            })
        }

        is Resource.Failure -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Failed to load product detail")
            }
        }

        else -> {}
    }
}