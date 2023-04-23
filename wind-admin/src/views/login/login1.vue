<template>
  <div class="all">
    <div class="left-text">
      <p>欢迎您</p>
      <p>—设计让生活更美好</p>
    </div>
    <div class="right-text">
      <div class="title">LOGO</div>
      <div style="margin-left:20px">
        <el-form ref="loginForm" :model="loginForm" :rules="loginRules" autocomplete="on">

          <el-form-item prop="username">
            <el-row>
              <el-col :span="20">
                <el-input
                  ref="username"
                  v-model="loginForm.username"
                  placeholder="请输入手机号码"
                  name="username"
                  type="text"
                  tabindex="1"
                  autocomplete="on"
                  size="large"
                />
              </el-col>
            </el-row>
          </el-form-item>
          <el-form-item prop="username">
            <el-row>
              <el-col :span="20">
                <el-input
                  ref="password"
                  v-model="loginForm.password"
                  placeholder="请输入动态码"
                  name="password"
                  type="text"
                  tabindex="1"
                  autocomplete="on"
                  size="large"
                >
                  <template #append>获取动态码</template>
                </el-input>
              </el-col>
            </el-row>
          </el-form-item>
          <el-form-item>
            <el-row>
              <el-col :span="20">
                <drag-verify/>
              </el-col>
            </el-row>
          </el-form-item>
          <el-form-item>
            <el-row>
              <el-col :span="20">
                <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:5px;" size="large" @click="handleLogin">
                  登陆
                </el-button>
              </el-col>
            </el-row>
          </el-form-item>
        </el-form>
      </div>

    </div>
  </div>
</template>
<script>
import { mixin } from '@/views/login/mixin'
import DragVerify from './dragVerify.vue'

export default {
  name: 'Login3',
  components: { DragVerify },
  mixins: [mixin]
}
</script>
<style lang="scss" scoped>
.all {
  width: 100%;
  height: 100%;
  background-image: url("@/assets/img/login/login4/login-pic.png");
  background-size: 70% 100%;
  background-repeat: no-repeat;
  display: flex;
  position: relative;

  .left-text{
    position:absolute;
    left: 20%;
    top: 30%;
    p{
      font-family: AlibabaPuHuiTiB;
      font-weight: 400;
      color: #FFFFFF;
      font-size: 32px;
      margin: 0;
      &:first-child{
        font-weight: 600;
        font-size: 50px;
        margin-bottom: 10px;
      }
    }
  }

  .right-text{
    position: absolute;
    right: 0;
    width: 30%;
    top: 20%;
    .title{
      text-align: center;
      font-size: 60px;
      font-weight: 500;
      font-family: SourceHanSansCN;
      color: #333333;
      margin-bottom: 100px;
    }

    :deep(.el-form){
      margin-left: 90px !important;
    }

    .el-button{
      background: #F4A122;
      border-radius: 6px;
      border: unset;
    }
  }
}
</style>
