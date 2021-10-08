const webpack = require('webpack');
module.exports = {
  devServer: {
    port: 9443,
    host: '0.0.0.0',
    open: true,
    https: true,
    proxy: {
      '/api/getIceServers': {
        target: 'http://' + require('os').hostname() +':8000/api/getIceServers',
        changeOrigin: true,
        // ws: true,
        pathRewrite: {
          '^/api/getIceServers': ''
        }
      },
      '/api/call': {
        target: 'http://' + require('os').hostname() +':8000/api/call',
        changeOrigin: true,
        // ws: true,
        pathRewrite: {
          '^/api/call': ''
        }
      },
      '/api/addIceCandidate': {
        target: 'http://' + require('os').hostname() + ':8000/api/addIceCandidate',
        changeOrigin: true,
        // ws: true,
        pathRewrite: {
          '^/api/addIceCandidate': ''
        }
      },
      '/api/getIceCandidate': {
        target: 'http://' + require('os').hostname() +':8000/api/getIceCandidate',
        changeOrigin: true,
        // ws: true,
        pathRewrite: {
          '^/api/getIceCandidate': ''
        }
      }
    }
  },
  lintOnSave: false,
  configureWebpack: {
    plugins: [
      new webpack.ProvidePlugin({
        $: 'jquery',
        jQuery: 'jquery',
        'windows.jQuery': 'jquery'
      })
    ]
  },
}
