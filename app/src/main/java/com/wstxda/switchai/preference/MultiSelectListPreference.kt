package com.wstxda.switchai.preference

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.withStyledAttributes
import androidx.preference.MultiSelectListPreference
import com.wstxda.switchai.R

class MultiSelectListPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : MultiSelectListPreference(context, attrs) {

    var minSelection = 1
    var maxSelection = Int.MAX_VALUE

    init {
        context.withStyledAttributes(attrs, R.styleable.CustomMultiSelectListPreference) {
            minSelection =
                getInt(R.styleable.CustomMultiSelectListPreference_minSelection, 0).coerceAtLeast(0)
            maxSelection = getInt(
                R.styleable.CustomMultiSelectListPreference_maxSelection, Int.MAX_VALUE
            ).coerceAtLeast(minSelection)
        }
    }
}