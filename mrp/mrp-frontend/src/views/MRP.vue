<template>
  <div class="mrp-container">
    <!-- Page Header -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">
          <el-icon class="title-icon"><DataAnalysis /></el-icon>
          MRP Calculation
        </h1>
        <p class="page-subtitle">Generate material requirements, production plans, and purchase recommendations</p>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="runMRP" :loading="isRunningMRP">
          <el-icon><Refresh /></el-icon> Run MRP
        </el-button>
      </div>
    </div>

    <!-- MRP Run Parameters -->
    <div class="params-section">
      <el-card class="params-card">
        <div class="params-grid">
          <el-form :model="mrpParams" label-width="120px">
            <el-form-item label="Planning Horizon">
              <el-date-picker
                v-model="mrpParams.planningHorizon"
                type="date"
                placeholder="Select planning horizon"
                style="width: 100%"
              />
            </el-form-item>
            <el-form-item label="Include Safety Stock">
              <el-switch v-model="mrpParams.includeSafetyStock" />
            </el-form-item>
            <el-form-item label="Consider Lead Time">
              <el-switch v-model="mrpParams.considerLeadTime" />
            </el-form-item>
          </el-form>
        </div>
      </el-card>
    </div>

    <!-- MRP Results -->
    <div v-if="isLoading" class="loading-container">
      <el-skeleton :rows="5" animated />
    </div>
    <div v-else class="results-section">
      <!-- Material Requirements -->
      <el-card class="result-card">
        <template #header>
          <div class="card-header">
            <span><el-icon><Goods /></el-icon> Material Requirements</span>
          </div>
        </template>
        <el-table :data="mrpResults.materialRequirements" style="width: 100%" border stripe>
          <el-table-column prop="materialCode" label="Material Code" width="120" />
          <el-table-column prop="materialName" label="Material Name" width="150" />
          <el-table-column prop="requiredQuantity" label="Required Qty" width="100" />
          <el-table-column prop="currentStock" label="Current Stock" width="100" />
          <el-table-column prop="netRequirement" label="Net Req" width="100" />
          <el-table-column prop="leadTime" label="Lead Time (days)" width="120" />
        </el-table>
      </el-card>

      <!-- Production Plans -->
      <el-card class="result-card">
        <template #header>
          <div class="card-header">
            <span><el-icon><Tools /></el-icon> Production Plans</span>
          </div>
        </template>
        <el-table :data="mrpResults.productionPlans" style="width: 100%" border stripe>
          <el-table-column prop="productCode" label="Product Code" width="120" />
          <el-table-column prop="productName" label="Product Name" width="150" />
          <el-table-column prop="quantity" label="Quantity" width="100" />
          <el-table-column prop="startDate" label="Start Date" width="120" />
          <el-table-column prop="endDate" label="End Date" width="120" />
        </el-table>
      </el-card>

      <!-- Purchase Recommendations -->
      <el-card class="result-card">
        <template #header>
          <div class="card-header">
            <span><el-icon><ShoppingBag /></el-icon> Purchase Recommendations</span>
          </div>
        </template>
        <el-table :data="mrpResults.purchaseRecommendations" style="width: 100%" border stripe>
          <el-table-column prop="materialCode" label="Material Code" width="120" />
          <el-table-column prop="materialName" label="Material Name" width="150" />
          <el-table-column prop="recommendedQuantity" label="Recommended Qty" width="120" />
          <el-table-column prop="supplier" label="Supplier" width="150" />
          <el-table-column prop="estimatedCost" label="Estimated Cost" width="120" />
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { DataAnalysis, Refresh, Goods, Tools, ShoppingBag } from '@element-plus/icons-vue'

const isLoading = ref(false)
const isRunningMRP = ref(false)

const mrpParams = ref({
  planningHorizon: new Date(),
  includeSafetyStock: true,
  considerLeadTime: true
})

const mrpResults = ref({
  materialRequirements: [],
  productionPlans: [],
  purchaseRecommendations: []
})

const runMRP = async () => {
  try {
    isRunningMRP.value = true
    isLoading.value = true
    
    // Simulate MRP calculation
    await new Promise(resolve => setTimeout(resolve, 1500))
    
    // Simulated results
    mrpResults.value = {
      materialRequirements: [
        { materialCode: 'MAT001', materialName: 'Steel Plate', requiredQuantity: 200, currentStock: 100, netRequirement: 100, leadTime: 7 },
        { materialCode: 'MAT002', materialName: 'Aluminum Sheet', requiredQuantity: 150, currentStock: 50, netRequirement: 100, leadTime: 5 },
        { materialCode: 'MAT003', materialName: 'Plastic Resin', requiredQuantity: 50, currentStock: 10, netRequirement: 40, leadTime: 3 },
        { materialCode: 'MAT004', materialName: 'Copper Wire', requiredQuantity: 100, currentStock: 0, netRequirement: 100, leadTime: 4 }
      ],
      productionPlans: [
        { productCode: 'PROD001', productName: 'Widget A', quantity: 100, startDate: '2024-01-20', endDate: '2024-01-25' },
        { productCode: 'PROD002', productName: 'Widget B', quantity: 50, startDate: '2024-01-22', endDate: '2024-01-27' },
        { productCode: 'PROD003', productName: 'Widget C', quantity: 75, startDate: '2024-01-25', endDate: '2024-01-30' }
      ],
      purchaseRecommendations: [
        { materialCode: 'MAT001', materialName: 'Steel Plate', recommendedQuantity: 100, supplier: 'Steel Co', estimatedCost: 1500 },
        { materialCode: 'MAT002', materialName: 'Aluminum Sheet', recommendedQuantity: 100, supplier: 'Aluminum Inc', estimatedCost: 2000 },
        { materialCode: 'MAT003', materialName: 'Plastic Resin', recommendedQuantity: 40, supplier: 'Plastic Co', estimatedCost: 800 },
        { materialCode: 'MAT004', materialName: 'Copper Wire', recommendedQuantity: 100, supplier: 'Copper Inc', estimatedCost: 1200 }
      ]
    }
  } catch (error) {
    console.error('Error running MRP:', error)
  } finally {
    isRunningMRP.value = false
    isLoading.value = false
  }
}

onMounted(() => {
  // Load initial data if needed
})
</script>

<style scoped>
.mrp-container {
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

.params-section {
  margin-bottom: 16px;
}

.params-card {
  border-radius: 12px;
}

.params-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 16px;
}

.loading-container {
  min-height: 300px;
}

.results-section {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.result-card {
  border-radius: 12px;
  overflow: hidden;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
}
</style>
