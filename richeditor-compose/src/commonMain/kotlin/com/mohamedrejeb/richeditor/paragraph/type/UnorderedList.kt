package com.mohamedrejeb.richeditor.paragraph.type

import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.sp
import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import com.mohamedrejeb.richeditor.model.DefaultListIndent
import com.mohamedrejeb.richeditor.model.RichSpan
import com.mohamedrejeb.richeditor.model.RichTextConfig
import com.mohamedrejeb.richeditor.paragraph.RichParagraph

internal class UnorderedList(
    initialIndent: Int = DefaultListIndent,
): ParagraphType {

    private var indent = initialIndent

    private var style: ParagraphStyle =
        getParagraphStyle()

    override fun getStyle(config: RichTextConfig): ParagraphStyle {
        if (config.listIndent != indent) {
            indent = config.listIndent
            style = getParagraphStyle()
        }

        return style
    }

    private fun getParagraphStyle() =
        ParagraphStyle(
            textIndent = TextIndent(
                firstLine = indent.sp,
                restLine = indent.sp
            )
        )

    @OptIn(ExperimentalRichTextApi::class)
    override var startRichSpan: RichSpan =
        RichSpan(
            paragraph = RichParagraph(type = this),
            text = "• ",
        )

    override fun getNextParagraphType(): ParagraphType =
        UnorderedList(
            initialIndent = indent
        )

    override fun copy(): ParagraphType =
        UnorderedList(
            initialIndent = indent
        )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UnorderedList) return false

        if (indent != other.indent) return false

        return true
    }

    override fun hashCode(): Int {
        return indent
    }
}
