package com.ismealdi.hidoc.view.user.doctor.list

import com.ismealdi.hidoc.base.AmPresenter
import com.ismealdi.hidoc.base.AmView
import com.ismealdi.hidoc.struct.Favorite
import com.ismealdi.hidoc.struct.User

interface DoctorListContract {

    interface View : AmView<Presenter> {

        fun displayDoctors(doctors: List<User>)
        fun displayFavorites(favorite: List<Favorite>)
        fun refreshDoctor(position: Int)

    }

    interface Presenter : AmPresenter {

        fun favorites()
        fun doctors()
        fun add(position: Int, doctor: User)

    }

}