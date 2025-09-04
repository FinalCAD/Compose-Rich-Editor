// Tests pour vérifier le bon fonctionnement du décodage HTML des titres
package com.mohamedrejeb.richeditor.parser.html

import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi
import com.mohamedrejeb.richeditor.model.HeadingStyle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalRichTextApi::class)
class RichTextStateHtmlParserDecodeTest {

    @Test
    fun testH1FontSize() {
        // Test pour connaître la taille de police du H1
        val h1Style = HeadingStyle.H1
        val textStyle = h1Style.getTextStyle()
        val spanStyle = h1Style.getSpanStyle()

        println("=== H1 FONT SIZE ===")
        println("TextStyle fontSize: ${textStyle.fontSize}")
        println("SpanStyle fontSize: ${spanStyle.fontSize}")
        println("TextStyle fontWeight: ${textStyle.fontWeight}")
        println("SpanStyle fontWeight: ${spanStyle.fontWeight}")

        // Test avec un HTML simple pour voir la fontSize générée
        val inputHtml = "<h1>Test</h1>"
        val richTextState = RichTextStateHtmlParser.encode(inputHtml)
        val paragraph = richTextState.richParagraphList.first()
        val richSpan = paragraph.children.first()

        println("RichSpan fontSize: ${richSpan.spanStyle.fontSize}")
        println("RichSpan fontWeight: ${richSpan.spanStyle.fontWeight}")

        // Vérification que c'est bien du H1
        assertTrue(richSpan.spanStyle.fontSize.value > 0, "H1 should have a font size")
    }

    @Test
    fun testH1WithDirectionStyle() {
        val inputHtml = "<h1 style=\"direction: ltr;\">Bonjour</h1>"

        val richTextState = RichTextStateHtmlParser.encode(inputHtml)
        val outputHtml = RichTextStateHtmlParser.decode(richTextState)

        // Should start with h1 tag
        assertTrue(outputHtml.startsWith("<h1"), "Should start with h1 tag, got: $outputHtml")

        // Should preserve direction style  
        assertTrue(
            outputHtml.contains("direction: ltr"),
            "Should preserve direction style, got: $outputHtml"
        )

        // Should contain the text
        assertTrue(outputHtml.contains("Bonjour"), "Should contain text, got: $outputHtml")

        // Should wrap text in span - this is what we want to achieve
        assertTrue(outputHtml.contains("<span"), "Should contain span wrapper, got: $outputHtml")
    }

    @Test
    fun testSimpleH1() {
        val inputHtml = "<h1>Simple</h1>"

        val richTextState = RichTextStateHtmlParser.encode(inputHtml)
        val outputHtml = RichTextStateHtmlParser.decode(richTextState)

        // Should be exactly: <h1><span>Simple</span></h1>
        val expected = "<h1><span>Simple</span></h1>"
        assertEquals(expected, outputHtml, "Simple H1 should match expected structure")
    }

    @Test
    fun testH2WithStyles() {
        val inputHtml = "<h2 style=\"color: red;\">Title</h2>"

        val richTextState = RichTextStateHtmlParser.encode(inputHtml)
        val outputHtml = RichTextStateHtmlParser.decode(richTextState)

        assertTrue(outputHtml.startsWith("<h2"), "Should be h2 tag")
        assertTrue(outputHtml.contains("<span"), "Should contain span")
    }

    @Test
    fun testParagraphWithSpan() {
        val inputHtml = "<p>Text</p>"

        val richTextState = RichTextStateHtmlParser.encode(inputHtml)
        val outputHtml = RichTextStateHtmlParser.decode(richTextState)

        // Should be: <p><span>Text</span></p>
        val expected = "<p><span>Text</span></p>"
        assertEquals(expected, outputHtml, "Simple paragraph should match expected structure")
    }

    @Test
    fun testAllHeadingFontSizes() {
        println("=== ALL HEADING FONT SIZES ===")

        val headings = listOf(
            HeadingStyle.H1, HeadingStyle.H2, HeadingStyle.H3,
            HeadingStyle.H4, HeadingStyle.H5, HeadingStyle.H6
        )

        headings.forEach { heading ->
            val textStyle = heading.getTextStyle()
            println(
                "${heading.htmlTag?.uppercase()}: ${textStyle.fontSize} (Material 3 Typography: ${
                    getTypographyName(
                        heading
                    )
                })"
            )
        }

        println("NORMAL: ${HeadingStyle.Normal.getTextStyle().fontSize}")
    }

    private fun getTypographyName(heading: HeadingStyle): String {
        return when (heading) {
            HeadingStyle.H1 -> "displayLarge"
            HeadingStyle.H2 -> "displayMedium"
            HeadingStyle.H3 -> "displaySmall"
            HeadingStyle.H4 -> "headlineMedium"
            HeadingStyle.H5 -> "headlineSmall"
            HeadingStyle.H6 -> "titleLarge"
            HeadingStyle.Normal -> "Default"
        }
    }
}