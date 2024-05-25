package com.example.studentweatherapp.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.studentweatherapp.R
import com.example.studentweatherapp.data.WeatherModel
import com.example.studentweatherapp.ui.theme.BlueLight

@Composable
fun MainCard(
    currentDay: MutableState<WeatherModel>,
    onClickFavorite: () -> Unit,
    onClickMap: () -> Unit,
    onClickSync: () -> Unit
) {
    val textMeasurer = rememberTextMeasurer()

    // Элементы в основной карточке отображения текущей погоды располагаются сверху вниз.
    Column(
        modifier = Modifier.padding(5.dp)
    ) {
        // Отображение текущей погоды в карточке.
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = BlueLight),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            // Элементы внутри карточки текущей погоды.
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Первая строка отображает дату/время обновления и иконку погоды.
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Дата/время обновления данных о погоде.
                    Text(
                        modifier = Modifier.padding(top = 8.dp, start = 8.dp),
                        text = currentDay.value.time,
                        style = TextStyle(fontSize = 15.sp),
                        color = Color.White
                    )

                    // Иконка текущей погоды.
                    AsyncImage(
                        model = "https:" + currentDay.value.icon,
                        contentDescription = "currentWeatherConditionIcon",
                        modifier = Modifier
                            .size(35.dp)
                            .padding(top = 3.dp, end = 8.dp)
                    )
                }

                // Вторая строка отображает текущее местоположение.
                Text(
                    text = currentDay.value.city,
                    style = TextStyle(fontSize = 24.sp, fontFamily = FontFamily.SansSerif),
                    color = Color.White
                )

                // Третья строка отображает текущие давление, температуру и ветер.
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Текущее давление.
                    if (currentDay.value.pressHg != "") {
                        Column(
                            modifier = Modifier.padding(all = 5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Давление",
                                style = TextStyle(fontSize = 16.sp),
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = (currentDay.value.pressHg.toFloat() * 0.75).toInt()
                                    .toString() + " мм рт.ст.",
                                style = TextStyle(fontSize = 14.sp),
                                color = Color.White
                            )
                        }
                    }

                    // Текущая температура.
                    Text(
                        modifier = Modifier.padding(top = 8.dp, start = 8.dp),
                        text = if (currentDay.value.currentTemp.isNotEmpty())
                            currentDay.value.currentTemp.toFloat().toInt().toString() + "°C"
                        else "${currentDay.value.maxTemp.toFloat().toInt()}°C" +
                                "/${currentDay.value.minTemp.toFloat().toInt()}°C",
                        style = TextStyle(fontSize = 65.sp),
                        color = Color.White
                    )

                    // Текущий ветер.
                    if (currentDay.value.windDegree != "") {
                        Column(
                            modifier = Modifier,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Ветер",
                                style = TextStyle(fontSize = 14.sp),
                                color = Color.White
                            )
                            // Отображение стрелки направления ветра.
                            Canvas(
                                modifier = Modifier
                                    .size(60.dp)
                            ) {
                                // Север.
                                drawText(
                                    textMeasurer =  textMeasurer,
                                    text =  "С",
                                    topLeft = Offset(size.width / 2 - 10, 0f),
                                    style = TextStyle(
                                        fontSize = 10.sp,
                                        color = Color.White
                                    )
                                )

                                // Восток.
                                drawText(
                                    textMeasurer =  textMeasurer,
                                    text =  "В",
                                    topLeft = Offset(size.width - 25, size.height / 2 - 15),
                                    style = TextStyle(
                                        fontSize = 10.sp,
                                        color = Color.White
                                    )
                                )

                                // Юг.
                                drawText(
                                    textMeasurer =  textMeasurer,
                                    text =  "Ю",
                                    topLeft = Offset(size.width / 2 - 10, size.height - 30),
                                    style = TextStyle(
                                        fontSize = 10.sp,
                                        color = Color.White
                                    )
                                )

                                // Запад.
                                drawText(
                                    textMeasurer =  textMeasurer,
                                    text =  "З",
                                    topLeft = Offset(10f, size.height / 2 - 15),
                                    style = TextStyle(
                                        fontSize = 10.sp,
                                        color = Color.White
                                    )
                                )

                                val arrowPath = Path().apply {
                                    moveTo(size.width / 2, 50f)
                                    lineTo((size.width / 2) - 5, size.height - 70)
                                    lineTo((size.width / 2) - 10, size.height - 70)
                                    lineTo(size.width / 2, size.height - 50)
                                    lineTo((size.width / 2) + 10, size.height - 70)
                                    lineTo((size.width / 2) + 5, size.height - 70)
                                    lineTo(size.width / 2, 50f)
                                    close()
                                }
                                rotate(degrees = currentDay.value.windDegree.toFloat()) {
                                    drawPath(
                                        path = arrowPath,
                                        color = Color.White
                                    )
                                }
                            }
                            Text(
                                text = (currentDay.value.windKph.toFloat() / 3.6).toInt()
                                    .toString() + " м/с",
                                style = TextStyle(fontSize = 14.sp),
                                color = Color.White
                            )
                        }
                    }
                }

                // Четвертая строка отображает текущее состояние погоды.
                Text(
                    //modifier = Modifier.padding(top = 8.dp, start = 8.dp),
                    text = currentDay.value.condition,
                    style = TextStyle(fontSize = 16.sp),
                    color = Color.White
                )

                // Пятая строка отображает макс/мин температуру сегодня,
                Text(
                    text = "${
                        currentDay.value.minTemp.toFloat().toInt()
                    }°C/${
                        currentDay.value.maxTemp.toFloat().toInt()
                    }°C",
                    style = TextStyle(fontSize = 16.sp),
                    color = Color.White
                )

                // Шестая строка отображает кнопки поиска местоположения, добавления в любимые места
                // и обновления.
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Кнопка поиска местоположения по названию н.п.
                    IconButton(
                        onClick = { onClickFavorite.invoke() }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_favorite),
                            contentDescription = "favoriteIcon",
                            tint = Color.White
                        )
                    }

                    // Кнопка просмотра осадков на карте.
                    IconButton(
                        onClick = { onClickMap.invoke() }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_map),
                            contentDescription = "mapIcon",
                            tint = Color.White
                        )
                    }

                    // Кнопка обновления данных о погоде.
                    IconButton(
                        onClick = { onClickSync.invoke() }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_sync),
                            contentDescription = "syncIcon",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}