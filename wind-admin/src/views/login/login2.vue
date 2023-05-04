<template>
  <div class="all">
    <div class="center">
      <div class="top">
        <div class="title">{{ title }}</div>
        <div class="fuTitle">
          <span></span>
          <p>提升中小企业影响力</p>
          <span></span>
        </div>
      </div>
      <div class="bottom">
        <div>
          <el-form ref="loginForm" :model="loginForm" :rules="loginRules" autocomplete="on" label-position="left">
            <el-row :gutter="20" style="padding-left: 40px; padding-right: 40px">
              <el-col :span="errorTime >= 3 ? 8 : 12">
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
              </el-col>
              <el-col :span="errorTime >= 3 ? 8 : 12">
                <el-tooltip v-model="capsTooltip" content="大写键已打开" placement="right" manual>
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
              <el-col v-if="errorTime >= 3" :span="8">
                <el-form-item prop="identify">
                  <el-row>
                    <el-col :span="14">
                      <el-input
                        ref="username"
                        v-model="loginForm.identify"
                        prefix-icon="Postcard"
                        :placeholder="验证码"
                        name="identify"
                        type="text"
                        tabindex="3" />
                    </el-col>
                    <el-col :span="10">
                      <indentify ref="identify" :identify-code="identifyCode" :content-width="80" @click="makeCode" />
                    </el-col>
                  </el-row>
                </el-form-item>

              </el-col>
            </el-row>
          </el-form>
        </div>
        <el-button :loading="loading" @click="handleLogin">
          登录
        </el-button>

      </div>
    </div>
  </div>
</template>
<script>
import { mixin } from '@/views/login/mixin'

export default {
  name: 'Login2',
  mixins: [mixin]

}
</script>
<style lang="scss" scoped>
.all {
  width: 100%;
  height: 100%;
  background-image: url("@/assets/img/login/login2/login.png");
  background-size: 100% 100%;
  background-repeat: no-repeat;
  display: flex;
  justify-content: center;
  align-items: center;
  > .center {
    width: 770px;
    height: 450px;
    // border: 1px solid red;
    > div {
      width: 100%;
      height: 50%;
      //    border: 1px solid blue;
    }
    > .top {
      .title {
        width: 100%;
        height: 50%;
        //    border: 1px solid green;
        color: #fff;
        display: flex;
        justify-content: center;
        align-items: flex-end;
        font-size: 55px;
        font-weight: 800;
      }
      .fuTitle {
        width: 100%;
        height: 50%;
        //    border: 1px solid green;
        display: flex;
        align-items: center;
        color: #fff;
        font-size: 30px;
        font-weight: 400;
        position: relative;
        > p {
          width: 100%;
          height: 100%;
          display: flex;
          justify-content: center;
          align-items: center;
          margin: 0;
        }
        > span {
          width: 100px;
          position: absolute;
          border-bottom: 2px solid #fff;
        }
        > span:nth-child(1) {
          left: 110px;
        }
        > span:nth-child(3) {
          right: 110px;
        }
      }
    }
    > .bottom {
      display: flex;
      flex-direction: column;
      align-items: center;
      position: relative;

      > div:nth-child(1) {
        position: absolute;
        width: 98%;
        height: 57%;
        background-image: url("@/assets/img/login/login2/login1.png");
        background-size: 100% 100%;
        display: flex;
        justify-content: center;
        align-items: center;
        > form {
          width: 100%;
          margin-top: 4%;
        }
      }
      > .el-button {
        width: 20%;
        position: absolute;
        bottom: 20px;
        background-color: transparent;
        color: #fff;
      }
    }
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
  .el-form-item {
    margin-bottom: 0px;
  }
}
</style>
