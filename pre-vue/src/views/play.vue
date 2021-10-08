<template>
  <el-container>
    <Nav />
    <el-main class="elmain">
      <div class="fl main-left custom-tree-container">
        <div class="search">
          <el-form ref="searchForm" :model="searchForm" label-width="80px">
            <el-form-item label="状态" >
              <el-checkbox-group v-model="isOnline">
                <el-checkbox label="1" style="color: white">在线</el-checkbox>
                <el-checkbox label="0" style="color: white">离线</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
            <el-input  size="mini" style="margin-left: 20px;" placeholder="请输入资源" v-model="filterText"></el-input>
          </el-form>
          <div class="fl tabpage pagecheck" id="resoucesView" @click="showTree()">资源总览</div>
        </div>
        <div ref="treeSearchOrign"  class="treeSearchOrign block"  style="overflow: auto;max-height: 630px;">
          <el-tree
            class="filter-tree"
            :data="orgsOptions"
            :props="defaultProps"
            highlight-current
            :filter-node-method="filterNode"
            ref="treeSearch">
             <span class="custom-tree-node" slot-scope="{ node }">
               <span>
                 <img v-if="node.data.type=='org'" src="../assets/images/orglogo.png" style="height: 15px;padding-right: 5px;">
                 <img v-if="node.data.type=='device'" src="../assets/images/encoder_online.png" style="height: 26px;padding-right: 5px;">
                 <i v-if="node.data.type=='user'"  class="el-icon-s-custom" style="font-size: 26px;padding-right: 5px" :style="userStyle(node.data.label,node.data.status)"></i>
                 <img title="视频呼叫" v-if="node.data.type=='user'&&node.data.label!==userAccount" src="../assets/images/web/business_start.png" style="float: right;height: 22px; padding-right: 10px; padding-top: 5px;"  @click="play(node.data.label,-1,'call',node.data.status,node.data.type)">
                 <img title="点播" v-if="(node.data.type=='user'||node.data.type=='device')&&node.data.label!==userAccount" src="../assets/images/web/play.png" style="float: right;height: 22px; padding-right: 10px; padding-left: 130px;padding-top: 5px;"  @click="play(node.data.label,-1,'play',node.data.status,node.data.type)">
                 <!--<img title="语音呼叫" v-if="node.data.type=='user'&&node.data.label!==userAccount" src="../assets/images/web/call.png" style="float: right;height: 22px; padding-right: 10px; padding-top: 5px;">-->

                 <!--<img v-if="type=='user'" src="../assets/images/orglogo.png" style="height: 15px;padding-right: 5px;">-->
              </span>
              <span :title="node.data.label" style=" display :inline-block ;width: 103px;overflow: hidden;textOverflow: ellipsis;whiteSpace: nowrap;font-size: 15px;text-align:left" >{{ node.label }}</span>
            </span>
          </el-tree>
        </div>
        </div>
      <div class="fl main-center" ref = "main" >
        <div class="fl item" v-for="index in 16" :class="{active:index==current}" :style="computeStyle(nums,index)" @click="clickScreen($event,index)">
          <video :id="videoCamera(index)"  :style=computeVideoStyle(nums,index) autoplay></video>
          <img :id="stopButton(index)" title="停止业务" src="../assets/images/web/bt-allstop.png"  style="z-index:100" :style="stopButtonLeft()" @click="stopNavigator(index)" >
          <img :id="muteButton(index)" title="关闭声音" src="../assets/images/sound.png"  style="z-index:100" :style="muteButtonLeft()" @click="muteSound(index)" >
          <img :id="muteOffButton(index)" title="打开声音" src="../assets/images/sound_mute.png"  style="z-index:100" :style="muteButtonLeft()" @click="muteSound(index)" >
          <span :id="name(index)" style="position: absolute;right: 0px;top: 0px;color: black"></span>
          <span :id="subtitles(index)" :style="subtitlesStyle(index)"></span>
        </div>
      </div>
      <div class="fr main-right">
        <div class="item"  style="width:100%;height: 35%" >

          <video id="videoCamera0" width="318px" height="318px"  muted></video>
          <span id="subtitles0" ></span>
        </div>
      </div>
    </el-main>

    <el-footer  style="height: 0px;letter-spacing: 8px;">
      <span style="padding-left: 26px;">webrtc视频控制系统</span>
      <span class="fr" style="padding-right: 45px;font-size: 20px">{{ this.week }} </span>
      <span class="fr" style="padding-right: 20px;font-size: 20px">{{ this.date }} </span>
      <span class="fr" style="padding-right: 20px; ">{{ this.time }} </span>
      <div class="fr" style="margin-right: 146px;border: 1px solid #eee;width: 510px;height: 100%">
        <div class="screen fl"  onmouseover="this.style='background-color: #6a7078;';" onmouseout="this.style='background-color: deepskyblue;';" @click="fullScreen()">
          <img src="../assets/images/web/bt-fullscreen.png" style="padding-left: 9px;">
        </div>
        <div class="screen fl"  onmouseover="this.style='background-color: #6a7078;';" onmouseout="this.style='background-color: deepskyblue;';" @click="stopAll()">
          <img src="../assets/images/web/bt-allstop.png" style="padding-left: 9px;" >
        </div>
        <div class="screen fl"  onmouseover="this.style='background-color: #6a7078;';" onmouseout="this.style='background-color: deepskyblue;';" @click="showScreen(1)">
          <img src="../assets/images/web/bt-onescreen.png" style="padding-left: 9px;">
        </div>
        <div class="screen fl"  onmouseover="this.style='background-color: #6a7078;';" onmouseout="this.style='background-color: deepskyblue;';" @click="showScreen(2)">
          <img src="../assets/images/web/bt-fourscreen.png" style="padding-left: 9px;">
        </div>
        <div class="screen fl"  onmouseover="this.style='background-color: #6a7078;';" onmouseout="this.style='background-color: deepskyblue;';" @click="showScreen(3)">
          <img src="../assets/images/web/bt-ninescreen.png" style="padding-left: 9px;">
        </div>
        <div class="screen fl"  onmouseover="this.style='background-color: #6a7078;';" onmouseout="this.style='background-color: deepskyblue;';" @click="showScreen(4)">
          <img src="../assets/images/web/bt-syscreen.png" style="padding-left: 9px;">
        </div>
      </div>
    </el-footer>
  </el-container>
