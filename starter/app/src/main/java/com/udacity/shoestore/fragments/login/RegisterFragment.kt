package com.udacity.shoestore.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.udacity.shoestore.databinding.FragmentRegisterBinding
import com.udacity.shoestore.data.events.LoginEvents
import com.udacity.shoestore.viewmodels.LoginViewModel

class RegisterFragment : Fragment() {

    private val viewModel : LoginViewModel by activityViewModels()

    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.eventsLiveData.observe(viewLifecycleOwner, { dataEvent ->
            dataEvent?.getDataOnce()?.let {
                when(it){
                    is LoginEvents.RegisteredUser -> findNavController().navigateUp()
                    else -> Unit
                }
            }
        })
    }
}
