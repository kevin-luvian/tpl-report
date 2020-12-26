package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.generalhelper

import java.util.*

object Utils {
    fun getRandomString(length: Int): String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }

    fun calendarToDateString(cal: Calendar): String =
        "${cal[Calendar.DAY_OF_MONTH]}/${cal[Calendar.MONTH]}/${cal[Calendar.YEAR]}"

    fun calendarToTimeString(cal: Calendar): String =
        String.format("%02d:%02d", cal[Calendar.HOUR_OF_DAY], cal[Calendar.MINUTE])
}