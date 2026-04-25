# 🍲 Homely Bites

A dual-workflow Android food delivery app connecting **customers** with **local home cooks**. Built with Java, Material Design 3, and Firebase.

---

## ✨ Features

### Customer Flow
- **Browse & Search** — Discover nearby cloud kitchens by category (North Indian, South Indian, Chinese, etc.)
- **Kitchen Detail** — View menus, add dishes to cart, and place orders
- **Order Management** — Track order status (Waiting → Accepted → Ongoing → Delivered)
- **Favorites** — Save and manage favorite kitchens
- **Ratings & Reviews** — Rate your experience after delivery
- **Profile** — View stats, change address, access help center

### Cook Flow
- **Dashboard** — Manage incoming order requests (Accept/Reject)
- **Menu Management** — Add dishes with pricing, servings, and day-of-week scheduling
- **Reliability Score** — Track performance metrics (orders completed, avg rating)
- **Order History** — View all past orders

### Shared
- **Become a Cook** — Customers can register their own kitchen
- **Dual Auth** — Separate login flows for customers and cooks
- **Password Reset** — Firebase-powered email reset

---

## 🏗️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 11 |
| UI | Material Design 3, ViewBinding |
| Auth | Firebase Authentication (Email/Password) |
| Database | Cloud Firestore |
| Storage | Firebase Storage |
| Images | Glide 4.16 |
| Navigation | Bottom Navigation + ViewPager2 + TabLayout |
| Min SDK | 24 (Android 7.0) |
| Target SDK | 35 |

---

## 📁 Project Structure

```
app/src/main/java/com/example/homelybites/
├── activities/          # 15 Activity classes
│   ├── SplashActivity          # Auto-routes by user role
│   ├── CustomerLoginActivity   # Customer auth (login/signup)
│   ├── CookLoginActivity       # Cook auth (login/signup + kitchen creation)
│   ├── ForgotPasswordActivity  # Password reset
│   ├── CustomerHomeActivity    # Main screen with bottom nav
│   ├── KitchenDetailActivity   # Menu browsing + cart
│   ├── OrderSummaryActivity    # Checkout with address & payment
│   ├── RatingActivity          # Post-delivery review
│   ├── CookDashboardActivity   # Cook home with tabs
│   ├── AddDishActivity         # Add menu items
│   ├── OrderHistoryActivity    # Past orders list
│   ├── ReliabilityScoreActivity# Cook performance stats
│   ├── BecomeCookActivity      # Kitchen registration
│   ├── HelpCenterActivity      # FAQ
│   └── ChangeAddressActivity   # Address management
├── fragments/           # 6 Fragment classes
│   ├── HomeFragment            # Categories + nearby kitchens
│   ├── SearchFragment          # Real-time Firestore search
│   ├── FavoritesFragment       # Saved kitchens
│   ├── ProfileFragment         # User stats + settings
│   ├── RequestsFragment        # Pending orders (cook)
│   └── MenuFragment            # Dish management (cook)
├── adapters/            # 4 RecyclerView adapters
│   ├── CategoryAdapter
│   ├── KitchenAdapter
│   ├── DishAdapter
│   └── OrderAdapter
├── models/              # 5 Data models
│   ├── User, Kitchen, Dish, Order, Review
└── utils/
    └── FirebaseHelper   # Singleton for all Firebase operations
```

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug or newer
- JDK 11+
- Firebase project with Email/Password auth enabled

### Setup

1. **Clone the repo**
   ```bash
   git clone https://github.com/your-username/Homelybites.git
   ```

2. **Firebase Configuration**
   - Create a project at [Firebase Console](https://console.firebase.google.com/)
   - Enable **Authentication** → Email/Password
   - Create a **Firestore Database** (start in test mode)
   - Download `google-services.json` and place it in `app/`

3. **Firestore Security Rules** (for development)
   ```
   rules_version = '2';
   service cloud.firestore {
     match /databases/{database}/documents {
       match /{document=**} {
         allow read, write: if request.auth != null;
       }
     }
   }
   ```

4. **Build & Run**
   - Open in Android Studio → Sync Gradle → Run on device/emulator

---

## 🗄️ Firestore Collections

| Collection | Description |
|-----------|-------------|
| `users` | User profiles (customers & cooks) |
| `kitchens` | Registered kitchen details |
| `dishes` | Menu items per kitchen |
| `orders` | Order records with status tracking |
| `reviews` | Ratings & comments per kitchen |

---

## 📸 Screens

| Auth | Customer | Cook |
|------|----------|------|
| Splash | Home (Categories + Kitchens) | Dashboard (Requests + Menu) |
| Customer Login/Signup | Search | Add Dish |
| Cook Login/Signup | Kitchen Detail + Cart | Reliability Score |
| Forgot Password | Order Summary | Order History |
| | Rating & Review | |
| | Favorites / Profile | |

---

## 🎨 Design

- **Color Palette**: Warm orange (#F57C00) primary with cream (#FFFDF5) backgrounds
- **Typography**: Material3 defaults
- **Components**: Rounded cards (16dp), pill buttons (12dp radius), custom status badges
- **Theme**: Light mode with Material3 `DayNight.NoActionBar`

---

## 📄 License

This project is for educational purposes as part of the MAD (Mobile Application Development) course project.

---

## 👤 Author

**Parikshit Kumar**

---
