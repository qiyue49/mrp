<template>
  <div id="carComponent">
    <div>
      <div class="filter-container">
        <div class="filter-item">
          <el-input v-model="listQuery.keyword" placeholder="请输入品牌名称或编码" @keyup.enter="handleFilter" />
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
        @row-click="refreshCarModel"
      >
        <el-table-column
          prop="name"
          label="品牌名称"
          width="160"
        />
        <el-table-column
          prop="code"
          label="品牌编码"
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

      <car-form ref="form" @get-list="getList" />
    </div>
  </div>
</template>

<script>
import { fetchCarList, deleteCar } from '@/api/demo/twoTable/car'
import carForm from './carForm'
import Pagination from '@/components/Pagination/index.vue'

export default {
  name: 'Car',
  components: { Pagination, carForm },
  emits: ['refreshCarModel'],
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      pageArray: this.$store.dictStore.pageArray,
      listQuery: {
        page: 1,
        limit: this.$store.dictStore.defaultPageSize,
        title: undefined,
        type: undefined
      },
      showReviewer: false
    }
  },
  created() {
    this.getList()
  },
  methods: {
    refreshCarModel(row) {
      this.$emit('refreshCarModel', row)
    },
    getList() {
      this.listLoading = true
      fetchCarList(this.listQuery).then(response => {
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
    handleUpdate(row) {
      this.$refs.form.handleUpdate(row.id)
    },
    handleCreate() {
      this.$refs.form.handleCreate()
    },
    handleDelete(row) {
      this.$confirm('确定删除该数据吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteCar(row.id).then((response) => {
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
