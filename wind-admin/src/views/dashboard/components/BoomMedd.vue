<template>
  <div class="all">
    <div class="tit">
      <div class="left">
        <el-radio-group v-model="radio1" size="large">
          <el-radio-button label="访问总量" />
          <el-radio-button label="新增访问" />
          <el-radio-button label="活跃访问" />
          <el-radio-button label="流失访问" />
        </el-radio-group>
        <!-- <span :class="{'clickbtn' : isclick ==1}" @click="isclick = 1">访问总量</span>
        <span :class="{'clickbtn' : isclick ==2}" @click="isclick = 2">新增访问</span>
        <span :class="{'clickbtn' : isclick ==3}" @click="isclick = 3">活跃访问</span>
        <span :class="{'clickbtn' : isclick ==4}" @click="isclick = 4">流失访问</span> -->

      </div>
      <div class="right">
        <span :class="{'clickbtn' : isclick1 ==1}" @click="isclick1 = 1">半年</span>
        <span :class="{'clickbtn' : isclick1 ==2}" @click="isclick1 = 2">本月</span>
        <span :class="{'clickbtn' : isclick1 ==3}" @click="isclick1 = 3">本周</span>
      </div>
    </div>
    <div ref="$chart1" style="width: 100%;height: 100%">
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'

export default {
  name: 'BoomMedd',
  data() {
    return {
      isclick: 1,
      isclick1: 1,
      radio1: ref('访问总量')
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
          trigger: 'axis'
          // formatter: '{b0}: {c0}<br />{b1}: {c1}'
        },
        grid: {
          left: '0',
          right: '0',
          bottom: '0',
          containLabel: true
        },
        xAxis: {
          axisTick: {
            show: false // 不显示坐标轴刻度线
          },
          axisLabel: {
            interval: 0,
            rotate: 1
          },
          // show: true, // 不显示坐标轴线、坐标轴刻度线和坐标轴上的文字
          type: 'category',
          // boundaryGap: true,
          boundaryGap: false,
          // boundaryGap: ['0%', '5%'], // 坐标轴两边留白策略
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
            data: [540, 280, 400, 300, 796, 598, 500],
            type: 'line',
            areaStyle: {
              color: new this.$echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                offset: 0,
                color: 'rgba(23, 98, 242, 0.31)'
              },
              {
                offset: 0.25,
                color: 'rgba(23, 98, 242, 0.25)'
              },
              {
                offset: 0.5,
                color: 'rgba(23, 98, 242, 0.02)'
              },
              {
                offset: 1,
                color: 'rgba(23, 98, 242, 0)'
              }
              ])
            },
            itemStyle: {
              color: '#1762F2', // 改变折线点的颜色
              lineStyle: {
                color: '#1762F2', // 改变折线颜色,
                width: 3
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
.all {
  padding: 20px;
  margin-left: 20px;
  border-radius: 10px;
  background: var(--ep-color-background2);

  .tit {
    display: flex;
    justify-content: space-between;

    .left {
      // border-left: 1px solid #EEF0F4;
      span {
        border-right: 1px solid #EEF0F4;
        border-top: 1px solid #EEF0F4;
        border-bottom: 1px solid #EEF0F4;
        padding-top: 10px;
        padding-bottom: 10px;
        width: 90px;
        border-radius: 7px 0px 0px 6px;
        display: inline-block;
        text-align: center;
      }

      .clickbtn {
        background-color: #1762F2;
        color: #fff;
      }
    }

    .right {
      span {
        padding: 5px 10px;
        font-size: 14px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #252631;
        letter-spacing: 2px;
      }

      .clickbtn {
        color: #1762F2;
        font-weight: 600;
      }
    }
  }
}
</style>
