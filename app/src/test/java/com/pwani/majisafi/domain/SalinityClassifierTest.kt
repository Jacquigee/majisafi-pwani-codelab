package com.pwani.majisafi.domain

import com.pwani.majisafi.domain.model.SalinityCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SalinityClassifierTest {

    @Test
    fun classify_freshWater_under500ppm() {
        assertEquals(SalinityCategory.FRESH, SalinityClassifier.classify(0))
        assertEquals(SalinityCategory.FRESH, SalinityClassifier.classify(250))
        assertEquals(SalinityCategory.FRESH, SalinityClassifier.classify(499))
        assertTrue(SalinityClassifier.isSafeForDrinking(420))
        assertTrue(SalinityClassifier.isPotableWithoutTreatment(420))
    }

    @Test
    fun classify_mildlyBrackish_500To1500ppm() {
        assertEquals(SalinityCategory.MILDLY_BRACKISH, SalinityClassifier.classify(500))
        assertEquals(SalinityCategory.MILDLY_BRACKISH, SalinityClassifier.classify(1000))
        assertEquals(SalinityCategory.MILDLY_BRACKISH, SalinityClassifier.classify(1500))
        assertFalse(SalinityClassifier.isSafeForDrinking(880))
        assertTrue(SalinityClassifier.isPotableWithoutTreatment(880))
    }

    @Test
    fun classify_brackish_1501To3000ppm() {
        assertEquals(SalinityCategory.BRACKISH, SalinityClassifier.classify(1501))
        assertEquals(SalinityCategory.BRACKISH, SalinityClassifier.classify(2150))
        assertEquals(SalinityCategory.BRACKISH, SalinityClassifier.classify(3000))
        assertFalse(SalinityClassifier.isSafeForDrinking(2150))
        assertFalse(SalinityClassifier.isPotableWithoutTreatment(2150))
    }

    @Test
    fun classify_saline_above3000ppm() {
        assertEquals(SalinityCategory.SALINE, SalinityClassifier.classify(3001))
        assertEquals(SalinityCategory.SALINE, SalinityClassifier.classify(4500))
        assertFalse(SalinityClassifier.isSafeForDrinking(3500))
        assertFalse(SalinityClassifier.isPotableWithoutTreatment(3500))
    }
}
