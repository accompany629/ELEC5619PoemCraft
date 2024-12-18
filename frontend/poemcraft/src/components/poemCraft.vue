<template>
  <div :class="{'dark-mode': isDarkMode, 'light-mode': !isDarkMode}" :style="{ backgroundColor: colors.pageBackgroundColor, color: colors.textColor }">
    <!-- 左侧弹出按钮 -->
    <div class="toggle-left-container">
      <button class="toggle-left" @click="toggleNavbar('left')" :style="{ backgroundColor: colors.buttonColor, color: colors.textColor }">
        <i class="fas fa-bars"></i>
      </button>
    </div>

    <!-- 左侧导航栏 -->
    <div class="navbar hidden" id="left-nav" :style="{ backgroundColor: colors.navbarBackgroundColor, color: colors.textColor }">
      <ul>
        <li><a href="#" @click="AccountPage" title="Account"><i class="fas fa-user"></i></a></li>
        <li><a href="#" @click="toggleSetting" title="Setting"><i class="fas fa-cog"></i></a></li>
        <li><a href="#" @click="logout" title="Log out"><i class="fas fa-sign-out-alt"></i></a></li>
        <li><a href="#" @click="Feedback" title="Feedback"><i class="fas fa-comment-dots"></i></a></li>
        <li><a href="#" @click="goToFavoritesPage" title="Favorites"><i class="fas fa-star"></i></a></li>
      </ul>
      <hr class="navbar-divider">

      <!-- 聊天记录部分 -->
      <div class="chat-list-container">
        <div v-for="chat in chatList.slice().reverse()" :key="chat.chatId" class="chat-item">
          <button class="chat-button" @click="handleChatClick(chat)" :style="{ backgroundColor: colors.buttonColor, color: colors.textColor }">
            {{ chat.description }}
          </button>
        </div>
      </div>
    </div>

    <!-- 设置面板 -->
    <div v-if="showSettingPanel" class="setting-panel">
      <h3 class="setting-title">Customize Colors</h3>
      <div class="setting-option">
        <label>Text Color:</label>
        <input type="color" v-model="colors.textColor" @input="updateColor('textColor', colors.textColor)">
      </div>
      <div class="setting-option">
        <label>Button Color:</label>
        <input type="color" v-model="colors.buttonColor" @input="updateColor('buttonColor', colors.buttonColor)">
      </div>
      <div class="setting-option">
        <label>Navbar Background Color:</label>
        <input type="color" v-model="colors.navbarBackgroundColor" @input="updateColor('navbarBackgroundColor', colors.navbarBackgroundColor)">
      </div>
      <div class="setting-option">
        <label>Top/Bottom Box Color:</label>
        <input type="color" v-model="colors.boxBackgroundColor" @input="updateColor('boxBackgroundColor', colors.boxBackgroundColor)">
      </div>
      <button @click="toggleSetting" :style="{ backgroundColor: colors.buttonColor, color: colors.textColor }">Close</button>
    </div>

    <!-- 右侧弹出按钮 -->
    <div class="toggle-right-container">
      <button class="toggle-right" @click="toggleNavbar('right')" :style="{ backgroundColor: colors.buttonColor, color: colors.textColor }">
        <i class="fas fa-bars"></i>
      </button>
    </div>

    <!-- 右侧导航栏 -->
    <div class="navbar-right hidden" id="right-nav" :style="{ backgroundColor: colors.navbarBackgroundColor, color: colors.textColor }">
      <button class="toggle-inside-right" @click="toggleNavbar('right')" :style="{ backgroundColor: colors.buttonColor, color: colors.textColor }"><i class="fas fa-bars"></i></button>
      <h5 class="style-title">Style</h5>
      <ul class="right-nav-list">
        <li><button class="style-button" @click="selectStyle('Classical Style')" :style="{ backgroundColor: colors.buttonColor, color: colors.textColor }"><i class="fas fa-book"></i> Classical Style</button></li>
        <li><button class="style-button" @click="selectStyle('Romanticism')" :style="{ backgroundColor: colors.buttonColor, color: colors.textColor }"><i class="fas fa-book"></i> Romanticism</button></li>
        <li><button class="style-button" @click="selectStyle('Realism')" :style="{ backgroundColor: colors.buttonColor, color: colors.textColor }"><i class="fas fa-book"></i> Realism</button></li>
        <li><button class="style-button" @click="selectStyle('Modernism')" :style="{ backgroundColor: colors.buttonColor, color: colors.textColor }"><i class="fas fa-book"></i> Modernism</button></li>
        <li><button class="style-button" @click="selectStyle('Symbolism')" :style="{ backgroundColor: colors.buttonColor, color: colors.textColor }"><i class="fas fa-book"></i> Symbolism</button></li>
        <li><button class="style-button" @click="selectStyle('Avant-garde')" :style="{ backgroundColor: colors.buttonColor, color: colors.textColor }"><i class="fas fa-book"></i> Avant-garde</button></li>
        <li><button class="style-button" @click="selectStyle('Colloquial Style')" :style="{ backgroundColor: colors.buttonColor, color: colors.textColor }"><i class="fas fa-book"></i> Colloquial Style</button></li>
      </ul>
      <h5 class="style-title">Topic</h5>
      <ul class="right-nav-list">
        <li><button class="topic-button" @click="selectTopic('Love')" :style="{ backgroundColor: colors.buttonColor, color: colors.textColor }"><i class="fas fa-palette"></i> Love</button></li>
        <li><button class="topic-button" @click="selectTopic('Nature')" :style="{ backgroundColor: colors.buttonColor, color: colors.textColor }"><i class="fas fa-palette"></i> Nature</button></li>
        <li><button class="topic-button" @click="selectTopic('Philosophy')" :style="{ backgroundColor: colors.buttonColor, color: colors.textColor }"><i class="fas fa-palette"></i> Philosophy</button></li>
        <li><button class="topic-button" @click="selectTopic('History and Heroism')" :style="{ backgroundColor: colors.buttonColor, color: colors.textColor }"><i class="fas fa-palette"></i> History and Heroism</button></li>
        <li><button class="topic-button" @click="selectTopic('Society and Politics')" :style="{ backgroundColor: colors.buttonColor, color: colors.textColor }"><i class="fas fa-palette"></i> Society and Politics</button></li>
      </ul>
    </div>

    <!-- 标题栏 -->
    <div id="title-bar" :style="{ backgroundColor: colors.boxBackgroundColor, color: colors.textColor }">
      <h1>POEMCRAFT</h1>
      <!-- <button @click="toggleTheme" :style="{ backgroundColor: colors.buttonColor, color: colors.textColor }">
        <i class="fas fa-paint-brush"></i> Change Style
      </button> -->
      <button id="new-chat-button" @click="startNewChat" :style="{ backgroundColor: colors.buttonColor, color: colors.textColor }">
        <i class="fas fa-plus"></i> New Chat
      </button>
    </div>

    <!-- 聊天展示区域 -->
    <div id="chat-area">
      <p>Welcome to PoemCraft Chatbot!</p>
      <p>Please select your preference from the right side</p>

      <div class="chat-container">
        <div v-for="message in chatHistory" :key="message.id" class="chat-message-container">

          <!-- 用户消息，左侧对齐 -->
          <div v-if="message.userMessage" class="user-message message left-message">
            <strong>You:</strong>
            <span v-for="line in message.userMessage.split('\n')" :key="line">
              {{ line }}<br />
            </span>
          </div>

          <!-- 机器人消息，右侧对齐 -->
          <div v-if="message.botResponse" class="bot-message message right-message">
            <strong>Bot:</strong>
            <span v-for="line in message.botResponse.split('\n')" :key="line">
              {{ line }}<br />
            </span>

            <button 
              class="favorite-button" 
              @click="toggleFavorite(message)" 
              :style="{ color: message.isFavorite ? colors.favoriteColor : colors.unfavoriteColor }">
              <i class="fas fa-star"></i>
            </button>


          </div>
        </div>
      </div>
    </div>

    <!-- 输入框 -->
    <div id="input-bar" :style="{ backgroundColor: colors.boxBackgroundColor }">
      <input
        type="text"
        v-model="userInput"
        placeholder="Please talk with me, I am a poem expert..."
        @keyup.enter="sendMessage"
      >
      <button @click="sendMessage" :style="{ backgroundColor: colors.buttonColor, color: colors.textColor }">
        <i class="fas fa-paper-plane"></i> Send
      </button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'PoemCraft',
  data() {
    return {
      isDarkMode: false,
      userInput: '',
      chatHistory: [],
      genre: '',
      theme: '',
      topic: '',
      chatList: [],
      chatId: null,
      showSettingPanel: false,
      colors: {
        textColor: '#222', // 文本颜色
        buttonColor: '#f5f5f5', // 按钮颜色
        navbarBackgroundColor: '#d3d3d3', // 左右导航栏背景颜色
        boxBackgroundColor: '#d3d3d3', // 上下框背景颜色

        favoriteColor: '#ff4500', // 收藏颜色（橙色）
        unfavoriteColor: '#ccc' // 未收藏颜色（灰色）
      }
    };
  },
  created() {
    this.getUserChats();
  },
  methods: {
  async toggleFavorite(message) {


  // 构造发送给后端的 JSON 数据，不包含 title 字段
  const poemData = {
    content: message.content,  // 使用传入的 message 参数中的内容
    genre: message.genre,      // 使用传入的 message 参数中的风格
    theme: message.theme       // 使用传入的 message 参数中的主题
  };


  console.log("Message:", message);
  console.log("Genre:", message.genre);
  console.log("Content:", message.content);

  try {
    // 从 localStorage 获取用户的 JWT token
    const token = localStorage.getItem('token');
    if (!token) {
      alert("Please login to continue!");
      return;
    }

    // 使用 axios 发送 POST 请求
    const response = await axios.post('/api/poems/add', poemData, {
      headers: {
        'Authorization': `Bearer ${token}`,  // 添加授权头部
        'Content-Type': 'application/json'   // 设置请求体的内容类型为 JSON
      }
    });

    // 根据后端响应进行相应的提示
    if (response.status === 200) {
      alert("Poem added successfully!");
      // 可以在这里设置 message 为已收藏状态，比如变更按钮样式
      message.isFavorite = true;
    } else {
      alert("Failed to add poem.");
    }
  } catch (error) {
    console.error("Error adding poem:", error);
    alert("An error occurred while adding the poem.");
  }
},

    startNewChat() {
      const rightNav = document.getElementById('right-nav');
      if (!rightNav.classList.contains('hidden')) {
        this.toggleNavbar('right');
      }

      // 重置当前聊天的相关数据
      this.toggleNavbar('right');
      this.chatHistory = [];
      this.userInput = '';
      this.chatId = null;
      this.genre = '';
      this.topic = '';
      // alert('New chat started!');
    },
    toggleSetting() {
      this.showSettingPanel = !this.showSettingPanel;
    },
    updateColor(property, value) {
      this.colors[property] = value;
    },
    toggleNavbar(side) {
      if (side === 'left') {
        const leftNav = document.getElementById('left-nav');
        leftNav.classList.toggle('hidden');

        const chatArea = document.getElementById('chat-area');
        const inputBar = document.getElementById('input-bar');
        const toggleLeftContainer = document.querySelector('.toggle-left-container');

        if (!leftNav.classList.contains('hidden')) {
          chatArea.style.marginLeft = '220px';
          inputBar.style.marginLeft = '220px';
          toggleLeftContainer.style.left = '220px';
        } else {
          chatArea.style.marginLeft = '0';
          inputBar.style.marginLeft = '0';
          toggleLeftContainer.style.left = '0';
        }
      } else if (side === 'right') {
        const rightNav = document.getElementById('right-nav');
        rightNav.classList.toggle('hidden');

        const chatArea = document.getElementById('chat-area');
        const inputBar = document.getElementById('input-bar');
        const toggleRightContainer = document.querySelector('.toggle-right-container');

        const newChatButton = document.getElementById('new-chat-button'); //


        if (!rightNav.classList.contains('hidden')) {
          chatArea.style.marginRight = '220px';
          inputBar.style.marginRight = '220px';
          toggleRightContainer.style.right = '220px';


          newChatButton.style.marginRight = '220px';
        } else {
          chatArea.style.marginRight = '0';
          inputBar.style.marginRight = '0';
          toggleRightContainer.style.right = '0';


          newChatButton.style.marginRight = '0';
        }
      }
    },
    toggleTheme() {
      this.isDarkMode = !this.isDarkMode;
    },

    goToFavoritesPage() {
    this.$router.push('/favorites');
  },


    async getUserChats() {
      const token = localStorage.getItem('token');
      if (!token) {
        alert("Please login to continue!");
        return;
      }

      try {
        const response = await axios.get('/api/chats/user', {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });

        this.chatList = response.data.map(chat => ({
          chatId: chat.chatId,
          description: chat.description
        }));

      } catch (error) {
        console.error('Error fetching chat history:', error);
      }
    },
    async handleChatClick(chat) {
      const token = localStorage.getItem('token');
      if (!token) {
        alert("Please login to continue!");
        return;
      }

      try {
        this.chatHistory = [];
        const ChatID = chat.chatId;

        const contentResponse = await axios.get('/api/content/getContent', {
          params: { ChatID: ChatID },
          headers: { Authorization: `Bearer ${token}` }
        });

        const contentList = contentResponse.data;

        contentList.forEach((content) => {
          if (content.role === 'user') {
            this.chatHistory.push({
              id: this.chatHistory.length + 1,
              userMessage: content.text,
              botResponse: null
            });
          } else if (content.role === 'Bot') {
            this.chatHistory.push({
              id: this.chatHistory.length + 1,
              userMessage: null,
              botResponse: content.text
            });
          }
        });

      } catch (error) {
        console.error('获取内容数据时出错：', error);
      }
    },
    selectStyle(style) {
      this.genre = style;
      console.log(`Selected style: ${style}`);
    },
    selectTopic(topic) {
      this.topic = topic;
      console.log(`Selected topic: ${topic}`);
    },
    async sendMessage() {
      if (this.userInput.trim() === '') return;

      const userMessage = this.userInput;
      this.userInput = '';

      const token = localStorage.getItem('token');
      if (!token) {
        alert("Please login to continue!");
        return;
      }

      try {
        if (!this.chatId) {
          const chatResponse = await axios.post('/api/chats/create', {
            theme: this.topic,
            genre: this.genre,
            topic: this.topic,
            starStatus: true
          }, {
            headers: { Authorization: `Bearer ${token}` }
          });

          this.chatId = chatResponse.data.chatId;
        }

        const response = await axios.post('/api/content/generate', {
          input: userMessage,
          chatId: this.chatId
        }, {
          headers: { Authorization: `Bearer ${token}` }
        });

        const botResponse = response.data.text;

        this.chatHistory.push({
          id: this.chatHistory.length + 1,
          userMessage: userMessage,
          botResponse: botResponse,
          content: botResponse,  // 添加 content，保存 bot 的生成内容
          genre: this.genre,      // 添加 genre 属性
          theme: this.topic       // 添加 theme 属性
        });

      } catch (error) {
        console.error('Error fetching chat response:', error);
        this.chatHistory.push({
          id: this.chatHistory.length + 1,
          userMessage: userMessage,
          botResponse: 'Error: Unable to get response from server',
          content: 'Error: Unable to get response from server',  // 即使报错，也添加 content
          genre: this.genre,
          theme: this.topic
        });
      }
    },
    logout() {
      this.$router.push('/');
    },
    Feedback() {
      this.$router.push('/feedback');
    },
    AccountPage() {
      this.$router.push('/account');
    },
  }
};
</script>

