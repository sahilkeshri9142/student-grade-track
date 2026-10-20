# NeuroBuddy AI – Smart Conversational Assistant
> A modern, offline, Java-based Artificial Intelligence study assistant with rule-based Natural Language Processing (NLP), sentiment awareness, confidence scoring, and an interactive dark navy & purple Java Swing GUI.

---

## 📌 Project Overview
**NeuroBuddy AI** is an intelligent desktop conversational assistant designed for college computer science students, educators, and software engineering enthusiasts. It operates **100% offline** without relying on third-party cloud APIs, external paid services, or machine-learning model latency.

Equipped with a transparent **6-stage rule-based Natural Language Processing (NLP) pipeline**, NeuroBuddy accurately understands user inquiries across core Computer Science domains, tolerates typos, extracts keywords, calculates statistical confidence scores, detects emotional tone, and responds with helpful, personalized academic explanations and study advice.

---

## ✨ Key Features

### 1. 🧠 Smart Rule-Based NLP Engine
- **Text Cleaning & Contraction Expansion**: Converts queries to lowercase, removes punctuation, and expands conversational idioms (e.g., *"what's"* ➔ *"what is"*, *"don't"* ➔ *"do not"*).
- **Tokenization & Stopword Elimination**: Splits text into word tokens while filtering out common non-content words without losing interrogative intent.
- **Rule-Based Suffix Stemming**: Normalizes word forms (e.g., *processes* ➔ *process*, *debugging* ➔ *debug*).
- **N-Gram Phrase Extraction**: Captures compound technical terms like *"operating system"*, *"binary search"*, *"linked list"*, and *"acid properties"*.
- **Fuzzy Typo Tolerance**: Employs Levenshtein distance matching to recognize words even with minor spelling mistakes (e.g., *"pyton"* ➔ *"python"*).
- **Confidence Scoring Engine**: Computes match percentages (0% – 100%) based on token overlap, Jaccard similarity against training questions, required keyword validation, and contextual topic continuity.

### 2. 📚 AI Study Assistant (6 Core Academic Domains)
Includes **over 40 predefined intents** with detailed, syllabus-standard explanations:
1. **Java Programming**: Platform independence, Bytecode & JVM, 4 Pillars of OOP, JDK vs JRE vs JVM, Garbage Collection & Generational Heap, ArrayList vs LinkedList, Multithreading & Thread Lifecycle.
2. **Python**: List vs Tuple (Mutability), Global Interpreter Lock (GIL), Decorators & Closures, Deep Copy vs Shallow Copy.
3. **Database Management Systems (DBMS)**: System architecture, ACID Properties with banking analogies, Database Normalization (1NF, 2NF, 3NF, BCNF), SQL vs NoSQL, Indexing & B-Trees.
4. **Data Structures & Algorithms (DSA)**: Arrays vs Linked Lists, Stacks vs Queues (LIFO/FIFO), Asymptotic Big-O Complexity, Binary Search algorithm & precondition, Binary Search Trees (BST) vs AVL Trees, Breadth-First Search (BFS) vs Depth-First Search (DFS).
5. **Operating Systems (OS)**: Process vs Thread, Deadlocks & the 4 Coffman Conditions, Virtual Memory & Paging (Page Faults), CPU Scheduling (FCFS, SJF, Round Robin).
6. **Computer Networks**: The 7 Layers of the OSI Model (with mnemonics), TCP vs UDP protocols, DNS resolution workflow, HTTP vs HTTPS (TLS Handshake), IP vs MAC vs Port numbers.

### 3. 💬 Mood-Aware Empathetic Replies
- Detects user sentiment (Positive, Frustrated, Stressed/Demotivated, Confused, Neutral) using an offline sentiment lexicon.
- Prepares supportive, constructive advice when students feel stuck on bugs, overwhelmed by exam pressure, or experiencing imposter syndrome.

### 4. 👤 Dynamic Personalization & Session Memory
- Remembers the user's nickname across the active conversation (e.g., *"Call me Anup"*, *"My name is Maya"*).
- Maintains conversation continuity and remembers the current study subject so follow-up inquiries receive higher relevance.
- Tracks real-time session metrics (messages sent, topics covered).

### 5. 🎨 Modern Dark Navy & Purple GUI (Java Swing)
- **Palette**: Dark Navy (`#0F1422`), Deep Slate (`#13182B`), and Purple Gradient (`#7C3AED` to `#6D28D9`).
- **Rounded Message Bubbles**: Right-aligned user bubbles and left-aligned bot bubbles with avatar icons.
- **Rich Text Formatting**: Markdown bold, lists, and monospace code blocks.
- **Confidence Pills**: Displays matching confidence (`🎯 85% Match`) on every bot message.
- **Copy to Clipboard**: Quick `📋 Copy` button on each bot message with animated feedback.
- **Animated Typing Indicator**: 3-dot pulsing indicator (`● ● ●`) simulating natural reading & thinking delay.
- **Interactive Suggestion Bar**: Clickable chips at the top and bottom that paste or run queries with a single click.
- **Chat Management**: One-click **Clear Chat** and **Export Chat History** (`.txt` transcript).

