<template>
  <div ref="view">
    <iframe id="template-iframe" ref="iframe" :src="src" style="width: 100%; height: 100%" frameborder="no" scrolling="auto" :style="'height:' + height"></iframe>
  </div>
</template>

<script>

import { useFullscreen } from '@vueuse/core'

export default {
  name: 'FramePage',
  data() {
    return {
      height: document.documentElement.clientHeight - 94.5 + 'px;',
      src: undefined
    }
  },
  computed: {
    fullscreen: {
      get() {
        return this.$store.settingStore.fullScreen
      },
      set(val) {
        this.$store.settingStore.changeSetting({
          key: 'fullScreen',
          value: val
        })
      }
    }
  },
  mounted() {
    this.src = 'http://www.163.com'
    const that = this
    window.onresize = function temp() {
      if (that.$store.settingStore.fullScreen) {
        that.height = '100vh'
      } else {
        that.height = document.documentElement.clientHeight - 94.5 + 'px;'
      }
    }
    this.$store.settingStore.setFun(this.toggleFullScreen)
  },
  methods: {
    toggleFullScreen() {
      const { toggle } = useFullscreen(this.$refs.view)
      toggle()
    }
  }
}
</script>

<style lang="scss" scoped>
.right-menu-item {
  display: inline-block;
  padding: 0 8px;
  height: 100%;
  font-size: 28px;
  color: #5a5e66;
  vertical-align: text-bottom;

  &.hover-effect {
     cursor: pointer;
     transition: background .3s;

  &:hover {
     background: rgba(0, 0, 0, .025)
   }
  }
}

</style>
