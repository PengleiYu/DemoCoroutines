package com.utopia.democoroutines

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel(
  private val loginRepository: LoginRepository
) : ViewModel() {

  fun login(username: String, token: String) {
    viewModelScope.launch {
      val jsonBody = "{ username: \"$username\", token: \"$token\"}"
      val result = loginRepository.makeLoginRequest(jsonBody)
      Log.d(TAG, "login: result=$result")
      Log.d(TAG, "login: ${Thread.currentThread()}")
//      when (result) {
//        is Result.Error -> return@launch result.exception.localizedMessage
//        is Result.Success -> TODO()
//      }
    }
  }

  companion object {
    private const val TAG = "viewmodel"
  }
}