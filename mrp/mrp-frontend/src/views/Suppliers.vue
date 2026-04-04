<template>
  <div class="suppliers-container">
    <!-- Page Header -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">
          <el-icon class="title-icon"><OfficeBuilding /></el-icon>
          Suppliers
        </h1>
        <p class="page-subtitle">Manage supplier information and relationships</p>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="handleAddSupplier">
          <el-icon><Plus /></el-icon> New Supplier
        </el-button>
      </div>
    </div>

    <!-- Filters -->
    <div class="filter-section">
      <el-card class="filter-card">
        <div class="filter-grid">
          <el-input
            v-model="searchQuery"
            placeholder="Search suppliers..."
            prefix-icon="Search"
            clearable
            class="filter-item"
          />
          <el-button type="info" @click="resetFilters" class="filter-item">
            <el-icon><Refresh /></el-icon> Reset
          </el-button>
        </div>
      </el-card>
    </div>

    <!-- Suppliers Table -->
    <div v-if="isLoading" class="loading-container">
      <el-skeleton :rows="5" animated />
    </div>
    <div v-else class="table-section">
      <el-card class="table-card">
        <el-table :data="filteredSuppliers" style="width: 100%" border stripe>
          <el-table-column prop="supplierCode" label="Code" width="120" />
          <el-table-column prop="supplierName" label="Name" width="200" />
          <el-table-column prop="contactPerson" label="Contact" width="150" />
          <el-table-column prop="phone" label="Phone" width="150" />
          <el-table-column prop="email" label="Email" />
          <el-table-column prop="address" label="Address" width="300" />
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
            :total="filteredSuppliers.length"
          />
        </div>
      </el-card>
    </div>

    <!-- Supplier Dialog -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
        <el-form-item label="Supplier Code" prop="supplierCode">
          <el-input v-model="form.supplierCode" placeholder="Enter supplier code" />
        </el-form-item>
        <el-form-item label="Supplier Name" prop="supplierName">
          <el-input v-model="form.supplierName" placeholder="Enter supplier name" />
        </el-form-item>
        <el-form-item label="Contact Person" prop="contactPerson">
          <el-input v-model="form.contactPerson" placeholder="Enter contact person" />
        </el-form-item>
        <el-form-item label="Phone" prop="phone">
          <el-input v-model="form.phone" placeholder="Enter phone number" />
        </el-form-item>
        <el-form-item label="Email" prop="email">
          <el-input v-model="form.email" placeholder="Enter email address" />
        </el-form-item>
        <el-form-item label="Address" prop="address">
          <el-input v-model="form.address" type="textarea" placeholder="Enter address" />
        </el-form-item>
        <el-form-item label="Status" prop="status">
          <el-select v-model="form.status" placeholder="Select status">
            <el-option label="Active" value="ACTIVE" />
            <el-option label="Inactive" value="INACTIVE" />
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
import { ref, computed, onMounted } from 'vue'
import { OfficeBuilding, Plus, Search, Refresh, View, Edit, Delete } from '@element-plus/icons-vue'
import { ElMessageBox, ElMessage } from 'element-plus'

const isLoading = ref(true)
const searchQuery = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const dialogVisible = ref(false)
const dialogTitle = ref('New Supplier')
const formRef = ref(null)

const form = ref({
  id: null,
  supplierCode: '',
  supplierName: '',
  contactPerson: '',
  phone: '',
  email: '',
  address: '',
  status: 'ACTIVE'
})

const rules = {
  supplierCode: [{ required: true, message: 'Please enter supplier code', trigger: 'blur' }],
  supplierName: [{ required: true, message: 'Please enter supplier name', trigger: 'blur' }],
  contactPerson: [{ required: true, message: 'Please enter contact person', trigger: 'blur' }],
  phone: [{ required: true, message: 'Please enter phone number', trigger: 'blur' }],
  email: [{ required: true, message: 'Please enter email address', trigger: 'blur' }]
}

