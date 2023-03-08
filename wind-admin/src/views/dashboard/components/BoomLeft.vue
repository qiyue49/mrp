<template>
  <div class="all">
    <div class="left">
      <div class="f1">
        <img src="../../../assets/img/avatar.png" alt=""/>
        <div>
          <span>620单</span>
          <span>订单总数</span>
        </div>
      </div>
      <div class="f1">
        <img src="../../../assets/img/avatar.png" alt=""/>
        <div>
          <span>620单</span>
          <span>销售总量</span>
        </div>
      </div>
      <div class="f1">
        <img src="../../../assets/img/avatar.png" alt=""/>
        <div>
          <span>620单</span>
          <span>总销售额</span>
        </div>
      </div>
    </div>
    <div class="right">
      <div ref="$chart1" style="width: 100%;height: 100%">
      </div>
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
        xAxis: {
          max: 'dataMax'
        },
        yAxis: {
          type: 'category',
          data: ['A', 'B', 'C', 'D', 'E'],
          inverse: true,
          animationDuration: 300,
          animationDurationUpdate: 300,
          max: 2 // only the largest 3 bars will be displayed
        },
        series: [
          {
            realtimeSort: true,
            name: 'X',
            type: 'bar',
            data,
            label: {
              show: true,
              position: 'right',
              valueAnimation: true
            }
          }
        ],
        legend: {
          show: true
        },
        animationDuration: 0,
        animationDurationUpdate: 3000,
        animationEasing: 'linear',
        animationEasingUpdate: 'linear'
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
    .left{
        .f1{
            display: flex;
                background-color: #F3F5F8;
                margin-top: 10px;
                border-radius: 5px;
                align-items: center;
            img{
                width: 50px;
                height: 50px;
            }
            div{
                padding: 10px;
                display: flex;
                flex-direction: column;
            }
        }
    }
}
</style>