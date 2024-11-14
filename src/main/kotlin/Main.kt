import java.awt.BorderLayout
import java.awt.event.ActionEvent
import java.nio.file.Files
import java.nio.file.Paths
import javax.swing.*

fun main() {
    SwingUtilities.invokeLater {
        val frame = JFrame("Markdown Viewer")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.setSize(600, 400)

        val textArea = JTextArea()
        textArea.isEditable = false
        val scrollPane = JScrollPane(textArea)

        val loadButton = JButton("Load Markdown File")
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
