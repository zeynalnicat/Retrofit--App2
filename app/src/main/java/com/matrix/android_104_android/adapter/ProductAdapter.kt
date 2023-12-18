package com.matrix.android_104_android.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.matrix.android_104_android.databinding.ItemProductBinding
import com.matrix.android_104_android.model.product.Product
import com.matrix.android_104_android.model.product.Products


class ProductAdapter(private val products: Products) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(view)

    }

    override fun getItemCount(): Int {
        return products.products.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        return holder.bind(products.products[position])
    }

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product) {
            Glide.with(binding.root)
                .load(item.images[0])
                .into(binding.imageView)

            binding.txtTitleValue.text = item.title
            binding.txtCategoryValue.text = item.category
            binding.txtDescriptionValue.text = item.description
            binding.txtPriceValue.text = item.price.toString()
        }
    }

}
