<template>
  <div class="orders-container">
    <!-- Page Header -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">
          <el-icon class="title-icon"><DocumentChecked /></el-icon>
          Sales Orders
        </h1>
        <p class="page-subtitle">Manage customer orders, track status, and process orders efficiently</p>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="handleAddOrder">
          <el-icon><Plus /></el-icon> New Order
        </el-button>
      </div>
    </div>

    <!-- Filters and Search -->
    <div class="filter-section">
      <el-card class="filter-card">
        <div class="filter-grid">
          <el-input
            v-model="searchQuery"
            placeholder="Search orders..."
            prefix-icon="Search"
            clearable
            class="filter-item"
          />
          <el-select
            v-model="statusFilter"
            placeholder="Filter by status"
            clearable
            class="filter-item"
          >
            <el-option label="All Statuses" value="" />
            <el-option label="Created" value="CREATED" />
            <el-option label="Processing" value="PROCESSING" />
            <el-option label="Completed" value="COMPLETED" />
            <el-option label="Cancelled" value="CANCELLED" />
          </el-select>
          <el-button
            type="info"
            @click="resetFilters"
            class="filter-item"
          >
            <el-icon><Refresh /></el-icon> Reset
          </el-button>
        </div>
      </el-card>
    </div>

    <!-- Orders Table -->
    <div v-if="isLoading" class="loading-container">
      <el-skeleton :rows="5" animated />
    </div>
    <div v-else class="table-section">
      <el-card class="table-card">
        <el-table
          :data="filteredOrders"
          style="width: 100%"
          border
          stripe
          :loading="isLoading"
        >
          <el-table-column prop="orderCode" label="Order Code" width="120" />
          <el-table-column prop="customerName" label="Customer" width="150" />
          <el-table-column prop="productCode" label="Product" width="120" />
          <el-table-column prop="quantity" label="Quantity" width="100" />
          <el-table-column prop="status" label="Status" width="120">
            <template #default="scope">
              <el-tag :type="getStatusType(scope.row.status)">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="deliveryDate" label="Delivery Date" width="120" />
          <el-table-column prop="createdAt" label="Created At" width="180" />
          <el-table-column label="Actions" width="150" fixed="right">
            <template #default="scope">
              <el-button
                type="primary"
                size="small"
                @click="handleViewOrder(scope.row)"
                class="action-btn"
              >
                <el-icon><View /></el-icon>
              </el-button>
              <el-button
                type="warning"
                size="small"
                @click="handleEditOrder(scope.row)"
                class="action-btn"
              >
                <el-icon><Edit /></el-icon>
              </el-button>
              <el-button
                type="danger"
                size="small"
                @click="handleDeleteOrder(scope.row.id)"
                class="action-btn"
              >
                <el-icon><Delete /></el-icon>
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        
        <!-- Pagination -->
        <div class="pagination-container">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="filteredOrders.length"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-card>
    </div>

    <!-- Order Dialog -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form
        :model="form"
        :rules="rules"
        ref="formRef"
        label-width="120px"
      >
        <el-form-item label="Customer Name" prop="customerName">
          <el-input v-model="form.customerName" placeholder="Enter customer name" />
        </el-form-item>
        <el-form-item label="Product Code" prop="productCode">
          <el-input v-model="form.productCode" placeholder="Enter product code" />
        </el-form-item>
        <el-form-item label="Quantity" prop="quantity">
          <el-input-number v-model="form.quantity" :min="1" placeholder="Enter quantity" />
        </el-form-item>
        <el-form-item label="Delivery Date" prop="deliveryDate">
          <el-date-picker
            v-model="form.deliveryDate"
            type="date"
            placeholder="Select delivery date"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="Status" prop="status">
          <el-select v-model="form.status" placeholder="Select status">
            <el-option label="Created" value="CREATED" />
            <el-option label="Processing" value="PROCESSING" />
            <el-option label="Completed" value="COMPLETED" />
            <el-option label="Cancelled" value="CANCELLED" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">Cancel</el-button>
          <el-button type="primary" @click="handleSaveOrder">Save</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import { DocumentChecked, Plus, Search, Refresh, View, Edit, Delete } from '@element-plus/icons-vue'
import { ElMessageBox } from 'element-plus'
import { OrderService } from '../api/order'

const isLoading = ref(true)
const searchQuery = ref('')
const statusFilter = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const dialogVisible = ref(false)
const dialogTitle = ref('New Order')
const formRef = ref(null)

const form = ref({
  id: null,
  orderCode: '',
  customerName: '',
  productCode: '',
  quantity: 1,
  status: 'CREATED',
  deliveryDate: null,
  createdAt: ''
})

const rules = {
  customerName: [{ required: true, message: 'Please enter customer name', trigger: 'blur' }],
  productCode: [{ required: true, message: 'Please enter product code', trigger: 'blur' }],
  quantity: [{ required: true, message: 'Please enter quantity', trigger: 'blur' }],
  deliveryDate: [{ required: true, message: 'Please select delivery date', trigger: 'blur' }],
  status: [{ required: true, message: 'Please select status', trigger: 'blur' }]
}

