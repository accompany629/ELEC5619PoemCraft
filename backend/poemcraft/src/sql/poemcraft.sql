-- 创建数据库 `poemcraft`
CREATE DATABASE IF NOT EXISTS poemcraft;
USE poemcraft;

-- 刷新机制：每次执行时，删除并重新创建所有表
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS Feedback, FavoritePoems, APIInteractionLog, Content, Poem, Chat, User;
SET FOREIGN_KEY_CHECKS = 1;

-- 重新创建表格
CREATE TABLE IF NOT EXISTS User (
    UserID INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(100) NOT NULL,
    Email VARCHAR(100) NOT NULL UNIQUE,
    salt VARCHAR(24) NOT NULL,
    Password VARCHAR(255) NOT NULL,
    Role VARCHAR(100) NOT NULL,
    Avatar VARCHAR(255),
    Preference TEXT
);

CREATE TABLE IF NOT EXISTS Chat (
    ChatID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT,
    Topic VARCHAR(255),
    Genre VARCHAR(100),
    Theme VARCHAR(100),
    StarStatus BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (UserID) REFERENCES User(UserID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Poem (
    PoemID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT,
    Title VARCHAR(255),
    Content LONGTEXT,
    Genre VARCHAR(100),
    Theme VARCHAR(100),
    FOREIGN KEY (UserID) REFERENCES User(UserID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Content (
    ContentID INT AUTO_INCREMENT PRIMARY KEY,
    ChatID INT,
    Role ENUM('Bot', 'User') NOT NULL,
    Text LONGTEXT,  -- 确保这里是 LONGTEXT
    FOREIGN KEY (ChatID) REFERENCES Chat(ChatID) ON DELETE CASCADE
);



CREATE TABLE IF NOT EXISTS Feedback (
    FeedbackID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT,
    User_Experience INT NULL,
    Functionality INT NULL,
    Content_Quality INT NULL,
    Feedback TEXT,
    FOREIGN KEY (UserID) REFERENCES User(UserID) ON DELETE CASCADE
);

-- 插入数据到 User 表
INSERT INTO User (Username, Email, Salt, Password, Role, Avatar, Preference)
VALUES
    ('Alice', 'alice@example.com', 'random_salt_for_alice', 'hashed_password_for_alice', 'Standard', 'avatar1.jpg', 'Poetry Lover'),
    ('Bob', 'bob@example.com', 'random_salt_for_bob', 'hashed_password_for_bob', 'Premium', 'avatar2.jpg', 'Nature Themes'),
    ('Charlie', 'charlie@example.com', 'random_salt_for_charlie', 'hashed_password_for_charlie', 'Standard', 'avatar3.jpg', 'Romantic Themes');


-- 插入数据到 Chat 表
INSERT INTO Chat (UserID, Topic, Genre, Theme, StarStatus)
VALUES
    (1, 'Nature Poetry', 'Nature', 'Green Earth', TRUE),
    (2, 'Romantic Poems', 'Romance', 'Love and Feelings', FALSE),
    (3, 'Philosophical Poems', 'Philosophy', 'Life and Existence', FALSE);
