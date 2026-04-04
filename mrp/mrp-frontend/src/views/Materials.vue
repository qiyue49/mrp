<template>
  <div class="materials-container">
    <!-- Page Header -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">
          <el-icon class="title-icon"><Box /></el-icon>
          Materials
        </h1>
        <p class="page-subtitle">Manage raw materials, components, and product specifications</p>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="handleAddMaterial">
          <el-icon><Plus /></el-icon> New Material
        </el-button>
      </div>
    </div>

    <!-- Filters -->
    <div class="filter-section">
      <el-card class="filter-card">
        <div class="filter-grid">
          <el-input
            v-model="searchQuery"
            placeholder="Search materials..."
            prefix-icon="Search"
            clearable
            class="filter-item"
          />
          <el-select
            v-model="categoryFilter"
            placeholder="Filter by category"
            clearable
            class="filter-item"
          >
            <el-option label="Raw Material" value="RAW" />
            <el-option label="Component" value="COMPONENT" />
            <el-option label="Finished Goods" value="FINISHED" />
          </el-select>
          <el-button type="info" @click="resetFilters" class="filter-item">
            <el-icon><Refresh /></el-icon> Reset
          </el-button>
        </div>
      </el-card>
    </div>

    <!-- Materials Table -->
    <div v-if="isLoading" class="loading-container">
      <el-skeleton :rows="5" animated />
    </div>
    <div v-else class="table-section">
      <el-card class="table-card">
        <el-table :data="filteredMaterials" style="width: 100%" border stripe>
          <el-table-column prop="materialCode" label="Code" width="120" />
          <el-table-column prop="materialName" label="Name" width="200" />
          <el-table-column prop="category" label="Category" width="120">
            <template #default="scope">
              <el-tag :type="getCategoryType(scope.row.category)">{{ scope.row.category }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="unit" label="Unit" width="80" />
          <el-table-column prop="cost" label="Cost" width="100">
            <template #default="scope">${{ scope.row.cost }}</template>
          </el-table-column>
          <el-table-column prop="leadTime" label="Lead Time (days)" width="140" />
          <el-table-column prop="supplier" label="Supplier" width="150" />
          <el-table-column label="Actions" width="150" fixed="right">
            <template #default="scope">
              <el-button type="primary" size="small" @click="handleView(scope.row)">
                <el-icon><View /></el-icon>
              </el-button>
              <el-button type="warning" size="small" @click="handleEdit(scope.row)">
                <el-icon><Edit /></el-icon>
              </el-button>
              <el-button type="danger" size="small" @click="handleDelete(scope.row.id)">
                <el-icon><Delete /></el-icon>
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <div class="pagination-container">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="filteredMaterials.length"
          />
        </div>
      </el-card>
    </div>

    <!-- Material Dialog -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
        <el-form-item label="Material Code" prop="materialCode">
          <el-input v-model="form.materialCode" placeholder="Enter material code" />
        </el-form-item>
        <el-form-item label="Material Name" prop="materialName">
          <el-input v-model="form.materialName" placeholder="Enter material name" />
        </el-form-item>
        <el-form-item label="Category" prop="category">
          <el-select v-model="form.category" placeholder="Select category">
            <el-option label="Raw Material" value="RAW" />
            <el-option label="Component" value="COMPONENT" />
            <el-option label="Finished Goods" value="FINISHED" />
          </el-select>
        </el-form-item>
        <el-form-item label="Unit" prop="unit">
          <el-select v-model="form.unit" placeholder="Select unit">
            <el-option label="EA" value="EA" />
            <el-option label="KG" value="KG" />
            <el-option label="M" value="M" />
            <el-option label="L" value="L" />
          </el-select>
        </el-form-item>
        <el-form-item label="Cost" prop="cost">
          <el-input-number v-model="form.cost" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="Lead Time (days)" prop="leadTime">
          <el-input-number v-model="form.leadTime" :min="0" />
        </el-form-item>
        <el-form-item label="Supplier" prop="supplier">
          <el-input v-model="form.supplier" placeholder="Enter supplier name" />
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
import { ref, computed, onMounted } from 'vue'
import { Box, Plus, Search, Refresh, View, Edit, Delete } from '@element-plus/icons-vue'
import { ElMessageBox, ElMessage } from 'element-plus'

const isLoading = ref(true)
const searchQuery = ref('')
const categoryFilter = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const dialogVisible = ref(false)
const dialogTitle = ref('New Material')
const formRef = ref(null)

const form = ref({
  id: null,
  materialCode: '',
  materialName: '',
  category: 'RAW',
  unit: 'EA',
  cost: 0,
  leadTime: 1,
  supplier: ''
})

const rules = {
  materialCode: [{ required: true, message: 'Please enter material code', trigger: 'blur' }],
  materialName: [{ required: true, message: 'Please enter material name', trigger: 'blur' }],
  category: [{ required: true, message: 'Please select category', trigger: 'blur' }],
  unit: [{ required: true, message: 'Please select unit', trigger: 'blur' }]
}

const materials = ref([
  { id: 1, materialCode: 'MAT001', materialName: 'Steel Plate', category: 'RAW', unit: 'KG', cost: 15.50, leadTime: 7, supplier: 'Steel Corp' },
  { id: 2, materialCode: 'MAT002', materialName: 'Aluminum Sheet', category: 'RAW', unit: 'KG', cost: 20.00, leadTime: 5, supplier: 'Aluminum Inc' },
  { id: 3, materialCode: 'MAT003', materialName: 'Plastic Resin', category: 'RAW', unit: 'KG', cost: 8.00, leadTime: 3, supplier: 'Plastic Co' },
  { id: 4, materialCode: 'MAT004', materialName: 'Copper Wire', category: 'COMPONENT', unit: 'M', cost: 2.50, leadTime: 4, supplier: 'Copper Inc' },
  { id: 5, materialCode: 'MAT005', materialName: 'Electronic Board', category: 'COMPONENT', unit: 'EA', cost: 25.00, leadTime: 2, supplier: 'Tech Electronics' },
  { id: 6, materialCode: 'MAT006', materialName: 'Plastic Housing', category: 'COMPONENT', unit: 'EA', cost: 3.50, leadTime: 3, supplier: 'Plastic Co' },
  { id: 7, materialCode: 'MAT007', materialName: 'Screws', category: 'COMPONENT', unit: 'EA', cost: 0.10, leadTime: 1, supplier: 'Hardware Inc' },
  { id: 8, materialCode: 'MAT008', materialName: 'Paint', category: 'RAW', unit: 'L', cost: 12.00, leadTime: 2, supplier: 'Paint Co' }
])

const filteredMaterials = computed(() => {
  let result = [...materials.value]
  
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    result = result.filter(material => 
      material.materialName.toLowerCase().includes(query) ||
      material.materialCode.toLowerCase().includes(query)
    )
  }
  
  if (categoryFilter.value) {
    result = result.filter(material => material.category === categoryFilter.value)
  }
  
  return result
})

