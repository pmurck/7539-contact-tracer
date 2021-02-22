package com.pmurck.contacttracer.stays

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.pmurck.contacttracer.Constants
import com.pmurck.contacttracer.R
import com.pmurck.contacttracer.contacts.ContactAdapter
import com.pmurck.contacttracer.contacts.ContactListViewModel
import com.pmurck.contacttracer.contacts.ContactListViewModelFactory
import com.pmurck.contacttracer.database.AppDatabase
import com.pmurck.contacttracer.databinding.FragmentContactListBinding
import com.pmurck.contacttracer.databinding.FragmentStayListBinding

class StayListFragment : Fragment() {

    companion object {
        fun newInstance() = StayListFragment()
    }

    private val viewModelFactory
        get() = StayListViewModelFactory(AppDatabase.getInstance(requireActivity().application).stayDAO)

    private val viewModel: StayListViewModel by viewModels{viewModelFactory}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentStayListBinding.inflate(inflater, container, false)

        val adapter = StayAdapter()
        binding.stays.adapter = adapter

        binding.stays.addItemDecoration(DividerItemDecoration(binding.stays.context, LinearLayout.VERTICAL))

        binding.stayListViewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.stays.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }

}