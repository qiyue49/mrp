<template>
  <div class="inventory-container">
    <!-- Page Header -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">
          <el-icon class="title-icon"><Goods /></el-icon>
          Inventory Management
        </h1>
        <p class="page-subtitle">Track stock levels, material availability, and inventory status</p>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="handleAddInventory">
          <el-icon><Plus /></el-icon> New Inventory
        </el-button>
      </div>
    </div>

    <!-- Filters and Search -->
    <div class="filter-section">
      <el-card class="filter-card">
        <div class="filter-grid">
          <el-input
            v-model="searchQuery"
            placeholder="Search inventory..."
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
            <el-option label="In Stock" value="IN_STOCK" />
            <el-option label="Low Stock" value="LOW_STOCK" />
            <el-option label="Out of Stock" value="OUT_OF_STOCK" />
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

    <!-- Inventory Table -->
    <div v-if="isLoading" class="loading-container">
      <el-skeleton :rows="5" animated />
    </div>
    <div v-else class="table-section">
      <el-card class="table-card">
        <el-table
          :data="filteredInventory"
          style="width: 100%"
          border
          stripe
          :loading="isLoading"
        >
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="materialCode" label="Material Code" width="120" />
          <el-table-column prop="materialName" label="Material Name" width="150" />
          <el-table-column prop="quantity" label="Quantity" width="100" />
          <el-table-column prop="unit" label="Unit" width="80" />
          <el-table-column prop="status" label="Status" width="120">
            <template #default="scope">
              <el-tag :type="getStatusType(scope.row.status)">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="lastUpdated" label="Last Updated" width="180" />
          <el-table-column label="Actions" width="150" fixed="right">
            <template #default="scope">
              <el-button
                type="primary"
                size="small"
                @click="handleViewInventory(scope.row)"
                class="action-btn"
              >
                <el-icon><View /></el-icon>
              </el-button>
              <el-button
                type="warning"
                size="small"
                @click="handleEditInventory(scope.row)"
                class="action-btn"
              >
                <el-icon><Edit /></el-icon>
              </el-button>
              <el-button
                type="danger"
                size="small"
                @click="handleDeleteInventory(scope.row.id)"
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
            :total="filteredInventory.length"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-card>
    </div>

    <!-- Inventory Dialog -->
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
        <el-form-item label="Material Code" prop="materialCode">
          <el-input v-model="form.materialCode" placeholder="Enter material code" />
        </el-form-item>
        <el-form-item label="Material Name" prop="materialName">
          <el-input v-model="form.materialName" placeholder="Enter material name" />
        </el-form-item>
        <el-form-item label="Quantity" prop="quantity">
          <el-input-number v-model="form.quantity" :min="0" placeholder="Enter quantity" />
        </el-form-item>
        <el-form-item label="Unit" prop="unit">
          <el-select v-model="form.unit" placeholder="Select unit">
            <el-option label="EA" value="EA" />
            <el-option label="KG" value="KG" />
            <el-option label="M" value="M" />
            <el-option label="L" value="L" />
            <el-option label="SET" value="SET" />
          </el-select>
        </el-form-item>
        <el-form-item label="Status" prop="status">
          <el-select v-model="form.status" placeholder="Select status">
            <el-option label="In Stock" value="IN_STOCK" />
            <el-option label="Low Stock" value="LOW_STOCK" />
            <el-option label="Out of Stock" value="OUT_OF_STOCK" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">Cancel</el-button>
          <el-button type="primary" @click="handleSaveInventory">Save</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { Plus, Search, Refresh, View, Edit, Delete } from '@element-plus/icons-vue'
import { ElMessageBox } from 'element-plus'
import { InventoryService } from '../api/inventory'

const isLoading = ref(true)
const searchQuery = ref('')
const statusFilter = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const dialogVisible = ref(false)
const dialogTitle = ref('New Inventory')
const formRef = ref(null)

const form = ref({
  id: null,
  materialCode: '',
  materialName: '',
  quantity: 0,
  unit: 'EA',
  status: 'IN_STOCK',
  lastUpdated: ''
})

