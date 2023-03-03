<template>
  <div ref="view">
    <div class="top">
      <div class="top_left">
        <div class="top_left_top">
          <chart-1 style="width:30%"/>
          <chart-2 style="marginLeft:20px;width:30%"/>
          <chart-3 style="marginLeft:20px;width:30%"/>
        </div>
        <div class="top_left_boom">
          <chart-4 style="width:100%"/>
        </div>
      </div>
      <div class="top_right"></div>
    </div>
  </div>

</template>
<script>
import { useFullscreen } from '@vueuse/core'
import chart1 from './components/chart1.vue'
import chart2 from './components/chart2.vue'
import Chart3 from './components/chart3.vue'
import Chart4 from './components/chart4.vue'
export default {
  name: 'DashBoard',
  components: { chart1, chart2, Chart3, Chart4 },
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
<style lang="scss" scoped>
.top{
  .top_left{
    width: 60%;
    .top_left_top{
      display: flex;
    }
  }
}
</style>
