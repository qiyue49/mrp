<template>
  <div class="app-container">
    <el-container style="height: 100vh">
      <el-header height="60px" class="header">
        <div class="header-content">
          <div class="logo-section">
            <el-icon class="logo-icon"><Setting /></el-icon>
            <h1 class="logo">MRP System</h1>
          </div>
          <div class="user-info">
            <el-dropdown>
              <span class="user-dropdown">
                <el-avatar size="small" :src="userAvatar"></el-avatar>
                <span class="user-name">{{ userName }}</span>
                <el-icon class="el-icon--right"><ArrowDown /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="handleLogout">
                    <el-icon><SwitchButton /></el-icon>
                    <span>Logout</span>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </el-header>
      <el-container>
        <el-aside width="220px" class="sidebar">
          <el-menu
            :default-active="activeMenu"
            class="menu"
            router
            :unique-opened="true"
            :collapse="isCollapse"
            :collapse-transition="false"
          >
            <el-menu-item index="/dashboard">
              <el-icon><HomeFilled /></el-icon>
              <span>Dashboard</span>
            </el-menu-item>
            
            <el-sub-menu index="sales">
              <template #title>
                <el-icon><ShoppingCart /></el-icon>
                <span>Sales</span>
              </template>
              <el-menu-item index="/orders">
                <el-icon><DocumentChecked /></el-icon>
                <span>Orders</span>
              </el-menu-item>
            </el-sub-menu>
            
            <el-sub-menu index="master">
              <template #title>
                <el-icon><Collection /></el-icon>
                <span>Master Data</span>
              </template>
              <el-menu-item index="/materials">
                <el-icon><Box /></el-icon>
                <span>Materials</span>
              </el-menu-item>
              <el-menu-item index="/bom">
                <el-icon><List /></el-icon>
                <span>BOM</span>
              </el-menu-item>
              <el-menu-item index="/suppliers">
                <el-icon><OfficeBuilding /></el-icon>
                <span>Suppliers</span>
              </el-menu-item>
            </el-sub-menu>
            
            <el-sub-menu index="inventory">
              <template #title>
                <el-icon><Goods /></el-icon>
                <span>Inventory</span>
              </template>
              <el-menu-item index="/inventory">
                <el-icon><Goods /></el-icon>
                <span>Stock Levels</span>
              </el-menu-item>
            </el-sub-menu>
            
            <el-sub-menu index="production">
              <template #title>
                <el-icon><Tools /></el-icon>
                <span>Production</span>
              </template>
              <el-menu-item index="/production">
                <el-icon><Memo /></el-icon>
                <span>Production</span>
              </el-menu-item>
              <el-menu-item index="/mrp">
                <el-icon><DataAnalysis /></el-icon>
                <span>MRP Calculation</span>
              </el-menu-item>
            </el-sub-menu>
            
            <el-menu-item index="/rocketmq">
              <el-icon><Message /></el-icon>
              <span>RocketMQ</span>
            </el-menu-item>
          </el-menu>
          
          <div class="sidebar-footer">
            <el-button 
              class="collapse-btn" 
              :icon="isCollapse ? Expand : Fold" 
              @click="toggleCollapse"
              text
            />
          </div>
        </el-aside>
        <el-main class="main-content">
          <router-view v-slot="{ Component }">
            <transition name="fade" mode="out-in">
              <component :is="Component" />
            </transition>
          </router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import {
  HomeFilled,
  ShoppingCart,
  DocumentChecked,
  Collection,
  Box,
  List,
  OfficeBuilding,
  Goods,
  Tools,
  Memo,
  DataAnalysis,
  Message,
  ArrowDown,
  SwitchButton,
  Setting,
  Expand,
  Fold
} from '@element-plus/icons-vue'

const route = useRoute()
const userName = ref('Admin')
const userAvatar = ref('https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png')
const isCollapse = ref(false)

const activeMenu = computed(() => {
  return route.path
})

const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}

const handleLogout = () => {
  console.log('Logout')
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.app-container {
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  height: 100vh;
}

.header {
  background: linear-gradient(135deg, #1a56db, #1e429f);
  color: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  z-index: 100;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
  padding: 0 24px;
}

.logo-section {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-icon {
  font-size: 28px;
  color: #60a5fa;
}

.logo {
  font-size: 22px;
  font-weight: 700;
  letter-spacing: -0.5px;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-dropdown {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 8px;
  transition: background-color 0.2s;
}

.user-dropdown:hover {
  background-color: rgba(255, 255, 255, 0.15);
}

.user-name {
  font-size: 14px;
  font-weight: 500;
}

.sidebar {
  background-color: #ffffff;
  border-right: 1px solid #e5e7eb;
  display: flex;
  flex-direction: column;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.05);
}

.menu {
  flex: 1;
  border-right: none;
  padding: 12px 0;
}

.menu .el-menu-item,
.menu .el-sub-menu__title {
  height: 48px;
  line-height: 48px;
  margin: 4px 12px;
  border-radius: 8px;
}

.menu .el-menu-item.is-active {
  background-color: #eff6ff;
  color: #1a56db;
  font-weight: 600;
}

.menu .el-menu-item:hover,
.menu .el-sub-menu__title:hover {
  background-color: #f3f4f6;
}

.sidebar-footer {
  padding: 12px;
  border-top: 1px solid #e5e7eb;
  display: flex;
  justify-content: center;
}

.collapse-btn {
  color: #6b7280;
}

.main-content {
  background-color: #f9fafb;
  padding: 24px;
  overflow-y: auto;
}

/* Transitions */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* Scrollbar styling */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>
