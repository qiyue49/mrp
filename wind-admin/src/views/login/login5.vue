<template>
  <div class="all">
    <div class="all-box">
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
        <div class="right-con">
          <div class="tit">
            欢迎登录
          </div>
          <div class="line-box"><span class="line"></span></div>
          <el-form
            ref="loginForm" :rules="loginRules" class="login-form" autocomplete="on"
            label-position="top"
            :model="loginForm"
          >
            <el-form-item label="用户名" prop="username" class="label-box">
              <el-input
                ref="username"
                v-model="loginForm.username"
                placeholder="请输入用户名"
                prefix-icon="User"
                name="username"
                type="text"
                tabindex="1"
                autocomplete="on"
                class="input-box"
              />
            </el-form-item>
            <el-form-item label="密码" prop="password" class="label-box">

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
            <el-form-item label="验证码" prop="identify" class="label-box">

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
              :loading="loading" type="primary"
              @click="handleLogin">
              确认登录
            </el-button>
          </el-form>
        </div>
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
    .all-box{
      width: 1625px;
      margin: 0 auto;
      display: flex;
    }
    .left{
    width:50%;
    margin-top: 130px;
    text-align: center;
    // display: flex;
    // flex-direction: column;
    // align-items: center;
    // justify-content: space-around;
    // padding: 60px 0;
    .left_f1{
      position: relative;
      color: #FFC700;
      font-size: 26px;
      font-weight: 500;
      margin-top: 30px;
      margin-bottom: 58px;
      div{
        width: 100%;
        position: absolute;
        display: flex;
        align-items: center;
        justify-content: center;
        top: 30%;
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
    .right-con{
    width: 75%;
    margin-top: 137px;
    margin-left: 203px;
    height: auto;
    background-color: #fff;
    border-radius: 30px;
    padding: 46px 50px 100px 50px;
    box-sizing: border-box;
    .tit {
        font-size: 32px;
        font-weight: 500;
        text-align: center;
      }

      .line-box{
        display: flex;
        justify-content: center;
        margin-bottom: 40px;
        .line{
         margin-top: 9px;
         width: 46px;
         height: 10px;
         background: #1762F2;
         border-radius: 5px;
         display: block;
         }
      }

        .el-form--label-top .el-form-item :deep .el-form-item__label {
          color: #252631;
          font-size: 24px !important;
          font-weight: 600 !important;
          letter-spacing: 6px;
          margin-bottom: 21px;
          // margin-top: 45px;
        }

        :deep .el-icon svg {
          width: 20px;
          height: 23px;
        }

      .login-form {

        .label-box{
          margin-bottom: 45px;
        }

        :deep .el-button{
          width: 100%;
          height: 80px;
          background: #1762F2;
          box-shadow: 0px 0px 22px 0px rgb(23 98 242 / 21%);
          border-radius: 40px;
          font-size: 24px;
          font-family: PingFangSC-Semibold, PingFang SC;
          font-weight: 600;
          color: #FFFFFF;
          letter-spacing: 10px;
        }

        :deep .el-input__inner{
                font-size: 20px;
                font-family: PingFangSC-Medium, PingFang SC;
                font-weight: 500;
                color: #BAC4D5;
                line-height: 28px;
                letter-spacing: 5px;
        }

        .el-form-item__content{
          .el-input{
            height: 80px;
            :deep .el-input__wrapper{
              border-radius: 40px !important;
            }
          }
        }

      }
    }

  }
}
</style>