---

## 🛠️ Technology Stack
- **Language**: Java 17+ (Developed and tested with OpenJDK 21 LTS)
- **GUI Framework**: Java Swing (with custom `Graphics2D` anti-aliasing and subpixel rendering)
- **Architecture**: Object-Oriented Design (MVC-inspired separation: Models, NLP, Engine, GUI, Data)
- **Storage & Memory**: Java Collections (`ArrayList`, `HashMap`, `HashSet`)
- **Dependencies**: 100% Pure Java SE standard library — **Zero external JARs or paid APIs needed**

---

## 📂 Project Structure

```
Artificial Intelligence Chatbot/
├── .vscode/
│   ├── launch.json               # F5 Debugger configuration for VS Code
│   ├── settings.json             # Java classpath & JDK path configuration
│   └── tasks.json                # VS Code build and run tasks
├── bin/                          # Compiled .class bytecode
├── src/
│   └── com/
│       └── neurobuddy/
│           └── ai/
│               ├── Main.java                    # Application Entry Point
│               ├── NeuroBuddyConsoleTest.java   # Automated CLI test suite (14 test cases)
│               ├── data/
│               │   └── KnowledgeBase.java       # 40+ structured intents and sample answers
│               ├── engine/
│               │   ├── ResponseEngine.java      # Pipeline orchestrator
│               │   └── SessionMemory.java       # Session context, topic tracking & file export
│               ├── gui/
│               │   ├── ChatbotGUI.java          # Main window and layout controller
│               │   ├── ChatBubblePanel.java     # Rounded user/bot chat bubble renderer
│               │   ├── ModernButton.java        # Custom styled button with hover gradients
│               │   ├── ModernScrollBarUI.java   # Minimalist dark scrollbar
│               │   ├── SidebarPanel.java        # Left navigation, branding, stats & profile
│               │   ├── SuggestionBar.java       # Dynamic horizontal suggestion chips
│               │   ├── ThemeConstants.java      # Dark navy and purple color tokens
│               │   └── TypingIndicatorPanel.java# Animated pulsing three-dot indicator
│               ├── model/
│               │   ├── ChatMessage.java         # Message data model (Sender, text, confidence)
│               │   ├── Intent.java              # Intent model (Keywords, templates, thresholds)
│               │   ├── StudyTopic.java          # Study category enumeration
│               │   └── UserProfile.java         # Nickname and exploration metrics model
│               └── nlp/
│                   ├── IntentMatcher.java       # Intent classification and confidence scoring
│                   ├── NLPProcessor.java        # Sanitization, tokenization, stemming & n-grams
│                   └── SentimentAnalyzer.java   # Rule-based sentiment/mood lexicon
├── index.html                    # Web Edition (Runs with VS Code Live Server)
├── style.css                     # Modern dark navy & purple web theme
├── app.js                        # Client-side NLP engine and UI controller
├── run.bat                       # One-click Java Desktop GUI launcher
├── run.ps1                       # One-click PowerShell Desktop GUI launcher
├── run_live_server.bat           # One-click Java HTTP Live Server launcher (Port 8080)
└── README.md                     # Comprehensive documentation
```

---

## 🚀 Execution & Setup Instructions

NeuroBuddy AI can be run in two ways:
1. **🌐 Web Edition (VS Code Live Server or Browser)**
2. **🖥️ Java Swing Desktop Application**

---

### Method 1: VS Code Live Server Extension (Web Edition)
1. In VS Code, open the project folder.
2. Right-click on **`index.html`** in the Explorer sidebar.
3. Select **"Open with Live Server"** (or click the **"Go Live"** button in the bottom status bar).
4. The web version will immediately open at `http://127.0.0.1:5500/index.html` with full offline NLP processing!

---

### Method 2: One-Click Java Live Server (Port 8080)
Double-click `run_live_server.bat` (or run `java -cp bin com.neurobuddy.ai.LiveServer`).
It starts an internal lightweight server and opens `http://localhost:8080` in your browser.

---

### Method 3: One-Click Desktop GUI (Java Swing)
Double-click `run.bat` or open PowerShell / Command Prompt in the project folder and type:
```cmd
run.bat
```
or via PowerShell:
```powershell
.\run.ps1
```
*The script automatically compiles the `.java` sources and launches the native desktop interface.*

---

### Method 4: Manual Terminal Compilation & Execution
1. Open PowerShell or Command Prompt in the project root directory.
2. Compile all source files:
```powershell
javac -encoding UTF-8 -d bin -sourcepath src (Get-ChildItem -Path src -Recurse -Filter "*.java" | Select-Object -ExpandProperty FullName)
```
3. Run the GUI application:
```powershell
java -cp bin com.neurobuddy.ai.Main
```
4. (Optional) Run the automated 14-point test suite:
```powershell
java -cp bin com.neurobuddy.ai.NeuroBuddyConsoleTest
```

---

