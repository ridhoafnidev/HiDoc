package com.ismealdi.hidoc.view.user.home.adapter.article

import android.annotation.SuppressLint
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.ismealdi.hidoc.R
import com.ismealdi.hidoc.struct.Article
import com.ismealdi.hidoc.utils.commons.Utils
import com.ismealdi.hidoc.utils.components.AmTextView
import com.ismealdi.hidoc.utils.extensions.toFormat
import kotlinx.android.synthetic.main.item_article.view.*

class ArticleAdapter(private var data: MutableList<Article>, private val context: Context, private val listener: ArticleAdapterInterface, private val isGrid: Boolean = false) : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

	class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		val image: ImageView = itemView.imageProfilePicture
		val title: AmTextView = itemView.labelTitle
		val writer: AmTextView = itemView.labelWriter
		val date: AmTextView = itemView.labelDate
		val container: ConstraintLayout = itemView.layoutContainer
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = if(isGrid) {
			LayoutInflater.from(parent.context).inflate(R.layout.item_article_grid, parent, false)
		}else {
			LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
		}
		return ViewHolder(view)
	}

	@SuppressLint("ClickableViewAccessibility")
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val article = data[position]
		Utils().imageTopRound(holder.image, article.image, context)

		holder.title.text = article.title
		holder.writer.text = article.writer
		holder.date.text = article.createdOn.toDate().toFormat()

		holder.container.setOnClickListener {
			listener.onItemClick(position, article)
		}

	}

	override fun getItemCount(): Int {
		return data.size
	}

	fun update(list: List<Article>) {
		data.clear()
		data.addAll(list)

		this.notifyDataSetChanged()
	}
}