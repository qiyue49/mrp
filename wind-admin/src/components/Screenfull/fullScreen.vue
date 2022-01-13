<template>
  <div>
    <svg-icon :icon-class="isFullscreen?'exit-fullscreen':'fullscreen'" @click="click" />
  </div>
</template>

<script>

export default {
  name: 'FullScreen',
  data() {
    return {
    }
  },
  computed: {
    isFullscreen: {
      get() {
        return this.$store.state.settings.fullScreen
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'fullScreen',
          value: val
        })
      }
    }
  },
  methods: {
    click() {
      this.$store.dispatch('settings/changeSetting', {
        key: 'fullScreen',
        value: true
      })
    }
  }
}
</script>

