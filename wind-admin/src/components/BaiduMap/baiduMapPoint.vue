<template>
  <div style="position: relative;">
    <div :style="{height:height,width:width}">
      <el-bmap ref="map" :center="centerLocation" :zoom="zoom" @click="getClickInfo">
        <el-bmap-marker :position="location" enable-dragging raise-on-drag/>
      </el-bmap>
    </div>
    <div style="position: absolute; top:10px; right: 10px; z-index: 10" >
      <el-button type="primary" icon="Delete" circle @click="clear"/>
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
    }
  },
  emits: ['update:modelValue'],
  data() {
    return {
      location: [],
      centerLocation: [],
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
        this.centerLocation = this.location
      }
    }
  },
  created() {
    this.centerLocation = this.center
  },
  methods: {
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
    },
    clear() {
      this.location = []
      this.updateData()
    }

  }

}
</script>

<style scoped>

</style>
