/**
 * 自定义组件,点击被插入该指令的元素外的任意空白处，触发指令所绑定的方法
 */
import Vue from 'vue'
import { on } from '../util/dom'

const nodeList = []
const ctx = '@@clickoutsideContext'

let startClick
let seed = 0

// 鼠标按下时 记录按下元素的事件对象
!Vue.prototype.$isServer && on(document, 'mousedown', e => (startClick = e))

// 鼠标松开时 遍历 nodeList 中的元素，执行 documentHandler
!Vue.prototype.$isServer && on(document, 'mouseup', e => {
  nodeList.forEach(node => node[ctx].documentHandler(e, startClick))
})
function createDocumentHandler (el, binding, vnode) {
  // 接收参数为：鼠标松开和鼠标按下的事件对象
  return function (mouseup = {}, mousedown = {}) {
    if (!vnode ||
      !vnode.context ||
      !mouseup.target ||
      !mousedown.target ||
      el.contains(mouseup.target) ||
      el.contains(mousedown.target) ||
      el === mouseup.target ||
      (vnode.context.popperElm &&
        (vnode.context.popperElm.contains(mouseup.target) ||
          vnode.context.popperElm.contains(mousedown.target)))) return
    // 执行绑定指令时的函数
    if (binding.expression &&
      el[ctx].methodName &&
      vnode.context[el[ctx].methodName]) {
      vnode.context[el[ctx].methodName]()
    } else {
      el[ctx].bindingFn && el[ctx].bindingFn()
    }
  }
}

export default {
  // 指令绑定时触发
  bind (el, binding, vnode) {
    nodeList.push(el)
    const id = seed++
    el[ctx] = {
      id,
      documentHandler: createDocumentHandler(el, binding, vnode),
      methodName: binding.expression,
      bindingFn: binding.value
    }
  },
  update (el, binding, vnode) {
    el[ctx].documentHandler = createDocumentHandler(el, binding, vnode)
    el[ctx].methodName = binding.expression
    el[ctx].bindingFn = binding.value
  },
  unbind (el) {
    const len = nodeList.length
    for (let i = 0; i < len; i++) {
      if (nodeList[i][ctx].id === el[ctx].id) {
        nodeList.splice(i, 1)
        break
      }
    }
    delete el[ctx]
  }
}
