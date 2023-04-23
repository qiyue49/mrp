<template>
  <div :class="classObj">
    <el-container>
      <el-aside>
        <sidebar class="sidebar-container" />
      </el-aside>
      <el-container>
        <el-header>
          <navbar />
          <tag-view />
        </el-header>
        <el-main>
          <app-main />
        </el-main>
        <el-footer>{{ title }} </el-footer>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import ResizeMixin from './mixin/ResizeHandler'
import AppMain from '@/layout/components/AppMain'
import Sidebar from '@/layout/components/Sidebar/index'
import Navbar from '@/layout/components/Navbar.vue'
import TagView from '@/layout/components/TagsView/index.vue'
import { useDark } from '@vueuse/core'

export default {
  name: 'Layout',
  components: { TagView, Navbar, Sidebar, AppMain },
  mixins: [ResizeMixin],
  setup() {
    const isDark = useDark({
      // 存储到localStorage/sessionStorage中的Key 根据自己的需求更改
      storageKey: 'useDarkKEY',
      // 暗黑class名字
      valueDark: 'dark',
      // 高亮class名字
      valueLight: 'light'
    })
    return {
      isDark
    }
  },
  computed: {
    sidebar() {
      return this.$store.appStore.sidebar
    },
    device() {
      return this.$store.appStore.device
    },
    title() {
      return this.$store.settingStore.title
    },
    classObj() {
      return {
        hideSidebar: !this.sidebar.opened,
        openSidebar: this.sidebar.opened,
        withoutAnimation: this.sidebar.withoutAnimation,
        mobile: this.device === 'mobile'
      }
    }
  },
  methods: {
    handleClickOutside() {
      this.$store.appStore.closeSideBar({ withoutAnimation: false })
    }
  }
}
</script>

<style lang="scss" scoped>
.sidebar-container {
  width: 210px !important;
}

.hideSidebar {
  .sidebar-container {
    width: 64px !important;
  }
}

.el-aside {
  border-right: solid 1px var(--el-menu-border-color);
  overflow: auto;
  box-sizing: border-box;
  flex-shrink: 0;
  width: auto;
}

.el-header {
  padding: 0px;
  height: auto;
}

.el-footer {
  height: auto;
}

.el-main {
  background: var(--ep-color-background);
  padding-top: 0px;
  min-height: calc(100vh - 109px);
  box-sizing: border-box;
  width: 100%;
}
</style>
