package com.ismealdi.hidoc.view.user.doctor.list.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.ismealdi.hidoc.R
import com.ismealdi.hidoc.struct.User
import com.ismealdi.hidoc.utils.commons.Utils
import com.ismealdi.hidoc.utils.components.AmButton
import com.ismealdi.hidoc.utils.components.AmTextView
import com.ismealdi.hidoc.view.user.home.adapter.doctor.DoctorAdapterInterface
import kotlinx.android.synthetic.main.item_doctor.view.buttonChat
import kotlinx.android.synthetic.main.item_doctor.view.buttonPlus
import kotlinx.android.synthetic.main.item_doctor.view.labelName
import kotlinx.android.synthetic.main.item_doctor.view.labelSpecialist
import kotlinx.android.synthetic.main.item_doctor.view.layoutContainer
import kotlinx.android.synthetic.main.item_doctor_grid.view.*

class DoctorGridAdapter(private var data: MutableList<User>, private var friends: HashSet<String>, private val context: Context, private val listener: DoctorAdapterInterface) : RecyclerView.Adapter<DoctorGridAdapter.ViewHolder>() {

	class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		val imageDoctor: ImageView = itemView.imageDoctorGrid
		val name: AmTextView = itemView.labelName
		val specialist: AmTextView = itemView.labelSpecialist
		val plus: AmButton = itemView.buttonPlus
		val chat: AmButton = itemView.buttonChat
		val container: ConstraintLayout = itemView.layoutContainer
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.item_doctor_grid, parent, false)
		return ViewHolder(view)
	}

	@SuppressLint("ClickableViewAccessibility")
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val user = data[position]

		Utils().imageTopRound(holder.imageDoctor, user.photoUrl, context)

		holder.name.text = String.format(context.getString(R.string.text_dr), user.fullName)
		holder.specialist.text = user.specialist

        if(friends.contains(user.uid)) {
            holder.plus.visibility = View.GONE
        }else{
            holder.plus.setOnClickListener {
                it.visibility = View.GONE
                listener.onPlusClick(position, user)
            }
        }

		holder.chat.setOnClickListener {
			listener.onChatClick(position, user)
		}

		holder.container.setOnClickListener {
			listener.onDoctorClick(user)
		}

	}

	override fun getItemCount(): Int {
		return data.size
	}

	fun update(list: List<User>) {
        this.data.clear()
        this.data.addAll(list)

		this.notifyDataSetChanged()
	}

    fun refresh(position: Int, friends: HashSet<String>) {
        this.friends.clear()
        this.friends.addAll(friends)

        this.notifyItemChanged(position)
    }
}