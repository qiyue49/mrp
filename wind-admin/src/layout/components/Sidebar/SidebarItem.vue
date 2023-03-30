<template>
  <a v-if="isExternalUrl(menu.path) && !menu.children && !menu.hidden" :key="menu.id" :href="menu.path" target="_blank" rel="noopener">
    <el-menu-item index="">
      <img v-if="menu.parentId" src="../../../assets/img/shuxian2long.svg" alt=""/>
      <el-icon class="icon" style="margin-left: -8px"><component :is="menu.meta.icon"/></el-icon>
      <template #title>
        <span>{{ menu.meta.title }}22</span>
      </template>
    </el-menu-item>
  </a>

  <!-- 最后一级菜单 -->
  <el-menu-item v-else-if="!menu.children && !menu.hidden" :key="menu.id" :index="menu.path" @click="card(topmenu.id)">
    <template #title>
      <img v-if="menu.parentId" src="../../../assets/img/shuxian2long.svg" alt=""/>
      <el-icon v-else class="icon" style="margin-left: 8px"><component :is="menu.meta.icon"/></el-icon>
      <div style="width"></div>
      <span>{{ menu.meta.title }}</span>
    </template>
  </el-menu-item>

  <!-- 此菜单下还有子菜单 -->
  <el-sub-menu v-else-if="menu.children && !menu.hidden" :key="menu.id" class="ccc" :class="{'isclick':menu.id ===idd}" :index="menu.id" @click="topcard(menu.id)">
    <template #title>
      <img v-if="menu.parentId" src="../../../assets/img/shuxian2long.svg" alt=""/>
      <el-icon class="icon" ><component :is="menu.meta.icon"/></el-icon>
      <span>{{ menu.meta.title }}</span>
    </template>
    <!-- 递归 -->
    <sidebar-item v-for="item in menu.children" :key="item.id" :menu="item" :topmenu="menu" />
  </el-sub-menu>
</template>

<script>
import { isExternal } from '@/utils/validate'

export default {
  name: 'SidebarItem',
  props: {
    menu: {
      type: Object,
      required: true
    },
    topmenu: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      idd: null
    }
  },
  methods: {
    isExternalUrl(path) {
      return isExternal(path)
    },
    topcard(id) {
      console.log('sss', id)
    },
    card(id) {
      this.idd = id
      console.log('ccc', this.idd)
      console.log('上级', this.topmenu)
    }
  }
}
</script>

<style lang="scss" scoped>
  a {
    text-decoration: none;
    color: var(--el-text-color-primary);
  }
  :deep(.el-menu-item){
    position: relative !important;
    img{
      position: absolute !important;
      top: -40px !important;
      // left: 65px !important;
      // margin-right: 20px;
    }
    span{
      // margin-left: 20px;
    }
  }
  :deep(.el-sub-menu__title){
    position: relative !important;
    img{
      position: absolute !important;
      top: -40px !important;
      // left: 45px !important;
      // margin-right: 20px;
    }
    span{
      // margin-left: 20px;
    }
  }
  .isclick{
  background-color: #1762F2 !important;
   border-radius: 10px;
  // //  position: relative;
  //  color: #fff;
  //  height: 40px;
  //  z-index: 10;
  //  width: 170px;
  //  display: flex;
  //  align-items: center;
}
:deep(.el-sub-menu__title){
  background-color: #fff;
  z-index: 15;
  .el-sub-menu__title{
    z-index: 10;
  }
}
</style>

