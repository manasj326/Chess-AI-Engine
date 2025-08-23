# ♟️ Chess-AI-Engine

A **Java-based Chess Engine** I built from scratch to explore algorithms in **Game Theory**.  
The engine follows the full rules of chess and features a computer opponent that selects moves using **minimax with alpha-beta pruning**, combined with heuristics and an **opening book** for efficient and competitive play.

---

## 🧩 Key Features
- 🤖 AI opponent powered by **minimax + alpha-beta pruning**  
- ⚡ Optimized with **move ordering**, **piece-square heuristics**, and **MVV-LVA capture ordering**  
- 📚 Uses an **opening book** for stronger early-game play  
- 🎮 Playable via **command line**  
- 🛠️ Designed with **clean OOP principles** and modular code  

---

## 📊 How the AI Thinks
1. **Move Generation** – Collect all legal moves for the current position  
2. **Simulation & Evaluation** – Score board states using **piece values + positional scoring (piece-square tables)**  
3. **Search** – Explore move trees with **minimax and alpha-beta pruning**  
4. **Decision** – Apply **MVV-LVA move ordering** to prioritize captures and improve pruning efficiency  
5. **Opening Play** – Use a **preloaded opening book** to play optimal opening moves  

---

## 📸 Demo
<img width="389" height="656" alt="image" src="https://github.com/user-attachments/assets/5aedeacd-786b-4ce5-a0a8-ffceb7877715" />

---

## 🌱 Future Improvements
- Implement **transposition tables** with Zobrist hashing  
- Build a **GUI** for interactive play
- Add an **Endgame** book for perfect late-game play

---

## ▶️ Getting Started
- Clone the repo
- Compile the code - javac Main.java
- Run - java Main
---
