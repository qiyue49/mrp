<template>
  <div class="all">
    <div class="btn">
      <el-radio-group v-model="radio1" size="large">
        <el-radio-button label="本年" />
        <el-radio-button label="本季度" />
        <el-radio-button label="本月" />
        <el-radio-button label="本周" />
      </el-radio-group>
      <!-- <span :class="{'clickbtn' : isclick ==1}" @click="isclick = 1">本年</span>
      <span :class="{'clickbtn' : isclick ==2}" @click="isclick = 2">本季度</span>
      <span :class="{'clickbtn' : isclick ==3}" @click="isclick = 3">本月</span>
      <span :class="{'clickbtn' : isclick ==4}" @click="isclick = 4">本周</span> -->
    </div>
    <div ref="$chart1" style="width: 100%;height: 90%;"></div>
  </div>

</template>

<script>
import { ref } from 'vue'
export default {
  name: 'Chart4',
  data() {
    return {
      isclick: 1,
      radio1: ref('本年')
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
            data: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'June', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
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
              color: '#1762F2',
              borderRadius: [30, 30, 0, 0]
            },
            // tooltip: {
            //   valueFormatter: function (value) {
            //     return value + ' ml'
            //   }
            // },
            data: [
              140, 160, 100, 210, 70, 140, 40, 160, 100, 30, 130, 130
            ]
          },
          {
            name: '出院人数',
            type: 'bar',
            barWidth: 15,
            itemStyle: {
              color: '#D34D25',
              borderRadius: [30, 30, 0, 0]
            },
            // tooltip: {
            //   valueFormatter: function (value) {
            //     return value + ' ml'
            //   }
            // },
            data: [
              120, 152, 102, 230, 115, 70, 30, 180, 87, 145, 170, 170
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
              color: '#FFC700',
              lineStyle: {
                width: 3
              }

            },
            // tooltip: {
            //   valueFormatter: function (value) {
            //     return value + ' °C'
            //   }
            // },
            data: [2140, 2280, 4103, 3160, 3159, 2440, 2200, 3000, 2843, 3053, 3900, 3892]
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
    padding-bottom: 0;
    position: relative;
    background:var(--ep-color-background2) ;
    // display: flex;
    // flex-direction: column;
    // align-items: center;
    // margin-top: 20px;
    border-radius: 10px;
    height: 100%;
    width: 100%;
    .btn{
      // border-left: 1px solid #EEF0F4;
      border-top-left-radius: 5px;
      z-index: 99;
     position: absolute;
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
