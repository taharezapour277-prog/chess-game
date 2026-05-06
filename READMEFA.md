# ♟️ Chess Game — Java Swing

<div align="center">

**[English](README.md)** | **[فارسی](READMEFA.md)**

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/Swing-GUI-blue?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

یک برنامه شطرنج کامل ساخته شده با جاوا و Swing، بر اساس معماری تمیز (Clean Architecture) و تفکیک MVC.

[ویژگی‌ها](#-ویژگیها) • [نصب](#-نصب) • [استفاده](#-استفاده) • [تصاویر](#-تصاویر) • [ارتباط با ما](#-ارتباط-با-ما)

</div>

---

# 📋 فهرست مطالب

- [توضیحات](#-توضیحات)
- [ویژگی‌ها](#-ویژگیها)
- [معماری (MVC)](#-معماری-mvc)
- [ساختار پروژه](#-ساختار-پروژه)
- [پیشنیازها](#-پیشنیازها)
- [نصب](#-نصب)
- [استفاده](#-استفاده)
- [تصاویر](#-تصاویر)
- [نحوه بازی](#-نحوه-بازی)
- [مشارکت](#-مشارکت)
- [مجوز](#-مجوز)
- [ارتباط با ما](#-ارتباط-با-ما)

---

# 📖 توضیحات

این پروژه شطرنج استاندارد دو نفره را با رابط گرافیکی پیاده‌سازی می‌کند.  
موتور بازی از موارد زیر پشتیبانی می‌کند:

- کیش (Check)
- مات (Checkmate)
- پات (Stalemate)
- قلعه‌گیری (Castling)
- آن پاسان (En passant)
- ترفیع پیاده (Pawn promotion)

امکانات رابط کاربری (UI):

- ساعت شطرنج
- برجسته‌سازی حرکات مجاز
- سیستم ذخیره / بارگذاری (Save / Load)
- گزینه شروع مجدد بازی (Restart)

معماری پروژه **منطق بازی را از رابط کاربری** جدا می‌کند، که اجازه می‌دهد قوانین به صورت مستقل آزمایش یا توسعه داده شوند.

---

# ✨ ویژگی‌ها

- ♟️ **قوانین کامل شطرنج** — تمام حرکات استاندارد مهره‌ها و گرفتن‌ها
- 👑 **کیش، مات، پات** — تشخیص صحیح و مدیریت پایان بازی
- 🏰 **قلعه‌گیری** — قلعه‌گیری سمت شاه و وزیر با اعتبارسنجی
- 🎯 **آن پاسان** — گرفتن پیاده پس از حرکت دو خانه به جلو
- 🔄 **ترفیع** — ارتقای پیاده به وزیر در آخرین ردیف
- 💡 **برجسته‌سازی حرکات مجاز** — مشخص شدن مهره انتخاب شده و مقصدهای مجاز
- ⏱️ **ساعت شطرنج** — تایمر قابل تنظیم برای هر بازیکن (پیش‌فرض ۵ دقیقه)
- 💾 **ذخیره / بارگذاری** — ذخیره وضعیت بازی در فایل `chess.save`
- 🔄 **شروع مجدد** — بازنشانی فوری صفحه و تایمرها

---

# 🏗️ معماری (MVC)

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

### مدل (`پکیج model`)

شامل داده‌های اصلی شطرنج است:

- صفحه (Board)
- مهره‌ها (Pieces)
- موقعیت (Position)
- رنگ (Color)

بدون هیچ‌گونه منطق رابط کاربری.

### کنترلر (`پکیج game`)

مکانیک‌های بازی را مدیریت می‌کند:

- اعتبارسنجی حرکات
- مدیریت نوبت‌ها
- تشخیص کیش / مات
- قلعه‌گیری
- آن پاسان
- ترفیع

### رابط کاربری (`پکیج ui`)

رابط گرافیکی Swing:

- پنجره بازی (ChessFrame)
- صفحه نمایش شطرنج (ChessBoardPanel)
- نمایش تایمر
- کنترل‌های ذخیره / بارگذاری
- دریافت ورودی کاربر

---

# 📁 ساختار پروژه

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

# 🔧 پیشنیازها

قبل از اجرای پروژه، مطمئن شوید که موارد زیر نصب شده‌اند:

- **کیت توسعه جاوا (JDK) نسخه ۸ یا بالاتر**

بررسی وضعیت نصب:

```bash
java -version
```

در صورت نیاز، جاوا را دانلود کنید:

- Oracle JDK
- OpenJDK

---

# 📥 نصب

### گزینه ۱ — کلون کردن با Git

```bash
git clone https://github.com/taharezapour277-prog/chess-game.git
cd chess-game
```

### گزینه ۲ — دانلود فایل ZIP

1. به صفحه مخزن (Repository) بروید.
2. روی دکمه **Code** کلیک کنید.
3. گزینه **Download ZIP** را انتخاب کنید.
4. فایل ZIP را از حالت فشرده خارج کنید.
5. ترمینال را در پوشه پروژه باز کنید.

---

# 🚀 استفاده

## ویندوز (روش آسان)

کافیست فایل زیر را اجرا کنید:

run.bat

این فایل به صورت خودکار:

- نصب بودن جاوا را بررسی می‌کند.
- پروژه را کامپایل می‌کند.
- بازی را اجرا می‌کند.

---

## کامپایل دستی

### ویندوز

```cmd
javac -d out src/model/*.java src/game/*.java src/ui/*.java
java -cp out ui.Main
```

### مک (macOS) / لینوکس

```bash
javac -d out src/model/*.java src/game/*.java src/ui/*.java
java -cp out ui.Main
```

### در صورت عدم کارکرد Wildcard (علامت ستاره)

فایل‌ها را به صورت دستی کامپایل کنید:

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

# 🎮 نحوه بازی

- مهره سفید بازی را شروع می‌کند.
- برای انتخاب، روی یک **مهره سفید** کلیک کنید.
- حرکات مجاز با رنگ **سبز** نمایش داده می‌شوند.
- برای حرکت کردن، روی یکی از خانه‌های برجسته شده کلیک کنید.

### قوانین بازی

- ساعت شطرنج فقط برای بازیکنی که نوبت اوست کار می‌کند.
- اگر زمان به صفر برسد → حریف برنده می‌شود.
- رسیدن پیاده به آخرین ردیف → به طور خودکار به وزیر ارتقا می‌یابد.

### کنترل‌های بازی

| اکشن         | توضیحات                 |
| ------------ | ----------------------- |
| کلیک چپ      | انتخاب مهره / حرکت دادن |
| Save Game    | ذخیره بازی فعلی         |
| Load Game    | بارگذاری بازی ذخیره شده |
| Restart Game | شروع بازی جدید          |

---

# 📸 تصاویر

### صفحه اصلی بازی

صفحه اصلی شطرنج به همراه برجسته‌سازی مهره‌ها.

### حرکات مجاز

خانه‌های سبز نشان‌دهنده حرکات مجاز برای مهره انتخاب شده هستند.

### صفحه پایان بازی

اعلان مربوط به مات شدن یا اتمام زمان.

# 🤝 مشارکت

از مشارکت شما استقبال می‌شود.

```
git checkout -b feature/improvement
git commit -m "Add new feature"
git push origin feature/improvement
```

سپس یک **Pull Request** ایجاد کنید.

---

# 📄 مجوز

مجوز MIT

Copyright © 2026 Taha Rezapour

Permission is hereby granted, free of charge, to any person obtaining a copy  
of this software and associated documentation files (the "Software"), to deal  
in the Software without restriction, including without limitation the rights  
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell  
copies of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.

---

# 📞 ارتباط با ما

**Taha Rezapour**

📧 ایمیل: taharezapour277@gmail.com  
💬 تلگرام: Taharezapour@  
🐙 گیت‌هاب: https://github.com/taharezapour277-prog

<div align="center">

⭐ اگر این پروژه برای شما مفید بود، لطفاً به آن ستاره (Star) بدهید!  
ساخته شده با ❤️ توسط Taha Rezapour

</div>
