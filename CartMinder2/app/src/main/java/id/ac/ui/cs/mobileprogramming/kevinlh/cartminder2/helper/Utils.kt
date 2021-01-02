package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.helper

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

object Utils {
    fun numberToCurrencyFormat(num: Double): String {
        val formatter: NumberFormat = DecimalFormat("#,###")
        return formatter.format(num)
    }

    fun getRandomInt(min: Int, max: Int): Int = (min..max).random()

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

    fun isCalendarNotPassedAndToday(cal: Calendar): Boolean {
        val nowMillis = Calendar.getInstance().timeInMillis
        val tomorrowMillis = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_WEEK, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
        }.timeInMillis
        return cal.timeInMillis in nowMillis until tomorrowMillis
    }
}