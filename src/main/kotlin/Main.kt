import com.formdev.flatlaf.FlatDarkLaf
import org.commonmark.node.Node
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.nio.file.Files
import java.nio.file.Paths
import javax.swing.*
import javax.swing.event.HyperlinkEvent

fun main() {
    // Set the FlatLaf dark theme
    UIManager.setLookAndFeel(FlatDarkLaf())

    SwingUtilities.invokeLater {
        val frame = JFrame("Markdown Viewer")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.setSize(800, 600) // Adjusted size
        frame.minimumSize = Dimension(500, 500)

        // Create a text area for plain Markdown
        val textArea = JTextArea()
        textArea.isEditable = false
        textArea.background = Color(43, 43, 43)
        textArea.foreground = Color(187, 187, 187)
        textArea.caretColor = Color(187, 187, 187)
        textArea.font = textArea.font.deriveFont(14f)

        val scrollPane = JScrollPane(textArea)
        scrollPane.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)

        // Create an editor pane for formatted Markdown
        val formattedTextPane = JEditorPane("text/html", "")
        formattedTextPane.isEditable = false
        formattedTextPane.background = Color(43, 43, 43)
        formattedTextPane.foreground = Color(187, 187, 187)
        formattedTextPane.addHyperlinkListener { e ->
            if (e.eventType == HyperlinkEvent.EventType.ACTIVATED) {
                // Handle hyperlink clicks (if needed)
                JOptionPane.showMessageDialog(frame, "Link clicked: ${e.url}")
            }
        }

        val formattedScrollPane = JScrollPane(formattedTextPane)
        formattedScrollPane.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)

        // Buttons to load and render Markdown
        val loadButton = JButton("Load Markdown File")
        loadButton.preferredSize = Dimension(160, 30)
        loadButton.addActionListener { _: ActionEvent ->
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

        val renderButton = JButton("Render Markdown")
        renderButton.preferredSize = Dimension(160, 30)
        renderButton.addActionListener {
            val markdownText = textArea.text
            if (markdownText.isNotBlank()) {
                val parser: Parser = Parser.builder().build()
                val document: Node = parser.parse(markdownText)
                val renderer: HtmlRenderer = HtmlRenderer.builder().build()
                val htmlContent = renderer.render(document)
                formattedTextPane.text = htmlContent
            } else {
                JOptionPane.showMessageDialog(frame, "No Markdown content to render", "Empty Content", JOptionPane.WARNING_MESSAGE)
            }
        }

        // Layout setup
        val buttonPanel = JPanel()
        buttonPanel.layout = BoxLayout(buttonPanel, BoxLayout.X_AXIS)
        buttonPanel.add(loadButton)
        buttonPanel.add(Box.createHorizontalStrut(10)) // Spacer
        buttonPanel.add(renderButton)

        // SplitPane to equally divide space between the two panes
        val splitPane = JSplitPane(JSplitPane.VERTICAL_SPLIT)
        splitPane.topComponent = scrollPane
        splitPane.bottomComponent = formattedScrollPane
        splitPane.dividerLocation = frame.height / 2 // Divide space equally
        splitPane.resizeWeight = 0.5 // Allocate equal space to both panes
        splitPane.isContinuousLayout = true // Smooth resizing

        frame.layout = BorderLayout()
        frame.add(buttonPanel, BorderLayout.NORTH)
        frame.add(splitPane, BorderLayout.CENTER)
        frame.isVisible = true
    }
}
