<template>
  <div id="dictComponent">
    <div>
      <div class="filter-container">
        <div class="filter-item">
          <el-input v-model="listQuery.keyword" placeholder="请输入汽车型号标签或值" @keyup.enter="handleFilter" />
        </div>
        <el-button class="filter-item" type="primary" icon="Search" @click="handleFilter">搜索</el-button>
        <el-button class="filter-item" type="primary" icon="Plus" @click="handleCreate">新增</el-button>
      </div>

      <el-table
        :key="tableKey"
        v-loading="listLoading"
        :data="list"
        fit
        highlight-current-row
        style="width: 100%"
        header-cell-class-name="header-cell"
      >
        <el-table-column
          prop="name"
          label="汽车型号标签"
          width="160"
        />
        <el-table-column
          prop="value"
          label="汽车型号值"
          width="160"
        />
        <el-table-column label="操作" width="180">
          <template #default="scope">
            <el-button v-permission="['test:twotable:car:detail']" size="small" plain type="primary" icon="EditPen" @click="handleUpdate(scope.row)">编辑</el-button>
            <el-button v-permission="['test:twotable:car:delete']" size="small" plain type="danger" icon="Delete" @click="handleDelete(scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" v-model:page="listQuery.page" v-model:limit="listQuery.limit" :total="total" :page-sizes="pageArray" @pagination="getList" />

      <car-model-form ref="form" @get-list="getList" />
    </div>
  </div>
</template>

<script>
import { fetchCarModelList, deleteCarModel } from '@/api/demo/twoTable/carModel'
import carModelForm from './carModelForm'
import Pagination from '@/components/Pagination/index.vue'

export default {
  name: 'CarModel',
  components: { Pagination, carModelForm },
  data() {
    return {
      dictGroup: {},
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: false,
      pageArray: this.$store.dictStore.pageArray,
      listQuery: {
        page: 1,
        limit: this.$store.dictStore.defaultPageSize,
        title: undefined,
        type: undefined,
        carId: undefined
      },
      showReviewer: false,
      downloadLoading: false
    }
  },
  methods: {
    getList() {
      this.listLoading = true
      fetchCarModelList(this.listQuery).then(response => {
        this.list = response.data.data
        this.total = response.data.total
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleModifyStatus(row, status) {
      this.$message({
        message: '操作成功',
        type: 'success'
      })
      row.status = status
    },
    refreshCarModel(row) {
      this.listQuery.carId = row.id
      this.listQuery.page = 1
      this.listQuery.keyword = ''
      this.dictGroup = row
      this.getList()
    },
    handleUpdate(row) {
      this.$refs.form.handleUpdate(row.id)
    },
    handleCreate() {
      this.$refs.form.handleCreate(this.dictGroup)
    },
    handleDelete(row) {
      this.$confirm('确定删除该数据吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteCarModel(row.id).then((response) => {
          const data = response.data
          if (data.code === 0) {
            this.dialogFormVisible = false
            this.$message.success('删除成功')
            this.getList()
          } else {
            this.$message.error(data.msg)
          }
        })
      })
    }

  }
}
</script>
