import com.formdev.flatlaf.FlatDarkLaf
import org.commonmark.node.Node
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.Desktop
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.net.URI
import javax.swing.*
import javax.swing.event.HyperlinkEvent

/**
 * Main function to initialize and launch the Markdown Viewer application.
 */
fun main() {
    UIManager.setLookAndFeel(FlatDarkLaf())
    SwingUtilities.invokeLater { createMainFrame() }
}

/**
 * Creates and configures the main frame of the application with text areas, buttons, and layout.
 */
fun createMainFrame() {
    val frame = JFrame("Markdown Viewer").apply {
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        setSize(800, 600)
        minimumSize = Dimension(500, 400)
    }

    val textArea = createTextArea()
    val formattedTextPane = createFormattedTextPane(frame)
    val buttonPanel = createButtonPanel(frame, textArea, formattedTextPane)
    val splitPane = createSplitPane(textArea, formattedTextPane)

    frame.layout = BorderLayout()
    frame.add(buttonPanel, BorderLayout.NORTH)
    frame.add(splitPane, BorderLayout.CENTER)
    frame.isVisible = true
}

/**
 * Creates a non-editable `JTextArea` for displaying raw Markdown content.
 *
 * @return A configured `JTextArea` component.
 */
fun createTextArea(): JTextArea {
    return JTextArea().apply {
        isEditable = false
        background = Color(43, 43, 43)
        foreground = Color(187, 187, 187)
        font = font.deriveFont(14f)
    }
}

/**
 * Creates a `JEditorPane` to render the HTML content generated from Markdown.
 * Also sets up hyperlink handling.
 *
 * @param frame The main frame to show error messages.
 * @return A configured `JEditorPane` component.
 */
fun createFormattedTextPane(frame: JFrame): JEditorPane {
    val formattedTextPane = JEditorPane("text/html", "").apply {
        isEditable = false
        background = Color(43, 43, 43)
        foreground = Color(187, 187, 187)
    }

    formattedTextPane.addHyperlinkListener { e ->
        if (e.eventType == HyperlinkEvent.EventType.ACTIVATED) {
            openLinkInBrowser(frame, e.url.toString())
        }
    }

    return formattedTextPane
}

/**
 * Opens a hyperlink in the default web browser.
 *
 * @param frame The main frame to show error messages.
 * @param url The URL to open in the browser.
 */
fun openLinkInBrowser(frame: JFrame, url: String) {
    try {
        Desktop.getDesktop().browse(URI.create(url))
    } catch (ex: IOException) {
        JOptionPane.showMessageDialog(frame, "Unable to open link: $url", "Error", JOptionPane.ERROR_MESSAGE)
    }
}

/**
 * Creates a panel containing buttons to load and render Markdown files.
 *
 * @param frame The main frame to show error messages.
 * @param textArea The `JTextArea` to display raw Markdown.
 * @param formattedTextPane The `JEditorPane` to display rendered HTML.
 * @return A configured `JPanel` containing the buttons.
 */
fun createButtonPanel(frame: JFrame, textArea: JTextArea, formattedTextPane: JEditorPane): JPanel {
    val loadButton = createLoadButton(frame, textArea)
    val renderButton = createRenderButton(frame, textArea, formattedTextPane)

    return JPanel().apply {
        layout = BoxLayout(this, BoxLayout.X_AXIS)
        add(Box.createHorizontalStrut(10))
        add(loadButton)
        add(Box.createHorizontalStrut(10))
        add(renderButton)
    }
}

/**
 * Creates a button to load a Markdown file.
 *
 * @param frame The main frame to show error messages.
 * @param textArea The `JTextArea` to display raw Markdown.
 * @return A configured `JButton` to load Markdown files.
 */
fun createLoadButton(frame: JFrame, textArea: JTextArea): JButton {
    return JButton("Load Markdown File").apply {
        preferredSize = Dimension(160, 30)
        addActionListener { _: ActionEvent ->
            loadMarkdownFile(frame, textArea)
        }
    }
}

/**
 * Loads a Markdown file selected by the user into the `JTextArea`.
 *
 * @param frame The main frame to show error messages.
 * @param textArea The `JTextArea` to display the loaded Markdown content.
 */
fun loadMarkdownFile(frame: JFrame, textArea: JTextArea) {
    val fileChooser = JFileChooser()
    val result = fileChooser.showOpenDialog(frame)
    if (result == JFileChooser.APPROVE_OPTION) {
        val file = fileChooser.selectedFile
        if (file.extension == "md") {
            val content = Files.readAllLines(Paths.get(file.absolutePath)).joinToString("\n")
            textArea.text = content
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a .md file", "Invalid File", JOptionPane.ERROR_MESSAGE)
        }
    }
}

/**
 * Creates a button to render the Markdown content into HTML.
 *
 * @param frame The main frame to show error messages.
 * @param textArea The `JTextArea` containing the raw Markdown text.
 * @param formattedTextPane The `JEditorPane` to display the rendered HTML.
 * @return A configured `JButton` to render the Markdown content.
 */
fun createRenderButton(frame: JFrame, textArea: JTextArea, formattedTextPane: JEditorPane): JButton {
    return JButton("Render Markdown").apply {
        preferredSize = Dimension(160, 30)
        addActionListener {
            renderMarkdown(textArea.text, formattedTextPane, frame)
        }
    }
}

/**
 * Renders the Markdown content into HTML and displays it in the `JEditorPane`.
 *
 * @param markdownText The raw Markdown text to render.
 * @param formattedTextPane The `JEditorPane` to display the rendered HTML.
 * @param frame The main frame to show error messages.
 */
fun renderMarkdown(markdownText: String, formattedTextPane: JEditorPane, frame: JFrame) {
    if (markdownText.isNotBlank()) {
        val parser: Parser = Parser.builder().build()
        val document: Node = parser.parse(markdownText)
        val renderer: HtmlRenderer = HtmlRenderer.builder().build()
        formattedTextPane.text = renderer.render(document)
    } else {
        JOptionPane.showMessageDialog(frame, "No Markdown content to render", "Empty Content", JOptionPane.WARNING_MESSAGE)
    }
}

/**
 * Creates a split pane that divides the screen between raw Markdown text and rendered HTML.
 *
 * @param textArea The `JTextArea` to display raw Markdown.
 * @param formattedTextPane The `JEditorPane` to display rendered HTML.
 * @return A configured `JSplitPane` for the layout.
 */
fun createSplitPane(textArea: JTextArea, formattedTextPane: JEditorPane): JSplitPane {
    val scrollPane = JScrollPane(textArea).apply {
        border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
    }
    val formattedScrollPane = JScrollPane(formattedTextPane).apply {
        border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
    }

    val splitPane = JSplitPane(JSplitPane.VERTICAL_SPLIT).apply {
        topComponent = scrollPane
        bottomComponent = formattedScrollPane
        isContinuousLayout = true
    }

    // Dynamically adjust the divider location based on window size
    splitPane.dividerLocation = splitPane.height / 2

    // Dynamically update divider location when the frame is resized
    splitPane.addComponentListener(object : ComponentAdapter() {
        override fun componentResized(e: ComponentEvent?) {
            splitPane.dividerLocation = splitPane.height / 2 // Recalculate divider location
        }
    })

    return splitPane
}
