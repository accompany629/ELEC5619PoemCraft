<template>
  <div class="favorite-box">
    <div class="favorite-content">
      <img src="poemcraft.jpg" alt="PoemCraft Logo" class="poemcraft-logo" />
      <div class="favorites-box">
        <h2>Welcome to PoemCraft Favorites</h2>
        <p>Here you can find all your favorite poems and manage them easily.</p>
      </div>
    </div>

    <!-- 更新后的内陷盒子，展示用户收藏内容 -->
    <div class="inset-box">
      <h3>Your Favorites Poems:</h3>
      <ul v-if="favorites.length">
        <li v-for="(favorite, index) in favorites" :key="index" class="favorite-item">
          <div class="favorite-genre-item">
            <strong>Genre:</strong> {{ favorite.genre }}
          </div>
          <div class="favorite-theme-item">
            <strong>Theme:</strong> {{ favorite.theme }}
          </div>
          <div class="favorite-content-item">
            <strong>Content:</strong> {{ favorite.content }}
          </div>
        </li>
      </ul>
      <p v-else>No favorite poems available. Start adding some!</p>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'FavoritesPage',
  data() {
    return {
      favorites: [] // 用于存储从后端获取的收藏内容
    };
  },
  created() {
    this.getFavorites(); // 页面创建时调用 getFavorites 方法
  },
  methods: {
    async getFavorites() {
      // 从 localStorage 获取 JWT token
      const token = localStorage.getItem('token');
      if (!token) {
        alert("Please login to continue!");
        return;
      }

      try {
        // 使用 axios 发送 GET 请求到后端
        const response = await axios.get('/api/poems/getPoem', {
          headers: {
            Authorization: `Bearer ${token}` // 添加授权头部
          }
        });

        console.log('Favorites data:', response.data);
        // 将响应数据存储到 favorites 中
        this.favorites = response.data;


      } catch (error) {
        console.error('Error fetching favorites:', error);
      }
    }
  }
};
</script>

<style scoped>

@media (max-width: 768px) {
  .favorite-box {
    width: 90%;
    padding: 20px;
  }
}

@media (min-width: 769px) {
  .favorite-box {
    width: 70%;
    max-width: 1200px;
    padding: 20px;
    margin: 0 auto;
  }
}

.favorite-box {
  display: flex;
  flex-direction: column;
  height: 90vh; /* 设置高度占据视口的90% */
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  position: relative;
  margin-top: 0;
  width: 95%;
  max-width: 1600px; /* 增大最大宽度 */
  padding: 20px;
  margin: 0 auto; /* 使其水平居中 */
}

.favorite-content {
  display: flex;
  align-items: center;
  gap: 20px;
}

.poemcraft-logo {
  width: 100px;
  height: auto;
}

.favorites-box {
  padding: 10px;
}

.favorites-box h2,
.favorites-box p {
  margin: 0;
}

.inset-box {
  width: 97.6%; /* 占满主盒子的宽度 */
  height: 100%; /* 让内陷盒子的高度也竖直铺满 */
  margin-top: 20px;
  padding: 20px;
  background: #f0f0f0;
  border-radius: 10px;
  box-shadow: inset 4px 4px 8px rgba(0, 0, 0, 0.2), inset -4px -4px 8px rgba(255, 255, 255, 0.7);
  border: 1px solid #e0e0e0;
  text-align: left;
  overflow-y: auto; /* 当内容过多时，内部出现滚动条 */
}

/* 美化滚动条样式 */
.inset-box::-webkit-scrollbar {
  width: 8px; /* 调整滚动条的宽度，使其更细 */
}

.inset-box ul {
  list-style: none;
  padding: 0;
}

.inset-box li {
  margin: 10px 0;
  padding: 10px;
  border-bottom: 1px solid #ccc;
}

.favorite-content-item,
.favorite-genre-item,
.favorite-theme-item {
  margin: 5px 0;
}


</style>
