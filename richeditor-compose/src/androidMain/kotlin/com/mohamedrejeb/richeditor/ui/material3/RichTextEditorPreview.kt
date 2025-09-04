package com.mohamedrejeb.richeditor.ui.material3

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.richeditor.model.rememberRichTextState

/**
 * Exemple d'utilisation simple du RichTextEditor
 *
 * Usage:
 * ```
 * RichTextEditorSimpleExample()
 * ```
 */
@Composable
public fun RichTextEditorSimpleExample() {
    val state = rememberRichTextState()

    MaterialTheme {
        Surface {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                RichTextEditor(
                    state = state,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Écrivez votre texte ici...") }
                )
            }
        }
    }
}

/**
 * Exemple du RichTextEditor avec un label
 *
 * Usage:
 * ```
 * RichTextEditorWithLabelExample()
 * ```
 */
@Composable
public fun RichTextEditorWithLabelExample() {
    val state = rememberRichTextState()

    MaterialTheme {
        Surface {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                RichTextEditor(
                    state = state,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Contenu riche") },
                    placeholder = { Text("Tapez votre texte...") }
                )
            }
        }
    }
}

/**
 * Exemple du RichTextEditor avec des icônes de début et fin
 *
 * Usage:
 * ```
 * RichTextEditorWithIconsExample()
 * ```
 */
@Composable
public fun RichTextEditorWithIconsExample() {
    val state = rememberRichTextState()

    MaterialTheme {
        Surface {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                RichTextEditor(
                    state = state,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Message") },
                    placeholder = { Text("Composez votre message...") },
                    leadingIcon = {
                        Icon(Icons.Default.Edit, contentDescription = "Éditer")
                    },
                    trailingIcon = {
                        Icon(Icons.Default.Email, contentDescription = "Envoyer")
                    }
                )
            }
        }
    }
}

/**
 * Exemple du RichTextEditor avec du contenu HTML pré-rempli
 *
 * Usage:
 * ```
 * RichTextEditorWithContentExample()
 * ```
 */
@Composable
public fun RichTextEditorWithContentExample() {
    val state = rememberRichTextState().apply {
        setHtml(
            """
            <p>Voici un exemple de <strong>texte en gras</strong> et <em>texte en italique</em>.</p>
            <p>Vous pouvez également ajouter des <a href="https://example.com">liens</a>.</p>
            <ul>
                <li>Premier élément de liste</li>
                <li>Deuxième élément de liste</li>
            </ul>
        """.trimIndent()
        )
    }

    MaterialTheme {
        Surface {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                RichTextEditor(
                    state = state,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Éditeur riche") },
                    maxLines = 8
                )
            }
        }
    }
}

/**
 * Exemple du RichTextEditor en état d'erreur
 *
 * Usage:
 * ```
 * RichTextEditorErrorExample()
 * ```
 */
@Composable
public fun RichTextEditorErrorExample() {
    val state = rememberRichTextState()

    MaterialTheme {
        Surface {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                RichTextEditor(
                    state = state,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Champ requis") },
                    placeholder = { Text("Ce champ est obligatoire") },
                    isError = true,
                    supportingText = {
                        Text(
                            text = "Ce champ ne peut pas être vide",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                )
            }
        }
    }
}

/**
 * Exemple du RichTextEditor désactivé
 *
 * Usage:
 * ```
 * RichTextEditorDisabledExample()
 * ```
 */
@Composable
public fun RichTextEditorDisabledExample() {
    val state = rememberRichTextState().apply {
        setHtml("<p>Ce contenu ne peut pas être modifié.</p>")
    }

    MaterialTheme {
        Surface {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                RichTextEditor(
                    state = state,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Contenu désactivé") },
                    enabled = false
                )
            }
        }
    }
}

/**
 * Exemple du RichTextEditor en mode lecture seule
 *
 * Usage:
 * ```
 * RichTextEditorReadOnlyExample()
 * ```
 */
@Composable
public fun RichTextEditorReadOnlyExample() {
    val state = rememberRichTextState().apply {
        setHtml(
            """
            <p>Ce contenu est en <strong>lecture seule</strong>. Vous pouvez le sélectionner mais pas le modifier.</p>
            <p>Ceci est utile pour afficher du contenu formaté.</p>
        """.trimIndent()
        )
    }

    MaterialTheme {
        Surface {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                RichTextEditor(
                    state = state,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Aperçu du document") },
                    readOnly = true
                )
            }
        }
    }
}

