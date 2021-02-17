package com.pmurck.contacttracer.pings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.pmurck.contacttracer.database.AppDatabase
import com.pmurck.contacttracer.databinding.FragmentPingListBinding

class PingListFragment : Fragment() {

    companion object {
        fun newInstance() = PingListFragment()
    }

    private val viewModelFactory
        get() = PingListViewModelFactory(
            AppDatabase.getInstance(requireActivity().application).pingDAO)

    private val viewModel: PingListViewModel by viewModels{viewModelFactory}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Get a reference to the binding object and inflate the fragment views.
        val binding = FragmentPingListBinding.inflate(inflater, container, false)

        val adapter = PingAdapter()
        binding.pings.adapter = adapter

        binding.pings.addItemDecoration(DividerItemDecoration(binding.pings.context, LinearLayout.VERTICAL))

        binding.pingListViewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.pings.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }

}