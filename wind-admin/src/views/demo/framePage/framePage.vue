<template>
  <div :style="'height:' + height">
    <iframe id="template-iframe" ref="iframe" :src="src" style="width: 100%; height: 100%" frameborder="no" scrolling="auto"></iframe>
  </div>
</template>

<script>
export default {
  name: 'FramePage',
  data() {
    return {
      height: document.documentElement.clientHeight - 94.5 + 'px;',
      src: undefined
    }
  },
  mounted() {
    this.src = 'http://www.163.com'
    const that = this
    window.onresize = function temp() {
      if (that.$store.state.settings.fullScreen) {
        that.height = '100vh'
      } else {
        that.height = document.documentElement.clientHeight - 94.5 + 'px;'
      }
    }
  }
}
</script>

<style scoped>

</style>
