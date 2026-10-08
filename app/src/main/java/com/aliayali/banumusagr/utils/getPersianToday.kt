package com.aliayali.banumusagr.utils

import java.util.Calendar

fun getPersianToday(): String {
    val calendar = Calendar.getInstance()

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val jalaliDate = gregorianToJalali(
        year,
        month,
        day
    )

    val jalaliYear = jalaliDate[0]
    val jalaliMonth = jalaliDate[1]
    val jalaliDay = jalaliDate[2]

    val monthName = when (jalaliMonth) {
        1 -> "فروردین"
        2 -> "اردیبهشت"
        3 -> "خرداد"
        4 -> "تیر"
        5 -> "مرداد"
        6 -> "شهریور"
        7 -> "مهر"
        8 -> "آبان"
        9 -> "آذر"
        10 -> "دی"
        11 -> "بهمن"
        12 -> "اسفند"
        else -> ""
    }

    val date = "$jalaliDay $monthName $jalaliYear"

    return date.toPersianDigits()
}

fun String.toPersianDigits(): String {
    return this
        .replace("0", "۰")
        .replace("1", "۱")
        .replace("2", "۲")
        .replace("3", "۳")
        .replace("4", "۴")
        .replace("5", "۵")
        .replace("6", "۶")
        .replace("7", "۷")
        .replace("8", "۸")
        .replace("9", "۹")
}

fun gregorianToJalali(
    gy: Int,
    gm: Int,
    gd: Int
): IntArray {

    val gregorianDaysInMonth = intArrayOf(
        31, 28, 31, 30, 31, 30,
        31, 31, 30, 31, 30, 31
    )

    val year = gy

    val adjustedYear = if (gm > 2) {
        year + 1
    } else {
        year
    }

    var days =
        355666 +
                (365 * year) +
                ((adjustedYear + 3) / 4) -
                ((adjustedYear + 99) / 100) +
                ((adjustedYear + 399) / 400)

    for (i in 0 until gm - 1) {
        days += gregorianDaysInMonth[i]
    }

    if (
        gm > 2 &&
        (
                year % 4 == 0 &&
                        year % 100 != 0 ||
                        year % 400 == 0
                )
    ) {
        days++
    }

    days += gd

    var jalaliYear = -1595 + 33 * (days / 12053)

    var dayOfYear = days % 12053

    jalaliYear += 4 * (dayOfYear / 1461)

    dayOfYear %= 1461

    if (dayOfYear > 365) {
        jalaliYear += (dayOfYear - 1) / 365
        dayOfYear = (dayOfYear - 1) % 365
    }

    val jalaliMonth: Int
    val jalaliDay: Int

    if (dayOfYear < 186) {
        jalaliMonth = 1 + dayOfYear / 31
        jalaliDay = 1 + dayOfYear % 31
    } else {
        jalaliMonth = 7 + (dayOfYear - 186) / 30
        jalaliDay = 1 + (dayOfYear - 186) % 30
    }

    return intArrayOf(
        jalaliYear,
        jalaliMonth,
        jalaliDay
    )
}