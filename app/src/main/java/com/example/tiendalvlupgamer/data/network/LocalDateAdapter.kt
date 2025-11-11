package com.example.tiendalvlupgamer.data.network

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateAdapter : TypeAdapter<LocalDate>() {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: LocalDate?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.value(formatter.format(value))
        }
    }

    @Throws(IOException::class)
    override fun read(input: JsonReader): LocalDate? {
        if (input.peek() == JsonToken.NULL) {
            input.nextNull()
            return null
        }
        val dateString = input.nextString()
        return if (dateString.isNullOrEmpty()) {
            null
        } else {
            LocalDate.parse(dateString, formatter)
        }
    }
}