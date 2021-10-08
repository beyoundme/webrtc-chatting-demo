<template>
    <el-menu-item :index="item.url"  v-if="!item.child">{{item.name}}</el-menu-item>
    <el-submenu :index="item.url" v-else>
      <template slot="title">{{item.name}}</template>
      <NavItem v-for="v in item.child" :key="v.url" :item="v"/>
    </el-submenu>
</template>

<script>
export default {
  name: 'NavItem',
  data () {
    return {
      items: []
    }
  },
  props: ['item']
}
</script>
