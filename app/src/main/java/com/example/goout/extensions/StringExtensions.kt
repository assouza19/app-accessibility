@file:JvmName("StringExtensions")

package com.example.goout.extensions

import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import java.math.BigDecimal
import java.math.BigDecimal.ROUND_FLOOR
import java.math.BigDecimal.ZERO
import java.text.Normalizer
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

//Pattern extract from android library, to remove android dependency
val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile(
    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
)

val CPF_MASK = "###.###.###-##"
val CNPJ_MASK = "##.###.###/####-##"
val CREDITCARD_MASK = "#### #### #### ####"
val CEP = "#####-###"

fun String.isValidEmail(): Boolean {
    return if (this.isBlank()) {
        false
    } else {
        EMAIL_ADDRESS_PATTERN.matcher(this).matches()
    }
}

fun String.isNotValidEmail() = !isValidEmail()

fun String.emailDomain() = if (isValidEmail()) {
    "@${substringAfter("@")}"
} else {
    Log.d(null, "não é um email válido, não é possivél estrair o dominio")
    ""
}

private val weightCpf = intArrayOf(11, 10, 9, 8, 7, 6, 5, 4, 3, 2)
private val weightCnpj = intArrayOf(6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2)
const val MONTH_DAY_PATTERN = "dd 'de' MMMM"
val BR_APP_LOCALE = Locale("pt", "BR")
private const val DATE_FORMAT = "yyyy-MM-dd"
const val DATE_AND_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
const val DATE_AND_HOUR_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
const val DATE_WITH_HOUR = "dd/MM - HH:mm'h'"
private const val TIME_FORMAT = "HH:mm:ss"
private const val HOUR_PATTERN = "HH"
private const val MINUTES_PATTERN = "mm"
private const val SECONDS_PATTERN = "ss"
private const val HOUR_MINUTES_PATTERN = "H:mm"
private const val WEAK_DAY_FORMAT = "EEEE"
private const val MONTH_WITH_THREE_DIGITS_PATTERN = "MMM"
private const val MONTH_FULL_NAME_PATTERN = "MMMM"
private const val DECIMAL_PLACES_DEFAULT = 2

private fun calculate(str: String, weight: IntArray): Int {
    var sum = 0
    var digit: Int
    for (i in str.length - 1 downTo 0) {
        digit = Integer.parseInt(str.substring(i, i + 1))
        sum += digit * weight[weight.size - str.length + i]
    }

    sum = 11 - sum % 11
    return if (sum > 9) 0 else sum
}

/**
 * Valida CPF
 */
fun String.isCPF(): Boolean {
    val unformattedCpf = this.removeFormat()
    return if (unformattedCpf.length != 11 ||
        unformattedCpf.matches((unformattedCpf[0] + "{11}").toRegex())
    ) {
        false
    } else {
        val digit1 = calculate(
            unformattedCpf.substring(0, 9), weightCpf
        )
        val digit2 = calculate(
            unformattedCpf.substring(0, 9) + digit1,
            weightCpf
        )
        unformattedCpf == "${unformattedCpf.substring(0, 9)}$digit1$digit2"
    }
}

fun String.isNotCPF() = !isCPF()

/**
 * Valida CNPJ
 */
fun String.isCnpj(): Boolean {
    val unformattedCnpj = this.removeFormat()
    return if (unformattedCnpj.length != 14 ||
        unformattedCnpj.matches((unformattedCnpj[0] + "{14}").toRegex())
    ) {
        false
    } else {
        val digit1 = calculate(
            unformattedCnpj.substring(0, 12),
            weightCnpj
        )
        val digit2 = calculate(
            unformattedCnpj.substring(0, 12) + digit1,
            weightCnpj
        )
        unformattedCnpj == "${unformattedCnpj.substring(0, 12)}$digit1$digit2"
    }
}

fun String.unformatCPF() =
    this.replace("[,/.-]".toRegex(), "")

fun String.isNotCnpj() = !isCnpj()

fun String.unformatCNPJ() = this.unformatCPF()