<style scoped>
.setting-panel {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: #fff;
  border: 1px solid #ccc;
  padding: 20px;
  z-index: 1000;
  width: 300px;
  border-radius: 8px;
}

.setting-option {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 10px;
}

.setting-title {
  text-align: center;
  margin-bottom: 20px;
  color: #ff4500;
}

input[type="color"] {
  margin-left: 10px;
}

body {
  background-color: #333;
  color: #fff;
  font-family: Arial, sans-serif;
  margin: 0;
  padding: 0;
}

.dark-mode {
  background-color: #333;
  color: #fff;
}

.light-mode {
  background-color: #fff;
  color: #000;
}

#title-bar,
#chat-area,
#input-bar,
.navbar,
.navbar-right {
  background-color: inherit;
  color: inherit;
}

.dark-mode,
.light-mode {
  transition: background-color 0.3s ease, color 0.3s ease;
}

.dark-mode #chat-area {
  background-color: #000000;
  color: #ffffff;
}

.toggle-inside {
  position: absolute;
  top: 400px;
  right: -20px;
  background-color: #ff0000;
  border: none;
  color: white;
  /* width: 30px;
  height: 30px; */
  cursor: pointer;
  z-index: 1001;
  padding: 10px;
  border-radius: 5px;
}

.toggle-insideright{
  position: absolute;
  top: 400px;
  right: 220px;
  background-color: #ff0000;
  border: none;
  color: white;
  cursor: pointer;
  z-index: 1001;
  padding: 10px;
  border-radius: 5px;
}


