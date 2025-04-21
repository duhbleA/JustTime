package me.duhblea.justtime.complication

import android.app.PendingIntent
import android.content.Intent
import android.graphics.drawable.Icon
import android.provider.AlarmClock
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationText
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.GoalProgressComplicationData
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
import java.time.temporal.ChronoField

/**
 * Serves the complication.
 */
class MainComplicationService : SuspendingComplicationDataSourceService() {
    companion object {
        private const val TIME_FORMAT = "h:mm"
        private const val SECONDS_IN_DAY = 86400f
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

            ComplicationType.GOAL_PROGRESS -> {
                GoalProgressComplicationData.Builder(
                    value = 608f,
                    targetValue = SECONDS_IN_DAY,
                    contentDescription = ComplicationText.EMPTY,
                )
                    .setTitle(null)
                    .setText(PlainComplicationText.Builder(text = "10:08").build())
                    .setMonochromaticImage(
                        MonochromaticImage.Builder(
                            image = Icon.createWithResource(
                                this,
                                R.drawable.ic_clock
                            )
                        ).build()
                    ).build()
            }

            ComplicationType.RANGED_VALUE -> {
                RangedValueComplicationData.Builder(
                    value = 608f,
                    min = 0f,
                    max = SECONDS_IN_DAY,
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
        val progress = currentTime.get(ChronoField.SECOND_OF_DAY).toFloat()

        val currentTimeText = TimeFormatComplicationText.Builder(format = TIME_FORMAT).build()

        return when (request.complicationType) {
            ComplicationType.SHORT_TEXT -> {
                ShortTextComplicationData.Builder(
                    text = currentTimeText,
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
                    .setTapAction(createLaunchIntent())
                    .build()
            }

            ComplicationType.LONG_TEXT -> {
                LongTextComplicationData.Builder(
                    text = currentTimeText,
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
                    .setTapAction(createLaunchIntent())
                    .build()
            }

            ComplicationType.RANGED_VALUE -> {
                RangedValueComplicationData.Builder(
                    value = progress,
                    min = 0f,
                    max = SECONDS_IN_DAY,
                    contentDescription = ComplicationText.EMPTY
                )
                    .setText(currentTimeText)
                    .setTitle(null)
                    .setMonochromaticImage(
                        MonochromaticImage.Builder(
                            image = Icon.createWithResource(
                                this,
                                R.drawable.ic_clock
                            )
                        ).build()
                    )
                    .setTapAction(createLaunchIntent())
                    .build()
            }

            ComplicationType.GOAL_PROGRESS -> {
                GoalProgressComplicationData.Builder(
                    value = progress,
                    targetValue = SECONDS_IN_DAY,
                    contentDescription = ComplicationText.EMPTY,
                )
                    .setTitle(null)
                    .setText(currentTimeText)
                    .setTapAction(createLaunchIntent())
                    .setMonochromaticImage(
                        MonochromaticImage.Builder(
                            image = Icon.createWithResource(
                                this,
                                R.drawable.ic_clock
                            )
                        ).build()
                    ).build()
            }

            else -> {
                throw IllegalStateException("Unexpected complication type")
            }
        }
    }

    private fun createLaunchIntent(): PendingIntent? {
        val alarmClockIntent = Intent(AlarmClock.ACTION_SHOW_ALARMS)

        return PendingIntent.getActivity(
            this, 0, alarmClockIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}