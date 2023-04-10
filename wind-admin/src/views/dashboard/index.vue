<template>
  <div ref="view">
    <div class="top">
      <div class="top_left">
        <div class="top_left_top">
          <chart-1 style="width:32%;height:100%"/>
          <chart-2 style="marginLeft:20px;width:32%;height:100%"/>
          <chart-3 style="marginLeft:20px;width:32%;height:100%"/>
        </div>
        <div class="top_left_boom">
          <chart-4 style="width:100%"/>
        </div>
      </div>
      <div class="top_right">
        <div class="title">
          <span class="tit">早上好，欢迎进入飞廉系统</span>
          <div class="weather">晴</div>
          <div class="time">
            <div class="text">
              <span>2月16</span>
              <span>星期四</span>
            </div>
            <div class="times">
              <span class="timess">23</span>
              <span class="temp">℃</span>
            </div>
          </div>
        </div>
        <calen-dar class="calen-dar"/>
        <div class="lists">
          <div v-for="item in 6" :key="item" class="list">
            <img src="../../assets/img/dashboard/doctor.jpg" alt=""/>
            <div class="con">
              <div>
                <span>Jel Chibuzo</span>
                <span class="tits">呼吸内科</span>
              </div>
              <div class="show-text" style="color:#999BAF">擅长看呼吸系统疾病，特别是下呼吸道疾病以擅长看呼吸系统疾病</div>
            </div>
            <span class="yu">+预约</span>
          </div>
        </div>
      </div>
    </div>
    <div class="boom">
      <boom-left style="width:40%"/>
      <boom-medd style="width:40%"/>
      <boom-right/>
    </div>
  </div>

</template>
<script>
import { useFullscreen } from '@vueuse/core'
import chart1 from './components/chart1.vue'
import chart2 from './components/chart2.vue'
import Chart3 from './components/chart3.vue'
import Chart4 from './components/chart4.vue'
import CalenDar from './components/CalenDar.vue'
import BoomLeft from './components/BoomLeft.vue'
import BoomMedd from './components/BoomMedd.vue'
import BoomRight from './components/BoomRight.vue'
export default {
  name: 'DashBoard',
  components: { chart1, chart2, Chart3, Chart4, CalenDar, BoomLeft, BoomMedd, BoomRight },
  mounted() {
    this.$store.settingStore.setFun(this.toggleFullScreen)
  },
  methods: {
    toggleFullScreen() {
      const { toggle } = useFullscreen(this.$refs.view)
      toggle()
    }
  }

}
</script>
<style lang="scss" scoped>
*{
  box-sizing: border-box;
}
.top{
  display: flex;
  height: 80vh;
  .top_left{
    width: 65%;
    .top_left_top{
      display: flex;
      justify-content: space-between;
      height: 45%;
    }
    .top_left_boom{
      // width: 100%;
      margin-top: 2%;
      height: 52%;
    }
  }
  .top_right{
    // padding: 20px;
    background:var(--ep-color-background2) ;
    padding: 30px 29px 31px 25px;
    margin-left: 20px;
    width: 35%;
    border-radius: 10px;
    display: flex;
    flex-direction: column;
    align-items: center;
    box-sizing: border-box;
    .title{
      // background-color: #6C99FF;
      width: 100%;
      background-image: url("../../assets/img/dashboard/weatherBackground.png");
      background-size: 100% 100%;
      background-repeat: no-repeat;
      box-sizing: border-box;
      padding-top: 22px;
      padding-left: 21px;
      padding-bottom: 10px;
      border-radius: 10px;
      color: #FFFFFF;
      margin-left: 6px;
      .tit{
        font-size: 24px;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        color: #FFFFFF;
        letter-spacing: 4px;
      }

      .weather{
        font-size: 18px;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        color: #FFFFFF;
        letter-spacing: 3px;
      }
      .time{
        margin-top: -10px;
        width: 50%;
        display: flex;
        justify-content: space-between;
        align-items: center;
        .text{
          font-size: 16px;
          font-family: PingFangSC-Semibold, PingFang SC;
          font-weight: 600;
          color: #FFFFFF;
          letter-spacing: 2px;
          span{
            &:first-child{
              display: inline-block;
              margin-right: 16px;
            }

          }
        }
        .times{
          display: flex;
          align-items: center;
          .timess{
            font-size: 48px;
            font-family: DINAlternate-Bold, DINAlternate;
            font-weight: 600;
            color: #FFFFFF;
            letter-spacing: 4px;
          }
          .temp{
            font-size: 28px;
            font-family: DINAlternate-Bold, DINAlternate;
            font-weight: bold;
            color: #FFFFFF;
            letter-spacing: 2px;
          }
        }
      }
    }
    .calen-dar{
      margin-top: 20px;
    }
    .lists{
      width: 100%;
      overflow: hidden;
      // height: 50%;
      .list{
      display: flex;
      height: 60px;
      margin-top: 32px;
      overflow: auto;
      align-items: center;
      position: relative;
      // padding: 10px 0;
      img{
        // width: 10%;
        height: 100%;
        border-radius: 50%;
      }
      .con{
        display: flex;
        flex-direction: column;
        justify-content: space-around;
        margin-left: 15px;
        width: 62%;
        .tits{
          border: 1px solid #FFC700;
          color: #FFC700;
          background-color: rgba(255, 199, 0, 0.05);
          padding: 5px;
          font-size: 10px;
          margin-left: 20px;
          border-radius: 3px;
        }
      }
      .show-text{
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        margin-top: 9px;
      }
      .yu{
        background-color: #1762F2;
        border-radius: 20px;
        padding: 0 20px;
        height: 30px;
        line-height: 30px;
        color: #fff;
        position:absolute;
        right: 5px;
        box-sizing: border-box;
      }
    }
    }
  }
}
.boom{
  display: flex;
  width: 100%;
  margin-top: 20px;
  // height: 20vh;
}
</style>
