const { defineConfig } = require('@vue/cli-service');
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 3000, // 将端口改为 3000
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // 后端服务器地址
        changeOrigin: true,
        pathRewrite: {
          '^/api': '/api' // 重写路径
        }
      }
    }
  }
});
