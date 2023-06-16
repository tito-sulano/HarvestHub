package com.example.capstoneharvesthub.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.capstoneharvesthub.ImageUri.RequestContentPermission
import com.example.capstoneharvesthub.R
import com.example.capstoneharvesthub.data.utils.UserData
import com.example.capstoneharvesthub.navigation.Routes
import com.example.capstoneharvesthub.ui.theme.auth.AuthViewModel

@Composable
fun UpdateProfileScreenPetani(
    viewModel: AuthViewModel?,
    navController: NavHostController
) {
    val userID = viewModel?.currentUser?.uid
    var name: String by remember { mutableStateOf(viewModel?.currentUser?.displayName ?: "") }
    var address: String by remember { mutableStateOf("") }
    var contactNumber: String by remember { mutableStateOf("") }
    var email: String by remember { mutableStateOf(viewModel?.currentUser?.email ?: "") }
    var image: String by remember { mutableStateOf("") }
    var role: String by remember { mutableStateOf("petani") }

    val context = LocalContext.current

    LaunchedEffect(key1 = userID) {
        if (userID != null) {
            viewModel.getDataUserPetani(userID = userID, context = context) { data ->
                name = data.name
                address = data.address
                contactNumber = data.contactNumber
                email = data.email
                image = data.image
                role = data.role
            }
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = Color.White,
            elevation = 10.dp
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(R.string.back),
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        navController.navigate(Routes.ROUTE_PROFILE_PETANI.route)
                    },
                tint = Color.White
            )
            Text(
                text = stringResource(R.string.menu_profile),
                modifier = Modifier.padding(horizontal = 12.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center, color = Color.White
            )
        }
    }, content = { it ->
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
                RequestContentPermission {
                    image = it.toString()
                }
            }
            Row(
                modifier = Modifier
                    .padding(30.dp)
                    .padding(20.dp, 0.dp, 0.dp, 0.dp)
            ) {
                Column {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = name,
                        onValueChange = {
                            name = it
                        },
                        label = {
                            Text(text = "Nama")
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Characters,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )
                    )
                    // Profession
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = address,
                        onValueChange = {
                            address = it
                        },
                        label = {
                            Text(text = "Alamat")
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Characters,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )
                    )
                    // Age
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = contactNumber,
                        onValueChange = {
                            contactNumber = it
                        },
                        label = {
                            Text(text = "Nomor Telepon")
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        )
                    )
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = email,
                        onValueChange = {
                            email = it
                        },
                        label = {
                            Text(text = "Email")
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )
                    )
                    // save Button
                    Button(
                        modifier = Modifier
                            .padding(top = 50.dp)
                            .fillMaxWidth(),
                        onClick = {
                            val userDataPetani = userID?.let {
                                UserData(
                                    userID = it,
                                    name = name,
                                    address = address,
                                    contactNumber = contactNumber,
                                    email = email,
                                    image = image,
                                    role = role
                                )
                            }

                            if (userDataPetani != null) {
                                viewModel.saveDataPetani(
                                    userDataPetani = userDataPetani,
                                    context = context
                                )
                            }
                            navController.navigate(Routes.ROUTE_UPDATE_PROFILE_PETANI.route) {
                                popUpTo(Routes.ROUTE_UPDATE_PROFILE_PETANI.route) { inclusive = true }
                            }
                        }
                    ) {
                        Text(text = "Save")
                    }

                }
            }
        }
    })
}