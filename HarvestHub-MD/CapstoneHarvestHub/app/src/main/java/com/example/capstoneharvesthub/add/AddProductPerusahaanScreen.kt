package com.example.capstoneharvesthub.add

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
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
import com.example.capstoneharvesthub.data.utils.Product
import com.example.capstoneharvesthub.data.utils.UserData
import com.example.capstoneharvesthub.navigation.Routes
import com.example.capstoneharvesthub.ui.theme.auth.AuthViewModel
import java.util.UUID

@Composable
fun AddProductPerusahaanScreen(viewModel: AuthViewModel?, navController: NavHostController) {

    val userID = viewModel?.currentUser?.uid
    var price: Int by remember { mutableStateOf(0) }
    var demand: String by remember { mutableStateOf("") }
    var imageUrl: String by remember { mutableStateOf("") }
    var description: String by remember { mutableStateOf("") }
    var productName: String by remember { mutableStateOf("") }

    val context = LocalContext.current

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
                        navController.navigate(Routes.ROUTE_HOME_PERUSAHAAN.route)
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
    }) { it ->
        Column(
            modifier = Modifier
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .padding(20.dp)
                    .size(200.dp)
                    .clip(RectangleShape)
                    .background(Color.Gray)
                    .align(Alignment.CenterHorizontally)
            ) {
                RequestContentPermission {
                    imageUrl = it.toString()
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
                        value = productName,
                        onValueChange = {
                            productName = it
                        },
                        label = {
                            Text(text = "Nama Barang")
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )
                    )
                    // Profession
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = price.toString(),
                        onValueChange = {
                            price = it.toInt()
                        },
                        label = {
                            Text(text = "Harga")
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
                        value = demand,
                        onValueChange = {
                            demand = it
                        },
                        label = {
                            Text(text = "Kebutuhan")
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )
                    )
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = description,
                        onValueChange = {
                            description = it
                        },
                        label = {
                            Text(text = "Deskripsi")
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
                            val userDataPerusahaan = userID?.let {
                                UserData(
                                    userID = it,
                                )
                            }

                            val productData = Product(
                                id = UUID.randomUUID().toString(),
                                productName = productName,
                                price = price,
                                demand = demand,
                                imageUrl = imageUrl,
                                description = description
                            )

                            if (userDataPerusahaan != null) {
                                viewModel.saveDataProductPerusahaan(
                                    userDataPerusahaan = userDataPerusahaan,
                                    product = productData,
                                    context = context
                                )
                                Toast.makeText(
                                    context,
                                    "Data Berhasil Ditambahkan",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    ) {
                        Text(text = "Upload")
                    }
                }
            }
        }
    }
}