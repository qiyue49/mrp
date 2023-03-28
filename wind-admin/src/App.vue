<template>
  <el-config-provider :locale="locale">
    <router-view />
  </el-config-provider>
</template>

<script>
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import { ElConfigProvider } from 'element-plus'
export default {
  name: 'App',
  components: { ElConfigProvider },
  setup() {
    return {
      locale: zhCn
    }
  }

}
</script>
// <style lang="scss">
//  .dialog-title .el-dialog__header {
//    background-color: #3CA1FF;
//    margin-right: 0px;
//    display: flex;
//    justify-content: center;
//    .el-dialog__title, .el-dialog__headerbtn .el-dialog__close {
//      color: #ffffff;
//    }
//  }
// </style>