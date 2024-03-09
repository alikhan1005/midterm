package com.example.aviatickets.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.aviatickets.R
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.aviatickets.activity.DiffUtil.OfferDiffCallback
import com.example.aviatickets.databinding.ItemOfferBinding
import com.example.aviatickets.model.entity.Offer

class OfferListAdapter : RecyclerView.Adapter<OfferListAdapter.ViewHolder>() {

    private var items: List<Offer> = arrayListOf()

    fun updateItems(newItems: List<Offer>) {
        val diffCallback = OfferDiffCallback(this.items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemOfferBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(private val binding: ItemOfferBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("StringFormatMatches")
        fun bind(offer: Offer) {
            val flight = offer.flight

            with(binding) {
                departureTime.text = flight.departureTimeInfo
                arrivalTime.text = flight.arrivalTimeInfo
                route.text = itemView.context.getString(
                    R.string.route_fmt,
                    flight.departureLocation.code,
                    flight.arrivalLocation.code
                )
                duration.text = itemView.context.getString(
                    R.string.time_fmt,
                    getTimeFormat(flight.duration).first,
                    getTimeFormat(flight.duration).second
                )
                direct.text = itemView.context.getString(R.string.direct)
                price.text = itemView.context.getString(R.string.price_fmt, offer.price.toString())
            }
        }

        private fun getTimeFormat(minutes: Int): Pair<Int, Int> = Pair(minutes / 60, minutes % 60)
    }
}
