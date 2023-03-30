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
        <!-- <el-sub-menu v-for="item in menus" :key="item.id" :route="item.path" :class="{'isclick':item.id ===idd}" :index="item.id+''">
          <template #title>
            <el-icon><Setting /></el-icon>
            <span>{{ item.meta.title }}</span>
          </template>
          <el-menu-item v-for="subItem in item.children" :key="subItem.id" :route="subItem.path" :index="subItem.id+''" @click="cc(item.id)">
            <template #title>
              <img src="../../../assets/img/shuxian1.png" alt=""/>
              <span>{{ subItem.meta.title }}</span>
            </template>
          </el-menu-item>
        </el-sub-menu> -->
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
  data() {
    return {
      idd: null
    }
  },
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
  },
  mounted() {
    console.log('导航', this.$store.permissionStore.menus)
  },
  methods: {
    cc(id) {
      this.idd = id
    }
  }
}
</script>
<style scoped lang="scss">
:deep(.el-menu.el-menu--vertical){
padding: 0 20px;

}
.isclick{
  background-color: #1762F2 !important;
   border-radius: 10px;
}
 :deep(.el-sub-menu.is-active > .el-sub-menu__title) {
  background-color: #1762F2 !important;
   border-radius: 10px;
  //  position: relative;
   color: #fff;
   height: 40px;
   z-index: 15;
  }
  :deep(.el-menu.el-menu--inline){
    .el-sub-menu.is-active{
      .el-sub-menu__title{
         background-color: #fff !important;
         color: black;
         z-index: 5;
      }
    }
  }
</style>

