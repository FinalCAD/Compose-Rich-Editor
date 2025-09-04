package com.mohamedrejeb.richeditor.parser.html

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import androidx.compose.ui.util.fastForEachReversed
import com.mohamedrejeb.ksoup.entities.KsoupEntities
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlHandler
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlParser
import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import com.mohamedrejeb.richeditor.model.HeadingStyle
import com.mohamedrejeb.richeditor.model.RichSpan
import com.mohamedrejeb.richeditor.model.RichSpanStyle
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.paragraph.RichParagraph
import com.mohamedrejeb.richeditor.paragraph.type.ConfigurableListLevel
import com.mohamedrejeb.richeditor.paragraph.type.DefaultParagraph
import com.mohamedrejeb.richeditor.paragraph.type.OrderedList
import com.mohamedrejeb.richeditor.paragraph.type.ParagraphType
import com.mohamedrejeb.richeditor.paragraph.type.UnorderedList
import com.mohamedrejeb.richeditor.parser.RichTextStateParser
import com.mohamedrejeb.richeditor.utils.customMerge
import com.mohamedrejeb.richeditor.utils.diff

internal object RichTextStateHtmlParser : RichTextStateParser<String> {

    @OptIn(ExperimentalRichTextApi::class)
    override fun encode(input: String): RichTextState {
        val openedTags = mutableListOf<Pair<String, Map<String, String>>>()
        val stringBuilder = StringBuilder()
        val richParagraphList = mutableListOf(RichParagraph())
        val lineBreakParagraphIndexSet = mutableSetOf<Int>()
        val toKeepEmptyParagraphIndexSet = mutableSetOf<Int>()
        var currentRichSpan: RichSpan? = null
        var currentListLevel = 0

        val handler = KsoupHtmlHandler
            .Builder()
            .onText {
                // In html text inside ul/ol tags is skipped
                val lastOpenedTag = openedTags.lastOrNull()?.first
                if (lastOpenedTag == "ul" || lastOpenedTag == "ol") return@onText

                if (lastOpenedTag in skippedHtmlElements) return@onText

                val addedText = KsoupEntities.decodeHtml(
                    removeHtmlTextExtraSpaces(
                        input = it,
                        trimStart = stringBuilder.lastOrNull() == null || stringBuilder.lastOrNull()
                            ?.isWhitespace() == true || stringBuilder.lastOrNull() == '\n',
                    )
                )

                if (addedText.isEmpty()) return@onText

                stringBuilder.append(addedText)

                val currentRichParagraph = richParagraphList.last()
                val safeCurrentRichSpan = currentRichSpan ?: RichSpan(paragraph = currentRichParagraph)

                if (safeCurrentRichSpan.children.isEmpty()) {
                    safeCurrentRichSpan.text += addedText
                } else {
                    val newRichSpan = RichSpan(paragraph = currentRichParagraph)
                    newRichSpan.text = addedText
                    safeCurrentRichSpan.children.add(newRichSpan)
                }

                if (currentRichSpan == null) {
                    currentRichSpan = safeCurrentRichSpan
                    currentRichParagraph.children.add(safeCurrentRichSpan)
                }
            }
            .onOpenTag { name, attributes, _ ->
                val lastOpenedTag = openedTags.lastOrNull()?.first

                openedTags.add(name to attributes)

                if (name in skippedHtmlElements) {
                    return@onOpenTag
                }

                if (name == "ul" || name == "ol") {
                    // Todo: Apply ul/ol styling if exists
                    currentListLevel = currentListLevel + 1
                    return@onOpenTag
                }

                if (name == "body") {
                    stringBuilder.clear()
                    richParagraphList.clear()
                    richParagraphList.add(RichParagraph())
                    currentRichSpan = null
                }

                val cssStyleMap = attributes["style"]?.let { CssEncoder.parseCssStyle(it) } ?: emptyMap()
                val cssSpanStyle = CssEncoder.parseCssStyleMapToSpanStyle(cssStyleMap)

                val tagSpanStyle = htmlElementsSpanStyleEncodeMap[name]
                val tagParagraphStyle = htmlElementsParagraphStyleEncodeMap[name]

                val currentRichParagraph = richParagraphList.lastOrNull()
                val isCurrentRichParagraphBlank = currentRichParagraph?.isBlank() == true
                val isCurrentTagBlockElement = name in htmlBlockElements
                val isLastOpenedTagBlockElement = lastOpenedTag in htmlBlockElements

                // For <li> tags inside <ul> or <ol> tags
                if (
                    lastOpenedTag != null &&
                    isCurrentTagBlockElement &&
                    isLastOpenedTagBlockElement &&
                    name == "li" &&
                    currentRichParagraph != null &&
                    currentRichParagraph.type is DefaultParagraph &&
                    isCurrentRichParagraphBlank
                ) {
                    val paragraphType =
                        encodeHtmlElementToRichParagraphType(lastOpenedTag, currentListLevel)
                    currentRichParagraph.type = paragraphType

                    val cssParagraphStyle = CssEncoder.parseCssStyleMapToParagraphStyle(cssStyleMap,attributes)
                    currentRichParagraph.paragraphStyle =
                        currentRichParagraph.paragraphStyle.merge(cssParagraphStyle)
                }

                if (isCurrentTagBlockElement) {
                    val newRichParagraph =
                        if (isCurrentRichParagraphBlank)
                            currentRichParagraph!!
                        else
                            RichParagraph()

                    var paragraphType: ParagraphType = DefaultParagraph()
                    if (name == "li" && lastOpenedTag != null) {
                        paragraphType =
                            encodeHtmlElementToRichParagraphType(lastOpenedTag, currentListLevel)
                    }
                    val cssParagraphStyle = CssEncoder.parseCssStyleMapToParagraphStyle(cssStyleMap, attributes)

                    newRichParagraph.paragraphStyle =
                        newRichParagraph.paragraphStyle.merge(cssParagraphStyle)
                    newRichParagraph.type = paragraphType

                    // Apply paragraph style (if applicable)
                    tagParagraphStyle?.let {
                        newRichParagraph.paragraphStyle = newRichParagraph.paragraphStyle.merge(it)
                    }

                    if (!isCurrentRichParagraphBlank) {
                        stringBuilder.append(' ')

                        richParagraphList.add(newRichParagraph)
                    }

                    val newRichSpan = RichSpan(paragraph = newRichParagraph)
                    newRichSpan.spanStyle = cssSpanStyle.customMerge(tagSpanStyle)

                    if (newRichSpan.spanStyle != SpanStyle()) {
                        currentRichSpan = newRichSpan
                        newRichParagraph.children.add(newRichSpan)
                    } else {
                        currentRichSpan = null
                    }
                } else if (name != BrElement) {
                    val richSpanStyle = encodeHtmlElementToRichSpanStyle(name, attributes)

                    val currentRichParagraph = richParagraphList.last()
                    val newRichSpan = RichSpan(paragraph = currentRichParagraph)
                    newRichSpan.spanStyle = cssSpanStyle.customMerge(tagSpanStyle)
                    newRichSpan.richSpanStyle = richSpanStyle

                    if (currentRichSpan != null) {
                        newRichSpan.parent = currentRichSpan
                        currentRichSpan?.children?.add(newRichSpan)
                    } else {
                        currentRichParagraph.children.add(newRichSpan)
                    }
                    currentRichSpan = newRichSpan
                } else {
                    // name == "br"
                    stringBuilder.append(' ')

                    val newParagraph =
                        if (richParagraphList.isEmpty())
                            RichParagraph()
                        else
                            RichParagraph(paragraphStyle = richParagraphList.last().paragraphStyle)

                    richParagraphList.add(newParagraph)

                    if (richParagraphList.lastIndex > 0)
                        lineBreakParagraphIndexSet.add(richParagraphList.lastIndex - 1)

                    lineBreakParagraphIndexSet.add(richParagraphList.lastIndex)

                    // Keep the same style when having a line break in the middle of a paragraph,
                    // Ex: <h1>Hello<br>World!</h1>
                    if (isLastOpenedTagBlockElement && !isCurrentRichParagraphBlank)
                        currentRichSpan?.let { richSpan ->
                            val newRichSpan = richSpan.copy(
                                text = "",
                                textRange = TextRange.Zero,
                                paragraph = newParagraph,
                                children = mutableListOf(),
                            )

                            newParagraph.children.add(newRichSpan)

                            currentRichSpan = newRichSpan
                        }
                    else
                        currentRichSpan = null
                }
            }
            .onCloseTag { name, _ ->
                openedTags.removeLastOrNull()

                val isCurrentRichParagraphBlank = richParagraphList.lastOrNull()?.isBlank() == true
                val isCurrentTagBlockElement = name in htmlBlockElements && name != "li"

                if (isCurrentTagBlockElement && !isCurrentRichParagraphBlank) {
                    stringBuilder.append(' ')

                    //TODO - This was causing the paragraph style from heading tags to be applied to
                    // subsequent paragraphs. Verify that this isn't crucial (all the tests still pass)
                    val newParagraph = RichParagraph()

                    richParagraphList.add(newParagraph)

                    toKeepEmptyParagraphIndexSet.add(richParagraphList.lastIndex)

                    currentRichSpan = null
                }

                if (name == "ul" || name == "ol") {
                    currentListLevel = (currentListLevel - 1).coerceAtLeast(0)
                    return@onCloseTag
                }

                if (name in skippedHtmlElements)
                    return@onCloseTag

                if (name != BrElement)
                    currentRichSpan = currentRichSpan?.parent
            }
            .build()

        val parser = KsoupHtmlParser(
            handler = handler
        )

        parser.write(input)
        parser.end()

        for (i in richParagraphList.lastIndex downTo 0) {
            // Keep empty paragraphs if they are line breaks <br> or by block html elements
            if (i in lineBreakParagraphIndexSet || (i != richParagraphList.lastIndex && i in toKeepEmptyParagraphIndexSet))
                continue

            // Remove empty paragraphs
            if (richParagraphList[i].isBlank())
                richParagraphList.removeAt(i)
        }

        richParagraphList.forEach { richParagraph ->
            richParagraph.removeEmptyChildren()
        }

        return RichTextState(
            initialRichParagraphList = richParagraphList,
        )
    }

