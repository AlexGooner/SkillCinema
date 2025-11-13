package com.citrus.skillcinema.presentation.onboaringpage

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.citrus.skillcinema.R
import com.citrus.skillcinema.databinding.FragmentSplashBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null

    private val binding get() = _binding!!

    @Inject
    lateinit var prefs: DataStore<Preferences>

    private val handler = Handler(Looper.getMainLooper())

    private val runnable = Runnable {
        lifecycleScope.launch {
            prefs.data.collectLatest {
                requireView().findNavController()
                    .navigate(R.id.action_splash_to_homepage)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val botNav = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        botNav.visibility = View.GONE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.insetsController?.hide(WindowInsets.Type.captionBar())
        }
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable, 2000)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

}