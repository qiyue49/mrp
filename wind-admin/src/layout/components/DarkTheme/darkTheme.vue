<template>
  <div>
    <svg-icon :icon-class="isDark ? 'weather-sun' : 'weather-moon'" color="#6B738D" @click="toggleDark()" />
  </div>
</template>

<script setup>
import { useToggle } from '@vueuse/shared'
import { useDark } from '@vueuse/core'
import SvgIcon from '../../../components/SvgIcon/index.vue'
// import { isDark } from '../../.vitepress/theme/composables/dark'
const isDark = useDark({
  // 存储到localStorage/sessionStorage中的Key 根据自己的需求更改
  storageKey: 'useDarkKEY',
  // 暗黑class名字
  valueDark: 'dark',
  // 高亮class名字
  valueLight: 'light'
})
const toggleDark = useToggle(isDark)
</script>
