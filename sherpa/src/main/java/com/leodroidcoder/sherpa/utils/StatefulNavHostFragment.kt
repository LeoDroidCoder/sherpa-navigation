package com.leodroidcoder.sherpa.utils

import androidx.navigation.NavController
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment

class StatefulNavHostFragment : NavHostFragment() {

    override fun createFragmentNavigator(): Navigator<out FragmentNavigator.Destination> =
        StatefulFragmentNavigator(requireContext(), childFragmentManager, id)

    override fun onCreateNavController(navController: NavController) {
        super.onCreateNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination is FragmentNavigator.Destination) {
                childFragmentManager.fragments.find { it.javaClass.name == destination.className }
                    ?.onResume()
            }
        }
    }
}