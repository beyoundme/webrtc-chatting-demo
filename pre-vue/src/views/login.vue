<template>
  <div class="login" >
    <div id="mainitem">
      <!--<p>管理系统</p>-->
      <p id="title">webrtc视频控制系统</p>
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item  prop="name" class="name">
          <el-input  class="fl" v-model="ruleForm.name" placeholder="用户名" prefix-icon="el-icon-user" style="font-size: 22px"></el-input>
        </el-form-item>
        <el-form-item prop="pass" id="pass">
          <el-input  ref="psword" :type="prop" v-model="ruleForm.pass" prefix-icon="el-icon-lock" suffix-icon = "el-icon-view" placeholder="登录密码" style="font-size: 22px"></el-input>
          <!--<i class="el-icon-view"/>-->
        </el-form-item>
        <el-form-item label="验证码" style="padding-left: 77px;margin-top: 40px" prop="vrifycode" id="verify">
          <el-input  v-model="ruleForm.verify" style="font-size: 20px;width: 130px;float: left" ></el-input>
          <div class="identifybox">
            <div class="code" @click="refreshCode">
              <s-identify :identifyCode="identifyCode"></s-identify>
            </div>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="login()" id="loginbtn">登 录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
import * as Qs from 'qs'
import SIdentify from '../components/Identify'
export default {
  components: { SIdentify },
  name: 'login',
  data () {
    return {
      checked: false,
      identifyCode: '',
      ruleForm: {
        name: '',
        pass: '',
        verify: '',
      },
      prop: 'password',
      rules: {
        name: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        pass: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ]
      }
    }
  },
  created () {
    this.keyupSubmit()
  },
  mounted: function () {
    this.refreshCode()
    document.addEventListener('click', evt => {
      var box = document.getElementsByClassName('el-input__suffix')[0]
      if (box !== null && typeof box != 'undefined'&& box.contains(evt.target)) {
        if (this.prop === 'password') {
          this.prop = 'text'
        } else {
          this.prop = 'password'
        }
      }
    })
  },
  methods: {
    keyupSubmit () {
      document.onkeypress = ev => {
        const key = ev.keyCode
        if (key === 13) {
          this.login()
        }
      }
    },
    randomNum () {
      return Math.floor(Math.random() * 9 + 1)
    },
    // 更新验证码
    refreshCode () {
      this.identifyCode = ''
      this.makeCode()
    },
    // 随机生成验证码字符串
    makeCode () {
      for (let i = 0; i < 4; i++) {
        this.identifyCode += this.randomNum()
      }
    },
    login () {
      const { name, pass, verify } = this.ruleForm
      if (verify !== this.identifyCode) {
        this.$message('验证码输入错误！')
        return
      }
      if (name.length === 0 || pass.length === 0) {
        this.$message('请输入用户名和密码！')
        return
      }
      this.$http({
        method: 'post',
        url: 'https://test.lx.com:4101/api/user/get_login',
        transformRequest: [function (data) {
          // 对 data 进行任意转换处理
          return Qs.stringify(data)
        }],
        data: { user: name, pass: pass }
      }).then(res => {
        if (res.data.code === 200) {
          sessionStorage.setItem('username', name)
          this.$router.push('/play')
        } else {
          this.$message(res.data.msg)
        }
      })
    }
  }
}
</script>

<style scoped>
  .login {
    background-image: url('../assets/images/loginBackground.png');
    background-size:cover;
    background-repeat:no-repeat;
    background-position:center;
    height:100%;
    width:100%
  }
  #mainitem {
    border-radius: 15px;
    top: 18%;
    left: 69%;
    position: absolute;
    min-height: 55%;
    min-width: 27%;
    background-color: white;
  }
  #title {
    padding-top: 45px;
    padding-bottom: 35px;
    text-align: center;
    font-size: 30px;
    color: #00459F;
  }
  .name {
    border: none;
    margin-left: -10px;
  }
  .name /deep/ .el-input__inner{
    border: none;
    border-bottom: 1px solid #DCDFE6;
    width: 350px;
    padding-left: 40px;
  }
  #pass {
    border: none;
    margin-left: -10px;
  }
  #pass /deep/ .el-input__inner{
    border: none;
    border-bottom: 1px solid #DCDFE6;
    width: 350px;
    padding-left: 40px;
  }
  .identifybox{
    display: flex;
    justify-content: space-between;
    padding-left:14px;
  }
  #verify /deep/ .el-form-item__label{
    font-size: 22px;
    color: gray;
  }
  #loginbtn {
    background-color: #32C7FF;
    border: none;
    width: 300px;
    height: 50px;
    border-radius: 12px;
    font-size: 21px;
  }
  .el-input {
    width: auto;
  }
</style>
