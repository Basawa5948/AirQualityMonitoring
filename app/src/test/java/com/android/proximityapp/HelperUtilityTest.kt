package com.android.proximityapp

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.android.proximityapp.utility.HelperUtility
import com.android.proximityapp.utility.StringConstants
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.runners.MockitoJUnitRunner

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
class HelperUtilityTest {

    private lateinit var helperUtility: HelperUtility
    private lateinit var mContext: Context

    @Before
    fun beforeTest() {
        helperUtility = Mockito.mock(HelperUtility::class.java)
        Mockito.`when`(mContext.resources.getColor(R.color.good)).thenReturn(R.color.good)
        Mockito.`when`(mContext.resources.getColor(R.color.satisfactory)).thenReturn(R.color.satisfactory)
        Mockito.`when`(mContext.resources.getColor(R.color.moderate)).thenReturn(R.color.moderate)
        Mockito.`when`(mContext.resources.getColor(R.color.poor)).thenReturn(R.color.poor)
        Mockito.`when`(mContext.resources.getColor(R.color.very_poor)).thenReturn(R.color.very_poor)
        Mockito.`when`(mContext.resources.getColor(R.color.severe)).thenReturn(R.color.severe)
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        mContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.android.proximityapp", mContext.packageName)
    }

    @Test
    fun getMinuteFromTimeTest() {
        val testString1 = "13:25:59 AM"
        val list = helperUtility.getMinuteFromTime(testString1)
        val minutes = list[0]
        assertEquals("25",minutes)

        val testString2 = "13:25:59 AM"
        val list1 = helperUtility.getMinuteFromTime(testString2)
        val minutes1 = list1[0]
        assertNotEquals("24",minutes1)
    }

    @Test
    fun getColorFromAQIValueTest() {
        val colorCode1 = helperUtility.getColorFromAQIValue(47)
        assertEquals(R.color.good,colorCode1)
        val colorCode2 = helperUtility.getColorFromAQIValue(67)
        assertEquals(R.color.satisfactory,colorCode2)
        val colorCode3 = helperUtility.getColorFromAQIValue(107)
        assertEquals(R.color.moderate,colorCode3)
        val colorCode4 = helperUtility.getColorFromAQIValue(207)
        assertEquals(R.color.poor,colorCode4)
        val colorCode5 = helperUtility.getColorFromAQIValue(307)
        assertEquals(R.color.very_poor,colorCode5)
        val colorCode6 = helperUtility.getColorFromAQIValue(407)
        assertEquals(R.color.severe,colorCode6)
    }

    @Test
    fun getLastUpdatedValueTest() {
        val lastUpdatedString1 = helperUtility.getLastUpdatedValue(10,10)
        assertEquals(StringConstants.HEADINGONE,lastUpdatedString1)
        val lastUpdatedString2 = helperUtility.getLastUpdatedValue(11,10)
        assertEquals(StringConstants.HEADINGTWO,lastUpdatedString2)
        val lastUpdatedString3 = helperUtility.getLastUpdatedValue(12,10)
        assertEquals("",lastUpdatedString3)
    }



}