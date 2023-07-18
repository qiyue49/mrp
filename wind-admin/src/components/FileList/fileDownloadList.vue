<template>
  <div>
    <el-link v-for="item in fileList" :key="item.url" target="_blank" :href="item.url" :underline="true" style="margin: 10px">
      <el-icon>
        <Document />
      </el-icon>
      {{ item.name }}
    </el-link>
  </div>

</template>

<script>
// 本组件和上传文件组件配套使用，用于展示页面中展示上传的文件列表
// value是一个json数组，字段有name和url
import { isJson } from '@/utils'

export default {
  name: 'FileDownloadList',
  props: {
    modelValue: {
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
    modelValue: {
      immediate: true,
      handler(val) {
        if (this.isNull(val)) {
          this.fileList = []
          return
        }
        if (isJson(val)) {
          const array = JSON.parse(val)
          array.forEach(item => {
            const file = {}
            file.name = item.name
            file.url = item.url
            this.fileList.push(file)
          })
        } else {
          const array = val.split(',')
          array.forEach(item => {
            const file = {}
            file.name = item
            file.url = item
            this.fileList.push(file)
          })
        }
      }
    }
  }
}
</script>
