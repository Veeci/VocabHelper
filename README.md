# VocabHelper

As an English learner, I see the difficulties in new words learning, which is one of the reason why I, with my passion for Android developing, created this application. VocabHelper is a small mobile tool designed to help users improve their English vocabulary through an interactive learning process. The app encourages users to not only learn new words but also gain a deeper understanding by exploring their meanings, synonyms, antonyms, collocations, and even recording their own pronunciation of each word. Additionally, the app features a Pomodoro timer to help users focus during their learning sessions.
And the importnt thing is, although this software product support many languages, but I strongly recommend users to use English thoroughly, to describe meaning of the word added in English, to lookup and see the word in English, ...

## Features

- **Vocabulary Learning Method**: 
  - Learn new words and understand their meanings.
  - Explore synonyms and antonyms of each word.
  - Record and store your pronunciation of words for practice.
  - Discover word collocations and make your own sentences for deeper learning.

- **Dictionary Integration**: 
  - Integrated with the [Free Dictionary API](https://dictionaryapi.dev/) for quick word lookups and definitions.
  
- **Pomodoro Timer**: 
  - Integrated Pomodoro method to help users maintain focus and efficiency during study sessions. It includes:
    - 25-minute focus sessions
    - 5-minute short breaks
    - 10-minute long breaks after three focus sessions

- **Authentication**:
  - Users can create accounts and sign in using email/password or Google account authentication, ensuring security and a personalized learning experience.
  
## Technologies Used

- **Frontend**: Android (Kotlin)
- **Backend**: Firebase Authentication (Email/Password & Google Sign-In)
- **Database**: Firebase Firestore (User data storage), Firebase Cloud Storage (Audio file storage)
- **API**: Free Dictionary API for word lookups
- **UI Components**: Material Design, ViewBinding, RecyclerView
- **Tools**: Retrofit for API requests, Coroutines for asynchronous tasks

## Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/your-username/vocabhelper.git
    ```
    
2. Open the project in Android Studio.

3. Set up Firebase:
    - Add the `google-services.json` file from your Firebase Console.
    - Ensure you have added your app’s SHA-1 or SHA-256 fingerprint in the Firebase Console.

4. Build the project and run it on an Android device/emulator.

## Usage

1. **Sign up / Sign in**:
    - Create an account using email/password or sign in with Google for secure access.
    
2. **Vocabulary Learning**:
    - Lookup a new word, learn its meaning, synonyms, antonyms, and collocations.
    - Record your pronunciation for each word and store it for future review.
    
3. **Pomodoro Sessions**:
    - Use the Pomodoro timer to focus on learning with 25-minute sessions, followed by short breaks for better retention.

## API Reference

- **Free Dictionary API**: [https://dictionaryapi.dev/](https://dictionaryapi.dev/)
  
## Contributing

Contributions are welcome! Please follow these steps:
1. Fork the project.
2. Create a new feature branch (`git checkout -b feature/branch`).
3. Commit your changes (`git commit -m 'Add new feature'`).
4. Push to the branch (`git push origin feature-branch`).
5. Open a pull request.

Happy learning with VocabHelper!

##Coming soon on Google Play!
