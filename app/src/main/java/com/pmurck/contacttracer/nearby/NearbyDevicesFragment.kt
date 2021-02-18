package com.pmurck.contacttracer.nearby

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
import com.pmurck.contacttracer.databinding.FragmentNearbyDevicesBinding


class NearbyDevicesFragment : Fragment() {

    companion object {
        fun newInstance() = NearbyDevicesFragment()
    }

    private val viewModelFactory
        get() = NearbyDevicesViewModelFactory(
                AppDatabase.getInstance(requireActivity().application).pingDAO)

    private val viewModel: NearbyDevicesViewModel by viewModels{viewModelFactory}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentNearbyDevicesBinding.inflate(inflater, container, false)

        val adapter = PingWithMinDistanceAdapter()
        binding.pings.adapter = adapter

        binding.pings.addItemDecoration(DividerItemDecoration(binding.pings.context, LinearLayout.VERTICAL))

        binding.nearbyDevicesViewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.pings.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }

}