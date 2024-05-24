package com.example.bobthedefender.ui.fragments.adapters

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bobthedefender.databinding.WeaponListItemBinding
import com.example.bobthedefender.ui.models.Weapon

class WeaponListAdapter : ListAdapter<Weapon, WeaponListAdapter.WeaponViewHolder>(DiffCallBack) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeaponViewHolder {
        return WeaponViewHolder(
            WeaponListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WeaponViewHolder, position: Int) {
        val current = getItem(position)

        holder.bind(current)
    }

    class WeaponViewHolder(
        private var binding: WeaponListItemBinding
    ) : ViewHolder(binding.root) {
        fun bind(weapon: Weapon) {
            binding.apply {
                weaponName.text = weapon.name
                weaponDamage.text = weapon.damage.toString()
                weaponCost.text = weapon.cost.toString()
            }
        }
    }

    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<Weapon>() {
            override fun areItemsTheSame(oldItem: Weapon, newItem: Weapon): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Weapon, newItem: Weapon): Boolean {
                return oldItem.name == newItem.name
                        && oldItem.damage == newItem.damage
                        && oldItem.cost == newItem.cost
            }
        }
    }
}