package com.matrix.android_104_android.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.matrix.android_104_android.MainActivity
import com.matrix.android_104_android.adapter.ProductAdapter
import com.matrix.android_104_android.databinding.FragmentProductsBinding
import com.matrix.android_104_android.model.product.Products
import com.matrix.android_104_android.retrofit.ProductApi
import com.matrix.android_104_android.retrofit.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class ProductsFragment : Fragment() {
    private lateinit var binding: FragmentProductsBinding
    private var retrofit: ProductApi? = null
    private var adapter: ProductAdapter? = null
    private var token: String = ""
    private var selectedCategory: String = "All categories"
    private var listCategory: MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductsBinding.inflate(layoutInflater)
        getToken()
        setRetrofit()
        setSpinner()
        setAdapter()
        val activity = activity as MainActivity
        activity.setBottomNavigation(true)
        return binding.root

    }

    private fun setRetrofit() {
        retrofit = RetrofitInstance.getProductApi(token)
    }

    private fun getToken() {
        val sharedPreference = activity?.getSharedPreferences("userDetails", Context.MODE_PRIVATE)
        token = sharedPreference?.getString("token", "").toString()

    }

    private fun setSpinner() {

        val spinner = binding.spinner
        CoroutineScope(Dispatchers.IO).launch {
            val categoriesResponse = retrofit?.getCategories()
            val categories = categoriesResponse?.body()


            withContext(Dispatchers.Main) {
                if (categoriesResponse?.isSuccessful == true) {
                    listCategory.add("All categories")
                    categories?.let {
                        it.let { it1 -> listCategory.addAll(it1) }
                    }

                    val arrayAdapter = ArrayAdapter(
                        requireContext(), android.R.layout.simple_spinner_item, listCategory
                    )
                    spinner.adapter = arrayAdapter

                    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long
                        ) {
                            selectedCategory = listCategory[p2]
                            binding.spinner.setSelection(p2)
                            setAdapter()
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {
                        }
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "There was a problem in fetching categories, Please log in again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setAdapter() {
        CoroutineScope(Dispatchers.IO).launch {
            var productsResponse: Response<Products>?
            if (selectedCategory == "All categories") {
                productsResponse = retrofit?.getProducts()


            } else {
                productsResponse = retrofit?.getSpecific(selectedCategory)
            }

            Log.i("products", productsResponse.toString())
            val products = productsResponse?.body()

            withContext(Dispatchers.Main) {
                if (products?.products?.size == 0) {
                    binding.progressBar.visibility = View.VISIBLE
                }

                if (productsResponse?.isSuccessful == true) {
                    products?.let {
                        binding.progressBar.visibility = View.GONE
                        adapter = ProductAdapter()
                        adapter!!.submitList(it.products)
                        binding.recyclerView.layoutManager =LinearLayoutManager(binding.root.context)
                        binding.recyclerView.adapter = adapter
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "There was a problem in fetching products, Please log in again",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }
    }
}