### Method 5: Running inside VS Code
1. Open Visual Studio Code.
2. Select **File ➔ Open Folder...** and choose `Artificial Intelligence Chatbot`.
3. If prompted to install recommended Java extensions (*Extension Pack for Java*), you may accept.
4. Press **`Ctrl + Shift + B`** to run the default build task (`Build NeuroBuddy AI`).
5. Press **`F5`** to launch the GUI with debugging, or open `Main.java` and click the **Run** button at the top right.

---

## 🧪 Automated Test Suite Output
NeuroBuddy AI includes a verification suite (`NeuroBuddyConsoleTest.java`) that validates response accuracy, confidence thresholds, and sentiment recognition across all 14 baseline test scenarios:

```
=========================================================
  NeuroBuddy AI - Automated Verification & NLP Test Suite
=========================================================

[01] User Input: "Hello"
     Intent: greeting             | Confidence: 75% | Mood: NEUTRAL
[02] User Input: "What is DBMS?"
     Intent: dbms_overview        | Confidence: 81% | Mood: NEUTRAL
[03] User Input: "I feel demotivated."
     Intent: motivation_demotivated | Confidence: 80% | Mood: STRESSED_DEMOTIVATED
[04] User Input: "Tell me a joke."
     Intent: jokes_programming    | Confidence: 75% | Mood: NEUTRAL
[05] User Input: "How does quantum teleportation bake cookies in Mars?"
     Intent: unknown_fallback     | Confidence: 35% | Mood: NEUTRAL
[06] User Input: "Explain OOP concepts in Java"
     Intent: java_oop             | Confidence: 81% | Mood: NEUTRAL
[07] User Input: "Difference between list and tuple in python"
     Intent: python_lists_tuples  | Confidence: 90% | Mood: NEUTRAL
[08] User Input: "What is binary search algorithm?"
     Intent: dsa_binary_search    | Confidence: 64% | Mood: NEUTRAL
[09] User Input: "What is deadlock in operating systems?"
     Intent: os_deadlock          | Confidence: 69% | Mood: NEUTRAL
[10] User Input: "Explain 7 layers of OSI model"
     Intent: cn_osi_model         | Confidence: 80% | Mood: NEUTRAL
[11] User Input: "What is ACID properties in DBMS?"
     Intent: dbms_acid            | Confidence: 76% | Mood: NEUTRAL
[12] User Input: "My name is Anup"
     Intent: set_name             | Confidence: 98% | Mood: NEUTRAL
[13] User Input: "Can you help me with exam stress?"
     Intent: motivation_exam_stress | Confidence: 51% | Mood: STRESSED_DEMOTIVATED
[14] User Input: "Thank you so much!"
     Intent: gratitude            | Confidence: 83% | Mood: POSITIVE

Test Summary: 14/14 test cases executed successfully!
Total Messages in Session Memory: 14
Topics Explored: [Computer Networks, Motivation & Wellness, Java, Humor & Fun, DSA, Operating Systems, General, DBMS, Python]
Final User Nickname: Anup
```

---

## 🎓 College Demonstration Script (Step-by-Step)

Follow this sequence for an impressive, professional presentation to professors and evaluators:

1. **Launch**: Run `run.bat`. Show the modern dark navy & purple interface, left navigation sidebar, and initial welcome greeting.
2. **Personalization**:
   - Type: `"My name is [YourName]"` or click **Edit** on the user profile card.
   - Show how the bot updates your nickname in the profile card and addresses you by name in subsequent messages.
3. **Core CS Assistant**:
   - Ask: `"What is DBMS?"` ➔ Highlight the clean definition and the `🎯 Match %` badge.
   - Click the follow-up suggestion: `Explain ACID properties in DBMS`.
   - Ask: `"Difference between list and tuple in python"` or `"Explain 7 layers of OSI model"`.
   - Show the `📋 Copy` button on the message and paste it into Notepad to prove clipboard integration.
4. **Typing Indicator**:
   - Show how the animated 3-dot typing indicator pulses before each response, providing a smooth conversational feel.
5. **Mood-Aware NLP**:
   - Type: `"I feel demotivated with my code bugs"`.
   - Highlight the empathetic response acknowledging stress and providing a step-by-step mental reset.
6. **Humor & Fun**:
   - Click the **Tell a Joke** button on the sidebar. Show how it cycles through clean developer humor.
7. **Handling Unknown Questions (Fallback)**:
   - Type an out-of-domain query: `"How to cook pancakes?"`.
   - Show that the bot transparently acknowledges low confidence, displays helpful fallback guidance, and suggests valid CS topics.
8. **Exporting History**:
   - Click **Export Chat History** in the sidebar. Save the transcript as `my_notes.txt`.
   - Open `my_notes.txt` to show full conversation logs with timestamps and confidence scores.

---

## 🔒 Offline Transparency Note
NeuroBuddy AI is an honest, explainable AI project designed for educational clarity. It implements rule-based NLP, Jaccard similarity, and keyword matching rather than opaque neural network black-boxes. Its modular architecture is structured so that machine-learning classification models (such as Naive Bayes, TF-IDF, or ONNX neural models) can easily replace or augment the `IntentMatcher` in future versions.

---

## 📄 License
This project is open-source and free to use for academic submissions, portfolio presentations, and educational projects.
