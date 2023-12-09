package com.headtopics.composecanva

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ContentAlpha

@Composable
fun CustomComponent(
    canvasSize : Dp = 300.dp,
    indicatorValue : Int = 0,
    maxIndicatorValue:Int = 100,
    backgroundIndicatorColor: Color = Color.Gray,
    backgroundIndicatorStrokeWith:Float = 100f,
    foregroundIndicatorColor: Color = Color.Blue,
    foregroundIndicatorStrokeWith:Float = 100f,
    bigTextFontSize: TextUnit = MaterialTheme.typography.headlineLarge.fontSize,
    bigTextColor: Color = MaterialTheme.colorScheme.onSurface,
    bigTExtSuffix: String = "GB",
    smallText: String = "Remaining",
    smallTextFontSize: TextUnit = MaterialTheme.typography.headlineMedium.fontSize,
    smallTextColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)


){

    var animatedIndicatorValue by remember { mutableStateOf(0f) }

    var allowedMaxIndıcatorValue by remember { mutableStateOf(maxIndicatorValue) }
    allowedMaxIndıcatorValue = if(indicatorValue <= maxIndicatorValue){
        indicatorValue
    }else{
        maxIndicatorValue
    }
    LaunchedEffect(key1 = allowedMaxIndıcatorValue){
        animatedIndicatorValue = allowedMaxIndıcatorValue.toFloat()
    }
    val percentage = (animatedIndicatorValue / maxIndicatorValue) * 100
    val sweepAngle by animateFloatAsState(targetValue = (2.4 * percentage).toFloat(),
        animationSpec = tween(durationMillis = 1000), label = ""
    )
    val receivedValue by animateIntAsState(
        targetValue = allowedMaxIndıcatorValue,
        animationSpec = tween(
            durationMillis = 1000
        ), label = ""
    )
    val animatedBigTextColor by animateColorAsState(
        targetValue = if(allowedMaxIndıcatorValue == 0){
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        }else{
            bigTextColor
        }, animationSpec = tween(
            durationMillis = 1000
        )
        , label = ""
    )

    Column (modifier = Modifier
        .size(canvasSize)
        .drawBehind {
            val componentSize = size / 1.25f
            backgroundIndicator(
                componentSize = componentSize,
                indicatorColor = backgroundIndicatorColor,
                indicatorStrokeWith = backgroundIndicatorStrokeWith
            )
            foregroundIndicator(
                sweepAngle = sweepAngle,
                componentSize = componentSize,
                indicatorColor = foregroundIndicatorColor,
                indicatorStrokeWith = foregroundIndicatorStrokeWith
            )
        }, verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
            EmbeddedElement(
                bigText = receivedValue ,
                bigTextFontSize = bigTextFontSize,
                bigTextColor = animatedBigTextColor,
                bigTExtSuffix = bigTExtSuffix,
                smallText = smallText,
                smallTextColor = smallTextColor ,
                smallTextFontSize =smallTextFontSize
            )
    }

}

fun DrawScope.backgroundIndicator(
    componentSize: Size,
    indicatorColor: Color,
    indicatorStrokeWith: Float
){
    drawArc(
        size = componentSize,
        color =  indicatorColor,
        startAngle = 150f,
        sweepAngle = 240f,
        useCenter = false,
        style = Stroke(
            width = indicatorStrokeWith,
            cap = StrokeCap.Round
        ),
        topLeft = Offset(
            x = (size.width - componentSize.width) / 2f,
            y= (size.height - componentSize.height) / 2f
        )
    )
}

fun DrawScope.foregroundIndicator(
    sweepAngle:Float,
    componentSize: Size,
    indicatorColor: Color,
    indicatorStrokeWith: Float
){
    drawArc(
        size = componentSize,
        color =  indicatorColor,
        startAngle = 150f,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(
            width = indicatorStrokeWith,
            cap = StrokeCap.Round
        ),
        topLeft = Offset(
            x = (size.width - componentSize.width) / 2f,
            y= (size.height - componentSize.height) / 2f
        )
    )
}

@Composable
fun EmbeddedElement(
    bigText:Int,
    bigTextFontSize:TextUnit,
    bigTextColor:Color,
    bigTExtSuffix:String,
    smallText:String,
    smallTextColor:Color,
    smallTextFontSize:TextUnit,
){
    Text(
        text = smallText,
        color = smallTextColor,
        fontSize = smallTextFontSize,
        textAlign = TextAlign.Center
    )

    Text(
        text = "${bigText} ${bigTExtSuffix.take(2)}",
        color = bigTextColor,
        fontSize = bigTextFontSize,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold
    )
}

@Composable
@Preview (showBackground = true)
fun CustomComponentPreview(){
    CustomComponent()
}