const rules = {
  materialCode: [{ required: true, message: 'Please enter material code', trigger: 'blur' }],
  materialName: [{ required: true, message: 'Please enter material name', trigger: 'blur' }],
  quantity: [{ required: true, message: 'Please enter quantity', trigger: 'blur' }],
  unit: [{ required: true, message: 'Please select unit', trigger: 'blur' }],
  status: [{ required: true, message: 'Please select status', trigger: 'blur' }]
}

const inventory = ref([])

const filteredInventory = computed(() => {
  let result = [...inventory.value]
  
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    result = result.filter(item => 
      item.materialCode.toLowerCase().includes(query) ||
      item.materialName.toLowerCase().includes(query)
    )
  }
  
  if (statusFilter.value) {
    result = result.filter(item => item.status === statusFilter.value)
  }
  
  return result
})

const loadInventory = async () => {
  try {
    isLoading.value = true
    
    // Simulated data - replace with actual API call
    inventory.value = [
      { id: 1, materialCode: 'MAT001', materialName: 'Steel Plate', quantity: 100, unit: 'EA', status: 'IN_STOCK', lastUpdated: '2024-01-15 10:00:00' },
      { id: 2, materialCode: 'MAT002', materialName: 'Aluminum Sheet', quantity: 50, unit: 'KG', status: 'IN_STOCK', lastUpdated: '2024-01-14 14:30:00' },
      { id: 3, materialCode: 'MAT003', materialName: 'Plastic Resin', quantity: 10, unit: 'KG', status: 'LOW_STOCK', lastUpdated: '2024-01-13 09:15:00' },
      { id: 4, materialCode: 'MAT004', materialName: 'Copper Wire', quantity: 0, unit: 'M', status: 'OUT_OF_STOCK', lastUpdated: '2024-01-12 16:45:00' },
      { id: 5, materialCode: 'MAT005', materialName: 'Electronic Component', quantity: 200, unit: 'EA', status: 'IN_STOCK', lastUpdated: '2024-01-11 11:20:00' },
      { id: 6, materialCode: 'MAT006', materialName: 'Paint', quantity: 5, unit: 'L', status: 'LOW_STOCK', lastUpdated: '2024-01-10 15:50:00' },
      { id: 7, materialCode: 'MAT007', materialName: 'Packaging Material', quantity: 500, unit: 'EA', status: 'IN_STOCK', lastUpdated: '2024-01-09 10:30:00' },
      { id: 8, materialCode: 'MAT008', materialName: 'Screws', quantity: 1000, unit: 'EA', status: 'IN_STOCK', lastUpdated: '2024-01-08 13:15:00' }
    ]
  } catch (error) {
    console.error('Error loading inventory:', error)
  } finally {
    isLoading.value = false
  }
}

const getStatusType = (status) => {
  const types = { IN_STOCK: 'success', LOW_STOCK: 'warning', OUT_OF_STOCK: 'danger' }
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

const handleAddInventory = () => {
  form.value = {
    id: null,
    materialCode: '',
    materialName: '',
    quantity: 0,
    unit: 'EA',
    status: 'IN_STOCK',
    lastUpdated: ''
  }
  dialogTitle.value = 'New Inventory'
  dialogVisible.value = true
}

const handleEditInventory = (item) => {
  form.value = { ...item }
  dialogTitle.value = 'Edit Inventory'
  dialogVisible.value = true
}

const handleViewInventory = (item) => {
  console.log('View inventory:', item)
  // Implement view inventory functionality
}

const handleDeleteInventory = (id) => {
  ElMessageBox.confirm(
    'Are you sure you want to delete this inventory item?',
    'Warning',
    {
      confirmButtonText: 'Yes',
      cancelButtonText: 'No',
      type: 'warning'
    }
  ).then(() => {
    // Implement delete functionality
    console.log('Delete inventory:', id)
  }).catch(() => {
    // Cancel operation
  })
}

const handleSaveInventory = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    
    if (form.value.id) {
      // Update existing inventory
      console.log('Update inventory:', form.value)
    } else {
      // Create new inventory
      console.log('Create inventory:', form.value)
    }
    
    dialogVisible.value = false
    loadInventory()
  } catch (error) {
    console.error('Validation error:', error)
  }
}

onMounted(() => {
  loadInventory()
})
</script>

<style scoped>
.inventory-container {
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
