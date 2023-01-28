import { login as userLogin, getInfo as getUserInfo, logout as userLogout } from '@/api/sys/oauth2'
import {
  getRefreshToken,
  getToken,
  removeRefreshToken,
  removeToken,
  setRefreshToken,
  setToken as saveToken
} from '@/utils/auth'
import { defineStore } from 'pinia'
import { resetRouter } from '@/router'
import stores from '@/stores'

export const userStore = defineStore('user', () => {
  let userInfo = {}
  let token = getToken()
  let refreshToken = getRefreshToken()
  let roles = []

  function setToken(token) {
    return new Promise((resolve) => {
      this.token = token
      saveToken(token)
      resolve()
    })
  }

  // user login
  function login(userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      userLogin(username.trim(), password).then(response => {
        const data = response.data
        token = data.access_token
        setToken(response.data.access_token)
        refreshToken = data.refresh_token
        setRefreshToken(response.data.refresh_token)
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
          roles = data.roles
        } else {
          reject(response)
        }
        userInfo = data
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
        token = ''
        refreshToken = ''
        userInfo = {}
        roles = []
        removeToken()
        removeRefreshToken()
        resetRouter()
        stores.permissionStore.resetPermissionsAndRoute()
        stores.tagsViewStore.delAllViews()
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
