package com.example.practica12

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserListActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var userListView: ListView
    private lateinit var userList: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        // Inicializar la base de datos y la lista
        database = FirebaseDatabase.getInstance("https://practica12-36084-default-rtdb.firebaseio.com/").getReference("users")
        userListView = findViewById(R.id.user_list_view)
        userList = mutableListOf()

        // Leer datos de Firebase
        fetchUsersFromDatabase()

    }

    private fun fetchUsersFromDatabase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear() // Limpia la lista antes de actualizar
                for (userSnapshot in snapshot.children) {
                    val name = userSnapshot.child("name").getValue(String::class.java)
                    val email = userSnapshot.child("email").getValue(String::class.java)

                    if (name != null && email != null) {
                        userList.add("$name - $email")
                    }
                }
                // Actualizar el ListView
                val adapter = ArrayAdapter(this@UserListActivity, android.R.layout.simple_list_item_1, userList)
                userListView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@UserListActivity, "Error al cargar usuarios: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}