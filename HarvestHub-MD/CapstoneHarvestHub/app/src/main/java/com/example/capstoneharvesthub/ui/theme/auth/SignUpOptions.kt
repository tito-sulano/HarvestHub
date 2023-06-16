package com.example.capstoneharvesthub.ui.theme.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.capstoneharvesthub.R
import com.example.capstoneharvesthub.navigation.Routes

@Composable
fun SignupScreenOption(
    navController: NavHostController,
    onRoleSelected: (String) -> Unit
) {


    Column(
        modifier = Modifier
            .background(color = Color(0xFFE5E5E5))
    ) {
        Box(
            modifier = Modifier
                .width(500.dp)
                .height(230.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.masktanaman),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxSize()
            )

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.Center)
            )
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "SignUp Sebagai Pengguna Aplikasi",
                fontSize = 20.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            SignupOption(imageId = R.drawable.petani, text = "Petani", onClick = {
                onRoleSelected("petani")
                navController.navigate("${Routes.ROUTE_SIGNUP_PETANI.route}/petani")
            })
            SignupOption(imageId = R.drawable.perusahaan, text = "Perusahaan", onClick = {
                onRoleSelected("perusahaan")
                navController.navigate("${Routes.ROUTE_SIGNUP_PERUSAHAAN.route}/perusahaan")
            })
            SignupOption(imageId = R.drawable.pembeli, text = "Pembeli", onClick = {
                onRoleSelected("pembeli")
                navController.navigate("${Routes.ROUTE_SIGNUP_PEMBELI.route}/pembeli")
            })
            Row(modifier = Modifier.padding(top = 8.dp)) {
                Text(text = "Sudah Memiliki Akun?", fontSize = 18.sp)
                Text(modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable {
                        navController.navigate(Routes.ROUTE_LOGIN_OPTIONS.route) {
                            popUpTo(Routes.ROUTE_LOGIN_OPTIONS.route) { inclusive = true }
                        }
                    }, text = "Login", fontSize = 18.sp, color = Color.Green)
            }
        }
    }
}

@Composable
fun SignupOption(imageId: Int, text: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(top = 8.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 8.dp),

            contentScale = ContentScale.FillBounds
        )
        Text(text = text, color = Color.DarkGray, fontSize = 14.sp)
    }
}