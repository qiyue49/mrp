<template>
  <div class="login-box">
    <el-row style="height:100%">
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
                      placeholder="请输入管理员姓名"
                      name="username"
                      prefix-icon="User"
                      type="text"
                      size="large"
                      tabindex="1"
                      autocomplete="on"
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
                        prefix-icon="Lock"
                        placeholder="请输入密码"
                        name="password"
                        tabindex="2"
                        size="large"
                        autocomplete="on"
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
                  <el-col :span="14">
                    <el-input
                      ref="identify"
                      v-model="loginForm.identify"
                      prefix-icon="Postcard"
                      placeholder="验证码"
                      name="identify"
                      size="large"
                      type="text"
                      tabindex="3" />
                  </el-col>
                  <el-col :span="6">
                    <indentify ref="identify" :identify-code="identifyCode" :content-width="80" @click="makeCode" />
                  </el-col>
                </el-row>
              </el-form-item>
            </div>

            <div>
              <el-form-item prop="username">
                <el-row>
                  <el-col :span="6">
                    <el-button :loading="loading" color="#233A97" style="width:100%;margin-bottom:30px;" @click="handleLogin">
                      登录
                    </el-button>
                  </el-col>
                </el-row>
              </el-form-item>
            </div>
          </div>
        </el-form>
      </el-col>
      <el-col :span="14" style="padding:100px">
        <div class="app">
        </div>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import { mixin } from './mixin'

export default {
  name: 'Login3',
  mixins: [mixin]
}
</script>
  <style lang="scss" scoped>

  .login-box{
    height: 100%;
    width: 100%;
    overflow: hidden;
    .app {
      min-width: 100%;
      background-image: url('../../assets/img/login/login3/login_pic.png');
      height: 100%;
      background-repeat: no-repeat;
      background-size: 100% 100%;
    }
  }

  .login-form {
    padding: 20px;
    max-width: 100%;
    margin: calc((100vh - 464px) / 2) 0 20% 20%;
    overflow: hidden;
  }

  .card {
    .title {
        font-size: 44px;
        font-family: SourceHanSansCN;
        font-weight: bold;
        color: #333333;
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

       border-radius: 25px;
    }

    .input {
        margin-bottom: 36px;

      p {
        font-size: 16px;
        color: #bbb7b7;
      }

      :deep(.el-input__wrapper){
        border-radius: 30px;
        background: #F6F2F9;
      }

    }
  }
  </style>
