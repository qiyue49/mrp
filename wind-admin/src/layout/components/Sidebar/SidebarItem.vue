<template>
  <a v-if="isExternalUrl(menu.path) && !menu.children && !menu.hidden" :key="menu.id" :href="menu.path" target="_blank" rel="noopener">
    <el-sub-menu :index="menu.path">
      <template #title>
        <component :is="menu.meta.icon" class="icon"/>
        <span>{{ menu.meta.title }}</span>
      </template>
    </el-sub-menu>
  </a>

  <!-- 最后一级菜单 -->
  <el-menu-item v-else-if="!menu.children && !menu.hidden" :key="menu.id" :index="menu.path">
    <template #title>
      <component :is="menu.meta.icon" class="icon"/>
      <span>{{ menu.meta.title }}</span>
    </template>
  </el-menu-item>

  <!-- 此菜单下还有子菜单 -->
  <el-sub-menu v-else-if="menu.children && !menu.hidden" :key="menu.id" :index="menu.id">
    <template #title>
      <component :is="menu.meta.icon" class="icon"/>
      <span>{{ menu.meta.title }}</span>
    </template>
    <!-- 递归 -->
    <sidebar-item v-for="item in menu.children" :key="item.id" :menu="item" />
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
    }
  },
  data() {
    return {}
  },
  methods: {
    isExternalUrl(path) {
      return isExternal(path)
    }
  }
}
</script>

<style scoped>
  $icon-size: 20px;
  .el-menu-item [class^="el-icon-"] {
    margin-right: 5px;
    text-align: center;
    width: 20px;
    font-size: 18px;
    vertical-align: middle;
  }
  i {
    margin-right: 5px;
    text-align: center;
    width: 20px;
    font-size: 16px;
    vertical-align: middle;
  }
  .icon{
    width: $icon-size;
    height: $icon-size;
    padding: 8px;
    vertical-align: middle;
  }
</style>

