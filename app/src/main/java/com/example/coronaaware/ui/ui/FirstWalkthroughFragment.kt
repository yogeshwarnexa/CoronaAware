package com.example.coronaaware.ui.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.coronaaware.R
import kotlinx.android.synthetic.main.fragment_first_walkthrough.*

class FirstWalkthroughFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return inflater.inflate(R.layout.fragment_first_walkthrough, container, false)
        var view = inflater.inflate(R.layout.fragment_first_walkthrough, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val imageResId = arguments?.getInt(EXTRA_IMAGE)
        if (imageResId != null) {
            imageView.setImageResource(imageResId)
        }
    }

    companion object {

        private const val EXTRA_TEXT = "text"
        private const val EXTRA_IMAGE = "image"

        fun newInstance(text: String, @DrawableRes imageResId: Int? = null) = FirstWalkthroughFragment().apply {
            arguments = bundleOf(

                    EXTRA_TEXT to text,
                    EXTRA_IMAGE to imageResId
            )

        }
    }
}

