import { constantRoutes, constantMenus } from '@/router'
import { getMenus, fetchPermissionList } from '@/api/sys/menu'
import store from '../index'
import Layout from '@/layout/index.vue'
import { isNull } from '@/utils'
import { isExternal } from '@/utils/validate'
import { defineStore } from 'pinia'

export const permissionStore = defineStore('permission', () => {
  let routes = []
  let menus = []
  let addMenus = []
  let addRoutes = []
  let fetchRoutes = []
  let permissions = []

  function filterAsyncRoutes(routes) {
    let res = []
    routes.forEach(route => {
      const tmp = { ...route }
      if (tmp.children) {
        res = res.concat(filterAsyncRoutes(tmp.children))
      } else {
        if (isNull(tmp.path)) {
          console.log('tmp', tmp)
          return res
        }
        if (isExternal(tmp.path)) {
          return
        }
        const attr = tmp.path.split('?')
        const query = []
        if (attr.length > 1) {
          tmp.path = attr[0]
          attr[1].split('&').forEach(item => {
            const pair = item.split('=')
            query[pair[0]] = pair[1]
          })
        }
        const item = {
          component: Layout,
          path: '/',
          // meta: { title: tmp.meta.title, affix: tmp.meta.affix },
          children: [
            {
              path: tmp.path,
              name: tmp.name,
              meta: { title: tmp.meta.title, affix: tmp.meta.affix, query }
            }
          ]
        }

        const component = tmp.component
        try {
          if (component) {
            item.children[0].component = _import(component)
          } else {
            item.children[0].component = undefined
          }
        } catch (e) {
          item.children[0].component = () => import('@/views/errorPage/404')
        }
        res.push(item)
      }
    })
    return res
  }

  function setRoutes(routes) {
    addRoutes = routes
    routes = constantRoutes.concat(routes)
  }
  function toggleMenu() {
    if (store.settingStore.topMenu) {
      for (let i = 0; i < fetchRoutes.length; i++) {
        const accessedMenusKey = fetchRoutes[i]
        if (accessedMenusKey.children && accessedMenusKey.children.length !== 0) {
          menus = fetchRoutes[i].children
        }
      }
    } else {
      menus = constantMenus.concat(fetchRoutes)
    }
  }
  function updateMenu(menu) {
    const result = addMenus.find(item => {
      return item.id === menu.id
    })
    menus = result.children
  }

  // 获取未认证路由
  function getRoutes() {
    return new Promise((resolve, reject) => {
      getMenus(store.userStore.token).then(response => {
        if (fetchRoutes.length === 0) {
          // console.log('response.data.data', response.data.data)
          fetchRoutes = response.data.data
          const accessedRouters = filterAsyncRoutes(response.data.data)
          // console.log('accessedRouters', accessedRouters)
          setRoutes(accessedRouters)
          const accessedMenus = response.data.data
          // console.log('accessedMenus', accessedMenus)
          if (store.state.settings.topMenu) {
            for (let i = 0; i < accessedMenus.length; i++) {
              const accessedMenusKey = accessedMenus[i]
              if (accessedMenusKey.children && accessedMenusKey.children.length !== 0) {
                menus = accessedMenus[0].children
              }
            }
          } else {
            menus = constantMenus.concat(accessedMenus)
          }
          addMenus = accessedMenus
        }
        resolve(response)
      }).catch(error => {
        reject(error)
      })
    })
  }
  // 获取权限
  function getPermissions() {
    return new Promise((resolve, reject) => {
      fetchPermissionList(store.userStore.token).then(response => {
        permissions = response.data.data
        resolve(response)
      }).catch(error => {
        reject(error)
      })
    })
  }
  function resetPermissionsAndRoute() {
    // console.log('ResetPermissions')
    permissions = []
    addRoutes = []
    routes = constantRoutes
    fetchRoutes = []
    menus = []
    addMenus = []
  }
  return { routes, fetchRoutes, addRoutes, menus, addMenus, permissions, toggleMenu, updateMenu, getPermissions, getRoutes, getMenus, resetPermissionsAndRoute }
})