const orders = ref([])

const filteredOrders = computed(() => {
  let result = [...orders.value]
  
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    result = result.filter(order => 
      order.orderCode.toLowerCase().includes(query) ||
      order.customerName.toLowerCase().includes(query) ||
      order.productCode.toLowerCase().includes(query)
    )
  }
  
  if (statusFilter.value) {
    result = result.filter(order => order.status === statusFilter.value)
  }
  
  return result
})

const loadOrders = async () => {
  try {
    isLoading.value = true
    
    // Simulated data - replace with actual API call
    orders.value = [
      { id: 1, orderCode: 'SO0001', customerName: 'ABC Corp', productCode: 'PROD001', quantity: 100, status: 'PROCESSING', deliveryDate: '2024-02-01', createdAt: '2024-01-15 10:30:00' },
      { id: 2, orderCode: 'SO0002', customerName: 'XYZ Inc', productCode: 'PROD002', quantity: 250, status: 'CREATED', deliveryDate: '2024-02-15', createdAt: '2024-01-16 14:20:00' },
      { id: 3, orderCode: 'SO0003', customerName: 'Acme Ltd', productCode: 'PROD001', quantity: 50, status: 'COMPLETED', deliveryDate: '2024-01-30', createdAt: '2024-01-10 09:15:00' },
      { id: 4, orderCode: 'SO0004', customerName: 'Global Co', productCode: 'PROD003', quantity: 150, status: 'PROCESSING', deliveryDate: '2024-02-10', createdAt: '2024-01-18 11:45:00' },
      { id: 5, orderCode: 'SO0005', customerName: 'Tech Corp', productCode: 'PROD002', quantity: 75, status: 'CREATED', deliveryDate: '2024-02-20', createdAt: '2024-01-20 16:30:00' },
      { id: 6, orderCode: 'SO0006', customerName: 'Manufacturing Inc', productCode: 'PROD001', quantity: 300, status: 'COMPLETED', deliveryDate: '2024-01-25', createdAt: '2024-01-05 13:10:00' },
      { id: 7, orderCode: 'SO0007', customerName: 'Industrial Ltd', productCode: 'PROD003', quantity: 120, status: 'PROCESSING', deliveryDate: '2024-02-05', createdAt: '2024-01-12 08:45:00' },
      { id: 8, orderCode: 'SO0008', customerName: 'Tech Solutions', productCode: 'PROD002', quantity: 90, status: 'CREATED', deliveryDate: '2024-02-25', createdAt: '2024-01-22 15:20:00' }
    ]
  } catch (error) {
    console.error('Error loading orders:', error)
  } finally {
    isLoading.value = false
  }
}

const getStatusType = (status) => {
  const types = { CREATED: 'warning', PROCESSING: 'primary', COMPLETED: 'success', CANCELLED: 'danger' }
  return types[status] || 'info'
}

const resetFilters = () => {
  searchQuery.value = ''
  statusFilter.value = ''
  currentPage.value = 1
}

const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
}

const handleCurrentChange = (current) => {
  currentPage.value = current
}

const handleAddOrder = () => {
  form.value = {
    id: null,
    orderCode: '',
    customerName: '',
    productCode: '',
    quantity: 1,
    status: 'CREATED',
    deliveryDate: null,
    createdAt: ''
  }
  dialogTitle.value = 'New Order'
  dialogVisible.value = true
}

const handleEditOrder = (order) => {
  form.value = { ...order }
  dialogTitle.value = 'Edit Order'
  dialogVisible.value = true
}

const handleViewOrder = (order) => {
  console.log('View order:', order)
  // Implement view order functionality
}

const handleDeleteOrder = (id) => {
  ElMessageBox.confirm(
    'Are you sure you want to delete this order?',
    'Warning',
    {
      confirmButtonText: 'Yes',
      cancelButtonText: 'No',
      type: 'warning'
    }
  ).then(() => {
    // Implement delete functionality
    console.log('Delete order:', id)
  }).catch(() => {
    // Cancel operation
  })
}

const handleSaveOrder = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    
    if (form.value.id) {
      // Update existing order
      console.log('Update order:', form.value)
    } else {
      // Create new order
      console.log('Create order:', form.value)
    }
    
    dialogVisible.value = false
    loadOrders()
  } catch (error) {
    console.error('Validation error:', error)
  }
}

onMounted(() => {
  loadOrders()
})
</script>

<style scoped>
.orders-container {
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

.filter-section {
  margin-bottom: 16px;
}

.filter-card {
  border-radius: 12px;
}

.filter-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 16px;
  align-items: end;
}

.filter-item {
  width: 100%;
}

.loading-container {
  min-height: 300px;
}

.table-card {
  border-radius: 12px;
  overflow: hidden;
}

.action-btn {
  margin-right: 8px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.dialog-footer {
  width: 100%;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>
