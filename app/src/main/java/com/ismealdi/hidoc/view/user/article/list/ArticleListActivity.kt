package com.ismealdi.hidoc.view.user.article.list

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.view.Gravity
import com.ismealdi.hidoc.R
import com.ismealdi.hidoc.base.AmActivity
import com.ismealdi.hidoc.struct.Article
import com.ismealdi.hidoc.utils.commons.Constants
import com.ismealdi.hidoc.view.user.article.detail.ArticleDetailActivity
import com.ismealdi.hidoc.view.user.home.adapter.article.ArticleAdapter
import com.ismealdi.hidoc.view.user.home.adapter.article.ArticleAdapterInterface
import kotlinx.android.synthetic.main.activity_article_list.*

class ArticleListActivity: AmActivity(R.layout.activity_article_list), ArticleListContract.View,
    ArticleAdapterInterface {

    override var presenter: ArticleListContract.Presenter? = ArticleListPresenter(this, this)

    private var articlesAdapter: ArticleAdapter? = null

    override fun initView() {
        initToolbar(back = true)
        setPageName(getString(R.string.title_articles), line = false)
        setTitleGravity(Gravity.CENTER)

        articlesAdapter = ArticleAdapter(mutableListOf(), this, this, isGrid = true)

        lists.layoutManager = GridLayoutManager(this, 2)
        lists.adapter = articlesAdapter

        presenter?.articles()
    }

    override fun displayArticles(articles: List<Article>) {
        articlesAdapter?.update(articles)
    }

    override fun onItemClick(position: Int, article: Article) {
        val intent = Intent(this, ArticleDetailActivity::class.java)
        intent.putExtra(Constants.INTENT.ACTIVITY.ARTICLE, article)
        startActivity(intent)
    }


}