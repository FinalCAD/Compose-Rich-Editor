/*
package com.mohamedrejeb.richeditor.model

import androidx.compose.ui.text.input.TextFieldValue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ListBehaviorTest {

    @Test
    fun testExitListOnEmptyItem_defaultBehavior() {
        val state = RichTextState()
        state.config.exitListOnEmptyItem = true

        // Start with an unordered list
        state.setText("- Item 1")
        state.addUnorderedList()

        // Press enter to create a new list item
        state.addTextAfterSelection("\n")

        // Verify we're in an unordered list
        assertTrue(state.isUnorderedList)

        // Press enter again on empty list item - should exit list
        state.addTextAfterSelection("\n")

        // Should not be in list anymore
        assertFalse(state.isUnorderedList)
    }

    @Test
    fun testExitListOnEmptyItem_disabled() {
        val state = RichTextState()
        state.config.exitListOnEmptyItem = false

        // Start with an unordered list
        state.setText("- Item 1")
        state.addUnorderedList()

        // Press enter to create a new list item
        state.addTextAfterSelection("\n")

        // Verify we're in an unordered list
        assertTrue(state.isUnorderedList)

        // Press enter again on empty list item - should stay in list
        state.addTextAfterSelection("\n")

        // Should still be in list
        assertTrue(state.isUnorderedList)
    }

    @Test
    fun testListLevelIndentConfig() {
        val state = RichTextState()
        
        // Test default list indent
        assertEquals(25, state.config.listIndent)
        
        // Test custom list indent
        state.config.listIndent = 40
        assertEquals(40, state.config.listIndent)
        
        // Test specific ordered list indent
        state.config.orderedListIndent = 50
        assertEquals(50, state.config.orderedListIndent)
        
        // Test specific unordered list indent
        state.config.unorderedListIndent = 30
        assertEquals(30, state.config.unorderedListIndent)
    }

    @Test
    fun testPreserveStyleOnEmptyLine() {
        val state = RichTextState()
        
        // Test default behavior
        assertTrue(state.config.preserveStyleOnEmptyLine)
        
        // Test changing the config
        state.config.preserveStyleOnEmptyLine = false
        assertFalse(state.config.preserveStyleOnEmptyLine)
    }

    @Test
    fun testListItemCreation() {
        val state = RichTextState()
        
        // Test automatic list creation from "- "
        state.setText("- ")
        assertTrue(state.isUnorderedList)
        assertEquals("", state.toText().trim())
        
        // Test automatic ordered list creation from "1. "
        state.setText("1. ")
        assertTrue(state.isOrderedList)
        assertEquals("", state.toText().trim())
    }
}
*/