/**
 * Exemple montrant différentes variations du RichTextEditor
 *
 * Usage:
 * ```
 * RichTextEditorVariationsExample()
 * ```
 */
@Composable
public fun RichTextEditorVariationsExample() {
    MaterialTheme {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Variations du RichTextEditor",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                // Éditeur simple
                RichTextEditor(
                    state = rememberRichTextState(),
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Éditeur simple") }
                )

                // Éditeur avec label
                RichTextEditor(
                    state = rememberRichTextState(),
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Avec label") },
                    placeholder = { Text("Tapez ici...") }
                )

                // Éditeur multiligne
                RichTextEditor(
                    state = rememberRichTextState(),
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Multiligne") },
                    placeholder = { Text("Contenu multiligne...") },
                    minLines = 3,
                    maxLines = 6
                )

                // Éditeur avec texte d'aide
                RichTextEditor(
                    state = rememberRichTextState(),
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Avec aide") },
                    placeholder = { Text("Exemple avec texte d'aide") },
                    supportingText = {
                        Text("Ce texte d'aide apparaît en bas du champ")
                    }
                )
            }
        }
    }
}

/**
 * Exemple complet montrant l'utilisation du RichTextEditor avec du contenu Markdown
 *
 * Usage:
 * ```
 * RichTextEditorMarkdownExample()
 * ```
 */
@Composable
public fun RichTextEditorMarkdownExample() {
    val state = rememberRichTextState().apply {
        setMarkdown(
            """
            # Titre principal
            
            Ceci est un exemple de **texte en gras** et _texte en italique_.
            
            ## Sous-titre
            
            Voici une liste :
            - Premier élément
            - Deuxième élément
            - Troisième élément
            
            Et voici un [lien vers exemple](https://example.com).
        """.trimIndent()
        )
    }

    MaterialTheme {
        Surface {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                RichTextEditor(
                    state = state,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Éditeur Markdown") },
                    maxLines = 12
                )
            }
        }
    }
}

/**
 * Preview simple du RichTextEditor
 */
@Preview(name = "RichTextEditor Simple", showBackground = true)
@Composable
private fun RichTextEditorSimplePreview() {
    val state = rememberRichTextState()

    MaterialTheme {
        Surface {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                RichTextEditor(
                    state = state,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Écrivez votre texte ici...") }
                )
            }
        }
    }
}

/**
 * Preview du RichTextEditor avec label
 */
@Preview(name = "RichTextEditor avec Label", showBackground = true)
@Composable
private fun RichTextEditorWithLabelPreview() {
    val state = rememberRichTextState()

    MaterialTheme {
        Surface {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                RichTextEditor(
                    state = state,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Contenu riche") },
                    placeholder = { Text("Tapez votre texte...") }
                )
            }
        }
    }
}

/**
 * Preview du RichTextEditor avec icônes
 */
@Preview(name = "RichTextEditor avec Icônes", showBackground = true)
@Composable
private fun RichTextEditorWithIconsPreview() {
    val state = rememberRichTextState()

    MaterialTheme {
        Surface {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                RichTextEditor(
                    state = state,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Message") },
                    placeholder = { Text("Composez votre message...") },
                    leadingIcon = {
                        Icon(Icons.Default.Edit, contentDescription = "Éditer")
                    },
                    trailingIcon = {
                        Icon(Icons.Default.Email, contentDescription = "Envoyer")
                    }
                )
            }
        }
    }
}

/**
 * Preview du RichTextEditor avec contenu pré-rempli
 */
@Preview(name = "RichTextEditor avec Contenu", showBackground = true)
@Composable
private fun RichTextEditorWithContentPreview() {
    val state = rememberRichTextState().apply {
        setHtml("""
            <p>Voici un exemple de <strong>texte en gras</strong> et <em>texte en italique</em>.</p>
            <p>Vous pouvez également ajouter des <a href="https://example.com">liens</a>.</p>
            <ul>
                <li>Premier élément de liste</li>
                <li>Deuxième élément de liste</li>
            </ul>
            <h3>Exemple de Titre H3</h3>
        """.trimIndent())
    }

    MaterialTheme {
        Surface {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                RichTextEditor(
                    state = state,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Éditeur riche") },
                    maxLines = 8
                )
            }
        }
    }
}

