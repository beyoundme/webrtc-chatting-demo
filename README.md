# SUMMARY
    简书 NonTalk也是我的号，感谢支持。
    此项目是本人以java开发的角度，一步步学习，尝试后，构建的一套基于webrtc的视频播放，呼叫系统。
    由于在c++知识上的短缺，在调研可用的媒体服务上花费了大量的时间；
    前端方面也是从实现功能的角度出发，整体前端代码构筑会比较凌乱。希望对开发相关项目的朋友们有启发作用。
# 项目框架
![图片名称](https://github.com/beyoundme/webrtc-chatting-demo/blob/master/window.png) 
# 项目综述
    在这里，默认使用浏览器实现所有音视频操作的前提是webrtc协议，这也是当前较为成熟，多家浏览器厂商推崇的方式。

    服务器系统版本为centos7。

    vue版本为vue2，前端项目必须是https服务。

    https前端服务无法连接ws服务，因此ws服务也必须是wss，由于wss要求域名，于是需要为服务器指定一个假域名，临时在用户端修改host文件，添加：假域名  服务器ip

    在整个项目包含三条业务线，用户点播用户，用户呼叫用户，用户点播rtsp设备。下面就从这三条业务线出发，介绍每个服务的功能职责。
# 服务部署
    1.java项目依赖与gradle 5.76及java8，编译打包后，在服务器上运行jar包
    
    2.vue项目依赖于node.js，npm install载入依赖后，在服务器上运行npm run serve
    
    3.streamer拷贝到服务器后，注意赋予执行程序权限，./webrtc-streamer 执行，如果失败，则是因为系统环境不支持
    
    参考： [centos 7.6 使用webrtc-streamer + vue 播放rtsp视频，被迫升级gibc，升级gcc总结](https://www.jianshu.com/p/2035bba2b5e2)
    
    4.全部执行成功后通过lsof -i:port 命令检查，8000(streamer)，9443(vue)，4101（java）是否开放，开放则成功。
    
    5.在局域网内随意一台电脑，修改host文件，添加 test.lx.com 服务器ip
    
    6.打开谷歌浏览器访问https://服务器ip:9443/ 信任前端证书
    
    7.同浏览器访问https://test.lx.com 信任后台证书，因为接口无法弹出信任窗口，于是需要手动访问信任
    
    8.登录账号，笔者为了方便使用，在后台项目的resources文件夹存放了几个临时的账号和设备，可以直接使用
    
    9.登录root  123456
    
    10.点播rtcp设备，点播用户，呼叫用户（多个用户上线的情况下）都是成功的。亲测海康大华摄像头都是有效的，修改临时设备txt中的rtsp地址即可。
    
    11.注意服务器和浏览器所在电脑全部关闭防火墙。
    
# 用户点播用户
   ###### 参与方：浏览器；vue前端项目；java简易ws服务
   ###### 业务线：
       1）用户A在浏览器访问位于服务器上的vue前端项目，完成登录鉴权

       2）用户A在浏览器点播用户B，基于webrtc协议，浏览器向ws服务发起协商请求。

       3）ws服务解包后将数据发给目标浏览器，这里基本上只做委托转发。

       4）用户B的浏览器基于webrtc标准完成协商后，向用户A的浏览器推流。

# 用户呼叫用户
   ###### 参与方：浏览器；vue前端项目；java简易ws服务
   ###### 业务线：
        1）用户A在浏览器访问位于服务器上的vue前端项目，完成登录鉴权

        2）用户A在浏览器呼叫用户B，基于webrtc协议，浏览器向ws服务发起协商请求。

        3）ws服务解包后将数据发给目标浏览器，这里默认被呼叫方默认接受，因为基于浏览器做交互式延迟较高，这里不做。

        4）用户B的浏览器基于webrtc标准完成协商后，AB互相推流。

# 用户点播rtsp设备
   ###### 参与方：浏览器；vue前端项目；webrtc-streamer
   ###### 业务线：
        1）用户A在浏览器访问位于服务器上的vue前端项目，完成登录鉴权

        2）用户A在浏览器点播rtsp，基于webrtc协议，浏览器调用webrtc-streamer.js。

        3）webrtc-streamer.js请求webrtc-streamer服务，模拟浏览器进行udp协商。

        4）完成协商后，webrtc-streamer服务向用户A的浏览器推流。

# 代码讲解
挺麻烦的，算了吧

