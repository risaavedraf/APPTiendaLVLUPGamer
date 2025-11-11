package com.example.tiendalvlupgamer.data.network

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeAdapter : TypeAdapter<LocalDateTime>() {

    // Ajusta el formato si tu backend envía uno diferente. ISO_LOCAL_DATE_TIME es común.
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: LocalDateTime?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.value(formatter.format(value))
        }
    }

    @Throws(IOException::class)
    override fun read(reader: JsonReader): LocalDateTime? {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            return null
        }
        val dateString = reader.nextString()
        return if (dateString.isNullOrEmpty()) {
            null
        } else {
            // Puede que necesites ajustar el formato si el backend no usa el estándar ISO
            LocalDateTime.parse(dateString, formatter)
        }
    }
}
