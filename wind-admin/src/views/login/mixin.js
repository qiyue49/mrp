import SvgIcon from '@/components/SvgIcon/index.vue'
import Indentify from '@/components/Identify/identify.vue'
import { makeCode } from '@/utils'
import { ElMessage } from 'element-plus'

export const mixin = {
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
    },
    checkCapslock({ shiftKey, key } = {}) {
      if (key && key.length === 1) {
        this.capsTooltip = (shiftKey && (key >= 'a' && key <= 'z')) || (!shiftKey && (key >= 'A' && key <= 'Z'))
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
              if (!this.$store.userStore.token) {
                this.errorTime++
                this.makeCode()
                ElMessage.error(res.data.msg)
                this.loading = false
                return
              }
              const path = { path: this.redirect || '/', query: this.otherQuery }
              this.$router.push(path)
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
