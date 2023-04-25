<template>
  <div class="contain">
    <el-row>
      <el-col :span="10">
        <el-form
          ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" autocomplete="on"
          label-position="left">

          <div class="card">
            <p class="title">{{ title }}</p>
            <div class="input">
              <el-form-item prop="username">
                <el-row>
                  <el-col :span="18">
                    <el-input
                      ref="username"
                      v-model="loginForm.username"
                      prefix-icon="User"
                      placeholder="手机号/邮箱/昵称"
                      name="username"
                      type="text"
                      tabindex="1"
                      autocomplete="on"
                      size="large"
                    />
                  </el-col>
                </el-row>
              </el-form-item>
            </div>
            <div class="input">
              <el-tooltip :visible="capsTooltip" content="大写键已打开" placement="left" manual>
                <el-form-item prop="password">
                  <el-row>
                    <el-col :span="18">
                      <el-input
                        :key="passwordType"
                        ref="password"
                        v-model="loginForm.password"
                        :type="passwordType"
                        placeholder="请输入您的密码"
                        prefix-icon="Lock"
                        name="password"
                        tabindex="2"
                        autocomplete="on"
                        size="large"
                        @keyup="checkCapslock"
                        @blur="capsTooltip = false"
                        @keyup.enter="handleLogin"
                      >
                        <template #suffix>
                          <svg-icon :icon-class="passwordType === 'password' ? 'ui-eye' : 'ui-hide'" color="gray" @click="showPwd"/>
                        </template>
                      </el-input>
                    </el-col>
                  </el-row>
                </el-form-item>
              </el-tooltip>

            </div>

            <div v-if="errorTime >= 3" class="input">
              <el-form-item prop="identify">
                <el-row>
                  <el-col :span="12">
                    <el-input
                      ref="identify"
                      v-model="loginForm.identify"
                      prefix-icon="Postcard"
                      placeholder="请输入验证码"
                      name="identify"
                      type="text"
                      size="large"
                      tabindex="3" />
                  </el-col>
                  <el-col :span="6">
                    <indentify ref="identify" :identify-code="identifyCode" :content-width="80" @click="makeCode" />
                  </el-col>
                </el-row>
              </el-form-item>
            </div>
            <div>
              <el-form-item>
                <el-row>
                  <el-col :span="18">
                    <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:5px;" size="large" @click="handleLogin">
                      登陆
                    </el-button>
                  </el-col>
                </el-row>
              </el-form-item>
            </div>
          </div>
        </el-form>
      </el-col>
      <!-- <el-col :span="18">
        <div class="app"></div>
      </el-col> -->
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

  .contain{
    background-image: url("@/assets/img/login/login1/login_pic.png");
    width: 100%;
    height: 100%;
    background-size: 100% 100%;
    overflow: hidden;
  }

  .login-form {
    padding: 20px;
    max-width: 100%;
    margin: 20% 0 20% 20%;
    overflow: hidden;
    box-sizing: border-box;
  }

  .card {
    .title {
      font-size: 48px;
      font-family: Microsoft YaHei;
      font-weight: bold;
      color: #B74DD7;
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
      background: linear-gradient(90deg, #9224F0 0%, #B749AB 63%, #DC829E 100%);
      border-radius: 10px 10px 10px 10px;
      border-color: unset;
    }
    .el-button>sapn{
      font-size: 20px;
      font-family: Microsoft YaHei;
      font-weight: 400;
      color: #FEFEFE;
    }

    .input {

      p {
        font-size: 16px;
        color: #bbb7b7;
      }

    }
  }
  </style>
