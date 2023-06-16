package com.example.capstoneharvesthub.data

import android.content.Context
import android.net.Uri
import androidx.navigation.NavHostController
import com.example.capstoneharvesthub.data.utils.Product
import com.example.capstoneharvesthub.data.utils.Role
import com.example.capstoneharvesthub.data.utils.UserData
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String,navController: NavHostController): Resource<FirebaseUser>
    suspend fun signup(user: UserData): Resource<FirebaseUser>
    suspend fun getUserRole(user: FirebaseUser): Resource<Role>
    suspend fun saveDataPerusahaan(userDataPerusahaan: UserData, context: Context): Resource<FirebaseUser>
    suspend fun getDataUserPerusahaan(userID: String, context: Context,data: (UserData) -> Unit): Resource<FirebaseUser>
    suspend fun saveDataPembeli(userDataPembeli: UserData, context: Context): Resource<FirebaseUser>
    suspend fun getDataUserPembeli(userID: String, context: Context,data: (UserData) -> Unit): Resource<FirebaseUser>
    suspend fun saveDataPetani(userDataPetani: UserData, context: Context): Resource<FirebaseUser>
    suspend fun getDataUserPetani(userID: String, context: Context,data: (UserData) -> Unit): Resource<FirebaseUser>
    suspend fun saveDataProductPerusahaan(userDataPerusahaan: UserData, product: Product, context: Context): Resource<FirebaseUser>
    suspend fun getDataProductPerusahaan(userID: String, context: Context,data: (Product) -> Unit): Resource<List<Product>>
    suspend fun saveDataProductPetani(userDataPetani: UserData, product: Product, context: Context): Resource<FirebaseUser>
    suspend fun getDataProductPetani(userID: String, context: Context,data: (Product) -> Unit): Resource<List<Product>>
    suspend fun getDataProductPerusahaanId(userID: String,productID: String, context: Context,data: (Product) -> Unit): Resource<Product>
    suspend fun getDataProductPetaniId(userID: String,productID: String, context: Context,data: (Product) -> Unit): Resource<Product>
    suspend fun getProductsFromPetani(context: Context): Resource<List<Product>>
    fun logout()
}