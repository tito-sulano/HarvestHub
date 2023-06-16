package com.example.capstoneharvesthub.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.capstoneharvesthub.add.AddProductPerusahaanScreen
import com.example.capstoneharvesthub.add.AddProductPetaniScreen
import com.example.capstoneharvesthub.cart.CartScreenPembeli
import com.example.capstoneharvesthub.cart.CartScreenPerusahaan
import com.example.capstoneharvesthub.cart.CartScreenPetani
import com.example.capstoneharvesthub.data.Resource
import com.example.capstoneharvesthub.data.utils.Role
import com.example.capstoneharvesthub.detail.DetailScreenProductPembeli
import com.example.capstoneharvesthub.detail.DetailScreenProductPerusahaan
import com.example.capstoneharvesthub.detail.DetailScreenProductPetani
import com.example.capstoneharvesthub.home.HomeScreenPembeli
import com.example.capstoneharvesthub.home.HomeScreenPerusahaan
import com.example.capstoneharvesthub.home.HomeScreenPetani
import com.example.capstoneharvesthub.profile.ProfileScreenPembeli
import com.example.capstoneharvesthub.profile.ProfileScreenPerusahaan
import com.example.capstoneharvesthub.profile.ProfileScreenPetani
import com.example.capstoneharvesthub.profile.UpdateProfileScreenPembeli
import com.example.capstoneharvesthub.profile.UpdateProfileScreenPerusahaan
import com.example.capstoneharvesthub.profile.UpdateProfileScreenPetani
import com.example.capstoneharvesthub.splash.SplashScreen
import com.example.capstoneharvesthub.ui.theme.auth.AuthViewModel
import com.example.capstoneharvesthub.ui.theme.auth.LoginScreen
import com.example.capstoneharvesthub.ui.theme.auth.SignupScreenOption
import com.example.capstoneharvesthub.ui.theme.auth.SignupScreenPembeli
import com.example.capstoneharvesthub.ui.theme.auth.SignupScreenPerusahaan
import com.example.capstoneharvesthub.ui.theme.auth.SignupScreenPetani

@Composable
fun AppNavHost(
    viewModel: AuthViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {

    val userRole by viewModel.userRole.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getUserRole()
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Routes.ROUTE_SPLASH.route
    ) {
        composable(Routes.ROUTE_SPLASH.route) {
            SplashScreen(navController)
        }
        composable(Routes.ROUTE_LOGIN.route) {
            LoginScreen(viewModel, navController)
        }

        composable(Routes.ROUTE_SIGNUP_OPTIONS.route) {
            SignupScreenOption(navController) { role ->
                navController.navigate(Routes.ROUTE_SIGNUP_PEMBELI.route + "/$role")
                navController.navigate(Routes.ROUTE_SIGNUP_PERUSAHAAN.route + "/$role")
                navController.navigate(Routes.ROUTE_SIGNUP_PETANI.route + "/$role")
            }
        }
        composable(
            route = Routes.ROUTE_SIGNUP_PEMBELI.route + "/{role}",
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: ""
            SignupScreenPembeli(viewModel, navController, role)
        }
        composable(
            route = Routes.ROUTE_SIGNUP_PERUSAHAAN.route + "/{role}",
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: ""
            SignupScreenPerusahaan(viewModel, navController, role)
        }
        composable(
            route = Routes.ROUTE_SIGNUP_PETANI.route + "/{role}",
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: ""
            SignupScreenPetani(viewModel, navController, role)
        }


        composable(Routes.ROUTE_HOME_PEMBELI.route) {
            when (userRole) {
                is Resource.Success -> {
                    if ((userRole as Resource.Success<Role>).result == Role.PEMBELI) {
                        HomeScreenPembeli(viewModel, navController)
                    } else {
                        navController.navigate(Routes.ROUTE_LOGIN.route) {
                            popUpTo(Routes.ROUTE_LOGIN.route) {
                                inclusive = true
                            }
                        }
                    }
                }

                is Resource.Failure -> {
                }

                is Resource.Loading -> {
                }
            }
        }

        composable(Routes.ROUTE_HOME_PERUSAHAAN.route) {
            when (userRole) {
                is Resource.Success -> {
                    if ((userRole as Resource.Success<Role>).result == Role.PERUSAHAAN) {
                        HomeScreenPerusahaan(viewModel, navController)
                    } else {
                        navController.navigate(Routes.ROUTE_LOGIN.route) {
                            popUpTo(Routes.ROUTE_LOGIN.route) {
                                inclusive = true
                            }
                        }
                    }
                }

                is Resource.Failure -> {
                }

                is Resource.Loading -> {
                }
            }
        }

        composable(Routes.ROUTE_HOME_PETANI.route) {
            when (userRole) {
                is Resource.Success -> {
                    if ((userRole as Resource.Success<Role>).result == Role.PETANI) {
                        HomeScreenPetani(viewModel, navController)
                    } else {
                        navController.navigate(Routes.ROUTE_LOGIN.route) {
                            popUpTo(Routes.ROUTE_LOGIN.route) {
                                inclusive = true
                            }
                        }
                    }
                }

                is Resource.Failure -> {
                }

                is Resource.Loading -> {
                }
            }
        }

        composable(Routes.ROUTE_PROFILE_PERUSAHAAN.route) {
            ProfileScreenPerusahaan(viewModel, navController)
        }
        composable(Routes.ROUTE_UPDATE_PROFILE_PERUSAHAAN.route) {
            UpdateProfileScreenPerusahaan(viewModel, navController)
        }
        composable(Routes.ROUTE_PROFILE_PETANI.route) {
            ProfileScreenPetani(viewModel, navController)
        }
        composable(Routes.ROUTE_UPDATE_PROFILE_PETANI.route) {
            UpdateProfileScreenPetani(viewModel, navController)
        }
        composable(Routes.ROUTE_PROFILE_PEMBELI.route) {
            ProfileScreenPembeli(viewModel, navController)
        }
        composable(Routes.ROUTE_UPDATE_PROFILE_PEMBELI.route) {
            UpdateProfileScreenPembeli(viewModel, navController)
        }
        composable(Routes.ROUTE_CART_PEMBELI.route) {
                CartScreenPembeli(viewModel, navController)
        }
        composable(Routes.ROUTE_CART_PERUSAHAAN.route) {
            CartScreenPerusahaan(viewModel, navController)
        }
        composable(Routes.ROUTE_CART_PETANI.route) {
            CartScreenPetani(viewModel, navController)
        }
        composable("${Routes.ROUTE_DETAIL_PRODUCT_PERUSAHAAN.route}/{id}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("id")
            DetailScreenProductPerusahaan(viewModel, navController, productId)
        }
        composable("${Routes.ROUTE_DETAIL_PRODUCT_PETANI.route}/{id}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("id")
            DetailScreenProductPetani(viewModel, navController, productId)
        }
        composable("${Routes.ROUTE_DETAIL_PRODUCT_PEMBELI.route}/{id}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("id")
            DetailScreenProductPembeli(viewModel, navController, productId)
        }
        composable(Routes.ROUTE_ADD_PRODUCT_PERUSAHAAN.route) {
            AddProductPerusahaanScreen(viewModel, navController)
        }
        composable(Routes.ROUTE_ADD_PRODUCT_PETANI.route) {
            AddProductPetaniScreen(viewModel, navController)
        }
    }
}