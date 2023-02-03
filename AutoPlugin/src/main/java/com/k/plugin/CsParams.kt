package com.k.plugin

import com.android.build.api.instrumentation.InstrumentationParameters
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input

interface CsParams : InstrumentationParameters {
    @get:Input
    val writeToStdout: Property<Boolean>
}