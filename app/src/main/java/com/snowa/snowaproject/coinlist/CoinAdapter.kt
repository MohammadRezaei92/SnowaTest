package com.snowa.snowaproject.coinlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.snowa.snowaproject.data.Coin
import com.snowa.snowaproject.databinding.CoinItemBinding

class CoinAdapter : ListAdapter<Coin, CoinAdapter.CoinViewHolder>(diff) {

    lateinit var binding: CoinItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        binding = CoinItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CoinViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class CoinViewHolder(binding: CoinItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(coin: Coin) {
            binding.coinName.text = coin.name
            binding.coinPrice.text = "${coin.currentPrice} ${coin.symbol}"
            binding.marketCap.text = "Cap: ${coin.marketCap}"
            binding.coinHigh.text = "High: ${coin.high24h}"
            binding.coinLow.text = "Low: ${coin.low24h}"
            binding.coinIcon.load(coin.image)
        }
    }

    companion object {
        val diff = object : DiffUtil.ItemCallback<Coin>() {
            override fun areItemsTheSame(
                oldItem: Coin,
                newItem: Coin
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Coin,
                newItem: Coin
            ): Boolean =
                oldItem == newItem
        }
    }
}