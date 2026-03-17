package com.wstxda.switchai.fragment.preferences

import android.os.Bundle
import com.wstxda.switchai.R

class AccessibilityPreferencesFragment : BasePreferencesFragment() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_accessibility, rootKey)
    }
}