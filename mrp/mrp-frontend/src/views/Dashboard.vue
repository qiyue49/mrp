<template>
  <div class="dashboard-container">
    <!-- Page Header -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">
          <el-icon class="title-icon"><HomeFilled /></el-icon>
          Dashboard
        </h1>
        <p class="page-subtitle">MRP System Overview - Real-time Manufacturing Intelligence</p>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="refreshData" :loading="isRefreshing">
          <el-icon><Refresh /></el-icon> Refresh
        </el-button>
      </div>
    </div>

    <!-- KPI Cards -->
    <div v-if="isLoading" class="loading-container">
      <el-skeleton :rows="2" animated />
    </div>
    <div v-else class="kpi-grid">
      <el-card class="kpi-card" :body-style="{ padding: '0' }">
        <div class="kpi-content blue">
          <div class="kpi-icon"><el-icon><ShoppingCart /></el-icon></div>
          <div class="kpi-info">
            <h3>{{ stats.orders }}</h3>
            <p>Sales Orders</p>
            <span class="kpi-trend positive">+12% vs last month</span>
          </div>
        </div>
      </el-card>

      <el-card class="kpi-card" :body-style="{ padding: '0' }">
        <div class="kpi-content green">
          <div class="kpi-icon"><el-icon><Memo /></el-icon></div>
          <div class="kpi-info">
            <h3>{{ stats.production }}</h3>
            <p>Production Orders</p>
            <span class="kpi-trend positive">+8% vs last month</span>
          </div>
        </div>
      </el-card>

      <el-card class="kpi-card" :body-style="{ padding: '0' }">
        <div class="kpi-content purple">
          <div class="kpi-icon"><el-icon><Goods /></el-icon></div>
          <div class="kpi-info">
            <h3>{{ stats.inventory }}</h3>
            <p>Inventory Items</p>
            <span class="kpi-trend neutral">3 low stock items</span>
          </div>
        </div>
      </el-card>

      <el-card class="kpi-card" :body-style="{ padding: '0' }">
        <div class="kpi-content orange">
          <div class="kpi-icon"><el-icon><DataAnalysis /></el-icon></div>
          <div class="kpi-info">
            <h3>{{ stats.bom }}</h3>
            <p>BOMs</p>
            <span class="kpi-trend positive">All up to date</span>
          </div>
        </div>
      </el-card>
    </div>

    <!-- Tables Row -->
    <div class="tables-row">
      <el-card class="table-card">
        <template #header>
          <div class="card-header">
            <span><el-icon><Clock /></el-icon> Recent Orders</span>
            <el-button text type="primary" @click="$router.push('/orders')">View All</el-button>
          </div>
        </template>
        <el-table :data="recentOrders" size="small" style="width: 100%">
          <el-table-column prop="orderCode" label="Order" width="100" />
          <el-table-column prop="customerName" label="Customer" width="120" />
          <el-table-column prop="quantity" label="Qty" width="70" />
          <el-table-column prop="status" label="Status" width="100">
            <template #default="scope">
              <el-tag :type="getStatusType(scope.row.status)" size="small">{{ scope.row.status }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="deliveryDate" label="Delivery" width="110" />
        </el-table>
      </el-card>

      <el-card class="table-card">
        <template #header>
          <div class="card-header">
            <span><el-icon><Warning /></el-icon> Alerts & Notifications</span>
          </div>
        </template>
        <div class="alerts-list">
          <div v-for="(alert, index) in alerts" :key="index" class="alert-item" :class="alert.type">
            <el-icon class="alert-icon"><Warning v-if="alert.type === 'warning'" /><CircleCheck v-if="alert.type === 'success'" /><InfoFilled v-if="alert.type === 'info'" /></el-icon>
            <div class="alert-content">
              <p class="alert-title">{{ alert.title }}</p>
              <p class="alert-desc">{{ alert.description }}</p>
            </div>
            <span class="alert-time">{{ alert.time }}</span>
          </div>
        </div>
      </el-card>
    </div>

    <!-- MRP Status -->
    <el-card class="mrp-status-card">
      <template #header>
        <div class="card-header">
          <span><el-icon><DataAnalysis /></el-icon> MRP System Status</span>
          <el-tag :type="systemStatus === 'Online' ? 'success' : 'danger'">{{ systemStatus }}</el-tag>
        </div>
      </template>
      <div class="status-grid">
        <div class="status-item">
          <div class="status-label">Last MRP Run</div>
          <div class="status-value">{{ lastMRPRun }}</div>
        </div>
        <div class="status-item">
          <div class="status-label">Planned Orders</div>
          <div class="status-value">{{ plannedOrders }}</div>
        </div>
        <div class="status-item">
          <div class="status-label">Purchase Requisitions</div>
          <div class="status-value">{{ purchaseReqs }}</div>
        </div>
        <div class="status-item">
          <div class="status-label">Inventory Accuracy</div>
          <div class="status-value">{{ inventoryAccuracy }}%</div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { HomeFilled, ShoppingCart, Memo, Goods, DataAnalysis, Refresh, Clock, Warning, CircleCheck, InfoFilled } from '@element-plus/icons-vue'

const router = useRouter()
const isLoading = ref(true)
const isRefreshing = ref(false)

const stats = ref({
  orders: 0,
  production: 0,
  inventory: 0,
  bom: 0
})

const recentOrders = ref([])
const systemStatus = ref('Online')
const lastMRPRun = ref('2024-01-15 09:30:00')
const plannedOrders = ref(12)
const purchaseReqs = ref(8)
const inventoryAccuracy = ref(98.5)

const alerts = ref([
  { type: 'warning', title: 'Low Stock Alert', description: 'MAT0001 (Steel Plate) below safety stock', time: '2h ago' },
  { type: 'info', title: 'PO Approved', description: 'PO00005 approved and sent to supplier', time: '4h ago' },
  { type: 'success', title: 'WO Completed', description: 'WO00012 production completed successfully', time: '6h ago' },
  { type: 'warning', title: 'Delivery Delay', description: 'Supplier SUP0003 reported 2-day delay', time: '1d ago' },
])

const loadData = async () => {
  try {
    isLoading.value = true
    
    // Simulated data - replace with actual API calls
    stats.value = {
      orders: 25,
      production: 18,
      inventory: 156,
      bom: 32
    }
    
    recentOrders.value = [
      { orderCode: 'SO0001', customerName: 'ABC Corp', quantity: 100, status: 'PROCESSING', deliveryDate: '2024-02-01' },
      { orderCode: 'SO0002', customerName: 'XYZ Inc', quantity: 250, status: 'CREATED', deliveryDate: '2024-02-15' },
      { orderCode: 'SO0003', customerName: 'Acme Ltd', quantity: 50, status: 'COMPLETED', deliveryDate: '2024-01-30' },
      { orderCode: 'SO0004', customerName: 'Global Co', quantity: 150, status: 'PROCESSING', deliveryDate: '2024-02-10' },
      { orderCode: 'SO0005', customerName: 'Tech Corp', quantity: 75, status: 'CREATED', deliveryDate: '2024-02-20' }
    ]
  } catch (error) {
    console.error('Error loading dashboard data:', error)
  } finally {
    isLoading.value = false
  }
}

const refreshData = async () => {
  isRefreshing.value = true
  await loadData()
  isRefreshing.value = false
}

const getStatusType = (status) => {
  const types = { CREATED: 'warning', PROCESSING: 'primary', COMPLETED: 'success', CANCELLED: 'danger' }
  return types[status] || 'info'
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.dashboard-container {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 4px;
}

.header-content {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.page-title {
  font-size: 28px;
  font-weight: 700;
  color: #111827;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 12px;
}

.title-icon {
  color: #1a56db;
}

.page-subtitle {
  font-size: 14px;
  color: #6b7280;
  margin: 0;
}

.loading-container {
  min-height: 120px;
}

.kpi-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
}

.kpi-card {
  border-radius: 12px;
  overflow: hidden;
  transition: transform 0.2s, box-shadow 0.2s;
}

.kpi-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
}

.kpi-content {
  display: flex;
  align-items: center;
  padding: 24px;
  gap: 16px;
}

.kpi-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: white;
}

