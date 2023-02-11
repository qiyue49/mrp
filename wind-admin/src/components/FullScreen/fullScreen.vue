<template>
  <div>
    <svg-icon icon-class="fullscreen" @click="click" />
  </div>
</template>

<script>

import SvgIcon from '@/components/SvgIcon/index.vue'

export default {
  name: 'FullScreen',
  components: { SvgIcon },
  methods: {
    click() {
      this.$store.settingStore.toggleFullScreen()
    }
  }
}
</script>

