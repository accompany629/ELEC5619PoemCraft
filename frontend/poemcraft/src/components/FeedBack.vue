<script setup>
import { ref } from 'vue';
import axios from 'axios'; // 引入 axios

const ratings = ref({
  ue: 0, // User Experience
  fn: 0, // Functionality
  cq: 0  // Content Quality
});

const hoverRatings = ref({
  ue: 0,
  fn: 0,
  cq: 0
});

const resetRatings = () => {
  ratings.value = { ue: 0, fn: 0, cq: 0 };
  hoverRatings.value = { ue: 0, fn: 0, cq: 0 };
};

const hoverRating = (category, value) => {
  hoverRatings.value[category] = value;
};

const resetHover = (category) => {
  hoverRatings.value[category] = 0;
};

const setRating = (category, value) => {
  ratings.value[category] = value;
};

const getClass = (category, value) => {
  if (hoverRatings.value[category] >= value || ratings.value[category] >= value) {
    return 'fas fa-heart';
  }
  return 'far fa-heart';
};

// 提交反馈并发送到后端
const submitRatings = () => {
  const feedbackData = {
    userExperience: ratings.value.ue,
    functionality: ratings.value.fn,
    contentQuality: ratings.value.cq,
    feedback: "This is an optional feedback text."  // 可选反馈字段
  };

  const token = localStorage.getItem('token');
  if (!token) {
    alert("Please login to continue!"); // 如果没有 token，则提示用户登录
    return;
  }

  // 使用 axios 发送 POST 请求到后端
  axios.post('/api/feedback/submit', feedbackData, {
    headers: {
      'Authorization': `Bearer ${token}`  // 注意传递 token
    }
  })
      .then(response => {
        console.log('反馈提交成功:', response.data);
        // 可在此处显示提交成功的消息或重置表单
        resetRatings();  // 可选：提交成功后重置评分
      })
      .catch(error => {
        console.error('提交反馈时出错:', error);
      });
};
</script>

<template>
  <div class="rating-component">
    <div class="title-bar">
      <h1><i class="fas fa-user"></i> Feedback</h1>
    </div>

    <h2 class="rating-title">Rate PoemCraft!</h2>

    <div class="rating-category">
      <p>User Experience</p>
      <div class="hearts">
        <span v-for="n in 5" :key="'ue' + n" @mouseover="hoverRating('ue', n)" @mouseleave="resetHover('ue')" @click="setRating('ue', n)">
          <i :class="getClass('ue', n)"></i>
        </span>
      </div>
    </div>

    <div class="rating-category">
      <p>Functionality</p>
      <div class="hearts">
        <span v-for="n in 5" :key="'fn' + n" @mouseover="hoverRating('fn', n)" @mouseleave="resetHover('fn')" @click="setRating('fn', n)">
          <i :class="getClass('fn', n)"></i>
        </span>
      </div>
    </div>

    <div class="rating-category">
      <p>Content Quality</p>
      <div class="hearts">
        <span v-for="n in 5" :key="'cq' + n" @mouseover="hoverRating('cq', n)" @mouseleave="resetHover('cq')" @click="setRating('cq', n)">
          <i :class="getClass('cq', n)"></i>
        </span>
      </div>
    </div>

    <div class="button-group">
      <button @click="submitRatings">Submit</button>
      <button @click="resetRatings" class="reset-button">
        <i class="fas fa-redo"></i>
      </button>
    </div>
  </div>
</template>

<style scoped>
.rating-component {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.title-bar {
  justify-content: center; /* 水平居中 */
  display: flex;
  align-items: center;
  margin-bottom: 30px;
}


.rating-title{
  justify-content: center; /* 水平居中 */
}

.title-bar h1 {
  font-size: 2rem;
  color: #ff4500;
}

.title-bar i {
  margin-right: 10px;
}

.rating-category {
  width: 400px;
  background-color: #f5f5f5;
  padding: 5px;
  border-radius: 5px;
}

.hearts span {
  font-size: 24px;
  cursor: pointer;
  margin: 0 5px;
}

.far.fa-heart {
  color: #cccccc;
}

.fas.fa-heart {
  color: #ff0000;
}

.button-group {
  display: flex;
  justify-content: center;
  align-items: center;
}

button {
  margin-top: 20px;
  padding: 10px 15px;
  font-size: 16px;
  background-color: #ff4500;
  color: #fff;
  border: none;
  cursor: pointer;
  border-radius: 5px;
}

.reset-button {
  margin-left: 10px;
  padding: 10px;
  background-color: #ddd;
  border: none;
  border-radius: 50%;
  cursor: pointer;
}

.reset-button i {
  font-size: 16px;
  color: #333;
}

.reset-button:hover {
  background-color: #ccc;
}
</style>
