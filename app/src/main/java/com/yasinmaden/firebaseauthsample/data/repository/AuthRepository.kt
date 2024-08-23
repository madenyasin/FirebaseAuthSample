package com.yasinmaden.firebaseauthsample.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.yasinmaden.firebaseauthsample.common.Resource
import com.yasinmaden.firebaseauthsample.data.UserData
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(private val auth: FirebaseAuth) {
    fun isUserLoggedIn(): Boolean = auth.currentUser != null

    suspend fun signUp(email: String, password: String): Resource<String> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            Resource.Success(result.user?.uid.orEmpty())
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun signIn(email: String, password: String): Resource<String> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result.user?.uid.orEmpty())
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    fun getCurrentUserData(): Resource<UserData> {
        return try {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                val userData = UserData(
                    name = currentUser.displayName.orEmpty(),
                    email = currentUser.email.orEmpty(),
                    profilePicUrl = currentUser.photoUrl?.toString().orEmpty()
                )
                Resource.Success(userData)
            } else {
                Resource.Error(Exception("User not logged in"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    fun signOut(): Resource<Unit> {
        return try {
            auth.signOut()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

}