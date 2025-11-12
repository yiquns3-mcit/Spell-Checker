# 📝 Spell-Checker
A simple Java-based spell checking program that compares words from a text file with a given dictionary, identifies misspelled words, and provides multiple correction options.

---

## 🚀 Program Overview

1️⃣ **Dictionary Load**  
The program automatically loads `engDictionary.txt` as the dictionary source.

2️⃣ **User Input File**  
The user is prompted to enter the name of the text file `<input>.txt` to be spell-checked.  
If the file cannot be opened, the program asks again until a valid file is provided.

3️⃣ **Spell Checking**  
Each word from the text file is compared to the dictionary:
- Correct words are written as-is.  
- Misspelled words trigger user interaction with three options:
  - **r** → replace with a suggested word  
  - **a** → accept the original word  
  - **t** → manually type a replacement  

4️⃣ **Output**  
The corrected version of the input file is written to a new file named `<input>_chk.txt`.

---

## 👥 Collaborators

| Name | Role | Contribution |
|------|------|---------------|
| **Yiqun Su** | main developer | Implemented main program flow (`SpellChecker.java`) and unit testing. |
| **Lebin Hu** | algorithm developer | Implemented recommendation algorithm (`WordRecommender.java`) and unit testing. |

🟰 **Both collaborators contributed equally** to the design, implementation, and testing of the project.
