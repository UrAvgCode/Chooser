/*
 * Copyright (C) 2025 UrAvgCode
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *
 * @author UrAvgCode
 * @description JSON serializer for settings data
 */

package com.uravgcode.chooser.settings.data

import androidx.datastore.core.Serializer
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object SettingsSerializer : Serializer<SettingsData> {
    override val defaultValue: SettingsData
        get() = SettingsData()

    override suspend fun readFrom(input: InputStream): SettingsData {
        return try {
            Json.decodeFromString(
                deserializer = SettingsData.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(
        settingsData: SettingsData,
        output: OutputStream
    ) {
        output.write(
            Json.encodeToString(
                serializer = SettingsData.serializer(),
                value = settingsData
            ).encodeToByteArray()
        )
    }
}
