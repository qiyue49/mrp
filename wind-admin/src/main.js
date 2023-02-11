import { createApp } from 'vue'
import pinia, { store } from '@/stores'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue' // global css
import App from './App.vue'
import router from './router'

import { isNull, parseTime, dictLabel } from './utils'
import formValidate from './utils/formValidate'
import 'virtual:svg-icons-register'
import '@/styles/index.scss'
// import Vue3BaiduMapGL from 'vue3-baidu-map-gl'

const app = createApp(App)

app.use(pinia)
app.use(router)
app.use(ElementPlus)
// app.use(Vue3BaiduMapGL, {
//   ak: '6C45Mk02tovos4GSlTqp0Vptp8MZjQv7',
//   plugins: ['TrackAnimation']
// })

// 初始化图标
app.config.globalProperties.$icons = []
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.config.globalProperties.$icons.push(key)
  app.component(key, component)
}

// 定义字典全局方法
app.config.globalProperties.dictList = function (code) {
  return store.dictStore.dicts[code]
}
app.config.globalProperties.dictLabel = dictLabel

// 解析时间格式
app.config.globalProperties.parseTime = parseTime
// 表单校验
app.config.globalProperties.formValidate = formValidate
// pinia
app.config.globalProperties.$store = store
// 判空函数
app.config.globalProperties.isNull = isNull

app.mount('#app')
