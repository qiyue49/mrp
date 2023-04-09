<template>
  <svg aria-hidden="true" class="svg-icon" :style="{ width: size + 'px', height: size + 'px' }">
    <use :xlink:href="symbolId" :fill="color" :stroke-width="strokeWidth" :stroke="color"/>
  </svg>
</template>

<script>
import { computed } from 'vue'

export default {
  name: 'SvgIcon',
  props: {
    prefix: {
      type: String,
      default: 'icon'
    },
    iconClass: {
      type: String,
      required: true
    },
    color: {
      type: String,
      default: 'black'
    },
    strokeWidth: {
      type: Number,
      default: 0
    },
    size: {
      type: [String, Number],
      default: '18'
    }
  },
  setup (props) {
    const symbolId = computed(() => `#${props.prefix}-${props.iconClass}`)
    return { symbolId }
  }
}
</script>

<style scoped>
.svg-icon {
  margin-left: 10px;
  margin-right: 10px;
  overflow: hidden;
  fill: currentColor;
  vertical-align: middle;
}
</style>
