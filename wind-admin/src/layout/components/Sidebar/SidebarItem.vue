<template>
  <div>
    <template v-for="item in menu">
      <a v-if="isExternalUrl(item.path) && !item.children && !item.hidden" :key="item.id" :href="item.path" target="_blank" rel="noopener">
        <el-menu-item index="">
          <i :class="item.meta.icon"></i>
          <span slot="title">{{ item.meta.title }}</span>
        </el-menu-item>
      </a>

      <!-- 最后一级菜单 -->
      <el-menu-item v-else-if="!item.children && !item.hidden" :key="item.id" :index="item.path">
        <i :class="item.meta.icon"></i>
        <span slot="title">{{ item.meta.title }}</span>
      </el-menu-item>

      <!-- 此菜单下还有子菜单 -->
      <el-submenu v-else-if="item.children && !item.hidden" :key="item.id" :index="item.id">
        <template slot="title">
          <i :class="item.meta.icon"></i>
          <span>{{ item.meta.title }}</span>
        </template>
        <!-- 递归 -->
        <sidebar-item
          :menu="item.children"
          class="nest-menu"
        />
      </el-submenu>
    </template>
  </div>
</template>

<script>
import { isExternal } from '@/utils/validate'

export default {
  name: 'SidebarItem',
  props: {
    menu: {
      type: Array,
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
  .title {
    color: #fff;
  }
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
</style>

