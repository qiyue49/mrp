<template>
  <el-card class="el-card" >
    <div class="app-container">
      <div class="filter-container">
        <div class="filter-item">
          <span>租户标识:</span>
          <el-input v-model="listQuery.tenantId" placeholder="请输入租户标识" />
        </div>
        <div class="filter-item">
          <span>联系人:</span>
          <el-input v-model="listQuery.contact" placeholder="请输入联系人" />
        </div>
        <div class="filter-item">
          <span>电话:</span>
          <el-input v-model="listQuery.phone" placeholder="请输入电话" />
        </div>
        <div class="filter-item">
          <span>租户名称:</span>
          <el-input v-model="listQuery.name" placeholder="请输入租户名称" />
        </div>
        <el-button class="filter-item" type="primary" icon="Search" @click="handleFilter">
          搜索
        </el-button>
        <el-button v-permission="['sys:tenant:add']" class="filter-item" type="primary" icon="Plus" @click="handleCreate">
          新增
        </el-button>
      </div>

      <el-table
        :key="tableKey"
        v-loading="listLoading"
        :data="list"
        fit
        highlight-current-row
        header-cell-class-name="header-cell">
        <el-table-column label="租户标识" min-width="150px">
          <template #default="{row}">
            <span>{{ row.tenantId }}</span>
          </template>
        </el-table-column>
        <el-table-column label="联系人" min-width="150px">
          <template #default="{row}">
            <span>{{ row.contact }}</span>
          </template>
        </el-table-column>
        <el-table-column label="电话" min-width="150px">
          <template #default="{row}">
            <span>{{ row.phone }}</span>
          </template>
        </el-table-column>
        <el-table-column label="租户名称" min-width="150px">
          <template #default="{row}">
            <span>{{ row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="登录名" min-width="150px">
          <template #default="{row}">
            <span>{{ row.userName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="230">
          <template #default="{row}">
            <el-button v-permission="['sys:tenant:update']" size="small" plain type="primary" icon="EditPen" @click="handleUpdate(row)">
              编辑
            </el-button>
            <el-button v-permission="['sys:tenant:delete']" size="small" plain type="danger" icon="Delete" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" v-model:page="listQuery.page" v-model:limit="listQuery.limit" :total="total" :page-sizes="pageArray" @pagination="getList" />

      <tenant-form ref="form" @refresh-list="getList" />

    </div>
  </el-card>
</template>

<script>
import { deleteTenant, fetchTenantList } from '@/api/sys/tenant/tenant'
import tenantForm from './tenantForm'
import Pagination from '@/components/Pagination'

export default {
  name: 'TenantList',
  components: { tenantForm, Pagination },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      pageArray: this.$store.dictStore.pageArray,
      listQuery: {
        tenantId: undefined,
        contact: undefined,
        phone: undefined,
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
    getList() {
      this.listLoading = true
      fetchTenantList(this.listQuery).then(response => {
        this.list = response.data.data
        this.total = response.data.total
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
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
        deleteTenant(row.id).then(response => {
          if (response.data.code === 0) {
            this.getList()
            this.$message.success(response.data.msg)
          } else {
            this.$message.error(response.data.msg)
          }
        })
      })
    },
    handleCreate() {
      this.$refs.form.handleCreate()
    }
  }
}
</script>