.navbar {
  width: 200px;
  background-color: #222;
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0px;
  z-index: 1;
  padding: 10px;
  transition: all 0.3s ease;
}

.navbar.hidden {
  transform: translateX(-220px);
}

.navbar ul {
  padding: 0;
  margin: 0;
  list-style-type: none;
  display: flex; 
  flex-direction: row; 
  justify-content: space-around; 
}

.navbar li a {
  color: #fff;
  text-decoration: none;
  display: block;
  position: relative; /* 为提示信息定位 */
}

.navbar li i {
  font-size: 24px; /* 调整图标大小 */
}

.navbar-right.hidden {
  transform: translateX(220px);
}

/* .navbar ul {
  padding: 0;
  list-style-type: none;
} */

.navbar li {
  margin: 20px 0;
}

.navbar li a {
  color: #fff;
  text-decoration: none;
  display: flex;
  align-items: center;
}

.navbar li a:hover {
  color: #ff4500;
}

.navbar li i {
  margin-right: 10px;
}

#title-bar {
  position: absolute;
  left: 0px;
  border: none;
  top: 0px;
  background-color: #222;
  width: 100%;
  display: flex;
  justify-content: space-between;
  justify-content: center; /* 水平居中 */
}

#title-bar h1 {
  margin: 0;
  font-size: 1.5rem;
  color: #ff4500;
  align-items: center;
}

