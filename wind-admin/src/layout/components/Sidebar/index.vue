<template>
  <div :class="{'has-logo':showLogo}">
    <logo v-if="showLogo" :collapse="isCollapse" />
    <el-scrollbar :class="{'scrollbar-left':$store.appStore.sidebar.opened}" wrap-class="scrollbar-wrapper">
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
:deep(.el-sub-menu.is-active > .el-sub-menu__title) {
  background-color: #1762F2 !important;
  border-radius: 10px;
  //  position: relative;
  color: #fff;
  height: 40px;
  z-index: 10;
  width: 170px;
  //  display: flex;
  //  align-items: center;
}
:deep(.el-menu-item.is-active) {
  color: #1762F2;
  background-color: #ffffff;
  height: 40px;
  //  display: block;
  //  padding-left: 0;
  line-height: 40px;
  //  padding: 0 !important;
  //  width: 100px;
}
:deep(.el-menu-item) {
  height: 40px;
  //  display: block;
  //  padding-left: 0;
  line-height: 40px;
  //  padding: 0 !important;
  //  width: 100px;
  position: relative;
}
:deep(.el-menu.el-menu--inline > .el-menu-item > span){
  padding-left: 40px !important;
  // padding-right: 20px !important;
  position: absolute;
  right: 50px;
}
//对应子级，父级的样式
.el-submenu.is-active > .el-submenu__title {
  background: rgb(206, 169, 181) !important;
}
//当前选中子级的样式
.is-active{
  // background-color: #0242A3 !important;
  border-radius: 20px 0 0 20px;
  position: relative;
  color: #fff !important;
  margin-right: 20px;
}
.el-sub-menu{
  margin-right: 20px;
}
.scrollbar-left{
  margin-left: 20px;
}
.el-menu {
  border-right: 0px;
}
.el-menu-item{
  height: 40px;
}
.el-menu-item.is-active, li.el-menu-item.is-active {
  background-color: #1762F2 !important;
  border-radius: 10px;
  position: relative;
  color: #fff;
}
.el-sub-menu.is-active.is-opened{
  .el-menu.el-menu--inline{
    .el-menu-item.is-active{
      background: #1762F2 !important;
      border-radius: 20px 0 0 20px;
    }
  }
}
.el-sub-menu__title{
  .is-active{
    background: #1762F2 !important;
    border-radius: 20px 0 0 20px;
  }
}
</style>

