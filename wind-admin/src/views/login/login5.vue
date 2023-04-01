<template>
  <div class="all">
    <div class="left">
      <span class="left_f2">欢迎使用飞廉系统</span>
      <div class="left_f1">
        <img src="../../assets/img/login3.png" alt=""/>
        <div><img src="../../assets/img/login4.png" alt="" class="img1"/>
          <span>为中小企业提供便捷服务，提升企业影响力</span>
          <img src="../../assets/img/login4.png" alt="" class="img2"/>
        </div>
      </div>
      <img src="../../assets/img/login1.png" alt=""/>
    </div>
    <div class="right">
      <div class="right_con">
        <div class="tit">
          欢迎登录
        </div>
        <el-form
          ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" autocomplete="on"
          label-position="top">

          <el-form-item label="用户名" prop="username">
            <el-input
              ref="username"
              v-model="loginForm.username"
              placeholder="请输入用户名"
              prefix-icon="User"
              name="username"
              type="text"
              tabindex="1"
              autocomplete="on"
            />
          </el-form-item>
          <el-form-item label="密码" prop="password">

            <el-tooltip v-model="capsTooltip" content="大写键已打开" placement="left" manual>
              <el-input
                :key="passwordType"
                ref="password"
                v-model="loginForm.password"
                :type="passwordType"
                placeholder="请输入密码"
                prefix-icon="Lock"
                name="password"
                tabindex="2"
                autocomplete="on"
                @keyup="checkCapslock"
                @blur="capsTooltip = false"
                @keyup.enter="handleLogin"
              >
                <template #suffix>
                  <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" @click="showPwd"/>
                </template>
              </el-input>
            </el-tooltip>

          </el-form-item>
          <el-form-item v-if="errorTime >= 3" label="验证码" prop="identify">

            <el-row>
              <el-col :span="18">
                <el-input
                  ref="username"
                  v-model="loginForm.identify"
                  prefix-icon="Postcard"
                  placeholder="验证码"
                  name="identify"
                  type="text"
                  tabindex="3" />
              </el-col>
              <el-col :span="6">
                <indentify ref="identify" :identify-code="identifyCode" :content-width="80" @click="makeCode" />
              </el-col>
            </el-row>
          </el-form-item>

          <el-button
            :loading="loading" type="primary" style="width:100%;margin-bottom:30px;"
            @click="handleLogin">
            登录
          </el-button>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import { mixin } from './mixin'
export default {
  name: 'Login5',
  mixins: [mixin]
}
</script>

<style lang="scss" scoped>
*{
  padding: 0;
  margin: 0;
}
.all{
  background-image: url(@/assets/img/login2.png);
  background-size: 100% 100%;
  background-repeat: no-repeat;
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  top: 0;
    display: flex;
    width: 100%;
    height: 100vh;
    .left{
    width:50%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-around;
    padding: 60px 0;
    .left_f1{
      position: relative;
      color: #FFC700;
      font-size: 26px;
      font-weight: 500;
      // .img1{
      //   position: absolute;
      //   left: 0;
      //   top: 40%;
      // }
      div{
        width: 100%;
        position: absolute;
        // left: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        // transform: translateX(50%);
        top: 30%;
        span{
          margin: 0 20px;
        }
        // transform: translateY(-50%);
      }
    }
    .left_f2{
      font-size: 52px;
      font-weight: 900;
      color: #fff;
    }
    img{
      // width: 100%;
      // height: 100vh;
    }
}
.medd{
  width: 10px;
  height: 100%;
  background-color: #C24229;
}

  .right {
    width: 50%;
    // background-color: #0243A3;
    // padding: 200px;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;

    .right_con {
      background-color: #fff;
      padding: 40px;
      border-radius: 30px;
      display: flex;
      flex-direction: column;
      align-items: center;

      .tit {
        font-size: 32px;
        font-weight: 500;
      }

      .el-form {

        .el-form-item {
          p {
            font-weight: 900;
            margin: 20px 0;
          }

          .el-form-item__content {
            .el-input {
              .el-input__wrapper {
                border-radius: 0;
              }

              border-radius: 30px !important;
            }
          }
        }
      }
    }
  }
}
</style>
