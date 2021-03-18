package com.udacity.shoestore.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.udacity.shoestore.MainActivity
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.FragmentLoginBinding
import com.udacity.shoestore.data.events.LoginEvents
import com.udacity.shoestore.viewmodels.LoginViewModel

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by activityViewModels()

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.eventsLiveData.observe(this.viewLifecycleOwner, { loginEvents ->
            loginEvents?.getDataOnce()?.let {
                when (it) {
                    is LoginEvents.LoginUser -> findNavController().navigate(R.id.action_loginFragment_to_welcomeFragment)
                    is LoginEvents.OpenRegisterUser -> findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
                    is LoginEvents.ErrorLogin -> showToast(it.message)
                    else -> Unit
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).actionBarVisible(false)
    }

    private fun showToast(message: String) {
        Toast.makeText(this.activity, message, Toast.LENGTH_SHORT).show()
    }

}
