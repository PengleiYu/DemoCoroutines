package com.utopia.democoroutines

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
  private val viewModel = LoginViewModel(LoginRepository(LoginResponseParser()))

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    findViewById<Button>(R.id.btn_login).setOnClickListener {
      viewModel.login("", "")
    }
  }
}