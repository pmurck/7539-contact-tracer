package com.pmurck.contacttracer.contacts

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.VERTICAL
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.pmurck.contacttracer.Constants
import com.pmurck.contacttracer.database.AppDatabase
import com.pmurck.contacttracer.databinding.FragmentContactListBinding

class ContactListFragment : Fragment() {

    companion object {
        fun newInstance() = ContactListFragment()
    }

    private val viewModelFactory
        get() = ContactListViewModelFactory(AppDatabase.getInstance(requireActivity().application).contactDAO,
                                            requireContext().getSharedPreferences(Constants.SHARED_PREFS_CONFIG_NAME, Context.MODE_PRIVATE))

    private val viewModel: ContactListViewModel by viewModels{viewModelFactory}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding = FragmentContactListBinding.inflate(inflater, container, false)

        val adapter = ContactAdapter()
        binding.contacts.adapter = adapter

        binding.contacts.addItemDecoration(DividerItemDecoration(binding.contacts.context, VERTICAL))

        binding.contactListViewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.contacts.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }

}