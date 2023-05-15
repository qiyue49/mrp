<template>
  <div class="app-container">
    <div class="filter-container">
      <div class="filter-item">
        <span>名称:</span>
        <el-input v-model="listQuery.name" placeholder="请输入名称" />
      </div>
      <el-button class="filter-item" type="primary" icon="Search" @click="handleFilter">
        搜索
      </el-button>
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
      @row-click="refreshSub"
    >
      <el-table-column label="名称" min-width="150px">
        <template #default="{row}">
          <span>{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="联系人" min-width="150px">
        <template #default="{row}">
          <span>{{ row.contact }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" min-width="230">
        <template #default="{row}">
          <el-button v-permission="['biz:company:company:update']" plain type="primary" icon="EditPen" size="small" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button v-permission="['biz:company:company:delete']" plain type="danger" icon="Delete" size="small" @click="handleDelete(row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" v-model:page="listQuery.page" v-model:limit="listQuery.limit" :total="total" :page-sizes="pageArray" @pagination="getList" />

    <company-form ref="form" @refresh-list="getList" />

  </div>
</template>

<script>
import { deleteCompany, fetchCompanyList } from '@/api/biz/company/company'
import companyForm from './companyForm'
import Pagination from '@/components/Pagination'
export default {
  name: 'CompanyView',
  components: { companyForm, Pagination },
  emits: ['refreshSub'],
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      pageArray: this.$store.dictStore.pageArray,
      listQuery: {
        name: undefined,
        page: 1,
        limit: this.$store.dictStore.defaultPageSize
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    refreshSub(row) {
      this.$emit('refreshSub', row)
    },
    getList() {
      this.listLoading = true
      fetchCompanyList(this.listQuery).then(response => {
        this.list = response.data.data
        this.total = response.data.total
        this.listLoading = false
      })
    },
    handleCreate() {
      this.$refs.form.handleCreate()
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
        deleteCompany(row.id).then(response => {
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
