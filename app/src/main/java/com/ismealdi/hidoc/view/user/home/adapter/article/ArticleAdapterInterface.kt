package com.ismealdi.hidoc.view.user.home.adapter.article

import com.ismealdi.hidoc.struct.Article
import com.ismealdi.hidoc.struct.User


interface ArticleAdapterInterface {

    fun onItemClick(position: Int, article: Article)

}