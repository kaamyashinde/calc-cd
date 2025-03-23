module.exports = {
    devServer: {
      port: 8081,  // Endre porten til 8081
      proxy: {
        '/api': {
          target: 'http://127.0.0.1:8080',  // Backend server som kjører på port 8080
          changeOrigin: true,
          secure: false
        }
      }
    }
  };