const suppliers = ref([
  { id: 1, supplierCode: 'SUP001', supplierName: 'Steel Corp', contactPerson: 'John Smith', phone: '555-0101', email: 'john@steelcorp.com', address: '123 Steel Street, Industrial Park, City', status: 'ACTIVE' },
  { id: 2, supplierCode: 'SUP002', supplierName: 'Aluminum Inc', contactPerson: 'Jane Doe', phone: '555-0102', email: 'jane@aluminum.com', address: '456 Aluminum Avenue, Industrial Zone, City', status: 'ACTIVE' },
  { id: 3, supplierCode: 'SUP003', supplierName: 'Plastic Co', contactPerson: 'Bob Johnson', phone: '555-0103', email: 'bob@plasticco.com', address: '789 Plastic Road, Industrial Estate, City', status: 'ACTIVE' },
  { id: 4, supplierCode: 'SUP004', supplierName: 'Copper Inc', contactPerson: 'Alice Brown', phone: '555-0104', email: 'alice@copper.com', address: '321 Copper Lane, Industrial Area, City', status: 'ACTIVE' },
  { id: 5, supplierCode: 'SUP005', supplierName: 'Tech Electronics', contactPerson: 'David Lee', phone: '555-0105', email: 'david@techelectronics.com', address: '654 Electronics Drive, Tech Park, City', status: 'ACTIVE' },
  { id: 6, supplierCode: 'SUP006', supplierName: 'Hardware Inc', contactPerson: 'Sarah Chen', phone: '555-0106', email: 'sarah@hardware.com', address: '987 Hardware Boulevard, Industrial Complex, City', status: 'INACTIVE' },
  { id: 7, supplierCode: 'SUP007', supplierName: 'Paint Co', contactPerson: 'Michael Wang', phone: '555-0107', email: 'michael@paintco.com', address: '159 Paint Street, Industrial Park, City', status: 'ACTIVE' },
  { id: 8, supplierCode: 'SUP008', supplierName: 'Packaging Solutions', contactPerson: 'Lisa Zhang', phone: '555-0108', email: 'lisa@packaging.com', address: '753 Packaging Avenue, Industrial Zone, City', status: 'ACTIVE' }
])

const filteredSuppliers = computed(() => {
  let result = [...suppliers.value]
  
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    result = result.filter(supplier => 
      supplier.supplierName.toLowerCase().includes(query) ||
      supplier.supplierCode.toLowerCase().includes(query) ||
      supplier.contactPerson.toLowerCase().includes(query)
    )
  }
  
  return result
})

const getStatusType = (status) => {
  const types = { 'ACTIVE': 'success', 'INACTIVE': 'danger' }
  return types[status] || 'info'
}

const resetFilters = () => {
  searchQuery.value = ''
  currentPage.value = 1
}

const handleAddSupplier = () => {
  form.value = {
    id: null,
    supplierCode: '',
    supplierName: '',
    contactPerson: '',
    phone: '',
    email: '',
    address: '',
    status: 'ACTIVE'
  }
  dialogTitle.value = 'New Supplier'
  dialogVisible.value = true
}

const handleEdit = (supplier) => {
  form.value = { ...supplier }
  dialogTitle.value = 'Edit Supplier'
  dialogVisible.value = true
}

const handleView = (supplier) => {
  console.log('View supplier:', supplier)
  // Implement view functionality
}

const handleDelete = (id) => {
  ElMessageBox.confirm(
    'Are you sure you want to delete this supplier?',
    'Warning',
    {
      confirmButtonText: 'Yes',
      cancelButtonText: 'No',
      type: 'warning'
    }
  ).then(() => {
    const index = suppliers.value.findIndex(s => s.id === id)
    if (index > -1) {
      suppliers.value.splice(index, 1)
      ElMessage.success('Supplier deleted successfully')
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
      // Update existing supplier
      const index = suppliers.value.findIndex(s => s.id === form.value.id)
      if (index > -1) {
        suppliers.value[index] = { ...form.value }
        ElMessage.success('Supplier updated successfully')
      }
    } else {
      // Create new supplier
      form.value.id = suppliers.value.length + 1
      suppliers.value.push({ ...form.value })
      ElMessage.success('Supplier created successfully')
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
.suppliers-container {
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
