<template>
  <div>
    <svg-icon :icon-class="isFullscreen?'exit-fullscreen':'fullscreen'" @click="click" />
  </div>
</template>

<script>

import SvgIcon from '@/components/SvgIcon/index.vue'

export default {
  name: 'FullScreen',
  components: { SvgIcon },
  data() {
    return {
    }
  },
  methods: {
    click() {
      this.$store.settingStore.toggleFullScreen()
    }
  }
}
</script>

