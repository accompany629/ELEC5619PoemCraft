<template>
  <div class="account-page">
    <!-- 标题栏 -->
    <div class="title-bar">
      <h1><i class="fas fa-user"></i> Account</h1>
    </div>

    <!-- 账户信息区域 -->
    <div class="account-info">
      <div class="avatar">
        <!-- 绑定 avatarUrl 来显示头像 -->
        <img :src="avatarUrl" alt="User Avatar" />
      </div>
      <div class="user-details">
        <p>Email: {{ userEmail }}</p>
        <!-- 您可以根据需要添加更多用户信息 -->
      </div>
    </div>

    <div class="account-edit">
      <h3>Edit Account Information</h3>
      <form @submit.prevent="updateAccount">
        <div class="form-group">
          <label for="email">Email:</label>
          <input v-model="userEmail" type="email" id="email" required />
        </div>

        <div class="form-group">
          <label for="password">Password:</label>
          <input v-model="userName" type="text" id="password" required />
        </div>

        <!-- 按钮容器 -->
        <div class="button-group">
          <button type="submit">Save Changes</button>
          <button type="button" @click="changeAvatar">Change Avatar</button>
        </div>
      </form>

      <!-- 隐藏的文件输入 -->
      <input
        type="file"
        ref="avatarInput"
        @change="onAvatarSelected"
        accept="image/*"
        style="display: none;"
      />
    </div>
  </div>
</template>

<script>
export default {
  name: 'AccountPage',
  data() {
    return {
      userName: 'John Doe',
      userEmail: 'john.doe@example.com',
      avatarUrl: 'https://via.placeholder.com/150', // 初始头像URL
    };
  },
  methods: {
    updateAccount() {
      // 在这里处理账户信息更新逻辑，例如发送请求到后端API
      alert('Account information updated!');
    },
    changeAvatar() {
      // 触发隐藏的文件输入点击事件
      this.$refs.avatarInput.click();
    },
    onAvatarSelected(event) {
      const file = event.target.files[0];
      if (file) {
        // 验证文件类型（可选）
        if (!file.type.startsWith('image/')) {
          alert('Please select an image file.');
          return;
        }

        // 验证文件大小（可选，示例：不超过2MB）
        const maxSize = 2 * 1024 * 1024; // 2MB
        if (file.size > maxSize) {
          alert('The selected file is too large. Maximum size is 2MB.');
          return;
        }

        // 创建一个本地URL用于预览
        this.avatarUrl = URL.createObjectURL(file);

        // 如果需要上传到服务器，可以在这里处理上传逻辑
        // 例如，使用 FormData 和 axios 发送 POST 请求
      }
    },
    beforeDestroy() {
      // 释放本地URL，防止内存泄漏
      if (this.avatarUrl && this.avatarUrl.startsWith('blob:')) {
        URL.revokeObjectURL(this.avatarUrl);
      }
    },
  },
};
</script>

<style scoped>
.account-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.title-bar {
  display: flex;
  align-items: center;
  margin-bottom: 30px;
}

.title-bar h1 {
  font-size: 2rem;
  color: #ff4500;
}

.title-bar i {
  margin-right: 10px;
}

.account-info {
  display: flex;
  align-items: center;
  margin-bottom: 40px;
}

.avatar img {
  border-radius: 50%;
  width: 150px;
  height: 150px;
}

.user-details {
  margin-left: 20px;
}

.user-details h2 {
  margin: 0;
  font-size: 1.5rem;
}

.account-edit {
  background-color: #f5f5f5;
  padding: 20px;
  border-radius: 5px;
}

.account-edit h3 {
  margin-top: 0;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
}

.form-group input {
  width: 400px; /* 设置固定宽度 */
  padding: 8px;
  box-sizing: border-box;
}

.button-group {
  display: flex;
  justify-content: center; /* 将按钮居中对齐 */
  gap: 20px; /* 增大按钮之间的间距 */
  margin-top: 10px;
}

.button-group button {
  background-color: #ff4500;
  color: #fff;
  border: none;
  padding: 10px 15px;
  cursor: pointer;
  border-radius: 5px;
}

.button-group button:hover {
  background-color: #ff6347;
}
</style>
