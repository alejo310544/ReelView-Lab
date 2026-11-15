
package com.reelview.lab

import android.app.Activity
import android.os.Bundle
import android.os.SystemClock
import android.content.Intent
import android.net.Uri
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*

class MainActivity : Activity() {

    private lateinit var timer: Chronometer
    private lateinit var linkInput: EditText
    private lateinit var sessionsText: TextView

    private var sessions = 0
    private var running = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(28, 35, 28, 35)
            setBackgroundColor(Color.rgb(15, 23, 42))
        }

        fun text(value: String, size: Float, color: Int): TextView {
            return TextView(this).apply {
                text = value
                textSize = size
                setTextColor(color)
                gravity = Gravity.CENTER
            }
        }

        val title = text("▶ REELVIEW LAB", 27f, Color.WHITE)
        layout.addView(title)

        layout.addView(
            text("Laboratorio de reproducción", 15f,
                Color.LTGRAY)
        )

        val subtitle = text(
            "Pruebas de Reels de Facebook",
            19f, Color.WHITE
        )
        subtitle.setPadding(0, 35, 0, 15)
        layout.addView(subtitle)

        linkInput = EditText(this).apply {
            hint = "Pega aquí el enlace del Reel"
            setSingleLine(true)
            setTextColor(Color.WHITE)
            setHintTextColor(Color.GRAY)
            setPadding(15, 12, 15, 12)
            setBackgroundColor(Color.rgb(45, 55, 72))
        }

        layout.addView(linkInput)

        val openButton = Button(this).apply {
            text = "ABRIR REEL"
        }
        layout.addView(openButton)

        timer = Chronometer(this).apply {
            textSize = 34f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(0, 30, 0, 20)
        }
        layout.addView(timer)

        val startButton = Button(this).apply {
            text = "INICIAR PRUEBA"
        }
        layout.addView(startButton)

        val stopButton = Button(this).apply {
            text = "FINALIZAR PRUEBA"
        }
        layout.addView(stopButton)

        sessionsText = text(
            "Sesiones registradas: 0",
            17f, Color.WHITE
        )
        sessionsText.setPadding(0, 30, 0, 10)
        layout.addView(sessionsText)

        layout.addView(
            text(
                "Las sesiones son registros locales, " +
                "no visualizaciones oficiales de Facebook.",
                12f, Color.LTGRAY
            )
        )

        openButton.setOnClickListener {
            val address = linkInput.text.toString().trim()
            val uri = Uri.parse(address)
            val host = uri.host?.lowercase() ?: ""

            if (uri.scheme == "https" &&
                (host == "facebook.com" ||
                 host.endsWith(".facebook.com") ||
                 host == "fb.watch")) {

                startActivity(
                    Intent(Intent.ACTION_VIEW, uri)
                )
            } else {
                Toast.makeText(
                    this,
                    "Introduce un enlace válido de Facebook",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        startButton.setOnClickListener {
            if (!running) {
                timer.base = SystemClock.elapsedRealtime()
                timer.start()
                running = true
            }
        }

        stopButton.setOnClickListener {
            if (running) {
                timer.stop()
                running = false
                sessions++
                sessionsText.text =
                    "Sesiones registradas: $sessions"
            }
        }

        setContentView(
            ScrollView(this).apply {
                addView(
                    layout,
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                )
            }
        )
    }
}
