<template>
  <div class="all">
    <div class="left">
      <div class="f1">
        <img src="../../../assets/img/bz1.png" alt=""/>
        <div>
          <span>
            <span style="color:#1762F2;font-size:18px;font-weight: 500;">620</span>
            <span class="price">单</span></span>
          <span class="common">订单总数</span>
        </div>
      </div>
      <div class="f1">
        <img src="../../../assets/img/bz2.png" alt=""/>
        <div>
          <span>
            <span style="color:#D34D25;font-size:18px;font-weight: 500;">620</span>
            <span class="price">单</span></span>
          <span class="common">销售总量</span>
        </div>
      </div>
      <div class="f1">
        <img src="../../../assets/img/bz3.png" alt=""/>
        <div>
          <span>
            <span style="color:#FFC700;font-size:18px;font-weight: 500;">620</span>
            <span class="price">单</span></span>
          <span class="common">总销售额</span>
        </div>
      </div>
    </div>
    <div class="right">
      <div ref="$chart1" style="width: 100%;height: 100%">
      </div>
    </div>
  </div></template>
<script>
import bz11 from '../../../assets/img/bz11.png'
import bz22 from '../../../assets/img/bz22.png'
import bz33 from '../../../assets/img/bz33.png'
export default {
  name: 'Chart1',
  data() {
    return {
      rankIconsSize: 25,
      data: {
        first: '../../../assets/img/bz3.png',
        second: '../../../assets/img/bz3.png',
        third: '../../../assets/img/bz3.png',
        fourth: '../../../assets/img/bz3.png',
        fifth: '../../../assets/img/bz3.png',
        sixth: '../../../assets/img/bz3.png',
        seventh: '../../../assets/img/bz3.png',
        eighth: '../../../assets/img/bz3.png'
      },
      dataTitle: 'rrrr',
      data1: [
        '第一名第一名第一名第一名',
        '第二名第二名',
        '第三名',
        '第四名',
        '第五名',
        '第六名',
        '第七名',
        '第八名'
      ]
    }
  },
  created() {
  },
  mounted() {
    this.barGraph()
  },
  methods: {
    barGraph() {
      const myChart = this.$echarts.init(this.$refs.$chart1)
      const cate = [
        '女装',
        '百货',
        '美妆',
        '奢品',
        '电器',
        '运动'
      ]
      const barData = [
        {
          value: 900,
          itemStyle: {
            color: '#D34D25'
          }
        },
        801, 900, 125, 160, 411]
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        // 图表位置
        grid: {
          left: '-10%',
          right: '10%',
          bottom: '3%',
          top: '3%',
          containLabel: true
        },
        // X轴
        xAxis: {
          type: 'value',
          axisLine: {
            show: false
          },
          axisTick: {
            show: false
          },
          // 不显示X轴刻度线和数字
          splitLine: { show: false },
          axisLabel: { show: false }
        },
        yAxis: {
          type: 'category',
          data: cate,
          // 升序
          inverse: true,
          splitLine: { show: false },
          axisLine: {
            show: false
          },
          axisLabel: {
            margin: 70, // 距离右侧图形距离，配合axisLabel.left 和 grid.left 使用

            // formatter必须，配合rich使用
            formatter: (params, index) => {
              return [`{a${index + 1}|}    ${params}`].join('\n')
            },
            align: 'left', // 文字左排序
            rich: {
              a1: {
                backgroundColor: { image: bz11 },
                width: this.rankIconsSize,
                height: this.rankIconsSize
              },
              a2: {
                backgroundColor: { image: bz22 },
                width: this.rankIconsSize,
                height: this.rankIconsSize
              },
              a3: {
                backgroundColor: { image: bz33 },
                width: this.rankIconsSize,
                height: this.rankIconsSize
              },
              a4: {
                backgroundColor: { image: bz33 },
                width: this.rankIconsSize,
                height: this.rankIconsSize
              },
              a5: {
                backgroundColor: { image: bz33 },
                width: this.rankIconsSize,
                height: this.rankIconsSize
              },
              a6: {
                backgroundColor: { image: bz33 },
                width: this.rankIconsSize,
                height: this.rankIconsSize
              }
            }
          },
          axisTick: {
            show: false
          },
          // key和图间距
          offset: 10,
          // 动画部分
          animationDuration: 300,
          animationDurationUpdate: 300,
          // key文字大小
          nameTextStyle: {
            fontSize: 5
          }
        },
        series: [
          {
            // 柱状图自动排序，排序自动让Y轴名字跟着数据动
            realtimeSort: true,
            name: '数量',
            type: 'bar',
            data: barData,
            barWidth: 10,
            barCategoryGap: '42%',
            // barGap: 100,
            smooth: true,
            valueAnimation: true,
            // Y轴数字显示部分
            label: {
              normal: {
                show: true,
                position: 'right',
                valueAnimation: true,
                offset: [5, -2],
                textStyle: {
                  color: '#333',
                  fontSize: 13
                }
              }
            },
            itemStyle: {
              emphasis: {
                barBorderRadius: 7
              },
              // 颜色样式部分
              normal: {
                barBorderRadius: 7,
                color: '#1762F2'
              }
            }
          }
        ],
        // 动画部分

        animationDuration: 0,
        animationDurationUpdate: 3000,
        animationEasing: 'linear',
        animationEasingUpdate: 'linear'
      }
      myChart.setOption(option)
      // 图表大小变动从新渲染，动态自适应
      window.addEventListener('resize', function () {
        myChart.resize()
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
    background-color: #fff;
    padding: 20px;
    display: flex;
    border-radius: 10px;
    .left{
        .f1{

          &:first-child{
            margin-top: 0;
          }
          padding: 0 10px;
          display: flex;
          background-color: #F3F5F8;
          margin-top: 20px;
          border-radius: 10px;
          align-items: center;
          height: 27%;
            img{
                width: 50px;
                height: 50px;
            }
            div{
                padding: 10px;
                display: flex;
                flex-direction: column;
            }
            .common{
              font-size: 12px;
              font-family: PingFangSC-Medium, PingFang SC;
              font-weight: 500;
              color: #252631;
              letter-spacing: 2px;
            }

            .price{
              font-size: 10px;
              font-family: PingFangSC-Semibold, PingFang SC;
              font-weight: 600;
              color: #252631;
              letter-spacing: 1px;
            }
        }
    }
    .right{
      flex: 1;
      margin: 0 20px;
    }
}
</style>