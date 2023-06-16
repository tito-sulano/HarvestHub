package com.example.capstoneharvesthub.data

import android.content.Context
import android.widget.Toast
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import com.example.capstoneharvesthub.data.utils.Product
import com.example.capstoneharvesthub.data.utils.Role
import com.example.capstoneharvesthub.data.utils.UserData
import com.example.capstoneharvesthub.navigation.Routes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String, navController: NavHostController): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user!!


            firebaseUser.uid.let { userId ->
                val db = Firebase.firestore
                val userDocRef = db.collection("users").document(userId)
                val userSnapshot = userDocRef.get().await()

                if (userSnapshot.exists()) {
                    val userData = userSnapshot.toObject(UserData::class.java)
                    userData?.let { user ->
                        when (user.role) {
                            "pembeli" -> {
                                navController.navigate(Routes.ROUTE_HOME_PEMBELI.route)
                            }
                            "perusahaan" -> {
                                navController.navigate(Routes.ROUTE_HOME_PERUSAHAAN.route)
                            }
                            "petani" -> {
                                navController.navigate(Routes.ROUTE_HOME_PETANI.route)
                            }

                            else -> {}
                        }
                    }
                } else {
                    val userData = UserData(
                        userID = userId,
                        name = "",
                        email = email,
                        role = "",
                        password = ""
                    )
                    userDocRef.set(userData).await()
                }
            }

            Resource.Success(firebaseUser)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    override suspend fun signup(user: UserData): Resource<FirebaseUser> {
        return try {
            val result =
                firebaseAuth.createUserWithEmailAndPassword(user.email,user.password).await()
            val firebaseUser = result.user

            firebaseUser?.uid?.let { userId ->
                user.userID = userId

                val db = Firebase.firestore
                val userDocRef = db.collection("users").document(userId)

                val userSnapshot = userDocRef.get().await()
                if (userSnapshot.exists()) {
                    Resource.Failure(Exception("User with the same ID already exists"))
                } else {
                    userDocRef.set(user).await()
                    Resource.Success(firebaseUser)
                }
            } ?: Resource.Failure(Exception("User creation failed."))

        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    override suspend fun getUserRole(user: FirebaseUser): Resource<Role> {
        val userId = user.uid
        val db = Firebase.firestore
        val userDocRef = db.collection("users").document(userId)
        val userSnapshot = userDocRef.get().await()

        return if (userSnapshot.exists()) {
            val userData = userSnapshot.toObject<UserData>()
            val role = when (userData?.role) {
                "pembeli" -> Role.PEMBELI
                "perusahaan" -> Role.PERUSAHAAN
                "petani" -> Role.PETANI
                else -> Role.UNKNOWN
            }
            Resource.Success(role)
        } else {
            Resource.Failure(Exception("User data not found"))
        }
    }

    override suspend fun saveDataPerusahaan(userDataPerusahaan: UserData, context: Context): Resource<FirebaseUser> {

        val firestore = Firebase.firestore
        val firebaseStorage = FirebaseStorage.getInstance()
        val storageRef: StorageReference = firebaseStorage.reference

        val imageRef = storageRef.child("image/${userDataPerusahaan.userID}.jpg")
        val uploadTask = imageRef.putFile(userDataPerusahaan.image.toUri())

        return try {
            uploadTask.await()

            val downloadUrl = imageRef.downloadUrl.await().toString()

            userDataPerusahaan.image = downloadUrl

            firestore.collection("perusahaan")
                .document(userDataPerusahaan.userID)
                .set(userDataPerusahaan)
                .addOnSuccessListener {
                    Toast.makeText(context, "Successfully saved data", Toast.LENGTH_SHORT).show()
                }

            Resource.Success(firebaseAuth.currentUser!!)
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            Resource.Failure(e)
        }
    }

    override suspend fun getDataUserPerusahaan(userID: String, context: Context, data: (UserData) -> Unit): Resource<FirebaseUser> {
        val fireStoreRef = Firebase.firestore
            .collection("perusahaan")
            .document(userID)

        return try {
            val snapshot = fireStoreRef.get().await()
            if (snapshot.exists()) {
                val userData = snapshot.toObject<UserData>()!!
                data(userData)
                Resource.Success(firebaseAuth.currentUser!!)
            } else {
                Toast.makeText(context, "No User Data Found", Toast.LENGTH_SHORT).show()
                Resource.Failure(Exception("No User Data Found"))
            }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            Resource.Failure(e)
        }
    }

    override suspend fun saveDataPembeli(userDataPembeli: UserData, context: Context): Resource<FirebaseUser> {

        val firestore = Firebase.firestore
        val firebaseStorage = FirebaseStorage.getInstance()
        val storageRef: StorageReference = firebaseStorage.reference

        val imageRef = storageRef.child("image/${userDataPembeli.userID}.jpg")
        val uploadTask = imageRef.putFile(userDataPembeli.image.toUri())

        return try {
            uploadTask.await()

            val downloadUrl = imageRef.downloadUrl.await().toString()

            userDataPembeli.image = downloadUrl

            firestore.collection("pembeli")
                .document(userDataPembeli.userID)
                .set(userDataPembeli)
                .addOnSuccessListener {
                    Toast.makeText(context, "Successfully saved data", Toast.LENGTH_SHORT).show()
                }

            Resource.Success(firebaseAuth.currentUser!!)
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            Resource.Failure(e)
        }
    }

    override suspend fun getDataUserPembeli(userID: String, context: Context, data: (UserData) -> Unit): Resource<FirebaseUser> {
        val fireStoreRef = Firebase.firestore
            .collection("pembeli")
            .document(userID)

        return try {
            val snapshot = fireStoreRef.get().await()
            if (snapshot.exists()) {
                val userData = snapshot.toObject<UserData>()!!
                data(userData)
                Resource.Success(firebaseAuth.currentUser!!)
            } else {
                Toast.makeText(context, "No User Data Found", Toast.LENGTH_SHORT).show()
                Resource.Failure(Exception("No User Data Found"))
            }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            Resource.Failure(e)
        }
    }

    override suspend fun saveDataPetani(userDataPetani: UserData, context: Context): Resource<FirebaseUser> {

        val firestore = Firebase.firestore
        val firebaseStorage = FirebaseStorage.getInstance()
        val storageRef: StorageReference = firebaseStorage.reference

        val imageRef = storageRef.child("image/${userDataPetani.userID}.jpg")
        val uploadTask = imageRef.putFile(userDataPetani.image.toUri())

        return try {
            uploadTask.await()

            val downloadUrl = imageRef.downloadUrl.await().toString()

            userDataPetani.image = downloadUrl

            firestore.collection("petani")
                .document(userDataPetani.userID)
                .set(userDataPetani)
                .addOnSuccessListener {
                    Toast.makeText(context, "Successfully saved data", Toast.LENGTH_SHORT).show()
                }

            Resource.Success(firebaseAuth.currentUser!!)
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            Resource.Failure(e)
        }
    }

    override suspend fun getDataUserPetani(userID: String, context: Context, data: (UserData) -> Unit): Resource<FirebaseUser> {
        val fireStoreRef = Firebase.firestore
            .collection("petani")
            .document(userID)

        return try {
            val snapshot = fireStoreRef.get().await()
            if (snapshot.exists()) {
                val userData = snapshot.toObject<UserData>()!!
                data(userData)
                Resource.Success(firebaseAuth.currentUser!!)
            } else {
                Toast.makeText(context, "No User Data Found", Toast.LENGTH_SHORT).show()
                Resource.Failure(Exception("No User Data Found"))
            }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            Resource.Failure(e)
        }
    }

    override suspend fun saveDataProductPerusahaan(userDataPerusahaan: UserData, product: Product, context: Context): Resource<FirebaseUser> {
        val firestore = Firebase.firestore
        val firebaseStorage = FirebaseStorage.getInstance()
        val storageRef: StorageReference = firebaseStorage.reference

        val imageRef = storageRef.child("imageProduct/${product.id}.jpg")
        val uploadTask = imageRef.putFile(product.imageUrl.toUri())

        return try {
            uploadTask.await()

            val downloadUrl = imageRef.downloadUrl.await().toString()

            userDataPerusahaan.image = downloadUrl

            firestore.collection("product")
                .document(product.id)
                .set(product)
                .await()

            Resource.Success(firebaseAuth.currentUser!!)
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            Resource.Failure(e)
        }
    }

    override suspend fun getDataProductPerusahaan(
        userID: String,
        context: Context,
        data: (Product) -> Unit
    ): Resource<List<Product>> {
        val fireStoreRef = Firebase.firestore
            .collection("product")

        return try {
            val snapshot = fireStoreRef.get().await()
            val productList = mutableListOf<Product>()

            for (document in snapshot.documents) {
                val product = document.toObject<Product>()
                if (product != null ) {
                    productList.add(product)
                    data(product)
                }
            }

            if (productList.isNotEmpty()) {
                Resource.Success(productList)
            } else {
                Toast.makeText(context, "No User Data Found", Toast.LENGTH_SHORT).show()
                Resource.Failure(Exception("No User Data Found"))
            }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            Resource.Failure(e)
        }
    }

    override suspend fun saveDataProductPetani(userDataPetani: UserData,product: Product, context: Context): Resource<FirebaseUser> {
        val firestore = Firebase.firestore
        val firebaseStorage = FirebaseStorage.getInstance()
        val storageRef: StorageReference = firebaseStorage.reference

        val imageRef = storageRef.child("imageProduct/${product.id}.jpg")
        val uploadTask = imageRef.putFile(product.imageUrl.toUri())

        return try {
            uploadTask.await()

            val downloadUrl = imageRef.downloadUrl.await().toString()

            userDataPetani.image = downloadUrl

            firestore.collection("productPetani")
                .document(product.id)
                .set(product)
                .await()

            Resource.Success(firebaseAuth.currentUser!!)

        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            Resource.Failure(e)
        }
    }

    override suspend fun getDataProductPetani(
        userID: String,
        context: Context,
        data: (Product) -> Unit
    ): Resource<List<Product>> {
        val fireStoreRef = Firebase.firestore
            .collection("productPetani")

        return try {
            val snapshot = fireStoreRef.get().await()
            val productList = mutableListOf<Product>()

            for (document in snapshot.documents) {
                val product = document.toObject<Product>()
                if (product != null ) {
                    productList.add(product)
                    data(product)
                }
            }

            if (productList.isNotEmpty()) {
                Resource.Success(productList)

            } else {
                Toast.makeText(context, "No User Data Found", Toast.LENGTH_SHORT).show()
                Resource.Failure(Exception("No User Data Found"))
            }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            Resource.Failure(e)
        }
    }

    override suspend fun getDataProductPerusahaanId(
        userID: String,
        productID: String,
        context: Context,
        data: (Product) -> Unit
    ): Resource<Product> {
        val fireStoreRef = Firebase.firestore
            .collection("product")
            .document(productID)

        return try {
            val snapshot = fireStoreRef.get().await()
            if (snapshot.exists()) {
                val productDetail = snapshot.toObject<Product>()
                if (productDetail != null) {
                    data(productDetail)
                    Resource.Success(productDetail)
                } else {
                    Toast.makeText(context, "No Product Detail Found", Toast.LENGTH_SHORT).show()
                    Resource.Failure(Exception("No Product Detail Found"))
                }
            } else {
                Toast.makeText(context, "No Product Found", Toast.LENGTH_SHORT).show()
                Resource.Failure(Exception("No Product Found"))
            }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            Resource.Failure(e)
        }
    }

    override suspend fun getDataProductPetaniId(
        userID: String,
        productID: String,
        context: Context,
        data: (Product) -> Unit
    ): Resource<Product> {
        val fireStoreRef = Firebase.firestore
            .collection("productPetani")
            .document(productID)

        return try {
            val snapshot = fireStoreRef.get().await()
            if (snapshot.exists()) {
                val productDetail = snapshot.toObject<Product>()
                if (productDetail != null) {
                    data(productDetail)
                    Resource.Success(productDetail)
                } else {
                    Toast.makeText(context, "No Product Detail Found", Toast.LENGTH_SHORT).show()
                    Resource.Failure(Exception("No Product Detail Found"))
                }
            } else {
                Toast.makeText(context, "No Product Found", Toast.LENGTH_SHORT).show()
                Resource.Failure(Exception("No Product Found"))
            }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            Resource.Failure(e)
        }
    }

    override suspend fun getProductsFromPetani(context: Context): Resource<List<Product>> {
        val currentUser = firebaseAuth.currentUser
        val firestore = Firebase.firestore

        return try {
            if (currentUser != null) {
                val userID = currentUser.uid
                val productCollection = firestore.collection("petani").document(userID).collection("product")
                val snapshot = productCollection.get().await()

                val productList = mutableListOf<Product>()
                for (document in snapshot.documents) {
                    val product = document.toObject(Product::class.java)
                    product?.let {
                        productList.add(it)
                    }
                }

                Resource.Success(productList)
            } else {
                Resource.Failure(Exception("User not logged in"))
            }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            Resource.Failure(e)
        }
    }


    override fun logout() {
        firebaseAuth.signOut()
    }

}