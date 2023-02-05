<template>
  <div class="app-container">
    <el-row :gutter="6">
      <el-col :span="12">
        <el-card class="box-card">
          <template #header>
            <span>供货厂家</span>
          </template>
          <div class="item">
            <company-view @refresh-sub="refreshSub" />
          </div>
        </el-card>

      </el-col>
      <el-col :span="12">
        <el-card class="box-card">
          <template #header>
            <span>产品管理</span>
          </template>
          <div class="item">
            <product-view ref="form" />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import companyView from './components/companyView'
import productView from './components/productView'

export default {
  name: 'CompanyList',
  components: { companyView, productView },
  methods: {
    refreshSub: function(row) {
      this.$refs.form.refreshSub(row)
    }
  }
}
</script>
