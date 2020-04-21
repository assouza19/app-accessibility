package com.example.goout.Activity

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.com.userede.ui.adapter.MainPagerAdapter
import com.example.goout.R
import com.example.goout.ViewModel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var pagerAdapter: MainPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = MainViewModel()
        pagerAdapter = MainPagerAdapter(supportFragmentManager)

        view_pager.offscreenPageLimit = 4
        view_pager.adapter = pagerAdapter

        mainViewModel.init()
        bindViews()
        setupViewPager()
    }


    private fun bindViews() {
        hideLoading()

        bottom_navigation_view.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun hideLoading() =
        Handler().postDelayed({
            shimmerHome.visibility = mainViewModel.shimmerPresent.value!!
        }, 8000)

    private fun setupViewPager() {

        val firstFragment = HomeFragment.newInstance()
        val secondFragment = SearchFragment.newInstance()
        val thirstFragment =  MapsFragment.newInstance()
        pagerAdapter.addFragment(firstFragment, "Home")
        pagerAdapter.addFragment(secondFragment, "Busca")
        pagerAdapter.addFragment(thirstFragment, "Mapa")

        view_pager!!.adapter = pagerAdapter

    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.menu_item_resumo -> {
                view_pager.currentItem = 0
            }
            R.id.menu_search -> {
                view_pager.currentItem = 1
            }
            R.id.menu_add_map -> {
                view_pager.currentItem = 2
            }
        }
        false
    }
}
