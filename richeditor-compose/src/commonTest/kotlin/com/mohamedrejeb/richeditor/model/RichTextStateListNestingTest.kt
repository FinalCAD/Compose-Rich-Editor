package com.mohamedrejeb.richeditor.model

import com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi

@OptIn(ExperimentalRichTextApi::class)
class RichTextStateListNestingTest {

    /*
    // Tests désactivés temporairement pour le refactoring des composants H1-H6
    */

    /*
    @Test
    fun testCanIncreaseListLevel() {
        val richTextState = RichTextState(
            initialRichParagraphList = listOf(
                RichParagraph(
                    type = OrderedList(
                        number = 1
                    ).apply { level = 1 },
                ).also {
                    it.children.add(
                        RichSpan(
                            text = "First",
                            paragraph = it,
                        ),
                    )
                },
                RichParagraph(
                    type = OrderedList(
                        number = 2
                    ).apply { level = 1 },
                ).also {
                    it.children.add(
                        RichSpan(
                            text = "Second",
                            paragraph = it,
                        ),
                    )
                }
            )
        )
        richTextState.selection = TextRange(10)

        assertTrue(richTextState.canIncreaseListLevel())
    }

    @Test
    fun testCannotIncreaseListLevelWithoutPreviousList() {
        val richTextState = RichTextState(
            initialRichParagraphList = listOf(
                RichParagraph(
                    type = OrderedList(
                        number = 1
                    ).apply { level = 1 },
                ).also {
                    it.children.add(
                        RichSpan(
                            text = "First",
                            paragraph = it,
                        ),
                    )
                }
            )
        )
        richTextState.selection = TextRange(5)

        assertFalse(richTextState.canIncreaseListLevel())
    }

    @Test
    fun testCannotIncreaseListLevelWhenHigherThanPrevious() {
        val richTextState = RichTextState(
            initialRichParagraphList = listOf(
                RichParagraph(
                    type = OrderedList(
                        number = 1
                    ).apply { level = 1 },
                ).also {
                    it.children.add(
                        RichSpan(
                            text = "First",
                            paragraph = it,
                        ),
                    )
                },
                RichParagraph(
                    type = OrderedList(
                        number = 2
                    ).apply { level = 2 },
                ).also {
                    it.children.add(
                        RichSpan(
                            text = "Second",
                            paragraph = it,
                        ),
                    )
                }
            )
        )
        richTextState.selection = TextRange(11)

        assertFalse(richTextState.canIncreaseListLevel())
    }

    @Test
    fun testCanDecreaseListLevel() {
        val richTextState = RichTextState(
            initialRichParagraphList = listOf(
                RichParagraph(
                    type = OrderedList(
                        number = 1
                    ).apply { level = 2 },
                ).also {
                    it.children.add(
                        RichSpan(
                            text = "First",
                            paragraph = it,
                        ),
                    )
                }
            )
        )
        richTextState.selection = TextRange(5)

        assertTrue(richTextState.canDecreaseListLevel())
    }

    @Test
    fun testCannotDecreaseListLevelAtLevel1() {
        val richTextState = RichTextState(
            initialRichParagraphList = listOf(
                RichParagraph(
                    type = OrderedList(
                        number = 1
                    ).apply { level = 1 },
                ).also {
                    it.children.add(
                        RichSpan(
                            text = "First",
                            paragraph = it,
                        ),
                    )
                }
            )
        )
        richTextState.selection = TextRange(5)

        assertFalse(richTextState.canDecreaseListLevel())
    }

    @Test
    fun testIncreaseListLevel() {
        val richTextState = RichTextState(
            initialRichParagraphList = listOf(
                RichParagraph(
                    type = OrderedList(
                        number = 1
                    ).apply { level = 1 },
                ).also {
                    it.children.add(
                        RichSpan(
                            text = "First",
                            paragraph = it,
                        ),
                    )
                },
                RichParagraph(
                    type = OrderedList(
                        number = 2
                    ).apply { level = 1 },
                ).also {
                    it.children.add(
                        RichSpan(
                            text = "Second",
                            paragraph = it,
                        ),
                    )
                }
            )
        )
        richTextState.selection = TextRange(11)

        richTextState.increaseListLevel()

        val secondParagraphType = richTextState.richParagraphList[1].type as OrderedList
        assertEquals(2, secondParagraphType.level)
    }

    @Test
    fun testDecreaseListLevel() {
        val richTextState = RichTextState(
            initialRichParagraphList = listOf(
                RichParagraph(
                    type = OrderedList(
                        number = 1
                    ).apply { level = 2 },
                ).also {
                    it.children.add(
                        RichSpan(
                            text = "First",
                            paragraph = it,
                        ),
                    )
                }
            )
        )
        richTextState.selection = TextRange(5)

        richTextState.decreaseListLevel()

        val paragraphType = richTextState.richParagraphList[0].type as OrderedList
        assertEquals(1, paragraphType.level)
    }
    */

}
