<template>
  <div class="all">
    <div class="btn">
      <span>本年</span>
      <span>本季度</span>
      <span>本月</span>
      <span>本周</span>
    </div>
    <div ref="$chart1" style="width: 100%;height: 80%"></div>
  </div>

</template>

<script>
export default {
  name: 'Chart4',
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
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            crossStyle: {
              color: '#999'
            }
          }
        },
        // toolbox: {
        //   feature: {
        //     dataView: { show: false, readOnly: false },
        //     magicType: { show: false, type: ['line', 'bar'] },
        //     restore: { show: true },
        //     saveAsImage: { show: true }
        //   }
        // },
        legend: {
          data: ['入院人数', '出院人数', '花费'],
          x: 'right',
          y: 'top'
        },
        xAxis: [
          {
            type: 'category',
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
            axisPointer: {
              type: 'shadow'
            }
          }
        ],
        yAxis: [
          {
            type: 'value',
            name: '',
            min: 0,
            max: 250,
            interval: 50,
            axisLabel: {
              formatter: '{value}'
            }
          },
          {
            type: 'value',
            name: '花费',
            min: 0,
            max: 25,
            interval: 5,
            axisLabel: {
              formatter: '{value}'
            }
          }
        ],
        series: [
          {
            name: '入院人数',
            type: 'bar',
            itemStyle: {
              normal: {
                color: '#0243A3',
                barBorderRadius: [30, 30, 0, 0]
              }
            },
            tooltip: {
              valueFormatter: function (value) {
                return value + ' ml'
              }
            },
            data: [
              2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3
            ]
          },
          {
            name: '出院人数',
            type: 'bar',
            itemStyle: {
              normal: {
                color: '#C24229',
                barBorderRadius: [30, 30, 0, 0]
              }
            },
            tooltip: {
              valueFormatter: function (value) {
                return value + ' ml'
              }
            },
            data: [
              2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3
            ]
          },
          {
            name: '花费',
            type: 'line',
            yAxisIndex: 1,
            smooth: true,
            showSymbol: false,
            itemStyle: {
              normal: {
                color: '#FFBF49'
              }
            },
            tooltip: {
              valueFormatter: function (value) {
                return value + ' °C'
              }
            },
            data: [2.0, 2.2, 3.3, 4.5, 6.3, 10.2, 20.3, 23.4, 23.0, 16.5, 12.0, 6.2]
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
    // display: flex;
    // flex-direction: column;
    // align-items: center;
    margin-top: 20px;
    border-radius: 20px;
    height: 100%;
    width: 100%;
}
</style>