const getCategoryType = (category) => {
  const types = { 'RAW': 'info', 'COMPONENT': 'warning', 'FINISHED': 'success' }
  return types[category] || 'info'
}

const resetFilters = () => {
  searchQuery.value = ''
  categoryFilter.value = ''
  currentPage.value = 1
}

const handleAddMaterial = () => {
  form.value = {
    id: null,
    materialCode: '',
    materialName: '',
    category: 'RAW',
    unit: 'EA',
    cost: 0,
    leadTime: 1,
    supplier: ''
  }
  dialogTitle.value = 'New Material'
  dialogVisible.value = true
}

const handleEdit = (material) => {
  form.value = { ...material }
  dialogTitle.value = 'Edit Material'
  dialogVisible.value = true
}

const handleView = (material) => {
  console.log('View material:', material)
  // Implement view functionality
}

const handleDelete = (id) => {
  ElMessageBox.confirm(
    'Are you sure you want to delete this material?',
    'Warning',
    {
      confirmButtonText: 'Yes',
      cancelButtonText: 'No',
      type: 'warning'
    }
  ).then(() => {
    const index = materials.value.findIndex(m => m.id === id)
    if (index > -1) {
      materials.value.splice(index, 1)
      ElMessage.success('Material deleted successfully')
    }
  }).catch(() => {
    // Cancel operation
  })
}

const handleSave = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    
    if (form.value.id) {
      // Update existing material
      const index = materials.value.findIndex(m => m.id === form.value.id)
      if (index > -1) {
        materials.value[index] = { ...form.value }
        ElMessage.success('Material updated successfully')
      }
    } else {
      // Create new material
      form.value.id = materials.value.length + 1
      materials.value.push({ ...form.value })
      ElMessage.success('Material created successfully')
    }
    
    dialogVisible.value = false
  } catch (error) {
    console.error('Validation error:', error)
  }
}

onMounted(() => {
  isLoading.value = false
})
</script>

<style scoped>
.materials-container {
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

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