#title-bar button {
  position: absolute;
  height: 27px;
  right: 0px;
  color: #fff;
  background-color: #ff4500;
  border: none;
  padding: 5px 10px;
  cursor: pointer;
}

#chat-area {
  position: absolute;
  flex-grow: 1; /* 让它占满剩余空间 */
  left: 0px; 
  right: 0px; 
  top: 27px; 
  bottom: 0; 
  padding: 20px;
  background-color: #ffffff;
  height: calc(95vh - 27px); 
  overflow-y: auto;
  border-radius: 5px;
  box-sizing: border-box; 
  /* box-shadow: inset 5px 5px 15px rgba(0, 0, 0, 0.2), inset -5px -5px 15px rgba(0, 0, 0, 0.2);  */
}

#input-bar {
  position: absolute;
  flex-grow: 1; /* 让它占满剩余空间 */
  left: 0px;
  right: 0px;
  bottom: 0;
  padding: 10px;
  background-color: #222;
  display: flex;
  align-items: center;
  justify-content: center;
}

#input-bar input {
  width: 80%;
  padding: 10px;
  border: none;
  border-radius: 5px;
  margin-right: 10px;
}

#input-bar button {
  padding: 10px 20px;
  background-color: #ff4500;
  border: none;
  color: white;
  border-radius: 5px;
  cursor: pointer;
}

