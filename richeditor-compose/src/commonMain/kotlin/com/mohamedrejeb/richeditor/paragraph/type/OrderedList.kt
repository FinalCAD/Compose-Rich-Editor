package com.mohamedrejeb.richeditor.paragraph.type

import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.sp
import com.mohamedrejeb.richeditor.model.RichSpan
import com.mohamedrejeb.richeditor.model.RichTextConfig
import com.mohamedrejeb.richeditor.paragraph.RichParagraph

internal class OrderedList(
    number: Int,
    startTextSpanStyle: SpanStyle = SpanStyle(),
) : ParagraphType, ConfigurableListLevel {

    var number = number
        set(value) {
            field = value
            startRichSpan = getNewStartRichSpan()
        }

    override var level: Int = 1

    var startTextSpanStyle = startTextSpanStyle
        set(value) {
            field = value
            // style depends on config now; no cached style field
        }

    override fun getStyle(config: RichTextConfig): ParagraphStyle =
        ParagraphStyle(
            textIndent = TextIndent(
                firstLine = 38.sp,
                restLine = 38.sp
            )
        )

    override var startRichSpan: RichSpan =
        getNewStartRichSpan()

    private fun getNewStartRichSpan() =
        RichSpan(
            paragraph = RichParagraph(type = this),
            text = "$number. ",
            spanStyle = startTextSpanStyle
        )

    override fun getNextParagraphType(): ParagraphType =
        OrderedList(
            number = number + 1,
            startTextSpanStyle = startTextSpanStyle,
        ).also { it.level = this.level }

    override fun copy(): ParagraphType =
        OrderedList(
            number = number,
            startTextSpanStyle = startTextSpanStyle,
        ).also { it.level = this.level }
}