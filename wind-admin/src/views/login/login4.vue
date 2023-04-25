<template>
  <div class="all">
    <div class="left-text">
      <p>{{ title }}</p>
      <p>为中小企业提供便捷服务，提升企业影响力</p>
    </div>
    <div class="right-text">
      <div class="title">登录</div>
      <div style="margin-left:20px">
        <el-form ref="loginForm" :model="loginForm" :rules="loginRules" autocomplete="on">

          <el-form-item prop="username">
            <el-row>
              <el-col :span="20">
                <el-input
                  ref="username"
                  v-model="loginForm.username"
                  prefix-icon="User"
                  placeholder="请输入用户名"
                  name="username"
                  type="text"
                  tabindex="1"
                  autocomplete="on"
                  size="large"
                />
              </el-col>
            </el-row>
          </el-form-item>
          <el-form-item prop="password">
            <el-row>
              <el-col :span="20">
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
                    >
                      <template #suffix>
                        <svg-icon :icon-class="passwordType === 'password' ? 'ui-eye' : 'ui-hide'" color="gray" @click="showPwd"/>
                      </template>
                    </el-input>
                  </el-form-item>
                </el-tooltip>
              </el-col>
            </el-row>
          </el-form-item>
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
                    type="text"
                    tabindex="3" />
                </el-col>
                <el-col :span="6">
                  <indentify ref="identify" :identify-code="identifyCode" :content-width="80" @click="makeCode" />
                </el-col>
              </el-row>
            </el-form-item>
          </div>

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

export default {
  name: 'Login4',
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
