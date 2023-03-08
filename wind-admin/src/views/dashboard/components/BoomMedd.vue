<template>
  <div class="all">
    <div class="tit">
      <div class="left">
        <span>访问总量</span>
        <span>新增访问</span>
        <span>活跃访问</span>
        <span>流量访问</span>
      </div>
      <div class="right">
        <span>半年</span>
        <span>本月</span>
        <span>本周</span>
      </div>
    </div>
    <div ref="$chart1" style="width: 100%;height: 100%">
    </div>
  </div></template>

<script>
export default {
  name: 'BoomMedd',
  data() {
    return {}
  },
  mounted() {
    this.echartsInit()
  },
  methods: {
    echartsInit() {
      this.$echarts.init(this.$refs.$chart1).setOption({
        tooltip: {
          show: true,
          trigger: 'axis',
          formatter: '{b0}: {c0}<br />{b1}: {c1}'
        },
        xAxis: {
          show: false, // 不显示坐标轴线、坐标轴刻度线和坐标轴上的文字
          type: 'category',
          boundaryGap: false,
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
        },
        yAxis: {
          type: 'value',
          show: true // 不显示坐标轴线、坐标轴刻度线和坐标轴上的文字
        },
        series: [
          {
            showSymbol: false,
            smooth: true,
            data: [820, 932, 901, 934, 1290, 1330, 1320],
            type: 'line',
            areaStyle: {
              color: new this.$echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                offset: 0,
                color: 'rgba(26, 98, 206, 0.31)'
              },
              {
                offset: 0.25,
                color: 'rgba(32, 101, 206, 0.25)'
              },
              {
                offset: 0.5,
                color: 'rgba(61, 119, 208, 0.02)'
              },
              {
                offset: 1,
                color: 'rgba(72, 126, 208, 0)'
              }
              ])
            },
            itemStyle: {
              normal: {
                color: '#1E6ADB', // 改变折线点的颜色
                lineStyle: {
                  color: '#1E6ADB' // 改变折线颜色
                }
              }
            }
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
    margin-left: 20px;
    .tit{
       display: flex;
       justify-content: space-between;
    }
}
</style>