</template>

<script>
import Nav from '../components/Nav'
import * as Qs from 'qs'

export default {
  components: { Nav },

  name: 'play',
  data () {
    return {
      dialogVisible: false,
      dialogGroupVisible:true,
      current: 0,
      nums: 4,
      filterText: '',
      isOnline:["0","1"],
      userAccount: sessionStorage.getItem('username'),
      serverIp: sessionStorage.getItem('serverIp'),
      serverPort: sessionStorage.getItem('serverPort'),
      searchFilterText: '',
      week: '',
      date: '',
      searchForm: {
        searchType:'1',
        content:''
      },
      time: '',
      socket: '',
      screens: [],
      target:'',
      peers:{},
      fullIndex: 1,
      toIp:'',
      defaultProps: {
        children: 'children',
        label: 'label',
        type: 'type',
        status: 'status',
        uuid:''
      },
      groupForm:{
        groupName:'',
        groupMembers:[],
        groupDevices:[],
      },
      dataType:'resources',
      orgsOptions: [
      ],
      orgsBack:[],
      orgsDefault:[],
      isRouterAlive: true,
      videoWidth: '',
      videoHeight: '',
      uuid:'',
      tableData: [{
        name: '',
      },{
        name: '',
      },{
        name: '',
      },{
        name: '',
      },{
        name: '',
      },{
        name: '',
      },{
        name: '',
      },{
        name: '',
      },{
        name: '',
      },{
        name: '',
      },{
        name: '',
      },{
        name: '',
      },{
        name: '',
      },{
        name: '',
      },{
        name: '',
      },{
        name: '',
      },
      ]
    }
  },
  mounted () {
    this.dialogGroupVisible = false
    this.Timer()
    this.getData()
    this.initSocket()
    this.getLocalStream()
    this.getScreenInfo()
    this.subtitlesStyle(0)

  },
  provide () {
    return {
      reload: this.reload
    }
  },
  watch: {
    filterText(val) {
      this.$refs.treeSearch.filter(val);
      this.$refs.treeGroupSearch.filter(val);
    },
    isOnline(val) {
      var status = val.toString()
      if(status === ''){
        return
      }
      this.filterText = ''
      console.log(status)
      this.orgsOptions = JSON.parse(JSON.stringify(this.orgsBack))
      console.log(this.orgsBack)
      this.orgsOptions.forEach((bean) =>{
        this.searchBean(bean,status)
      })
    }
  },
  methods: {
    reload () {
      this.isRouterAlive = false
      this.$nextTick(function () {
        this.isRouterAlive = true
      })
    },
    showTree(){
      this.isOnline = ["0","1"]
      this.$refs.treeSearchOrign.style.display = 'block'
      var resoucesView = document.getElementById("resoucesView")
      if(!resoucesView.classList.contains("pagecheck")){
        resoucesView.classList.add("pagecheck")
      }
      this.dataType = 'resources'
      this.getData()

    },
    searchBean(bean,status){
      if(status.indexOf(bean.status)<0 && bean.type !== 'org'){
        return true
      }
      if(bean.children!=null){
        var temp = []
        bean.children.forEach((item)=>{
          var flag = this.searchBean(item,status)
          if(!flag){
            temp.push(item)
          }
        })
        bean.children = temp
      }
    },
    subtitlesStyle(index){
      var thisName = document.getElementById('name'+index)
      var thisSubtitles = document.getElementById('subtitles'+index)
      if(thisSubtitles!==null&&thisSubtitles.innerText !== ''){
        return
      }
      var name = '';
      if(index === 0){
        name = this.userAccount
      }else if(thisName!==null){
        name = thisName.innerText
      }
      if(name === ''){
        return
      }
    },
    videoCamera(index) {
      return 'videoCamera'+index;
    },
    stopButton(index){
      return 'stopButton'+index;
    },
    muteButton(index){
      return 'muteButton'+index;
    },
    muteOffButton(index){
      return 'muteOffButton'+index;
    },
    name(index) {
      return 'name'+index;
    },
    subtitles(index){
      return 'subtitles'+index;
    },
    userStyle(user,status) {
      if(user == this.userAccount){
        return {
          color: '#ed4321'
        }
      }else if (status === '1'){
        return {
          color: '#09e3eb'
        }
      }else {
        return {
          color: 'grey'
        }
      }

    },
    getData () {
      this.$http({
        method: 'get',
        url: 'https://test.lx.com:4101/api/user/getResources?dataType='+this.dataType + '&userName='+this.userAccount
      }).then(res => {
        if (res.data.code === 200) {
          this.orgsOptions = res.data.data.resources
          this.orgsBack = res.data.data.resources
          if(this.dataType === 'resources'){
            this.orgsDefault = res.data.data.resources
          }
        } else {
          this.$message({
            type: 'error',
            message: "获取数据失败"
          })
        }
      })
    },

    flag(node) {
      console.log(node)
    },
    Timer () {
      var timerID = setInterval(this.updateTime, 1000)
      this.updateTime()
      return timerID
    },
    updateTime () {
      var cd = new Date()
      this.week = '星期' + '日一二三四五六'.charAt(cd.getDay())
      this.time = this.zeroPadding(cd.getHours(), 2) + ':' + this.zeroPadding(cd.getMinutes(), 2) + ':' + this.zeroPadding(cd.getSeconds(), 2)
      this.date = this.zeroPadding(cd.getFullYear(), 4) + '/' + this.zeroPadding(cd.getMonth() + 1, 2) + '/' + this.zeroPadding(cd.getDate(), 2)
    },
    zeroPadding (num, digit) {
      var zero = ''
      for (var i = 0; i < digit; i++) {
        zero += '0'
      }
      return (zero + num).slice(-digit)
    },
    clickScreen(evt,index) {
      this.current = index;
    },
    showSearchTree () {
      if (typeof this.$refs.treeSearchOrign !== 'undefined') {
        this.$refs.treeSearchOrign.style.display = 'block'
      }
    },
    filterNode (value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },
    showScreen(num) {
      this.$http({
        method: 'get',
        url: 'https://test.lx.com:4101/api/user/changescreen?user='+this.userAccount+'&screen='+num
      }).then(res => {
        if (res.data.code === 200) {
          this.fullIndex = 1
          this.getScreenInfo();
        } else {
          this.$message({
            type: 'error',
            message: "切换屏幕失败"
          })
        }
      })
    },
    getScreenInfo() {
      this.$http({
        method: 'get',
        url: 'https://test.lx.com:4101/api/user/getscreen?user='+this.userAccount
      }).then(res => {
        if (res.data.code === 200) {
          this.nums = res.data.data.nums
        } else {
          this.$message({
            type: 'error',
            message: "获取屏幕信息失败"
          })
        }
      })
    },
    handleCheckChange (a) {
      // console.log(a)
      this.searchFilterText = a.label
      // this.searchForm.input_user_org = a.curenetId
    },
    computeStyle(nums,index) {
      var num = Math.sqrt(nums)
      //0.09 0.12 0.18 0.18
      var split = 0.18
      if(num === 4){
        split = 0.09
      }else  if (num === 3){
        split = 0.12
      }
      this.videoWidth  = 100/num - split*num
      this.videoHeight  = 100/num- split*num
      if(nums === 1 && index === this.fullIndex){
        return {
          width: this.videoWidth+'% ',
          height: this.videoHeight+'%',
          position: 'relative',
          display: 'block'
        }
      }else if(nums !== 1&&index<=nums){
        return {
          width: this.videoWidth+'%',
          height: this.videoHeight+'%',
          position: 'relative',
          display: 'block'
        }
      }else {
        return {
          display: 'none'
        }
      }

    },
    computeVideoStyle(nums,index) {
      if(nums === 1 && index === this.fullIndex){
        return {
          width: '100%',
          height: '100%',
          position: 'absolute',
          left: '0px',
          top: '0px',
          display: 'block'
        }
      }else if(nums !== 1 &&index<=nums){
        return {
          width: '100%',
          height: '100%',
          position: 'absolute',
          left: '0px',
          top: '0px',
          display: 'block'
        }
      }else {
        return {
          display: 'none'
        }
      }

    },
    stopButtonLeft() {
      return {
        left: 45 +'%',
        position: 'absolute',
        bottom: '0px',
        display: 'none',
        width: '21px'
      }
    },
    muteButtonLeft() {
      return {
        left: 55 +'%',
        position: 'absolute',
        bottom: '0px',
        display: 'none'
      }
    },
    play(user,index,oper,status,type){
      if(status == '0'){
        this.$message({
          type: 'error',
          message: '目标不在线'
        })
        return
      }
      for(var i = 1;i<17;i++){
        var name = document.getElementById('name'+i)
        if(user === name.innerText){
          this.$message({
            type: 'error',
            message: '请勿重复操作'
          })
          return
        }
      }
      if(type === 'user'){
        this.$http({
          method: 'get',
          url: 'https://test.lx.com:4101/api/user/play?user='+this.userAccount+'&index='+index+'&target='+user+'&oper='+oper
        }).then(res => {
          if (res.data.code === 200) {
            index = res.data.data.index
            this.call(user,index)
            if(index>this.nums){
              var num = Math.sqrt(this.nums)
              this.showScreen(num+1)
            }
            var note = '点播'
            if(oper ===  'call'){
              note = '呼叫'
            }
            this.$message({
              type: 'success',
              message: note+'成功'
            })
          } else {
            this.$message({
              type: 'error',
              message: "操作失败"
            })
          }
        })
      }else if(type === 'device'){
        this.$http({
          method: 'get',
          url: 'https://test.lx.com:4101/api/user/getUrl?user='+user
        }).then(res => {
          if (res.data.code === 200) {
            const newWindow = this.$router.resolve({path: '/vdo', query: {url: res.data.data,server:'https://' + require('os').hostname() + ':9443'}})
            window.open(newWindow.href,'_blank')
          } else {
            this.$message({
              type: 'error',
              message: "操作失败"
            })
          }
        })
      }


    },
    getLocalStream() {
      if (navigator.mediaDevices === undefined) {
        navigator.mediaDevices = {}
      }
      if (navigator.mediaDevices.getUserMedia === undefined) {
        navigator.mediaDevices.getUserMedia = function (constraints) {
          var getUserMedia = navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.getUserMedia
          if (!getUserMedia) {
            return Promise.reject(new Error('getUserMedia is not implemented in this browser'))
          }
          return new Promise(function (resolve, reject) {
            getUserMedia.call(navigator, constraints, resolve, reject)
          })
        }
      }
      var thisVideo = document.getElementById('videoCamera0')
      var constraints = { audio: true, video: { width: 318, height: 318, transform: 'scaleX(-1)' } }
      navigator.mediaDevices.getUserMedia(constraints).then(function (stream) {
        if ('srcObject' in thisVideo) {
          thisVideo.srcObject = stream
        } else {
          thisVideo.src = window.URL.createObjectURL(stream)
        }
        thisVideo.onloadedmetadata = function (e) {
          thisVideo.play()
        }
      }).catch(err => {
        console.log(err)
      })
    },
    addStreamAoto(stream){
      this.$http({
        method: 'get',
        url: 'https://test.lx.com:4101/api/user/getRelated?user='+this.userAccount
      }).then(res => {
        if (res.data.code === 200) {
          var num = res.data.data
          var index = 0;
          var flag = true
          var em = [];
          console.log(this.nums)
          for(var i = 1;i<=this.nums;i++){
            var thisVideo = document.getElementById('videoCamera'+i)
            if(thisVideo.srcObject === null){
              em.push(i)
            }else {
              index = index +1;
            }
          }
          if(index === num){
            flag = false
          }
          if(flag){
            if(num>this.nums){
              this.getScreenInfo()
            }else {
            }
            for(var i = 1;i<=this.nums;i++){
              var thisVideo = document.getElementById('videoCamera'+i)
              thisVideo.onloadeddata = () => {
                thisVideo.play();
              }
              if(thisVideo.srcObject === null){
                thisVideo.srcObject = stream;
                console.log(i)
                break;
              }
            }
          }else {
            em.forEach((v)=>{
              this.display(v,'',false)
            })

          }
        } else {
          this.$message({
            type: 'error',
            message: "操作失败"
          })
        }
      })
    },
    initSocket() {
      this.socket = new WebSocket('wss://test.lx.com:4101/ws?'+this.userAccount);
      // this.socket = new WebSocket('ws://' + require('os').hostname()+':9443/wssss?'+this.userAccount);
      this.socket.onerror = () => console.log('信令通道创建失败！');
      this.socket.onmessage = e => {
        if(e.data.startsWith("stop")){
          var oper = e.data.substring(4,8)
          var sipId = e.data.substring(9)
          console.log(sipId)
          console.log(oper)
          if(oper === 'play'){
            if(this.peers[sipId + "answer"]&&this.peers[sipId+"answer"]!=null){

              this.peers[sipId+"answer"].close()
              this.peers[sipId+"answer"] = null
            }
            return
          }

          var tempIndex = [];
          for(var index=1;index <=this.nums;index++ ){
            var name = document.getElementById('name'+index)
            if(sipId === name.innerText){
              tempIndex.push(index)
            }
          }
          if(tempIndex.length > 1){
            if(this.peers[sipId + "answer"]&&this.peers[sipId+"answer"]!=null){
              this.peers[sipId+"answer"].close()
              this.peers[sipId+"answer"] = null
            }
            this.display(tempIndex[1],'',false)
          }else if(tempIndex.length === 1){
            if(this.peers[sipId + "offer"]&&this.peers[sipId+"offer"]!=null){
              this.peers[sipId+"offer"].close()
              this.peers[sipId+"offer"] = null
            }
            this.display(tempIndex[0],'',false)
          }else {
          }
          return
        }
        if(e.data.startsWith("offline")){
          var sipId = e.data.substring(8)
          console.log(sipId)
          if(this.peers[sipId+"offer"]&&this.peers[sipId+"offer"]!=null){
            this.peers[sipId+"offer"].close()
            this.peers[sipId+"offer"] = null
          }
          this.getData()
          for(var index=1;index <=this.nums;index++ ){
            var name = document.getElementById('name'+index)
            if(sipId === name.innerText){
              this.display(index,'',false)
            }
          }
          return
        }
        if(e.data.startsWith("online")){
         this.getData()
          return
        }
        const { type, sdp, iceCandidate,fromIp } = JSON.parse(e.data)
        this.toIp = fromIp
        if (type === 'answer') {
          this.peers[fromIp + "offer"].setRemoteDescription(new RTCSessionDescription({ type, sdp }));
        } else if (type === 'answer_ice') {
          this.peers[fromIp + "offer"].addIceCandidate(iceCandidate);
        } else if (type === 'offer') {
          this.startLive(new RTCSessionDescription({ type, sdp }));
          var index = -1;
          for(var i = 1;i<=this.nums;i++){
            var thisVideo = document.getElementById('videoCamera'+i)
            if(thisVideo.srcObject === null){
              index = i;
              break
            }
          }
          if(index === -1){
            index = this.nums+1
            this.getScreenInfo()
          }
          this.display(index,fromIp,true)
        } else if (type === 'offer_ice') {
          this.peers[fromIp + "answer"].addIceCandidate(iceCandidate);
        }
      };
    },

    call(user,index) {
      this.target = 'offer'
      var thisVideo = document.getElementById('videoCamera'+index)
      thisVideo.muted = false
      this.display(index,user,true)
      thisVideo.onloadeddata = () => {
        thisVideo.play();
      }
      this.toIp = user;
      this.startLive()
    },
    getPeer(user){
      if(this.peers[user]&&this.peers[user]!==null){
        return this.peers[user]
      }
      const PeerConnection = window.RTCPeerConnection || window.mozRTCPeerConnection || window.webkitRTCPeerConnection;
      const peer = new PeerConnection();
      peer.ontrack = e => {
        if (e && e.streams) {
          this.addStreamAoto(e.streams[0])
        }
      };

      peer.onicecandidate = e => {
        if (e.candidate) {
          this.socket.send(JSON.stringify({
            data:{
              type: this.target+'_ice',
              iceCandidate: e.candidate
            },
            toIp: this.toIp,
            fromIp:this.userAccount
          }));
        } else {
        }
      };
      this.peers[user] = peer;
      return peer;
    },
    async startLive (offerSdp) {
      if(!offerSdp){
        this.target='offer'
      }else {
        this.target='answer'
      }
      var constraints = { audio: true, video: { width: 318, height: 318, transform: 'scaleX(-1)' } }
      const peer =this.getPeer(this.toIp + this.target);
      let stream;
      var thisVideo = document.getElementById('videoCamera0')
      stream = await navigator.mediaDevices.getUserMedia(constraints);
      thisVideo.srcObject = stream;
      stream.getTracks().forEach(track => {
        peer.addTrack(track, stream);
      });
      if (!offerSdp) {
        const offer = await peer.createOffer();
        await peer.setLocalDescription(offer);

        this.socket.send(JSON.stringify({data:offer,toIp: this.toIp,fromIp:this.userAccount}));
      } else {
        await peer.setRemoteDescription(offerSdp);

        const answer = await peer.createAnswer();
        this.socket.send(JSON.stringify({data:answer,toIp: this.toIp,fromIp:this.userAccount}));
        await peer.setLocalDescription(answer);
      }
    },
    muteSound(index) {
      var thisVideo = document.getElementById('videoCamera'+index)
      var thisSound = document.getElementById('muteButton'+index)
      var thisSoundOff = document.getElementById('muteOffButton'+index)
      if(thisVideo.muted){
        thisVideo.muted = false
        thisSound.style.display = 'block'
        thisSoundOff.style.display = 'none'
      }else {
        thisVideo.muted = 'muted'
        thisSoundOff.style.display = 'block'
        thisSound.style.display = 'none'

      }
    },

    stopNavigator (index) {
      this.$http({
        method: 'get',
        url: 'https://test.lx.com:4101/api/user/stopPlay?user='+this.userAccount+'&index='+index
      }).then(res => {
        if (res.data.code === 200) {
          if(index === -1){
            return
          }
          var oper = res.data.data
          console.log(oper)

          var name = document.getElementById('name'+index)
          var recent = name.innerText;
          var tempIndex = [];
          for(var i=1;i <=this.nums;i++ ){
            var nameTemp = document.getElementById('name'+i)
            if(recent === nameTemp.innerText){
              tempIndex.push(i)
            }
          }
          //有点播和被呼叫同时存在
          if(tempIndex.length > 1){
            if(index === tempIndex[1]){
              //被呼叫
              if(this.peers[recent + "answer"]&&this.peers[recent + "answer"]!=null){
                this.peers[recent  + "answer"].close()
                this.peers[recent  + "answer"] = null
              }
              this.display(tempIndex[1],'',false)
            }else {
              if(this.peers[recent + "offer"]&&this.peers[recent + "offer"]!=null){
                this.peers[recent  + "offer"].close()
                this.peers[recent  + "offer"] = null
              }
              this.display(tempIndex[0],'',false)
            }
          }else if(tempIndex.length ===1){
            //只有被呼叫或者呼叫发起方或者点播发起方
            if(oper === 'play'){
              if(this.peers[recent + "offer"]&&this.peers[recent + "offer"]!=null){
                this.peers[recent  + "offer"].close()
                this.peers[recent  + "offer"] = null
              }
            }else {
              if(this.peers[recent + "offer"]&&this.peers[recent + "offer"]!=null){
                this.peers[recent  + "offer"].close()
                this.peers[recent  + "offer"] = null
              }else {
                if(this.peers[recent + "answer"]&&this.peers[recent+"answer"]!=null){
                  this.peers[recent  + "answer"].close()
                  this.peers[recent  + "answer"] = null
                }
              }

            }

            this.display(tempIndex[0],'',false)
          }else {
          }
        } else {
          this.$message({
            type: 'error',
            message: "停止失败"
          })
        }
      })

    },
    fullScreen() {
      var pick = document.getElementsByClassName("active")
      if(pick&&pick[0]){
        var pickId = pick[0].children[0].getAttribute("id").substring(11)
        console.log(pickId)
        this.fullIndex = Number(pickId)
        this.nums=1
      }else {
        this.$message({
          type: 'error',
          message: '请先选中一个屏幕'
        })
      }
    },

    stopAll() {
      this.$http({
        method: 'get',
        url: 'https://test.lx.com:4101/api/user/stopPlay?user='+this.userAccount+'&index=-1'
      }).then(res => {
        if (res.data.code === 200) {
          this.getScreenInfo()
          for(var index=1;index <=this.nums;index++ ){
           this.display(index,'',false)
          }
        var temp = {};
          for (let peersKey in this.peers) {
            if(peersKey.indexOf("offer")>-1){
              var peer = this.peers[peersKey]
              if(peer !==null){

                peer.close()
              }
            }else {
              temp[peersKey] = this.peers[peersKey] ;
            }
          }
          this.peers = temp;
        } else {
          this.$message({
            type: 'error',
            message: "停止失败"
          })
        }
      })
    },
    display(index,fromIp,flag){
      if(flag){
        var thisStop = document.getElementById('stopButton'+index)
        thisStop.style.display = 'block'
        var thisMute = document.getElementById('muteButton'+index)
        thisMute.style.display = 'block'
        this.subtitlesStyle(index)
        var name = document.getElementById('name'+index)
        name.innerText = fromIp
        var thisSub = document.getElementById('subtitles'+index)
        thisSub.style.display = 'block'
        this.tableData[index-1].name = fromIp
      }else {
        var thisVideo = document.getElementById('videoCamera'+index)
        thisVideo.srcObject = null
        var thisStop = document.getElementById('stopButton'+index)
        thisStop.style.display = 'none'
        this.tableData[index-1].name = ''
        var thisMute = document.getElementById('muteButton'+index)
        thisMute.style.display = 'none'
        var thisMuteOff = document.getElementById('muteOffButton'+index)
        thisMuteOff.style.display = 'none'
        var name = document.getElementById('name'+index)
        name.innerText = ''
        var thisSub = document.getElementById('subtitles'+index)
        thisSub.style.display = 'none'
      }
    }
  }
}
</script>
<style  scoped>
  .el-container {
    height: 100%;
    width: 100%;
  }
  /deep/ .el-main{
    color: #333;
    text-align: center;
    background: url("../assets/images/menubackground.png") center
    center no-repeat;
    background-size: 100% auto;
    /*min-height: 88%;*/
    min-width: 100%;
    border-top: 1px solid #eee;
    border-bottom: 1px solid #eee;
    position: absolute;
    top: 60px;
    bottom: 60px;
    /*top: 90px;*/
    /*bottom: 90px;*/
  }
  .el-footer {
    background-color: deepskyblue;
    color: white;
    font-size: 25px;
    line-height: 60px;
    min-height: 60px;
    bottom: 0px;
    width: 100%;
    position: absolute;
  }
  .search_input {
    float: left;
    width: 137px;
  }
  .treeSearchOrign {
    height: auto;
    display: block;
    min-width: 137px;
    width: auto;
    transform-origin: center top;
    z-index: 2001;
    background-color: #2d3c4f ;
  }
