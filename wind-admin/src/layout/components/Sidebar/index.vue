<template>
  <div :class="{'has-logo':showLogo}">
    <logo v-if="showLogo" :collapse="isCollapse" />
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :unique-opened="false"
        :collapse-transition="false"
        mode="vertical"
        router
      >
        <sidebar-item v-for="item in menus" :key="item.id" :menu="item" />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import { mapState } from 'pinia'
import Logo from './Logo'
import SidebarItem from './SidebarItem'
import { permissionStore } from '@/stores/modules/permission'
import { appStore } from '@/stores/modules/app'

export default {
  name: 'Sidebar',
  components: { SidebarItem, Logo },
  computed: {
    ...mapState(permissionStore, ['menus']),
    ...mapState(appStore, ['sidebar']),
    activeMenu() {
      const route = this.$route
      const { meta, path } = route
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    },
    showLogo() {
      return this.$store.settingStore.sidebarLogo
    },
    isCollapse() {
      console.log('this.sidebar.opened', this.sidebar.opened)
      return !this.sidebar.opened
    }
  }
}
</script>
<style scoped>
.el-menu {
  border-right: 0px;
}
</style>

