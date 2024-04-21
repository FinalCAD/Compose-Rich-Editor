package com.mohamedrejeb.richeditor.sample.common.markdowneditor

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mohamedrejeb.richeditor.model.rememberRichTextState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarkdownEditorContent() {
    val navigator = LocalNavigator.currentOrThrow

    var isMarkdownToRichText by remember { mutableStateOf(false) }

    var markdown by remember {
        mutableStateOf(TextFieldValue(""))
    }
    val richTextState = rememberRichTextState()

    LaunchedEffect(richTextState.annotatedString, isMarkdownToRichText) {
        if (!isMarkdownToRichText) {
            markdown = TextFieldValue(richTextState.toMarkdown())
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Markdown Editor") },
                navigationIcon = {
                    IconButton(
                        onClick = { navigator.pop() }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            isMarkdownToRichText = !isMarkdownToRichText
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.SwapHoriz,
                            contentDescription = "Swap",
                        )
                    }
                }
            )
        },
        modifier = Modifier
            .fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .windowInsetsPadding(WindowInsets.ime)
                .fillMaxSize()
        ) {
            if (isMarkdownToRichText) {
                MarkdownToRichText(
                    markdown = markdown,
                    onMarkdownChange = {
                        markdown = it
                        richTextState.setMarkdown(it.text)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            } else {
                RichTextToMarkdown(
                    richTextState = richTextState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }
    }
}