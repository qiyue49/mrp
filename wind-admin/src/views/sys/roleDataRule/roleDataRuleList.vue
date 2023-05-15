<template>
  <el-drawer v-model="dialogFormVisible" :direction="'rtl'" :title="title" size="50%">
    <div class="app-container">
      <div class="filter-container">
        <div class="filter-item">
          <span>资源编号:</span>
          <el-input v-model="listQuery.resourceCode" placeholder="请输入资源编号" />
        </div>
        <div class="filter-item">
          <span>数据权限名称:</span>
          <el-input v-model="listQuery.scopeName" placeholder="请输入数据权限名称" />
        </div>
        <el-button class="filter-item" icon="Search" type="primary" @click="handleFilter">
          查询
        </el-button>
        <el-button v-permission="['sys:datarule:add']" icon="Plus" class="filter-item" type="primary" @click="handleCreate">
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
        @selection-change="handleSelectionChange">
        <el-table-column
          type="selection"
          width="55" />
        <el-table-column label="资源编号" min-width="100px">
          <template #default="{row}">
            <span>{{ row.resourceCode }}</span>
          </template>
        </el-table-column>
        <el-table-column label="数据权限名称" min-width="100px">
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
            <el-button v-permission="['sys:datarule:update']" size="small" icon="EditPen" type="primary" plain @click="handleUpdate(row)">
              编辑
            </el-button>
            <el-button v-permission="['sys:datarule:delete']" size="small" icon="Delete" plain type="danger" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" v-model:page="listQuery.page" v-model:limit="listQuery.limit" :total="total" :page-sizes="pageArray" @pagination="getList" />

      <data-rule-form ref="form" @refresh-list="getList" />

    </div>
    <div class="drawer-footer">
      <el-button @click="dialogFormVisible = false">
        取消
      </el-button>
      <el-button type="primary" :loading="loading" @click="updateData">
        确定
      </el-button>
    </div>
  </el-drawer>
</template>

<script>
import { deleteDataRule } from '@/api/sys/dataRule/dataRule'
import { fetchRoleDataRuleList, updateRoleDataRules } from '@/api/sys/roleDataRule/roleDataRule'
import dataRuleForm from '../dataRule/dataRuleForm'
import Pagination from '@/components/Pagination'

export default {
  name: 'RoleDataRuleList',
  components: { dataRuleForm, Pagination },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      pageArray: this.$store.dictStore.pageArray,
      listQuery: {
        resourceCode: undefined,
        roleId: undefined,
        scopeName: undefined,
        page: 1,
        limit: this.$store.dictStore.defaultPageSize
      },
      temp: {
        roleId: undefined,
        ids: []
      },
      title: undefined,
      loading: false,
      dialogFormVisible: false,
      multipleSelection: []
    }
  },
  methods: {
    handleRoleDataRuleUpdate(id) {
      this.title = '编辑'
      this.loading = false
      this.dialogFormVisible = true
      this.listQuery.roleId = id
      this.temp.roleId = id
      this.handleFilter()
    },
    getList() {
      this.listLoading = true
      fetchRoleDataRuleList(this.listQuery).then(response => {
        this.list = response.data.data
        this.total = response.data.total
        this.listLoading = false
        this.$nextTick(() => {
          this.list.forEach(item => {
            if (item.select === true) {
              this.$refs.table.toggleRowSelection(item, true)
            }
          })
        })
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
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    updateData() {
      const ids = []
      this.multipleSelection.forEach(item => {
        ids.push(item.id)
      })
      this.temp.ids = ids.join(',')
      this.loading = true
      updateRoleDataRules(this.temp).then(response => {
        this.loading = false
        if (response.data.code === 0) {
          this.dialogFormVisible = false
          this.$message.success(response.data.msg)
        } else {
          this.$message.error(response.data.msg)
        }
      })
    }

  }
}
</script>
