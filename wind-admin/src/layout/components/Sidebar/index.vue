<template>
  <div :class="{'has-logo':showLogo}">
    <logo v-if="showLogo" :collapse="isCollapse" />
    <el-scrollbar :class="{'scrollbar-left':$store.appStore.sidebar.opened}" wrap-class="scrollbar-wrapper">
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
:deep(.el-icon.el-sub-menu__icon-arrow){
  // display:none;
}
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
//  :deep(.el-sub-menu.is-active > .el-sub-menu__title::after) {
//   content: url(../../../assets/img/sanjiao-xia.svg);
//   background-color: transparent !important;
//   //  position: relative;
//    color: #fff;
//    z-index: 10;
//    height: 55px;
//    margin-left: 30px;
//   }
//  :deep(.el-sub-menu.is-active > .el-sub-menu__title::after) {
//   content: 's';
//    position: absolute;
//    right: 0;
//    color: #F5C265;
//    background-color: #F5C265;
//    height: 40px;
//   }
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
//  :deep(.el-menu-item.is-active::after) {
//    content: '>';
//    position: absolute;
//    right: 10px;
//    color: #0243A3;
//   //  background-color: #F5C265;
//    height: 40px;
//   }
//  :deep(.el-menu-item.is-active::before) {
//    content: url(../../../assets/img/shuxian2.png);
//    position: absolute;
//   //  left: -28px;
//   //  top: -200px;
//   top: -48px;
//   left: 42px;
//    z-index: 5;
//    color: #0243A3;
//   //  background-color: #F5C265;
//   //  height: 40px;
//   }
  :deep(.el-menu.el-menu--inline){
    // padding-right: 20px !important;
    // padding-right: 20px !important;
  }
  // :deep(.el-menu.el-menu--inline > .el-menu-item ::before){
  //   content: url(../../../assets/img/shuxian1.png);
  //   overflow: hidden;
  //  position: absolute;
  //   // position: absolute;
  //   // left: -60px;
  // }
  // :deep(.el-menu.el-menu--inline > .el-menu-item ::before){
  //   content: url(../../../assets/img/shuxian1.png);
  //  position: absolute;
  //   // width: 0;
  //   // height: 36px;
  //   position: absolute;
  //   // left: -80px;
  //   top: -50px;
  // }
  :deep(.el-menu.el-menu--inline > .el-menu-item > span){
    padding-left: 40px !important;
    // padding-right: 20px !important;
    position: absolute;
    right: 50px;
  }
  :deep(.el-menu-item > span){
    // padding-left: 40px !important;
    // padding-right: 20px !important;
    // margin-right: 100px !important
  }
//对应子级，父级的样式
.el-submenu.is-active > .el-submenu__title {
  background: rgb(206, 169, 181) !important;
}
// .el-submenu.is-active > .el-submenu__title::after {
//   background: rgb(206, 169, 181) !important;
//   content: 's';
//    position: absolute;
//    right: 0;
//    color: #F5C265;
//    background-color: #F5C265;
//    height: 40px;
// }
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
// .el-menu-item.is-active, li.el-menu-item.is-active::after {
//    content: 's';
//    position: absolute;
//    right: 0;
//    color: #F5C265;
//    background-color: #F5C265;
//    height: 40px;
//   //  width: 10px;
// }
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
:deep(.el-sub-menu){
  .el-menu.el-menu--inline{
    .el-menu-item{
      .el-icon{
        position: relative;
      }
      .el-icon::before{
        position: absolute;
        top: -50px;
        content: url(../../../assets/img/shuxian1.png);
      }
    }
    .el-menu-item.is-active{
      .el-icon{
        position: relative;
      }
      .el-icon::before{
        position: absolute;
        top: -50px;
        content: url(../../../assets/img/shuxian2.png);
      }
    }
  }
}
</style>

