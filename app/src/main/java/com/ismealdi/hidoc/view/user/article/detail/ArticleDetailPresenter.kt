package com.ismealdi.hidoc.view.user.article.detail

import android.content.Context
import com.google.android.gms.tasks.OnFailureListener

class ArticleDetailPresenter(private val view: ArticleDetailContract.View, private val context: Context) : ArticleDetailContract.Presenter {

    init {
        view.presenter = this
    }

    private val onFailureListener = OnFailureListener {
        view.onError(it.message.toString())
    }


}