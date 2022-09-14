package com.solarhot.imagetag

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

//
// Created by lipeng on 2022/9/13.
// Copyright (c) 2022 SolarHot. All rights reserved.
//
@Composable
fun ImageTag() {
    var scale by remember { mutableStateOf(1f) }
    var offset  by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data("file:///android_asset/EyeImage.jpg")
//                .crossfade(true)
                .build(),
//            filterQuality = FilterQuality.High
        )

        Image(
            painter = painter,
            contentDescription = "Main Image",
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                // transformable() can't handle single finger drag,so use detectTransformGestures()
//                .transformable(
//                    state = rememberTransformableState(onTransformation = { zoomChange, panChange, rotationChange })
//                )
                // graphicsLayer() is better then scale() and offset()
                // it will only cause the layer properties update without triggering recomposition and relayout.
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = scale * offset.x
                    translationY = scale * offset.y
                }
//                .scale(scale)
//                .offset {
//                    IntOffset(offset.x.toInt(), offset.y.toInt())
//                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            scale = if (scale <= 1f) {
                                2f
                            } else {
                                1f
                            }
                            offset = Offset.Zero
                        }
                    )
                }
                .pointerInput(Unit) {
                    detectTransformGestures { centroid, pan, zoom, rotation ->
                        scale = (zoom * scale).coerceAtLeast(1f)
                        scale = if (scale > 5f) {
                            5f
                        } else {
                            scale
                        }

                        offset += pan
                    }
                }
        )
    }
}