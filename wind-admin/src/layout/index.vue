<template>
  <div class="common-layout">
    <el-container>
      <el-aside width="200px">
        <sidebar class="sidebar-container" />
      </el-aside>
      <el-container>
        <el-header>Header</el-header>
        <el-main>
          <app-main />
        </el-main>
        <el-footer>Footer</el-footer>
      </el-container>
    </el-container>
  </div>
</template>

<script>
// import { AppMain, Navbar, Sidebar, TagsView } from './components'
import ResizeMixin from './mixin/ResizeHandler'
import { mapState } from 'pinia'
import { appStore } from '@/stores/modules/app'
import { settingStore } from '@/stores/modules/settings'
import AppMain from '@/layout/components/AppMain'
import Sidebar from '@/layout/components/Sidebar/index'

export default {
  name: 'Layout',
  components: { Sidebar, AppMain },
  mixins: [ResizeMixin],
  computed: {
    ...mapState(appStore, ['sidebar', 'device']),
    ...mapState(settingStore, ['showSettings', 'needTagsView', 'fixedHeader']),
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
  @import "@/styles/mixin.scss";
  @import "@/styles/variables.scss";

  .app-wrapper {
    @include clearfix;
    position: relative;
    height: 100%;
    width: 100%;

    &.mobile.openSidebar {
      position: fixed;
      top: 0;
    }
  }

  .drawer-bg {
    background: #000;
    opacity: 0.3;
    width: 100%;
    top: 0;
    height: 100%;
    position: absolute;
    z-index: 999;
  }

  .fixed-header {
    position: fixed;
    top: 0;
    right: 0;
    z-index: 9;
    width: calc(100% - 210px);
    transition: width 0.28s;
  }

  .hideSidebar .fixed-header {
    width: calc(100% - 54px)
  }

  .mobile .fixed-header {
    width: 100%;
  }
</style>
