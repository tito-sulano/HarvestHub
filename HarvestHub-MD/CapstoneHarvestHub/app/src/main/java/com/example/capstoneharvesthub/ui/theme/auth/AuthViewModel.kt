package com.example.capstoneharvesthub.ui.theme.auth

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.capstoneharvesthub.data.AuthRepository
import com.example.capstoneharvesthub.data.Resource
import com.example.capstoneharvesthub.data.utils.Product
import com.example.capstoneharvesthub.data.utils.Role
import com.example.capstoneharvesthub.data.utils.UserData
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow: StateFlow<Resource<FirebaseUser>?> = _loginFlow

    private val _signupFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val signupFlow: StateFlow<Resource<FirebaseUser>?> = _signupFlow

    private val _userRole: MutableStateFlow<Resource<Role>> = MutableStateFlow(Resource.Loading)
    val userRole: StateFlow<Resource<Role>> = _userRole.asStateFlow()

    private val _productListState: MutableStateFlow<Resource<List<Product>>?> = MutableStateFlow(null)
    val productListState: StateFlow<Resource<List<Product>>?> = _productListState

    private val _productDetailState: MutableStateFlow<Resource<Product>?> = MutableStateFlow(null)
    val productDetailState: StateFlow<Resource<Product>?> = _productDetailState

    val currentUser: FirebaseUser?
        get() = repository.currentUser

    init {
        viewModelScope.launch {
            if (repository.currentUser != null) {
                _loginFlow.value = Resource.Success(repository.currentUser!!)
            }
        }
    }

    fun loginUser(email: String, password: String,navController: NavHostController) = viewModelScope.launch {
        _loginFlow.value = Resource.Loading
        val result = repository.login(email, password, navController)
        _loginFlow.value = result

    }


    fun signupUser(user: UserData) = viewModelScope.launch {
        _signupFlow.value = Resource.Loading
        val result = repository.signup(user)
        _signupFlow.value = result
    }

    fun getUserRole() = viewModelScope.launch {
        _loginFlow.value = Resource.Loading
        val currentUser = repository.currentUser
        if (currentUser != null) {
            val result = repository.getUserRole(currentUser)
            // Mengubah _loginFlow menjadi _userRoleFlow sesuai dengan tujuan fungsi
            _userRole.value = result
        } else {
            _userRole.value = Resource.Failure(Exception("User not logged in"))
        }
    }

    fun saveDataPerusahaan(userDataPetani: UserData, context: Context) = viewModelScope.launch {
        _loginFlow.value = Resource.Loading
        val result = repository.saveDataPerusahaan(userDataPetani,context)
        _loginFlow.value = result
    }

    fun getDataUserPerusahaan(userID: String, context: Context, data: ((UserData) -> Unit)) = viewModelScope.launch {
        _loginFlow.value = Resource.Loading
        val result = data.let { repository.getDataUserPerusahaan(userID,context, it) }
        _loginFlow.value = result
    }

    fun saveDataPembeli(userDataPembeli: UserData, context: Context) = viewModelScope.launch {
        _loginFlow.value = Resource.Loading
        val result = repository.saveDataPembeli(userDataPembeli,context)
        _loginFlow.value = result
    }

    fun getDataUserPembeli(userID: String, context: Context, data: ((UserData) -> Unit)) = viewModelScope.launch {
        _loginFlow.value = Resource.Loading
        val result = data.let { repository.getDataUserPembeli(userID,context, it) }
        _loginFlow.value = result
    }

    fun saveDataPetani(userDataPetani: UserData, context: Context) = viewModelScope.launch {
        _loginFlow.value = Resource.Loading
        val result = repository.saveDataPetani(userDataPetani,context)
        _loginFlow.value = result
    }

    fun getDataUserPetani(userID: String, context: Context, data: ((UserData) -> Unit)) = viewModelScope.launch {
        _loginFlow.value = Resource.Loading
        val result = data.let { repository.getDataUserPetani(userID,context, it) }
        _loginFlow.value = result
    }

    fun saveDataProductPerusahaan(
        userDataPerusahaan: UserData,
        product: Product,
        context: Context
    ) = viewModelScope.launch {

        _loginFlow.value = Resource.Loading
        val result = repository.saveDataProductPerusahaan(userDataPerusahaan,product,context)
        _loginFlow.value = result
    }

    fun getDataProductPerusahaan(
        userID: String,
        context: Context,
        data: ((Product) -> Unit)?
    ) = viewModelScope.launch {
        _productListState.value = Resource.Loading
        val result = repository.getDataProductPerusahaan(userID, context){product ->
            println(product)
        }
        if (result is Resource.Success) {
            val productList = result.result
            if (productList.isNotEmpty()) {
                productList.forEach { product ->
                    data?.invoke(product)
                }
                _productListState.value = Resource.Success(productList)
            } else {
                _productListState.value = Resource.Failure(Exception("No Product Data Found"))
            }
        } else if (result is Resource.Failure) {
            _productListState.value = Resource.Failure(result.exception)
        }
    }

    fun saveDataProductPetani(
        userDataPetani: UserData,
        product: Product,
        context: Context
    ) = viewModelScope.launch {

        _loginFlow.value = Resource.Loading
        val result = repository.saveDataProductPetani(userDataPetani,product,context)
        _loginFlow.value = result
    }

    fun getDataProductPetani(
        userID: String,
        context: Context,
        data: ((Product) -> Unit)?
    ) = viewModelScope.launch {
        _productListState.value = Resource.Loading
        val result = repository.getDataProductPetani(userID, context){product ->
            println(product)
        }
        if (result is Resource.Success) {
            val productList = result.result
            if (productList.isNotEmpty()) {
                productList.forEach { product ->
                    data?.invoke(product)
                }
                _productListState.value = Resource.Success(productList)
            } else {
                _productListState.value = Resource.Failure(Exception("No Product Data Found"))
            }
        } else if (result is Resource.Failure) {
            _productListState.value = Resource.Failure(result.exception)
        }
    }

    fun getDataProductPerusahaanId(
        userID: String,
        productID: String,
        context: Context,
        data: ((Product) -> Unit)?
    ) = viewModelScope.launch {
        _productDetailState.value = Resource.Loading
        val result = repository.getDataProductPerusahaanId(userID, productID, context){product ->
            data?.invoke(product)
        }
        _productDetailState.value = result
    }

    fun getDataProductPetaniId(
        userID: String,
        productID: String,
        context: Context,
        data: ((Product) -> Unit)?
    ) = viewModelScope.launch {
        _productDetailState.value = Resource.Loading
        val result = repository.getDataProductPetaniId(userID, productID, context){product ->
            data?.invoke(product)
        }
        _productDetailState.value = result
    }

    fun getProductsFromPetani(context: Context) = viewModelScope.launch{
        _productListState.value = Resource.Loading
        val currentUser = repository.currentUser
        if (currentUser != null) {
            val result = repository.getProductsFromPetani(context)
            _productListState.value = result
        } else {
            _productListState.value = Resource.Failure(Exception("User not logged in"))
        }
    }

    fun logout() {
        repository.logout()
        _loginFlow.value = null
        _signupFlow.value = null
    }

}