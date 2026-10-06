// Copyright 2026, compose-miuix-ui contributors
// SPDX-License-Identifier: Apache-2.0

package com.wstxda.switchai.ui.components.effects

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Brush
import kotlin.math.cos
import kotlin.math.sin
import top.yukonga.miuix.kmp.blur.RuntimeShader
import top.yukonga.miuix.kmp.blur.asBrush

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
internal class BgEffectPainter {

    val runtimeShader by lazy {
        RuntimeShader(OS3_BG_FRAG).also {
            initStaticUniforms(it)
        }
    }

    val brush: Brush
        get() = runtimeShader.asBrush()

    private val resolution = FloatArray(2)
    private val bound = FloatArray(4)
    private val colorsBuffer = FloatArray(16)
    private val pointsAnimBuffer = FloatArray(8)

    private var animTime = Float.NaN
    private var isDarkCached: Boolean? = null
    private var deviceTypeCached: DeviceType? = null

    private var presetApplied = false

    private var cachedLogoHeight = Float.NaN
    private var cachedTotalHeight = Float.NaN
    private var cachedTotalWidth = Float.NaN

    private var cachedColorStage = Float.NaN
    private var cachedColorsPreset: BgEffectPreset? = null

    private var cachedPointsAnimTime = Float.NaN
    private var cachedPointsAnimPreset: BgEffectPreset? = null

    private fun initStaticUniforms(shader: RuntimeShader) {
        shader.setFloatUniform("uTranslateY", BgEffectConfig.U_TRANSLATE_Y)
        shader.setFloatUniform("uNoiseScale", BgEffectConfig.U_NOISE_SCALE)
        shader.setFloatUniform("uPointRadiusMulti", BgEffectConfig.U_POINT_RADIUS_MULTI)
        shader.setFloatUniform("uAlphaMulti", BgEffectConfig.U_ALPHA_MULTI)
    }

    fun updateResolution(width: Float, height: Float) {
        if (resolution[0] == width && resolution[1] == height) return
        resolution[0] = width
        resolution[1] = height
        runtimeShader.setFloatUniform("uResolution", resolution)
    }

    fun updateAnimTime(time: Float) {
        if (animTime == time) return
        animTime = time
        runtimeShader.setFloatUniform("uAnimTime", animTime)
    }

    fun updatePointsAnim(time: Float, preset: BgEffectPreset) {
        if (cachedPointsAnimTime == time && cachedPointsAnimPreset === preset) return

        val offset = preset.pointOffset
        var i = 0
        while (i < 4) {
            val srcX = preset.points[i * 3]
            val srcY = preset.points[i * 3 + 1]
            val animX = srcX + sin(time + srcY) * offset
            val animY = srcY + cos(time + animX) * offset
            pointsAnimBuffer[i * 2] = animX
            pointsAnimBuffer[i * 2 + 1] = animY
            i++
        }
        runtimeShader.setFloatUniform("uPointsAnim", pointsAnimBuffer)

        cachedPointsAnimTime = time
        cachedPointsAnimPreset = preset
    }

    fun updateColors(preset: BgEffectPreset, stage: Float) {
        if (cachedColorsPreset === preset && cachedColorStage == stage) return

        val base = stage.toInt()
        val fraction = stage - base
        val start = colorsForCycleIndex(preset, base)
        val end = colorsForCycleIndex(preset, base + 1)
        for (i in 0 until 16) {
            colorsBuffer[i] = start[i] + (end[i] - start[i]) * fraction
        }
        runtimeShader.setFloatUniform("uColors", colorsBuffer)

        cachedColorsPreset = preset
        cachedColorStage = stage
    }

    private fun colorsForCycleIndex(preset: BgEffectPreset, index: Int): FloatArray =
        when (index.mod(4)) {
            1 -> preset.colors1
            3 -> preset.colors3
            else -> preset.colors2
        }

    fun updateBoundIfNeeded(
        logoHeight: Float,
        totalHeight: Float,
        totalWidth: Float,
    ) {
        if (cachedLogoHeight == logoHeight && cachedTotalHeight == totalHeight && cachedTotalWidth == totalWidth) {
            return
        }

        updateBound(logoHeight, totalHeight, totalWidth)
        runtimeShader.setFloatUniform("uBound", bound)

        cachedLogoHeight = logoHeight
        cachedTotalHeight = totalHeight
        cachedTotalWidth = totalWidth
    }

    fun updatePresetIfNeeded(deviceType: DeviceType, isDark: Boolean) {
        if (presetApplied && isDarkCached == isDark && deviceTypeCached == deviceType) return

        applyPreset(deviceType, isDark)

        isDarkCached = isDark
        deviceTypeCached = deviceType
        presetApplied = true
    }

    private fun applyPreset(deviceType: DeviceType, isDark: Boolean) {
        val preset = bgEffectPreset(deviceType, isDark)

        runtimeShader.setFloatUniform("uPoints", preset.points)
        runtimeShader.setFloatUniform("uLightOffset", preset.lightOffset)
        runtimeShader.setFloatUniform("uSaturateOffset", preset.saturateOffset)
    }

    private fun updateBound(
        logoHeight: Float,
        totalHeight: Float,
        totalWidth: Float,
    ) {
        val heightRatio = logoHeight / totalHeight
        if (totalWidth <= totalHeight) {
            bound[0] = 0f
            bound[1] = 1f - heightRatio
            bound[2] = 1f
            bound[3] = heightRatio
        } else {
            val aspectRatio = totalWidth / totalHeight
            val contentCenterY = 1f - heightRatio / 2f
            bound[0] = 0f
            bound[1] = contentCenterY - aspectRatio / 2f
            bound[2] = 1f
            bound[3] = aspectRatio
        }
    }
}

internal fun bgEffectPreset(deviceType: DeviceType, isDark: Boolean): BgEffectPreset =
    when (deviceType) {
        DeviceType.PHONE -> if (isDark) BgEffectConfig.OS3_PHONE_DARK else BgEffectConfig.OS3_PHONE_LIGHT

        DeviceType.PAD -> if (isDark) BgEffectConfig.OS3_PAD_DARK else BgEffectConfig.OS3_PAD_LIGHT
    }