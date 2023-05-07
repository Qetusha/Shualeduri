package com.qetushaq.qeti

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.qetushaq.qeti.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    val database = Firebase.database
    private val auth = Firebase.auth

    private val db2 = FirebaseDatabase.getInstance().getReference("user")
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(MainFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {
            init()
            when(it.itemId){

                R.id.home -> replaceFragment(MainFragment())
                R.id.profile -> replaceFragment(ProfileFragment())
                else ->{
                }
            }
            true
        }
        val database = Firebase.database
        val myRef = database.getReference("email")



        val emailID = FirebaseAuth.getInstance().currentUser!!.uid
        db.collection("user").document(emailID).get().addOnSuccessListener {
            if (it != null){
                val email = it.data?.get("email")?.toString()
                binding.email.text = email
            }
        }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

    }

    private fun init(){
        binding.apply {
            db2.child(auth.currentUser!!.uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userInfo = snapshot.getValue(User::class.java) ?: return
                    email.text = userInfo.email
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@MainActivity, "error", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.framelayout,fragment)
        fragmentTransaction.commit()
    }

}