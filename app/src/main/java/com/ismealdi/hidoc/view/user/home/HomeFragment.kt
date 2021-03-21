package com.ismealdi.hidoc.view.user.home

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import com.ismealdi.hidoc.R
import com.ismealdi.hidoc.base.AmFragment
import com.ismealdi.hidoc.struct.Article
import com.ismealdi.hidoc.struct.Favorite
import com.ismealdi.hidoc.struct.User
import com.ismealdi.hidoc.utils.commons.Constants.INTENT.ACTIVITY.ARTICLE
import com.ismealdi.hidoc.utils.commons.Constants.INTENT.ACTIVITY.USER
import com.ismealdi.hidoc.utils.commons.Utils
import com.ismealdi.hidoc.utils.components.slider.AmImage
import com.ismealdi.hidoc.utils.components.slider.AmSliderAdapter
import com.ismealdi.hidoc.utils.components.slider.AmSliderTransformation
import com.ismealdi.hidoc.view.user.article.detail.ArticleDetailActivity
import com.ismealdi.hidoc.view.user.article.list.ArticleListActivity
import com.ismealdi.hidoc.view.user.conversation.ConversationActivity
import com.ismealdi.hidoc.view.user.detail.UserDetailActivity
import com.ismealdi.hidoc.view.user.doctor.list.DoctorListActivity
import com.ismealdi.hidoc.view.user.home.adapter.SpecialistAdapter
import com.ismealdi.hidoc.view.user.home.adapter.article.ArticleAdapter
import com.ismealdi.hidoc.view.user.home.adapter.article.ArticleAdapterInterface
import com.ismealdi.hidoc.view.user.home.adapter.doctor.DoctorAdapter
import com.ismealdi.hidoc.view.user.home.adapter.doctor.DoctorAdapterInterface
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

class HomeFragment : AmFragment(R.layout.fragment_home, R.string.title_home), HomeContract.View, DoctorAdapterInterface,
	ArticleAdapterInterface {
	
	override var presenter: HomeContract.Presenter? = null
	
	private var sliderCurrentPage = 0
	private var sliderNumberPage = 0
	private val sliderDelay = 3000L
	
	private var specialistsAdapter: SpecialistAdapter? = null
	private var doctorsAdapter: DoctorAdapter? = null
	private var articlesAdapter: ArticleAdapter? = null

	private var favorites: HashSet<String> = hashSetOf()

	override fun initView() {
		amActivity?.let {
			presenter = HomePresenter(this, it)

			initAdapter(it)
			initLists(it)

			presenter?.user()
			presenter?.sliders()
			presenter?.specialists()
			presenter?.articles()

			labelWelcome?.text = Utils().dayTimeGreeting(it)

		}
	}

    private fun initAdapter(context: Context) {
        specialistsAdapter = SpecialistAdapter(mutableListOf(), context, isMore= true)

        doctorsAdapter = DoctorAdapter(mutableListOf(), favorites, context, this)
        articlesAdapter = ArticleAdapter(mutableListOf(), context, this)
    }

	override fun initListener() {
		super.initListener()

		buttonSeeAllArticles.setOnClickListener {
			activity?.let {
				val intent = Intent(it, ArticleListActivity::class.java)

				startActivity(intent)
			}
		}

		buttonSeeAllDoctor.setOnClickListener {
			activity?.let {
				val intent = Intent(it, DoctorListActivity::class.java)

				startActivity(intent)
			}
		}
	}

    private fun initLists(context: Context) {
		gridSpecialists.layoutManager = GridLayoutManager(context, 5)
		gridSpecialists.adapter = specialistsAdapter
		
		gridDoctors.layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
		gridDoctors.adapter = doctorsAdapter
		
		gridArticles.layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
		gridArticles.adapter = articlesAdapter
	}

	override fun displayUser(user: User) {
		with(user) {
			labelName?.text = if(fullName.contains(" ")) fullName.split(" ")[0] else fullName

			activity?.let { context ->
				imageProfilePicture?.let { Utils().imageCircle(it, photoUrl, context) }
			}
		}
	}

	override fun displayDoctors(doctors: List<User>) {
		doctorsAdapter?.update(doctors)
	}

	override fun displaySpecialists(lists: List<String>) {
		specialistsAdapter?.update(lists)

		presenter?.favorites()
	}

	override fun displayArticles(articles: List<Article>) {
		articlesAdapter?.update(articles)
	}

	override fun displaySlider(images: List<AmImage>) {
		amActivity?.applicationContext?.let { initSlider(it, images) }
	}

	private fun initSlider(context: Context, slideList: List<AmImage>) {
		sliderNumberPage = slideList.size
		
		slider.adapter = AmSliderAdapter(context, slideList, this)
		sliderIndicator.setupWithViewPager(slider)
		slider.setPageTransformer(true, AmSliderTransformation())
		
		val handler = Handler()
		val update = Runnable {
			if (sliderCurrentPage == sliderNumberPage) {
				sliderCurrentPage = 0
			}
			
			slider?.setCurrentItem(sliderCurrentPage++, true)
		}
		
		val swipeTimer = Timer()
		swipeTimer.schedule(object : TimerTask() {
			override fun run() {
				handler.post(update)
			}
		}, sliderDelay, sliderDelay)
	}

	override fun onPlusClick(position: Int, user: User) {
		 presenter?.add(position, user)
	}

	override fun onItemClick(position: Int, article: Article) {
		activity?.let {
			val intent = Intent(it, ArticleDetailActivity::class.java)
			intent.putExtra(ARTICLE, article)
			startActivity(intent)
		}
	}

	override fun onChatClick(position: Int, user: User) {
		activity?.let {
			val intent = Intent(it, ConversationActivity::class.java)
			intent.putExtra(USER, user)
			startActivity(intent)
		}
	}

	override fun refreshDoctor(position: Int) {
		doctorsAdapter?.refresh(position, favorites)
	}

	override fun displayFavorites(favorite: List<Favorite>) {
		favorites.clear()
		favorite.forEach {
			favorites.add(it.friend)
		}
	}

	override fun onDoctorClick(user: User) {
		activity?.let {
			val intent = Intent(it, UserDetailActivity::class.java)
			intent.putExtra(USER, user)
			startActivity(intent)
		}
	}
	
}