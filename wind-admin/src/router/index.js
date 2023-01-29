import { createRouter, createWebHashHistory, createWebHistory } from 'vue-router'
import { getToken } from '@/utils/auth'
import store from '@/stores'
import { ElMessage } from 'element-plus'

export const Layout = () => import('@/layout/index.vue')

export const constantMenus = [
  {
    path: '/dashboard',
    component: () => import('@/views/dashboard/index'),
    name: 'dashboard',
    meta: { title: '首页', icon: 'el-icon-menu', affix: true }
  }

]
export const constantRoutes = [
  {
    path: '/redirect',
    hidden: true,
    children: [
      {
        path: '/redirect/:path*',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/whiteList',
    component: () => import('@/views/demo/whiteList/whiteList'),
    hidden: true
  },
  {
    path: '/auth-redirect',
    component: () => import('@/views/login/auth-redirect'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error-page/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error-page/401'),
    hidden: true
  },
  {
    path: '/',
    redirect: '/dashboard',
    hidden: true
  },
  {
    path: '/dashboard',
    component: Layout,
    children: [
      {
        path: '',
        component: () => import('@/views/dashboard/index'),
        name: 'dashboard',
        meta: { title: '首页', icon: 'el-icon-menu', affix: true }
      }
    ]
  },
  {
    path: '/profile/index',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '',
        component: () => import('@/views/profile/index'),
        name: 'Profile',
        meta: { title: '个人中心', icon: 'el-icon-user', noCache: true }
      }
    ]
  },
  {
    path: '/sys/user/info',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '',
        component: () => import('@/views/sys/user/info'),
        name: 'Info',
        meta: { title: '修改信息', icon: 'el-icon-user', noCache: true }
      }
    ]
  },
  {
    path: '/sys/user/changePassword',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '',
        component: () => import('@/views/sys/user/changePassword'),
        name: 'ChangePassword',
        meta: { title: '修改密码', icon: 'el-icon-user', noCache: true }
      }
    ]
  },
  { path: '/:path(.*)', redirect: '/404' }
]

// Nginx打包路由模式可以使hash也可以是history，但是tomcat打包只能是hash
const createFun = createWebHistory
// const createFun = createWebHashHistory
const router = createRouter({
  history: import.meta.env.MODE === 'tomcat' ? createWebHashHistory(import.meta.env.BASE_URL) : createFun(import.meta.env.BASE_URL),
  routes: constantRoutes,
  scrollBehavior: () => ({ left: 0, top: 0 })
})

const whiteList = ['/login', '/auth-redirect', '/whiteList'] // no redirect whitelist

router.beforeEach(async(to, from, next) => {
  // set page title
  document.title = to.meta.title

  // determine whether the user has logged in
  const hasToken = getToken()

  if (hasToken && whiteList.indexOf(to.path) === -1) {
    if (to.path === '/login') {
      // if is logged in, redirect to the home page
      // next({ path: '/' })
    } else {
      if (store.userStore.userInfo === undefined) { // 判断当前用户是否已拉取完user_info信息
        await store.userStore.getInfo()
        // 加载数据字典
        store.dictStore.initDict()
        // 获取系统配置
        store.sysConfigStore.getConfig()
        // 获取路由
        if (store.permissionStore.fetchRoutes.length === 0) { // 判断是否获取路由
          await store.permissionStore.getRoutes()
          if (store.permissionStore.fetchRoutes.length === 0) {
            ElMessage.error('你的账号权限存在问题')
            next('/login') // 否则全部重定向到登录页
            return
          }
          store.permissionStore.addRoutes.forEach(item => {
            router.addRoute(item) // 动态添加可访问路由表
          })
          // 获取权限
          await store.permissionStore.getPermissions()
          if (store.permissionStore.permissions.length === 0) {
            ElMessage.error('你的账号权限存在问题')
            next('/login') // 否则全部重定向到登录页
          } else {
            next('/dashboard')
          }
        }
      } else {
        next()
      }
    }
  } else {
    /* has no token*/

    if (whiteList.indexOf(to.path) !== -1) {
      // in the free login whitelist, go directly
      next()
    } else {
      // other pages that do not have permission to access are redirected to the login page.
      next(`/login`)
    }
  }
})

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