    override fun decode(richTextState: RichTextState): String {
        val builder = StringBuilder()

        val openedListTagNames = mutableListOf<String>()
        var lastParagraphGroupTagName: String? = null
        var lastParagraphGroupLevel = 0
        var isLastParagraphEmpty = false

        var currentListLevel = 0

        richTextState.richParagraphList.fastForEachIndexed { index, richParagraph ->
            val richParagraphType = richParagraph.type
            val isParagraphEmpty = richParagraph.isEmpty()
            val paragraphGroupTagName = decodeHtmlElementFromRichParagraph(richParagraph)

            val paragraphLevel =
                if (richParagraphType is ConfigurableListLevel)
                    richParagraphType.level
                else
                    0

            val isParagraphList = paragraphGroupTagName in listOf("ol", "ul")
            val isLastParagraphList = lastParagraphGroupTagName in listOf("ol", "ul")

            fun isCloseParagraphGroup(): Boolean {
                if (!isLastParagraphList)
                    return false

                if (paragraphLevel > lastParagraphGroupLevel)
                    return false

                if (
                    lastParagraphGroupTagName == paragraphGroupTagName &&
                    paragraphLevel == lastParagraphGroupLevel
                )
                    return false

                return true
            }

            fun isCloseAllOpenedTags(): Boolean {
                if (isParagraphList)
                    return false

                if (!isLastParagraphList)
                    return false

                return true
            }

            fun isOpenParagraphGroup(): Boolean {
                if (!isParagraphList)
                    return false

                if (
                    isLastParagraphList &&
                    paragraphGroupTagName == openedListTagNames.lastOrNull() &&
                    paragraphLevel < lastParagraphGroupLevel
                )
                    return false

                if (
                    isLastParagraphList &&
                    paragraphLevel == lastParagraphGroupLevel &&
                    paragraphGroupTagName == lastParagraphGroupTagName
                )
                    return false

                return true
            }

            if (isCloseAllOpenedTags()) {
                openedListTagNames.fastForEachReversed {
                    builder.append("</$it>")
                }
                openedListTagNames.clear()
            } else if (isCloseParagraphGroup()) {
                // Close last paragraph group tag
                builder.append("</$lastParagraphGroupTagName>")
                openedListTagNames.removeLastOrNull()

                // We can move from nested level: 3 to nested level: 1,
                // for this case we need to close more than one tag
                if (
                    isLastParagraphList &&
                    paragraphLevel < lastParagraphGroupLevel
                ) {
                    repeat(lastParagraphGroupLevel - paragraphLevel) {
                        openedListTagNames.removeLastOrNull()?.let {
                            builder.append("</$it>")
                        }
                    }
                }
            }

            if (isOpenParagraphGroup()) {
                builder.append("<$paragraphGroupTagName>")
                openedListTagNames.add(paragraphGroupTagName)
            }

            currentListLevel = paragraphLevel

            fun isLineBreak(): Boolean {
                if (!isParagraphEmpty)
                    return false

                if (isParagraphList && lastParagraphGroupTagName != paragraphGroupTagName)
                    return false

                return true
            }

            // Add line break if the paragraph is empty
            if (isLineBreak()) {
                val skipAddingBr =
                    isLastParagraphEmpty && richParagraph.isEmpty() && index == richTextState.richParagraphList.lastIndex

                if (!skipAddingBr)
                    builder.append("<$BrElement>")
            } else {
                // Create paragraph tag name
                val paragraphTagName =
                    if (paragraphGroupTagName == "ol" || paragraphGroupTagName == "ul") "li"
                    else paragraphGroupTagName

                // Create paragraph css
                val paragraphCssMap =
                    /*
                     Heading paragraph styles inherit custom ParagraphStyle from the Typography class.
                     This will allow us to remove any inherited ParagraphStyle properties, but keep the user added ones.
                     <h1> to <h6> tags will allow the browser to apply the default heading styles.
                     If the paragraphTagName isn't a h1-h6 tag, it will revert to the old behavior of applying whatever paragraphstyle is present.
                     */
                    if (paragraphTagName in HeadingStyle.headingTags) {
                        val headingType =
                            HeadingStyle.fromParagraphStyle(richParagraph.paragraphStyle)
                        val baseParagraphStyle = headingType.getParagraphStyle()
                        val diffParagraphStyle =
                            richParagraph.paragraphStyle.diff(baseParagraphStyle)
                        CssDecoder.decodeParagraphStyleToCssStyleMap(diffParagraphStyle)
                    } else {
                        CssDecoder.decodeParagraphStyleToCssStyleMap(richParagraph.paragraphStyle)
                    }

                val paragraphCss = CssDecoder.decodeCssStyleMap(paragraphCssMap)

                // Append paragraph opening tag
                builder.append("<$paragraphTagName")
                if (paragraphCss.isNotBlank()) builder.append(" style=\"$paragraphCss\"")
                builder.append(">")

                // Append paragraph children
                richParagraph.children.fastForEach { richSpan ->
                    builder.append(
                        decodeRichSpanToHtml(
                            richSpan,
                            headingType = HeadingStyle.fromRichSpan(richSpan)
                        )
                    )
                }

                // Append paragraph closing tag
                builder.append("</$paragraphTagName>")
            }

            // Save last paragraph group tag name
            lastParagraphGroupTagName = paragraphGroupTagName
            lastParagraphGroupLevel = paragraphLevel

            isLastParagraphEmpty = isParagraphEmpty
        }

        // Close the remaining list tags
        openedListTagNames.fastForEachReversed {
            builder.append("</$it>")
        }
        openedListTagNames.clear()

        return builder.toString()
    }

