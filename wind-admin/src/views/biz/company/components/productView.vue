<template>
  <div class="app-container">
    <div class="filter-container">
      <el-button v-permission="['biz:company:company:add']" class="filter-item" type="primary" icon="Plus" @click="handleCreate">
        新增
      </el-button>
    </div>

    <el-table
      ref="table"
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      fit
      highlight-current-row
      style="width: 100%;"
      header-cell-class-name="header-cell"
    >
      <el-table-column label="名称" min-width="150px">
        <template #default="{row}">
          <span>{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="价格" min-width="150px">
        <template #default="{row}">
          <span>{{ row.price }}</span>
        </template>
      </el-table-column>
      <el-table-column label="数量" min-width="150px">
        <template #default="{row}">
          <span>{{ row.count }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" min-width="230">
        <template #default="{row}">
          <el-button v-permission="['biz:company:company:update']" type="primary" icon="EditPen" plain size="small" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button v-permission="['biz:company:company:delete']" plain type="danger" icon="Delete" size="small" @click="handleDelete(row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" v-model:page="listQuery.page" v-model:limit="listQuery.limit" :total="total" :page-sizes="pageArray" @pagination="getList" />

    <product-form ref="form" @refresh-list="getList" />

  </div>
</template>

<script>
import { deleteProduct, fetchProductList } from '@/api/biz/company/product'
import productForm from './productForm'
import Pagination from '@/components/Pagination'
export default {
  name: 'ProductView',
  components: { productForm, Pagination },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: false,
      pageArray: this.$store.dictStore.pageArray,
      listQuery: {
        page: 1,
        limit: this.$store.dictStore.defaultPageSize
      }
    }
  },
  methods: {
    refreshSub(row) {
      this.listQuery.companyId = row.id
      this.listQuery.page = 1
      this.listQuery.keyword = ''
      this.getList()
    },
    getList() {
      this.listLoading = true
      fetchProductList(this.listQuery).then(response => {
        this.list = response.data.data
        this.total = response.data.total
        this.listLoading = false
      })
    },
    handleCreate() {
      if (this.isNull(this.listQuery.companyId)) {
        this.$message.warning('请先选择一条上级数据')
        return
      }
      this.$refs.form.handleCreate(this.listQuery.companyId)
    },
    handleUpdate(row) {
      this.$refs.form.handleUpdate(row.id)
    },
    handleDelete(row) {
      this.$confirm('确定删除该数据吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteProduct(row.id).then(response => {
          if (response.data.code === 0) {
            this.getList()
            this.$message.success(response.data.msg)
          } else {
            this.$message.error(response.data.msg)
          }
        })
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    }

  }
}
</script>
