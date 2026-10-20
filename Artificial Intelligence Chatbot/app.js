/**
 * NeuroBuddy AI – Frontend Client & Offline Rule-Based NLP Engine
 * 100% Offline-capable, zero external API dependencies.
 */

(function () {
    'use strict';

    // =========================================================================
    // 1. NLP Processor
    // =========================================================================
    const STOPWORDS = new Set([
        "a", "an", "the", "and", "or", "is", "are", "was", "were", "be", "been",
        "being", "have", "has", "had", "do", "does", "did", "to", "at", "by",
        "for", "with", "about", "into", "through", "during", "before", "after",
        "above", "below", "from", "up", "down", "in", "out", "on", "off", "over",
        "under", "again", "further", "then", "once", "here", "there", "any", "both",
        "each", "few", "more", "most", "other", "some", "such", "no", "nor", "not",
        "only", "own", "same", "so", "than", "too", "very", "can", "will", "just",
        "should", "now", "please", "kindly", "tell", "give", "me", "know"
    ]);

    function cleanText(raw) {
        if (!raw) return "";
        let text = raw.toLowerCase().trim();
        text = text.replace(/what's/g, "what is")
                   .replace(/who's/g, "who is")
                   .replace(/how's/g, "how is")
                   .replace(/it's/g, "it is")
                   .replace(/that's/g, "that is")
                   .replace(/i'm/g, "i am")
                   .replace(/you're/g, "you are")
                   .replace(/can't/g, "cannot")
                   .replace(/won't/g, "will not")
                   .replace(/don't/g, "do not")
                   .replace(/doesn't/g, "does not")
                   .replace(/didn't/g, "did not")
                   .replace(/tell me/g, "explain")
                   .replace(/explain to me/g, "explain")
                   .replace(/\bdsa\b/g, "data structures and algorithms dsa");

        text = text.replace(/[^a-zA-Z0-9\s]/g, " ");
        text = text.replace(/\s+/g, " ").trim();
        return text;
    }

    function tokenize(clean) {
        if (!clean) return [];
        return clean.split(/\s+/).filter(t => t.length > 0);
    }

    function stem(word) {
        if (!word || word.length <= 3) return word;
        if (word.endsWith("ies") && word.length > 4) return word.slice(0, -3) + "y";
        if (word.endsWith("es") && (word.endsWith("sses") || word.endsWith("shes") || word.endsWith("ches"))) {
            return word.slice(0, -2);
        }
        if (word.endsWith("s") && !word.endsWith("ss") && !word.endsWith("is") && !word.endsWith("us")) {
            return word.slice(0, -1);
        }
        if (word.endsWith("ing") && word.length > 5) return word.slice(0, -3);
        if (word.endsWith("ed") && word.length > 4) return word.slice(0, -2);
        return word;
    }

    function getStemmedSet(tokens) {
        const stems = new Set();
        tokens.forEach(t => {
            stems.add(t);
            stems.add(stem(t));
        });
        return stems;
    }

    function extractNGrams(tokens, n) {
        const ngrams = [];
        if (tokens.length < n) return ngrams;
        for (let i = 0; i <= tokens.length - n; i++) {
            ngrams.push(tokens.slice(i, i + n).join(" "));
        }
        return ngrams;
    }

    function jaccardSimilarity(set1, set2) {
        if (set1.size === 0 && set2.size === 0) return 1.0;
        if (set1.size === 0 || set2.size === 0) return 0.0;
        let intersect = 0;
        set1.forEach(item => { if (set2.has(item)) intersect++; });
        const union = set1.size + set2.size - intersect;
        return union === 0 ? 0.0 : intersect / union;
    }

    function levenshteinDistance(s1, s2) {
        if (s1 === s2) return 0;
        const len1 = s1.length;
        const len2 = s2.length;
        if (len1 === 0) return len2;
        if (len2 === 0) return len1;

        const dp = Array.from({ length: len1 + 1 }, () => new Array(len2 + 1).fill(0));
        for (let i = 0; i <= len1; i++) dp[i][0] = i;
        for (let j = 0; j <= len2; j++) dp[0][j] = j;

        for (let i = 1; i <= len1; i++) {
            for (let j = 1; j <= len2; j++) {
                const cost = s1[i - 1] === s2[j - 1] ? 0 : 1;
                dp[i][j] = Math.min(
                    dp[i - 1][j] + 1,
                    dp[i][j - 1] + 1,
                    dp[i - 1][j - 1] + cost
                );
            }
        }
        return dp[len1][len2];
    }

    // =========================================================================
    // 2. Sentiment Analyzer
    // =========================================================================
    const POSITIVE_WORDS = new Set(["happy", "great", "awesome", "good", "amazing", "love", "excited", "excellent", "wonderful", "cool", "fantastic", "yay", "super", "glad", "enjoy", "brilliant", "perfect", "thank"]);
    const FRUSTRATED_WORDS = new Set(["frustrated", "bug", "bugs", "error", "errors", "hate", "annoying", "annoyed", "angry", "mad", "broken", "failing", "fails", "crash", "crashed", "stuck", "fed up"]);
    const STRESSED_WORDS = new Set(["demotivated", "depressed", "sad", "stressed", "stress", "overwhelmed", "exhausted", "tired", "quit", "give up", "giving up", "hopeless", "nervous", "anxious", "scared", "fear", "failure", "imposter", "burnout"]);
    const CONFUSED_WORDS = new Set(["confused", "lost", "complicated", "unclear", "hard", "difficult", "tough", "clueless", "puzzled"]);

    function analyzeSentiment(cleanText, tokens) {
        let pos = 0, frust = 0, stress = 0, conf = 0;
        tokens.forEach(t => {
            if (POSITIVE_WORDS.has(t)) pos++;
            if (FRUSTRATED_WORDS.has(t)) frust++;
            if (STRESSED_WORDS.has(t)) stress++;
            if (CONFUSED_WORDS.has(t)) conf++;
        });

        if (cleanText.includes("give up") || cleanText.includes("feel down")) stress += 2;
        if (cleanText.includes("not working") || cleanText.includes("does not work")) frust += 2;
        if (cleanText.includes("do not understand")) conf += 2;

        if (stress > 0 && stress >= frust && stress >= pos) {
            return {
                type: "STRESSED_DEMOTIVATED",
                message: "I hear that you might be feeling overwhelmed or demotivated. Take a deep breath — learning takes time, and every programmer experiences tough days. You've got this!"
            };
        }
        if (frust > 0 && frust >= pos) {
            return {
                type: "FRUSTRATED",
                message: "Debugging and code errors can definitely feel frustrating! Let's tackle it methodically together. What specific part is causing issues?"
            };
        }
        if (conf > 0 && conf >= pos) {
            return {
                type: "CONFUSED",
                message: "Computer science topics can feel like a maze at first glance! No worries at all — we can break it down into simple, digestible steps."
            };
        }
        if (pos > 0) {
            return {
                type: "POSITIVE",
                message: "I love that positive energy! Keep that learning momentum going!"
            };
        }
        return { type: "NEUTRAL", message: null };
    }

    // =========================================================================
    // 3. Knowledge Base (40+ Predefined Intents)
    // =========================================================================
    const KNOWLEDGE_BASE = [
        // Greetings
        {
            id: "greeting",
            topic: "General",
            samples: ["hello", "hi", "hey", "good morning", "good evening", "namaste", "what's up", "howdy"],
            required: ["hello", "hi", "hey", "morning", "evening", "namaste", "howdy"],
            secondary: ["there", "buddy", "bot", "assistant"],
            threshold: 0.40,
            responses: [
                "Hey {name}! Welcome to NeuroBuddy AI. How can I help with your studies or coding today?",
                "Hello {name}! Ready to master some computer science concepts today? What are you working on?",
                "Hi there, {name}! Great to see you. Feel free to ask about Java, Python, DBMS, DSA, OS, or Computer Networks!"
            ],
            suggestions: ["What is DBMS?", "Explain OOP concepts in Java", "Tell me a joke", "I feel demotivated"]
        },
        {
            id: "farewell",
            topic: "General",
            samples: ["bye", "goodbye", "see you later", "exit", "quit", "cya", "talk to you soon"],
            required: ["bye", "goodbye", "cya", "farewell"],
            secondary: ["see", "later", "soon"],
            threshold: 0.40,
            responses: [
                "Goodbye {name}! Keep coding and keep learning. Have a wonderful day ahead!",
                "Catch you later, {name}! Don't forget: consistency is the key to mastering Computer Science."
            ],
            suggestions: ["Hello", "Tell me a fun tech fact"]
        },
        {
            id: "gratitude",
            topic: "General",
            samples: ["thank you", "thanks", "thanks a lot", "thank you so much", "appreciate it", "you are awesome"],
            required: ["thank", "thanks", "appreciate"],
            secondary: ["lot", "much", "awesome", "helpful"],
            threshold: 0.40,
            responses: [
                "You're very welcome, {name}! I'm always glad to help. What would you like to explore next?",
                "Happy to help, {name}! Asking great questions is the mark of a true engineer.",
                "Anytime! Learning together makes difficult topics much easier."
            ],
            suggestions: ["What is Normalization?", "Explain Binary Search", "Tell me a joke"]
        },
        {
            id: "bot_identity",
            topic: "General",
            samples: ["who are you", "what is your name", "what is neurobuddy", "who made you", "tell me about yourself"],
            required: ["who", "what", "name", "identity", "yourself", "neurobuddy"],
            secondary: ["you", "bot", "ai", "assistant", "created", "made"],
            threshold: 0.42,
            responses: [
                "I am **NeuroBuddy AI** – your offline, intelligent conversational study assistant! I help college students and developers master CS core subjects without relying on paid APIs.",
                "My name is **NeuroBuddy AI**! I'm designed using transparent rule-based NLP, tokenization, and confidence matching to assist you with academic topics, coding tips, and study motivation."
            ],
            suggestions: ["What can you do?", "What topics can I learn?", "Explain ACID properties"]
        },
        {
            id: "bot_capabilities",
            topic: "General",
            samples: ["what can you do", "features", "how do you work", "help", "commands", "options", "topics"],
            required: ["capabilities", "features", "help", "commands", "options", "work", "topics"],
            secondary: ["can", "do", "know", "show", "how"],
            threshold: 0.38,
            responses: [
                "Here is what I can do for you, {name}:\n1. 📚 **AI Study Assistant**: Expert answers in Java, Python, DBMS, DSA, Operating Systems, and Computer Networks.\n2. 💡 **Smart Intent & Mood Detection**: Understands question variations and offers supportive words when you feel stressed.\n3. 🧠 **Confidence Scoring**: Shows how accurately your question was matched.\n4. 📝 **Chat Export**: Save your revision notes anytime via the 'Export Chat History' button!\n5. 😄 **Developer Humor**: Ready to share programmer jokes to brighten your study sessions."
            ],
            suggestions: ["What is DBMS?", "Explain Big O notation", "Tell me a joke", "Clear Chat"]
        },
        // Java
        {
            id: "java_overview",
            topic: "Java",
            samples: ["what is java", "why is java platform independent", "how does java work", "what is bytecode in java"],
            required: ["java", "bytecode", "platform"],
            secondary: ["independent", "overview", "work", "architecture", "jvm"],
            threshold: 0.42,
            responses: [
                "**Java** is a high-level, class-based, object-oriented programming language designed for portability (\"Write Once, Run Anywhere\" or WORA).\n\n• **Bytecode**: The `javac` compiler translates Java source code into platform-neutral `.class` bytecode instead of machine code.\n• **JVM**: The Java Virtual Machine interprets or JIT-compiles this bytecode into machine code native to the host OS.\n\nThis separation of bytecode and JVM is what makes Java truly platform-independent!"
            ],
            suggestions: ["Difference between JDK, JRE and JVM?", "What are OOP concepts in Java?", "Explain Java Garbage Collector"]
        },
        {
            id: "java_oop",
            topic: "Java",
            samples: ["what is oop", "four pillars of oop", "explain oop concepts in java", "encapsulation inheritance polymorphism abstraction"],
            required: ["oop", "encapsulation", "inheritance", "polymorphism", "abstraction"],
            secondary: ["pillars", "concepts", "principles", "object", "oriented"],
            threshold: 0.40,
            responses: [
                "The **4 Core Pillars of OOP** in Java are:\n\n1. 🔒 **Encapsulation**: Bundling data (variables) and methods into a single class, keeping fields `private` and exposing public getters/setters.\n2. 🧬 **Inheritance**: Deriving a child class from a parent class (`extends`) to promote code reusability.\n3. 🎭 **Polymorphism**: Performing a single action in different ways (Compile-time via Overloading; Runtime via Overriding).\n4. 🎭 **Abstraction**: Hiding internal implementation complexities and showing only essential features using `abstract classes` and `interfaces`."
            ],
            suggestions: ["Difference between ArrayList and LinkedList?", "What is Multithreading in Java?", "What is DBMS?"]
        },
        {
            id: "java_jdk_jre_jvm",
            topic: "Java",
            samples: ["difference between jdk jre and jvm", "jdk vs jre vs jvm", "what is jvm", "what is jre", "what is jdk"],
            required: ["jdk", "jre", "jvm"],
            secondary: ["difference", "vs", "between", "stands"],
            threshold: 0.42,
            responses: [
                "Here is the hierarchy of Java's runtime ecosystem:\n\n• **JVM (Java Virtual Machine)**: The abstract execution engine that loads, verifies, and executes bytecode. It includes the JIT compiler and Garbage Collector.\n• **JRE (Java Runtime Environment)**: JVM + Core Standard Libraries (`rt.jar` etc.). It provides everything needed to **run** existing Java programs.\n• **JDK (Java Development Kit)**: JRE + Development Tools (`javac` compiler, `javadoc`, debugger `jdb`). It is required by developers to **write, compile, and run** Java software."
            ],
            suggestions: ["Explain Java Garbage Collector", "What is Multithreading in Java?", "What is Java?"]
        },
        {
            id: "java_gc",
            topic: "Java",
            samples: ["how does garbage collector work in java", "what is garbage collection in java", "gc in java", "heap and stack memory in java"],
            required: ["garbage", "gc", "heap", "stack"],
            secondary: ["collector", "collection", "memory", "destroy", "finalize"],
            threshold: 0.42,
            responses: [
                "**Garbage Collection (GC)** in Java is an automatic memory management process in the JVM that reclaims memory occupied by unreferenced objects on the **Heap**.\n\n• **Generational Heap Architecture**:\n  - *Young Generation* (Eden + Survivor S0/S1): New objects are allocated here. Cleaned by Minor GC.\n  - *Old Generation*: Objects that survive multiple GC cycles get promoted here. Cleaned by Major GC.\n• **Root Reachability**: If an object has no active references leading back to GC Roots (active stack threads, static variables), it is marked and swept automatically!"
            ],
            suggestions: ["ArrayList vs LinkedList in Java?", "What is Multithreading in Java?", "What is Virtual Memory?"]
        },
        {
            id: "java_collections",
            topic: "Java",
            samples: ["difference between arraylist and linkedlist", "arraylist vs linkedlist in java", "when to use arraylist or linkedlist"],
            required: ["arraylist", "linkedlist"],
            secondary: ["difference", "vs", "between", "collections", "faster"],
            threshold: 0.42,
            responses: [
                "**ArrayList vs LinkedList in Java**:\n\n• **ArrayList**: Backed by a dynamic array. Fast random access `O(1)` via index, but slow insertions/deletions in the middle `O(n)` because elements must be shifted.\n• **LinkedList**: Backed by doubly-linked nodes. Slow random access `O(n)` (must traverse from head/tail), but fast insertions/deletions `O(1)` once reference is found.\n\n👉 *Rule of Thumb*: Use **ArrayList** for read-heavy operations, and **LinkedList** for frequent head/middle modifications."
            ],
            suggestions: ["What is Stack and Queue?", "What is Big O notation?", "Explain Multithreading in Java"]
        },
        {
            id: "java_multithreading",
            topic: "Java",
            samples: ["what is multithreading in java", "thread lifecycle in java", "runnable vs thread in java"],
            required: ["multithreading", "thread", "runnable"],
            secondary: ["lifecycle", "states", "concurrency", "synchronized"],
            threshold: 0.42,
            responses: [
                "**Multithreading** in Java allows concurrent execution of two or more parts of a program for maximum CPU utilization.\n\n• **Creating Threads**: Extending `Thread` class or implementing `Runnable` interface (recommended to preserve class inheritance).\n• **Thread Lifecycle**: `NEW` ➔ `RUNNABLE` ➔ `BLOCKED`/`WAITING` ➔ `TERMINATED`.\n• **Synchronization**: The `synchronized` keyword prevents race conditions by locking access to shared critical resources."
            ],
            suggestions: ["Difference between Process and Thread in OS", "What is Deadlock?", "Explain OOP concepts in Java"]
        },
        // Python
        {
            id: "python_lists_tuples",
            topic: "Python",
            samples: ["difference between list and tuple in python", "list vs tuple in python", "mutable vs immutable python"],
            required: ["tuple", "list"],
            secondary: ["difference", "vs", "between", "python", "mutable", "immutable"],
            threshold: 0.42,
            responses: [
                "**List vs Tuple in Python**:\n\n• **Lists (`[...]`)**: Mutable (can append, modify, or delete elements in-place). Slightly higher memory overhead. Use when data needs to change dynamically.\n• **Tuples (`(...)`)**: Immutable (read-only after creation). Faster execution, lower memory overhead, and can be used as dictionary keys if all elements are hashable.\n\nExample:\n`my_list = [1, 2, 3]` -> `my_list[0] = 99` (Allowed)\n`my_tuple = (1, 2, 3)` -> `my_tuple[0] = 99` (Raises `TypeError`!)"
            ],
            suggestions: ["What is GIL in Python?", "What are Python Decorators?", "Explain Shallow Copy vs Deep Copy"]
        },
        {
            id: "python_gil",
            topic: "Python",
            samples: ["what is gil in python", "global interpreter lock", "why does python have gil"],
            required: ["gil", "lock"],
            secondary: ["global", "interpreter", "cpython", "threads", "multithreading"],
            threshold: 0.40,
            responses: [
                "The **Global Interpreter Lock (GIL)** in CPython is a mutex that allows only **one native thread** to execute Python bytecode at a time, even on multi-core processors.\n\n• **Why it exists**: Memory management in CPython uses reference counting; the GIL prevents concurrent race conditions on reference counts.\n• **Impact**:\n  - *I/O-bound tasks* (web scraping, API calls, file reads) still benefit from threading because the GIL is released during I/O waits.\n  - *CPU-bound tasks* (data crunching, ML) require `multiprocessing` to achieve true multi-core speedup!"
            ],
            suggestions: ["Difference between Process and Thread", "What are Python Decorators?", "List vs Tuple in Python"]
        },
        {
            id: "python_decorators",
            topic: "Python",
            samples: ["what are decorators in python", "how do decorators work in python", "explain python decorators"],
            required: ["decorator", "decorators"],
            secondary: ["python", "function", "wrapper", "syntax"],
            threshold: 0.42,
            responses: [
                "A **Decorator** in Python is a design pattern and syntactic feature (`@decorator_name`) that lets you add new behavior to an existing function without modifying its source code.\n\nBecause functions in Python are **first-class objects**, they can be passed as arguments, assigned to variables, and returned from other functions.\n\nUsed heavily for authentication, logging, caching (`@lru_cache`), and timing execution!"
            ],
            suggestions: ["What is GIL in Python?", "Difference between Deep Copy and Shallow Copy", "What is Java?"]
        },
        // DBMS
        {
            id: "dbms_overview",
            topic: "DBMS",
            samples: ["what is dbms", "database management system", "why use dbms", "advantages of dbms"],
            required: ["dbms", "database"],
            secondary: ["what", "why", "advantages", "system", "management"],
            threshold: 0.40,
            responses: [
                "**DBMS (Database Management System)** is system software that enables users to define, create, maintain, and control access to structured databases efficiently.\n\n• **Key Advantages over File Systems**:\n  1. Eliminates data redundancy and inconsistencies.\n  2. Provides ACID transaction security.\n  3. Enables concurrent multi-user access without data corruption.\n  4. Standardized querying via SQL.\n  5. Automated backup and recovery mechanisms."
            ],
            suggestions: ["Explain ACID properties in DBMS", "What is Normalization?", "SQL vs NoSQL"]
        },
        {
            id: "dbms_acid",
            topic: "DBMS",
            samples: ["what are acid properties in dbms", "explain acid properties", "atomicity consistency isolation durability"],
            required: ["acid", "atomicity", "durability"],
            secondary: ["properties", "dbms", "transaction", "consistency", "isolation"],
            threshold: 0.40,
            responses: [
                "**ACID Properties** guarantee reliable transaction processing in database systems:\n\n1. ⚡ **Atomicity (\"All or Nothing\")**: Either all operations of a transaction succeed, or the entire transaction is rolled back.\n2. 🛡️ **Consistency**: The database transitions from one valid state to another, satisfying all schema constraints and foreign keys.\n3. 🚪 **Isolation**: Concurrent transactions execute independently without interfering with each other.\n4. 💾 **Durability**: Once a transaction is committed, its changes survive permanently in non-volatile storage, even during system crashes!"
            ],
            suggestions: ["What is Normalization in DBMS?", "Difference between SQL and NoSQL?", "What are Database Indexes?"]
        },
        {
            id: "dbms_normalization",
            topic: "DBMS",
            samples: ["what is normalization in dbms", "explain 1nf 2nf 3nf bcnf", "why normalize database"],
            required: ["normalization", "1nf", "2nf", "3nf", "bcnf"],
            secondary: ["normal", "forms", "dbms", "redundancy", "anomalies"],
            threshold: 0.40,
            responses: [
                "**Normalization** is the process of organizing database relations to minimize data redundancy and eliminate insertion, update, and deletion anomalies.\n\n• **1NF**: Atomic (indivisible) column values, unique records.\n• **2NF**: In 1NF + no partial dependency (non-key attributes depend on whole primary key).\n• **3NF**: In 2NF + no transitive dependency (`X -> Y -> Z`).\n• **BCNF**: Stricter 3NF where for every functional dependency `X -> Y`, `X` must be a super key!"
            ],
            suggestions: ["Explain ACID properties", "SQL vs NoSQL databases", "What are Database Indexes?"]
        },
        {
            id: "dbms_sql_nosql",
            topic: "DBMS",
            samples: ["difference between sql and nosql", "sql vs nosql", "relational vs non relational database"],
            required: ["sql", "nosql"],
            secondary: ["difference", "vs", "between", "relational", "document", "mongodb", "mysql"],
            threshold: 0.42,
            responses: [
                "**SQL vs NoSQL Databases**:\n\n• **SQL (PostgreSQL, MySQL)**: Relational, structured tables, predefined schemas, ACID compliant, vertical scaling. Ideal for financial systems and complex joins.\n• **NoSQL (MongoDB, Redis, Cassandra)**: Non-relational, flexible document/key-value models, BASE consistency, horizontal clustering. Ideal for massive real-time streams and evolving schemas."
            ],
            suggestions: ["What is Indexing in DBMS?", "Explain ACID properties", "Difference between Array and Linked List"]
        },
        // DSA
        {
            id: "dsa_array_linkedlist",
            topic: "DSA",
            samples: ["difference between array and linked list", "array vs linked list in dsa"],
            required: ["array", "linkedlist", "linked"],
            secondary: ["difference", "vs", "between", "dsa", "complexity", "pointers"],
            threshold: 0.40,
            responses: [
                "**Array vs Linked List (DSA Comparison)**:\n\n• **Memory**: Arrays require contiguous memory blocks. Linked lists allocate individual nodes dynamically linked via pointers.\n• **Access Time**: Arrays provide instant `O(1)` random index access. Linked lists require sequential `O(n)` traversal.\n• **Insertions/Deletions**: Inserting into an array requires shifting elements `O(n)`. Linked list insertion at head is instant `O(1)` pointer change!"
            ],
            suggestions: ["What is Stack and Queue?", "Explain Big O notation", "How does Binary Search work?"]
        },
        {
            id: "dsa_stack_queue",
            topic: "DSA",
            samples: ["what is stack and queue", "stack vs queue in dsa", "lifo vs fifo"],
            required: ["stack", "queue"],
            secondary: ["lifo", "fifo", "applications", "dsa", "difference", "pop", "push"],
            threshold: 0.42,
            responses: [
                "**Stack vs Queue**:\n\n1. 📚 **Stack (LIFO - Last In, First Out)**: Elements are pushed and popped from the Top. Used in function recursion call-stacks, undo/redo mechanisms, and parentheses matching.\n2. 🚶 **Queue (FIFO - First In, First Out)**: Elements enter at the Rear and leave at the Front. Used in CPU scheduling, printer spooling, and Breadth-First Search (BFS)."
            ],
            suggestions: ["Difference between BFS and DFS", "Explain Big O notation", "How does Binary Search work?"]
        },
        {
            id: "dsa_big_o",
            topic: "DSA",
            samples: ["what is big o notation", "explain time and space complexity", "big o time complexity chart"],
            required: ["big", "complexity", "o"],
            secondary: ["notation", "time", "space", "asymptotic", "worst", "case"],
            threshold: 0.40,
            responses: [
                "**Big-O Notation** measures the worst-case upper bound growth rate of an algorithm's execution time or memory as input size `n` scales:\n\n• `O(1)`: Constant (hash map lookup)\n• `O(log n)`: Logarithmic (Binary Search)\n• `O(n)`: Linear (single loop search)\n• `O(n log n)`: Linearithmic (Merge Sort, Quick Sort)\n• `O(n^2)`: Quadratic (nested loops, Bubble Sort)\n• `O(2^n)`: Exponential (recursive subsets)"
            ],
            suggestions: ["How does Binary Search work?", "Difference between BFS and DFS", "What is a Binary Search Tree?"]
        },
        {
            id: "dsa_binary_search",
            topic: "DSA",
            samples: ["how does binary search work", "binary search algorithm", "time complexity of binary search"],
            required: ["binary", "search"],
            secondary: ["algorithm", "sorted", "divide", "conquer", "middle", "complexity"],
            threshold: 0.42,
            responses: [
                "**Binary Search** is an efficient divide-and-conquer algorithm with `O(log n)` time complexity.\n\n🚨 **Precondition**: The array MUST be sorted!\n\n• **Algorithm**:\n  1. Set `low = 0`, `high = n - 1`.\n  2. Calculate `mid = low + (high - low) / 2`.\n  3. If `arr[mid] == target`, item found!\n  4. If `target < arr[mid]`, search left (`high = mid - 1`).\n  5. If `target > arr[mid]`, search right (`low = mid + 1`)."
            ],
            suggestions: ["What is a Binary Search Tree?", "Difference between Array and Linked List", "Explain Big O notation"]
        },
        // OS
        {
            id: "os_process_thread",
            topic: "Operating Systems",
            samples: ["difference between process and thread", "process vs thread in os"],
            required: ["process", "thread"],
            secondary: ["difference", "vs", "between", "os", "operating", "system"],
            threshold: 0.40,
            responses: [
                "**Process vs Thread in OS**:\n\n• **Process**: An executing program with its own private virtual memory, PCB, heap, and file descriptors. Heavyweight; IPC requires pipes or sockets.\n• **Thread**: A lightweight execution unit within a process. Threads share the parent's memory space and open files, but keep their own Stack and Program Counter.\n\n👉 Context switching between threads is significantly faster than between processes!"
            ],
            suggestions: ["What is Deadlock in OS?", "Explain Virtual Memory and Paging", "What is Multithreading in Java?"]
        },
        {
            id: "os_deadlock",
            topic: "Operating Systems",
            samples: ["what is deadlock in operating system", "coffman conditions for deadlock", "how to prevent deadlock"],
            required: ["deadlock", "coffman"],
            secondary: ["os", "operating", "conditions", "prevention", "banker", "resource"],
            threshold: 0.40,
            responses: [
                "A **Deadlock** occurs when a set of processes are blocked because each is holding a resource and waiting for another held by another process.\n\n**The 4 Coffman Conditions**:\n1. 🔒 **Mutual Exclusion**: Non-shareable resource.\n2. ⏳ **Hold and Wait**: Holding one resource while waiting for others.\n3. 🚫 **No Preemption**: Cannot forcibly take resources.\n4. 🔄 **Circular Wait**: Closed chain of waiting processes.\n\nBreaking even *one* condition prevents deadlocks completely!"
            ],
            suggestions: ["Difference between Process and Thread", "What is Virtual Memory and Paging?", "What is CPU Scheduling?"]
        },
        // Networks
        {
            id: "cn_osi_model",
            topic: "Computer Networks",
            samples: ["explain 7 layers of osi model", "what is osi model in computer networks", "seven layers of osi"],
            required: ["osi", "layers"],
            secondary: ["model", "seven", "network", "physical", "transport", "application"],
            threshold: 0.40,
            responses: [
                "The **OSI 7-Layer Model**:\n\n💡 *Mnemonic*: **A**ll **P**eople **S**eem **T**o **N**eed **D**ata **P**rocessing\n\n7. **Application**: Network APIs for apps (HTTP, DNS, FTP)\n6. **Presentation**: Formatting, encryption (SSL/TLS), compression\n5. **Session**: Manages dialogue sessions (RPC)\n4. **Transport**: End-to-end reliability, flow control (TCP, UDP)\n3. **Network**: Logical routing and addressing (IP, Routers)\n2. **Data Link**: Node-to-node frame transfer (MAC, Switches)\n1. **Physical**: Raw bit transmission over physical media"
            ],
            suggestions: ["Difference between TCP and UDP?", "How does DNS work?", "Difference between HTTP and HTTPS"]
        },
        {
            id: "cn_tcp_udp",
            topic: "Computer Networks",
            samples: ["difference between tcp and udp", "tcp vs udp in computer networks", "tcp vs udp"],
            required: ["tcp", "udp"],
            secondary: ["difference", "vs", "between", "transport", "reliable", "handshake"],
            threshold: 0.42,
            responses: [
                "**TCP vs UDP**:\n\n• **TCP**: Connection-oriented (3-way handshake SYN ➔ SYN-ACK ➔ ACK), guaranteed delivery, automatic retransmissions, ordered packets. Used in Web (HTTP), Email, File transfers.\n• **UDP**: Connectionless (fire-and-forget), lightweight, no delivery guarantees, minimal latency. Used in Video streaming, Gaming, DNS, VoIP."
            ],
            suggestions: ["How does DNS work?", "Explain 7 Layers of OSI Model", "HTTP vs HTTPS"]
        },
        // Motivation & Humor
        {
            id: "motivation_demotivated",
            topic: "Motivation",
            samples: ["i feel demotivated", "feeling down", "i want to give up", "lost motivation", "imposter syndrome"],
            required: ["demotivated", "give", "imposter", "hopeless", "sad", "unmotivated"],
            secondary: ["feel", "down", "give up", "worthless", "stupid", "quit"],
            threshold: 0.38,
            responses: [
                "It is completely okay to have difficult days, {name}. Computer science and programming are inherently challenging, and every senior software engineer once felt stuck just like this.\n\n💡 **NeuroBuddy's 3-Step Reset**:\n1. **Step away for 15 minutes**: Clear your mental cache.\n2. **Decompose the challenge**: Solve ONE tiny line or definition first.\n3. **Remember**: Confusion is not failure — it's your brain growing neural pathways!",
                "Hey {name}, give yourself some credit! You're learning complex concepts that take months to click. Pick one small victory today, celebrate it, and let tomorrow take care of the rest."
            ],
            suggestions: ["Tell me a joke", "Exam stress tips", "Explain Big O notation"]
        },
        {
            id: "jokes_programming",
            topic: "Humor",
            samples: ["tell me a joke", "programmer joke", "coding joke", "funny developer joke"],
            required: ["joke", "jokes", "laugh", "funny"],
            secondary: ["tell", "programmer", "coding", "developer"],
            threshold: 0.38,
            responses: [
                "Why do programmers prefer dark mode?\n👉 **Because light attracts bugs!** 🐛",
                "There are 10 types of people in the world:\n👉 **Those who understand binary, and those who don't.**",
                "Why did the programmer quit their job?\n👉 **Because they didn't get arrays (a raise)!**",
                "A SQL query walks into a bar, walks up to two tables, and asks:\n👉 **\"Can I join you?\"** 🍻",
                "Why do Java programmers have to wear glasses?\n👉 **Because they don't C#!**"
            ],
            suggestions: ["Tell me another joke", "Tell me a fun tech fact", "What is Java?"]
        }
    ];

    // =========================================================================
    // 4. Intent Matcher
    // =========================================================================
    function matchIntent(clean, tokens, activeTopic) {
        const tokenSet = new Set(tokens);
        const stemSet = getStemmedSet(tokens);
        const bigrams = extractNGrams(tokens, 2);
        const trigrams = extractNGrams(tokens, 3);

        let bestIntent = null;
        let bestScore = 0.0;

        for (const intent of KNOWLEDGE_BASE) {
            let score = 0.0;

            // Required check
            if (intent.required && intent.required.length > 0) {
                let matchedReq = false;
                for (const req of intent.required) {
                    if (tokenSet.has(req) || stemSet.has(req) || clean.includes(req)) {
                        matchedReq = true;
                        score += 0.35;
                        break;
                    }
                }
                if (!matchedReq) continue;
            }

            // Secondary check
            if (intent.secondary && intent.secondary.length > 0) {
                let secCount = 0;
                for (const sec of intent.secondary) {
                    if (tokenSet.has(sec) || stemSet.has(sec) || clean.includes(sec)) {
                        secCount++;
                    }
                }
                score += (secCount / intent.secondary.length) * 0.30;
            }

            // Sample comparison
            let maxSim = 0.0;
            for (const sample of intent.samples) {
                const sampleClean = cleanText(sample);
                const sampleTokens = tokenize(sampleClean);
                const sim = jaccardSimilarity(tokenSet, new Set(sampleTokens));
                if (sim > maxSim) maxSim = sim;
                if (clean === sampleClean) { maxSim = 1.0; break; }
            }
            score += maxSim * 0.40;

            // N-Gram boost
            bigrams.forEach(b => {
                if (intent.required.includes(b) || intent.secondary.includes(b)) score += 0.15;
            });
            trigrams.forEach(t => {
                if (intent.required.includes(t) || intent.secondary.includes(t)) score += 0.20;
            });

            // Context topic boost
            if (activeTopic && activeTopic === intent.topic && activeTopic !== "General") {
                score += 0.08;
            }

            if (score > bestScore) {
                bestScore = score;
                bestIntent = intent;
            }
        }

        const confidence = Math.min(1.0, bestScore);
        const isConfident = bestIntent && (confidence >= (bestIntent.threshold || 0.40));
        return { intent: bestIntent, confidence, isConfident };
    }

    // =========================================================================
    // 5. Application State & Controller
    // =========================================================================
    let userNickname = "Scholar";
    let activeTopic = "General";
    let messageCount = 0;
    const topicsExplored = new Set();
    const chatHistory = [];

    // DOM Elements
    const chatMessages = document.getElementById("chatMessages");
    const chatInput = document.getElementById("chatInput");
    const sendBtn = document.getElementById("sendBtn");
    const typingIndicator = document.getElementById("typingIndicator");
    const suggestionChips = document.getElementById("suggestionChips");
    const displayNickname = document.getElementById("displayNickname");
    const editNameBtn = document.getElementById("editNameBtn");
    const nicknameModal = document.getElementById("nicknameModal");
    const nicknameInput = document.getElementById("nicknameInput");
    const modalSaveBtn = document.getElementById("modalSaveBtn");
    const modalCancelBtn = document.getElementById("modalCancelBtn");
    const modalCloseBtn = document.getElementById("modalCloseBtn");
    const statMsgCount = document.getElementById("statMsgCount");
    const statTopicCount = document.getElementById("statTopicCount");
    const exportChatBtn = document.getElementById("exportChatBtn");
    const clearChatBtn = document.getElementById("clearChatBtn");
    const toast = document.getElementById("toast");
    const mobileToggleBtn = document.getElementById("mobileToggleBtn");
    const sidebar = document.getElementById("sidebar");

    function showToast(msg) {
        toast.textContent = msg;
        toast.classList.add("show");
        setTimeout(() => toast.classList.remove("show"), 2500);
    }

    function formatTime(date = new Date()) {
        return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    }

    function updateStats() {
        statMsgCount.textContent = messageCount;
        statTopicCount.textContent = topicsExplored.size;
        displayNickname.textContent = userNickname;
    }

    function updateSuggestions(suggestions) {
        suggestionChips.innerHTML = "";
        if (!suggestions || suggestions.length === 0) return;
        suggestions.forEach(text => {
            const btn = document.createElement("button");
            btn.className = "chip-btn";
            btn.textContent = text;
            btn.onclick = () => handleSend(text);
            suggestionChips.appendChild(btn);
        });
    }

    function renderMarkdown(text) {
        if (!text) return "";
        let html = text.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
        html = html.replace(/\*\*(.*?)\*\*/g, "<strong>$1</strong>");
        html = html.replace(/`([^`]+)`/g, "<code>$1</code>");
        html = html.replace(/\n\n/g, "</p><p>");
        html = html.replace(/\n/g, "<br/>");
        return `<p>${html}</p>`;
    }

    function appendMessage(sender, content, meta = {}) {
        const row = document.createElement("div");
        row.className = `msg-row ${sender.toLowerCase()}`;

        const header = document.createElement("div");
        header.className = "msg-header";
        const senderLabel = sender === "USER" ? userNickname : "🤖 NeuroBuddy AI";
        header.innerHTML = `<span class="sender-name">${senderLabel}</span><span class="msg-time">${formatTime()}</span>`;
        row.appendChild(header);

        const bubble = document.createElement("div");
        bubble.className = "msg-bubble";
        bubble.innerHTML = renderMarkdown(content);

        // If Bot, append confidence pill, mood, and copy button
        if (sender === "BOT") {
            const footer = document.createElement("div");
            footer.className = "msg-footer";

            if (meta.confidence !== undefined) {
                const confPercent = Math.round(meta.confidence * 100);
                const pillClass = confPercent >= 75 ? "high" : (confPercent >= 50 ? "medium" : "low");
                footer.innerHTML += `<span class="confidence-pill ${pillClass}">🎯 ${confPercent}% Match</span>`;
            }

            if (meta.mood && meta.mood !== "NEUTRAL") {
                footer.innerHTML += `<span class="mood-pill">💬 ${meta.mood}</span>`;
            }

            const copyBtn = document.createElement("button");
            copyBtn.className = "copy-btn";
            copyBtn.innerHTML = "📋 Copy";
            copyBtn.onclick = () => {
                navigator.clipboard.writeText(content).then(() => {
                    copyBtn.innerHTML = "✓ Copied!";
                    setTimeout(() => { copyBtn.innerHTML = "📋 Copy"; }, 1500);
                });
            };
            footer.appendChild(copyBtn);
            bubble.appendChild(footer);

            // Inline follow-up chips
            if (meta.suggestions && meta.suggestions.length > 0) {
                const inlineChips = document.createElement("div");
                inlineChips.className = "inline-chips";
                meta.suggestions.forEach(s => {
                    const chip = document.createElement("button");
                    chip.className = "chip-btn";
                    chip.textContent = s;
                    chip.onclick = () => handleSend(s);
                    inlineChips.appendChild(chip);
                });
                bubble.appendChild(inlineChips);
            }
        }

        row.appendChild(bubble);
        chatMessages.appendChild(row);
        chatMessages.scrollTop = chatMessages.scrollHeight;

        chatHistory.push({
            sender,
            content,
            time: new Date().toISOString(),
            confidence: meta.confidence
        });
    }

    function processMessageLocally(raw) {
        const clean = cleanText(raw);
        const tokens = tokenize(clean);
        const sentiment = analyzeSentiment(clean, tokens);

        // Name change command check
        if (clean.startsWith("my name is ") || clean.startsWith("call me ") || clean.startsWith("i am ")) {
            let extracted = "";
            if (clean.startsWith("my name is ")) extracted = clean.slice("my name is ".length).trim();
            else if (clean.startsWith("call me ")) extracted = clean.slice("call me ".length).trim();
            else if (clean.startsWith("i am ")) extracted = clean.slice("i am ".length).trim();

            if (extracted) {
                userNickname = extracted.split(/\s+/).map(w => w.charAt(0).toUpperCase() + w.slice(1)).join(" ");
                updateStats();
                return {
                    reply: `Pleasure to meet you, **${userNickname}**! I'll call you ${userNickname} from now on. What would you like to study or discuss?`,
                    confidence: 0.98,
                    suggestions: ["What is DBMS?", "Explain OOP concepts in Java", "Tell me a joke"],
                    mood: sentiment.type
                };
            }
        }

        const match = matchIntent(clean, tokens, activeTopic);

        if (match.isConfident && match.intent) {
            activeTopic = match.intent.topic;
            topicsExplored.add(match.intent.topic);

            const tmpl = match.intent.responses[Math.floor(Math.random() * match.intent.responses.length)];
            let reply = tmpl.replace(/{name}/g, userNickname);

            if (sentiment.type !== "NEUTRAL" && sentiment.message && match.intent.topic !== "Motivation" && match.intent.topic !== "Humor") {
                reply = `${sentiment.message}\n\n${reply}`;
            }

            return {
                reply,
                confidence: match.confidence,
                suggestions: match.intent.suggestions || [],
                mood: sentiment.type
            };
        } else {
            let fallback = sentiment.message ? `${sentiment.message}\n\n` : "";
            fallback += `I'm still learning how to handle that specific question, ${userNickname}! Could you ask it in another way, or choose one of the study topics below?\n\n💡 *Tip*: I specialize in **Java, Python, DBMS, DSA, Operating Systems, Computer Networks**, and **Study Motivation**.`;
            return {
                reply: fallback,
                confidence: match.confidence || 0.35,
                suggestions: ["What is DBMS?", "Explain OOP concepts in Java", "Difference between TCP and UDP", "Tell me a joke"],
                mood: sentiment.type
            };
        }
    }

    async function handleSend(queryText) {
        const text = queryText || chatInput.value.trim();
        if (!text) return;

        chatInput.value = "";
        messageCount++;
        appendMessage("USER", text);
        updateStats();

        // Show typing indicator
        typingIndicator.style.display = "block";
        chatMessages.scrollTop = chatMessages.scrollHeight;

        // Simulate natural bot reading & thinking delay (400ms)
        await new Promise(r => setTimeout(r, 400));

        let response;
        try {
            // Optional: Check if Java LiveServer is running locally
            const res = await fetch("/api/chat", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ message: text, nickname: userNickname })
            });
            if (res.ok) {
                response = await res.json();
            } else {
                response = processMessageLocally(text);
            }
        } catch (_) {
            // Offline/Live Server fallback: use local client-side NLP engine
            response = processMessageLocally(text);
        }

        typingIndicator.style.display = "none";

        appendMessage("BOT", response.reply, {
            confidence: response.confidence,
            suggestions: response.suggestions,
            mood: response.mood
        });

        updateSuggestions(response.suggestions);
        updateStats();
    }

    function displayWelcome() {
        const welcome = `Hey **${userNickname}**! Welcome to **NeuroBuddy AI** – your intelligent conversational assistant for Computer Science and programming!\n\n` +
            `I can assist you with:\n` +
            `• ☕ **Java**: Core concepts, OOP, JVM, Collections, Multithreading\n` +
            `• 🐍 **Python**: Lists vs Tuples, GIL, Decorators, Deep/Shallow Copy\n` +
            `• 🗄️ **DBMS**: Relational models, ACID properties, Normalization (1NF-BCNF), SQL vs NoSQL, Indexing\n` +
            `• 🌲 **DSA**: Complexity analysis (Big-O), Arrays, Stacks/Queues, Binary Search, BST, BFS/DFS\n` +
            `• 💻 **Operating Systems**: Process vs Thread, Deadlocks, Virtual Memory, CPU Scheduling\n` +
            `• 🌐 **Computer Networks**: OSI 7-Layer Model, TCP vs UDP, DNS, HTTP/HTTPS, IP/MAC\n` +
            `• 💡 **Study Motivation & Humor**: Encouragement when stuck, and programmer jokes!\n\n` +
            `Ask me any question below, or click any suggested prompt to begin!`;

        appendMessage("BOT", welcome, {
            confidence: 1.0,
            suggestions: ["What is DBMS?", "Explain OOP concepts in Java", "Difference between TCP and UDP", "Tell me a joke"]
        });

        updateSuggestions(["What is DBMS?", "Explain OOP concepts in Java", "Difference between TCP and UDP", "Tell me a joke"]);
    }

    function exportHistory() {
        if (chatHistory.length === 0) {
            showToast("No messages to export yet!");
            return;
        }

        let transcript = "====================================================\n";
        transcript += "   NeuroBuddy AI - Conversation Transcript\n";
        transcript += "====================================================\n";
        transcript += `User Nickname: ${userNickname}\n`;
        transcript += `Exported At: ${new Date().toLocaleString()}\n`;
        transcript += `Total Messages: ${chatHistory.length}\n`;
        transcript += `Topics Explored: ${topicsExplored.size}\n`;
        transcript += "----------------------------------------------------\n\n";

        chatHistory.forEach(item => {
            const senderName = item.sender === "USER" ? userNickname : "NeuroBuddy AI";
            transcript += `[${item.time}] ${senderName}:\n${item.content}\n`;
            if (item.confidence) {
                transcript += `   (Confidence: ${Math.round(item.confidence * 100)}%)\n`;
            }
            transcript += "\n";
        });

        transcript += "====================================================\n";
        transcript += "End of Conversation Log - NeuroBuddy AI Assistant\n";

        const blob = new Blob([transcript], { type: "text/plain;charset=utf-8" });
        const url = URL.createObjectURL(blob);
        const a = document.createElement("a");
        a.href = url;
        a.download = "neurobuddy_chat_history.txt";
        a.click();
        URL.revokeObjectURL(url);
        showToast("✓ Chat transcript exported successfully!");
    }

    function clearChat() {
        if (confirm("Are you sure you want to clear the conversation history?")) {
            chatMessages.innerHTML = "";
            chatHistory.length = 0;
            messageCount = 0;
            topicsExplored.clear();
            activeTopic = "General";
            updateStats();
            displayWelcome();
            showToast("Chat history cleared");
        }
    }

    // Event Listeners
    sendBtn.addEventListener("click", () => handleSend());
    chatInput.addEventListener("keydown", (e) => {
        if (e.key === "Enter") handleSend();
    });

    document.querySelectorAll(".nav-btn").forEach(btn => {
        btn.addEventListener("click", () => {
            const q = btn.getAttribute("data-query");
            if (q) handleSend(q);
            if (window.innerWidth <= 820) sidebar.classList.remove("open");
        });
    });

    editNameBtn.addEventListener("click", () => {
        nicknameInput.value = userNickname;
        nicknameModal.classList.add("active");
        nicknameInput.focus();
    });

    function saveNickname() {
        const val = nicknameInput.value.trim();
        if (val) {
            userNickname = val;
            updateStats();
            nicknameModal.classList.remove("active");
            showToast(`Nickname updated to "${userNickname}"!`);
        }
    }

    modalSaveBtn.addEventListener("click", saveNickname);
    modalCancelBtn.addEventListener("click", () => nicknameModal.classList.remove("active"));
    modalCloseBtn.addEventListener("click", () => nicknameModal.classList.remove("active"));
    nicknameInput.addEventListener("keydown", (e) => {
        if (e.key === "Enter") saveNickname();
        if (e.key === "Escape") nicknameModal.classList.remove("active");
    });

    exportChatBtn.addEventListener("click", exportHistory);
    clearChatBtn.addEventListener("click", clearChat);

    if (mobileToggleBtn) {
        mobileToggleBtn.addEventListener("click", () => {
            sidebar.classList.toggle("open");
        });
    }

    // Init
    displayWelcome();
    updateStats();
})();
