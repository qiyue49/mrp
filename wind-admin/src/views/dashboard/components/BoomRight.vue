<template>
  <div class="all">
    <div class="f1">
      <span>
        <img src="../../../assets/img/lanquan.svg" alt=""/>
        2023年02月</span>
      <span>
        <img src="../../../assets/img/genglan.png" alt=""/>
      </span>
    </div>
    <div class="f2">飞廉系统开发进度</div>
    <el-progress :percentage="50" color="#C24229"/>
    <div class="f3">
      <span>参与者：</span>
      <div>
        <img v-for="item in 6" :key="item" src="../../../assets/img/avatar.png" alt=""/>
      </div>
    </div>
    <div class="f4">
      <div>邀请参与者</div>
    </div>
  </div>
</template>

<script>
export default {

}
</script>

<style lang="scss" scoped>
.all{
    background-color: #fff;
    margin-left: 20px;
    padding: 20px;
    border-radius: 10px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    .f1{
      display: flex;
      width: 100%;
      justify-content: space-between;
    }
    .f2{
      margin-top: 20px;
      text-align: center;
      font-weight: 900;
      font-size: 16px;
    }
    .el-progress{
      margin-top: 20px;
    }
    .f3{
      display: flex;
      align-items: center;
      margin-top: 20px;
      span{
        // flex: 1;
        width: 60px;
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