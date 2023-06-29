<template>
  <el-card class="el-card">
    <div class="app-container">
      <div class="filter-container">
        <div class="filter-item">
          <span>配置名称:</span>
          <el-input v-model="listQuery.name" placeholder="请输入配置名称" />
        </div>
        <div class="filter-item">
          <span>配置编码:</span>
          <el-input v-model="listQuery.code" placeholder="请输入配置编码" />
        </div>
        <el-button class="filter-item" type="primary" icon="Search" @click="handleFilter">
          搜索
        </el-button>
        <el-button v-permission="['sys:config:add']" class="filter-item" type="primary" icon="Plus" @click="handleCreate">
          新增
        </el-button>
      </div>
      <el-table
        :key="tableKey"
        v-loading="listLoading"
        :data="list"
        highlight-current-row
        style="width: 100%;"
        header-cell-class-name="header-cell"
      >
        <el-table-column min-width="150" label="配置名称">
          <template #default="{row}">
            {{ row.name }}
          </template>
        </el-table-column>
        <el-table-column min-width="150" label="配置编码">
          <template #default="{row}">
            {{ row.code }}
          </template>
        </el-table-column>
        <el-table-column min-width="150" label="参数值">
          <template #default="{row}">
            {{ row.value }}
          </template>
        </el-table-column>
        <el-table-column min-width="150" label="备注">
          <template #default="{row}">
            {{ row.remarks }}
          </template>
        </el-table-column>
        <el-table-column min-width="150" label="操作">
          <template #default="{row}">
            <el-button v-permission="['sys:config:update']" size="small" type="primary" icon="EditPen" plain @click="handleUpdate(row)">
              编辑
            </el-button>
            <el-button v-if="!row.isSys" v-permission="['sys:config:delete']" size="small" icon="Delete" plain type="danger" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" v-model:page="listQuery.page" v-model:limit="listQuery.limit" :total="total" :page-sizes="pageArray" @pagination="getList" />

      <config-form ref="form" @refresh-list="getList" />
    </div>
  </el-card>
</template>

<script>
import { fetchConfigList, deleteConfig } from '@/api/sys/config'
import configForm from './configForm'
import Pagination from '@/components/Pagination/index.vue'
export default {
  name: 'Config',
  components: { Pagination, configForm },
  data() {
    return {
      tableKey: 0,
      list: null,
      listLoading: true,
      total: 0,
      listQuery: {
        name: undefined,
        code: undefined,
        page: 1,
        limit: this.$store.dictStore.defaultPageSize
      },
      pageArray: this.$store.dictStore.pageArray
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      fetchConfigList(this.listQuery).then(response => {
        this.listLoading = false
        if (response.data.code === 0) {
          this.total = response.data.total
          this.list = response.data.data
        }
      })
    },
    handleCreate() {
      this.$refs.form.handleCreate()
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
        deleteConfig(row.id).then(response => {
          if (response.data.code === 0) {
            this.getList()
            this.$message.success(response.data.msg)
          } else {
            this.$message.error(response.data.msg)
          }
        })
      })
    }
  }
}
</script>

