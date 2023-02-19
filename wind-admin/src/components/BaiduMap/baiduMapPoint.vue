<template>
  <div>
    <el-input v-if="showText" v-model="location" />
    <el-button v-if="showText" type="primary" @click="addPath">数据画点</el-button>
    <div :style="{height:height,width:width}">
      <el-bmap ref="map" :center="center" :zoom="zoom" @click="getClickInfo">
        <el-bmap-marker :position="location" enable-dragging raise-on-drag/>
      </el-bmap>
    </div>
  </div>

</template>

<script>
import { nextTick } from 'vue'

export default {
  name: 'BaiduMapPoint',
  props: {
    modelValue: {
      type: String,
      default: undefined
    },
    center: {
      type: Array,
      default: () => {
        return [121.59996, 31.197646]
      }
    },
    zoom: {
      type: Number,
      default: 15
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '400px'
    },
    showText: {
      type: Boolean,
      default: false
    }
  },
  emits: ['update:modelValue'],
  data() {
    return {
      location: [],
      emitting: false
    }
  },
  watch: {
    modelValue: {
      immediate: true,
      handler(val) {
        if (this.emitting) {
          return
        }
        if (val) {
          this.location = JSON.parse(val)
        } else {
          this.point = {}
          this.location = []
        }
      }
    }
  },
  created() {
  },
  methods: {
    addPath() {

    },
    getClickInfo(e) {
      const point = e.latlng
      this.location = [point.lng, point.lat]
      this.updateData()
    },
    updateData() {
      this.emitting = true
      this.$emit('update:modelValue', JSON.stringify(this.location))
      nextTick(() => {
        this.emitting = false
      })
    }

  }

}
</script>

<style scoped>

</style>
