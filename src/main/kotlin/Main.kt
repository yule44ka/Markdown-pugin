import com.formdev.flatlaf.FlatDarkLaf
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.nio.file.Files
import java.nio.file.Paths
import javax.swing.*

fun main() {
    // Set the FlatLaf dark theme
    UIManager.setLookAndFeel(FlatDarkLaf())

    SwingUtilities.invokeLater {
        val frame = JFrame("Markdown Viewer")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.setSize(500, 400) // Reduced size for a more compact window
        frame.minimumSize = Dimension(500, 400) // Minimum size to prevent too much shrinking

        // Create a stylish JTextArea with custom font and colors
        val textArea = JTextArea()
        textArea.isEditable = false
        textArea.background = Color(43, 43, 43) // Dark background color
        textArea.foreground = Color(187, 187, 187) // Light text color
        textArea.caretColor = Color(187, 187, 187) // Caret color for better visibility
        textArea.font = textArea.font.deriveFont(14f) // Set a readable font size

        val scrollPane = JScrollPane(textArea)
        scrollPane.border = BorderFactory.createEmptyBorder(10, 10, 10, 10) // Add padding around the text area

        val loadButton = JButton("Load Markdown File")
        loadButton.preferredSize = Dimension(160, 30) // Set a consistent button size
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

        frame.layout = BorderLayout()
        frame.add(loadButton, BorderLayout.NORTH)
        frame.add(scrollPane, BorderLayout.CENTER)
        frame.isVisible = true
    }
}
