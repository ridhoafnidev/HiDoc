package com.ismealdi.hidoc.view.user.article.list

import com.ismealdi.hidoc.base.AmPresenter
import com.ismealdi.hidoc.base.AmView
import com.ismealdi.hidoc.struct.Article

interface ArticleListContract {

    interface View : AmView<Presenter> {

        fun displayArticles(articles: List<Article>)

    }

    interface Presenter : AmPresenter {

        fun articles()

    }

}