package com.pwani.majisafi.domain

import com.pwani.majisafi.domain.model.SalinityCategory

object SalinityClassifier {

    /**
     * Mombasa Coastal Aquifer Salinity Classification Rules:
     * - Fresh: < 500 ppm
     * - Mildly Brackish: 500 - 1500 ppm
     * - Brackish: 1501 - 3000 ppm
     * - Saline: > 3000 ppm
     */
    fun classify(tdsPpm: Int): SalinityCategory {
        return when {
            tdsPpm < 500 -> SalinityCategory.FRESH
            tdsPpm in 500..1500 -> SalinityCategory.MILDLY_BRACKISH
            tdsPpm in 1501..3000 -> SalinityCategory.BRACKISH
            else -> SalinityCategory.SALINE
        }
    }

    fun isPotableWithoutTreatment(tdsPpm: Int): Boolean {
        return tdsPpm < 1500
    }

    fun isSafeForDrinking(tdsPpm: Int): Boolean {
        return tdsPpm < 500
    }
}
