<template>
  <div class="all">
    <div class="btn">
      <span :class="{'clickbtn' : isclick ==1}" @click="isclick = 1">本年</span>
      <span :class="{'clickbtn' : isclick ==2}" @click="isclick = 2">本季度</span>
      <span :class="{'clickbtn' : isclick ==3}" @click="isclick = 3">本月</span>
      <span :class="{'clickbtn' : isclick ==4}" @click="isclick = 4">本周</span>
    </div>
    <div ref="$chart1" style="width: 100%;height: 90%;margin-top: -30px;"></div>
  </div>

</template>

<script>
export default {
  name: 'Chart4',
  data() {
    return {
      isclick: 1
    }
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
        grid: {
          left: '0',
          right: '0',
          bottom: '0',
          containLabel: true
        },
        legend: {
          data: ['入院人数', '出院人数', '花费'],
          x: 'right',
          y: 'top'
        },
        xAxis: [
          {
            type: 'category',
            axisTick: {
              show: false // 不显示坐标轴刻度线
            },
            data: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
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
            // name: '花费',
            min: 0,
            max: 5000,
            interval: 1000,
            axisLabel: {
              formatter: '{value}'
            }
          }
        ],
        series: [
          {
            name: '入院人数',
            type: 'bar',
            barWidth: 15,
            itemStyle: {
              normal: {
                color: '#1762F2',
                barBorderRadius: [30, 30, 0, 0]
              }
            },
            tooltip: {
              valueFormatter: function (value) {
                return value + ' ml'
              }
            },
            data: [
              2.0, 4.9, 7.0, 232, 25.6, 76.7, 135.6, 162.2, 32.6, 200, 64, 33
            ]
          },
          {
            name: '出院人数',
            type: 'bar',
            barWidth: 15,
            itemStyle: {
              normal: {
                color: '#D34D25',
                barBorderRadius: [30, 30, 0, 0]
              }
            },
            tooltip: {
              valueFormatter: function (value) {
                return value + ' ml'
              }
            },
            data: [
              190.0, 50.9, 90, 26.4, 287, 70.7, 1756, 1822, 487, 188, 60, 23
            ]
          },
          {
            name: '花费',
            type: 'line',
            // type: 'bar',
            // lineWidth: 100,
            yAxisIndex: 1,
            smooth: true,
            showSymbol: false,
            itemStyle: {
              normal: {
                color: '#FFC700',
                lineStyle: {
                  width: 3
                }
              }

            },
            tooltip: {
              valueFormatter: function (value) {
                return value + ' °C'
              }
            },
            data: [200, 202, 303, 2000, 4003, 1002, 20.3, 23.4, 23.0, 1605, 12.0, 602]
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
    padding-bottom: 0;
    // display: flex;
    // flex-direction: column;
    // align-items: center;
    // margin-top: 20px;
    border-radius: 10px;
    height: 100%;
    width: 100%;
    .btn{
      border-left: 1px solid #EEF0F4;
      border-top-left-radius: 5px;
      span{
        border-right: 1px solid #EEF0F4;
        border-top: 1px solid #EEF0F4;
        border-bottom: 1px solid #EEF0F4;
        padding-top: 10px;
        padding-bottom: 10px;
        width: 100px;
        display: inline-block;
        border-radius: 7px 0px 0px 6px;
        text-align: center;
      }
      .clickbtn{
        background-color: #1762F2;
        color: #fff;
      }
    }
}
</style>