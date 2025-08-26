package com.mohamedrejeb.richeditor.paragraph.type

import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.sp
import com.mohamedrejeb.richeditor.model.RichSpan
import com.mohamedrejeb.richeditor.model.RichTextConfig
import com.mohamedrejeb.richeditor.paragraph.RichParagraph

internal class UnorderedList : ParagraphType, ConfigurableListLevel, ListLevel {

    override var level: Int = 1

    override fun getStyle(config: RichTextConfig): ParagraphStyle =
        ParagraphStyle(
            textIndent = TextIndent(
                firstLine = 38.sp,
                restLine = 38.sp
            )
        )

    override val startRichSpan: RichSpan =
        RichSpan(
            paragraph = RichParagraph(type = this),
            text = "• ",
        )

    override fun getNextParagraphType(): ParagraphType =
        UnorderedList().also { it.level = this.level }

    override fun copy(): ParagraphType =
        UnorderedList().also { it.level = this.level }
}