# Simple File Downloader

This is a simple file downloader project written in Java. The application supports downloading files over HTTP and was developed using an older version of Java. It demonstrates basic networking and file handling capabilities in Java.

---

## Features
- Downloads files from any HTTP URL.
- Saves the downloaded file to a specified location.
- Simple and lightweight design.

---

## Prerequisites
- **Java Development Kit (JDK):** Ensure that you have a Java version compatible with older Java codebases installed on your system.

---

## How to Run

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/yourusername/file-downloader.git
   cd file-downloader
   ```

2. **Compile the Code:**
   Navigate to the `src` directory and compile the Java files using the terminal:
   ```bash
   javac *.java
   ```

3. **Run the Application:**
   Execute the main class to run the file downloader:
   ```bash
   java MainClassName
   ```
   Replace `MainClassName` with the actual name of the main class.

4. **Provide Input:**
   - Enter the HTTP URL of the file you want to download.
   - Specify the destination path where the file should be saved.

---

## Example Usage
```
Enter the URL of the file to download: http://example.com/file.txt
Enter the destination path: /path/to/save/file.txt
File downloaded successfully!
```

---

## Limitations
- **Protocol Support:** Only supports HTTP (no HTTPS support).
- **Error Handling:** Minimal error handling for invalid URLs or connection issues.
- **File Size:** Performance may degrade with very large files.

---

## Contributing
Contributions are welcome! Feel free to fork this repository and submit a pull request with your improvements or suggestions.

---

## License
This project is open-source and available under the [MIT License](LICENSE).

---

## Author
[Benjamin](https://github.com/the-JVM-jedi)
