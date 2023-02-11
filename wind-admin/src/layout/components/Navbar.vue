<template>
  <div class="navbar">
    <hamburger id="hamburger-container" :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />
    <top-menu v-if="topMenu" class="topmenu-container" />

    <div class="right-menu">
      <template v-if="device!=='mobile'">
        <search class="right-menu-item" />
        <full-screen class="right-menu-item" />
        <setting class="right-menu-item hover-effect" />
      </template>

      <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
        <div class="avatar-wrapper">
          <img :src="avatarUrl+'?imageView2/1/w/80/h/80'" class="user-avatar" />
          <i class="el-icon-caret-bottom"></i>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <router-link to="/profile/index">
              <el-dropdown-item>
                个人信息
              </el-dropdown-item>
            </router-link>
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
</template>

<script>
import { mapState } from 'pinia'
import Hamburger from '@/layout/components/Hamburger'
import Search from '@/layout/components/HeaderSearch'
import defaultAvatar from '@/assets/img/avatar.png'
import Setting from './Settings'
import TopMenu from '@/layout/components/TopMenu/topMenu'
import { appStore } from '@/stores/modules/app'
import { userStore } from '@/stores/modules/user'
import { settingStore } from '@/stores/modules/settings'
import FullScreen from '@/components/FullScreen/fullScreen'

export default {
  components: {
    FullScreen,
    TopMenu,
    Setting,
    Hamburger,
    Search
  },
  computed: {
    ...mapState(appStore, ['sidebar', 'device']),
    ...mapState(userStore, ['avatar']),
    ...mapState(settingStore, ['topMenu']),
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
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);

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
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

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

