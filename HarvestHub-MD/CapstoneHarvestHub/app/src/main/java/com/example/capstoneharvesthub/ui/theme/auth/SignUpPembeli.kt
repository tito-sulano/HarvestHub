package com.example.capstoneharvesthub.ui.theme.auth

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.example.capstoneharvesthub.R
import com.example.capstoneharvesthub.data.Resource
import com.example.capstoneharvesthub.data.utils.UserData
import com.example.capstoneharvesthub.navigation.Routes
import com.example.capstoneharvesthub.ui.theme.spacing
import com.google.firebase.auth.FirebaseUser


@Composable
fun SignupScreenPembeli(viewModel: AuthViewModel?, navController: NavHostController, role: String) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current

    val signupFlow = viewModel?.signupFlow?.collectAsState()

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (refHeader, refName, refEmail, refPassword, refButtonSignup, refTextSignup, refLoader) = createRefs()
        val spacing = MaterialTheme.spacing

        Box(
            modifier = Modifier
                .constrainAs(refHeader) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                .wrapContentSize()
        ) {
            AuthHeader()
        }

        TextField(
            value = name,
            onValueChange = {
                name = it
            },
            label = {
                Text(text = stringResource(id = R.string.name))
            },
            modifier = Modifier.constrainAs(refName) {
                top.linkTo(refHeader.bottom, spacing.extraLarge)
                start.linkTo(parent.start, spacing.large)
                end.linkTo(parent.end, spacing.large)
                width = Dimension.fillToConstraints
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        TextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text(text = stringResource(id = R.string.email))
            },
            modifier = Modifier.constrainAs(refEmail) {
                top.linkTo(refName.bottom, spacing.medium)
                start.linkTo(parent.start, spacing.large)
                end.linkTo(parent.end, spacing.large)
                width = Dimension.fillToConstraints
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        TextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text(text = stringResource(id = R.string.password))
            },
            modifier = Modifier.constrainAs(refPassword) {
                top.linkTo(refEmail.bottom, spacing.medium)
                start.linkTo(parent.start, spacing.large)
                end.linkTo(parent.end, spacing.large)
                width = Dimension.fillToConstraints
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )

        Button(
            onClick = {
                if (name.isNotBlank() && email.isNotBlank() && password.isNotBlank() ) {
                    viewModel?.signupUser(UserData(name = name, email = email, password = password, role = role, userID = ""))
                } else {
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }

            },
            modifier = Modifier.constrainAs(refButtonSignup) {
                top.linkTo(refPassword.bottom, spacing.large)
                start.linkTo(parent.start, spacing.extraLarge)
                end.linkTo(parent.end, spacing.extraLarge)
                width = Dimension.fillToConstraints
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
        ) {
            Text(text = stringResource(id = R.string.SignUp), style = MaterialTheme.typography.titleMedium)
        }


        Text(
            modifier = Modifier
                .constrainAs(refTextSignup) {
                    top.linkTo(refButtonSignup.bottom, spacing.medium)
                    start.linkTo(parent.start, spacing.extraLarge)
                    end.linkTo(parent.end, spacing.extraLarge)
                }
                .clickable {
                    navController.navigate(Routes.ROUTE_LOGIN.route) {
                        popUpTo(Routes.ROUTE_LOGIN.route) { inclusive = true }
                    }
                },
            text = stringResource(id = R.string.already_have_account),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )

        signupFlow?.value?.let {
            when (it) {
                is Resource.Failure -> {
                    Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()
                }
                Resource.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.constrainAs(refLoader){
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.end)
                    })
                }

                is Resource.Success -> {
                    val firebaseUser = it.result as FirebaseUser?
                    val uid = firebaseUser?.uid
                    if (uid != null) {
                        viewModel.signupUser(UserData(name = name, email = email, password = password, role = role, userID = uid))
                        LaunchedEffect(Unit) {
                            navController.navigate(Routes.ROUTE_LOGIN.route) {
                                popUpTo(Routes.ROUTE_LOGIN.route) { inclusive = true }
                            }
                        }
                        Toast.makeText(context, "User Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to get UID", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }
}
