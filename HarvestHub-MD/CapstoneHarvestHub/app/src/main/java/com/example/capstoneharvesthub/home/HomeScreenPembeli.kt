package com.example.capstoneharvesthub.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.example.capstoneharvesthub.NavigationItem
import com.example.capstoneharvesthub.R
import com.example.capstoneharvesthub.data.Resource
import com.example.capstoneharvesthub.data.utils.Product
import com.example.capstoneharvesthub.navigation.Routes
import com.example.capstoneharvesthub.ui.theme.auth.AuthViewModel

@Composable
fun HomeScreenPembeli(viewModel: AuthViewModel, navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val productListState by viewModel.productListState.collectAsState()

    val context = LocalContext.current

    val userID = viewModel.currentUser?.uid
    var productName: String by remember { mutableStateOf("") }
    var price: Int by remember { mutableStateOf(0) }
    var demand: String by remember { mutableStateOf("") }
    var imageUrl: String by remember { mutableStateOf("") }
    var description: String by remember { mutableStateOf("") }


    LaunchedEffect(key1 = userID) {
        if (userID != null) {
            viewModel.getDataProductPetani(userID = userID, context = context) { product ->
                productName = product.productName
                price = product.price
                demand = product.demand
                imageUrl = product.imageUrl
                description = product.description
            }
        }
    }

    when (val resource = productListState) {
        is Resource.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is Resource.Success -> {
            val productList = resource.result

            productList.let { list ->
                Scaffold(
                    topBar = {
                        TopAppBar(
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = Color.White,
                            elevation = 10.dp
                        ) {
                            Text(
                                text = stringResource(R.string.menu_home),
                                modifier = Modifier.padding(horizontal = 12.dp),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center, color = Color.White
                            )
                        }
                    },
                    content = {
                        Modifier.padding(it)
                        if (list.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Data Kosong")
                            }
                        } else {
                            Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(bottom = 56.dp)) {
                                Box(modifier = Modifier) {
                                    Image(
                                        painter = painterResource(R.drawable.banner),
                                        contentDescription = "Banner Image",
                                        contentScale = ContentScale.FillWidth,
                                        modifier = Modifier.height(200.dp)
                                    )
//                                    Search(
//                                        query = query,
//                                        onQueryChange = onQueryChange
//                                    )
//                                    if (keranjangBarang.isEmpty()) {
//                                        EmptyList(
//                                            Warning = stringResource(androidx.compose.foundation.layout.R.string.data_tidak_ada)
//                                        )
//                                    }
                                }
                                Text(
                                    modifier = Modifier.padding(30.dp, 5.dp, 0.dp, 5.dp),
                                    text = stringResource(R.string.pilihan),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )

                                val rows = productList.chunked(2)

                                for (row in rows) {
                                    Row(
                                        modifier = Modifier
                                            .padding(horizontal = 20.dp)
                                    ) {
                                        for (item in row) {
                                            Card(
                                                elevation = 15.dp,
                                                shape = RoundedCornerShape(10.dp),
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .padding(8.dp)
                                                    .clickable {
                                                        navController.navigate("${Routes.ROUTE_DETAIL_PRODUCT_PEMBELI.route}/${item.id}")
                                                    }
                                            ) {
                                                ProductItemPembeli(product = item)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    },
                    bottomBar = {
                        if (currentRoute == Routes.ROUTE_HOME_PEMBELI.route) {
                            BottomBar(navController)
                        }
                    },
                )
            }
        }

        is Resource.Failure -> {
            Scaffold(bottomBar = {
                if (currentRoute == Routes.ROUTE_HOME_PEMBELI.route) {
                    BottomBar(navController)
                }
            }, content = {
                Modifier.padding(it)
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    androidx.compose.material.Text("No Data Product")
                }
            }
            )
        }

        else -> {}
    }
}

@Composable
private fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    BottomNavigation(
        modifier = modifier
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val navigationItems = listOf(
            NavigationItem(
                title = stringResource(R.string.menu_home),
                icon = Icons.Default.Home,
                screen = Routes.ROUTE_HOME_PEMBELI
            ),
            NavigationItem(
                title = stringResource(R.string.menu_keranjang),
                icon = Icons.Default.ShoppingCart,
                screen = Routes.ROUTE_CART_PEMBELI
            ),
            NavigationItem(
                title = stringResource(R.string.menu_profile),
                icon = Icons.Default.AccountCircle,
                screen = Routes.ROUTE_PROFILE_PEMBELI
            ),
        )
        BottomNavigation {
            navigationItems.map { item ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title
                        )
                    },
                    selected = currentRoute == item.screen.route,
                    onClick = {
                        navController.navigate(item.screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ProductItemPembeli(product: Product, modifier: Modifier = Modifier) {
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
                    style = androidx.compose.material.MaterialTheme.typography.subtitle1.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                androidx.compose.material.Text(
                    text = stringResource(R.string.harga, product.price),
                    style = androidx.compose.material.MaterialTheme.typography.subtitle2,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}