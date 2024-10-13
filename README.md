# WhatColour
WhatColour is a fun and simple two-screen game where the challenge lies in choosing the correct color, not the word. The user has one minute to select as many correct colors as possible. After the timer runs out, the player's score is displayed along with the highest score recorded on the current device.

## Game Extension
The application also features an extended version with three screens, incorporating a cloud-based leaderboard using Firebase Realtime Database. This enables global high scores, allowing players to see and compete against others' scores in real-time.

## Competences and Techniques

### Core Version:
- **Jetpack Compose**: UI development using modern declarative UI design.
- **Model-View-ViewModel (MVVM)**: Architecture pattern with state management.
- **Internationalization (I18n) & Localization (L10n)**: Support for multiple languages.
- **Adaptive Design**: Responsive UI across different devices and orientations.
- **Simple Data Persistence**: Local score storage on the device.

### Extended Version:
- **Firebase Realtime Database**: Cloud database to store and sync global high scores.
- **Hilt Dependency Injection**: Manage dependencies efficiently.
