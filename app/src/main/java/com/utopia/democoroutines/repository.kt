package com.utopia.democoroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

sealed class Result<out R> {
  data class Success<out T>(val data: T) : Result<T>()
  data class Error(val exception: Exception) : Result<Nothing>()
}

class LoginRepository(private val responseParser: LoginResponseParser) {

  // Function that makes the network request, blocking the current thread
  suspend fun makeLoginRequest(
    jsonBody: String
  ): Result<LoginResponse> {
    return withContext(Dispatchers.IO) {
      val url = URL(loginUrl)
      (url.openConnection() as? HttpURLConnection)?.run {
        requestMethod = "POST"
        setRequestProperty("Content-Type", "application/json; utf-8")
        setRequestProperty("Accept", "application/json")
        doOutput = true
        outputStream.write(jsonBody.toByteArray())
        return@withContext Result.Success(responseParser.parse(inputStream))
      }
      return@withContext Result.Error(Exception("Cannot open HttpURLConnection"))
    }
  }

  companion object {
    private const val loginUrl = "https://baidu.com"
  }
}

data class LoginResponse(val text: String)

class LoginResponseParser {
  fun parse(inputStream: InputStream?): LoginResponse {
    BufferedReader(InputStreamReader(inputStream)).use {
      val readText = it.readText()
      return LoginResponse(readText)
    }
  }
}
