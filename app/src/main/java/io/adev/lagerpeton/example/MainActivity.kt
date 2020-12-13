package io.adev.lagerpeton.example

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sendEventButton = findViewById<Button>(R.id.sendEventButton)
        sendEventButton.setOnClickListener {
            viewModel.sendEventClicked()
        }

        val runUseCaseButton = findViewById<Button>(R.id.runUseCaseButton)
        runUseCaseButton.setOnClickListener {
            viewModel.runUseCaseClicked()
        }
    }
}
