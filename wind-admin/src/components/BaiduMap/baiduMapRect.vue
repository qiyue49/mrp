<template>
  <div>
    <el-button type="primary" @click="newRect">添加多边形</el-button>
    <el-button type="primary" @click="clearRect">清除多边形</el-button>
    <el-button v-if="showText" type="primary" @click="addPath">数据画线</el-button>
    <el-input v-if="showText" v-model="path" style="margin-top: 10px; margin-bottom: 10px" />
    <div :style="{height:height,width:width}">
      <el-bmap :center="center" :zoom="zoom" @moveend="syncCenterAndZoom" @zoomend="syncCenterAndZoom">
        <el-bmap-polygon v-if="show" :path="path" :stroke-color="color" :stroke-opacity="opacity" :stroke-weight="weight" enable-editing @editend="updatePolygonPath" />
      </el-bmap>
    </div>
  </div>
</template>

<script>
export default {
  name: 'BaiduMapRect',
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
    },
    showText: {
      type: Boolean,
      default: false
    }
  },
  emits: ['update:modelValue'],
  data() {
    return {
      mapCenter: {},
      show: false,
      path: undefined
    }
  },
  watch: {
    modelValue: {
      immediate: true,
      handler(val) {
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
    this.mapCenter.lat = this.center[1]
    this.mapCenter.lng = this.center[0]
  },
  methods: {
    syncCenterAndZoom(e) {
      const { lng, lat } = e.target.getCenter()
      this.mapCenter.lng = lng
      this.mapCenter.lat = lat
    },
    newRect() {
      const lng = this.mapCenter.lng
      const lat = this.mapCenter.lat
      this.path = []
      this.path.push([lng + 0.01, lat + 0.01])
      this.path.push([lng + 0.01, lat - 0.01])
      this.path.push([lng - 0.01, lat - 0.01])
      this.path.push([lng - 0.01, lat + 0.01])
      this.show = true
      this.updateData()
    },
    clearRect() {
      this.path = []
      this.show = false
      this.updateData()
    },
    addPath: function() {
    },
    updatePolygonPath(e) {
      const polygonPath = e.target.getPath()
      this.path = []
      polygonPath.forEach(item => {
        this.path.push([item.lng, item.lat])
      })
      this.updateData()
    },
    updateData() {
      this.$emit('update:modelValue', JSON.stringify(this.path))
    }

  }
}
</script>

<style scoped>

</style>
