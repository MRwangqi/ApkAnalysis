package com.codelang.apkanalysis.ui.widget

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun PieChartWidget(innerCircleColor: Color,list: List<Pair<Long, Color>>, modifier: Modifier = Modifier) {
    val total = list.sumOf { it.first }.toFloat()

    val angleList = list.map {
        Pair(it.first / total, it.second)
    }.toList()

    Canvas(modifier) {
        val width = size.width
        val height = size.height

        val radius = if (width > height) height / 4 else width / 4
        var currentAngle = 0f

        angleList.forEach { triple ->
            val angle = triple.first * 360f
            val color = triple.second
            drawArc(color, currentAngle, angle, true)
            currentAngle += angle
        }
        drawCircle(innerCircleColor, radius)
    }
}

@Preview
@Composable
fun PreviewPieChart() {
    val list = arrayListOf<Pair<Long, Color>>()
    list.add(Pair(1, Color.Red))
    list.add(Pair(4, Color.Blue))
    list.add(Pair(8, Color.Gray))
    list.add(Pair(2, Color.Green))
    list.add(Pair(2, Color.Cyan))
    list.add(Pair(1, Color.Black))

    MaterialTheme {
        Surface {
            Column {
                PieChartWidget(Color.White,list,Modifier.size(50.dp))
                PieChartWidget(Color.White,list,Modifier.size(100.dp))
            }
        }
    }
}