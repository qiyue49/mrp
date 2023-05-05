import { login as userLogin, logout as userLogout } from '@/api/sys/oauth2'
import { getInfo as getUserInfo } from '@/api/sys/user'
import {
  getRefreshToken,
  getToken,
  removeRefreshToken,
  removeToken,
  setRefreshToken as saveRefreshToken,
  setToken as saveToken
} from '@/utils/auth'
import { defineStore } from 'pinia'
import { resetRouter } from '@/router'
import { store } from '@/stores'
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

export const userStore = defineStore('user', () => {
  const userInfo = ref()
  const token = ref(getToken())
  const refreshToken = ref(getRefreshToken())
  const roles = ref([])

  function setToken(tokenParam) {
    token.value = tokenParam
    saveToken(tokenParam)
  }
  function setRefreshToken(tokenParam) {
    refreshToken.value = tokenParam
    saveRefreshToken(tokenParam)
  }

  // user login
  function login(userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      userLogin(username.trim(), password).then(response => {
        const data = response.data
        if (response.data.code !== 0) {
          ElMessage.error(response.data.msg)
        }
        setToken(data.data.accessToken)
        setRefreshToken(data.data.refreshToken)
        resolve(response)
      }).catch(error => {
        reject(error)
      })
    })
  }

  // get user info
  function getInfo() {
    return new Promise((resolve, reject) => {
      getUserInfo(token).then(response => {
        const data = response.data.data

        if (data.roles && data.roles.length > 0) { // 验证返回的roles是否是一个非空数组
          roles.value = data.roles
        } else {
          reject(response)
        }
        userInfo.value = data
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  }

  // user logout
  function logout() {
    return new Promise((resolve, reject) => {
      userLogout(token).then(() => {
        token.value = ''
        refreshToken.value = ''
        userInfo.value = {}
        roles.value = []
        removeToken()
        removeRefreshToken()
        resetRouter()
        store.permissionStore.resetPermissionsAndRoute()
        store.tagsViewStore.delAllViews()
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  }

  // dynamically modify permissions
  // function changeRoles(role) {
  //   return new Promise(resolve => {
  //     const token = role + '-token'
  //
  //     commit('SET_TOKEN', token)
  //     setToken(token)
  //
  //     getInfo().then(({ roles }) => {
  //       resetRouter()
  //
  //       // generate accessible routes map based on roles
  //       dispatch('permission/generateRoutes', roles, { root: true }).then(accessRoutes => {
  //         // dynamically add accessible routes
  //         router.addRoutes(accessRoutes)
  //
  //         // reset visited views and cached views
  //         dispatch('tagsView/delAllViews', null, { root: true })
  //
  //         resolve()
  //       })
  //     })
  //   })
  // }

  return { userInfo, token, refreshToken, roles, login, getInfo, logout, setToken }
})
