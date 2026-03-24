![ ](README/mockup.png)<br>
<img width="200" src="./README/ic_logo.png">

# 소집 : SOZIP <br>
### An Application for Delivery Cost Dutch Pay<br>
###### '소집(SOZIP) means to gather (people, etc.) in Korean.'
ⓒ 2021 Changjin Ha, Sojung Moon. All Rights Reserved.<br><br>

## 🚀 Tech Stack

### Client (Android)

<img src="https://img.shields.io/badge/Kotlin-7F52FF?style=flat-square&logo=Kotlin&logoColor=white"/></a>
<img src="https://img.shields.io/badge/Jetpack%20Compose-7F52FF?style=flat-square&logo=Android&logoColor=white"/></a>

### Backend (BaaS & Serverless)

- Firebase Firestore: Realtime chat and sync SOZIP data
- Firebase Cloud Functions: Process backend business logic for reduce client burden
- Firebase Cloud Messaging: Send push notifications to target user when created new SOZIP and chat

### External API

- Naver Maps API (Render location based markers)

## 🏗️ Architecture

```mermaid
graph TD
    %% Client
    subgraph Client [📱 Android App]
        UI[Jetpack Compose]
        State[ViewModel & StateFlow]
    end

    %% Backend & Infra
    subgraph Serverless [☁️ Firebase BaaS]
        Auth[Firebase Auth]
        DB[Firebase Firestore]
        Storage[Firebase Storage]
        Functions[Firebase Cloud Functions]
        FCM[Firebase Cloud Messaging]
    end

    %% External API
    subgraph External [External APIs]
        Map[Naver Maps API]
    end

    %% Data flow
    UI <--> State
    State <--> Auth
    State <--> DB
    State <--> Storage
    State --> Map

    %% Push notifications and BE logic flow
    DB -->|Event Trigger| Functions
    Functions -->|Send Push Request| FCM
    FCM -.->|Push Notification| UI
```

## 🧱 If I were to rebuild it in 2026

| Layer | Original | 2026 Pick | Reason |
|---|---|---|---|
| UI | Jetpack Compose (M2 + M3) | Jetpack Compose + Material 3 | Consistent design system |
| State management | `companion object` + callback | `ViewModel` + `StateFlow` + `collectAsStateWithLifecycle` | Lifecycle-aware, test available |
| Async | `addOnCompleteListener` nested callback | `suspend fun` + `kotlinx-coroutines-play-services` `.await()` | Serialized code, clerable error handling |
| Encrypt | NDK + `Android.mk` (AES256) | Android keystore system + `javax.crypto` | Isolate the key in TEE, unnecessary NDK |
| Process date | `SimpleDateFormat` | `java.time.format.DateTimeFormatter` | Thread-safe, unnecessary desugaring in minSdk 29 |

## ✨ Core Features<br>
<details>
<summary>Show Contents</summary>

#### Home<br>
> Check any SOZIPs near by you, or notifications <br>

![ ](README/img_home.png)
![ ](README/img_notifications.png)<br>

#### SOZIP Map<br>
> Check and contact to all SOZIPs <br>

![ ](README/img_sozipMap.png)<br>

#### SOZIP<br>
> Check location, participants, or manage your SOZIP in SOZIP Details<br>

![ ](README/img_details.png)<br>

> Don't you have a favorite SOZIP? Feel free to make it!<br>

![ ](README/img_addSOZIP_1.png)
![ ](README/img_addSOZIP_2.png)<br>

#### Chat<br>
> Feel free to talk to the participants <br>

![ ](README/img_chat_1.png)<br>

> Or you can send any images, bank accounts, or manage your SOZIP. it's so easy. <br>

![ ](README/img_chat_2.png)<br>

#### Profile<br>
> Show others your unique personality in your profile <br>

![ ](README/img_profile.png)<br>

#### Feedback Hub<br>
> Improve 소집 : SOZIP with your opinion. <br>

![ ](README/img_feedbackHub.png)<br>

#### And so much more.<br>
> Notice, Consult, or you can change your informations <br>

![ ](README/img_more.png)

</details>
