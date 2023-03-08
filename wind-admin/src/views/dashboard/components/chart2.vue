<template>
  <div class="all">
    <div class="f1">
      <div class="f1_1">
        <div class="left">
          <img src="../../../assets/img/avatar.png" alt=""/>
          <div>
            <span class="left_top">1568</span>
            <span class="left_boom">访问次数</span>
          </div>
        </div>
        <div class="right">...</div>
      </div>
    </div>
    <div class="f2">
      <div ref="$chart1" style="width: 100%;height: 100%">
      </div>
    </div>
    <div class="f3">
      <span>查看数据详情</span>
    </div>
  </div></template>

<script>
export default {
  name: 'Chart1',
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
          show: false // 不显示坐标轴线、坐标轴刻度线和坐标轴上的文字
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
                color: 'rgba(245, 194, 101, 0.41)'
              }, {
                offset: 1,
                color: 'rgba(245, 194, 101, 0.02)'
              }])
            },
            itemStyle: {
              normal: {
                color: '#F5C265', // 改变折线点的颜色
                lineStyle: {
                  color: '#F5C265' // 改变折线颜色
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
 *{
        box-sizing: border-box;
    }
.all{
    background-color: #C24229;
    padding: 20px 0;
    display: flex;
    flex-direction: column;
    align-items: center;
    border-radius: 20px;
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
            div{
                margin-left: 20px;
                display: flex;
                flex-direction: column;
                .left_top{
                    font-size: 1.5rem;
                    font-weight: 500;
                }
            }
            img{
                width: 50px;
            }
        }
    }
    }
    .f2{
        width: 100%;
        padding: 0 -40px;
        canvas{
            height: 500px;
        }
    }
    .f3{
        color: #C24229;
        padding: 0 30px;
        text-align: center;
        span{
        background-color: #fff;
        border-radius: 20px;
        padding: 10px 40px;
        }
    }
}
</style>