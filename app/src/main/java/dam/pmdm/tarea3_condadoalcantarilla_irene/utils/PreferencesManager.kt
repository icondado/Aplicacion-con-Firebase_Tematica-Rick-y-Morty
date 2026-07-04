package dam.pmdm.tarea3_condadoalcantarilla_irene.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import java.util.*
import androidx.core.content.edit

@Suppress("DEPRECATION")
class PreferencesManager(context: Context) {
    
    private val prefs: SharedPreferences = 
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    companion object {
        private const val PREFS_NAME = "rick_and_morty_prefs"
        private const val KEY_LANGUAGE = "language"
        private const val KEY_THEME = "theme"
        
        const val LANGUAGE_SPANISH = "es"
        const val LANGUAGE_ENGLISH = "en"
        
        const val THEME_LIGHT = "light"
        const val THEME_DARK = "dark"
    }
    
    // Language
    fun saveLanguage(language: String) {
        prefs.edit { putString(KEY_LANGUAGE, language) }
    }
    
    fun getLanguage(): String {
        return prefs.getString(KEY_LANGUAGE, LANGUAGE_SPANISH) ?: LANGUAGE_SPANISH
    }
    
    fun setLocale(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        
        val config = context.resources.configuration
        config.setLocale(locale)
        context.createConfigurationContext(config)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
    
    // Theme
    fun saveTheme(theme: String) {
        prefs.edit { putString(KEY_THEME, theme) }
        applyTheme(theme)
    }
    
    fun getTheme(): String {
        return prefs.getString(KEY_THEME, THEME_LIGHT) ?: THEME_LIGHT
    }
    
    fun applyTheme(theme: String) {
        when (theme) {
            THEME_LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            THEME_DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}
