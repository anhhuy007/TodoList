package com.example.todolist.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.todolist.R

private val Ubuntu = FontFamily(
    Font(R.font.ubuntu_regular),
    Font(R.font.ubuntu_bold, FontWeight.Bold)
)

// Set of Material typography styles to start with
val Typography = Typography(
    h1 = TextStyle(
        fontFamily = Ubuntu,
        fontWeight = FontWeight.Normal,
        fontSize = 50.sp
    ),
    h2 = TextStyle(
        fontFamily = Ubuntu,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    h3 = TextStyle(
        fontFamily = Ubuntu,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),
    body1 = TextStyle(
        fontFamily = Ubuntu,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    )
)

val UbuntuTypography = Typography(
    h1 = TextStyle(
        fontFamily = Ubuntu,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp
    ),

    body1 = TextStyle(
        fontFamily = Ubuntu,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
)

