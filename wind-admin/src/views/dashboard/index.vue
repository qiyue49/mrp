<template>
  <div ref="view">
    <h1>11111111</h1>
  </div>

</template>
<script>
import { useFullscreen } from '@vueuse/core'
export default {
  name: 'DashBoard',
  mounted() {
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
