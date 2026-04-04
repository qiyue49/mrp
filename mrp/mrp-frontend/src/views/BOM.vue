<template>
  <div class="bom-container">
    <!-- Page Header -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">
          <el-icon class="title-icon"><List /></el-icon>
          Bill of Materials (BOM)
        </h1>
        <p class="page-subtitle">Manage product structures, components, and material requirements</p>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="handleAddBOM">
          <el-icon><Plus /></el-icon> New BOM
        </el-button>
      </div>
    </div>

    <!-- Filters and Search -->
    <div class="filter-section">
      <el-card class="filter-card">
        <div class="filter-grid">
          <el-input
            v-model="searchQuery"
            placeholder="Search BOMs..."
            prefix-icon="Search"
            clearable
            class="filter-item"
          />
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

    <!-- BOM Table -->
    <div v-if="isLoading" class="loading-container">
      <el-skeleton :rows="5" animated />
    </div>
    <div v-else class="table-section">
      <el-card class="table-card">
        <el-table
          :data="filteredBOMs"
          style="width: 100%"
          border
          stripe
          :loading="isLoading"
        >
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="productCode" label="Product Code" width="120" />
          <el-table-column prop="materialCode" label="Material Code" width="120" />
          <el-table-column prop="quantity" label="Quantity" width="100" />
          <el-table-column prop="unit" label="Unit" width="80" />
          <el-table-column prop="createdAt" label="Created At" width="180" />
          <el-table-column label="Actions" width="150" fixed="right">
            <template #default="scope">
              <el-button
                type="primary"
                size="small"
                @click="handleViewBOM(scope.row)"
                class="action-btn"
              >
                <el-icon><View /></el-icon>
              </el-button>
              <el-button
                type="warning"
                size="small"
                @click="handleEditBOM(scope.row)"
                class="action-btn"
              >
                <el-icon><Edit /></el-icon>
              </el-button>
              <el-button
                type="danger"
                size="small"
                @click="handleDeleteBOM(scope.row.id)"
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
            :total="filteredBOMs.length"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-card>
    </div>

    <!-- BOM Dialog -->
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
        <el-form-item label="Product Code" prop="productCode">
          <el-input v-model="form.productCode" placeholder="Enter product code" />
        </el-form-item>
        <el-form-item label="Material Code" prop="materialCode">
          <el-input v-model="form.materialCode" placeholder="Enter material code" />
        </el-form-item>
        <el-form-item label="Quantity" prop="quantity">
          <el-input-number v-model="form.quantity" :min="0.01" :step="0.01" placeholder="Enter quantity" />
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
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">Cancel</el-button>
          <el-button type="primary" @click="handleSaveBOM">Save</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { List, Plus, Search, Refresh, View, Edit, Delete } from '@element-plus/icons-vue'
import { ElMessageBox } from 'element-plus'
import { BOMService } from '../api/bom'

const isLoading = ref(true)
const searchQuery = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const dialogVisible = ref(false)
const dialogTitle = ref('New BOM')
const formRef = ref(null)

const form = ref({
  id: null,
  productCode: '',
  materialCode: '',
  quantity: 1,
  unit: 'EA',
  createdAt: ''
})

const rules = {
  productCode: [{ required: true, message: 'Please enter product code', trigger: 'blur' }],
  materialCode: [{ required: true, message: 'Please enter material code', trigger: 'blur' }],
  quantity: [{ required: true, message: 'Please enter quantity', trigger: 'blur' }],
  unit: [{ required: true, message: 'Please select unit', trigger: 'blur' }]
}

const boms = ref([])

const filteredBOMs = computed(() => {
  let result = [...boms.value]
  
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    result = result.filter(bom => 
      bom.productCode.toLowerCase().includes(query) ||
      bom.materialCode.toLowerCase().includes(query)
    )
  }
  
  return result
})

const loadBOMs = async () => {
  try {
    isLoading.value = true
    
    // Simulated data - replace with actual API call
    boms.value = [
      { id: 1, productCode: 'PROD001', materialCode: 'MAT001', quantity: 2, unit: 'EA', createdAt: '2024-01-01 10:00:00' },
      { id: 2, productCode: 'PROD001', materialCode: 'MAT002', quantity: 1, unit: 'KG', createdAt: '2024-01-01 10:00:00' },
      { id: 3, productCode: 'PROD001', materialCode: 'MAT003', quantity: 3, unit: 'EA', createdAt: '2024-01-01 10:00:00' },
      { id: 4, productCode: 'PROD002', materialCode: 'MAT001', quantity: 1, unit: 'EA', createdAt: '2024-01-02 11:00:00' },
      { id: 5, productCode: 'PROD002', materialCode: 'MAT004', quantity: 2, unit: 'M', createdAt: '2024-01-02 11:00:00' },
      { id: 6, productCode: 'PROD003', materialCode: 'MAT002', quantity: 1.5, unit: 'KG', createdAt: '2024-01-03 12:00:00' },
      { id: 7, productCode: 'PROD003', materialCode: 'MAT005', quantity: 4, unit: 'EA', createdAt: '2024-01-03 12:00:00' },
      { id: 8, productCode: 'PROD003', materialCode: 'MAT006', quantity: 0.5, unit: 'L', createdAt: '2024-01-03 12:00:00' }
    ]
  } catch (error) {
    console.error('Error loading BOMs:', error)
  } finally {
    isLoading.value = false
  }
}

const resetFilters = () => {
  searchQuery.value = ''
  currentPage.value = 1
}

const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
}

const handleCurrentChange = (current) => {
  currentPage.value = current
}

const handleAddBOM = () => {
  form.value = {
    id: null,
    productCode: '',
    materialCode: '',
    quantity: 1,
    unit: 'EA',
    createdAt: ''
  }
  dialogTitle.value = 'New BOM'
  dialogVisible.value = true
}

const handleEditBOM = (bom) => {
  form.value = { ...bom }
  dialogTitle.value = 'Edit BOM'
  dialogVisible.value = true
}

const handleViewBOM = (bom) => {
  console.log('View BOM:', bom)
  // Implement view BOM functionality
}

const handleDeleteBOM = (id) => {
  ElMessageBox.confirm(
    'Are you sure you want to delete this BOM?',
    'Warning',
    {
      confirmButtonText: 'Yes',
      cancelButtonText: 'No',
      type: 'warning'
    }
  ).then(() => {
    // Implement delete functionality
    console.log('Delete BOM:', id)
  }).catch(() => {
    // Cancel operation
  })
}

const handleSaveBOM = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    
    if (form.value.id) {
      // Update existing BOM
      console.log('Update BOM:', form.value)
    } else {
      // Create new BOM
      console.log('Create BOM:', form.value)
    }
    
    dialogVisible.value = false
    loadBOMs()
  } catch (error) {
    console.error('Validation error:', error)
  }
}

onMounted(() => {
  loadBOMs()
})
</script>

<style scoped>
.bom-container {
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
