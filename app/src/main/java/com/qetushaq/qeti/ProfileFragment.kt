package com.qetushaq.qeti

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.qetushaq.qeti.R
import com.qetushaq.qeti.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private lateinit var binding: FragmentProfileBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        binding.logout.setOnClickListener{
            Firebase.auth.signOut()
            activity?.let{
                val intent = Intent (it, SignIn::class.java)
                it.startActivity(intent)
            }
        }
    }

}