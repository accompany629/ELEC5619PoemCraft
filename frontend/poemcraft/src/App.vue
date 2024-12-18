<template>
  <div id="app">
    <!-- 导航栏放在页面顶部 -->
    <!-- <nav>
      <router-link to="/">Home</router-link> |
      <router-link to="/register">Register</router-link> |
      <router-link to="/poem">Poem</router-link>
    </nav> -->

    <!-- 表单居中显示 -->
    <div class="form-container">
      <router-view />
    </div>
  </div>
</template>

<script>
export default {
  name: 'App',
};
</script>

<style scoped>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

/* 导航栏样式：放在页面顶部，不居中 */
nav {
  text-align: center;
  margin-top: 270px;
  margin-bottom: 40px;
}

/* 导航栏链接样式 */
nav a {
  margin: 0 10px;
  text-decoration: none;
  color: #42b983;
}

nav a.router-link-exact-active {
  font-weight: bold;
  color: #42b983;
}

/* 表单容器：表单居中 */
.form-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 100px); /* 确保表单垂直居中，减去导航栏的高度 */
}
</style>
