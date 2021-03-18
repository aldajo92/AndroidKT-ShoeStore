package com.udacity.shoestore

import android.widget.Toast
import androidx.fragment.app.Fragment

abstract class BaseNavigationFragment() : Fragment() {

    protected fun showToast(message: String) {
        Toast.makeText(this.activity, message, Toast.LENGTH_SHORT).show()
    }

}