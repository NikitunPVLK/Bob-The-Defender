package com.example.bobthedefender.ui.fragments.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bobthedefender.R
import com.example.bobthedefender.databinding.WeaponListItemBinding
import com.example.bobthedefender.ui.models.Weapon

class WeaponListAdapter(
    private val resources: Resources,
    private val isBuyEnabled: (Weapon) -> Boolean,
    private val onBuyClicked: (Weapon) -> Unit,
    private val equipWeapon: (Weapon) -> Unit,
    private val isWeaponEquipped: (Weapon) -> Boolean
) : ListAdapter<Weapon, WeaponListAdapter.WeaponViewHolder>(DiffCallBack) {

    override fun submitList(list: List<Weapon>?) {
        super.submitList(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeaponViewHolder {
        return WeaponViewHolder(
            WeaponListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            resources,
            isBuyEnabled,
            onBuyClicked,
            equipWeapon,
            isWeaponEquipped
        )
    }

    override fun onBindViewHolder(holder: WeaponViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class WeaponViewHolder(
        private var binding: WeaponListItemBinding,
        private var resources: Resources,
        private val isBuyEnabled: (Weapon) -> Boolean,
        private val onBuyClicked: (Weapon) -> Unit,
        private val equipWeapon: (Weapon) -> Unit,
        private val isWeaponEquipped: (Weapon) -> Boolean
    ) : ViewHolder(binding.root) {
        fun bind(weapon: Weapon) {
            binding.apply {
                weaponName.text = weapon.name
                weaponDamage.text = weapon.damage.toString()
                weaponCost.text = weapon.cost.toString()
                weaponImage.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        weapon.imageId,
                        null
                    )
                )
                setupButtonState(weapon)
            }
        }

        private fun setupButtonState(weapon: Weapon) {
            with(binding) {
                if (weapon.isBought) {
                    if (isWeaponEquipped(weapon)) {
                        setupButtonDisabledView(resources.getString(R.string.equipped))
                    } else {
                        setupButtonEnabledView(resources.getString(R.string.equip))
                        buyButton.setOnClickListener {
                            equipWeapon(weapon)
                        }
                    }
                } else {
                    if (isBuyEnabled(weapon)) {
                        setupButtonEnabledView(resources.getString(R.string.buy))
                        buyButton.setOnClickListener {
                            onBuyClicked(weapon)
                        }
                    } else {
                        setupButtonDisabledView(resources.getString(R.string.buy))
                    }
                }
            }
        }

        private fun setupButtonDisabledView(text: String) {
            with(binding) {
                buyButton.isEnabled = false
                buyButton.text = text
                buyButton.setBackgroundColor(
                    resources.getColor(
                        R.color.disabled_button_color,
                        null
                    )
                )
                buyButton.setTextColor(
                    resources.getColor(
                        R.color.disabled_button_text_color,
                        null
                    )
                )
            }
        }

        private fun setupButtonEnabledView(text: String) {
            with(binding) {
                buyButton.isEnabled = true
                buyButton.text = text
                buyButton.setBackgroundColor(
                    resources.getColor(
                        R.color.golden_yellow,
                        null
                    )
                )
                buyButton.setTextColor(
                    resources.getColor(
                        R.color.dark_yellow,
                        null
                    )
                )
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
                        && oldItem.isBought == newItem.isBought
            }
        }
    }
}