<template>
  <div>
    <svg-icon icon-class="ui-resolution" color="#6B738D" @click="click" />
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

