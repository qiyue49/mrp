<template>
  <div>
    <el-link v-for="item in fileList" :key="item.response.data" target="_blank" :href="item.response.data" :underline="true">
      {{ item.name }}
    </el-link>
  </div>

</template>

<script>
// 本组件和上传文件组件配套使用，用于展示页面中展示上传的文件列表
// value是一个json数组，字段有name和url
export default {
  name: 'FileDownloadList',
  props: {
    value: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      fileList: []
    }
  },
  watch: {
    value: {
      immediate: true,
      handler(val) {
        if (this.isNull(val)) {
          this.fileList = []
          return
        }
        if (this.flag) {
          return
        }
        const array = JSON.parse(val)
        array.forEach(item => {
          const file = {}
          file.name = item.name
          file.status = 'success'
          file.response = { data: item.url }
          this.fileList.push(file)
        })
      }
    }
  }
}
</script>

<style scoped>

</style>
