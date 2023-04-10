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
          text: '订单处理',
          subtext: '状态统计',
          left: 'center',
          top: '33%',
          // backgroundColor: 'red',
          borderRadius: '10',
          textStyle: {
            fontSize: 14,
            color: '#252631',
            fontFamily: 'PingFangSC-Semibold',
            fontWeight: '600',
            letterSpacing: '2px',
            width: 30,
            height: 60
          },
          subtextStyle: {
            fontSize: 14,
            color: '#252631',
            fontFamily: 'PingFangSC-Semibold',
            fontWeight: '600',
            letterSpacing: '2px',
            width: 30,
            height: 60
          }
        },
        tooltip: {
          trigger: 'item'
        },
        grid: {
          left: '-10%',
          right: '0',
          bottom: '0',
          top: '5%',
          containLabel: true
        },
        legend: {
          bottom: '30',
          left: 'center'
        },
        series: [
          {
            name: 'Access From',
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['50%', '40%'],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: 'top'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 14,
                fontWeight: '600'
              }
            },
            labelLine: {
              show: false
            },
            data: [
              {
                value: 1000,
                name: '已处理',
                itemStyle: {
                  color: '#1762F2'
                }
              },
              {
                value: 1000,
                name: '未处理',
                itemStyle: {
                  color: '#FFC700'
                }
              },
              {
                value: 1000,
                name: '进行中',
                itemStyle: {
                  color: '#D34D25'
                }
              }
            ]
          },
          {
            name: 'Access From',
            type: 'pie',
            radius: ['30%', '0%'],
            center: ['50%', '40%'],
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: 'top'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 14,
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: [
              {
                value: 1000,
                itemStyle: {
                  color: '#F3F5F8'
                  // color: '#red'
                }
              }
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
    // padding: -40px;
    display: flex;
    flex-direction: column;
    align-items: center;
    border-radius: 10px;
    background:var(--ep-color-background2) ;
}
</style>
