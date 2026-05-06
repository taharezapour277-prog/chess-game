# ♟️ Chess Game — Java Swing

<div align="center">

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/Swing-GUI-blue?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

A full-featured chess application built in Java with Swing, following clean architecture and MVC separation.

[Features](#-features) • [Installation](#-installation) • [Usage](#-usage) • [Screenshots](#-screenshots) • [Contact](#-contact)

</div>
---

| [English](README.md) | [Persian](READMEFA.md) |
| :---: | :---: |
---

# 📋 Table of Contents

- [Description](#-description)
- [Features](#-features)
- [Architecture](#-architecture-mvc)
- [Project Structure](#-project-structure)
- [Prerequisites](#-prerequisites)
- [Installation](#-installation)
- [Usage](#-usage)
- [Screenshots](#-screenshots)
- [How to Play](#-how-to-play)
- [Contributing](#-contributing)
- [License](#-license)
- [Contact](#-contact)

---

# 📖 Description

This project implements standard two-player chess with a graphical interface.  
The game engine supports:

- Check
- Checkmate
- Stalemate
- Castling
- En passant
- Pawn promotion

The UI provides:

- Chess clock
- Legal move highlighting
- Save / Load system
- Restart option

The architecture separates **game logic from the UI**, allowing the rules to be tested or extended independently.

---

# ✨ Features

- ♟️ **Full chess rules** — All standard piece movements and captures
- 👑 **Check, checkmate, stalemate** — Correct detection and game-end handling
- 🏰 **Castling** — Kingside and queenside with validation
- 🎯 **En passant** — Pawn capture after two-square pawn advance
- 🔄 **Promotion** — Pawns promote to Queen on last rank
- 💡 **Legal move highlighting** — Selected piece and legal destinations highlighted
- ⏱️ **Chess clock** — Configurable timer per player (default 5 minutes)
- 💾 **Save / Load** — Save game state to `chess.save`
- 🔄 **Restart** — Reset board and timers instantly

---

# 🏗️ Architecture (MVC)

```
┌──────────────────────────────┐
│            View              │
│  ChessFrame, ChessBoardPanel │
│  Rendering, UI, Timers       │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│         Controller           │
│           Game               │
│   Move validation, Rules     │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│            Model             │
│  Board, Pieces, Position     │
│  Color                       │
└──────────────────────────────┘
```

### Model (`model` package)

Contains core chess data:

- Board
- Pieces
- Position
- Color

No UI logic.

### Controller (`game` package)

Handles game mechanics:

- Move validation
- Turn handling
- Check / checkmate detection
- Castling
- En passant
- Promotion

### View (`ui` package)

Swing interface:

- ChessFrame
- ChessBoardPanel
- Timer display
- Save / Load controls
- User input

---

# 📁 Project Structure

```
chess-game/

├── README.md
├── run.bat
├── docs/
│   ├── screenshot-1.png
│   ├── screenshot-2.png
│   └── screenshot-3.png
└── src/

    ├── model/
    │   ├── Board.java
    │   ├── Color.java
    │   ├── Piece.java
    │   ├── King.java
    │   ├── Queen.java
    │   ├── Rook.java
    │   ├── Bishop.java
    │   ├── Knight.java
    │   ├── Pawn.java
    │   └── Position.java

    ├── game/
    │   ├── Game.java
    │   ├── GameStatus.java
    │   └── Move.java

    └── ui/
        ├── Main.java
        ├── ChessFrame.java
        └── ChessBoardPanel.java
```

---

# 🔧 Prerequisites

Before running the project, install:

- **Java Development Kit (JDK) 8 or later**

Check installation:

```bash
java -version
```

Download Java if needed:

- Oracle JDK
- OpenJDK

---

# 📥 Installation

### Option 1 — Clone with Git

```bash
git clone https://github.com/taharezapour277-prog/chess-game.git
cd chess-game
```

### Option 2 — Download ZIP

1. Go to the repository page
2. Click **Code**
3. Click **Download ZIP**
4. Extract the ZIP
5. Open terminal in the project folder

---

# 🚀 Usage

## Windows (Easy Method)

Simply run:

```
run.bat
```

It will:

- Check Java installation
- Compile the project
- Run the game

---

## Manual Compilation

### Windows

```cmd
javac -d out src/model/*.java src/game/*.java src/ui/*.java
java -cp out ui.Main
```

### macOS / Linux

```bash
javac -d out src/model/*.java src/game/*.java src/ui/*.java
java -cp out ui.Main
```

### If wildcard expansion fails

Compile manually:

```bash
javac -d out \
src/model/Position.java \
src/model/Color.java \
src/model/Piece.java \
src/model/King.java \
src/model/Queen.java \
src/model/Rook.java \
src/model/Bishop.java \
src/model/Knight.java \
src/model/Pawn.java \
src/model/Board.java \
src/game/GameStatus.java \
src/game/Move.java \
src/game/Game.java \
src/ui/ChessBoardPanel.java \
src/ui/ChessFrame.java \
src/ui/Main.java

java -cp out ui.Main
```

---

# 🎮 How to Play

- White moves first
- Click a **white piece** to select it
- Legal moves appear in **green**
- Click a highlighted square to move

### Game Rules

- Chess clock runs only for the current player
- If time reaches zero → opponent wins
- Pawn reaching last rank → automatically promotes to Queen

### Game Controls

| Action       | Description         |
| ------------ | ------------------- |
| Left Click   | Select piece / Move |
| Save Game    | Save current game   |
| Load Game    | Load saved game     |
| Restart Game | Start new game      |

---

# 📸 Screenshots

### Main Game Board

The main chess board with piece highlighting.

### Legal Moves Highlighted

Green squares show legal moves for selected piece.

### Game End Screen

Checkmate or time-out notification.

### Adding Screenshots

1. Create a `docs` folder
2. Take screenshots
3. Save as:

```
docs/screenshot-1.png
docs/screenshot-2.png
docs/screenshot-3.png
```

4. Commit and push to GitHub.

---

# 🤝 Contributing

Contributions are welcome.

```
git checkout -b feature/improvement
git commit -m "Add new feature"
git push origin feature/improvement
```

Then open a **Pull Request**.

---

# 📄 License

MIT License

Copyright © 2025 Taha Rezapour

Permission is hereby granted, free of charge, to any person obtaining a copy  
of this software and associated documentation files (the "Software"), to deal  
in the Software without restriction, including without limitation the rights  
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell  
copies of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.

---

# 📞 Contact

**Taha Rezapour**

📧 Email: taharezapour277@gmail.com  
💬 Telegram: @Taharezapour  
🐙 GitHub: https://github.com/taharezapour277-prog

<div align="center">

⭐ If you found this project helpful, please give it a star!  
Made with ❤️ by Taha Rezapour

</div>
