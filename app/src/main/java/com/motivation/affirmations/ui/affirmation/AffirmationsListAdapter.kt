package com.motivation.affirmations.ui.affirmation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.motivation.affirmations.domain.model.Affirmation
import com.motivation.affirmations.domain.model.enums.AffirmationClickEvent
import com.motivation.app.R
import com.motivation.app.databinding.AffirmationItemBinding

class AffirmationsListAdapter :
    ListAdapter<Affirmation, AffirmationsListAdapter.AffirmationViewHolder>(DiffCallback()) {

    private var onClick: ((AffirmationClickEvent, Any) -> Unit)? = null

    fun onItemClick(onClick: (AffirmationClickEvent, Any) -> Unit) {
        this.onClick = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AffirmationViewHolder {
        return AffirmationViewHolder(
            AffirmationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    fun isSelectionIsAvailable(): Boolean {
        return currentList.isNotEmpty() && currentList.any { it.isSelected }
    }

    fun isAllSelected(): Boolean {
        return currentList.isNotEmpty() && currentList.all { it.isSelected }
    }

    fun selectedIds(): List<Int> {
        return currentList.filter { it.isSelected }.map { it.id }
    }

    fun selectAll() {
        currentList.forEach { it.isSelected = true }
        notifyDataSetChanged()
    }

    fun unSelectAll() {
        currentList.forEach { it.isSelected = false }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: AffirmationViewHolder, position: Int) {
        holder.bind(getItem(position)) { event, value ->
            if (event == AffirmationClickEvent.ON_SELECT && value is Boolean) {
                currentList[position].isSelected = value
            }
            onClick?.let {
                it(event, value)
            }
        }
    }

    class AffirmationViewHolder(private val binding: AffirmationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(affirmation: Affirmation, onClick: ((AffirmationClickEvent, Any) -> Unit)?) {
            binding.apply {
                affirmationDescriptionTxt.text = affirmation.text
                selectChk.isChecked = affirmation.isSelected
                micImg.setImageResource(
                    if (affirmation.isRecorded) R.drawable.ic_dark_play else R.drawable.ic_dark_mic
                )
                onClick?.let {
                    micImg.setOnClickListener {
                        it(
                            if (affirmation.isRecorded) {
                                AffirmationClickEvent.ON_PLAY
                            } else {
                                AffirmationClickEvent.ON_RECORD
                            },
                            affirmation.id
                        )
                    }

                    settingsImg.setOnClickListener {
                        it(AffirmationClickEvent.ON_MORE, affirmation.id)
                    }

                    selectChk.setOnClickListener {
                        it(AffirmationClickEvent.ON_SELECT, selectChk.isChecked)
                    }
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Affirmation>() {
        override fun areItemsTheSame(
            oldItem: Affirmation,
            newItem: Affirmation
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Affirmation,
            newItem: Affirmation
        ): Boolean {
            return oldItem == newItem
        }
    }
}
