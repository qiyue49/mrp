<template>
  <div class="app-container">
    <el-row :gutter="6">
      <el-col :span="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>供货厂家</span>
          </div>
          <div class="item">
            <company-view @refreshSub="refreshSub" />
          </div>
        </el-card>

      </el-col>
      <el-col :span="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>产品管理</span>
          </div>
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
