package com.example.capstoneharvesthub.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.capstoneharvesthub.R
import com.example.capstoneharvesthub.navigation.Routes
import com.example.capstoneharvesthub.ui.theme.auth.AuthViewModel

@Composable
fun ProfileScreenPembeli(viewModel: AuthViewModel?, navController: NavHostController) {

    val userID = viewModel?.currentUser?.uid
    var name: String by remember { mutableStateOf("") }
    var address: String by remember { mutableStateOf("") }
    var contactNumber: String by remember { mutableStateOf("") }
    var email: String by remember { mutableStateOf("") }
    var image: String by remember { mutableStateOf("") }
    var role: String by remember { mutableStateOf("pembeli") }

    val context = LocalContext.current

    LaunchedEffect(key1 = userID) {
        if (userID != null) {
            viewModel.getDataUserPembeli(userID = userID, context = context) {data ->
                name = data.name
                address = data.address
                contactNumber = data.contactNumber
                email = data.email
                image = data.image
                role = data.image
            }
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = Color.White,
            elevation = 10.dp
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            navController.navigate(Routes.ROUTE_HOME_PEMBELI.route)
                        },
                    tint = Color.White
                )
                Text(
                    text = stringResource(R.string.menu_profile),
                    modifier = Modifier.weight(1f).padding(end = 220.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "edit",
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .clickable {
                            navController.navigate(Routes.ROUTE_UPDATE_PROFILE_PEMBELI.route){
                                popUpTo(Routes.ROUTE_UPDATE_PROFILE_PEMBELI.route) { inclusive = true }
                            }
                        },
                    tint = Color.White
                )
            }
        }
    }, content = {
        Column(
            modifier = Modifier
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .padding(20.dp)
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .align(Alignment.CenterHorizontally)
            ) {
                AsyncImage(
                    model = image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize(),
                )
            }
            Row(
                modifier = Modifier
                    .padding(30.dp)
                    .padding(20.dp, 0.dp, 0.dp, 0.dp)
            ) {
                Column {
                    Text(
                        text = name,
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = address,
                        color = Color.Black,
                        fontSize = 16.sp,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = contactNumber,
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = email,
                        color = Color.Black,
                        fontSize = 16.sp,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
            Button(
                onClick = {
                    viewModel?.logout()
                    navController.navigate(Routes.ROUTE_LOGIN.route) {
                        popUpTo(Routes.ROUTE_LOGIN.route) { inclusive = true }
                    }
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(64.dp)
                    .height(50.dp)
                    .width(250.dp)


            ) {
                Text(text = stringResource(id = R.string.logout), textAlign = TextAlign.Center)
            }
        }
    })
}