#input-bar button:hover {
  background-color: #ff6347;
}

.navbar-right {
  right: 0;
  width: 200px;
  background-color: #222;
  position: fixed;
  top: 0;
  bottom: 0;
  z-index: 1;
  padding: 10px;
  transition: all 0.3s ease;
}

.navbar .toggle, .navbar-right .toggle {
  cursor: pointer;
  padding: 10px;
  background-color: #ff4500;
  color: #fff;
  border: none;
  border-radius: 5px;
  margin-bottom: 20px;
  display: inline-block;
}

/* 左侧按钮容器 */
.toggle-left-container {
  position: fixed;
  top: 400px;
  left: 0px;
  width: 30px; /* 调整悬停区域的宽度 */
  height: 50px; /* 调整悬停区域的高度 */
  z-index: 1000;
}

.toggle-left-container:hover >>> .toggle-left {
  opacity: 1;
}

.toggle-left {
  background-color: #ff4500;
  border: none;
  color: white;
  padding: 10px;
  border-radius: 5px;
  cursor: pointer;
  opacity: 0; 
}

/* 右侧按钮容器 */
.style-title {
    color: #ff4500; /* 设置为指定的颜色 */
    margin: 10px 0; /* 添加上下间距 */
    font-size: 20px; /* 设置字体大小，您可以根据需要调整 */
}
.toggle-right-container {
  position: fixed;
  top: 400px;
  right: 0px;
  width: 30px; /* 调整悬停区域的宽度 */
  height: 50px; /* 调整悬停区域的高度 */
  z-index: 1000;
}

.toggle-right-container:hover >>> .toggle-right {
  opacity: 1;
}

.toggle-right {
  background-color: #ff4500;
  border: none;
  color: white;
  padding: 10px;
  border-radius: 5px;
  cursor: pointer;
  opacity: 0; 
}

.navbar-divider {
  border: none;
  border-top: 1px solid #fff;
  margin: 10px 0;
  width: 100%;
}

