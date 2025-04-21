package me.duhblea.justtime.complication

import android.app.PendingIntent
import android.content.Intent
import android.graphics.drawable.Icon
import android.provider.AlarmClock
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationText
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.LongTextComplicationData
import androidx.wear.watchface.complications.data.MonochromaticImage
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.RangedValueComplicationData
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.data.TimeFormatComplicationText
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService
import me.duhblea.justtime.R
import java.time.LocalDateTime

/**
 * Skeleton for complication data source that returns short text.
 */
class MainComplicationService : SuspendingComplicationDataSourceService() {
    private val timeFormat = "h:mm"

    private fun launchIntent(): PendingIntent? {

        val alarmClockIntent = Intent(AlarmClock.ACTION_SHOW_ALARMS)

        return PendingIntent.getActivity(
            this, 0, alarmClockIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    override fun getPreviewData(type: ComplicationType): ComplicationData? {
        return when (type) {
            ComplicationType.SHORT_TEXT -> {
                ShortTextComplicationData.Builder(
                    text = PlainComplicationText.Builder(text = "10:08").build(),
                    contentDescription = ComplicationText.EMPTY
                )
                    .setMonochromaticImage(
                        MonochromaticImage.Builder(
                            image = Icon.createWithResource(
                                this,
                                R.drawable.ic_clock
                            )
                        ).build()
                    )
                    .setTitle(null)
                    .build()
            }

            ComplicationType.LONG_TEXT -> {
                LongTextComplicationData.Builder(
                    text = PlainComplicationText.Builder(text = "10:08").build(),
                    contentDescription = ComplicationText.EMPTY
                )
                    .setMonochromaticImage(
                        MonochromaticImage.Builder(
                            image = Icon.createWithResource(
                                this,
                                R.drawable.ic_clock
                            )
                        ).build()
                    )
                    .setTitle(null)
                    .build()
            }

            ComplicationType.RANGED_VALUE -> {
                RangedValueComplicationData.Builder(
                    value = 608f,
                    min = 0f,
                    max = 1440f,
                    contentDescription = ComplicationText.EMPTY
                )
                    .setText(PlainComplicationText.Builder(text = "10:08").build())
                    .setTitle(null)
                    .setMonochromaticImage(
                        MonochromaticImage.Builder(
                            image = Icon.createWithResource(
                                this,
                                R.drawable.ic_clock
                            )
                        ).build()
                    )
                    .build()
            }

            else -> {
                null
            }
        }
    }

    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData {

        val currentTime = LocalDateTime.now()
        val hour = currentTime.hour
        val min = currentTime.minute

        val progress = hour * 60 + min.toFloat()

        val text = TimeFormatComplicationText.Builder(format = timeFormat).build()

        return when (request.complicationType) {
            ComplicationType.SHORT_TEXT -> {
                ShortTextComplicationData.Builder(
                    text = text,
                    contentDescription = ComplicationText.EMPTY
                )
                    .setTitle(null)
                    .setMonochromaticImage(
                        MonochromaticImage.Builder(
                            image = Icon.createWithResource(
                                this,
                                R.drawable.ic_clock
                            )
                        ).build()
                    )
                    .setTapAction(launchIntent())
                    .build()
            }

            ComplicationType.LONG_TEXT -> {
                LongTextComplicationData.Builder(
                    text = text,
                    contentDescription = ComplicationText.EMPTY
                )
                    .setTitle(null)
                    .setMonochromaticImage(
                        MonochromaticImage.Builder(
                            image = Icon.createWithResource(
                                this,
                                R.drawable.ic_clock
                            )
                        ).build()
                    )
                    .setTapAction(launchIntent())
                    .build()
            }

            ComplicationType.RANGED_VALUE -> {
                RangedValueComplicationData.Builder(
                    value = progress,
                    min = 0f,
                    max = 1440f,
                    contentDescription = ComplicationText.EMPTY
                )
                    .setText(text)
                    .setTitle(null)
                    .setMonochromaticImage(
                        MonochromaticImage.Builder(
                            image = Icon.createWithResource(
                                this,
                                R.drawable.ic_clock
                            )
                        ).build()
                    )
                    .setTapAction(launchIntent())
                    .build()
            }

            else -> {
                throw IllegalStateException("Unexpected complication type")
            }
        }
    }
}