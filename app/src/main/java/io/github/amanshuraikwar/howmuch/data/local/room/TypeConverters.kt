package io.github.amanshuraikwar.howmuch.data.local.room

import android.arch.persistence.room.TypeConverter
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

/**
 * Created by amanshuraikwar on 09/03/18.
 */
class TypeConverters {

    companion object {

        @JvmStatic
        private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

        @TypeConverter
        @JvmStatic
        fun toOffsetDateTime(value: String?): OffsetDateTime? {
            return value?.let {
                return formatter.parse(value, OffsetDateTime::from)
            }
        }

        @TypeConverter
        @JvmStatic
        fun fromOffsetDateTime(date: OffsetDateTime?): String? {
            return date?.format(formatter)
        }
    }
}