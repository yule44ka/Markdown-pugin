# Markdown Viewer

A desktop application developed in Kotlin to load and display the contents of Markdown files in both plain text and formatted HTML.

## Features

- **Load Markdown Files**: Easily select and load `.md` files from your local disk.
- **Plain Text Display**: Displays the contents of Markdown files without formatting.

   *Additional*:
- **Formatted Markdown Rendering**: Renders Markdown files in HTML format for better visualization.
- **Hyperlink Support**: Clickable links in the rendered Markdown content.

## Getting Started

Follow these instructions to get a copy of the project up and running on your local machine.

### Prerequisites
- **Kotlin**: Make sure you have Kotlin set up in your development environment.
- **Gradle**: Ensure Gradle is installed or use the provided Gradle wrapper.
- **FlatLaf**: Used for the dark theme UI.

### Setup Instructions
1. **Clone the Repository**
2. **Open the Project in IntelliJ IDEA**
3. **Build and Run**:
   - **Using IntelliJ IDEA**: Click on `Build > Build Project`, then run `Main.kt`.
   - **Using Command Line**:
     ```
     ./gradlew run
     ```

## Usage

1. **Load Markdown File**: Click the `Load Markdown File` button to select a `.md` file from your file system.
2. **Render Markdown**: Click the `Render Markdown` button to display the rendered HTML version of the Markdown file.
