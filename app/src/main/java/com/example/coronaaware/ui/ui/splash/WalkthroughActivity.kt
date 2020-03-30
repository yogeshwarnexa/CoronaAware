package com.example.coronaaware.ui.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.coronaaware.R
import com.example.coronaaware.utlies.event.NextPageEvent
import com.example.coronaaware.utlies.event.RetryWalkthroughEvent
import kotlinx.android.synthetic.main.activity_walkthrough.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

abstract class WalkthroughActivity : AppCompatActivity() {

    private lateinit var indicators: List<ImageView>

    protected abstract val pages: List<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walkthrough)

        val inflater = LayoutInflater.from(this)

        indicators = pages.map {
            inflater.inflate(R.layout.page_dot, pageIndicatorWrapper, false)
                    .also { pageDot -> pageIndicatorWrapper.addView(pageDot) } as ImageView
        }
        indicators.select(0)

        pager.adapter = WalkthroughPagerAdapter(supportFragmentManager)
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // Do nothing
            }

            override fun onPageSelected(position: Int) {
                // スキップボタンの表示/非表示
                val isLast = (position == indicators.lastIndex)
                skipButton.visibility = if (isLast) View.INVISIBLE else View.VISIBLE

                // Indicator
                indicators.select(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                // Do nothing
            }
        })

        skipButton.setOnClickListener {
            pager.setCurrentItem(indicators.lastIndex, true)
        }
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        EventBus.getDefault().unregister(this)
        super.onPause()
    }

    protected abstract fun onComplete()

    @Subscribe
    fun onNext(event: NextPageEvent) {
        val adapter = pager.adapter ?: return
        val nextItem = pager.currentItem + 1
        if (nextItem < adapter.count) {
            pager.setCurrentItem(nextItem, true)
        } else {
            onComplete()
        }
    }

    @Subscribe
    fun onRetry(event: RetryWalkthroughEvent) {
        pager.setCurrentItem(0, true)
    }

    private fun List<ImageView>.select(position: Int) {
        forEachIndexed { index, indicator ->
            val dotImage = if (index == position) R.drawable.page_dot_selected else R.drawable.page_dot
            indicator.setImageResource(dotImage)
        }
    }

    private inner class WalkthroughPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return pages[position]
        }

        override fun getCount(): Int {
            return indicators.size
        }
    }
}
