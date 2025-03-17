package com.uravgcode.chooser.settings.data

import androidx.datastore.core.Serializer
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object SettingsSerializer : Serializer<Settings> {
    override val defaultValue: Settings
        get() = Settings()

    override suspend fun readFrom(input: InputStream): Settings {
        return try {
            Json.decodeFromString(
                deserializer = Settings.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(
        settings: Settings,
        output: OutputStream
    ) {
        output.write(
            Json.encodeToString(
                serializer = Settings.serializer(),
                value = settings
            ).encodeToByteArray()
        )
    }
}
