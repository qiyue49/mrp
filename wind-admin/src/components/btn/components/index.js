// import myAddBtn from './AddBtn.vue'
// import myDeleteBtn from './DeleteBtn.vue'
// import myEditBtn from './EditBtn.vue'
// import myExportBtn from './ExportBtn.vue'
// import myImportBtn from './ImportBtn.vue'
// import myUpdataBtn from './UpdataBtn.vue'

// export default () => {
//   const AddBtn = {
//     install: (Vue) => {
//       Vue.component('AddBtn', myAddBtn)
//     }
//   }
//   const DeleteBtn = {
//     install: (Vue) => {
//       Vue.component('DeleteBtn', myDeleteBtn)
//     }
//   }
//   const EditBtn = {
//     install: (Vue) => {
//       Vue.component('EditBtn', myEditBtn)
//     }
//   }
//   const ExportBtn = {
//     install: (Vue) => {
//       Vue.component('ExportBtn', myExportBtn)
//     }
//   }
//   const ImportBtn = {
//     install: (Vue) => {
//       Vue.component('ImportBtn', myImportBtn)
//     }
//   }
//   const UpdataBtn = {
//     install: (Vue) => {
//       Vue.component('UpdataBtn', myUpdataBtn)
//     }
//   }
//   return { AddBtn, DeleteBtn, EditBtn, ExportBtn, ImportBtn, UpdataBtn }
// }
// // export default { AddBtn, DeleteBtn, EditBtn, ExportBtn, ImportBtn, UpdataBtn }
// const importFn = require.context('./', false, /\.vue$/)
// // console.dir(importFn.keys()) 文件名称数组
// export default {
//   install(app) {
//     // 批量注册全局组件
//     importFn.keys().forEach(key => {
//       // 导入组件
//       const component = importFn(key).default
//       // 注册组件
//       app.component(component.name, component)
//     })
//   }
// }