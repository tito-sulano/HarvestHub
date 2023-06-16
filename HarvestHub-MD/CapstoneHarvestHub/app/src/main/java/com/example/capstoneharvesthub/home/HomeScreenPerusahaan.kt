package com.example.capstoneharvesthub.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.capstoneharvesthub.NavigationItem
import com.example.capstoneharvesthub.R
import com.example.capstoneharvesthub.data.Resource
import com.example.capstoneharvesthub.navigation.Routes
import com.example.capstoneharvesthub.ui.theme.auth.AuthViewModel

@Composable
fun HomeScreenPerusahaan(viewModel: AuthViewModel, navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val productListState by viewModel.productListState.collectAsState()

    val userID = viewModel.currentUser?.uid
    var productName: String by remember { mutableStateOf("") }
    var price: Int by remember { mutableStateOf(0) }
    var demand: String by remember { mutableStateOf("") }
    var imageUrl: String by remember { mutableStateOf("") }
    var description: String by remember { mutableStateOf("") }

    val context = LocalContext.current

    LaunchedEffect(key1 = userID) {
        if (userID != null) {
            viewModel.getDataProductPerusahaan(userID = userID, context = context) { product ->
                productName = product.productName
                price = product.price
                demand = product.demand
                imageUrl = product.imageUrl
                description = product.description
            }
        }
    }

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
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }
        },
        content = {
            Box(
                modifier = Modifier.padding(it)
            ) {
                when (val resource = productListState) {
                    is Resource.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    is Resource.Success -> {
                        val productList = resource.result

                        if (productList.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Data Kosong")
                            }
                        } else {
                            LazyColumn(modifier = Modifier.fillMaxWidth().padding(bottom = 56.dp)) {
                                items(productList) { product ->
                                    Card(
                                        elevation = 15.dp,
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier
                                            .padding(20.dp, 10.dp)
                                            .width(500.dp)
                                            .height(260.dp)
                                            .clickable {
                                                navController.navigate("${Routes.ROUTE_DETAIL_PRODUCT_PERUSAHAAN.route}/${product.id}")
                                            }
                                    ) {
                                        ProductItem(product = product)
                                    }
                                }
                            }
                        }
                    }

                    is Resource.Failure -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No Data Product")
                        }
                    }

                    else -> {}
                }
            }
        },
        bottomBar = {
            if (currentRoute == Routes.ROUTE_HOME_PERUSAHAAN.route) {
                BottomBar(navController)
            }
        }
    )
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
                screen = Routes.ROUTE_HOME_PERUSAHAAN
            ),
            NavigationItem(
                title = stringResource(R.string.menu_add),
                icon = Icons.Default.Add,
                screen = Routes.ROUTE_ADD_PRODUCT_PERUSAHAAN
            ),
            NavigationItem(
                title = stringResource(R.string.menu_keranjang),
                icon = Icons.Default.ShoppingCart,
                screen = Routes.ROUTE_CART_PERUSAHAAN
            ),
            NavigationItem(
                title = stringResource(R.string.menu_profile),
                icon = Icons.Default.AccountCircle,
                screen = Routes.ROUTE_PROFILE_PERUSAHAAN
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