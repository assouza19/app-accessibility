package com.example.goout.extensions


import java.lang.Math.abs
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

const val DEFAULT_DATE_PATTERN = "yyyy-MM-dd"
const val DATE_SIMPLE_PATTERN = "yyyyMMdd"
const val WEEK_DAY_PATTERN = "EEEE"
const val WEEK_DAY_PATTERN_OTHER = "EEE"
const val DAY_OF_MONTH_PATTERN = "dd 'de' MMMM"
const val DAY_MONTH_PATTERN = "dd/MM"
const val INTERVAL_DESCRIPTION_HOUR_PATTERN = "HH:mm"
const val INTERVAL_DESCRIPTION_DAY_PATTERN = "dd/MM"
const val DAY_PATTERN = "dd"
const val PATTERN_DAY_AND_DATE = "dd/MM/yyyy"
const val PATTERN_SPECIFIC_PERIOD = "EEE, dd/MM/yyyy"
const val PATTERN_WEEK_WITH_COMPLETE_DATE = "EEEE, dd/MM/yyyy"
const val DAY_PATTERN_WITHOUT_ZEROS = "d"
const val DAY_OF_MONTH_PATTERN_WITHOUT_ZEROS = "d 'de' MMMM"
const val DAY_OF_MONTH_WITH_THREE_LETTERS_PATTERN = "dd 'de' MMM"
const val PATTERN_DAY_MONTH_AND_COMPLETE_WEEK_DAY = "dd 'de' MMMM, EEEE"
const val PATTERN_DAY_MONTH_YEAR_AT_HOUR = "dd/MM/yyyy 'às' HH:mm"
const val MONTH_ABBREVIATION_PATTERN = "MMM"
const val MONTH_ABBREVIATION_WITH_YEAR_PATTERN = "MMM/yy"
const val MONTH_AND_YEAR_PATTERN = "MMMM/yyyy"
const val MONTH_PATTERN = "MMMM"
const val MONTH_OF_YEAR_PATTERN = "MMMM 'de' yyyy"
const val YEAR_PATTERN = "yyyy"
const val YEAR_MONTH_DAY_TIME_WITH_SECONDS_PATTERN = "yyyy-MM-dd'T'HH:mm:ss"
const val PATTERN_DD_MM_YY = "dd/MM/yy"
const val DATE_WITH_ABBREV_MONTH = "dd MMM. yyyy"
const val DATE_FORMAT_PATTERN = "ddMMHHmmssSSS"

val localeBR = Locale("pt", "BR")

fun Date.plusDays(days: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.DAY_OF_YEAR, days)

    return calendar.time
}

fun Date.plusWeeks(weeks: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.WEEK_OF_MONTH, weeks)

    return calendar.time
}

fun Date.plusMonths(months: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.MONTH, months)

    return calendar.time
}

fun Date.plusHours(hours: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.HOUR_OF_DAY, hours)

    return calendar.time
}

fun Date.toFirstDay(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.DAY_OF_MONTH, 1)

    return calendar.time
}

fun Date.toLastDayOfMonth(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))

    return calendar.time
}

fun Date.toLastDayOfWeek(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.DAY_OF_WEEK, calendar.getActualMaximum(Calendar.DAY_OF_WEEK))

    return calendar.time
}

fun Date.toFirstDayOfWeek(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this

    while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
        calendar.add(Calendar.DATE, -1)
    }

    return calendar.time
}

fun Date.toSimpleString(datePattern: String = DEFAULT_DATE_PATTERN): String {
    val format = SimpleDateFormat(datePattern, localeBR)
    return format.format(this)
}

fun Date.isLastDayOfMonth(): Boolean {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.DATE) == calendar.getActualMaximum(Calendar.DATE)
}

fun Date.dateYear(): Int {
    val cal = Calendar.getInstance()
    cal.time = this
    return cal.get(Calendar.YEAR)
}

fun Date.dateMonth(): Int {
    val cal = Calendar.getInstance()
    cal.time = this
    return cal.get(Calendar.MONTH)
}

fun Date.dateDay(): Int {
    val cal = Calendar.getInstance()
    cal.time = this
    return cal.get(Calendar.DAY_OF_MONTH)
}

fun Date.weekDayNumber(): Int {
    val cal = Calendar.getInstance()
    cal.time = this
    return cal.get(Calendar.DAY_OF_WEEK)
}

fun Date.formatDate(pattern: String): String {
    return SimpleDateFormat(pattern, BR_APP_LOCALE)
        .format(this)
}

fun Date.monthsDifference(date: Date): Int {
    return (this.dateYear() - date.dateYear()) * 12 + (this.dateMonth() - date.dateMonth())
}

fun Date.weeksBetween(date: Date): Long {
    val diff = date.time - this.time
    return abs(diff / (1000 * 60 * 60 * 24 * 7))
}

@Deprecated("Use Date.daysBetween instead")
fun Date.daysBetweenDates(date: Date): Long {
    val selfCalendar = Calendar.getInstance()
    selfCalendar.time = this
    val compareCalendar = Calendar.getInstance()
    compareCalendar.time = date
    val selfMilis = selfCalendar.timeInMillis + selfCalendar.get(Calendar.DST_OFFSET)
    val compareMilis = compareCalendar.timeInMillis + compareCalendar.get(Calendar.DST_OFFSET)
    return ((selfMilis - compareMilis) / (1000 * 60 * 60 * 24))
}

fun Date.clearTime(): Date {
    val cal = Calendar.getInstance()
    cal.time = this
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.time
}

fun Date.setTime(time: String): Date? {
    val date = toSimpleString()
    val hour = time.toHours()
    val minutes = time.toMinutes()
    val seconds = time.toSeconds()

    return "${date}T$hour:$minutes:$seconds".toDate(DATE_AND_HOUR_FORMAT)
}

fun Date.inRange(startDate: Date, endDate: Date): Boolean {
    val dateTime = this.clearTime().time
    return dateTime >= startDate.clearTime().time && dateTime <= endDate.clearTime().time
}

fun Date.convertMillisecondsToDate(dateMilliseconds: Long, dateFormat: String): String? {
    this.time = dateMilliseconds / 1000
    val sdf = SimpleDateFormat(dateFormat)
    sdf.timeZone = TimeZone.getTimeZone("GMT-3")
    return sdf.format(this)

}

fun Date.setupDate(year: Int, month: Int, day: Int): Date {
    val calendar = Calendar.getInstance()
    val dataFormat = SimpleDateFormat(DEFAULT_DATE_PATTERN, localeBR)
    calendar.set(year, month, day)

    this.time = dataFormat.parse(dataFormat.format(calendar.time)).time

    return this
}