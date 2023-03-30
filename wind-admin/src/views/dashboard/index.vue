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
      <div class="top_right" style="marginLeft:20px">
        <div class="title">
          <span class="tit">早上好欢迎进入飞廉系统</span>
          <div>晴</div>
          <div class="time">
            <span>2月16   星期四  </span>
            <div class="times">
              <span class="timess">23</span>
              <span>℃</span>
              </div>
          </div>
        </div>
        <calen-dar class="calen-dar"/>
        <div class="lists">
          <div v-for="item in 10" :key="item" class="list">
            <img src="../../assets/img/OIP-C.jpg" alt=""/>
            <div class="con">
              <div>
                <span>Jel Chibuzo</span>
                <span class="tits">呼吸内科</span>
              </div>
              <div style="color:#999BAF">擅长看呼吸系统疾病，特别是下呼吸道疾病以</div>
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
  // overflow: hidden;
  margin-top: -20px;
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
      height: 53%;
    }
  }
  .top_right{
    background-color: #fff;
    padding: 20px;
    width: 35%;
    border-radius: 10px;
    display: flex;
    flex-direction: column;
    align-items: center;
    .title{
      // background-color: #6C99FF;
      width: 100%;
      background-image: url("../../assets/img/lanbg.png");
  background-size: 100% 100%;
  background-repeat: no-repeat;
      padding: 20px;
      padding-bottom: 0;
      border-radius: 10px;
      color: #FFFFFF;
      .tit{
        font-size: 1.5rem;
      }
      .time{
        margin-top: -10px;
        width: 50%;
        display: flex;
        justify-content: space-between;
        align-items: center;
        .times{
          display: flex;
          align-items: center;
          .timess{
            font-size: 40px;
            // font-weight: 500;
          }
        }
      }
    }
    .calen-dar{
      margin: 20px;
      margin-top: 0;
    }
    .lists{
      width: 100%;
      overflow: hidden;
      // height: 50%;
      flex: 1;
      .list{
      display: flex;
      justify-content: space-between;
      height: 60px;
      margin-top: 10px;
      overflow: auto;
      align-items: center;
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
      .yu{
        background-color: #1762F2;
        border-radius: 20px;
        padding: 0 20px;
        height: 30px;
        line-height: 30px;
        color: #fff;
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
