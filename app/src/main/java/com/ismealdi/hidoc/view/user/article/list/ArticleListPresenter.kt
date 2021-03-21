package com.ismealdi.hidoc.view.user.article.list

import android.content.Context
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.ismealdi.hidoc.App
import com.ismealdi.hidoc.struct.Article
import com.ismealdi.hidoc.utils.extensions.articles

class ArticleListPresenter(private val view: ArticleListContract.View, private val context: Context) : ArticleListContract.Presenter {

    init {
        view.presenter = this
    }

    private val articles = mutableListOf<Article>()

    private val onFailureListener = OnFailureListener {
        view.onError(it.message.toString())
    }

    override fun articles() {
        App.amDatabase.articles().orderBy(Article.CREATOR.Fields.createdOn, Query.Direction.DESCENDING).get().addOnSuccessListener {
            it.documents.forEach { document: DocumentSnapshot? ->
                document?.let {
                    document.toObject(Article::class.java)?.let { item ->
                        articles.add(item)
                    }
                }
            }

            view.displayArticles(articles)

        }.addOnFailureListener(onFailureListener)
    }


}