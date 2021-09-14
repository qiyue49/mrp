<template>
  <div class="app">
    <div class="card">
      <div class="title">{{ title }}</div>
      <div class="content">
        <div class="left-img">
          <img src="~@/assets/login6/left-img.png" alt="" />
        </div>
        <div class="right-content">
          <h2 class="right-title">
            登陆LOGIN
          </h2>
          <div class="input">
            <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" autocomplete="on"
                 label-position="left">
            <el-form-item prop="username">
                <el-input
                  ref="username"
                  v-model="loginForm.username"
                  :placeholder="$t('login.username')"
                  name="username"
                  type="text"
                  tabindex="1"
                  autocomplete="on"
                />
              </el-form-item>
            <el-tooltip v-model="capsTooltip" content="大写键已打开" placement="left" manual>
                <el-form-item prop="password">
                  <el-input
                    :key="passwordType"
                    ref="password"
                    v-model="loginForm.password"
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

            <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;"
                         @click.native.prevent="handleLogin">
                {{ $t('login.logIn') }}
            </el-button>
            </el-form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { configureWebpack } from '../../../vue.config'
import { mapState } from 'vuex'
import { Message } from 'element-ui'

export default {
  name: 'Login1',
  data() {
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('密码不能小于6位'))
      } else {
        callback()
      }
    }
    return {
      title: configureWebpack.name,
      isLogin: false,
      loginForm: {
        username: process.env.NODE_ENV === 'development' ? 'admin' : undefined,
        password: process.env.NODE_ENV === 'development' ? '123456' : undefined
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur' }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }]
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
  },
  destroyed() {
    // window.removeEventListener('storage', this.afterQRScan)
  },
  methods: {
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
.app {
  height: 100%;
  background: url("~@/assets/login6/operations-bgc.png") no-repeat;
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
      .el-input {
        margin: 20px 0px;
      }
      .el-button {
        margin-top: 50px;
        width: 100%;
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
}
</style>
