package com.mohamedrejeb.richeditor.parser.html

import androidx.compose.ui.text.SpanStyle
import com.mohamedrejeb.richeditor.parser.utils.BoldSpanStyle
import com.mohamedrejeb.richeditor.parser.utils.H1ParagraphStyle
import com.mohamedrejeb.richeditor.parser.utils.H1SpanStyle
import com.mohamedrejeb.richeditor.parser.utils.H2ParagraphStyle
import com.mohamedrejeb.richeditor.parser.utils.H2SpanStyle
import com.mohamedrejeb.richeditor.parser.utils.H3ParagraphStyle
import com.mohamedrejeb.richeditor.parser.utils.H3SpanStyle
import com.mohamedrejeb.richeditor.parser.utils.H4ParagraphStyle
import com.mohamedrejeb.richeditor.parser.utils.H4SpanStyle
import com.mohamedrejeb.richeditor.parser.utils.H5ParagraphStyle
import com.mohamedrejeb.richeditor.parser.utils.H5SpanStyle
import com.mohamedrejeb.richeditor.parser.utils.H6ParagraphStyle
import com.mohamedrejeb.richeditor.parser.utils.H6SpanStyle
import com.mohamedrejeb.richeditor.parser.utils.ItalicSpanStyle
import com.mohamedrejeb.richeditor.parser.utils.MarkSpanStyle
import com.mohamedrejeb.richeditor.parser.utils.SmallSpanStyle
import com.mohamedrejeb.richeditor.parser.utils.StrikethroughSpanStyle
import com.mohamedrejeb.richeditor.parser.utils.SubscriptSpanStyle
import com.mohamedrejeb.richeditor.parser.utils.SuperscriptSpanStyle
import com.mohamedrejeb.richeditor.parser.utils.UnderlineSpanStyle

// Public constants and maps shared between HTML and Markdown parsers
internal const val BrElement: String = "br"
internal const val CodeSpanTagName: String = "code"
internal const val OldCodeSpanTagName: String = "code-span"

internal val htmlElementsSpanStyleEncodeMap: Map<String, SpanStyle> = mapOf(
    "b" to BoldSpanStyle,
    "strong" to BoldSpanStyle,
    "i" to ItalicSpanStyle,
    "em" to ItalicSpanStyle,
    "u" to UnderlineSpanStyle,
    "ins" to UnderlineSpanStyle,
    "s" to StrikethroughSpanStyle,
    "strike" to StrikethroughSpanStyle,
    "del" to StrikethroughSpanStyle,
    "sub" to SubscriptSpanStyle,
    "sup" to SuperscriptSpanStyle,
    "mark" to MarkSpanStyle,
    "small" to SmallSpanStyle,
    "h1" to H1SpanStyle,
    "h2" to H2SpanStyle,
    "h3" to H3SpanStyle,
    "h4" to H4SpanStyle,
    "h5" to H5SpanStyle,
    "h6" to H6SpanStyle,
)

/**
 * Encodes the HTML elements to [androidx.compose.ui.text.ParagraphStyle].
 * Some HTML elements have both an associated SpanStyle and ParagraphStyle.
 * Ensure both the [SpanStyle] (via [htmlElementsSpanStyleEncodeMap] - if applicable) and
 * [androidx.compose.ui.text.ParagraphStyle] (via [htmlElementsParagraphStyleEncodeMap] - if applicable)
 * are applied to the text.
 * @see <a href="https://www.w3schools.com/html/html_formatting.asp">HTML formatting</a>
 */
internal val htmlElementsParagraphStyleEncodeMap = mapOf(
    "h1" to H1ParagraphStyle,
    "h2" to H2ParagraphStyle,
    "h3" to H3ParagraphStyle,
    "h4" to H4ParagraphStyle,
    "h5" to H5ParagraphStyle,
    "h6" to H6ParagraphStyle,
)

/**
 * Decodes HTML elements from [SpanStyle].
 *
 * @see <a href="https://www.w3schools.com/html/html_formatting.asp">HTML formatting</a>
 */
internal val htmlElementsSpanStyleDecodeMap = mapOf(
    BoldSpanStyle to "b",
    ItalicSpanStyle to "i",
    UnderlineSpanStyle to "u",
    StrikethroughSpanStyle to "s",
    SubscriptSpanStyle to "sub",
    SuperscriptSpanStyle to "sup",
    MarkSpanStyle to "mark",
    SmallSpanStyle to "small",
    H1SpanStyle to "h1",
    H2SpanStyle to "h2",
    H3SpanStyle to "h3",
    H4SpanStyle to "h4",
    H5SpanStyle to "h5",
    H6SpanStyle to "h6",
)