    @OptIn(ExperimentalRichTextApi::class)
    private fun decodeRichSpanToHtml(
        richSpan: RichSpan,
        parentFormattingTags: List<String> = emptyList(),
        headingType: HeadingStyle = HeadingStyle.Normal,
    ): String {
        val stringBuilder = StringBuilder()

        // Check if span is empty
        if (richSpan.isEmpty()) return ""

        // Get HTML element and attributes
        val spanHtml = decodeHtmlElementFromRichSpanStyle(richSpan.richSpanStyle)
        val tagName = spanHtml.first
        val tagAttributes = spanHtml.second

        // Convert attributes map to HTML string
        val tagAttributesStringBuilder = StringBuilder()
        tagAttributes.forEach { (key, value) ->
            tagAttributesStringBuilder.append(" $key=\"$value\"")
        }

        // Convert span style to CSS string
        val htmlStyleFormat =
            if (headingType == HeadingStyle.Normal)
                CssDecoder.decodeSpanStyleToHtmlStylingFormat(richSpan.spanStyle)
            else
                CssDecoder.decodeSpanStyleToHtmlStylingFormat(richSpan.spanStyle.diff(headingType.getSpanStyle()))
        val spanCss = CssDecoder.decodeCssStyleMap(htmlStyleFormat.cssStyleMap)
        val htmlTags = htmlStyleFormat.htmlTags.filter { it !in parentFormattingTags }

        // Handle special tags like links, images, code
        if (tagName == "a" || tagName == CodeSpanTagName || tagName == "img") {
            // Add the special tag wrapper
            stringBuilder.append("<$tagName$tagAttributesStringBuilder")
            if (tagName != "img" && spanCss.isNotEmpty()) {
                stringBuilder.append(" style=\"$spanCss\"")
            }
            stringBuilder.append(">")

            // For self-closing tags like img, don't add span content
            if (tagName == "img") {
                stringBuilder.append("</$tagName>")
                return stringBuilder.toString()
            }

            // For links and code, always add span inside
            stringBuilder.append("<span>")
            stringBuilder.append(KsoupEntities.encodeHtml(richSpan.text))

            // Append children
            richSpan.children.fastForEach { child ->
                stringBuilder.append(
                    decodeRichSpanToHtml(
                        richSpan = child,
                        parentFormattingTags = parentFormattingTags + htmlTags,
                    )
                )
            }

            stringBuilder.append("</span>")
            stringBuilder.append("</$tagName>")
        } else {
            // For regular content, always wrap in span with formatting tags
            // Add formatting tags first (strong, em, etc.)
            htmlTags.forEach {
                stringBuilder.append("<$it>")
            }

            // Always add span wrapper for text content
            stringBuilder.append("<span")
            if (spanCss.isNotEmpty()) {
                stringBuilder.append(" style=\"$spanCss\"")
            }
            stringBuilder.append(">")

            // Append text
            stringBuilder.append(KsoupEntities.encodeHtml(richSpan.text))

            // Append children
            richSpan.children.fastForEach { child ->
                stringBuilder.append(
                    decodeRichSpanToHtml(
                        richSpan = child,
                        parentFormattingTags = parentFormattingTags + htmlTags,
                    )
                )
            }

            stringBuilder.append("</span>")

            // Close formatting tags in reverse order
            htmlTags.reversed().forEach {
                stringBuilder.append("</$it>")
            }
        }

        return stringBuilder.toString()
    }

