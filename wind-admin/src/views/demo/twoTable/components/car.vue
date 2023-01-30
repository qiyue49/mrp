<template>
  <div id="carComponent">
    <div>
      <div class="filter-container">
        <el-input v-model="listQuery.keyword" style="width: 200px;" class="filter-item" placeholder="请输入品牌名称或编码" @keyup.enter.native="handleFilter" />
        <el-button v-waves class="filter-item" type="primary" icon="Search" @click="handleFilter">搜索</el-button>
        <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="Plus" @click="handleCreate">新增</el-button>
      </div>

      <el-table
        :key="tableKey"
        v-loading="listLoading"
        :data="list"
        element-loading-text="给我一点时间"
        border
        fit
        highlight-current-row
        style="width: 100%"
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
        <el-table-column :label="操作" align="center" width="180" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-button v-permission="['test:car:car:detail']" size="small" type="primary" text icon="Edit" @click="handleUpdate(scope.row)">编辑</el-button>
            <el-button v-permission="['test:car:car:delete']" size="small" type="danger" text icon="Delete" @click="handleDelete(scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          :current-page.sync="listQuery.page"
          :page-sizes="pageArray"
          :page-size="listQuery.limit"
          :total="total"
          background
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
      <car-form ref="form" @getList="getList" />
    </div>
  </div>
</template>

<script>
import { fetchCarList, deleteCar } from '@/api/demo/twoTable/car'
import permission from '@/directive/permission/permission'
import waves from '@/directive/waves' // 水波纹指令
import carForm from './carForm' // 水波纹指令

export default {
  name: 'Car',
  directives: {
    waves, permission
  },
  components: { carForm },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: null,
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
    handleSizeChange(val) {
      this.listQuery.limit = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.listQuery.page = val
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
    }

  }
}
</script>