fun String.formatDate(pattern: String = PATTERN_DAY_AND_DATE): String {
    return try {
        val date = this.toDate() ?: throw ParseException("Erro ao converter string em data", 0)
        return date.toSimpleString(pattern)
    } catch (e: ParseException) {
        ""
    }
}

fun String.dateToDateMonth(): String {
    return try {
        SimpleDateFormat(MONTH_DAY_PATTERN, BR_APP_LOCALE).format(convertToDate(this))
    } catch (e: ParseException) {
        Log.d(null, "String.dateToDateMonth - Erro ao converter string em data: " + e.message)
        ""
    }
}

fun String.toMonthNameWithThreeDigits(): String = try {
    SimpleDateFormat(MONTH_WITH_THREE_DIGITS_PATTERN, BR_APP_LOCALE)
        .format(convertToDate(this))
} catch (e: ParseException) {
    Log.d(null, e.toString())
    ""
}

fun String.toMonthCompleteName(): String = try {
    SimpleDateFormat(MONTH_FULL_NAME_PATTERN, BR_APP_LOCALE)
        .format(convertToDate(this))
} catch (e: ParseException) {
    Log.d(null, e.toString())
    ""
}

fun String.toDayOfMonth(): String = try {
    SimpleDateFormat(DAY_PATTERN, BR_APP_LOCALE)
        .format(convertToDate(this))
} catch (e: ParseException) {
    Log.d(null, e.toString())
    ""
}

fun String.toHours(): String = try {
    SimpleDateFormat(HOUR_PATTERN, BR_APP_LOCALE)
        .format(convertToDate(this, TIME_FORMAT))
} catch (e: ParseException) {
    ""
}

fun String.toMinutes(): String = try {
    SimpleDateFormat(MINUTES_PATTERN, BR_APP_LOCALE)
        .format(convertToDate(this, TIME_FORMAT))
} catch (e: ParseException) {
    ""
}

fun String.toSeconds(): String = try {
    SimpleDateFormat(SECONDS_PATTERN, BR_APP_LOCALE)
        .format(convertToDate(this, TIME_FORMAT))
} catch (e: ParseException) {
    ""
}

fun String.toHoursAndMinutes(): String = try {
    SimpleDateFormat(HOUR_MINUTES_PATTERN, BR_APP_LOCALE)
        .format(convertToDate(this, TIME_FORMAT))
} catch (e: ParseException) {
    ""
}

fun String.dateToWeakDay(): String {
    return try {
        SimpleDateFormat(WEAK_DAY_FORMAT, BR_APP_LOCALE).format(convertToDate(this))
    } catch (e: ParseException) {
        Log.d(null, "String.dateToWeakDay - Erro ao converter string em data: " + e.message)
        ""
    }
}

fun String.toDate(dateFormat: String = DATE_FORMAT): Date? {
    return try {
        convertToDate(this, dateFormat)
    } catch (e: ParseException) {
        Log.d(null, "String.toDate - Erro ao converter string em data: " + e.message)
        null
    }
}

fun String.splitSeconds(): String {
    val separator = ":"
    val str = this.split(separator)

    // remove segundos
    if (str.size > 2) {
        return """${str[0]}$separator${str[1]}"""
    }

    return this
}

private fun convertToDate(date: String, dateFormat: String = DATE_FORMAT): Date {
    val df = SimpleDateFormat(dateFormat, BR_APP_LOCALE)
    return df.parse(date)
}

fun String.formatCNPJ() =
    formatByMask(this, CNPJ_MASK)

fun String.formatCPF() =
    formatByMask(this, CPF_MASK)

fun String.formatCreditCardNumber() = formatByMask(this, CREDITCARD_MASK)

fun String.formatCEP() =
    formatByMask(this, CEP)

private fun formatByMask(text: String, mask: String): String {
    var maskAux = ""
    var i = 0
    for (m in mask.toCharArray()) {
        if (m != '#' && text.length != i) {
            maskAux += m
            continue
        }
        try {
            maskAux += text[i]
        } catch (e: Exception) {
            break
        }
        i++
    }
    return maskAux
}

