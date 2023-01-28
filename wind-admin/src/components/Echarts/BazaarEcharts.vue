<template>
  <div id="echarts_bazaar" ref="echarts"></div>
</template>
<script>
export default {
  props: {
    option: {
      type: Object,
      required: true
    }
  },
  data() {
    return {}
  },
  watch: {
    option(newval) {
      if (newval) {
        this.getEcharts()
      }
    }
  },
  mounted() {
    this.getEcharts()
  },
  methods: {
    getEcharts() {
      const myChart = this.$echarts.init(this.$refs.echarts)
      myChart.setOption(this.option)
      window.addEventListener('resize', () => {
        myChart.resize()
      })
    }
  }
}
</script>
<style scoped lang="scss">
#echarts_bazaar {
  width: 100%;
  height: 100%;
  position: relative;
  z-index: 5;
}
</style>
