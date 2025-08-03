package com.wstxda.switchai.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.wstxda.switchai.ui.component.AssistantSelectorBottomSheet
import com.wstxda.switchai.utils.Constants

class AssistantSelectorActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val bottomSheet = AssistantSelectorBottomSheet()
            bottomSheet.show(supportFragmentManager, Constants.DIGITAL_ASSISTANT_SELECTOR_DIALOG)
        }

        supportFragmentManager.registerFragmentLifecycleCallbacks(object :
            FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
                super.onFragmentViewDestroyed(fm, f)
                if (f is AssistantSelectorBottomSheet) {
                    if (!isFinishing && !isChangingConfigurations) {
                        finish()
                    }
                    fm.unregisterFragmentLifecycleCallbacks(this)
                }
            }
        }, false)
    }
}