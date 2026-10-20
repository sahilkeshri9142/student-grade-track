package com.neurobuddy.ai.data;

import com.neurobuddy.ai.model.Intent;
import com.neurobuddy.ai.model.StudyTopic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Knowledge Base for NeuroBuddy AI.
 * Contains over 40 structured intents covering greetings, bot identity,
 * Computer Science study domains (Java, Python, DBMS, DSA, OS, Networks),
 * wellness/motivation, and programmer humor.
 */
public class KnowledgeBase {

    private final List<Intent> intents;

    public KnowledgeBase() {
        this.intents = new ArrayList<>();
        loadIntents();
    }

    public List<Intent> getIntents() {
        return Collections.unmodifiableList(intents);
    }

    private void addIntent(String id, StudyTopic topic, String description,
                           List<String> sampleQuestions,
                           Set<String> requiredKeywords,
                           Set<String> secondaryKeywords,
                           List<String> responses,
                           List<String> suggestedFollowUps,
                           double threshold) {
        intents.add(new Intent(id, topic, description, sampleQuestions,
                requiredKeywords, secondaryKeywords, responses, suggestedFollowUps, threshold));
    }

    private void loadIntents() {

        // ==========================================
        // 1. GREETINGS & CASUAL
        // ==========================================
        addIntent("greeting", StudyTopic.GENERAL, "User greeting",
                Arrays.asList("hello", "hi", "hey", "hey there", "good morning", "good evening", "namaste", "what's up", "howdy"),
                new HashSet<>(Arrays.asList("hello", "hi", "hey", "morning", "evening", "afternoon", "namaste", "howdy")),
                new HashSet<>(Arrays.asList("there", "buddy", "bot", "assistant")),
                Arrays.asList(
                        "Hey {name}! Welcome to NeuroBuddy AI. How can I help with your studies or coding today?",
                        "Hello {name}! Ready to master some computer science concepts today? What are you working on?",
                        "Hi there, {name}! Great to see you. Feel free to ask about Java, Python, DBMS, DSA, OS, or Computer Networks!"
                ),
                Arrays.asList("What is DBMS?", "Explain OOP concepts in Java", "Tell me a joke", "I feel demotivated"),
                0.40
        );

        addIntent("farewell", StudyTopic.GENERAL, "User saying goodbye",
                Arrays.asList("bye", "goodbye", "see you", "see you later", "exit", "quit", "cya", "talk to you soon"),
                new HashSet<>(Arrays.asList("bye", "goodbye", "cya", "farewell")),
                new HashSet<>(Arrays.asList("see", "later", "soon")),
                Arrays.asList(
                        "Goodbye {name}! Keep coding and keep learning. Have a wonderful day ahead!",
                        "Catch you later, {name}! Don't forget: consistency is the key to mastering Computer Science.",
                        "Bye for now! Whenever you get stuck with code or theory, NeuroBuddy will be right here for you."
                ),
                Arrays.asList("Hello", "Tell me a fun tech fact"),
                0.40
        );

        addIntent("gratitude", StudyTopic.GENERAL, "User expressing gratitude",
                Arrays.asList("thank you", "thanks", "thanks a lot", "thank you so much", "appreciate it", "you are awesome"),
                new HashSet<>(Arrays.asList("thank", "thanks", "appreciate")),
                new HashSet<>(Arrays.asList("lot", "much", "awesome", "helpful")),
                Arrays.asList(
                        "You're very welcome, {name}! I'm always glad to help. What would you like to explore next?",
                        "Happy to help, {name}! Asking great questions is the mark of a true engineer.",
                        "Anytime! Learning together makes difficult topics much easier."
                ),
                Arrays.asList("What is Normalization?", "Explain Binary Search", "Tell me a joke"),
                0.40
        );

        addIntent("bot_identity", StudyTopic.GENERAL, "Bot identity and origin",
                Arrays.asList("who are you", "what is your name", "what is neurobuddy", "who made you", "tell me about yourself", "are you an ai"),
                new HashSet<>(Arrays.asList("who", "what", "name", "identity", "yourself", "neurobuddy")),
                new HashSet<>(Arrays.asList("you", "bot", "ai", "assistant", "created", "made")),
                Arrays.asList(
                        "I am **NeuroBuddy AI** – your offline, intelligent conversational study assistant built with Core Java and Swing! I help college students and developers master CS core subjects without relying on paid APIs.",
                        "My name is **NeuroBuddy AI**! I'm designed using transparent rule-based NLP, tokenization, and confidence matching to assist you with academic topics, coding tips, and study motivation.",
                        "I'm **NeuroBuddy AI**, your pair-programming and revision partner! Think of me as a smart pocket tutor for Java, Python, DBMS, DSA, OS, and Computer Networks."
                ),
                Arrays.asList("What can you do?", "What topics can I learn?", "Explain ACID properties"),
                0.42
        );

        addIntent("bot_capabilities", StudyTopic.GENERAL, "Bot capabilities and features",
                Arrays.asList("what can you do", "features", "how do you work", "help", "commands", "options", "what topics do you know"),
                new HashSet<>(Arrays.asList("capabilities", "features", "help", "commands", "options", "work", "topics")),
                new HashSet<>(Arrays.asList("can", "do", "know", "show", "how")),
                Arrays.asList(
                        "Here is what I can do for you, {name}:\n" +
                        "1. 📚 **AI Study Assistant**: Answers in Java, Python, DBMS, DSA, Operating Systems, and Computer Networks.\n" +
                        "2. 💡 **Smart Intent & Mood Detection**: Understands question variations and offers supportive words when you feel stressed.\n" +
                        "3. 🧠 **Confidence Scoring**: Shows how accurately your question was matched.\n" +
                        "4. 📝 **Chat Export**: Save your revision notes anytime via the 'Export History' button!\n" +
                        "5. 😄 **Developer Humor**: Ready to share programmer jokes to brighten your study sessions.",
                        "I'm equipped with a 6-phase NLP pipeline:\n" +
                        "• Text Sanitization & Contraction Expansion\n" +
                        "• Tokenization & Stopword Elimination\n" +
                        "• Rule-Based Suffix Stemming & N-gram Extraction\n" +
                        "• Intent & Sentiment Keyword Matching with Confidence Scoring\n" +
                        "Try asking me any CS concept or hit the Quick Action buttons on the sidebar!"
                ),
                Arrays.asList("What is DBMS?", "Explain Big O notation", "Tell me a joke", "Clear Chat"),
                0.38
        );

        addIntent("change_name", StudyTopic.GENERAL, "Change user nickname",
                Arrays.asList("my name is", "call me", "change my name", "set my nickname", "i am called"),
                new HashSet<>(Arrays.asList("name", "nickname", "call")),
                new HashSet<>(Arrays.asList("my", "change", "set", "call")),
                Arrays.asList(
                        "Nice to officially meet you! You can update your nickname anytime by clicking the **'Change Nickname'** button in the sidebar.",
                        "Noted! If you'd like to update how I address you across all responses, click the profile card in the sidebar anytime."
                ),
                Arrays.asList("What is Java?", "Explain Process vs Thread"),
                0.45
        );

        // ==========================================
        // 2. STUDY ASSISTANT: JAVA
        // ==========================================
        addIntent("java_overview", StudyTopic.JAVA, "Java overview and platform independence",
                Arrays.asList("what is java", "why is java platform independent", "how does java work", "what is bytecode in java", "explain java architecture"),
                new HashSet<>(Arrays.asList("java", "bytecode", "platform")),
                new HashSet<>(Arrays.asList("independent", "overview", "work", "architecture", "jvm")),
                Arrays.asList(
                        "**Java** is a high-level, class-based, object-oriented programming language designed for portability (\"Write Once, Run Anywhere\" or WORA).\n\n" +
                        "• **Bytecode**: The `javac` compiler translates Java source code into platform-neutral `.class` bytecode instead of machine code.\n" +
                        "• **JVM**: The Java Virtual Machine interprets or JIT-compiles this bytecode into machine code native to the host OS.\n" +
                        "This separation of bytecode and JVM is what makes Java truly platform-independent!",
                        "Java achieves platform independence through its **Bytecode & JVM architecture**. While the JVM itself is platform-dependent (different builds for Windows, Mac, Linux), the compiled `.class` bytecode is universally portable across all JVMs."
                ),
                Arrays.asList("Difference between JDK, JRE and JVM?", "What are OOP concepts in Java?", "Explain Java Garbage Collector"),
                0.42
        );

        addIntent("java_oop", StudyTopic.JAVA, "Object Oriented Programming (OOP) concepts in Java",
                Arrays.asList("what is oop", "four pillars of oop", "explain oop concepts in java", "encapsulation inheritance polymorphism abstraction"),
                new HashSet<>(Arrays.asList("oop", "encapsulation", "inheritance", "polymorphism", "abstraction")),
                new HashSet<>(Arrays.asList("pillars", "concepts", "principles", "object", "oriented")),
                Arrays.asList(
                        "The **4 Core Pillars of OOP** in Java are:\n\n" +
                        "1. 🔒 **Encapsulation**: Bundling data (variables) and methods into a single class, keeping fields `private` and exposing public getters/setters.\n" +
                        "2. 🧬 **Inheritance**: Deriving a child class from a parent class (`extends`) to promote code reusability.\n" +
                        "3. 🎭 **Polymorphism**: Performing a single action in different ways (Compile-time via Method Overloading; Runtime via Method Overriding).\n" +
                        "4. 🎭 **Abstraction**: Hiding internal implementation complexities and showing only essential features using `abstract classes` and `interfaces`."
                ),
                Arrays.asList("Difference between ArrayList and LinkedList?", "What is Multithreading in Java?", "What is DBMS?"),
                0.40
        );

        addIntent("java_jdk_jre_jvm", StudyTopic.JAVA, "Difference between JDK, JRE, and JVM",
                Arrays.asList("difference between jdk jre and jvm", "jdk vs jre vs jvm", "what is jvm", "what is jre", "what is jdk"),
                new HashSet<>(Arrays.asList("jdk", "jre", "jvm")),
                new HashSet<>(Arrays.asList("difference", "vs", "between", "stands")),
                Arrays.asList(
                        "Here is the hierarchy of Java's runtime ecosystem:\n\n" +
                        "• **JVM (Java Virtual Machine)**: The abstract execution engine that loads, verifies, and executes bytecode. It includes the JIT (Just-In-Time) compiler and Garbage Collector.\n" +
                        "• **JRE (Java Runtime Environment)**: JVM + Core Standard Libraries (`rt.jar` etc.). It provides everything needed to **run** existing Java programs.\n" +
                        "• **JDK (Java Development Kit)**: JRE + Development Tools (`javac` compiler, `javadoc`, debugger `jdb`). It is required by developers to **write, compile, and run** Java software."
                ),
                Arrays.asList("Explain Java Garbage Collector", "What is Multithreading in Java?", "What is Java?"),
                0.42
        );

        addIntent("java_gc", StudyTopic.JAVA, "Java Garbage Collection and Memory Management",
                Arrays.asList("how does garbage collector work in java", "what is garbage collection in java", "gc in java", "heap and stack memory in java"),
                new HashSet<>(Arrays.asList("garbage", "gc", "heap", "stack")),
                new HashSet<>(Arrays.asList("collector", "collection", "memory", "destroy", "finalize")),
                Arrays.asList(
                        "**Garbage Collection (GC)** in Java is an automatic memory management process in the JVM that reclaims memory occupied by unreferenced objects on the **Heap**.\n\n" +
                        "• **Generational Heap Architecture**:\n" +
                        "  - *Young Generation* (Eden + Survivor spaces S0/S1): New objects are allocated here. Minor GC cleans short-lived objects.\n" +
                        "  - *Old (Tenured) Generation*: Objects that survive multiple GC cycles get promoted here. Cleaned by Major GC.\n" +
                        "• **Root Reachability**: If an object has no active references leading back to GC Roots (active stack threads, static variables), it is marked and swept automatically!"
                ),
                Arrays.asList("ArrayList vs LinkedList in Java?", "What is Multithreading in Java?", "What is Virtual Memory?"),
                0.42
        );

        addIntent("java_collections", StudyTopic.JAVA, "ArrayList vs LinkedList in Java Collections",
                Arrays.asList("difference between arraylist and linkedlist", "arraylist vs linkedlist", "when to use arraylist or linkedlist in java"),
                new HashSet<>(Arrays.asList("arraylist", "linkedlist")),
                new HashSet<>(Arrays.asList("difference", "vs", "between", "collections", "faster")),
                Arrays.asList(
                        "**ArrayList vs LinkedList in Java**:\n\n" +
                        "| Feature | ArrayList | LinkedList |\n" +
                        "|---|---|---|\n" +
                        "| **Underlying Structure** | Resizable Dynamic Array | Doubly-Linked List nodes |\n" +
                        "| **Random Access `get(i)`** | ⚡ Fast `O(1)` | 🐢 Slow `O(n)` traversal |\n" +
                        "| **Insertion/Deletion in middle** | 🐢 Slow `O(n)` (requires element shifting) | ⚡ Fast `O(1)` (once node reference is found) |\n" +
                        "| **Memory Overhead** | Low (continuous memory block) | High (stores prev/next pointers for every node) |\n\n" +
                        "👉 *Best Practice*: Use **ArrayList** for read-heavy operations, and **LinkedList** when you need frequent insertions/removals at beginning or middle."
                ),
                Arrays.asList("What is Stack and Queue?", "What is Big O notation?", "Explain Multithreading in Java"),
                0.42
        );

        addIntent("java_multithreading", StudyTopic.JAVA, "Multithreading and Thread Lifecycle in Java",
                Arrays.asList("what is multithreading in java", "thread lifecycle in java", "runnable vs thread in java", "how does multithreading work"),
                new HashSet<>(Arrays.asList("multithreading", "thread", "runnable")),
                new HashSet<>(Arrays.asList("lifecycle", "states", "concurrency", "synchronized", "parallel")),
                Arrays.asList(
                        "**Multithreading** in Java allows concurrent execution of two or more parts of a program for maximum CPU utilization.\n\n" +
                        "• **Ways to create a Thread**:\n" +
                        "  1. Extending `Thread` class and overriding `run()`.\n" +
                        "  2. Implementing `Runnable` interface (Recommended, as Java supports single class inheritance but multiple interfaces).\n" +
                        "• **Thread Lifecycle States**: `NEW` ➔ `RUNNABLE` ➔ `BLOCKED` / `WAITING` / `TIMED_WAITING` ➔ `TERMINATED`.\n" +
                        "• **Synchronization**: The `synchronized` keyword prevents race conditions by locking access to shared critical resources."
                ),
                Arrays.asList("Difference between Process and Thread in OS", "What is Deadlock?", "Explain OOP concepts in Java"),
                0.42
        );

        // ==========================================
        // 3. STUDY ASSISTANT: PYTHON
        // ==========================================
        addIntent("python_lists_tuples", StudyTopic.PYTHON, "Difference between List and Tuple in Python",
                Arrays.asList("difference between list and tuple in python", "list vs tuple in python", "mutable vs immutable python"),
                new HashSet<>(Arrays.asList("tuple", "list")),
                new HashSet<>(Arrays.asList("difference", "vs", "between", "python", "mutable", "immutable")),
                Arrays.asList(
                        "**List vs Tuple in Python**:\n\n" +
                        "• **Lists (`[...]`)**: Mutable (can append, modify, or delete elements in-place). Slightly higher memory overhead. Use when data needs to change dynamically.\n" +
                        "• **Tuples (`(...)`)**: Immutable (read-only after creation). Faster execution, lower memory overhead, and can be used as dictionary keys if all elements are hashable.\n\n" +
                        "Example:\n" +
                        "`my_list = [1, 2, 3]` -> `my_list[0] = 99` (Allowed)\n" +
                        "`my_tuple = (1, 2, 3)` -> `my_tuple[0] = 99` (Raises `TypeError`!)"
                ),
                Arrays.asList("What is GIL in Python?", "What are Python Decorators?", "Explain Shallow Copy vs Deep Copy"),
                0.42
        );

        addIntent("python_gil", StudyTopic.PYTHON, "Global Interpreter Lock (GIL) in Python",
                Arrays.asList("what is gil in python", "global interpreter lock", "why does python have gil", "does python support multithreading"),
                new HashSet<>(Arrays.asList("gil", "lock")),
                new HashSet<>(Arrays.asList("global", "interpreter", "cpython", "threads", "multithreading")),
                Arrays.asList(
                        "The **Global Interpreter Lock (GIL)** in CPython is a mutex that allows only **one native thread** to execute Python bytecode at a time, even on multi-core processors.\n\n" +
                        "• **Why it exists**: Memory management in CPython uses reference counting; the GIL prevents concurrent race conditions on reference counts.\n" +
                        "• **Impact**:\n" +
                        "  - *I/O-bound tasks* (web scraping, API calls, file reads) still benefit from threading because the GIL is released during I/O waits.\n" +
                        "  - *CPU-bound tasks* (data crunching, ML) require `multiprocessing` (separate processes with independent GILs) or C-extensions to achieve true multi-core speedup!"
                ),
                Arrays.asList("Difference between Process and Thread", "What are Python Decorators?", "List vs Tuple in Python"),
                0.40
        );

        addIntent("python_decorators", StudyTopic.PYTHON, "Python Decorators explained",
                Arrays.asList("what are decorators in python", "how do decorators work in python", "explain python decorators with example"),
                new HashSet<>(Arrays.asList("decorator", "decorators")),
                new HashSet<>(Arrays.asList("python", "function", "wrapper", "syntax")),
                Arrays.asList(
                        "A **Decorator** in Python is a design pattern and syntactic feature (`@decorator_name`) that lets you add new behavior to an existing function without modifying its source code.\n\n" +
                        "Because functions in Python are **first-class objects**, they can be passed as arguments, assigned to variables, and returned from other functions.\n\n" +
                        "```python\n" +
                        "def my_logger(func):\n" +
                        "    def wrapper(*args, **kwargs):\n" +
                        "        print(f\"Executing {func.__name__}...\")\n" +
                        "        return func(*args, **kwargs)\n" +
                        "    return wrapper\n\n" +
                        "@my_logger\n" +
                        "def greet(name): return f\"Hello {name}\"\n" +
                        "```\n" +
                        "Used heavily for authentication, logging, caching (`@lru_cache`), and timing execution!"
                ),
                Arrays.asList("What is GIL in Python?", "Difference between Deep Copy and Shallow Copy", "What is Java?"),
                0.42
        );

        addIntent("python_copy", StudyTopic.PYTHON, "Shallow Copy vs Deep Copy in Python",
                Arrays.asList("difference between deep copy and shallow copy", "shallow copy vs deep copy in python", "copy module in python"),
                new HashSet<>(Arrays.asList("copy", "deepcopy", "shallow")),
                new HashSet<>(Arrays.asList("difference", "vs", "between", "python", "nested")),
                Arrays.asList(
                        "**Shallow Copy vs Deep Copy in Python**:\n\n" +
                        "• **Shallow Copy (`copy.copy(obj)`)**: Constructs a new compound object, but inserts **references** to the child objects contained inside the original. If you modify a nested mutable list in the copy, it affects the original!\n" +
                        "• **Deep Copy (`copy.deepcopy(obj)`)**: Recursively clones the outer object AND copies every single nested object inside it. Modifying the deep copy will never affect the original."
                ),
                Arrays.asList("List vs Tuple in Python", "What is DBMS?", "What is Big O notation?"),
                0.42
        );

        // ==========================================
        // 4. STUDY ASSISTANT: DBMS
        // ==========================================
        addIntent("dbms_overview", StudyTopic.DBMS, "DBMS overview and architecture",
                Arrays.asList("what is dbms", "database management system", "why use dbms", "advantages of dbms", "what is a database"),
                new HashSet<>(Arrays.asList("dbms", "database")),
                new HashSet<>(Arrays.asList("what", "why", "advantages", "system", "management")),
                Arrays.asList(
                        "**DBMS (Database Management System)** is system software that enables users to define, create, maintain, and control access to structured databases efficiently.\n\n" +
                        "• **Key Advantages over File Processing Systems**:\n" +
                        "  1. Eliminates data redundancy and inconsistencies.\n" +
                        "  2. Provides ACID transaction security.\n" +
                        "  3. Enables concurrent multi-user access without data corruption.\n" +
                        "  4. Standardized querying via SQL.\n" +
                        "  5. Automated backup and recovery mechanisms."
                ),
                Arrays.asList("Explain ACID properties in DBMS", "What is Normalization?", "SQL vs NoSQL"),
                0.40
        );

        addIntent("dbms_acid", StudyTopic.DBMS, "ACID properties in DBMS transactions",
                Arrays.asList("what are acid properties in dbms", "explain acid properties", "atomicity consistency isolation durability", "acid properties with example"),
                new HashSet<>(Arrays.asList("acid", "atomicity", "durability")),
                new HashSet<>(Arrays.asList("properties", "dbms", "transaction", "consistency", "isolation")),
                Arrays.asList(
                        "**ACID Properties** guarantee reliable transaction processing in database systems:\n\n" +
                        "1. ⚡ **Atomicity (\"All or Nothing\")**: Either all operations of a transaction succeed, or the entire transaction is rolled back (e.g. if money is deducted from Account A, it must be credited to Account B).\n" +
                        "2. 🛡️ **Consistency**: The database transitions from one valid state to another, satisfying all schema constraints, foreign keys, and triggers.\n" +
                        "3. 🚪 **Isolation**: Concurrent transactions execute independently without interfering with each other (managed via lock mechanisms & isolation levels).\n" +
                        "4. 💾 **Durability**: Once a transaction is committed, its changes survive permanently in non-volatile storage, even during system crashes or power failures!"
                ),
                Arrays.asList("What is Normalization in DBMS?", "Difference between SQL and NoSQL?", "What are Database Indexes?"),
                0.40
        );

        addIntent("dbms_normalization", StudyTopic.DBMS, "Database Normalization (1NF, 2NF, 3NF, BCNF)",
                Arrays.asList("what is normalization in dbms", "explain 1nf 2nf 3nf bcnf", "why normalize database", "normal forms in dbms"),
                new HashSet<>(Arrays.asList("normalization", "1nf", "2nf", "3nf", "bcnf")),
                new HashSet<>(Arrays.asList("normal", "forms", "dbms", "redundancy", "anomalies")),
                Arrays.asList(
                        "**Normalization** is the process of organizing database relations to minimize data redundancy and eliminate insertion, update, and deletion anomalies.\n\n" +
                        "• **1NF (First Normal Form)**: Each table column must contain atomic (indivisible) values, and each record must be unique.\n" +
                        "• **2NF (Second Normal Form)**: Must be in 1NF + eliminate partial functional dependencies (all non-key attributes must depend on the whole primary key).\n" +
                        "• **3NF (Third Normal Form)**: Must be in 2NF + eliminate transitive dependencies (non-key attribute depends on another non-key attribute: `X -> Y -> Z`).\n" +
                        "• **BCNF (Boyce-Codd Normal Form)**: A stricter 3NF where for every functional dependency `X -> Y`, `X` must be a super key!"
                ),
                Arrays.asList("Explain ACID properties", "SQL vs NoSQL databases", "What are Database Indexes?"),
                0.40
        );

        addIntent("dbms_sql_nosql", StudyTopic.DBMS, "SQL vs NoSQL Databases",
                Arrays.asList("difference between sql and nosql", "sql vs nosql", "relational vs non relational database", "when to use nosql"),
                new HashSet<>(Arrays.asList("sql", "nosql")),
                new HashSet<>(Arrays.asList("difference", "vs", "between", "relational", "document", "mongodb", "mysql")),
                Arrays.asList(
                        "**SQL (Relational) vs NoSQL (Non-Relational) Databases**:\n\n" +
                        "| Characteristic | SQL (PostgreSQL, MySQL, Oracle) | NoSQL (MongoDB, Redis, Cassandra) |\n" +
                        "|---|---|---|\n" +
                        "| **Data Model** | Tabular with predefined fixed schemas | Document (JSON), Key-Value, Graph, Columnar |\n" +
                        "| **Scaling** | Vertical (scale-up with bigger CPU/RAM) | Horizontal (scale-out across distributed clusters) |\n" +
                        "| **Transactions** | Strict ACID compliance | BASE model (Eventual Consistency, CAP Theorem) |\n" +
                        "| **Best For** | Complex multi-table JOINs, financial data | High-velocity streaming, unstructured data, rapid schemas |"
                ),
                Arrays.asList("What is Indexing in DBMS?", "Explain ACID properties", "Difference between Array and Linked List"),
                0.42
        );

        addIntent("dbms_indexing", StudyTopic.DBMS, "Database Indexing and B-Trees",
                Arrays.asList("what is indexing in dbms", "how do database indexes work", "b tree index in dbms", "clustered vs non clustered index"),
                new HashSet<>(Arrays.asList("indexing", "index", "indexes")),
                new HashSet<>(Arrays.asList("dbms", "database", "btree", "clustered", "speed", "search")),
                Arrays.asList(
                        "An **Index** is a data structure (typically a **B-Tree** or **B+ Tree**) that allows the database engine to locate specific rows in `O(log n)` time instead of doing an expensive full table scan `O(n)`.\n\n" +
                        "• **Clustered Index**: Defines the actual physical ordering of records in storage. A table can have only ONE clustered index (usually the Primary Key).\n" +
                        "• **Non-Clustered Index**: Maintains a separate index structure where leaf nodes contain pointers (row locators) to data pages. A table can have multiple non-clustered indexes.\n" +
                        "⚠️ *Trade-off*: Indexes accelerate `SELECT` read queries, but slow down `INSERT`, `UPDATE`, and `DELETE` writes because the B-Tree must be rebalanced!"
                ),
                Arrays.asList("What is Normalization?", "Explain Binary Search in DSA", "What is Virtual Memory?"),
                0.40
        );

        // ==========================================
        // 5. STUDY ASSISTANT: DSA (Data Structures & Algorithms)
        // ==========================================
        addIntent("dsa_array_linkedlist", StudyTopic.DSA, "Array vs Linked List complexity and usage",
                Arrays.asList("difference between array and linked list", "array vs linked list in dsa", "when to use linked list over array"),
                new HashSet<>(Arrays.asList("array", "linkedlist", "linked")),
                new HashSet<>(Arrays.asList("difference", "vs", "between", "dsa", "complexity", "pointers")),
                Arrays.asList(
                        "**Array vs Linked List (DSA Comparison)**:\n\n" +
                        "• **Memory Allocation**: Arrays require contiguous blocks of memory allocated at once. Linked lists allocate individual nodes dynamically on the heap connected via pointers.\n" +
                        "• **Access Time**: Arrays provide instant `O(1)` random access via index arithmetic (`base_address + index * size`). Linked lists require sequential `O(n)` traversal.\n" +
                        "• **Insertion/Deletion**: Inserting at the head or middle of an array requires shifting elements `O(n)`. Linked list insertion at head is `O(1)` pointer reassignment!"
                ),
                Arrays.asList("What is Stack and Queue?", "Explain Big O notation", "How does Binary Search work?"),
                0.40
        );

        addIntent("dsa_stack_queue", StudyTopic.DSA, "Stack vs Queue data structures",
                Arrays.asList("what is stack and queue", "stack vs queue in dsa", "lifo vs fifo", "real world applications of stack and queue"),
                new HashSet<>(Arrays.asList("stack", "queue")),
                new HashSet<>(Arrays.asList("lifo", "fifo", "applications", "dsa", "difference", "pop", "push")),
                Arrays.asList(
                        "**Stack vs Queue**:\n\n" +
                        "1. 📚 **Stack (LIFO - Last In, First Out)**:\n" +
                        "   - Elements are inserted (`push`) and removed (`pop`) from the same end (the Top).\n" +
                        "   - *Applications*: Function call call-stack & recursion, Undo/Redo operations in editors, Balanced parenthesis checking.\n\n" +
                        "2. 🚶 **Queue (FIFO - First In, First Out)**:\n" +
                        "   - Elements enter at the `Rear` (`enqueue`) and exit from the `Front` (`dequeue`).\n" +
                        "   - *Applications*: CPU task scheduling, Printer spooling, Breadth-First Search (BFS) graph traversal."
                ),
                Arrays.asList("Difference between BFS and DFS", "Explain Big O notation", "How does Binary Search work?"),
                0.42
        );

        addIntent("dsa_big_o", StudyTopic.DSA, "Big-O Notation and Algorithm Complexity",
                Arrays.asList("what is big o notation", "explain time and space complexity", "big o time complexity chart", "what is time complexity"),
                new HashSet<>(Arrays.asList("big", "complexity", "o")),
                new HashSet<>(Arrays.asList("notation", "time", "space", "asymptotic", "worst", "case")),
                Arrays.asList(
                        "**Big-O Notation** measures the upper bound (worst-case scenario) of an algorithm's growth rate in runtime or memory as input size `n` approaches infinity.\n\n" +
                        "• `O(1)` - **Constant**: Direct array index access, hash map lookup.\n" +
                        "• `O(log n)` - **Logarithmic**: Binary Search, Balanced BST operations.\n" +
                        "• `O(n)` - **Linear**: Single loop, linear search.\n" +
                        "• `O(n log n)` - **Linearithmic**: Merge Sort, Heap Sort, Quick Sort (average).\n" +
                        "• `O(n^2)` - **Quadratic**: Nested loops, Bubble Sort, Selection Sort.\n" +
                        "• `O(2^n)` - **Exponential**: Recursive Fibonacci, generating all subsets."
                ),
                Arrays.asList("How does Binary Search work?", "Difference between BFS and DFS", "What is a Binary Search Tree?"),
                0.40
        );

        addIntent("dsa_binary_search", StudyTopic.DSA, "Binary Search Algorithm and Precondition",
                Arrays.asList("how does binary search work", "binary search algorithm", "time complexity of binary search", "precondition for binary search"),
                new HashSet<>(Arrays.asList("binary", "search")),
                new HashSet<>(Arrays.asList("algorithm", "sorted", "divide", "conquer", "middle", "complexity")),
                Arrays.asList(
                        "**Binary Search** is an efficient divide-and-conquer algorithm with `O(log n)` time complexity.\n\n" +
                        "🚨 **Mandatory Precondition**: The array MUST already be sorted!\n\n" +
                        "• **Step-by-Step Execution**:\n" +
                        "  1. Define two pointers: `low = 0` and `high = n - 1`.\n" +
                        "  2. Calculate midpoint: `mid = low + (high - low) / 2` (prevents integer overflow).\n" +
                        "  3. If `arr[mid] == target`, item found!\n" +
                        "  4. If `target < arr[mid]`, search left half (`high = mid - 1`).\n" +
                        "  5. If `target > arr[mid]`, search right half (`low = mid + 1`).\n" +
                        "Halves the search space every single iteration!"
                ),
                Arrays.asList("What is a Binary Search Tree?", "Difference between Array and Linked List", "Explain Big O notation"),
                0.42
        );

        addIntent("dsa_trees", StudyTopic.DSA, "Binary Search Tree (BST) vs Balanced Trees",
                Arrays.asList("what is a binary search tree", "bst vs avl tree", "properties of bst", "balanced tree in dsa"),
                new HashSet<>(Arrays.asList("bst", "tree", "avl")),
                new HashSet<>(Arrays.asList("binary", "search", "balanced", "height", "skewed")),
                Arrays.asList(
                        "A **Binary Search Tree (BST)** is a node-based binary tree data structure with the ordering invariant:\n\n" +
                        "• All nodes in the left subtree have keys strictly **less** than the node's key.\n" +
                        "• All nodes in the right subtree have keys strictly **greater** than the node's key.\n" +
                        "• In-order traversal (`Left -> Root -> Right`) visits keys in strictly sorted ascending order!\n\n" +
                        "⚠️ *Worst-case risk*: If elements are inserted in already-sorted order, BST degrades into a linked list of height `O(n)`. Self-balancing trees like **AVL** and **Red-Black Trees** perform rotations to enforce `O(log n)` height."
                ),
                Arrays.asList("Difference between BFS and DFS", "How does Binary Search work?", "Stack vs Queue"),
                0.42
        );

        addIntent("dsa_bfs_dfs", StudyTopic.DSA, "BFS vs DFS Graph Traversals",
                Arrays.asList("difference between bfs and dfs", "breadth first search vs depth first search", "bfs vs dfs in graphs"),
                new HashSet<>(Arrays.asList("bfs", "dfs")),
                new HashSet<>(Arrays.asList("breadth", "depth", "first", "search", "graph", "traversal")),
                Arrays.asList(
                        "**BFS (Breadth-First Search) vs DFS (Depth-First Search)**:\n\n" +
                        "• **BFS** explores neighbor nodes level by level using a **Queue (FIFO)**.\n" +
                        "  - *Optimal for*: Finding the shortest path on unweighted graphs, peer-to-peer networking.\n" +
                        "• **DFS** explores down a branch as deep as possible before backtracking using a **Stack / Recursion (LIFO)**.\n" +
                        "  - *Optimal for*: Topological sorting, cycle detection, solving mazes/puzzles, game trees.\n\n" +
                        "Both visit all vertices and edges in `O(V + E)` time."
                ),
                Arrays.asList("What is Stack and Queue?", "What is Big O notation?", "Difference between Process and Thread"),
                0.42
        );

        // ==========================================
        // 6. STUDY ASSISTANT: OPERATING SYSTEMS
        // ==========================================
        addIntent("os_process_thread", StudyTopic.OS, "Difference between Process and Thread",
                Arrays.asList("difference between process and thread", "process vs thread in os", "what is a process", "what is a thread"),
                new HashSet<>(Arrays.asList("process", "thread")),
                new HashSet<>(Arrays.asList("difference", "vs", "between", "os", "operating", "system", "pcb")),
                Arrays.asList(
                        "**Process vs Thread in Operating Systems**:\n\n" +
                        "• **Process**: An executing instance of a program in its own independent virtual memory space. It owns a dedicated Process Control Block (PCB), file descriptors, and heap. Inter-process communication (IPC) requires pipes or sockets.\n" +
                        "• **Thread**: A lightweight unit of execution within a parent process. Multiple threads share the same code segment, data segment, and open files, but maintain their own independent **Program Counter (PC), CPU registers, and Stack**.\n\n" +
                        "👉 *Key Benefit*: Context switching between threads is dramatically faster than between processes!"
                ),
                Arrays.asList("What is Deadlock in OS?", "Explain Virtual Memory and Paging", "What is Multithreading in Java?"),
                0.40
        );

        addIntent("os_deadlock", StudyTopic.OS, "Deadlock and the 4 Coffman Conditions",
                Arrays.asList("what is deadlock in operating system", "coffman conditions for deadlock", "how to prevent deadlock", "deadlock in os"),
                new HashSet<>(Arrays.asList("deadlock", "coffman")),
                new HashSet<>(Arrays.asList("os", "operating", "conditions", "prevention", "banker", "resource")),
                Arrays.asList(
                        "A **Deadlock** is a situation where a set of processes are blocked because each process is holding a resource and waiting for another resource acquired by someone else in the set.\n\n" +
                        "A deadlock can only occur if all **4 Coffman Conditions** hold simultaneously:\n" +
                        "1. 🔒 **Mutual Exclusion**: At least one resource is non-shareable.\n" +
                        "2. ⏳ **Hold and Wait**: A process holds at least one resource while requesting new ones.\n" +
                        "3. 🚫 **No Preemption**: Resources cannot be forcibly taken away until the process voluntarily releases them.\n" +
                        "4. 🔄 **Circular Wait**: A closed chain of processes exists where P0 waits for P1, P1 for P2, and Pn waits for P0.\n\n" +
                        "Breaking even *one* condition prevents deadlocks from ever happening!"
                ),
                Arrays.asList("Difference between Process and Thread", "What is Virtual Memory and Paging?", "What is CPU Scheduling?"),
                0.40
        );

        addIntent("os_virtual_memory", StudyTopic.OS, "Virtual Memory, Paging, and Page Faults",
                Arrays.asList("what is virtual memory and paging", "explain virtual memory in os", "what is a page fault", "paging in operating systems"),
                new HashSet<>(Arrays.asList("virtual", "paging", "page")),
                new HashSet<>(Arrays.asList("memory", "fault", "os", "mmu", "tlb", "ram")),
                Arrays.asList(
                        "**Virtual Memory** is a memory management technique that gives an application the illusion of having a vast, contiguous address space, even if physical RAM is limited.\n\n" +
                        "• **Paging**: Physical RAM is divided into fixed-size frames (typically 4 KB), and virtual address space is split into matching pages.\n" +
                        "• **Page Table & MMU**: The Memory Management Unit hardware translates virtual page addresses to physical frame addresses.\n" +
                        "• **Page Fault**: When a process accesses a page not currently residing in physical RAM, the CPU triggers an interrupt (Page Fault), causing the OS to fetch the page from disk swap space into RAM!"
                ),
                Arrays.asList("What is CPU Scheduling in OS?", "What is Deadlock?", "Explain Database Indexing"),
                0.40
        );

        addIntent("os_cpu_scheduling", StudyTopic.OS, "CPU Scheduling Algorithms (FCFS, SJF, Round Robin)",
                Arrays.asList("what is cpu scheduling", "cpu scheduling algorithms in os", "round robin vs fcfs vs sjf", "scheduling algorithms"),
                new HashSet<>(Arrays.asList("scheduling", "fcfs", "sjf", "round")),
                new HashSet<>(Arrays.asList("cpu", "robin", "preemptive", "turnaround", "burst", "os")),
                Arrays.asList(
                        "**CPU Scheduling** decides which ready process is allocated the CPU core by the short-term scheduler.\n\n" +
                        "• **FCFS (First-Come, First-Served)**: Non-preemptive. Simple FIFO queue, but suffers from the **Convoy Effect** (short jobs wait behind long jobs).\n" +
                        "• **SJF (Shortest Job First)**: Gives minimal average waiting time, but requires knowing burst times in advance and can starve long jobs.\n" +
                        "• **Round Robin (RR)**: Preemptive time-sharing. Each process gets a fixed slice of time (**Time Quantum**). Ideal for interactive multi-user OS environments!"
                ),
                Arrays.asList("Difference between Process and Thread", "What is Deadlock in OS?", "Explain OSI Model"),
                0.42
        );

        // ==========================================
        // 7. STUDY ASSISTANT: COMPUTER NETWORKS
        // ==========================================
        addIntent("cn_osi_model", StudyTopic.NETWORKS, "The 7 Layers of the OSI Model",
                Arrays.asList("explain 7 layers of osi model", "what is osi model in computer networks", "osi model layers with mnemonic", "seven layers of osi"),
                new HashSet<>(Arrays.asList("osi", "layers")),
                new HashSet<>(Arrays.asList("model", "seven", "network", "physical", "transport", "application")),
                Arrays.asList(
                        "The **OSI (Open Systems Interconnection) Model** has 7 conceptual layers:\n\n" +
                        "💡 *Mnemonic (Top-to-Bottom)*: **A**ll **P**eople **S**eem **T**o **N**eed **D**ata **P**rocessing\n\n" +
                        "7. **Application**: High-level network APIs for apps (HTTP, FTP, DNS, SMTP)\n" +
                        "6. **Presentation**: Data translation, encryption (SSL/TLS), and compression\n" +
                        "5. **Session**: Manages dialogue sessions between endpoints (RPC)\n" +
                        "4. **Transport**: End-to-end reliability, flow control, and ports (TCP, UDP)\n" +
                        "3. **Network**: Logical routing and addressing across networks (IP, Routers)\n" +
                        "2. **Data Link**: Node-to-node frame transfer via physical addresses (MAC, Switches)\n" +
                        "1. **Physical**: Raw bit transmission over physical media (Cables, Radio frequencies)"
                ),
                Arrays.asList("Difference between TCP and UDP?", "How does DNS work?", "Difference between HTTP and HTTPS"),
                0.40
        );

        addIntent("cn_tcp_udp", StudyTopic.NETWORKS, "TCP vs UDP protocols in Transport Layer",
                Arrays.asList("difference between tcp and udp", "tcp vs udp in computer networks", "when to use udp instead of tcp", "transmission control protocol vs user datagram protocol"),
                new HashSet<>(Arrays.asList("tcp", "udp")),
                new HashSet<>(Arrays.asList("difference", "vs", "between", "transport", "reliable", "handshake", "packet")),
                Arrays.asList(
                        "**TCP vs UDP (Transport Layer Protocols)**:\n\n" +
                        "| Feature | TCP (Transmission Control Protocol) | UDP (User Datagram Protocol) |\n" +
                        "|---|---|---|\n" +
                        "| **Connection** | Connection-oriented (3-way handshake SYN ➔ SYN-ACK ➔ ACK) | Connectionless (fire and forget) |\n" +
                        "| **Reliability** | Guaranteed delivery (acknowledgments & retransmissions) | No guarantees (packets may drop or arrive out of order) |\n" +
                        "| **Speed** | Slower (protocol overhead, congestion control) | Extremely fast with minimal header overhead |\n" +
                        "| **Use Cases** | Web browsing (HTTP/S), Email (SMTP), File transfer (FTP) | Video streaming, Online multiplayer gaming, DNS, VoIP |"
                ),
                Arrays.asList("How does DNS work?", "Explain 7 Layers of OSI Model", "HTTP vs HTTPS"),
                0.42
        );

        addIntent("cn_dns", StudyTopic.NETWORKS, "Domain Name System (DNS) resolution workflow",
                Arrays.asList("how does dns work", "domain name system explained", "dns lookup steps", "what is dns in computer networks"),
                new HashSet<>(Arrays.asList("dns", "domain")),
                new HashSet<>(Arrays.asList("how", "work", "lookup", "ip", "resolver", "server")),
                Arrays.asList(
                        "**DNS (Domain Name System)** translates human-readable domain names (like `google.com`) into computer-routable IP addresses (like `142.250.190.46`).\n\n" +
                        "• **The 4-Step Resolution Hierarchy**:\n" +
                        "  1. *Browser / Local Cache*: Checks if the IP is already cached locally.\n" +
                        "  2. *Recursive Resolver*: Your ISP or public DNS (e.g. `8.8.8.8`) handles the query.\n" +
                        "  3. *Root Nameserver (`.`)*: Directs the resolver to the appropriate Top-Level Domain (TLD) server.\n" +
                        "  4. *TLD Nameserver (`.com`)*: Directs resolver to the Authoritative Nameserver.\n" +
                        "  5. *Authoritative Nameserver*: Returns the exact `A` or `AAAA` IP record!"
                ),
                Arrays.asList("Difference between HTTP and HTTPS?", "Difference between IP and MAC address", "TCP vs UDP"),
                0.40
        );

        addIntent("cn_http_https", StudyTopic.NETWORKS, "HTTP vs HTTPS and SSL/TLS Handshake",
                Arrays.asList("difference between http and https", "why is https secure", "ssl tls handshake in https", "http vs https"),
                new HashSet<>(Arrays.asList("http", "https")),
                new HashSet<>(Arrays.asList("difference", "vs", "ssl", "tls", "security", "encryption", "port")),
                Arrays.asList(
                        "**HTTP vs HTTPS**:\n\n" +
                        "• **HTTP (Port 80)**: Hypertext Transfer Protocol sends data in plaintext. Vulnerable to packet sniffing and Man-in-the-Middle (MitM) attacks.\n" +
                        "• **HTTPS (Port 443)**: HTTP over **SSL/TLS**. Encrypts the entire payload using asymmetric encryption during the TLS handshake, followed by high-speed symmetric session keys.\n\n" +
                        "Provides the **Triple Security Guarantee**: Confidentiality (eavesdroppers see ciphertext), Integrity (data cannot be tampered with in transit), and Authentication (digital certificates verify server identity)!"
                ),
                Arrays.asList("Difference between IP and MAC address", "How does DNS work?", "Explain OSI Model"),
                0.42
        );

        addIntent("cn_ip_mac_port", StudyTopic.NETWORKS, "IP address vs MAC address vs Port number",
                Arrays.asList("difference between ip address and mac address", "ip vs mac vs port", "what is a mac address and ip address"),
                new HashSet<>(Arrays.asList("mac", "ip", "port")),
                new HashSet<>(Arrays.asList("difference", "address", "layer", "physical", "logical")),
                Arrays.asList(
                        "An intuitive analogy for **MAC, IP, and Port**:\n\n" +
                        "• 🏷️ **MAC Address (Data Link Layer - L2)**: Permanent physical identifier burned into the Network Interface Card (NIC) by manufacturer (e.g. `00:1A:2B:3C:4D:5E`). Like your **Government ID**.\n" +
                        "• 📬 **IP Address (Network Layer - L3)**: Dynamic logical address indicating where a device is connected in the global network (e.g. `192.168.1.15`). Like your current **Street Address**.\n" +
                        "• 🚪 **Port Number (Transport Layer - L4)**: Identifies the exact application service running on the host (e.g. Port 80 for web, Port 22 for SSH). Like the **Apartment Number** inside the building!"
                ),
                Arrays.asList("Explain 7 Layers of OSI Model", "TCP vs UDP", "How does DNS work?"),
                0.40
        );

        // ==========================================
        // 8. WELLNESS & STUDY MOTIVATION
        // ==========================================
        addIntent("motivation_demotivated", StudyTopic.MOTIVATION, "Handling demotivation and imposter syndrome",
                Arrays.asList("i feel demotivated", "feeling down", "i want to give up", "lost motivation", "i feel dumb", "imposter syndrome"),
                new HashSet<>(Arrays.asList("demotivated", "give", "imposter", "hopeless", "sad", "unmotivated")),
                new HashSet<>(Arrays.asList("feel", "down", "give up", "worthless", "stupid", "quit")),
                Arrays.asList(
                        "It is completely okay to have difficult days, {name}. Computer science and programming are inherently challenging, and every senior software engineer you admire once felt stuck just like this.\n\n" +
                        "💡 **NeuroBuddy's 3-Step Reset**:\n" +
                        "1. **Step away for 15 minutes**: Take a walk or drink a glass of water to clear mental cache.\n" +
                        "2. **Decompose the challenge**: Don't try to solve the whole project at once. Pick ONE tiny function or definition.\n" +
                        "3. **Remember**: Confusion is not failure — it's the exact sensation of your brain expanding its neural pathways. You've made progress already!",
                        "Hey {name}, give yourself some credit! You're learning complex concepts that take months to click. Rome wasn't built in a day, and neither is great code. Pick one small victory today, celebrate it, and let tomorrow take care of the rest."
                ),
                Arrays.asList("Tell me a joke", "Exam stress tips", "Explain Big O notation"),
                0.38
        );

        addIntent("motivation_exam_stress", StudyTopic.MOTIVATION, "Exam stress and effective study habits",
                Arrays.asList("exam stress", "how to study effectively", "study tips", "stressed about exams", "pomodoro technique"),
                new HashSet<>(Arrays.asList("exam", "exams", "study", "pomodoro")),
                new HashSet<>(Arrays.asList("stress", "stressed", "tips", "techniques", "prepare", "revision")),
                Arrays.asList(
                        "Here are 4 evidence-backed study strategies for tech exams, {name}:\n\n" +
                        "1. 🍅 **The Pomodoro Technique**: Study for 25 minutes of unbroken focus, followed by a 5-minute break. After 4 sessions, take a longer 20-minute rest.\n" +
                        "2. 🔄 **Active Recall over Passive Reading**: Instead of re-reading slides, close the book and write down core definitions or draw architecture diagrams from memory.\n" +
                        "3. 👨‍🏫 **The Feynman Technique**: Explain the concept (like ACID or Binary Search) in simple words as if teaching a beginner.\n" +
                        "4. 🛌 **Prioritize Sleep**: Memory consolidation during sleep is scientifically proven to improve recall by over 40%!"
                ),
                Arrays.asList("I feel demotivated", "Tell me a joke", "What is ACID properties?"),
                0.38
        );

        addIntent("motivation_coding_bug", StudyTopic.MOTIVATION, "Frustration with code bugs and errors",
                Arrays.asList("frustrated with code", "bug won't fix", "cant fix my code error", "my code is broken", "error in my program"),
                new HashSet<>(Arrays.asList("bug", "error", "broken", "debug")),
                new HashSet<>(Arrays.asList("fix", "code", "frustrated", "cant", "solve")),
                Arrays.asList(
                        "Take a deep breath, {name}! As the saying goes: *\"Debugging is twice as hard as writing the code in the first place.\"*\n\n" +
                        "🔍 **Proven Debugging Checklist**:\n" +
                        "1. Read the **first line** of the stack trace carefully — it tells you the exact exception and line number!\n" +
                        "2. Print out the input variables immediately before the failing line.\n" +
                        "3. Rubber duck debugging: Explain out loud what each line is supposed to do.\n" +
                        "4. Check for null pointer references, out-of-bounds indices, and uninitialized objects."
                ),
                Arrays.asList("Tell me a joke", "What is Multithreading in Java?", "Difference between List and Tuple"),
                0.38
        );

        // ==========================================
        // 9. HUMOR & FUN
        // ==========================================
        addIntent("jokes_programming", StudyTopic.JOKES, "Programmer jokes and lighthearted laughs",
                Arrays.asList("tell me a joke", "programmer joke", "coding joke", "funny developer joke", "make me laugh"),
                new HashSet<>(Arrays.asList("joke", "jokes", "laugh", "funny")),
                new HashSet<>(Arrays.asList("tell", "programmer", "coding", "developer")),
                Arrays.asList(
                        "Why do programmers prefer dark mode?\n👉 **Because light attracts bugs!** 🐛",
                        "There are 10 types of people in the world:\n👉 **Those who understand binary, and those who don't.**",
                        "Why did the programmer quit their job?\n👉 **Because they didn't get arrays (a raise)!**",
                        "A SQL query walks into a bar, walks up to two tables, and asks:\n👉 **\"Can I join you?\"** 🍻",
                        "How many programmers does it take to change a light bulb?\n👉 **None. It's a hardware problem!**",
                        "Why do Java programmers have to wear glasses?\n👉 **Because they don't C#!**"
                ),
                Arrays.asList("Tell me another joke", "Tell me a fun tech fact", "What is Java?"),
                0.38
        );

        addIntent("tech_trivia", StudyTopic.JOKES, "Fun tech facts and trivia",
                Arrays.asList("tell me a fun tech fact", "fun fact", "trivia", "did you know", "tech facts"),
                new HashSet<>(Arrays.asList("fact", "trivia", "interesting")),
                new HashSet<>(Arrays.asList("tech", "fun", "know", "tell")),
                Arrays.asList(
                        "🧠 **Did you know?** The first computer bug was an actual real bug! In 1947, computer pioneer Grace Hopper found a moth trapped between relays of the Harvard Mark II computer.",
                        "☕ **Did you know?** Java was originally called **Oak**, named after an oak tree that stood outside James Gosling's office! It was later renamed Java after Peet's coffee.",
                        "🌐 **Did you know?** The first domain name ever registered in internet history was `symbolics.com` on March 15, 1985."
                ),
                Arrays.asList("Tell me a joke", "What is DBMS?", "Explain OSI Model"),
                0.38
        );
    }
}
