import { createApp } from 'vue'
import pinia, { store } from '@/stores'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue' // global css
import App from './App.vue'
import router from './router'
import * as echarts from 'echarts'
// import { AddBtn, DeleteBtn, EditBtn, ExportBtn, ImportBtn, UpdataBtn } from './components/btn/components/index'
import myAddBtn from './components/btn/components/AddBtn.vue'
import myDeleteBtn from './components/btn/components/DeleteBtn.vue'
import myEditBtn from './components/btn/components/EditBtn.vue'
import myExportBtn from './components/btn/components/ExportBtn.vue'
import myImportBtn from './components/btn/components/ImportBtn.vue'
import myUpdataBtn from './components/btn/components/UpdataBtn.vue'

import { isNull, parseTime, dictLabel } from './utils'
import formValidate from './utils/formValidate'
import 'virtual:svg-icons-register'
import '@/styles/index.scss'
import VueBMap, { initBMapApiLoader } from 'vue-bmap-gl'
import 'vue-bmap-gl/dist/style.css'

const app = createApp(App)
app.component('AddBtn', myAddBtn)
  .component('DeleteBtn', myDeleteBtn)
  .component('EditBtn', myEditBtn)
  .component('ExportBtn', myExportBtn)
  .component('ImportBtn', myImportBtn)
  .component('UpdataBtn', myUpdataBtn)
app.config.globalProperties.$echarts = echarts
// app.use(AddBtn, DeleteBtn, EditBtn, ExportBtn, ImportBtn, UpdataBtn)
app.use(pinia)
app.use(router)
app.use(ElementPlus)
initBMapApiLoader({
  ak: '6C45Mk02tovos4GSlTqp0Vptp8MZjQv7',
  plugins: 'Lushu,TrackAnimation'
})
app.use(VueBMap)

// 初始化图标
app.config.globalProperties.$icons = []
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  // console.log('图标key', key)
  // console.log('图标com', component)
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
