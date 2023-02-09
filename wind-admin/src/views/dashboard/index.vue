<template>
  <fullscreen v-model:fullscreen="fullScreen">
    <div ref="view">
      <h1>11111111</h1>
    </div>
  </fullscreen>

</template>
<script>

export default {
  name: 'DashBoard',
  data() {
    return {
      fullScreenState: this.$store.settingStore.fullScreen
    }
  },
  mounted() {
    this.$store.settingStore.setFun(this.toggleFullScreen)
  },
  methods: {
    toggleFullScreen() {
      this.fullScreenState = true
    }
  }

}
</script>
