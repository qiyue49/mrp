<template>
  <div>
    <svg-icon icon-class="quanping" @click="click" />
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

