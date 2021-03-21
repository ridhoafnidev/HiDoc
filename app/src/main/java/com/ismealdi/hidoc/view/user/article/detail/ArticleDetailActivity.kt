package com.ismealdi.hidoc.view.user.article.detail

import android.view.Gravity
import com.ismealdi.hidoc.R
import com.ismealdi.hidoc.base.AmActivity
import com.ismealdi.hidoc.struct.Article
import com.ismealdi.hidoc.utils.commons.Constants
import com.ismealdi.hidoc.utils.commons.Utils
import com.ismealdi.hidoc.utils.extensions.toFormat
import kotlinx.android.synthetic.main.activity_article_detail.*

class ArticleDetailActivity: AmActivity(R.layout.activity_article_detail), ArticleDetailContract.View {

    override var presenter: ArticleDetailContract.Presenter? = ArticleDetailPresenter(this, this)

    private var article: Article? = null

    override fun initView() {
        initToolbar(back = true)

        article = intent.getParcelableExtra(Constants.INTENT.ACTIVITY.ARTICLE)

        article?.let {
            with(it) {
                setPageName("", false)
                setTitleGravity(Gravity.CENTER)
                labelTitle.text = title
                labelWriter.text = writer
                labelContent.text = contents
                labelDate.text = createdOn.toDate().toFormat()

                Utils().image(imageArticle, image, this@ArticleDetailActivity)
            }
        }
    }

}