<template>
  <!-- <div class="all">
    <div class="serc">
      <img src="@/assets/img/searc.png" alt=""/>
      <input v-model="input" placeholder="请输入内容"/>
    </div>
    <span>搜索</span>
  </div> -->
  <div :class="{'show':show}" class="header-search">
    <svg-icon class="search-icon" icon-class="ui-search" color="#6B738D" @click.stop="click" />
    <el-select
      ref="headerSearchSelect"
      v-model="search"
      :remote-method="querySearch"
      filterable
      default-first-option
      remote
      placeholder="Search"
      class="header-search-select"
      @change="change"
    >
      <el-option v-for="item in options" :key="item.path" :value="item" :label="item.title" />
    </el-select>
  </div>
</template>

<script>
import Fuse from 'fuse.js'
import SvgIcon from '@/components/SvgIcon/index.vue'

export default {
  name: 'HeaderSearch',
  components: { SvgIcon },
  data() {
    return {
      search: '',
      options: [],
      searchPool: [],
      show: false,
      fuse: undefined
    }
  },
  computed: {
    routes() {
      return this.$store.permissionStore.routes
    }
  },
  watch: {
    lang() {
      this.searchPool = this.generateRoutes(this.routes)
    },
    routes() {
      this.searchPool = this.generateRoutes(this.routes)
    },
    searchPool(list) {
      this.initFuse(list)
    },
    show(value) {
      if (value) {
        document.body.addEventListener('click', this.close)
      } else {
        document.body.removeEventListener('click', this.close)
      }
    }
  },
  mounted() {
    this.searchPool = this.generateRoutes(this.routes)
  },
  methods: {
    click() {
      this.show = !this.show
      if (this.show) {
        this.$refs.headerSearchSelect && this.$refs.headerSearchSelect.focus()
      }
    },
    close() {
      this.$refs.headerSearchSelect && this.$refs.headerSearchSelect.blur()
      this.options = []
      this.show = false
    },
    change(val) {
      this.$router.push(val.path)
      this.search = ''
      this.options = []
      this.$nextTick(() => {
        this.show = false
      })
    },
    initFuse(list) {
      this.fuse = new Fuse(list, {
        shouldSort: true,
        threshold: 0.4,
        location: 0,
        distance: 100,
        maxPatternLength: 32,
        minMatchCharLength: 1,
        keys: [{
          name: 'title',
          weight: 0.7
        }, {
          name: 'path',
          weight: 0.3
        }]
      })
    },
    // Filter out the routes that can be displayed in the sidebar
    // And generate the internationalized title
    generateRoutes(routes, basePath = '/', prefixTitle = []) {
      let res = []

      routes.forEach(router => {
        // skip hidden router
        if (router.hidden) { return }

        const data = {
          path: router.path,
          title: [...prefixTitle]
        }

        if (router.meta && router.meta.title) {
          // generate internationalized title
          data.title = router.meta.title
          // const hasKey = router.meta.title
          // if (hasKey) {
          //   const i18ntitle = router.meta.title
          //   data.title = [...data.title, i18ntitle]
          // } else {
          //   data.title = [...data.title, router.meta.title]
          // }
          // const i18ntitle = generateTitle(router.meta.title)

          // data.title = [...data.title, i18ntitle]

          if (router.redirect !== 'noRedirect') {
            // only push the routes with title
            // special case: need to exclude parent router without redirect
            res.push(data)
          }
        }

        // recursive child routes
        if (router.children) {
          const tempRoutes = this.generateRoutes(router.children, data.path, data.title)
          if (tempRoutes.length >= 1) {
            res = [...res, ...tempRoutes]
          }
        }
      })
      return res
    },
    querySearch(query) {
      this.options = []
      if (query !== '') {
        this.fuse.search(query).forEach(item => {
          this.options.push(item.item)
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.all{
  font-size: 14px;
  padding: 10px 0;
  display: flex;
  align-items: center;
  .serc{
  border: 1px solid #E7ECF3;
  height: 30px;
  line-height: 30px;
  border-top-left-radius: 10PX;
  border-bottom-left-radius: 10PX;
  display: flex;
  align-items: center;
  padding: 0 10px;
  input:focus{
        border: none !important;
    }
  input::placeholder{
        color: #E7ECF3;
    }

  input{
    border: none !important;
    color: #E7ECF3;
    padding: 0 10px;
    // outline: none;
    .el-input__wrapper{
      border: 0 !important;
    }
  }
   input:focus{
       border: 0;
}
}
span{
    background-color: #0243A3;
    height: 30PX;
    line-height: 30PX;
    padding: 0 10PX;
    border-top-right-radius: 10PX;
    border-bottom-right-radius: 10PX;
    border: 1px solid #0243A3;
    color: #FFF;
  }
}
.header-search {
  font-size: 0 !important;

  .search-icon {
    cursor: pointer;
    font-size: 18px;
    vertical-align: middle;
    width: 23px !important;
    height: 23px !important;
  }

  .header-search-select {
    font-size: 18px;
    transition: width 0.2s;
    width: 0;
    overflow: hidden;
    background: transparent;
    border-radius: 0;
    display: inline-block;
    vertical-align: middle;

    :deep(.el-input__inner) {
      border-radius: 0;
      border: 0;
      padding-left: 0;
      padding-right: 0;
      box-shadow: none !important;
      border-bottom: 1px solid #d9d9d9;
      vertical-align: middle;
    }
  }

  &.show {
    .header-search-select {
      width: 210px;
      margin-left: 10px;
    }
  }
}
</style>
