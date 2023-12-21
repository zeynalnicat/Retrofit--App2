package com.matrix.android_104_android.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.matrix.android_104_android.MainActivity
import com.matrix.android_104_android.R
import com.matrix.android_104_android.databinding.FragmentLoginBinding
import com.matrix.android_104_android.model.users.LoginRequest
import com.matrix.android_104_android.retrofit.RetrofitInstance
import com.matrix.android_104_android.retrofit.UserApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)

        val activity = activity as MainActivity
        activity.setBottomNavigation(false)
        login()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = activity?.getSharedPreferences("userDetails", Context.MODE_PRIVATE)
        val isLogin = sharedPreferences?.getBoolean("isLogin", false)
        if (isLogin == true) {
            findNavController().navigate(R.id.action_loginFragment_to_productsFragment)
        }
    }

    private fun login() {
        binding.btn.setOnClickListener {
            val username = binding.edtUsername.text.toString()
            val password = binding.edtPassword.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                val retrofit = RetrofitInstance.userApi
                val response = retrofit.getToken(LoginRequest(username, password))
                if (response.isSuccessful) {
                    val token = response.body()?.token
                    val sPreference =
                        activity?.getSharedPreferences(
                            "userDetails",
                            Context.MODE_PRIVATE
                        )
                    val editor = sPreference?.edit()
                    val user = response.body()
                    user?.let { user ->
                        editor?.let {
                            it.putString("img", user.image)
                            it.putString("token", token)
                            it.putString("email", user.email)
                            it.putString("gender", user.gender)
                            it.putBoolean("isLogin", true)
                            it.putString(
                                "username",
                                user.firstName + " " + user.lastName
                            )
                            it.apply()
                        }

                    }

                    withContext(Dispatchers.Main) {
                        findNavController().navigate(R.id.action_loginFragment_to_productsFragment)
                    }
                } else {
                    Snackbar.make(
                        requireView(),
                        "Wrong username or password",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

            }

        }

    }
}
