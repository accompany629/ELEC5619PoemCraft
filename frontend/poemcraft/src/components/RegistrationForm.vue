<template>
    <div class="register-form-container">
    <!-- Circular Image above the form -->
    <div class="image-container">
      <img src="/poemcraft.jpg" alt="Profile Image" class="profile-image" />
    </div>
    <div class="register-form">
      <form>
        <div>
          <label>Email</label>
          <input type="email" v-model="email" required />
        </div>

        <div>
          <label>Password</label>
          <input type="password" v-model="password" required />
        </div>

        <div class="verification-field">
          <label>Verification Code</label>
          <div class="input-group">
            <input type="text" v-model="verificationCode" required />
            <button class="send-code-btn" @click.prevent="sendCode">Send Code</button>
          </div>
        </div>

        <button type="submit" @click = "register">Register</button>
        <button type="submit" @click = "login">Login</button>

      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'RegisterForm', // 定义组件名字
  data() {
    return {
      email: '',
      password: '',
      verificationCode: '',
    };
  },
  methods: {
    // 发送验证码
    async sendCode() {
      try {
        const response = await axios.post('/api/users/send-code', {
          email: this.email,
        });

        if (response.data.success) {
          alert(response.data.message);  // 成功时，显示后端返回的消息
        } else {
          alert('Error: ' + response.data.message);  // 后端返回的错误消息
        }
      } catch (error) {
        console.error('Error:', error);
        alert('An error occurred while sending the verification code.');
      }
    },


    // 注册用户
    async register() {
      try {
        const response = await axios.post('/api/users/register', {
          username: 'usertest',
          email: this.email,
          password: this.password,
          verificationCode: this.verificationCode,
          role: 'USER',
          avatar: 'avatar.jpg',
          preference: 'preferences',
        });

        if (response.data.success) {
          alert(response.data.message);  // 注册成功
          // 存储 JWT token
          // localStorage.setItem('token', response.data.token);
          // this.$router.push('/poem'); // 跳转到主页
          window.location.reload(); 
          
        } else {
          alert('Registration failed: ' + response.data.message);  // 注册失败，显示后端返回的错误消息
        }
      } catch (error) {
        console.error('Error:', error);
        if (error.response && error.response.data && error.response.data.message) {
          alert('Registration failed: ' + error.response.data.message);
        } else {
          alert('An error occurred during registration.');
        }
      }
    },

    // 登录用户
    async login() {
      try {
        const response = await axios.post('http://localhost:8080/api/users/login', {
          email: this.email,
          password: this.password,
        });
        alert('Login successful!');
        // 存储 JWT token
        localStorage.setItem('token', response.data.token);
        this.$router.push('/poem'); // 跳转到主页
      } catch (error) {
        if (error.response && error.response.data) {
          alert('Login failed: ' + (error.response.data.message || 'Unknown error'));
        } else {
          alert('An error occurred during login.');
        }
        console.error('Error:', error);
      }
    },
  },
};
</script>


<style scoped>
.register-form-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border: 1px solid #ccc;
  border-radius: 12px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  padding: 20px 20px 40px; /* 调整上下内边距以使外框更加紧凑 */
  background-color: #fff;
  max-width: 400px;
  margin: 0 auto;
  position: relative;
}

.image-container {
  position: absolute;
  top: -50px;
  width: 100px;
  height: 100px;
  border-radius: 50%;
  overflow: hidden;
  border: 4px solid #fff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.profile-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.register-form {
  display: flex;
  flex-direction: column;
  width: 100%;
  padding-top: 60px;
}

label {
  display: block;
  margin-bottom: 8px;
  font-size: 14px;
}
input {
  display: block;
  width: 95%;
  padding: 10px;
  margin-bottom: 16px;
  border-radius: 8px;
  border: 1px solid #ccc;
}

.input-group {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.input-group input {
  flex: 1;
  min-width: 200px;
  padding: 10px;
  margin-right: 10px;
  margin-bottom: 16px;
  border-radius: 8px;
  border: 1px solid #ccc;
  box-sizing: border-box;
}
.send-code-btn {
  width: 200px;
  padding: 10px 16px;
  background-color: #000;
  color: #fff;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  display: flex;
  margin-bottom: 26px;
  align-items: center;
  justify-content: center;
}

button {
  width: 100%;
  padding: 10px;
  background-color: #000;
  color: #fff;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  margin-top: 10px;
}

button:active {
  background-color: #555;
}/* 点击时按钮颜色变为浅灰色 */

</style>
