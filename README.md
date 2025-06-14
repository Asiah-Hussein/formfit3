# FormFit - AI Exercise Form Analysis Application

## Overview
A mobile application that provides real-time exercise form analysis using AI-powered pose detection and wearable sensor integration. Developed as part of the CMP6213 Individual Project at Birmingham City University.

## Features
* Real-time exercise form analysis and correction
* Comprehensive exercise library with form guides
* Progress tracking and performance metrics
* Achievement system for user motivation
* Offline functionality with local database
* Wearable device integration for haptic feedback

## Technologies Used
* Java for Android development
* SQLite for local data persistence
* Material Design for UI components
* Android SDK for mobile functionality
* MVC architecture pattern

## Getting Started
1. Clone the repository
2. Open project in Android Studio
3. Sync Gradle dependencies
4. Run on emulator or physical device (API level 24+)

## Project Structure
app/
├── main/java/com/asiah/formfit/
│   ├── main/              # Core activities
│   ├── data/              # Data models & database
│   ├── adapters/          # RecyclerView adapters
│   ├── utils/             # Utility classes
│   └── wearable/          # Sensor integration
└── res/                   # Layouts and resources

## Key Components
* **Motion Analysis**: Pattern detection algorithm for form evaluation
* **Database Helper**: SQLite implementation for exercise tracking
* **Exercise Library**: Categorized exercises with difficulty levels
* **Progress Tracking**: Historical data visualization and statistics

## Author
Asiah Abdisalam Hussein - Birmingham City University
