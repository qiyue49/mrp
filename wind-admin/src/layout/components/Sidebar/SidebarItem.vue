<template>
  <a v-if="isExternalUrl(menu.path) && !menu.children && !menu.hidden" :key="menu.id" :href="menu.path" target="_blank" rel="noopener">
    <el-menu-item index="">
      <el-icon v-if="menu.meta.icon" class="icon" style="margin-left: -8px"><component :is="menu.meta.icon"/></el-icon>
      <template #title>
        <span>{{ menu.meta.title }}</span>
      </template>
    </el-menu-item>
  </a>

  <!-- 最后一级菜单 -->
  <el-menu-item v-else-if="!menu.children && !menu.hidden" :key="menu.id" :index="menu.path">
    <el-icon v-if="menu.meta.icon" class="icon" style="margin-left: -8px"><component :is="menu.meta.icon"/></el-icon>
    <template #title>
      <span>{{ menu.meta.title }}</span>
    </template>
  </el-menu-item>

  <!-- 此菜单下还有子菜单 -->
  <el-sub-menu v-else-if="menu.children && !menu.hidden" :key="menu.id" :index="menu.id">
    <template #title>
      <el-icon v-if="menu.meta.icon" class="icon" style="margin-left: -8px"><component :is="menu.meta.icon"/></el-icon>
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

<style lang="scss" scoped>
.el-menu-item {
  height: 40px;
}

.el-menu-item.is-active {
  background-color: var(--el-color-primary) !important;
  border-radius: 10px;
  color: #fff;
  height: 40px;
  z-index: 10;
}

.el-sub-menu.is-active {
  :deep(.el-icon) {
    color: #1762f2;
  }

  :deep(.el-sub-menu__title span) {
    color: #1762f2;
  }

  ul .el-sub-menu {
    :deep(.el-sub-menu__title span) {
      color: #000;
    }

    :deep(.el-icon) {
      color: #000;
    }
  }

  ul .is-active {
    :deep(.el-sub-menu__title span) {
      color: #1762f2 !important;
    }

    :deep(.el-icon) {
      color: #1762f2 !important;
    }
  }
}

a {
  text-decoration: none;
  color: var(--el-text-color-primary);
}
</style>
