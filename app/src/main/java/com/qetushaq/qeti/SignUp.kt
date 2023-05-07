package com.qetushaq.qeti

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.qetushaq.qeti.databinding.ActivitySignUpBinding

class SignUp:AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        signupListeners()

        binding.SignIn.setOnClickListener{
            startActivity(Intent(this,SignIn::class.java))
            finish()
        }

    }

    private fun signupListeners() {
        binding.submit.setOnClickListener {
            val email = binding.edEmail.text.toString().trim()
            val pass = binding.edPassword.text.toString().trim()
            val confirmPass = binding.edConfPassword.text.toString().trim()




            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {

                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {

                            val user = hashMapOf(
                                "email" to email,
                                "password" to pass
                            )

                            val userId = FirebaseAuth.getInstance().currentUser!!.uid

                            db.collection("user").document(userId).set(user).addOnSuccessListener {
                                binding.edEmail.text?.clear()
                                binding.edPassword.text?.clear()

                            }


                            val intent = Intent(this, SignIn::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

                        }
                    }
                } else {
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()

            }
        }
    }

}