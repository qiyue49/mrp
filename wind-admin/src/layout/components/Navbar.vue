<template>
  <div class="navbar">
    <div>
      <hamburger id="hamburger-container" :is-active="sidebar.opened" class="hamburger-container" @toggle-click="toggleSideBar" />
      <top-menu v-if="topMenu" class="topmenu-container" />

      <div class="right-menu">
        <template v-if="device!=='mobile'">
          <search class="right-menu-item hover-effect" />
          <full-screen class="right-menu-item hover-effect" />
          <dark-theme class="right-menu-item hover-effect" />
          <setting class="right-menu-item hover-effect" />
        </template>

        <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
          <div class="avatar-wrapper">
            <img :src="avatarUrl+'?imageView2/1/w/80/h/80'" class="user-avatar" />
            <i class="el-icon-caret-bottom"></i>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <router-link to="/sys/user/info">
                <el-dropdown-item>
                  修改信息
                </el-dropdown-item>
              </router-link>
              <router-link to="/sys/user/changePassword">
                <el-dropdown-item>
                  修改密码
                </el-dropdown-item>
              </router-link>
              <el-dropdown-item divided>
                <span style="display:block;" @click="logout">退出登录</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </div>
</template>

<script>
import Hamburger from '@/layout/components/Hamburger'
import Search from '@/layout/components/HeaderSearch'
import defaultAvatar from '@/assets/img/dashboard/avatar.png'
import Setting from './Settings'
import TopMenu from '@/layout/components/TopMenu/topMenu'
import FullScreen from '@/layout/components/FullScreen/fullScreen'
import DarkTheme from '@/layout/components/DarkTheme/darkTheme.vue'

export default {
  components: {
    DarkTheme,
    FullScreen,
    TopMenu,
    Setting,
    Hamburger,
    Search
  },
  computed: {
    device() {
      return this.$store.appStore.device
    },
    sidebar() {
      return this.$store.appStore.sidebar
    },
    avatar() {
      return this.$store.userStore.userInfo.portrait
    },
    topMenu() {
      return this.$store.settingStore.topMenu
    },
    avatarUrl() {
      if (this.avatar === undefined) {
        return defaultAvatar
      }
      return this.avatar
    }
  },
  methods: {
    fullScreen() {
      this.$store.settingStore.changeSetting('fullScreen', {
        key: 'fullScreen',
        value: true
      })
    },
    toggleSideBar() {
      this.$store.appStore.toggleSideBar()
    },
    async logout() {
      await this.$store.userStore.logout()
      this.$router.push(`/login`)
    }
  }
}
</script>

<style lang="scss" scoped>
.router-link-active {
  text-decoration: none;
}
a{
  text-decoration: none;
}

.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
  .topleft{
    // display: flex;
    // align-items: center;
  }
  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color:transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }
  .topmenu-container {
    position: absolute;
    left: 50px;
  }
  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 10px;
    display: flex;
    align-items: center;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 30px;
      color:#6B738D !important;
      vertical-align: text-bottom;

      :deep(.svg-icon){
        width: 23px !important;
        height: 23px !important;
      }

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 30px;
      line-height: 35px;
      .avatar-wrapper {
        margin-top: 5px;
        position: relative;
        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>

