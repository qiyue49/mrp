<template>
  <div ref="view">
    <div class="top">
      <div class="top_left">
        <div class="top_left_top">
          <chart-1 style="width:30%;height:100%"/>
          <chart-2 style="marginLeft:20px;width:30%;height:100%"/>
          <chart-3 style="marginLeft:20px;width:30%;height:100%"/>
        </div>
        <div class="top_left_boom">
          <chart-4 style="width:100%"/>
        </div>
      </div>
      <div class="top_right" style="marginLeft:20px">
        <div class="title">
          <h3>早上好欢迎进入飞廉系统</h3>
          <div>晴</div>
          <div>2月16   星期四  23℃</div>
        </div>
        <!-- <calen-dar/> -->
        <div class="lists">
          <div v-for="item in 10" :key="item" class="list">
            <img src="../../assets/img/avatar.png" alt=""/>
            <div class="con">
              <div>
                <span>Jel Chibuzo</span>
                <span class="tits">呼吸内科</span>
              </div>
              <div>擅长看呼吸系统疾病，特别是下呼吸道疾病以</div>
            </div>
            <span class="yu">+预约</span>
          </div>
        </div>
      </div>
    </div>
    <div class="boom">
      <boom-left style="width:20%"/>
      <boom-medd style="width:50%"/>
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
.top{
  display: flex;
  height: 60vh;
  // overflow: hidden;
  .top_left{
    width: 60%;
    .top_left_top{
      display: flex;
      height: 50%;
    }
    .top_left_boom{
      width: 100%;
      height: 50%;
    }
  }
  .top_right{
    background-color: #fff;
    padding: 20px;
    width: 35%;
    .title{
      background-color: #6C99FF;
      padding: 20px;
    }
    .lists{
      overflow: auto;
      height: 50%;
      .list{
      display: flex;
      justify-content: space-between;
      height: 50px;
      margin-top: 10px;
      overflow: auto;
      img{
        // width: 10%;
        height: 100%;
        border-radius: 50%;
      }
      .con{
        .tits{
          border: 1px solid #C24229;
          color: #C24229;
          background-color: rgba(194, 66, 41, 0.05);
          padding: 5px;
          margin-left: 20px;
        }
      }
      .yu{
        background-color: #0243A3;
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
}
</style>
