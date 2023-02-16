import { constantRoutes, constantMenus } from '@/router'
import { getMenus, fetchPermissionList } from '@/api/sys/menu'
import { store } from '../index'
import { isNull } from '@/utils'
import { isExternal } from '@/utils/validate'
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const Layout = () => import('@/layout/index.vue')
const modules = import.meta.glob('../../views/**/**.vue')

export const permissionStore = defineStore('permission', () => {
  const routes = ref([])
  const menus = ref([])
  const addMenus = ref([])
  const addRoutes = ref([])
  const fetchRoutes = ref([])
  const permissions = ref([])

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
            const componentFile = modules[`../../${tmp.component}.vue`]
            item.children[0].component = componentFile
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

  function setRoutes(routesParam) {
    addRoutes.value = routesParam
    routes.value = constantRoutes.concat(routesParam)
  }
  function toggleMenu() {
    if (store.settingStore.topMenu) {
      for (let i = 0; i < fetchRoutes.value.length; i++) {
        const accessedMenusKey = fetchRoutes.value[i]
        if (accessedMenusKey.children && accessedMenusKey.children.length !== 0) {
          menus.value = fetchRoutes.value[i].children
        }
      }
    } else {
      menus.value = constantMenus.concat(fetchRoutes.value)
    }
  }
  function updateMenu(menu) {
    const result = addMenus.value.find(item => {
      return item.id === menu.id
    })
    menus.value = result.children
  }

  // 获取未认证路由
  function getRoutes() {
    return new Promise((resolve, reject) => {
      getMenus(store.userStore.token).then(response => {
        if (fetchRoutes.value.length === 0) {
          // console.log('response.data.data', response.data.data)
          fetchRoutes.value = response.data.data
          const accessedRouters = filterAsyncRoutes(response.data.data)
          // console.log('accessedRouters', accessedRouters)
          setRoutes(accessedRouters)
          const accessedMenus = response.data.data
          // console.log('accessedMenus', accessedMenus)
          if (store.settingStore.topMenu) {
            for (let i = 0; i < accessedMenus.length; i++) {
              const accessedMenusKey = accessedMenus[i]
              if (accessedMenusKey.children && accessedMenusKey.children.length !== 0) {
                menus.value = accessedMenus[0].children
              }
            }
          } else {
            menus.value = constantMenus.concat(accessedMenus)
          }
          addMenus.value = accessedMenus
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
        permissions.value = response.data.data
        resolve(response)
      }).catch(error => {
        reject(error)
      })
    })
  }
  function resetPermissionsAndRoute() {
    // console.log('ResetPermissions')
    permissions.value = []
    addRoutes.value = []
    routes.value = constantRoutes
    fetchRoutes.value = []
    menus.value = []
    addMenus.value = []
  }
  return { routes, fetchRoutes, addRoutes, menus, addMenus, permissions, toggleMenu, updateMenu, getPermissions, getRoutes, getMenus, resetPermissionsAndRoute }
})

