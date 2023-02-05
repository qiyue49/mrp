<template>
  <div>
    <el-row>
      <el-col :span="6">
        <el-form
          ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" autocomplete="on"
          label-position="left">

          <div class="card">
            <p class="title">{{ title }}</p>
            <div class="input">
              <p>用户名</p>
              <el-form-item prop="username">
                <el-input
                  ref="username"
                  v-model="loginForm.username"
                  prefix-icon="User"
                  placeholder="请输入用户名"
                  name="username"
                  type="text"
                  tabindex="1"
                  autocomplete="on"
                />
              </el-form-item>
            </div>
            <div class="input">
              <p>登陆密码</p>
              <el-tooltip :visible="capsTooltip" content="大写键已打开" placement="left" manual>
                <el-form-item prop="password">
                  <el-input
                    :key="passwordType"
                    ref="password"
                    v-model="loginForm.password"
                    prefix-icon="Lock"
                    :type="passwordType"
                    placeholder="请输入密码"
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

            </div>

            <div v-if="errorTime >= 3" class="input">
              <p>验证码</p>
              <el-form-item prop="identify">
                <el-row>
                  <el-col :span="18">
                    <el-input
                      ref="identify"
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
            </div>

            <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;" @click="handleLogin">
              登录
            </el-button>

          </div>
        </el-form>
      </el-col>
      <el-col :span="18">
        <div class="app"></div>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import { mixin } from './mixin'

export default {
  name: 'Login1',
  mixins: [mixin]
}
</script>
<style lang="scss" scoped>
.app {
  width: 75%;
  height: 100vh;
  background-position: top center;
  background-size: 100% 110%;
  position: absolute;
  background-image: url("@/assets/img/login/login1/login1.png");
  justify-content: center;
  align-items: center;

}

.login-form {
  padding: 20px;
  max-width: 100%;
  margin: calc((100vh - 464px) / 2) 0 20% 20%;
  overflow: hidden;
}

.card {
  .title {
    font-size: 38px;
    font-weight: 600;
    color: #fd6905;
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 0px;
    font-size: 16px;
    color: #889aa4;
    cursor: pointer;
    user-select: none;
  }

  .el-button {
    margin-top: 50px;
    width: 90%;
    background-color: #fd6e07;
    border-color: #fd6e07;
  }

  .input {

    p {
      font-size: 16px;
      color: #bbb7b7;
    }

  }
}
</style>
