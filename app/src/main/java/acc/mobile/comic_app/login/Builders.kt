package acc.mobile.comic_app.login

import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*

fun createDatePicker(): MaterialDatePicker<Long> {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    calendar.timeInMillis = MaterialDatePicker.todayInUtcMilliseconds()

    // Build constraints.
    val dateConstraints =
        CalendarConstraints.Builder()
            .setEnd(calendar.timeInMillis)
            .build()

    return MaterialDatePicker.Builder.datePicker()
        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        .setTitleText("Select birthdate")
        .setCalendarConstraints(dateConstraints)
        .build()
}