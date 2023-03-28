<template>
  <div class="app-container">
    <div class="filter-container">
      <div class="filter-item">
        <span>资源编号:</span>
        <el-input v-model="listQuery.resourceCode" placeholder="请输入资源编号" />
        <span>数据权限名称:</span>
        <el-input v-model="listQuery.scopeName" placeholder="请输入数据权限名称" />
        <el-button v-waves class="filter-item" icon="Search" type="primary" @click="handleFilter">
          查询
        </el-button>
        <el-button v-permission="['sys:datarule:add']" icon="Plus" class="filter-item" type="danger" @click="handleCreate">
          新增
        </el-button>
      </div>

    </div>

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      fit
      highlight-current-row
      style="width: 100%;"
      :header-cell-style="{background:'#F4F7FC',color:'#1762F2',borderTop:'4px solid #1762F2'}">
      <el-table-column label="资源编号" min-width="150px">
        <template #default="{row}">
          <span>{{ row.resourceCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="数据权限名称" min-width="150px">
        <template #default="{row}">
          <span>{{ row.scopeName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="数据权限字段" min-width="150px">
        <template #default="{row}">
          <span>{{ row.scopeField }}</span>
        </template>
      </el-table-column>
      <el-table-column label="数据权限类型" min-width="150px">
        <template #default="{row}">
          <span>{{ dictLabel(row.scopeType, 'data_rule_type') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="230">
        <template #default="{row}">
          <el-button v-permission="['sys:datarule:update']" size="small" type="primary" icon="EditPen" plain @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button v-permission="['sys:datarule:delete']" size="small" plain icon="Delete" type="error" @click="handleDelete(row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" v-model:page="listQuery.page" v-model:limit="listQuery.limit" :total="total" :page-sizes="pageArray" @pagination="getList" />

    <data-rule-form ref="form" @refresh-list="getList" />

  </div>
</template>

<script>
import { deleteDataRule, fetchDataRuleList } from '@/api/sys/dataRule/dataRule'
import waves from '@/directive/waves' // waves directive
import permission from '@/directive/permission/permission'
import dataRuleForm from './dataRuleForm'
import Pagination from '@/components/Pagination'
export default {
  name: 'DataRuleList',
  components: { dataRuleForm, Pagination },
  directives: { waves, permission },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      pageArray: this.$store.dictStore.pageArray,
      listQuery: {
        resourceCode: undefined,
        scopeName: undefined,
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
      fetchDataRuleList(this.listQuery).then(response => {
        this.list = response.data.data
        this.total = response.data.total
        // Just to simulate the time of the request
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
        deleteDataRule(row.id).then(response => {
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
