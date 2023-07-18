<template>
  <div>
    <el-row :gutter="gutter">
      <el-col v-for="(item, index) in imageList" :key="index" :span="span" style="margin-top: 10px">
        <el-image :src="item" :preview-src-list="imageList" :style="{width: width, height: height}" :fit="fit" />
      </el-col>
    </el-row>
  </div>
</template>
<script>
import { isJson } from '@/utils'

export default {
  name: 'ImageGallery',
  props: {
    modelValue: {
      type: [String, Array],
      required: false,
      default: ''
    },
    fit: {
      type: String,
      required: false,
      default: 'fill'
    },
    span: {
      type: Number,
      required: false,
      default: 8
    },
    width: {
      type: String,
      required: false,
      default: '100%'
    },
    height: {
      type: String,
      required: false,
      default: '100%'
    },
    gutter: {
      type: Number,
      required: false,
      default: 10
    }
  },
  data() {
    return {
      imageList: []
    }
  },
  watch: {
    modelValue: {
      immediate: true,
      handler(val) {
        this.imageList = []
        if (this.isNull(val)) {
          return
        }
        if (val instanceof Array) {
          this.imageList = val
        } else if (isJson(val)) {
          const list = JSON.parse(val)
          list.forEach(item => {
            this.imageList.push(item.url)
          })
        } else {
          this.imageList = val.split(',')
        }
      }
    }
  }
}
</script>

<style scoped>

</style>
