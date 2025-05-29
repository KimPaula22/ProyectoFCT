package com.example.proyectofct.Controler.llamadas

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.proyectofct.Controler.RetrofitClient
import com.example.proyectofct.Controler.recibirMensajeDeError
import com.example.proyectofct.MainActivity.Companion.tokenDatabaseManager
import com.example.proyectofct.Model.Requests
import com.example.proyectofct.Model.Responses
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


fun realizarLogin(
    correo: String,
    contrasenia: String,
    intentos: Int = 0, // Parámetro para el contador de intentos
    onResultado: (mensaje: String) -> Unit
) {
    val loginRequest = Requests.LoginRequest(correo, contrasenia)

    RetrofitClient.instance.login(loginRequest).enqueue(object : Callback<Responses.LoginResponse> {
        override fun onResponse(call: Call<Responses.LoginResponse>, response: Response<Responses.LoginResponse>) {
            if (response.isSuccessful) {
                val tokens = response.body()
                if (tokens?.accessToken != null && tokens?.refreshToken != null) {
                    Log.d("LOGIN", "Token recibido: ${tokens.accessToken}")

                    // Guardar los tokens en SQLite
                    tokenDatabaseManager?.guardarTokens(tokens.accessToken, tokens.refreshToken)

                    // Pasamos el mensaje al callback
                    onResultado("Login exitoso")
                } else {
                    Log.e("LOGIN", "Token no recibido del servidor.")
                    onResultado("Token no recibido del servidor.")
                }
            } else {
                Log.e("LOGIN", "Error HTTP: ${response.code()} - ${response.errorBody()?.string()}")
                onResultado("Error en el login: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<Responses.LoginResponse>, t: Throwable) {
            Log.e("LOGIN", "Error de red: ${t.message}")

            if (intentos < 6) {
                // Si aún no hemos alcanzado el límite de intentos, intentamos de nuevo
                Log.d("LOGIN", "Intento ${intentos + 1} fallido. Reintentando...")

                // Usamos Handler para retrasar el siguiente intento
                Handler(Looper.getMainLooper()).postDelayed({
                    realizarLogin(correo, contrasenia,intentos + 1, onResultado )
                }, 7000) // 7 segundos de espera entre intentos
            } else {
                // Si ya hemos intentado 3 veces, mostramos el mensaje de error
                val mensaje = recibirMensajeDeError(t) + " Intentos agotados."
                Log.e("LOGIN", mensaje)
                onResultado(mensaje)
            }
        }
    })
}





//EJEMPLO DE USO
/*
realizarLogin("usuario@example.com", "123456",0) { mensaje ->
    Log.d("LOGIN", mensaje)
}
*/