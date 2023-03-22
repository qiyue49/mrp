<template>
  <div class="all">
    <div class="tit">
      <div class="left">
        <span :class="{'clickbtn' : isclick ==1}" @click="isclick = 1">访问总量</span>
        <span :class="{'clickbtn' : isclick ==2}" @click="isclick = 2">新增访问</span>
        <span :class="{'clickbtn' : isclick ==3}" @click="isclick = 3">活跃访问</span>
        <span :class="{'clickbtn' : isclick ==4}" @click="isclick = 4">流失访问</span>

      </div>
      <div class="right">
        <span :class="{'clickbtn' : isclick1 ==1}" @click="isclick1 = 1">半年</span>
        <span :class="{'clickbtn' : isclick1 ==2}" @click="isclick1 = 2">本月</span>
        <span :class="{'clickbtn' : isclick1 ==3}" @click="isclick1 = 3">本周</span>
      </div>
    </div>
    <div ref="$chart1" style="width: 100%;height: 100%">
    </div>
  </div></template>

<script>
export default {
  name: 'BoomMedd',
  data() {
    return {
      isclick: 1,
      isclick1: 1
    }
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
        grid: {
		        left: '0',
		        right: '0',
		        bottom: '0',
		        containLabel: true
		    },
        xAxis: {
          show: true, // 不显示坐标轴线、坐标轴刻度线和坐标轴上的文字
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
            data: [0, 1500, 90, 94, 1290, 1330, 320],
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
                  color: '#1E6ADB', // 改变折线颜色,
                  width: 3
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
  padding: 20px;
    background-color: #fff;
    margin-left: 20px;
    border-radius: 10px;
    .tit{
       display: flex;
       justify-content: space-between;
       .left{
        border-left: 1px solid #EEF0F4;
        span{
        border-right: 1px solid #EEF0F4;
        border-top: 1px solid #EEF0F4;
        border-bottom: 1px solid #EEF0F4;
        padding: 5px 10px;
      }
      .clickbtn{
        background-color: #1762F2;
        color: #fff;
      }
       }
       .right{
        span{
        padding: 5px 10px;
      }
      .clickbtn{
        color: #1762F2;
      }
       }
    }
}
</style>