<template>
  <div class="app-container">
    <el-row :gutter="6">
      <el-col :span="12">
        <el-card class="box-card">
          <template #header>
            <span>汽车品牌</span>
          </template>
          <div class="item">
            <car @refresh-car-model="refreshCarModel" />
          </div>
        </el-card>

      </el-col>
      <el-col :span="12">
        <el-card class="box-card">
          <template #header>
            <span>[{{ carName }}]品牌名</span>
          </template>
          <div class="item">
            <car-model ref="carModel" />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import car from './components/car'
import carModel from './components/carModel'

export default {
  name: 'TwoTable',
  components: { car, carModel },
  data() {
    return {
      carName: '未选择'
    }
  },
  methods: {
    refreshCarModel: function(row) {
      console.log('refreshCarModel', row.name)
      this.carName = row.name
      this.$refs.carModel.refreshCarModel(row)
    }
  }
}
</script>