/**
 * Preview du RichTextEditor en état d'erreur
 */
@Preview(name = "RichTextEditor Erreur", showBackground = true)
@Composable
private fun RichTextEditorErrorPreview() {
    val state = rememberRichTextState()

    MaterialTheme {
        Surface {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                RichTextEditor(
                    state = state,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Champ requis") },
                    placeholder = { Text("Ce champ est obligatoire") },
                    isError = true,
                    supportingText = {
                        Text(
                            text = "Ce champ ne peut pas être vide",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                )
            }
        }
    }
}

/**
 * Preview du RichTextEditor désactivé
 */
@Preview(name = "RichTextEditor Désactivé", showBackground = true)
@Composable
private fun RichTextEditorDisabledPreview() {
    val state = rememberRichTextState().apply {
        setHtml("<p>Ce contenu ne peut pas être modifié.</p>")
    }

    MaterialTheme {
        Surface {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                RichTextEditor(
                    state = state,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Contenu désactivé") },
                    enabled = false
                )
            }
        }
    }
}

/**
 * Preview du RichTextEditor en lecture seule
 */
@Preview(name = "RichTextEditor Lecture Seule", showBackground = true)
@Composable
private fun RichTextEditorReadOnlyPreview() {
    val state = rememberRichTextState().apply {
        setHtml("""
            <p>Ce contenu est en <strong>lecture seule</strong>. Vous pouvez le sélectionner mais pas le modifier.</p>
            <p>Ceci est utile pour afficher du contenu formaté.</p>
        """.trimIndent())
    }

    MaterialTheme {
        Surface {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                RichTextEditor(
                    state = state,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Aperçu du document") },
                    readOnly = true
                )
            }
        }
    }
}

/**
 * Preview montrant différents styles de RichTextEditor
 */
@Preview(name = "RichTextEditor Variations", showBackground = true, heightDp = 800)
@Composable
private fun RichTextEditorVariationsPreview() {
    MaterialTheme {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Variations du RichTextEditor",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                // Éditeur simple
                RichTextEditor(
                    state = rememberRichTextState(),
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Éditeur simple") }
                )

                // Éditeur avec label
                RichTextEditor(
                    state = rememberRichTextState(),
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Avec label") },
                    placeholder = { Text("Tapez ici...") }
                )

                // Éditeur multiligne
                RichTextEditor(
                    state = rememberRichTextState(),
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Multiligne") },
                    placeholder = { Text("Contenu multiligne...") },
                    minLines = 3,
                    maxLines = 6
                )

                // Éditeur avec texte d'aide
                RichTextEditor(
                    state = rememberRichTextState(),
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Avec aide") },
                    placeholder = { Text("Exemple avec texte d'aide") },
                    supportingText = {
                        Text("Ce texte d'aide apparaît en bas du champ")
                    }
                )
            }
        }
    }
}

/**
 * Preview avec contenu Markdown
 */
@Preview(name = "RichTextEditor Markdown", showBackground = true)
@Composable
private fun RichTextEditorMarkdownPreview() {
    val state = rememberRichTextState().apply {
        setMarkdown("""
            # Titre principal
            
            Ceci est un exemple de **texte en gras** et _texte en italique_.
            
            ## Sous-titre
            
            Voici une liste :
            - Premier élément
            - Deuxième élément
            - Troisième élément
            
            Et voici un [lien vers exemple](https://example.com).
        """.trimIndent())
    }

    MaterialTheme {
        Surface {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                RichTextEditor(
                    state = state,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Éditeur Markdown") },
                    maxLines = 12
                )
            }
        }
    }
}

/**
 * Preview avec singleLine = true
 */
@Preview(name = "RichTextEditor Single Line", showBackground = true)
@Composable
private fun RichTextEditorSingleLinePreview() {
    val state = rememberRichTextState()

    MaterialTheme {
        Surface {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                RichTextEditor(
                    state = state,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Ligne unique") },
                    placeholder = { Text("Saisissez une ligne...") },
                    singleLine = true,
                    trailingIcon = {
                        Icon(Icons.Default.Email, contentDescription = "Envoyer")
                    }
                )
            }
        }
    }
}