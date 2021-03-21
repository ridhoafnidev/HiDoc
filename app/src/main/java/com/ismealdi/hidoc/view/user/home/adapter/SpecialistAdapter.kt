package com.ismealdi.hidoc.view.user.home.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ismealdi.hidoc.R
import com.ismealdi.hidoc.utils.components.AmButton
import com.ismealdi.hidoc.utils.components.AmTextView
import kotlinx.android.synthetic.main.item_specialist.view.*

class SpecialistAdapter(private var data: MutableList<String>, private val context: Context, private val isMore: Boolean = false) : RecyclerView.Adapter<SpecialistAdapter.ViewHolder>() {
	
	class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		val button: AmButton = itemView.buttonSpecialist
		val name: AmTextView = itemView.labelName
	}
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.item_specialist, parent, false)
		return ViewHolder(view)
	}
	
	@SuppressLint("ClickableViewAccessibility")
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		if(isMore && (position + 1) == data.size) {
			holder.button.text = context.getString(R.string.text_more).toUpperCase()
			holder.button.background = ContextCompat.getDrawable(context, R.drawable.button_primary)
		}else{
			holder.name.text = data[position]
			holder.button.background = ContextCompat.getDrawable(context, R.drawable.button_light)
			holder.button.text = ""
		}
		
	}
	
	override fun getItemCount(): Int {
		return data.size
	}

	fun update(list: List<String>) {
		data.clear()
		data.addAll(list)

		this.notifyDataSetChanged()
	}
}
