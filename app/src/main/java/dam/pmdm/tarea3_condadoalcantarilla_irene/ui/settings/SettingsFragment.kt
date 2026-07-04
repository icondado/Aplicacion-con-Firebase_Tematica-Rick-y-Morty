package dam.pmdm.tarea3_condadoalcantarilla_irene.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import dam.pmdm.tarea3_condadoalcantarilla_irene.R
import dam.pmdm.tarea3_condadoalcantarilla_irene.databinding.FragmentSettingsBinding
import dam.pmdm.tarea3_condadoalcantarilla_irene.MainActivity
import dam.pmdm.tarea3_condadoalcantarilla_irene.utils.PreferencesManager

class SettingsFragment : Fragment() {
    
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var preferencesManager: PreferencesManager
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        preferencesManager = PreferencesManager(requireContext())
        
        loadCurrentSettings()
        setupListeners()
    }
    
    private fun loadCurrentSettings() {
        // Load language setting
        when (preferencesManager.getLanguage()) {
            PreferencesManager.LANGUAGE_SPANISH -> {
                binding.radioSpanish.isChecked = true
            }
            PreferencesManager.LANGUAGE_ENGLISH -> {
                binding.radioEnglish.isChecked = true
            }
        }
        
        // Load theme setting
        when (preferencesManager.getTheme()) {
            PreferencesManager.THEME_LIGHT -> {
                binding.radioLightTheme.isChecked = true
            }
            PreferencesManager.THEME_DARK -> {
                binding.radioDarkTheme.isChecked = true
            }
        }
    }
    
    private fun setupListeners() {
        // Language change
        binding.radioGroupLanguage.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioSpanish -> {
                    changeLanguage(PreferencesManager.LANGUAGE_SPANISH)
                }
                R.id.radioEnglish -> {
                    changeLanguage(PreferencesManager.LANGUAGE_ENGLISH)
                }
            }
        }
        
        // Theme change
        binding.radioGroupTheme.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioLightTheme -> {
                    preferencesManager.saveTheme(PreferencesManager.THEME_LIGHT)
                }
                R.id.radioDarkTheme -> {
                    preferencesManager.saveTheme(PreferencesManager.THEME_DARK)
                }
            }
        }
        
        // Logout
        binding.btnLogout.setOnClickListener {
            showLogoutConfirmation()
        }
    }
    
    private fun changeLanguage(language: String) {
        preferencesManager.saveLanguage(language)
        
        // Show dialog to restart app
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.language)
            .setMessage("La aplicación se reiniciará para aplicar el cambio de idioma")
            .setPositiveButton(R.string.close) { _, _ ->
                restartApp()
            }
            .setCancelable(false)
            .show()
    }
    
    private fun restartApp() {
        val intent = requireActivity().intent
        requireActivity().finish()
        startActivity(intent)
    }
    
    private fun showLogoutConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.logout)
            .setMessage("¿Estás seguro de que quieres cerrar sesión?")
            .setPositiveButton(R.string.logout) { _, _ ->
                (requireActivity() as MainActivity).logout()
            }
            .setNegativeButton(R.string.cancel_selection, null)
            .show()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