.style-button,
.topic-button {
    background-color: #ff4500; /* 按钮背景颜色 */
    color: white; /* 按钮文字颜色 */
    border: 2px solid transparent; /* 边框颜色 */
    border-radius: 5px; /* 圆角 */
    padding: 10px; /* 内边距 */
    margin: 5px 0; /* 上下间距 */
    cursor: pointer; /* 鼠标悬停时显示手型 */
    display: flex; /* 使用 Flexbox */
    align-items: center; /* 垂直居中 */
    justify-content: flex-start; /* 左对齐 */
    width: 100%; /* 按钮宽度 */
    font-size: 16px; /* 字体大小 */
    margin-bottom: 8px;
    transition: background-color 0.3s, border-color 0.3s; /* 过渡效果 */
}

.style-button i,
.topic-button i {
  margin-right: 10px; /* 增大图标与文字之间的水平间距 */
}

.style-button:hover,
.topic-button:hover {
    background-color: #f0f0f0; /* 鼠标悬停时背景颜色 */
    border-color: #ccc; /* 鼠标悬停时边框颜色 */
}

/* 聊天信息样式 */
#chat-area {
  display: flex;
  flex-direction: column;
  margin-bottom: 20px; /* 保持输入框与聊天区域的间隔 */
}
.chat-container {
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 10px;
}

.chat-message-container {
  display: flex;
  width: 100%;
}

.left-message {
  align-self: flex-start; /* 左侧对齐 */
  background-color: #f0f0f0;
  padding: 10px;
  border-radius: 10px;
  max-width: 70%;
  margin-bottom: 10px;
}

.right-message {
  align-self: flex-end; /* 右侧对齐 */
  background-color: #d1e7dd;
  padding: 10px;
  border-radius: 10px;
  max-width: 70%;
  margin-bottom: 10px;
}

.user-message {
  margin-right: auto; /* 将用户消息推到左侧 */
}

.bot-message {
  margin-left: auto; /* 将机器人消息推到右侧 */
}

/* 聊天记录部分 */
.chat-list-container {
  margin-top: 20px; /* 添加间距以区分导航和聊天记录 */
  max-height: 855px; /* 设置固定高度，防止过多的记录溢出导航栏 */
  overflow-y: auto;  /* 启用垂直滚动条 */
  padding-right: 5px; /* 防止滚动条遮挡内容 */
}

/* 聊天记录部分 */
.chat-list-container {
  margin-top: 20px; /* 添加间距以区分导航和聊天记录 */
  display: flex;
  flex-direction: column; /* 垂直排列 */
  align-items: stretch; /* 确保每个项占据整个宽度 */
}

.chat-list-container::-webkit-scrollbar {
  width: 6px; /* 设置滚动条的宽度，使其更细 */
}

.chat-list-container::-webkit-scrollbar-thumb {
  background-color: #888; /* 滚动条的颜色，可以根据需要自定义 */
  border-radius: 10px; /* 圆角，使滚动条更美观 */
}

.chat-list-container::-webkit-scrollbar-thumb:hover {
  background-color: #555; /* 当鼠标悬停时滚动条的颜色 */
}

.chat-list-container::-webkit-scrollbar-track {
  background: #f1f1f1; /* 滚动条轨道的颜色 */
}

.chat-item {
  margin-bottom: 10px; /* 每条聊天记录之间的间距 */
}

/* 聊天记录按钮样式 */
.chat-button {
  width: 100%; /* 占满父容器的宽度 */
  padding: 10px;
  background-color: #333; /* 按钮背景色 */
  color: #fff; /* 按钮文字颜色 */
  border: 1px solid #ccc; /* 边框颜色 */
  border-radius: 5px; /* 圆角边框 */
  text-align: left; /* 文本左对齐 */
  cursor: pointer; /* 鼠标悬停时显示手型 */
  box-sizing: border-box; /* 考虑内边距，防止内容溢出 */
}

.chat-button:hover {
  background-color: #444; /* 悬停时改变背景颜色 */
}

.chat-button:focus {
  outline: none;
  box-shadow: 0 0 5px #ff4500; /* 聚焦时添加阴影，增强视觉效果 */
}


.right-nav-list {
  list-style-type: none; /* 移除列表项的黑点 */
  padding-left: 0;       /* 移除左侧的默认内边距 */
  margin-left: 0;        /* 移除左侧的默认外边距 */
}

.favorite-button {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1.2rem; /* 调整图标大小 */
  margin-left: 10px; /* 和文本之间的间距 */
  transition: color 0.3s; /* 平滑过渡颜色变化 */
}

.favorite-button:focus {
  outline: none;
}

</style>