/**
 * Remove formatação de string, retornando sempre apenas números
 */
fun String.removeFormat() =
    replace("[^0-9]*".toRegex(), "")

/**
 * Remove espaços em branco de uma string
 */
fun String.removeWhiteSpace() = replace("\\s".toRegex(), "")

/**
 * Verifica se a string é uma senha válida
 * A senha deve ter:
 * entre 8 e 15 caracteres
 * letras maiusculas e minusculas
 * pelo menus 1 número
 * e não pode conter caracteres especiais ou espaço
 */
fun String.isValidPassword(callback: PasswordValidationResult.() -> Unit) {
    callback(
        PasswordValidationResult(
            length in 8..15,
            contains("[a-z]".toRegex()) && contains("[A-Z]".toRegex()),
            contains("\\d".toRegex()),
            "^[a-zA-Z0-9]+\$".toRegex().matches(this)
        )
    )
}

fun String.getPhoneDDD(): String {
    val unformatted = removeFormat()
    return when {
        unformatted.length <= 9 -> {
            Log.d(null, "o telefone informado não tem DDD - numero: $this")
            ""
        }
        unformatted.length > 11 -> {
            Log.d(null, "o telefone informado inválido - numero: $this")
            ""
        }
        else -> unformatted.substring(0, 2)
    }
}

fun String.getPhoneNumber(): String {
    val unformatted = removeFormat()
    return when {
        unformatted.length < 8 -> {
            Log.d(null, "o telefone informado é inválido - numero: $this")
            ""
        }
        unformatted.length in 8..9 -> unformatted
        unformatted.length > 11 -> {
            Log.d(null, "o telefone informado inválido - numero: $this")
            ""
        }
        else -> //com ddd
            unformatted.substring(2)
    }
}

fun String.isInvalidCep(): Boolean {
    return this.isEmpty() || this.removeCharacters().length < 8
}

fun String.isInvalidPhone(): Boolean {
    return this.isEmpty() || this.removeCharacters().length < 10
}

fun String.isInvalidCellPhone(): Boolean {
    return this.isEmpty() || this.removeCharacters().length < 11
}

fun String.formatPhoneNumber(isWithAsterisk: Boolean): String? {
    return formatPhoneNumberGeneric(this, isWithAsterisk, 5)
}

fun String.formatCellNumber(isWithAsterisk: Boolean): String? {
    return formatPhoneNumberGeneric(this, isWithAsterisk, 4)
}

private fun formatPhoneNumberGeneric(
    number: String,
    isWithAsterisk: Boolean,
    qtdAsterisk: Int
): String? {
    var phoneNumber: String
    var mask = StringBuilder("($1) $2-$3")
    phoneNumber = number.replace("(\\D)".toRegex(), "")
    if (isWithAsterisk) {
        val qtdAsterisk = phoneNumber.length - qtdAsterisk
        for (i in 0 until qtdAsterisk) {
            if (i == 0) {
                mask = StringBuilder("(")
            }
            if (i == 2) {
                mask.append(") ")
            }
            mask.append("*")
        }
        mask.append("-$3")
    }
    val pattern = Pattern.compile("(\\d{2})(\\d{4,5})(\\d{4})")
    if (!phoneNumber.isEmpty()) {
        val matcher = pattern.matcher(phoneNumber)
        if (matcher.matches())
            phoneNumber = matcher.replaceAll(mask.toString())
    }
    return phoneNumber
}

fun String.removeCharacters(): String {

    val txt = this.replace("[^0-9]".toRegex(), "")

    return txt.replace("\\-".toRegex(), "")
}

fun String.containsAccentedOrNot(text: CharSequence?): Boolean {

    val normalizedItem = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
        .matcher(Normalizer.normalize(this, Normalizer.Form.NFD))
        .replaceAll("")

    return normalizedItem.toLowerCase().contains(text.toString().toLowerCase())
            || this.toLowerCase().contains(text.toString().toLowerCase())
}

