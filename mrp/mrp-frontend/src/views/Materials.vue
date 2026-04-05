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
            @keyup.enter="loadMaterials"
          />
          <el-select
            v-model="categoryFilter"
            placeholder="Filter by category"
            clearable
            class="filter-item"
            @change="loadMaterials"
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
        <el-table :data="materials" style="width: 100%" border stripe>
          <el-table-column prop="materialCode" label="Code" width="120" />
          <el-table-column prop="materialName" label="Name" width="200" />
          <el-table-column prop="category" label="Category" width="120">
            <template #default="scope">
              <el-tag :type="getCategoryType(scope.row.category)">{{ scope.row.category }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="unit" label="Unit" width="80" />
          <el-table-column prop="price" label="Price" width="100">
            <template #default="scope">${{ scope.row.price.toFixed(2) }}</template>
          </el-table-column>
          <el-table-column prop="leadTime" label="Lead Time (days)" width="140" />
          <el-table-column prop="status" label="Status" width="100">
            <template #default="scope">
              <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
            </template>
          </el-table-column>
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
            :total="total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
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
        <el-form-item label="Price" prop="price">
          <el-input-number v-model="form.price" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="Lead Time (days)" prop="leadTime">
          <el-input-number v-model="form.leadTime" :min="0" />
        </el-form-item>
        <el-form-item label="Alternative Material" prop="alternativeMaterial">
          <el-input v-model="form.alternativeMaterial" placeholder="Enter alternative material code" />
        </el-form-item>
        <el-form-item label="Status" prop="status">
          <el-select v-model="form.status" placeholder="Select status">
            <el-option label="ACTIVE" value="ACTIVE" />
            <el-option label="INACTIVE" value="INACTIVE" />
          </el-select>
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
import { ref, onMounted } from 'vue'
import { Box, Plus, Search, Refresh, View, Edit, Delete } from '@element-plus/icons-vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import materialApi from '../api/materials'

const isLoading = ref(true)
const searchQuery = ref('')
const categoryFilter = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const materials = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('New Material')
const formRef = ref(null)

const form = ref({
  id: null,
  materialCode: '',
  materialName: '',
  category: 'RAW',
  unit: 'EA',
  price: 0,
  leadTime: 1,
  alternativeMaterial: '',
  status: 'ACTIVE'
})

const rules = {
  materialCode: [{ required: true, message: 'Please enter material code', trigger: 'blur' }],
  materialName: [{ required: true, message: 'Please enter material name', trigger: 'blur' }],
  category: [{ required: true, message: 'Please select category', trigger: 'blur' }],
  unit: [{ required: true, message: 'Please select unit', trigger: 'blur' }],
  price: [{ required: true, message: 'Please enter price', trigger: 'blur' }],
  leadTime: [{ required: true, message: 'Please enter lead time', trigger: 'blur' }],
  status: [{ required: true, message: 'Please select status', trigger: 'blur' }]
}

const loadMaterials = async () => {
  isLoading.value = true
  try {
    const params = {
      page: currentPage.value - 1,
      size: pageSize.value,
      sortBy: 'id',
      direction: 'asc'
    }
    
    const response = await materialApi.getMaterials(params)
    materials.value = response.data.content
    total.value = response.data.totalElements
  } catch (error) {
    console.error('Error loading materials:', error)
    ElMessage.error('Failed to load materials')
  } finally {
    isLoading.value = false
  }
}

const getCategoryType = (category) => {
  const types = { 'RAW': 'info', 'COMPONENT': 'warning', 'FINISHED': 'success' }
  return types[category] || 'info'
}

const getStatusType = (status) => {
  const types = { 'ACTIVE': 'success', 'INACTIVE': 'danger' }
  return types[status] || 'info'
}

const resetFilters = () => {
  searchQuery.value = ''
  categoryFilter.value = ''
  currentPage.value = 1
  loadMaterials()
}

const handleAddMaterial = () => {
  form.value = {
    id: null,
    materialCode: '',
    materialName: '',
    category: 'RAW',
    unit: 'EA',
    price: 0,
    leadTime: 1,
    alternativeMaterial: '',
    status: 'ACTIVE'
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
  // Implement view functionality if needed
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
  ).then(async () => {
    try {
      await materialApi.deleteMaterial(id)
      ElMessage.success('Material deleted successfully')
      loadMaterials()
    } catch (error) {
      console.error('Error deleting material:', error)
      ElMessage.error('Failed to delete material')
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
      await materialApi.updateMaterial(form.value.id, form.value)
      ElMessage.success('Material updated successfully')
    } else {
      // Create new material
      await materialApi.createMaterial(form.value)
      ElMessage.success('Material created successfully')
    }
    
    dialogVisible.value = false
    loadMaterials()
  } catch (error) {
    console.error('Error saving material:', error)
    ElMessage.error('Failed to save material')
  }
}

const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  loadMaterials()
}

const handleCurrentChange = (current) => {
  currentPage.value = current
  loadMaterials()
}

onMounted(() => {
  loadMaterials()
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
