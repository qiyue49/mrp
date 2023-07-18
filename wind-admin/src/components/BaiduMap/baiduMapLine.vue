<template>
  <div style="position: relative;">
    <div :style="{height:height,width:width}" >
      <el-bmap :center="centerLocation" :zoom="zoom" @moveend="syncCenterAndZoom" @zoomend="syncCenterAndZoom">
        <el-bmap-polyline v-if="show" :path="path" :stroke-color="color" :stroke-opacity="opacity" :stroke-weight="weight" enable-editing @editend="updatePolyLinePath" />
      </el-bmap>
    </div>
    <div style="position: absolute; top:10px; right: 10px; z-index: 10" >
      <el-button type="primary" icon="Plus" circle @click="newLine"/>
      <el-button type="primary" icon="Delete" circle @click="clearLine"/>
    </div>
  </div>
</template>

<script>
import { nextTick } from 'vue'

export default {
  name: 'BaiduMapLine',
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
    color: {
      type: String,
      default: 'blue'
    },
    opacity: {
      type: Number,
      default: 0.5
    },
    weight: {
      type: Number,
      default: 5
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '400px'
    }
  },
  emits: ['update:modelValue'],
  data() {
    return {
      centerLocation: [],
      show: false,
      emitting: false,
      path: undefined
    }
  },
  watch: {
    modelValue: {
      immediate: true,
      handler(val) {
        if (this.emitting) {
          return
        }
        if (!this.isNull(val)) {
          this.path = JSON.parse(val)
          if (!(this.path instanceof Array)) {
            this.path = undefined
            this.show = false
            return
          }
          this.show = true
        } else {
          this.path = undefined
          this.show = false
        }
      }
    }
  },
  created() {
    this.centerLocation = this.center
  },
  methods: {
    syncCenterAndZoom(e) {
      const { lng, lat } = e.target.getCenter()
      this.centerLocation = [lng, lat]
    },
    newLine() {
      const lng = this.centerLocation[0]
      const lat = this.centerLocation[1]
      this.path = []
      this.path.push([lng + 0.01, lat + 0.01])
      this.path.push([lng - 0.01, lat - 0.01])
      this.show = true
      this.updateData()
    },
    clearLine() {
      this.path = []
      this.show = false
      this.updateData()
    },
    updatePolyLinePath(e) {
      const polylinePath = e.target.getPath()
      this.path = []
      polylinePath.forEach(item => {
        this.path.push([item.lng, item.lat])
      })
      this.updateData()
    },
    updateData() {
      this.emitting = true
      this.$emit('update:modelValue', JSON.stringify(this.path))
      nextTick(() => {
        this.emitting = false
      })
    }
  }
}
</script>