class PasswordValidationResult(
    val hasCorrectLength: Boolean, val hasCaps: Boolean,
    val hasNumber: Boolean, val hasOnlyValidChars: Boolean
) {

    val isValid = hasCorrectLength && hasCaps && hasNumber && hasOnlyValidChars
}

/**
 * Recupera as inicias do nome a partir do email, separando por ".", "_", "," e "-"
 * @param limit  valor, não negativo, que indica a quantidade de iniciais a serem retornadas.
 * O Valor Zero, que é o padrão, indica que não existe limite
 *
 * @throws IllegalArgumentException
 */
fun String.getNameInitialsFromEmail(limit: Int = 0): String {
    val builder = StringBuilder()
    substringBefore('@')
        .split("[,._-]".toRegex(), limit)
        .filter { it.isNotBlank() }
        .forEach { builder.append(it.trim().first().toUpperCase()) }

    return builder.toString()
}

fun String.addSpaceBetweenCharacters(): String {
    return this.replace(Regex("(.)"), "$1 ")
}

fun String.replaceSpecialSpaces() =
    this.replace("\uFEFF", "")
        .replace("\u00A0", " ")

fun String.hasSpecialChar(): Boolean =
    !Pattern.matches("^[A-Za-záàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ\\s]+\$", this.removeWhiteSpace())

fun String.toCamelCase(): String {
    val normalizer = Normalizer.normalize(this, Normalizer.Form.NFD)
    normalizer.replace("[^{ASCII}]", "")
    val regex = Regex("[^0-9a-zA-Z]+")
    val bound = Pattern.compile("\\b(\\w)")
    val stringBuffer = StringBuffer(normalizer.length)
    val matcher = bound.matcher(normalizer)
    while (matcher.find()) {
        matcher.appendReplacement(stringBuffer, matcher.group().toUpperCase())
    }
    matcher.appendTail(stringBuffer)

    return regex.replace(stringBuffer, "")
}

fun String.addWhiteSpaceWhenEmpty(): String {
    return if (this.isEmpty()) this.plus(" ") else this
}

fun String.toBasicDateFormat(): String? {
    val locale = Locale.getDefault()
    val calendar = Calendar.getInstance(locale)
    calendar.set(Calendar.HOUR_OF_DAY, this.substringBefore(":").toInt())
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    return SimpleDateFormat(DATE_AND_HOUR_FORMAT, locale).format(calendar.timeInMillis)
}

fun String.toHourValue(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this.toDate(HOUR_MINUTES_PATTERN)
    return calendar.get(Calendar.HOUR_OF_DAY)
}

@SuppressWarnings("deprecation")
fun String.removeHtml() =
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
    } else {
        Html.fromHtml(this).toString()
    }

@SuppressWarnings("deprecation")
fun String.toHtml(): Spanned =
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }

fun String.getHtmlLinkInnerContent() = split("</")
    .filter { it.contains("<a") }
    .map { it.substringAfterLast(">") }

fun String.parseToBigDecimal(): BigDecimal {
    val cleanString = this.filter { it.isDigit() }
    val value: BigDecimal = when {
        cleanString.isEmpty() -> BigDecimal(0.0)
        else -> BigDecimal(cleanString)
    }

    return value
        .setScale(
            2, ROUND_FLOOR
        ).divide(
            BigDecimal(100), ROUND_FLOOR
        )
}

fun String.toCurrencyBigDecimal(
    decimalPlaces: Int = DECIMAL_PLACES_DEFAULT,
    round: Int = ROUND_FLOOR
): BigDecimal {
    return try {
        val unformatted = this.removeFormat()
        BigDecimal(unformatted)
            .setScale(decimalPlaces, round)
            .divide(BigDecimal(100), round)
    } catch (e: NumberFormatException) {
        ZERO
    }
}

fun String.setSpannableColor(target: String, color: Int): SpannableString {
    val spannable = SpannableString(this)
    spannable.setSpan(
        ForegroundColorSpan(color), this.indexOf(target),
        this.indexOf(target) + target.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return spannable
}