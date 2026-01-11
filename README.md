# 🏥 Hospital Management System

A complete **Hospital Management System** built with Java demonstrating **Object-Oriented Programming (OOP)** concepts. Features a clean, modern, light-themed GUI using Java Swing.

---

## ✨ Features

### 🔐 Authentication System
- Role-based login (Administrator, Doctor, Receptionist)
- Automatic user account creation for doctors
- Secure logout functionality

### 📊 Dashboard
- Real-time statistics overview
- Patient, doctor, and appointment counts
- Revenue tracking and room occupancy rates

### 👥 Patient Management
- Patient registration with complete details
- Search patients by name
- Admission and discharge management

### 👨‍⚕️ Doctor Management  
- Add doctors with auto-generated login credentials
- View doctor profiles and specializations
- Manage consultation fees

### 📅 Appointment Scheduling
- Book, complete, or cancel appointments
- View today's appointments

### 🏨 Room Management
- Visual room grid with color-coded status
- Room allocation to patients

### 💵 Billing System
- Generate bills and add items
- Process payments (Cash, Card, UPI)

---

## 🎯 OOP Concepts Demonstrated

| Concept | Implementation |
|---------|----------------|
| **Abstraction** | Abstract `Person` class with `getRole()` method |
| **Inheritance** | `Patient`, `Doctor`, `Staff` extend `Person` |
| **Encapsulation** | Private fields with getters/setters |
| **Polymorphism** | `getRole()` returns different values per subclass |
| **Singleton** | `HospitalManager` manages entire system |
| **Composition** | `Bill` contains `BillItem` objects |

---

## 📁 Project Structure

```
Hospital_Management_System/
├── src/
│   ├── Main.java
│   ├── models/          # Data classes
│   ├── managers/        # Business logic
│   ├── utils/           # Utilities
│   └── gui/             # User interface
├── data/                # Auto-generated data files
├── run.bat              # Windows run script
└── README.md
```

---

## 🚀 How to Run

### Prerequisites
- Java JDK 8 or higher

### Windows (Easiest)
```batch
# Double-click run.bat or run:
run.bat
```

### Command Line
```bash
# Compile
javac -d out -sourcepath src src/Main.java src/models/*.java src/managers/*.java src/utils/*.java src/gui/*.java src/gui/components/*.java

# Run
java -cp out Main
```

### Using IDE
1. Open project in IntelliJ IDEA / Eclipse / NetBeans
2. Mark `src` as Sources Root
3. Run `Main.java`

---

## 🔑 Default Login Credentials

| Username | Password | Role |
|----------|----------|------|
| `admin` | `admin123` | Administrator |
| `reception` | `reception123` | Receptionist |

**Doctor accounts** are auto-created when admin adds a new doctor.

---

## 👥 Team

| Name | USN |
|------|-----|
| Vignesh B S | 1BI24IS187 |
| Rohit Maiya M | 1BI24IS131 |
| Shailesh Nayak | 1BI24IS142 |
| Shreenivas CS | 1BI25IS414 |

**College:** Bangalore Institute of Technology  
**Department:** Information Science and Engineering  
**Subject:** Object Oriented Programming with Java

---

## 📄 License

This project is for educational purposes (OOP course mini-project).
