<template>
  <el-card class="el-card">
    <div style="padding: 20px">
      <h1>这是一个白名单页面</h1>
      <h3>什么是白名单页面</h3>
      <p>白名单页面是不需要登录也能够访问的页面,例如本系统的登录页。如果你有一些页面需要对外直接展示，可以将他们加入白名单。</p>
      <h3>如何添加一个白名单</h3>
      <p>首先创建好白名单页面</p>
      <p>其次在router的index.js中constantRoutes数组中配置该页面的路由。本例中为/whiteList</p>
      <p>然后在src的permission.js中whiteList数组中添加该页面的路由。本例中为/whiteList</p>
      <p>最后运行项目，直接在浏览器中输入对应网址，就能看到该页面。本例中为
        <br/>
        <a target="_blank" href="http://localhost:9527/#/whiteList?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlSWQiOiIxIiwidGVuYW50SWQiOiIwMDAwMDAwMCIsImlkIjoiYzkxYTk4NmE3OTdhOGI2OWQ2ZTEzZTk0ZTJjNzg3NDAiLCJleHAiOjE2NjAxNDQ1NjUsInVzZXJuYW1lIjoiZGlob25nIiwicmVhbG5hbWUiOiLluJ3msZ8oaG9uZykifQ._CRfVpLDEAw2OogkTBHTq_4W6W5igjMAzBM2UwKFWa8">http://localhost:9527/#/whiteList?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlSWQiOiIxIiwidGVuYW50SWQiOiIwMDAwMDAwMCIsImlkIjoiYzkxYTk4NmE3OTdhOGI2OWQ2ZTEzZTk0ZTJjNzg3NDAiLCJleHAiOjE2NjAxNDQ1NjUsInVzZXJuYW1lIjoiZGlob25nIiwicmVhbG5hbWUiOiLluJ3msZ8oaG9uZykifQ._CRfVpLDEAw2OogkTBHTq_4W6W5igjMAzBM2UwKFWa8
        </a>
      </p>
      <h3>白名单页面需要注意什么</h3>
      <p>因为白名单页面不需要登录就可以使用，所以白名单页面是没有加载任何系统数据的，例如数据字典。</p>
      <p>如果需要加载字典，可以调用pinia的相关action。当然你也可以自己写网络请求。请参考init方法。</p>
      <p>如果你的页面是需要开放给三方系统使用。并且需要获取用户信息，可以将用户标识符作为参数传入进来。请参考created。</p>
      <p>白名单页面同样可以作为一个普通页面展示，菜单和权限的配置方式和其他页面没区别。</p>
      <el-divider>
        字典状态
      </el-divider>
      <div>
        <el-select v-model="status" class="filter-item" placeholder="请选择登陆状态">
          <el-option
            v-for="item in dictList('loginstatus')"
            :key="item.label + 'loginstatus'"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </div>
    </div>
  </el-card>

</template>

<script>

export default {
  name: 'WhiteList',
  data() {
    return {
      token: undefined,
      status: undefined
    }
  },
  created() {
    this.init()
  },
  methods: {
    init() {
      this.token = this.$route.query.token
      if (!this.isNull(this.token)) {
        // 一定要设置完token才能发起网络请求，否则会被拒绝
        this.$store.userStore.setToken(this.token)
        // 加载字典
        this.$store.dictStore.initDict()
        // 加载系统配置
        this.$store.sysConfigStore.getConfig()
      }
    }

  }
}
</script>

<style scoped>

</style>
