package com.android.proximityapp.utility

import com.android.proximityapp.R

class HelperUtility {

    fun getMinuteFromTime(lastUpdatedValue:String): List<String> {
        return lastUpdatedValue.replace("(?s)^.*?:|:[^:]*$".toRegex(), "").split("(?s):([^:]*\\s[^:]*:)?".toRegex())
    }

    fun getColorFromAQIValue(aqiValue:Int): Int {
        when(aqiValue) {
            in 1..50 ->{
                //Category = Good
                return R.color.good
            }in 51..100 ->{
                //Category = Satisfactory
                return R.color.satisfactory
            }in 101..200 ->{
                //Category = Moderate
                return R.color.moderate
            }in 201..300 ->{
                //Category = Poor
                return R.color.poor
            }in 301..400 ->{
                //Category = Very Poor
                return R.color.very_poor
            }else ->{
                //Category = Severe
                return R.color.severe
            }
        }
    }

    fun getLastUpdatedValue(newValue:Int,oldValue:Int):String{
        return when(newValue - oldValue){
            0 ->{
                StringConstants.HEADINGONE
            } in 1..1 ->{
                StringConstants.HEADINGTWO
            } else ->{
                ""
            }
        }
    }
}