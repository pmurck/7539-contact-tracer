package com.pmurck.contacttracer

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.mohsenoid.closetome.CloseToMeState
import com.pmurck.contacttracer.database.AppDatabase
import com.pmurck.contacttracer.databinding.FragmentHomeBinding
import kotlin.random.Random

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()

        const val PERMISSIONS_REQUEST = 1010

        val permissions: Array<String> = arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private val viewModelFactory
        get() = HomeViewModelFactory(AppDatabase.getInstance(requireActivity().application).stayDAO)

    private val viewModel: HomeViewModel by viewModels{viewModelFactory}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)


        val anyPermissionsNotGranted =
            permissions.any { permission -> ContextCompat.checkSelfPermission(this.requireActivity(), permission) != PackageManager.PERMISSION_GRANTED }

        if (anyPermissionsNotGranted) {
            requestPermissions(permissions, PERMISSIONS_REQUEST)

        }

        // TODO: Permitir cambiar /primer carga de DNI/device id
        val sharedPrefs = requireActivity().applicationContext.getSharedPreferences(Constants.SHARED_PREFS_CONFIG_NAME, Context.MODE_PRIVATE)
        if (!sharedPrefs.contains(Constants.DEVICE_ID_PREF_KEY)) {
            sharedPrefs.edit {
                putInt(Constants.DEVICE_ID_PREF_KEY, Random.nextInt(1_000_000,100_000_000))
            }
        }

        binding.homeViewModel = viewModel
        binding.lifecycleOwner = this
        //binding.user.text = "USER TODO"//"User: $userUuid"
        //binding.log.movementMethod = ScrollingMovementMethod()
        binding.start.setOnClickListener { onStartClick() }
        binding.stop.setOnClickListener { onStopClick() }
        binding.goToScanner.setOnClickListener { findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToScannerFragment()) }
        binding.goToContacts.setOnClickListener { findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToContactListFragment()) }
        binding.goToPings.setOnClickListener { findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPingListFragment()) }

        BeaconService.state.observe(viewLifecycleOwner, Observer { state ->
            //log("Beacon state: $state")

            when (state) {
                CloseToMeState.STARTED -> {
                    binding.start.isVisible = false
                    binding.stop.isVisible = true
                }
                else -> {
                    binding.start.isVisible = true
                    binding.stop.isVisible = false
                }
            }
        })

        return binding.root
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST -> {
                if (grantResults.isEmpty()) {
                    onPermissionNotGranted()
                }

                val isNotGranted = grantResults.any {
                    it != PackageManager.PERMISSION_GRANTED
                }

                if (isNotGranted) {
                    onPermissionNotGranted()
                } else {
                    //initCloseToMe()
                }
            }
        }
    }

    private fun onPermissionNotGranted() {
        Toast.makeText(this.requireContext(), "Se requiere bluetooth para el rastreo de dispositivos cercanos", Toast.LENGTH_SHORT).show()
    }

    private fun onStartClick() {
        //initService()
        with(this.requireActivity()) {
            Intent(this, BeaconService::class.java).also { intent ->
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    this.startForegroundService(intent)
                } else {
                    this.startService(intent)
                }
            }
        }
    }

    private fun onStopClick() {
        with(this.requireActivity()) {
            Intent(this, BeaconService::class.java).also { intent ->
                this.stopService(intent)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}