    /**
     * Encodes HTML elements to [RichSpanStyle].
     */
    @OptIn(ExperimentalRichTextApi::class)
    private fun encodeHtmlElementToRichSpanStyle(
        tagName: String,
        attributes: Map<String, String>,
    ): RichSpanStyle =
        when (tagName) {
            "a" ->
                RichSpanStyle.Link(url = attributes["href"].orEmpty())

            CodeSpanTagName, OldCodeSpanTagName ->
                RichSpanStyle.Code()

            "img" ->
                RichSpanStyle.Image(
                    model = attributes["src"].orEmpty(),
                    width = (attributes["width"]?.toIntOrNull() ?: 0).sp,
                    height = (attributes["height"]?.toIntOrNull() ?: 0).sp,
                    contentDescription = attributes["alt"] ?: ""
                )

            else ->
                RichSpanStyle.Default
        }

    /**
     * Decodes HTML elements from [RichSpanStyle].
     */
    @OptIn(ExperimentalRichTextApi::class)
    private fun decodeHtmlElementFromRichSpanStyle(
        richSpanStyle: RichSpanStyle,
    ): Pair<String, Map<String, String>> =
        when (richSpanStyle) {
            is RichSpanStyle.Link ->
                "a" to mapOf(
                    "href" to richSpanStyle.url,
                    "target" to "_blank"
                )

            is RichSpanStyle.Code ->
                CodeSpanTagName to emptyMap()

            is RichSpanStyle.Image ->
                if (richSpanStyle.model is String)
                    "img" to mapOf(
                        "src" to richSpanStyle.model,
                        "width" to richSpanStyle.width.value.toString(),
                        "height" to richSpanStyle.height.value.toString(),
                    )
                else
                    "span" to emptyMap()

            else ->
                "span" to emptyMap()
        }

    /**
     * Encodes HTML elements to [ParagraphType].
     */
    private fun encodeHtmlElementToRichParagraphType(
        tagName: String,
        listLevel: Int,
    ): ParagraphType {
        return when (tagName) {
            "ul" -> UnorderedList().apply { level = listLevel }
            "ol" -> OrderedList(number = 1).apply { level = listLevel }
            else -> DefaultParagraph()
        }
    }

    /**
     * Decodes HTML elements from [RichParagraph].
     */
    private fun decodeHtmlElementFromRichParagraph(
        richParagraph: RichParagraph,
    ): String {
        val paragraphType = richParagraph.type
        return when (paragraphType) {
            is UnorderedList -> "ul"
            is OrderedList -> "ol"
            else -> richParagraph.getHeadingStyle().htmlTag ?: "p"
        }
    }

}


