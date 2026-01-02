package fi.antero.aurorastars.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import java.util.Calendar

object TimeTicker {

    val minuteFlow = flow {
        while (true) {
            emit(Unit)


            val calendar = Calendar.getInstance()
            val seconds = calendar.get(Calendar.SECOND)
            val millis = calendar.get(Calendar.MILLISECOND)

            val msToNextMinute = ((60 - seconds) * 1000L) - millis

            delay(msToNextMinute.coerceAtLeast(1L))
        }
    }
}