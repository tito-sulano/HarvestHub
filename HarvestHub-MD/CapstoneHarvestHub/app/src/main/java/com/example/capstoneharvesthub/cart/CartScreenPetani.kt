package com.example.capstoneharvesthub.cart

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.capstoneharvesthub.R
import com.example.capstoneharvesthub.navigation.Routes
import com.example.capstoneharvesthub.ui.theme.auth.AuthViewModel

@Composable
fun CartScreenPetani(viewModel: AuthViewModel?, navController: NavHostController) {

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
                    .clickable {
                        navController.navigate(Routes.ROUTE_HOME_PETANI.route) {
                            popUpTo(Routes.ROUTE_HOME_PETANI.route) { inclusive = true }
                        }
                    }
            )
            Text(
                text = stringResource(R.string.menu_keranjang),
                modifier = Modifier.padding(horizontal = 12.dp),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        }
    }, content = {
        Box {
            Column(
                modifier = Modifier
                    .padding(it)
            ) {
                Text(text = "Cart Screen Petani")
            }
        }
    })

}
