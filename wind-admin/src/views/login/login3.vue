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
                    prefix-icon="el-icon-user"
                    :placeholder="$t('login.username')"
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
                      prefix-icon="el-icon-lock"
                      :type="passwordType"
                      :placeholder="$t('login.password')"
                      name="password"
                      tabindex="2"
                      autocomplete="on"
                      @keyup.native="checkCapslock"
                      @blur="capsTooltip = false"
                      @keyup.enter.native="handleLogin"
                    />
                    <span class="show-pwd" @click="showPwd">
                      <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
                    </span>
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
                        prefix-icon="el-icon-postcard"
                        :placeholder="$t('login.identify')"
                        name="identify"
                        type="text"
                        tabindex="3" />
                    </el-col>
                    <el-col :span="10">
                      <indentify ref="identify" :identify-code="identifyCode" :content-width="80" @click.native.prevent="makeCode" />
                    </el-col>
                  </el-row>
                </el-form-item>

              </el-col>
            </el-row>
          </el-form>
        </div>
        <el-button :loading="loading" @click.native.prevent="handleLogin">
          {{ $t('login.logIn') }}
        </el-button>

      </div>
    </div>
  </div>
</template>
<script>
import { configureWebpack } from '../../../vue.config'
import { mapState } from 'vuex'
import { Message } from 'element-ui'
import Indentify from '@/components/Identify/identify'
import { makeCode } from '@/utils'

export default {
  name: 'Login3',
  components: { Indentify },
  data() {
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('密码不能小于6位'))
      } else {
        callback()
      }
    }
    const validateIdentify = (rule, value, callback) => {
      if (value.toLowerCase() != this.identifyCode.toLowerCase()) {
        this.makeCode()
        callback(new Error('验证码错误'))
      } else {
        callback()
      }
    }
    return {
      title: configureWebpack.name,
      identifyCode: undefined,
      isLogin: false,
      errorTime: 0,
      loginForm: {
        username: process.env.NODE_ENV === 'development' ? 'admin' : undefined,
        password: process.env.NODE_ENV === 'development' ? '123456' : undefined,
        identify: undefined
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur' }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }],
        identify: [{ required: true, trigger: 'blur', validator: validateIdentify }]
      },
      passwordType: 'password',
      capsTooltip: false,
      loading: false,
      showDialog: false,
      redirect: undefined,
      otherQuery: {}
    }
  },
  computed: {
    ...mapState({
      token: state => state.user.token
    })
  },
  watch: {
    $route: {
      handler: function(route) {
        const query = route.query
        if (query) {
          this.redirect = query.redirect
          this.otherQuery = this.getOtherQuery(query)
        }
      },
      immediate: true
    }
  },
  created() {
  },
  mounted() {
    if (this.loginForm.username === '') {
      this.$refs.username.focus()
    } else if (this.loginForm.password === '') {
      this.$refs.password.focus()
    }
    this.makeCode()
  },
  destroyed() {
    // window.removeEventListener('storage', this.afterQRScan)
  },
  methods: {
    makeCode() {
      this.identifyCode = makeCode(4)
      // console.log('identifyCode', this.identifyCode)
    },
    checkCapslock({ shiftKey, key } = {}) {
      if (key && key.length === 1) {
        if (shiftKey && (key >= 'a' && key <= 'z') || !shiftKey && (key >= 'A' && key <= 'Z')) {
          this.capsTooltip = true
        } else {
          this.capsTooltip = false
        }
      }
      if (key === 'CapsLock' && this.capsTooltip === true) {
        this.capsTooltip = false
      }
    },
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('user/login', this.loginForm)
            .then((res) => {
              this.loading = false
              if (!this.$store.getters.token) {
                this.errorTime++
                this.makeCode()
                Message.error(res.data.msg)
                return
              }

              this.$router.push({ path: this.redirect || '/', query: this.otherQuery })
            })
            .catch(() => {
              this.loading = false
            })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    getRandomArbitrary(min, max) {
      return Math.floor(Math.random() * (max - min) + min)
    },
    getOtherQuery(query) {
      return Object.keys(query).reduce((acc, cur) => {
        if (cur !== 'redirect') {
          acc[cur] = query[cur]
        }
        return acc
      }, {})
    }
  }
}
</script>
<style lang="scss" scoped>
.all {
  width: 100%;
  height: 100%;
  background-image: url("~@/assets/img/login/login3/login3.png");
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
        background-image: url("~@/assets/img/login/login3/login3-1.png");
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
