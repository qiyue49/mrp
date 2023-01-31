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
              <el-tooltip v-model="capsTooltip" content="大写键已打开" placement="left" manual>
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
import { ElMessage } from 'element-plus'
import Indentify from '@/components/Identify/identify'
import { makeCode } from '@/utils'
import SvgIcon from '@/components/SvgIcon/index.vue'

export default {
  name: 'Login1',
  components: { SvgIcon, Indentify },
  data() {
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('密码不能小于6位'))
      } else {
        callback()
      }
    }
    const validateIdentify = (rule, value, callback) => {
      if (value.toLowerCase() !== this.identifyCode.toLowerCase()) {
        this.makeCode()
        callback(new Error('验证码错误'))
      } else {
        callback()
      }
    }
    return {
      title: this.$store.settingStore.title,
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
  methods: {
    makeCode() {
      this.identifyCode = makeCode(4)
      console.log('identifyCode', this.identifyCode)
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
          this.$store.userStore.login(this.loginForm)
            .then((res) => {
              this.loading = false
              if (!this.$store.userStore.token) {
                this.errorTime++
                this.makeCode()
                ElMessage.error(res.data.msg)
                return
              }
              const path = { path: this.redirect || '/', query: this.otherQuery }
              console.log(path)
              this.$router.push('/')
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
  width: 100%;
  height: 100vh;
  background-position: top center;
  background-size: 100% 100%;
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
