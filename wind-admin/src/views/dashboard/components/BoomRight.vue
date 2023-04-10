<template>
  <div class="all">
    <div class="f1">
      <span>
        <img src="../../../assets/img/dashboard/ring.svg" alt=""/>
        2023年02月</span>
      <span>
        <img src="../../../assets/img/dashboard/moreBule.png" alt=""/>
      </span>
    </div>
    <div class="f2">飞 廉 系 统 开 发 进 度</div>
    <el-progress :percentage="50" color="#C24229"/>
    <div class="f3">
      <span>参与者：</span>
      <div>
        <img v-for="item in 6" :key="item" src="../../../assets/img/dashboard/avatar.png" alt=""/>
      </div>
    </div>
    <div class="f4">
      <div>邀 请 参 与 者</div>
    </div>
  </div>
</template>

<script>
export default {

}
</script>

<style lang="scss" scoped>
.all{
    margin-left: 20px;
    padding: 20px;
    border-radius: 10px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    background:var(--ep-color-primary2) ;
    .f1{
      display: flex;
      width: 100%;
      justify-content: space-between;
      align-items: center;

      span{
        font-size: 16px;
        font-family: DIN-Bold, DIN;
        font-weight: bold;
        color: #252631;
        letter-spacing: 2px;
        img{
          margin-right: 8px;
        }
      }
    }
    .f2{
      text-align: center;
      font-size: 20px;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #252631;
      letter-spacing: 5px;
      margin-top: 15px;
    }
    .el-progress{
      margin-top: 41px;
    }
    .f3{
      display: flex;
      align-items: center;
      margin-top: 20px;
      span{
        // flex: 1;
        width: 80px;
        font-size: 14px;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        color: #252631;
        letter-spacing: 2px;
      }
     div{
      display: flex;
      width: 225px;
      overflow: hidden;
       img{
width: 50px;
                height: 50px;
      }
      img:nth-child(n+2){
                margin-left: -15px;
                // justify-content: flex-start;
            }
     }
    }
    .f4{
      text-align: center;
      margin-top: 20px;
      width: 100%;
      display: flex;
      justify-content: center;
      div{
        background-color: #1762F2;
        border-radius: 20px;
        color: #fff;
        padding: 10px 0;
        width: 80%;
      }
    }
}
</style>