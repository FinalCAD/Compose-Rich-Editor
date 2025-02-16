package com.mohamedrejeb.richeditor.paragraph.type

import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import com.mohamedrejeb.richeditor.model.DefaultListIndent
import com.mohamedrejeb.richeditor.model.DefaultOrderedListStyleType
import com.mohamedrejeb.richeditor.model.RichSpan
import com.mohamedrejeb.richeditor.model.RichTextConfig
import com.mohamedrejeb.richeditor.paragraph.RichParagraph

internal class OrderedList private constructor(
    number: Int,
    initialIndent: Int = DefaultListIndent,
    startTextWidth: TextUnit = 0.sp,
    initialNestedLevel: Int = 1,
    initialStyleType: OrderedListStyleType = DefaultOrderedListStyleType,
) : ParagraphType, ConfigurableStartTextWidth, ConfigurableNestedLevel {

    constructor(
        number: Int,
        initialNestedLevel: Int = 1,
    ) : this(
        number = number,
        initialIndent = DefaultListIndent,
        initialNestedLevel = initialNestedLevel,
    )

    constructor(
        number: Int,
        config: RichTextConfig,
        startTextWidth: TextUnit = 0.sp,
        initialNestedLevel: Int = 1,
    ) : this(
        number = number,
        initialIndent = config.orderedListIndent,
        startTextWidth = startTextWidth,
        initialNestedLevel = initialNestedLevel,
        initialStyleType = config.orderedListStyleType,
    )

    var number = number
        set(value) {
            field = value
            startRichSpan = getNewStartRichSpan(startRichSpan.textRange)
        }

    override var startTextWidth: TextUnit = startTextWidth
        set(value) {
            field = value
            style = getNewParagraphStyle()
        }

    private var indent = initialIndent
        set(value) {
            field = value
            style = getNewParagraphStyle()
        }

    override var nestedLevel = initialNestedLevel
        set(value) {
            field = value
            style = getNewParagraphStyle()
        }

    private var styleType = initialStyleType
        set(value) {
            field = value
            startRichSpan = getNewStartRichSpan(startRichSpan.textRange)
        }

    private var style: ParagraphStyle =
        getNewParagraphStyle()

    override fun getStyle(config: RichTextConfig): ParagraphStyle {
        if (config.orderedListIndent != indent) {
            indent = config.orderedListIndent
        }

        if (config.orderedListStyleType != styleType) {
            styleType = config.orderedListStyleType
        }

        return style
    }

    private fun getNewParagraphStyle() =
        ParagraphStyle(
            textIndent = TextIndent(
                firstLine = ((indent * nestedLevel) - startTextWidth.value).sp,
                restLine = (indent * nestedLevel).sp
            )
        )

    override var startRichSpan: RichSpan =
        getNewStartRichSpan()

    @OptIn(ExperimentalRichTextApi::class)
    private fun getNewStartRichSpan(textRange: TextRange = TextRange(0)): RichSpan {
        val text = styleType.format(number, nestedLevel) + styleType.getSuffix(nestedLevel)

        return RichSpan(
            paragraph = RichParagraph(type = this),
            text = text,
            textRange = TextRange(
                textRange.min,
                textRange.min + text.length
            )
        )
    }

    override fun getNextParagraphType(): ParagraphType =
        OrderedList(
            number = number + 1,
            initialIndent = indent,
            startTextWidth = startTextWidth,
            initialNestedLevel = nestedLevel,
            initialStyleType = styleType,
        )

    override fun copy(): ParagraphType =
        OrderedList(
            number = number,
            initialIndent = indent,
            startTextWidth = startTextWidth,
            initialNestedLevel = nestedLevel,
            initialStyleType = styleType,
        )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OrderedList) return false

        if (number != other.number) return false
        if (indent != other.indent) return false
        if (startTextWidth != other.startTextWidth) return false
        if (nestedLevel != other.nestedLevel) return false
        if (styleType != other.styleType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = indent
        result = 31 * result + number
        result = 31 * result + indent
        result = 31 * result + startTextWidth.hashCode()
        result = 31 * result + nestedLevel
        result = 31 * result + styleType.hashCode()
        return result
    }
}
