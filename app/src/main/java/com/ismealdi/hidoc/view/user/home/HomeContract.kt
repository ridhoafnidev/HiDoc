package com.ismealdi.hidoc.view.user.home

import com.ismealdi.hidoc.base.AmPresenter
import com.ismealdi.hidoc.base.AmView
import com.ismealdi.hidoc.struct.Article
import com.ismealdi.hidoc.struct.Favorite
import com.ismealdi.hidoc.struct.User
import com.ismealdi.hidoc.utils.components.slider.AmImage

interface HomeContract  {

	interface View : AmView<Presenter> {
        fun displayUser(user: User)
        fun displaySlider(images: List<AmImage>)
        fun displaySpecialists(lists: List<String>)
        fun displayArticles(articles: List<Article>)
        fun displayDoctors(doctors: List<User>)
        fun displayFavorites(favorite: List<Favorite>)
        fun refreshDoctor(position: Int)
    }

    interface Presenter : AmPresenter {
        fun user()
        fun favorites()
        fun sliders()
        fun specialists()
        fun articles()
        fun doctors()
        fun add(position: Int, doctor: User)
    }

}