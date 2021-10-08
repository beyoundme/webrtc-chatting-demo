<template>
  <iframe name="ddddd" id="iframes" style="width: 100%;height: 100%;" scrolling="auto" :src=url></iframe>
</template>

<script>


export default {
  name: 'vdo',
  data() {
    return {
      url:'/static/test.html?'+this.$route.query.url+"~"+this.$route.query.server
    }
  },
  mounted(){
    console.log(this.$route)
    console.log(this.$route.query.url)
  }
}
</script>
