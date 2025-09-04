/*
package com.mohamedrejeb.richeditor.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import com.mohamedrejeb.richeditor.paragraph.RichParagraph
import com.mohamedrejeb.richeditor.paragraph.type.OrderedList
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@OptIn(ExperimentalRichTextApi::class)
class RichParagraphTest {

    @Test
    fun testSliceWithEmptyRichSpans() {
        val paragraph = RichParagraph()
        val richSpan = RichSpan(paragraph = paragraph, text = "Hello World")
        paragraph.children.add(richSpan)

        val newParagraph = paragraph.slice(5, richSpan, false)

        assertEquals("Hello", richSpan.text)
        assertEquals(" World", newParagraph.children.first().text)
    }

    @Test
    fun testSliceWithNestedRichSpans() {
        val paragraph = RichParagraph()
        val parentSpan = RichSpan(
            paragraph = paragraph,
            text = "Hello",
            spanStyle = SpanStyle(fontWeight = FontWeight.Bold)
        )
        val childSpan = RichSpan(
            paragraph = paragraph,
            parent = parentSpan,
            text = " World",
            spanStyle = SpanStyle(color = Color.Red)
        )
        parentSpan.children.add(childSpan)
        paragraph.children.add(parentSpan)

        val newParagraph = paragraph.slice(7, childSpan, false)

        assertEquals("Hello", parentSpan.text)
        assertEquals(" W", childSpan.text)
        assertEquals("orld", newParagraph.children.first().text)
        assertTrue(newParagraph.children.first().spanStyle.color == Color.Red)
    }

    @Test
    fun testSliceWithMultipleChildren() {
        val paragraph = RichParagraph()
        val firstSpan = RichSpan(paragraph = paragraph, text = "First")
        val secondSpan = RichSpan(paragraph = paragraph, text = " Second")
        val thirdSpan = RichSpan(paragraph = paragraph, text = " Third")

        paragraph.children.addAll(listOf(firstSpan, secondSpan, thirdSpan))

        val newParagraph = paragraph.slice(7, secondSpan, false)

        assertEquals("First", firstSpan.text)
        assertEquals(" S", secondSpan.text)
        assertEquals("econd", newParagraph.children.first().text)
        assertEquals(" Third", newParagraph.children[1].text)
    }

    @Test
    fun testGetTextRange() {
        val paragraph = RichParagraph(
            type = OrderedList(number = 1)
        )
        val richSpan1 = RichSpan(paragraph = paragraph, text = "Hello", textRange = TextRange(3, 8))
        val richSpan2 = RichSpan(paragraph = paragraph, text = " World", textRange = TextRange(8, 14))

        paragraph.children.addAll(listOf(richSpan1, richSpan2))

        val textRange = paragraph.getTextRange()
        assertEquals(3, textRange.start)
        assertEquals(14, textRange.end)
    }

    @Test
    fun testGetFirstNonEmptyChild() {
        val paragraph = RichParagraph()
        val emptySpan = RichSpan(paragraph = paragraph, text = "")
        val nonEmptySpan = RichSpan(paragraph = paragraph, text = "Content")

        paragraph.children.addAll(listOf(emptySpan, nonEmptySpan))

        val firstNonEmpty = paragraph.getFirstNonEmptyChild()
        assertNotNull(firstNonEmpty)
        assertEquals("Content", firstNonEmpty.text)
    }

    @Test
    fun testIsEmpty() {
        val paragraph = RichParagraph()
        assertTrue(paragraph.isEmpty())

        val emptySpan = RichSpan(paragraph = paragraph, text = "")
        paragraph.children.add(emptySpan)
        assertTrue(paragraph.isEmpty())

        val nonEmptySpan = RichSpan(paragraph = paragraph, text = "Content")
        paragraph.children.add(nonEmptySpan)
        assertTrue(!paragraph.isEmpty())
    }

    @Test
    fun testRemoveEmptyChildren() {
        val paragraph = RichParagraph()
        val emptySpan1 = RichSpan(paragraph = paragraph, text = "")
        val nonEmptySpan = RichSpan(paragraph = paragraph, text = "Content")
        val emptySpan2 = RichSpan(paragraph = paragraph, text = "")

        paragraph.children.addAll(listOf(emptySpan1, nonEmptySpan, emptySpan2))
        assertEquals(3, paragraph.children.size)

        paragraph.removeEmptyChildren()
        assertEquals(1, paragraph.children.size)
        assertEquals("Content", paragraph.children.first().text)
    }

    @Test
    fun testUpdateChildrenParagraph() {
        val originalParagraph = RichParagraph()
        val newParagraph = RichParagraph()

        val span1 = RichSpan(paragraph = originalParagraph, text = "Span 1")
        val span2 = RichSpan(paragraph = originalParagraph, text = "Span 2")
        originalParagraph.children.addAll(listOf(span1, span2))

        originalParagraph.updateChildrenParagraph(newParagraph)

        assertEquals(newParagraph, span1.paragraph)
        assertEquals(newParagraph, span2.paragraph)
    }

    @Test
    fun testCopy() {
        val originalParagraph = RichParagraph(
            type = OrderedList(number = 5)
        )
        val span = RichSpan(
            paragraph = originalParagraph,
            text = "Test content",
            spanStyle = SpanStyle(fontSize = 16.sp)
        )
        originalParagraph.children.add(span)

        val copiedParagraph = originalParagraph.copy()

        assertEquals(originalParagraph.type::class, copiedParagraph.type::class)
        assertEquals((originalParagraph.type as OrderedList).number, (copiedParagraph.type as OrderedList).number)
        assertEquals(originalParagraph.children.size, copiedParagraph.children.size)
        assertEquals(originalParagraph.children.first().text, copiedParagraph.children.first().text)
        assertEquals(originalParagraph.children.first().spanStyle.fontSize, copiedParagraph.children.first().spanStyle.fontSize)

        // Verify it's a deep copy
        assertTrue(originalParagraph !== copiedParagraph)
        assertTrue(originalParagraph.children.first() !== copiedParagraph.children.first())
    }

    @Test
    fun testGetRichSpanByTextIndex() {
        val paragraph = RichParagraph()
        val span1 = RichSpan(paragraph = paragraph, text = "First", textRange = TextRange(0, 5))
        val span2 = RichSpan(paragraph = paragraph, text = " Second", textRange = TextRange(5, 12))

        paragraph.children.addAll(listOf(span1, span2))

        val (newIndex, foundSpan) = paragraph.getRichSpanByTextIndex(
            paragraphIndex = 0,
            textIndex = 3,
            offset = 0
        )

        assertNotNull(foundSpan)
        assertEquals("First", foundSpan.text)
    }

    @Test
    fun testGetRichSpanListByTextRange() {
        val paragraph = RichParagraph()
        val span1 = RichSpan(paragraph = paragraph, text = "First", textRange = TextRange(0, 5))
        val span2 = RichSpan(paragraph = paragraph, text = " Second", textRange = TextRange(5, 12))
        val span3 = RichSpan(paragraph = paragraph, text = " Third", textRange = TextRange(12, 18))

        paragraph.children.addAll(listOf(span1, span2, span3))

        val (newIndex, spanList) = paragraph.getRichSpanListByTextRange(
            paragraphIndex = 0,
            searchTextRange = TextRange(3, 15),
            offset = 0
        )

        assertEquals(3, spanList.size)
        assertEquals("First", spanList[0].text)
        assertEquals(" Second", spanList[1].text)
        assertEquals(" Third", spanList[2].text)
    }

    @Test
    fun testGetStartTextSpanStyle() {
        val paragraph = RichParagraph(
            type = OrderedList(number = 1)
        )
        val span = RichSpan(
            paragraph = paragraph,
            text = "Content",
            spanStyle = SpanStyle(fontWeight = FontWeight.Bold)
        )
        paragraph.children.add(span)

        val startTextSpanStyle = paragraph.getStartTextSpanStyle()
        assertNotNull(startTextSpanStyle)
        assertEquals(FontWeight.Bold, startTextSpanStyle.fontWeight)
    }
}
*/