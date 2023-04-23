<template>
  <div class="all">
    <div class="f1">
      <div class="f1_1">
        <div class="left">
          <img src="../../../assets/img/dashboard/visitNumber.png" alt=""/>
          <div>
            <span class="left_top">{{ Math.round(count) }}</span>
            <span class="left_boom">访问次数</span>
          </div>
        </div>
        <div class="right">
          <img src="../../../assets/img/dashboard/more.png" alt=""/>
        </div>
      </div>
    </div>
    <div class="f2">
      <div ref="$chart1" style="width: 100%;height: 100%">
      </div>
    </div>
    <div class="f3">
      查 看 数 据 详 情
      <!-- <span>查看数据详情</span> -->
    </div>
  </div></template>

<script>
import { ref } from 'vue'
import { TransitionPresets, useTransition } from '@vueuse/core'

export default {
  name: 'Chart1',
  data() {
    return {
      charts: null,
      count: 0
    }
  },
  mounted() {
    const number = ref(0)
    this.count = useTransition(number, {
      duration: 3000,
      transition: TransitionPresets.easeOutExpo
    })
    number.value = 1568
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
          left: '-5%',
          right: '5%',
          bottom: '5%',
          top: '5%',
          containLabel: true
        },
        xAxis: {
          show: false,
          type: 'category',
          boundaryGap: false,
          data: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'June', 'Aug', 'Sep']
        },
        yAxis: {
          type: 'value',
          show: false
        },
        series: [
          {
            showSymbol: false,
            smooth: true,
            data: [100, 230, 160, 630, 260, 300, 165, 598, 400],
            type: 'line',
            areaStyle: {
              color: new this.$echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                offset: 0,
                color: 'rgba(255, 199, 0, 0.41)'
              }, {
                offset: 1,
                color: 'rgba(249, 196, 81, 0)'
              }])
            },
            itemStyle: {
              color: '#FFC700',
              lineStyle: {
                color: '#FFC700'
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
 *{
        box-sizing: border-box;
    }
.all{
    padding: 20px 0;
    display: flex;
    flex-direction: column;
    align-items: center;
    border-radius: 10px;
    background: var(--ep-color-primary4);
    .f1{
        padding: 0 20px;
        width: 100%;
        .f1_1{
        display: flex;
        justify-content: space-between;
        width: 100%;
        .left{
            display: flex;
            align-items: center;
            color: #fff;
            img{
              border-radius: 10px;
            }
            div{
                margin-left: 20px;
                display: flex;
                flex-direction: column;
                .left_top{
                   font-weight: 500;
                    font-size: 28px;
                    font-family: DIN-Medium, DIN;
                    font-weight: 500;
                    color: #FFFFFF;
                    letter-spacing: 2px;
                }

                .left_boom{
                  font-size: 16px;
                  font-family: PingFangSC-Medium, PingFang SC;
                  font-weight: 500;
                  color: #FFFFFF;
                  letter-spacing: 4px;
                }
            }
            img{
              width: 66px;
              height: 64px;
              background: linear-gradient(180deg, #FFFFFF 0%, #D8E5FF 100%);
              box-shadow: 0px 4px 0px 0px #f4f6ff;
              border-radius: 16px;
            }
        }

        .right{
          img{
            margin-top: 8px;
          }
        }
    }
    }
    .f2{
        width: 100%;
        // padding: 0 -40px;
        margin-top: 20px;
        height: 450px;
    }
    .f3{
        color: #1762F2;
        background-color: var(--ep-color-background2);
        border-radius: 20px;
        width: 80%;
        text-align: center;
        padding: 10px 0;
        font-size: 14px;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        letter-spacing: 3px;
        margin-bottom: 30px;
        span{
        background-color: #fff;
        border-radius: 20px;
        width: 80%;
        padding: 10px 40px;
        }
    }
}
</style>
