package com.example.coronaaware.ui.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.coronaaware.R
import com.example.coronaaware.utlies.event.NextPageEvent
import kotlinx.android.synthetic.main.fragment_start.*
import org.greenrobot.eventbus.EventBus


class StartFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        startButton.setOnClickListener {
            EventBus.getDefault().post(NextPageEvent())
        }
    }

    companion object {

        fun newInstance(): Fragment {
            return StartFragment()
        }
    }
}
