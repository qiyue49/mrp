import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/dashboard'
    },
    {
      path: '/dashboard',
      name: 'dashboard',
      component: () => import('../views/Dashboard.vue')
    },
    {
      path: '/orders',
      name: 'orders',
      component: () => import('../views/Orders.vue')
    },
    {
      path: '/bom',
      name: 'bom',
      component: () => import('../views/BOM.vue')
    },
    {
      path: '/inventory',
      name: 'inventory',
      component: () => import('../views/Inventory.vue')
    },
    {
      path: '/mrp',
      name: 'mrp',
      component: () => import('../views/MRP.vue')
    },
    {
      path: '/rocketmq',
      name: 'rocketmq',
      component: () => import('../views/RocketMQ.vue')
    },
    {
      path: '/materials',
      name: 'materials',
      component: () => import('../views/Materials.vue')
    },
    {
      path: '/suppliers',
      name: 'suppliers',
      component: () => import('../views/Suppliers.vue')
    },
    {
      path: '/production',
      name: 'production',
      component: () => import('../views/Production.vue')
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'not-found',
      component: () => import('../views/NotFound.vue')
    }
  ]
})

export default router
