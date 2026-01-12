# UPM Event Announcement Board 📢

A JavaFX-based digital announcement board application for Universiti Putra Malaysia (UPM) that allows admins to post event announcements and students to view them in a centralized platform.

---

## 📋 Table of Contents

- [Problem Statement](#problem-statement)
- [Solution](#solution)
- [Features](#features)
- [Architecture](#architecture)
- [File Structure](#file-structure)
- [Database Schema](#database-schema)
- [Security Features](#security-features)
- [How to Run](#how-to-run)
- [User Guide](#user-guide)
- [Technical Details](#technical-details)

---

## 🎯 Problem Statement

Currently, UPM events are announced and shared through WhatsApp groups, which creates several issues:

- **No Central Location**: Events scattered across multiple group chats
- **Easily Missed**: Messages get buried in conversations
- **No Persistence**: Difficult to find past announcements
- **No Organization**: Hard to categorize or search events

## 💡 Solution

A dedicated **Digital Announcement Board** where:

- **Admins** can post event announcements with images and descriptions
- **Students** can view all events in a clean, scrollable feed
- **Central Platform**: Single source of truth for all UPM events
- **Persistent Storage**: Events stored in database for future reference

---

## ✨ Features

### 🏠 **HomePage** (Public View)

- **Event Feed**: Scrollable list of all active event announcements
- **Post Cards**: Each event displays:
  - User information (name, faculty, timestamp)
  - Event category badge
  - Event description with "See more" for long text
  - Event image
  - Share button (copy link functionality)
- **Refresh Button**: Reload posts to see latest events
- **Admin Login**: Gateway to event posting features
- **Singleton Windows**: Prevents duplicate login popups

### 🔐 **LogInPage** (Admin Only)

- **Authentication**: Validates matric number and password against database
- **Security**: PreparedStatement prevents SQL injection attacks
- **Error Messages**: Clear feedback for invalid credentials
- **Auto-redirect**: Opens NewEventPage on successful login
- **Singleton Management**: Only one NewEventPage can be open at a time

### 📝 **NewEventPage** (Admin Only)

- **Event Creation Form**:
  - Event Description (required): Multi-line text area
  - Event Category (optional): Text field for categorization
  - Event Image (required): File chooser for PNG/JPG/JPEG/GIF
- **Image Preview**: Live preview before publishing
- **Form Validation**: Ensures all required fields filled
- **Success Dialog**: Confirmation overlay after publishing
- **Auto-reset**: Form clears after successful publish
- **Database Integration**: Saves to AB_POSTS and AB_IMAGES tables

### 🗄️ **DBConnection** (Utility)

- **Connection Management**: Centralized database connection factory
- **Resource Safety**: Encourages try-with-resources pattern
- **Oracle Database**: Connects to UPM FSKTM server

---

## 🏗️ Architecture

### Application Flow

```
Entry Point: HomePage.java (main)
     │
     ├─► [Public Users]
     │   └─► View event feed
     │   └─► Click share button
     │   └─► Click refresh to see new events
     │
     └─► [Admin Users]
         └─► Click "Admin Login" button
         └─► LogInPage.java opens
             │
             ├─► Invalid credentials → Error message
             │
             └─► Valid credentials → NewEventPage.java opens
                 └─► Fill event form
                 └─► Upload image
                 └─► Click "Publish Event"
                 └─► Event saved to database
                 └─► Visible on HomePage after refresh
```

### Singleton Pattern

The application implements singleton window management to prevent duplicate windows:

- **HomePage**: Tracks `loginStageInstance` - only one login window at a time
- **LogInPage**: Tracks `newEventStageInstance` - only one event creation window at a time
- **Behavior**: If window already open → bring to front instead of creating duplicate

### Database Connection Strategy

**Why Fresh Connection on Refresh?**

Oracle databases use **transaction isolation** - connections maintain a "snapshot" of data from when the transaction started. Even if other users add/delete posts, your connection won't see those changes.

**Solution**: When refresh button clicked:

1. Close old database connection
2. Create new database connection (sees current state)
3. Query database with fresh view
4. Rebuild UI with updated posts

---

## 📁 File Structure

```
Project_AnnouncementBoard/
│
├── DBConnection.java          # Database connection factory
│   ├── getConnection()        # Returns new Connection (recommended)
│   └── InitializeDB()         # Deprecated - returns Statement
│
├── HomePage.java              # Main public-facing page
│   ├── start()                # Initialize UI and load posts
│   ├── createHeading()        # Header with logo, title, buttons
│   ├── refreshPosts()         # Reload posts with fresh DB connection
│   ├── loadPosts()            # Query database and build post cards
│   ├── createPostCard()       # Build individual post card UI
│   └── cleanup()              # Close database connection
│
├── LogInPage.java             # Admin authentication page
│   ├── start()                # Build login form UI
│   ├── authenticateUser()     # Validate credentials with PreparedStatement
│   └── cleanup()              # Close database connection
│
├── NewEventPage.java          # Event creation page (admin only)
│   ├── start()                # Build event creation form
│   ├── createHeader()         # User info header
│   ├── createContentArea()    # Form with caption, category, image
│   ├── fetchUserInfo()        # Get admin name from database
│   ├── publishEvent()         # Save image and insert to database
│   ├── saveImage()            # Copy image to local filesystem
│   ├── showSuccessDialog()    # Display success confirmation
│   └── cleanup()              # Close database connection
│
├── images/                    # Uploaded event images stored here
│   └── {timestamp}_{filename} # e.g., 1736748923456_event_poster.jpg
│
└── README.md                  # This documentation file
```

---

## 🗃️ Database Schema

### Tables Used

#### **AB_USERS**

Stores user/admin information

```sql
MATRIC_NUM      VARCHAR2   (Primary Key)
NAME            VARCHAR2
EMAIL           VARCHAR2
PASSWORD_HASH   VARCHAR2   (TODO: Implement actual hashing)
FACULTY         VARCHAR2
ROLE            VARCHAR2   (e.g., 'admin')
CREATED_AT      TIMESTAMP
```

#### **AB_IMAGES**

Stores event image metadata

```sql
IMAGE_ID        NUMBER     (Primary Key)
FILE_NAME       VARCHAR2
FILE_PATH       VARCHAR2   (Relative path: 'images/filename')
BACKUP_URL      VARCHAR2
MIME_TYPE       VARCHAR2
WIDTH_PX        NUMBER
HEIGHT_PX       NUMBER
SIZE_BYTES      NUMBER
UPLOADED_AT     TIMESTAMP
```

#### **AB_POSTS**

Stores event announcement posts

```sql
POST_ID         NUMBER     (Primary Key)
MATRIC_NUM      VARCHAR2   (Foreign Key → AB_USERS)
UPLOAD_TIME     TIMESTAMP
CAPTION         CLOB
IMAGE_ID        NUMBER     (Foreign Key → AB_IMAGES)
CATEGORY        VARCHAR2   (Optional)
SHARE_SLUG      VARCHAR2
ACTIVE_STATUS   NUMBER     (1 = active, 0 = inactive)
```

### Query Examples

**Load Posts (HomePage)**

```sql
SELECT p.POST_ID, p.CAPTION, p.UPLOAD_TIME, p.CATEGORY, p.SHARE_SLUG,
       u.NAME, u.FACULTY,
       i.FILE_PATH, i.FILE_NAME, i.WIDTH_PX, i.HEIGHT_PX
FROM AB_POSTS p
LEFT JOIN AB_USERS u ON p.MATRIC_NUM = u.MATRIC_NUM
LEFT JOIN AB_IMAGES i ON p.IMAGE_ID = i.IMAGE_ID
WHERE p.ACTIVE_STATUS = ?
ORDER BY p.UPLOAD_TIME DESC
```

**Authenticate Admin (LogInPage)**

```sql
SELECT MATRIC_NUM, PASSWORD_HASH, ROLE
FROM AB_USERS
WHERE MATRIC_NUM = ? AND PASSWORD_HASH = ?
```

**Insert New Post (NewEventPage)**

```sql
-- First, insert image
INSERT INTO AB_IMAGES (IMAGE_ID, FILE_NAME, FILE_PATH, UPLOADED_AT)
VALUES (?, ?, ?, SYSTIMESTAMP)

-- Then, insert post
INSERT INTO AB_POSTS (POST_ID, MATRIC_NUM, CAPTION, IMAGE_ID, CATEGORY, ACTIVE_STATUS, UPLOAD_TIME)
VALUES (?, ?, ?, ?, ?, 1, SYSTIMESTAMP)
```

---

## 🔒 Security Features

### 1. **SQL Injection Prevention**

All database queries use **PreparedStatement** with parameter binding:

❌ **VULNERABLE CODE (Never do this!)**

```java
String query = "SELECT * FROM AB_USERS WHERE MATRIC_NUM = '" + userInput + "'";
// Attacker can input: A123' OR '1'='1
// Result: SELECT * FROM AB_USERS WHERE MATRIC_NUM = 'A123' OR '1'='1'
// This returns all users! Authentication bypassed!
```

✅ **SECURE CODE (What we use)**

```java
String query = "SELECT * FROM AB_USERS WHERE MATRIC_NUM = ?";
PreparedStatement pstmt = connection.prepareStatement(query);
pstmt.setString(1, userInput);
// The '?' is safely replaced - input treated as DATA not SQL CODE
// Even if input is "A123' OR '1'='1", it searches for that exact string
```

### 2. **Resource Management**

All database connections use **try-with-resources** to prevent resource leaks:

```java
try (Connection conn = DBConnection.getConnection();
     PreparedStatement pstmt = conn.prepareStatement(query)) {
    // Use connection
} // Automatically closes connection, even if exception occurs
```

### 3. **Password Security**

⚠️ **TODO**: Currently passwords stored as plain text in database

- **Current**: Direct string comparison
- **Future**: Implement BCrypt or PBKDF2 password hashing

---

## 🚀 How to Run

### Prerequisites

1. **Java Development Kit (JDK) 11+**

   - JavaFX included in JDK or added as dependency

2. **Oracle Database Access**

   - Connection string: `jdbc:oracle:thin:@fsktmdbora.upm.edu.my:1521:fsktm`
   - Valid UPM credentials

3. **Database Tables**
   - AB_USERS, AB_IMAGES, AB_POSTS must exist
   - At least one admin user in AB_USERS table

### Running the Application

**Option 1: Run HomePage.java**

```bash
java Project_AnnouncementBoard.HomePage
```

**Option 2: Run LogInPage.java (redirects to HomePage)**

```bash
java Project_AnnouncementBoard.LogInPage
```

**Option 3: Run NewEventPage.java (redirects to HomePage)**

```bash
java Project_AnnouncementBoard.NewEventPage
```

All entry points redirect to **HomePage** as the single application entry point.

---

## 📖 User Guide

### For Students (Public View)

1. **View Events**

   - Application opens to HomePage automatically
   - Scroll through event announcements
   - Click "See more" to expand long captions

2. **Refresh Feed**

   - Click 🔄 **Refresh** button in header
   - Latest events will appear

3. **Share Events**
   - Click 📤 **Share** button on any post
   - Link copied to clipboard (functionality ready for implementation)

### For Admins

1. **Login**

   - Click 👤 **Admin Login** button on HomePage
   - Enter matric number and password
   - Click **Login**

2. **Create Event**

   - NewEventPage opens after successful login
   - Fill **Event Description** (required)
   - Enter **Event Category** (optional, e.g., "Academic", "Career")
   - Click 📷 **Choose Image** and select event poster (required)
   - Preview appears below
   - Click 📢 **Publish Event**

3. **After Publishing**
   - Success dialog appears: "Event Published Successfully! 🎉"
   - Form clears automatically
   - Create more events or close window
   - Return to HomePage and click Refresh to see new event

---

## 🔧 Technical Details

### Design Patterns Used

1. **Singleton Pattern**: Window management to prevent duplicates
2. **Factory Pattern**: DBConnection.getConnection() for connection creation
3. **MVC-like Separation**: UI creation methods separated from logic

### UI Components

**Color Theme: Pink Material Design**

- Primary: `#e91e63` (Pink 500)
- Background: `#fce4ec` (Pink 50)
- Hover: `#f8bbd0` (Pink 100)
- Dark Accent: `#c2185b` (Pink 700)

**Typography**

- Font Family: Segoe UI
- Font Smoothing: LCD (`-fx-font-smoothing-type: lcd`)
- Sizes: 11px–32px depending on hierarchy

**Layout Structure**

- BorderPane: Main layout container
- VBox: Vertical stacking (posts, forms)
- HBox: Horizontal layout (headers, buttons)
- ScrollPane: Scrollable post feed
- StackPane: Logo wrapper for rounded corners

### Image Handling

**Storage Location**: `HelloFX/src/Project_AnnouncementBoard/images/`

**File Naming**: `{timestamp}_{original_filename}`

- Example: `1736748923456_event_poster.jpg`
- Prevents filename conflicts

**Database Storage**: Relative path stored (`images/filename`)

- Resolved to absolute path when loading: `{projectRoot}/HelloFX/src/Project_AnnouncementBoard/images/filename`

**Image Preview**

- Uses `ImageView` with `setManaged(false)` when invisible
- Collapses properly when cleared after publish
- High-quality rendering with `setSmooth(true)` and `setCache(true)`

### Database Connection Lifecycle

**HomePage**:

- Connection created in `start()`
- Reused for initial `loadPosts()`
- Closed and recreated on each `refreshPosts()`
- Final cleanup in `cleanup()` when window closes

**LogInPage**:

- Connection created in `start()`
- Reused for all authentication attempts
- Closed in `cleanup()` when window closes

**NewEventPage**:

- Connection created in `start()`
- Reused for `fetchUserInfo()` and `publishEvent()`
- Closed in `cleanup()` when window closes

---

## 👥 Credits

**Development Team**: UPM Announcement Board Team  
**Institution**: Universiti Putra Malaysia (UPM)  
**Faculty**: Faculty of Computer Science and Information Technology (FSKTM)  
**Version**: 2.0 - Optimized with PreparedStatement and resource management  
**Date**: January 2026

---

## 🔮 Future Enhancements

- [ ] Implement password hashing (BCrypt/PBKDF2)
- [ ] Add clipboard functionality for share button
- [ ] Implement post editing for admins
- [ ] Add post deletion feature
- [ ] Event search and filtering by category
- [ ] Image upload size validation
- [ ] Event expiration dates
- [ ] Email notifications for new events
- [ ] Mobile responsive web version
- [ ] Rich text editor for event descriptions
- [ ] Analytics dashboard for admins

---

## 📝 License

Internal UPM Project - Educational Use Only

---

## 📧 Support

For questions or issues, contact the FSKTM IT support team.

---

**Happy Announcing! 🎉**
