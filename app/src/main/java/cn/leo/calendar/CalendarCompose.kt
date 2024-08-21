package cn.leo.calendar

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar

/**
 * @Author     :Leo
 * Date        :2024/8/20
 * Description : calendar 日历
 */

/**
 * 日历状态
 */
enum class CalendarItemState {
    NORMAL, SELECTED, OTHER
}

/**
 * 日历数据
 */
data class CalendarData(
    val text: String,
    val state: CalendarItemState
)

class CalendarWrapper(val calendar: Calendar) {
    /**
     * 切换上一个月
     */
    fun monthMinus(): CalendarWrapper {
        calendar.add(Calendar.MONTH, -1)
        return CalendarWrapper(calendar)
    }

    /**
     * 切换下一个月
     */
    fun monthPlus(): CalendarWrapper {
        calendar.add(Calendar.MONTH, 1)
        return CalendarWrapper(calendar)
    }

    /**
     * 切换回今天
     */
    fun today(): CalendarWrapper {
        return CalendarWrapper(Calendar.getInstance())
    }
}

/**
 * 日历组件
 */
@Composable
fun CalendarItem(
    data: CalendarData
) {
    Text(
        text = data.text,
        modifier = Modifier
            .width(30.dp)
            .height(30.dp)
            .background(
                color = when (data.state) {
                    CalendarItemState.NORMAL -> Color.Transparent
                    CalendarItemState.SELECTED -> Color.Blue
                    CalendarItemState.OTHER -> Color.Transparent
                }, shape = CircleShape
            )
            .padding(5.dp)
            .wrapContentSize(Alignment.Center),
        fontSize = 14.sp,
        textAlign = TextAlign.Center,
        color = when (data.state) {
            CalendarItemState.NORMAL -> Color.Black
            CalendarItemState.SELECTED -> Color.White
            CalendarItemState.OTHER -> Color.LightGray
        },
        style = TextStyle(
            platformStyle = PlatformTextStyle(
                includeFontPadding = false
            )
        )
    )
}

/**
 * 日历抬头
 */
@Composable
fun CalendarHead(mutableCalendar: MutableState<CalendarWrapper>) {
    val calendar = mutableCalendar.value.calendar
    val weekHead = arrayOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")
    val yyyyMMdd =
        "${calendar.get(Calendar.YEAR)} / ${calendar.get(Calendar.MONTH) + 1}"
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {
                mutableCalendar.value = mutableCalendar.value.monthMinus()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "ArrowBack"
                )
            }
            Text(text = yyyyMMdd)
            IconButton(onClick = {
                mutableCalendar.value = mutableCalendar.value.monthPlus()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "ArrowForward"
                )
            }
            TextButton(onClick = {
                mutableCalendar.value = mutableCalendar.value.today()
            }) {
                Text(text = "Today")
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            weekHead.map {
                Text(text = it)
            }
        }
    }
}

/**
 * 判断2个 Calendar 是不是同一条
 */
private fun Calendar.isSameDay(calendar: Calendar): Boolean {
    return this.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
            this.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
            this.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)
}

/**
 * 日历面板
 */
@Composable
fun LeoCalendar(calendar: Calendar) {
    val c = remember(calendar) {
        mutableStateOf(CalendarWrapper(calendar))
    }
    val weekList = getCalendarList(c.value.calendar)
    Column {
        CalendarHead(c)
        Spacer(modifier = Modifier.height(8.dp))
        weekList.forEach { days ->
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                days.forEach {
                    CalendarItem(data = it)
                }
            }
        }
    }
}

private fun getCalendarList(c: Calendar): ArrayList<MutableList<CalendarData>> {
    Log.d("getCalendarList", "${c.time}")
    val calendar = Calendar.getInstance().apply {
        time = c.time
    }
    //设置calendar为当月第一天
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    //当前第几月
    val month = calendar.get(Calendar.MONTH)
    //当月第一天是周几
    val week = calendar.get(Calendar.DAY_OF_WEEK)
    //生成当月数据
    val weekList = arrayListOf<MutableList<CalendarData>>()
    //第一行第一天
    calendar.add(Calendar.DAY_OF_MONTH, -week + 1)
    var day = calendar.get(Calendar.DAY_OF_MONTH)
    repeat(6) {
        val weekDayList = mutableListOf<CalendarData>()
        repeat(7) {
            var state = CalendarItemState.NORMAL
            if (month != calendar.get(Calendar.MONTH))
                state = CalendarItemState.OTHER
            val isToday = Calendar.getInstance().isSameDay(calendar)
            if (isToday) state = CalendarItemState.SELECTED
            val element = CalendarData(day.toString(), state)
            weekDayList.add(element)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            day = calendar.get(Calendar.DAY_OF_MONTH)
        }
        weekList.add(weekDayList)
    }
    return weekList
}

/**
 * 预览日历组件
 */

@Composable
@Preview
fun PreviewItem() {
    CalendarItem(data = CalendarData("31", CalendarItemState.SELECTED))
}

/**
 * 预览日历面板
 */

@Composable
@Preview(widthDp = 375, backgroundColor = 0xFFFFFFFF, showBackground = true)
fun PreviewCalendar() {
    val calendar = Calendar.getInstance()
    //calendar.add(Calendar.MONTH, -1)
    LeoCalendar(calendar)
}
