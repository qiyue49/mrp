<template>
  <div class="app">
    <div class="card">
      <div class="title">{{ title }}</div>
      <div class="content">
        <div class="left-img">
          <img src="~@/assets/img/login/login4/left-img.png" alt="" />
        </div>
        <div class="right-content">
          <h2 class="right-title">
            登陆LOGIN
          </h2>
          <div class="input">
            <el-form
              ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" autocomplete="on"
              label-position="left">
              <el-form-item prop="username">
                <p>用户名</p>
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
              <el-tooltip v-model="capsTooltip" content="大写键已打开" placement="left" manual>
                <el-form-item prop="password">
                  密码
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
                  />
                  <span class="show-pwd" @click="showPwd">
                    <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
                  </span>
                </el-form-item>
              </el-tooltip>
              <el-form-item v-if="errorTime >= 3" prop="identify">
                验证码
                <el-row>
                  <el-col :span="18">
                    <el-input
                      ref="username"
                      v-model="loginForm.identify"
                      prefix-icon="Postcard"
                      :placeholder="验证码"
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
    </div>
  </div>
</template>
<script>
import { mixin } from '@/views/login/mixin'

export default {
  name: 'Login3',
  mixins: [mixin]
}

</script>
<style lang="scss" scoped>
.app {
  height: 100%;
  background: url("@/assets/img/login/login4/operations-bgc.png") no-repeat;
  background-position: top center;
  background-size: 100% 100%;
}
.card {
  position: relative;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 1000px;
  height: 700px;
  background-color: #fff;
  border-radius: 30px;
  .title {
    height: 100px;
    font-size: 40px;
    color: #34a1ff;
    display: flex;
    align-items: center;
    margin-left: 70px;
    font-weight: 600;
  }
  .content {
    display: flex;
  }
  .left-img {
    width: 640px;
    height: 450px;
    img {
      width: 100%;
      height: 100%;
    }
  }
  .right-content {
    width: 460px;
    padding-top: 60px;
    .right-title {
      font-size: 25px;
      text-align: center;
    }
    .input {
      margin: 0 25px;
      .el-button {
        margin-top: 50px;
        width: 100%;
      }
    }
  }
  .show-pwd {
    position: absolute;
    right: 10px;
    font-size: 16px;
    color: #889aa4;
    cursor: pointer;
    user-select: none;
  }
}
</style>