</style>
<style>
  .screen {
    margin-left: 30px;border: none;margin-top: 10px;height: 46px;width: 50px
  }
  .main-left {
    border: 3px solid #145f79;
    height: 100%;
    width: 17.3%;
    background-color: #2d3c4f;
  }
  .main-center {
    border: 1px solid #145f79;
    position: relative;
    height: 100%;
    width: 64.5%;
  }
  .main-right {
    border: 3px solid #145f79;
    height: 100%;
    width: 17.3%;
  }
  .item {
    border: 2px solid #145f79;
    background-color: #091c23;
    position: relative;
  }
  .infoList {
    border: 2px solid #145f79;
    position: relative;
  }
  .active {
    border: 2px solid #99f3f5;
  }
  .search {
    width: 100%;
    height: 150px;
    background-color: #132531;
  }
  .el-tree-node{
    border-bottom: 1px solid #27c9b4;
  }
  .el-tree {
    background-color: #2d3c4f ;
    color: white;
  }
  .el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content {
    background-color: #145f79;
  }
  .el-tree-node__content {
    display: flex;
    align-items: center;
    height: 40px;
    cursor: pointer;
  }
  .el-tree-node__content:hover {
    background-color: #145f79;
  }
  .el-table td, .el-table th {
    padding: 9px 0;
  }
  .el-table th {
    background-color: #065671;
  }
  .el-table th>.cell {
    color: white;
  }
  .el-dialog__title,.el-dialog__body,.el-dialog__footer,.el-form-item__label,.el-radio{
    color: black;
  }
  .el-dialog__header {
    background-color: #1e2f7f;
  }
  .el-dialog__body {
    background-color: #89a4d2;
  }
  .dataInfo .el-dialog__body {
    background-color: #89a4d2;
  }
  el-dialog__footer {
    background-color: #e5eaf3;
  }
  .el-input {
     width: 180px;
  }
  .el-button--primary {
    color: black;
    background-color: #64eefa;
    border-color: #64eefa;
  }
  .el-form-item {
    margin-bottom: 0px;
  }
  .tabpage {
    background-color: rgb(11, 74, 88);
    width: 100%;
    line-height: 58px;
    font-size: 28px;
    height: 60px;
    color: white;
    border-right: 1px solid black;
    margin-top: 15px;
  }
  .tabpage:hover {
    background-color: rgb(11, 74, 44);
  }
  .pagecheck {
    background-color: rgb(21 133 148);
  }
  .main-left .el-form-item__label {
    color: white;
  }
  .el-main{
    padding: 0px;
    padding-bottom: 0px;
    padding-top: 0px;
  }
</style>
