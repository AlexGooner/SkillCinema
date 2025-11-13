package com.citrus.skillcinema.presentation.onboaringpage

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.citrus.skillcinema.presentation.adapters.onboardingpage.OnboardingSliderAdapter
import com.citrus.skillcinema.R
import com.citrus.skillcinema.databinding.FragmentOnboardingBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingFragment : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null

    private val binding get() = _binding!!

    @Inject
    lateinit var prefs: DataStore<Preferences>
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = Runnable {
        lifecycleScope.launch {
            prefs.data.collectLatest {
                if (it[booleanPreferencesKey("onBoard")] == true)
                    findNavController().navigate(R.id.navigation_splash)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            prefs.data.collectLatest {
                if (it[booleanPreferencesKey("onBoard")] == true) findNavController().navigate(R.id.navigation_splash)
                else binding.root.visibility = View.VISIBLE
            }
        }

        val onboardingSliderAdapter = OnboardingSliderAdapter(
            listOf(
                OnboardingSlide(
                    R.drawable.picture_splash,
                    getString(R.string.onboarding_first_page)
                ),
                OnboardingSlide(
                    R.drawable.picture_onboarding_second,
                    getString(R.string.onboarding_second_text)
                ),
                OnboardingSlide(
                    R.drawable.picture_onboarding_third,
                    getString(R.string.onboarding_third_text)
                )
            )
        )

        val botNav = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        botNav.visibility = View.GONE
        binding.onboardingViewPager.adapter = onboardingSliderAdapter

        binding.onboardingIndicator.setViewPager(binding.onboardingViewPager)
        binding.onboardingMiss.setOnClickListener {
            lifecycleScope.launch {
                saveOnboarding()
            }
            findNavController()
                .navigate(R.id.action_onboarding_to_splash)
        }
        binding.onboardingViewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {

            }
        )

    }

    private suspend fun saveOnboarding() {
        prefs.edit {
            val oneTime = true
            it[booleanPreferencesKey("onBoard")] = oneTime
        }
    }

    override fun onResume() {
        super.onResume()
        handler.removeCallbacks(runnable)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

}