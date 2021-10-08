<template>
  <div id="app">
    <!--<el-button type="primary">button</el-button>-->
    <router-view v-if="isRouterAlive"></router-view>
  </div>
</template>
<script>
  export default {
    name: 'App',
    provide () {
      return {
        reload: this.reload
      }
    },
    mounted() {
      document.title="webrtc视频控制系统"
    },
    data () {
      return {
        isRouterAlive: true
      }
    },
    methods: {
      reload () {
        this.isRouterAlive = false
        this.$nextTick(function () {
          this.isRouterAlive = true
        })
      }
    }
  }
</script>

<style>
  html, body, #app{
    height: 100%;
    width: 100%;
    margin: 0px;
    padding: 0px;
    font-family: "Microsoft YaHei UI";
    /*font-weight: bold;*/
    word-spacing: 2px;
    letter-spacing: 1px;
  }
  .fl {
    float: left;
  }
  .fr {
    float: right
  }
</style>
