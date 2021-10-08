<template>
  <div class="s-canvas">
    <canvas id="s-canvas" :width="contentWidth" :height="contentHeight"></canvas>
  </div>
</template>
<script>
export default {
  name: 'SIdentify',
  props: {
    // 默认注册码
    identifyCode: {
      type: String,
      default: '1234'
    },
    // 字体最小值
    fontSizeMin: {
      type: Number,
      default: 35
    },
    // 字体最大值
    fontSizeMax: {
      type: Number,
      default: 35
    },
    // 背景颜色色值最小值
    backgroundColorMin: {
      type: Number,
      default: 180
    },
    // 背景颜色色值最大值
    backgroundColorMax: {
      type: Number,
      default: 240
    },
    // 字体颜色色值最小值
    colorMin: {
      type: Number,
      default: 50
    },
    // 字体颜色色值最大值
    colorMax: {
      type: Number,
      default: 160
    },
    // 干扰线颜色色值最小值
    lineColorMin: {
      type: Number,
      default: 100
    },
    // 干扰线颜色色值最大值
    lineColorMax: {
      type: Number,
      default: 200
    },
    // 干扰点颜色色值最小值
    dotColorMin: {
      type: Number,
      default: 0
    },
    // 干扰点颜色色值最大值
    dotColorMax: {
      type: Number,
      default: 255
    },
    // 画布宽度
    contentWidth: {
      type: Number,
      default: 120
    },
    // 画布高度
    contentHeight: {
      type: Number,
      default: 40
    }
  },
  methods: {
    // 生成一个随机数
    randomNum (min, max) {
      return Math.floor(Math.random() * (max - min) + min)
    },
    // 生成一个随机的颜色
    randomColor (min, max) {
      const r = this.randomNum(min, max)
      const g = this.randomNum(min, max)
      const b = this.randomNum(min, max)
      return 'rgb(' + r + ',' + g + ',' + b + ')'
    },
    drawPic () {
      const canvas = document.getElementById('s-canvas')
      const ctx = canvas.getContext('2d')
      ctx.textBaseline = 'bottom'
      // 绘制背景
      ctx.fillStyle = this.randomColor(this.backgroundColorMin, this.backgroundColorMax)
      ctx.fillRect(0, 0, this.contentWidth, this.contentHeight)
      // 绘制文字
      for (let i = 0; i < this.identifyCode.length; i++) {
        this.drawText(ctx, this.identifyCode[i], i)
      }
      this.drawLine(ctx)
      this.drawDot(ctx)
    },
    drawText (ctx, txt, i) {
      // 随机生产字体颜色
      ctx.fillStyle = this.randomColor(this.colorMin, this.colorMax)
      // 随机生成字体大小
      ctx.font = this.randomNum(this.fontSizeMin, this.fontSizeMax) + 'px SimHei'
      const x = (i + 1) * (this.contentWidth / (this.identifyCode.length + 1))
      const y = this.randomNum(this.fontSizeMax, this.contentHeight - 5)
      var deg = this.randomNum(-45, 45)
      // 修改坐标原点和旋转角度
      ctx.translate(x, y)
      ctx.rotate(deg * Math.PI / 180)
      ctx.fillText(txt, 0, 0)
      // 恢复坐标原点和旋转角度
      ctx.rotate(-deg * Math.PI / 180)
      ctx.translate(-x, -y)
    },
    drawLine (ctx) {
      // 绘制干扰线
      for (let i = 0; i < 5; i++) {
        ctx.strokeStyle = this.randomColor(this.lineColorMin, this.lineColorMax)
        ctx.beginPath()
        ctx.moveTo(this.randomNum(0, this.contentWidth), this.randomNum(0, this.contentHeight))
        ctx.lineTo(this.randomNum(0, this.contentWidth), this.randomNum(0, this.contentHeight))
        ctx.stroke()
      }
    },
    drawDot (ctx) {
      // 绘制干扰点
      for (let i = 0; i < 80; i++) {
        ctx.fillStyle = this.randomColor(0, 255)
        ctx.beginPath()
        ctx.arc(this.randomNum(0, this.contentWidth), this.randomNum(0, this.contentHeight), 1, 0, 2 * Math.PI)
        ctx.fill()
      }
    }
  },
  watch: {
    identifyCode () {
      this.drawPic()
    }
  },
  mounted () {
    this.drawPic()
  }
}
</script>
