package cn.kuolemax.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.kuolemax.calculator.ui.theme.Background
import cn.kuolemax.calculator.ui.theme.DarkGray
import cn.kuolemax.calculator.ui.theme.LightGray
import cn.kuolemax.calculator.ui.theme.Orange

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Calculator()
        }
    }
}

data class Button(
    val text: String,
    val weight: Float = 1f,
    val color: Color = Color.DarkGray
)

// 所有的按钮
val buttons = arrayOf(
    arrayOf(
        Button("AC", color = LightGray),
        Button("+/-", color = LightGray),
        Button("%", color = LightGray),
        Button("/", color = Orange)
    ),
    arrayOf(
        Button("7", color = DarkGray),
        Button("8", color = DarkGray),
        Button("9", color = DarkGray),
        Button("x", color = Orange)
    ),
    arrayOf(
        Button("4", color = DarkGray),
        Button("5", color = DarkGray),
        Button("6", color = DarkGray),
        Button("-", color = Orange)
    ),
    arrayOf(
        Button("1", color = DarkGray),
        Button("2", color = DarkGray),
        Button("3", color = DarkGray),
        Button("+", color = Orange)
    ),
    arrayOf(
        Button("0", 2f, DarkGray),
        Button(".", color = DarkGray),
        Button("=", color = Orange)
    )
)

// 计算状态
data class CalculatorState(
    val number1: Int = 0,
    val number2: Int = 0,
    val opt: String? = null
)

@Preview(showSystemUi = true)
@Composable
fun Calculator() {

    var state by remember {
        mutableStateOf(CalculatorState())
    }

    Column(
        Modifier
            .background(Background)
            .padding(horizontal = 10.dp)
    ) {
        Box(
            Modifier
                .fillMaxHeight(0.3f)
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text("${state.number2}", fontSize = 100.sp, color = Color.White)
        }
        Column(Modifier.fillMaxSize()) {
            buttons.forEach { row ->
                Row(
                    Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    row.forEach { col ->
                        CalculationButton(
                            Modifier
                                .weight(col.weight)
                                .aspectRatio(col.weight)
                                .background(col.color),
                            col.text
                        ) {
                            state = calculator(state, col.text)
                        }
                    }
                }
            }
        }
    }
}

fun calculator(curState: CalculatorState, input: String): CalculatorState {
    // number2 存放输入，number1存储结果
    return when (input) {
        in "0".."9" -> curState.copy(number2 = input.toInt(), number1 = curState.number2)
        in arrayOf("+", "-", "x", "/") -> curState.copy(opt = input)
        // @formatter:off
        "=" -> return when (curState.opt) {
            "+" -> curState.copy(number2 = curState.number1 + curState.number2, opt = null)
            "-" -> curState.copy(number2 = curState.number1 - curState.number2, opt = null)
            "x" -> curState.copy(number2 = curState.number1 * curState.number2, opt = null)
            "/" -> curState.copy(number2 = curState.number1 / curState.number2, opt = null)
            else -> curState
        }
        "AC" -> return CalculatorState()
        "+/-" -> return curState.copy(number2 = 0 - curState.number2, number1 = 0, opt = null)
        else -> curState
        // @formatter:on
    }

}


@Composable
fun CalculationButton(modifier: Modifier, symbol: String, onClick: () -> Unit = {}) {
    Box(
        Modifier
            // clip 必须放在背景前面才能生效，链式调用需要注意顺序
            .clip(CircleShape)
            .then(modifier)
            .clickable { onClick.invoke() },
        contentAlignment = Alignment.Center
    ) {
        Text(symbol, fontSize = 40.sp, color = Color.White)
    }
}
