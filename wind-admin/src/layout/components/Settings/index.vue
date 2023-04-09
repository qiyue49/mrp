<template>
  <div>
    <svg-icon icon-class="ui-settings" color="#6B738D" @click="click" />
    <el-drawer v-model="show" size="500">
      <div class="drawer-container">
        <h3 class="drawer-title">系统布局配置</h3>

        <div class="drawer-item">
          <span>主题色</span>
        </div>

        <div class="drawer-item">
          <span>开启 Tags-View</span>
          <el-switch v-model="tagsView" class="drawer-switch" />
        </div>

        <div class="drawer-item">
          <span>开启 顶部菜单</span>
          <el-switch v-model="topMenu" class="drawer-switch" />
        </div>

        <div v-if="false" class="drawer-item">
          <span>固定 Header</span>
          <el-switch v-model="fixedHeader" class="drawer-switch" />
        </div>

        <div class="drawer-item">
          <span>侧边栏 Logo</span>
          <el-switch v-model="sidebarLogo" class="drawer-switch" />
        </div>

      </div>
    </el-drawer>
  </div>
</template>

<script>
import SvgIcon from '@/components/SvgIcon/index.vue'

export default {
  name: 'Setting',
  components: { SvgIcon },
  data() {
    return {
      show: false
    }
  },
  computed: {
    fixedHeader: {
      get() {
        return this.$store.settingStore.fixedHeader
      },
      set(val) {
        this.$store.settingStore.changeSetting({
          key: 'fixedHeader',
          value: val
        })
      }
    },
    topMenu: {
      get() {
        return this.$store.settingStore.topMenu
      },
      set(val) {
        this.$store.settingStore.changeSetting({
          key: 'topMenu',
          value: val
        })
        this.$store.permissionStore.toggleMenu()
      }
    },
    tagsView: {
      get() {
        return this.$store.settingStore.tagsView
      },
      set(val) {
        this.$store.settingStore.changeSetting({
          key: 'tagsView',
          value: val
        })
      }
    },
    sidebarLogo: {
      get() {
        return this.$store.settingStore.sidebarLogo
      },
      set(val) {
        this.$store.settingStore.changeSetting({
          key: 'sidebarLogo',
          value: val
        })
      }
    }
  },
  methods: {
    themeChange(val) {
      this.$store.settingStore.changeSetting({
        key: 'theme',
        value: val
      })
    },
    click() {
      this.show = true
    }
  }
}
</script>

<style lang="scss" scoped>
.drawer-container {
  padding: 24px;
  font-size: 14px;
  line-height: 1.5;
  word-wrap: break-word;

  .drawer-title {
    margin-bottom: 12px;
    color: rgba(0, 0, 0, .85);
    font-size: 14px;
    line-height: 22px;
  }

  .drawer-item {
    color: rgba(0, 0, 0, .65);
    font-size: 14px;
    padding: 12px 0;
  }

  .drawer-switch {
    float: right
  }
}
</style>
