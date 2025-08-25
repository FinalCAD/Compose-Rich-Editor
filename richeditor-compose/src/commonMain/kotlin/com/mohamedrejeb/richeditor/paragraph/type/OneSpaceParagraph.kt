package com.mohamedrejeb.richeditor.paragraph.type

import androidx.compose.ui.text.ParagraphStyle
import com.mohamedrejeb.richeditor.model.RichSpan
import com.mohamedrejeb.richeditor.model.RichTextConfig
import com.mohamedrejeb.richeditor.paragraph.RichParagraph

internal class OneSpaceParagraph : ParagraphType {
    override fun getStyle(config: RichTextConfig): ParagraphStyle =
        ParagraphStyle()

    override val startRichSpan: RichSpan =
        RichSpan(
            paragraph = RichParagraph(type = this),
            text = " "
        )

    override fun getNextParagraphType(): ParagraphType =
        OneSpaceParagraph()

    override fun copy(): ParagraphType =
        OneSpaceParagraph()
}