package com.hayao0819.liconify.ui.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hayao0819.liconify.Iconify.Companion.appContext
import com.hayao0819.liconify.Iconify.Companion.appContextLocale
import com.hayao0819.liconify.R
import com.hayao0819.liconify.data.common.Preferences.SELECTED_ICON_SHAPE
import com.hayao0819.liconify.data.config.RPrefs
import com.hayao0819.liconify.databinding.ViewIconShapeBinding
import com.hayao0819.liconify.data.models.IconShapeModel

class IconShapeAdapter (
    var context: Context,
    private var itemList: ArrayList<IconShapeModel>,
    private var iconShapeClick: OnShapeClick
) : RecyclerView.Adapter<IconShapeAdapter.ViewHolder>() {

    private var selected = RPrefs.getInt(SELECTED_ICON_SHAPE, -1)
    @ColorInt
    val colorBackground = appContextLocale.resources.getColor(
        R.color.colorBackground,
        appContext.theme
    )
    @ColorInt
    val colorSuccess = appContextLocale.resources.getColor(
        R.color.colorSuccess,
        appContext.theme
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ViewIconShapeBinding =
            ViewIconShapeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        if (selected != -1) {
            itemList[selected].selected = true
        } else {
            itemList[0].selected = true
        }
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = itemList[holder.bindingAdapterPosition]
        holder.binding.shapeName.text = appContextLocale.resources.getString(model.title)
        holder.binding.maskShapeBg.background = ContextCompat.getDrawable(appContext, model.style)
        holder.binding.maskShapeFg.background = ContextCompat.getDrawable(appContext, model.style)
        holder.binding.maskShapeFg.setBackgroundTintList(ColorStateList.valueOf(colorBackground))
        if (model.selected) {
            holder.binding.shapeName.setTextColor(colorSuccess)
            holder.binding.maskShapeBg.setBackgroundTintList(ColorStateList.valueOf(colorSuccess))
        } else {
            holder.binding.shapeName.setTextColor(
                appContextLocale.resources.getColor(
                    R.color.textColorSecondary,
                    appContext.theme
                )
            )
        }
        holder.binding.listItemShape.setOnClickListener {
            iconShapeClick.onShapeClick(holder.bindingAdapterPosition, model)
        }
    }

    fun notifyChange() {
        refresh(false)
        selected = RPrefs.getInt(SELECTED_ICON_SHAPE, -1)
        refresh(true)
    }

    private fun refresh(select: Boolean) {
        if (selected != -1) {
            itemList[selected].selected = select
            notifyItemChanged(selected)
        } else {
            itemList[0].selected = select
            notifyItemChanged(0)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(val binding: ViewIconShapeBinding) : RecyclerView.ViewHolder(binding.getRoot()) {
    }

    /**
     * Interface for the click on the item
     */
    interface OnShapeClick {
        fun onShapeClick(position: Int, item: IconShapeModel)
    }

}