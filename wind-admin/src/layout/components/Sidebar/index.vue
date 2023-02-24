<template>
  <div :class="{'has-logo':showLogo}">
    <logo v-if="showLogo" :collapse="isCollapse" />
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :unique-opened="false"
        :collapse-transition="false"
        active-text-color="#fff"
        mode="vertical"
        router
      >
        <sidebar-item v-for="item in menus" :key="item.id" :menu="item" />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import Logo from './Logo'
import SidebarItem from './SidebarItem'

export default {
  name: 'Sidebar',
  components: { SidebarItem, Logo },
  computed: {
    menus() {
      return this.$store.permissionStore.menus
    },
    sidebar() {
      return this.$store.appStore.sidebar
    },
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
      return !this.sidebar.opened
    }
  }
}
</script>
<style scoped lang="scss">
.el-scrollbar{
  margin-left: 20px;
}
.el-menu {
  border-right: 0px;
}
.el-menu-item{
  height: 40px;
}
.el-menu-item.is-active, li.el-menu-item.is-active {
   background-color: #0242A3 !important;
   border-radius: 20px 0 0 20px;
}
.el-sub-menu.is-active.is-opened{
  .el-menu.el-menu--inline{
    .el-menu-item.is-active{
  background: #0242A3 !important;
  border-radius: 20px 0 0 20px;
}
  }
}
.el-sub-menu__title{
.is-active{
  background: #0242A3 !important;
  border-radius: 20px 0 0 20px;
}
}

</style>

