package com.udacity.shoestore.dashboard

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.udacity.shoestore.MainActivity
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.FragmentDashboardBinding
import com.udacity.shoestore.databinding.ShoeItemBinding
import com.udacity.shoestore.viewmodels.DashboardViewModel

class DashboardFragment : Fragment() {

    private val viewModel : DashboardViewModel by activityViewModels()

    private lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).actionBarVisible(true)
        setHasOptionsMenu(true)

        binding = FragmentDashboardBinding.inflate(layoutInflater)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.shoeList.observe(viewLifecycleOwner, {
            it.forEach{ shoe ->
                DataBindingUtil.inflate<ShoeItemBinding>(
                    layoutInflater,
                    R.layout.shoe_item,
                    binding.itemList,
                    true
                ).apply {
                    this.shoeModel = shoe
                }
            }
        })

        viewModel.openAddItem.observe(this.viewLifecycleOwner, {
            if (it) findNavController().navigate(R.id.action_dashboardFragment_to_detailFragment)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)

        // Remove up arrow to prevent user from going back to login screen
        val actionBar: ActionBar? = (activity as MainActivity).supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false) // disable the button
            actionBar.setDisplayHomeAsUpEnabled(false) // remove the left caret
            actionBar.setDisplayShowHomeEnabled(false) // remove the icon
        }
    }

}