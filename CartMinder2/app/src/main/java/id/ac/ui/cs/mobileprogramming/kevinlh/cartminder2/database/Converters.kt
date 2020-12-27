package id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import id.ac.ui.cs.mobileprogramming.kevinlh.cartminder2.enums.MarketCategory
import java.lang.reflect.Type
import java.util.*

class Converters {

    @TypeConverter
    fun fromCalendar(value: Calendar): Long = value.timeInMillis

    @TypeConverter
    fun toCalendar(value: Long): Calendar = Calendar.getInstance().apply { timeInMillis = value }

    @TypeConverter
    fun fromMarketCategory(value: MarketCategory): String = value.name

    @TypeConverter
    fun toMarketCategory(value: String): MarketCategory = MarketCategory.valueOf(value)

    @TypeConverter
    fun fromMutableListOfString(value: MutableList<String>): String {
        val gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun toMutableListOfString(value: String): MutableList<String> {
        val listType: Type = object : TypeToken<MutableList<String>>() {}.type
        return Gson().fromJson(value, listType)
    }
}