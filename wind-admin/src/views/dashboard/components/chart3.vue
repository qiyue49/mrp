<template>
  <div class="all">
    <div ref="$chart1" style="width: 100%;height: 100%"></div>
  </div>

</template>

<script>
export default {
  name: 'Chart3',
  data() {
    return {}
  },
  mounted() {
    this.echartsInit()
  },
  methods: {
    echartsInit() {
      this.$echarts.init(this.$refs.$chart1).setOption({
        title: {
          text: '订单处理状态统计',
          left: 'center',
          top: '48%',
          textStyle: {
            fontSize: 14,
            fontWeight: 'normal',
            width: 30,
            height: 60
          }
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          bottom: '0',
          left: 'center'
        },
        series: [
          {
            name: 'Access From',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: 'top'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 40,
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: [
              { value: 1048, name: 'Search Engine' },
              { value: 735, name: 'Direct' },
              { value: 580, name: 'Email' },
              { value: 484, name: 'Union Ads' },
              { value: 300, name: 'Video Ads' }
            ]
          }
        ]
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.all{
    background-color: #fff;
    padding: 20px;
    display: flex;
    flex-direction: column;
    align-items: center;
    border-radius: 20px;
}
</style>