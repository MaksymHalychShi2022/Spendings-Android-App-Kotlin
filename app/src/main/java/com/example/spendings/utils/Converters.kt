package com.example.spendings.utils

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromUnitOfMeasurement(unit: UnitOfMeasurement): String {
        return unit.unit
    }

    @TypeConverter
    fun toUnitOfMeasurement(unit: String): UnitOfMeasurement {
        return UnitOfMeasurement.values().firstOrNull { it.unit == unit } ?: UnitOfMeasurement.UNITS
    }

}