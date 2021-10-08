<template>
    <el-header style="height: 0px;">
      <div class="fl header_one">
        <div class="fr" style="width: 130px;text-overflow: ellipsis;overflow:hidden;white-space: nowrap;text-align: left"><span style="margin-right: 15px;margin-left: 15px;">{{this.passForm.userAccount}}</span></div>
        <i class="el-icon-s-custom fr" style="font-size: 30px;padding-top: 14px"></i>
      </div>
      <div class="fl header_two">
        <el-menu
          :default-active="$route.path"
          exact
          class="el-menu-demo"
          mode="horizontal"
          @select="handleSelect"
          background-color="dodgerblue"
          text-color="#fff"
          router
          active-text-color="#ffd04b">
          <el-menu-item index="/play"  >视频控制</el-menu-item>
        </el-menu>
      </div>



      <div class="fr">
        <img src="../assets/images/exit.png" @click="exitForm = true" style="margin-top: 10px;margin-right: 53px;padding-top: 10px">
        <el-dialog title="提示" :visible.sync="exitForm" id="quitUser" style="margin-top: 15vh">
          <div data-v-65af85a3="" style=" text-align: center;padding-top: 60px;font-size: 14px;"><span data-v-65af85a3="">确认注销用户？</span></div>
          <div slot="footer" class="dialog-footer">
            <el-button class="fr opera" type="primary"  size="small" @click="exitForm = false" style="margin-right: 75px;">
              <img src="../assets/images/cansl.png" style="top: 1px;position: relative;">
              取消
            </el-button>
            <el-button class="fr opera" type="primary"  size="small" @click="quitUser()" style="margin-right: 33px">
              <img src="../assets/images/confirm.png" style="top: 1px;position: relative;">
              确定
            </el-button>
          </div>
        </el-dialog>
      </div>
    </el-header>
</template>

<script>
import * as Qs from 'qs'

export default {
  name: 'Nav',
  data () {
    return {
      dialogFormVisible: false,
      centerDialogVisible: false,
      exitForm: false,
      passForm: {
        userAccount: sessionStorage.getItem('username'),
        originPass: '',
        newPass: '',
        checkNewPass: ''
      },
      activeIndex: '/storemaneger/setting',
      routerData: [
      ],
      formLabelWidth: '120px'
    }
  },
  mounted () {
  },
  methods: {
    quitUser () {
      sessionStorage.removeItem('username')
      this.$router.push('/login')
    },
    handleSelect (key, keyPath) {
    }
  }
}
</script>

<style  scoped>

  .el-header {
    background-color: deepskyblue;
    color: #333;
    text-align: center;
    line-height: 60px;
    min-height: 60px;
    top: 0px;
    width: 100%;
    position: absolute;
  }
  .header_one {
    width: 230px;
    margin-left: 0px;
    color: white;
    font-size: 20px;
    min-height: 6%;
  }
  .header_two {
    color: white;
    min-height: 6%;
  }
  .el-menu-demo {
    min-height: 6%;
    position: relative;
    line-height: 0px;
  }
  #quitUser /deep/ .el-dialog{
    line-height: 0px;
  }
  #quitUser /deep/ .el-dialog__body {
    height: 101px;
    padding: 5px 0px;
  }
  .dataInfo /deep/ .el-dialog{
    line-height: 0px;
  }
  /deep/ .el-submenu__title {
    font-size: 16px;
  }


</style>

<style>
  .el-menu.el-menu--popup.el-menu--popup-bottom-start {
   text-align: left;
  }
  /* 提示框规范*/
  #about  .el-form-item {
    margin-bottom: 1px;
  }
  #about  .el-form-item__content {
    width: 160px;
  }
  #about  .el-dialog{
    margin-top: 30vh;
    width: 490px;
    height: 297px;
    /*line-height: 0px;*/
    text-align: left;
    background-image: url('../assets/images/about.jpg');
  }
  #about  .el-form-item__label {
    line-height: 34px;
  }
  #about  .el-dialog__body {
    padding: 0px;
    overflow: auto;
    border: 13px black;
    color: white;
  }
  #about  .el-dialog__header {
    background-color: #0f1d5f;
    height: 12px;
  }
  #about  .el-dialog__title {
    font-size: 15px;
    color: white;
  }
  #about  .el-dialog__headerbtn {
    top: 15px;
  }

  @import "../assets/css/common.css";
</style>
