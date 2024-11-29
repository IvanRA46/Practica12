package com.example.practica12

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        database = FirebaseDatabase.getInstance("https://practica12-36084-default-rtdb.firebaseio.com/").reference

        val btnRegister = findViewById<Button>(R.id.btn_register_user)
        val editEmail = findViewById<EditText>(R.id.edit_email)
        val editPassword = findViewById<EditText>(R.id.edit_password)
        val editName = findViewById<EditText>(R.id.edit_name)


        btnRegister.setOnClickListener {
            val email = editEmail.text.toString().trim()
            val password = editPassword.text.toString().trim()
            val name = editName.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()) {
                saveUserToDatabase(name, email, password)
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        val btnShowUsers = findViewById<Button>(R.id.btn_mostrar)
        btnShowUsers.setOnClickListener {
            val intent = Intent(this, UserListActivity::class.java)
            startActivity(intent)
        }


    }

    private fun saveUserToDatabase(name: String, email: String, password: String) {
        // Crear un objeto de usuario
        val user = User(name, email, password)

        // Guardar en la base de datos bajo un nodo "users"
        val userId = database.child("users").push().key // Genera una clave única
        if (userId != null) {
            database.child("users").child(userId).setValue(user)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                        finish() // Cierra la actividad después de registrar
                    } else {
                        Toast.makeText(this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Error al generar la ID del usuario", Toast.LENGTH_SHORT).show()
        }
    }
}

data class User(val name: String, val email: String, val password: String)