.kpi-content.blue .kpi-icon { background: linear-gradient(135deg, #1a56db, #1e429f); }
.kpi-content.green .kpi-icon { background: linear-gradient(135deg, #0e9f6e, #057a55); }
.kpi-content.purple .kpi-icon { background: linear-gradient(135deg, #7e3af2, #6c2bd9); }
.kpi-content.orange .kpi-icon { background: linear-gradient(135deg, #ff5a1f, #d03801); }

.kpi-info h3 {
  font-size: 32px;
  font-weight: 700;
  margin: 0;
  color: #111827;
}

.kpi-info p {
  font-size: 14px;
  color: #6b7280;
  margin: 4px 0 8px 0;
}

.kpi-trend {
  font-size: 12px;
  font-weight: 500;
  padding: 2px 8px;
  border-radius: 12px;
}

.kpi-trend.positive { color: #0e9f6e; background: #def7ec; }
.kpi-trend.warning { color: #c27803; background: #fdf6b2; }
.kpi-trend.neutral { color: #6b7280; background: #f3f4f6; }

.tables-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 20px;
}

.table-card {
  border-radius: 12px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
}

.alerts-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.alert-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 12px;
  border-radius: 8px;
  background: #f9fafb;
}

.alert-item.warning { background: #fffbeb; border-left: 3px solid #f59e0b; }
.alert-item.success { background: #f0fdf4; border-left: 3px solid #0e9f6e; }
.alert-item.info { background: #eff6ff; border-left: 3px solid #1a56db; }

.alert-icon {
  font-size: 20px;
  margin-top: 2px;
}

.alert-item.warning .alert-icon { color: #f59e0b; }
.alert-item.success .alert-icon { color: #0e9f6e; }
.alert-item.info .alert-icon { color: #1a56db; }

.alert-content {
  flex: 1;
}

.alert-title {
  font-weight: 600;
  font-size: 14px;
  margin: 0 0 4px 0;
  color: #111827;
}

.alert-desc {
  font-size: 13px;
  color: #6b7280;
  margin: 0;
}

.alert-time {
  font-size: 12px;
  color: #9ca3af;
}

.mrp-status-card {
  border-radius: 12px;
}

.status-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 24px;
}

.status-item {
  text-align: center;
  padding: 16px;
  border-radius: 8px;
  background: #f9fafb;
}

.status-label {
  font-size: 13px;
  color: #6b7280;
  margin-bottom: 8px;
}

.status-value {
  font-size: 20px;
  font-weight: 700;
  color: #111827;
}
</style>
