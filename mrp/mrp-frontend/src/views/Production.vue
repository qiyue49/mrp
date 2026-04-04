<template>
  <div class="production-container">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">
          <el-icon class="title-icon"><Tools /></el-icon>
          Production
        </h1>
        <p class="page-subtitle">Manage production orders and track manufacturing progress</p>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon> New Order
        </el-button>
      </div>
    </div>

    <el-card class="filter-card">
      <el-input v-model="searchQuery" placeholder="Search production orders..." clearable style="width: 300px" />
    </el-card>

    <el-card>
      <el-table :data="filteredOrders" border stripe>
        <el-table-column prop="orderNo" label="Order No" width="150" />
        <el-table-column prop="product" label="Product" />
        <el-table-column prop="quantity" label="Quantity" width="100" />
        <el-table-column prop="status" label="Status" width="120">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="startDate" label="Start Date" width="120" />
        <el-table-column prop="endDate" label="End Date" width="120" />
        <el-table-column label="Actions" width="200">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.row)">Edit</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? 'Edit Production Order' : 'New Production Order'" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="Order No">
          <el-input v-model="form.orderNo" />
        </el-form-item>
        <el-form-item label="Product">
          <el-input v-model="form.product" />
        </el-form-item>
        <el-form-item label="Quantity">
          <el-input-number v-model="form.quantity" :min="1" />
        </el-form-item>
        <el-form-item label="Status">
          <el-select v-model="form.status">
            <el-option label="Planned" value="Planned" />
            <el-option label="In Progress" value="In Progress" />
            <el-option label="Completed" value="Completed" />
            <el-option label="Cancelled" value="Cancelled" />
          </el-select>
        </el-form-item>
        <el-form-item label="Start Date">
          <el-date-picker v-model="form.startDate" type="date" style="width: 100%" />
        </el-form-item>
        <el-form-item label="End Date">
          <el-date-picker v-model="form.endDate" type="date" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="handleSave">Save</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { Tools, Plus } from '@element-plus/icons-vue'

const searchQuery = ref('')
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = ref({ orderNo: '', product: '', quantity: 1, status: 'Planned', startDate: '', endDate: '' })

const orders = ref([
  { orderNo: 'WO001', product: 'Widget A', quantity: 100, status: 'In Progress', startDate: '2024-01-15', endDate: '2024-01-20' },
  { orderNo: 'WO002', product: 'Widget B', quantity: 50, status: 'Planned', startDate: '2024-01-18', endDate: '2024-01-25' },
  { orderNo: 'WO003', product: 'Widget C', quantity: 200, status: 'Completed', startDate: '2024-01-10', endDate: '2024-01-14' },
  { orderNo: 'WO004', product: 'Widget A', quantity: 75, status: 'Planned', startDate: '2024-01-22', endDate: '2024-01-28' }
])

const filteredOrders = computed(() => {
  if (!searchQuery.value) return orders.value
  return orders.value.filter(o => 
    o.product.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
    o.orderNo.toLowerCase().includes(searchQuery.value.toLowerCase())
  )
})

const getStatusType = (status) => {
  const types = { 'Planned': 'info', 'In Progress': 'warning', 'Completed': 'success', 'Cancelled': 'danger' }
  return types[status] || 'info'
}

const handleAdd = () => {
  isEdit.value = false
  form.value = { orderNo: '', product: '', quantity: 1, status: 'Planned', startDate: '', endDate: '' }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  form.value = { ...row }
  dialogVisible.value = true
}

const handleDelete = (row) => {
  const index = orders.value.indexOf(row)
  if (index > -1) orders.value.splice(index, 1)
}

const handleSave = () => {
  if (isEdit.value) {
    const index = orders.value.findIndex(o => o.orderNo === form.value.orderNo)
    if (index > -1) orders.value[index] = { ...form.value }
  } else {
    orders.value.push({ ...form.value })
  }
  dialogVisible.value = false
}
</script>

<style scoped>
.production-container { padding: 20px; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-title { display: flex; align-items: center; gap: 10px; font-size: 24px; font-weight: bold; }
.title-icon { color: #409EFF; }
.filter-card { margin-bottom: 20px; }
</style>
