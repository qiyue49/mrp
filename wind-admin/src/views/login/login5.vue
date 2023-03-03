<template>
  <div class="all">
    <div class="left"></div>
    <div class="right">
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
</template>

<script>
import { mixin } from '@/views/login/mixin'
export default {
  name: 'Login5',
  mixins: [mixin]
}
</script>

<style lang="scss" scoped>
.all{
    display: flex;
    .left{
    width:50%
}
.right{
    width:50%
}
}
</style>