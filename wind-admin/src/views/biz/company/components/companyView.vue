<template>
  <div class="app-container">
    <div class="filter-container">
      <div class="filter-item">
        <span>名称:</span>
        <el-input v-model="listQuery.name" placeholder="请输入名称" style="width: 200px" />
      </div>
      <el-button v-waves class="filter-item" type="primary" icon="Search" @click="handleFilter">
        搜索
      </el-button>
      <el-button v-waves v-permission="['biz:company:company:add']" class="filter-item" style="margin-left: 10px;" type="primary" icon="Plus" @click="handleCreate">
        新增
      </el-button>
    </div>

    <el-table
      ref="table"
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @row-click="refreshSub"
    >
      <el-table-column label="名称" min-width="150px">
        <template slot-scope="{row}">
          <span>{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="联系人" min-width="150px">
        <template slot-scope="{row}">
          <span>{{ row.contact }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" min-width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button v-permission="['biz:company:company:update']" type="primary" text size="small" icon="Edit" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button v-permission="['biz:company:company:delete']" type="danger" text size="small" icon="Delete" @click="handleDelete(row,'deleted')">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" :page-sizes="pageArray" @pagination="getList" />

    <company-form ref="form" @refreshList="getList" />

  </div>
</template>

<script>
import { deleteCompany, fetchCompanyList } from '@/api/biz/company/company'
import waves from '@/directive/waves' // waves directive
import permission from '@/directive/permission/permission'
import companyForm from './companyForm'
import Pagination from '@/components/Pagination'

export default {
  name: 'CompanyView',
  components: { companyForm, Pagination },
  directives: { waves, permission },
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
