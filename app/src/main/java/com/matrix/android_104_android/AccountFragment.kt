package com.matrix.android_104_android

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.matrix.android_104_android.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater)
        setLayout()
        setNavigation()
        return binding.root
    }


    private fun setLayout() {
        val sharedPreferences = activity?.getSharedPreferences("userDetails", Context.MODE_PRIVATE)
        val username = sharedPreferences?.getString("username", "N/A")
        val gender = sharedPreferences?.getString("gender", "Not assigned")
        val email = sharedPreferences?.getString("email", "Not assigned")
        val img = sharedPreferences?.getString("img", "")
        Glide.with(binding.root)
            .load(img)
            .into(binding.imgProfile)

        binding.txtUsername.text = username
        binding.txtEmail.text = email
        binding.txtGender.text = gender
    }

    private fun setNavigation() {
        val sharedPreferences = activity?.getSharedPreferences("userDetails", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        binding.button.setOnClickListener {
            editor?.remove("isLogin")
            val check = editor?.commit()
            if (check == true) {
                findNavController().navigate(R.id.action_accountFragment_to_loginFragment)
            }
        }
    }
}