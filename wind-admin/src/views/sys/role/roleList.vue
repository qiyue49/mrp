<template>
  <el-card class="el-card">
    <div>
      <div class="filter-container">
        <div class="filter-item">
          <span>角色名称:</span>
          <el-input v-model="listQuery.name" placeholder="请输入角色名称" @keyup.enter="handleFilter" />
        </div>
        <div class="filter-item">
          <span>角色编码:</span>
          <el-input v-model="listQuery.code" placeholder="请输入角色编码" @keyup.enter="handleFilter" />
        </div>
        <el-button v-permission="['sys:role:list']" class="filter-item" type="primary" icon="Search" @click="handleFilter">搜索</el-button>
        <el-button v-permission="['sys:role:add']" class="filter-item" type="primary" icon="Plus" @click="handleCreate">新增</el-button>
      </div>

      <el-table v-loading="listLoading" style="border:none" :data="list" fit highlight-current-row header-cell-class-name="header-cell">
        <el-table-column min-width="150" label="角色名称">
          <template #default="{row}">
            <span>{{ row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="150" label="角色编码">
          <template #default="{row}">
            <span>{{ row.code }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="150" label="是否可用">
          <template #default="{row}">
            <span>{{ dictLabel(row.usable, 'sf') }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="200" label="操作">
          <template #default="{row}">
            <span v-if="row.tenantId === tenantId">
              <el-button v-permission="['sys:role:update']" size="small" type="primary" plain icon="Setting" @click="toSetMenu(row)">设置菜单</el-button>
              <el-button v-permission="['sys:role:update']" size="small" type="primary" plain icon="Operation" @click="toSetPermission(row)">设置权限</el-button>
              <el-button v-permission="['sys:datarule:update']" size="small" type="primary" plain icon="Finished" @click="toSetDataRule(row)">数据权限</el-button>
              <el-button v-permission="['sys:role:detail']" size="small" type="primary" plain icon="EditPen" @click="handleUpdate(row)">编辑</el-button>
              <el-button v-permission="['sys:role:delete']" size="small" plain type="danger" @click="handleDelete(row)">删除
              </el-button>
            </span>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" v-model:page="listQuery.page" v-model:limit="listQuery.limit" :total="total" :page-sizes="pageArray" @pagination="getList" />

      <role-form ref="form" @refresh-list="getList" />
      <!--设置权限-->
      <role-setting-form ref="settingForm" />

      <role-data-rule-list ref="ruleForm" />
    </div>
  </el-card>
</template>

<script>
import { fetchList, deleteRole } from '@/api/sys/role'
import roleDataRuleList from '../roleDataRule/roleDataRuleList'
import RoleForm from './roleForm'
import RoleSettingForm from './roleSettingForm'
import Pagination from '@/components/Pagination/index.vue'
export default {
  name: 'RoleList',
  components: { Pagination, RoleSettingForm, RoleForm, roleDataRuleList },
  data() {
    return {
      tableKey: 0,
      tenantId: this.$store.userStore.userInfo.tenantId,
      list: null,
      total: 0,
      listLoading: true,
      pageArray: this.$store.dictStore.pageArray,
      listQuery: {
        page: 1,
        limit: this.$store.dictStore.defaultPageSize,
        importance: undefined,
        title: undefined,
        type: undefined
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      fetchList(this.listQuery).then(response => {
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
    toSetMenu(row) {
      this.$refs.settingForm.toSetMenu(row, 1)
    },
    toSetPermission(row) {
      this.$refs.settingForm.toSetMenu(row, 2)
    },
    handleUpdate(row) {
      this.$refs.form.handleUpdate(row)
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
        deleteRole(row.id).then(response => {
          if (response.data.code === 0) {
            this.getList()
            this.$message.success(response.data.msg)
          } else {
            this.$message.error(response.data.msg)
          }
        })
      })
    },
    toSetDataRule(row) {
      this.$refs.ruleForm.handleRoleDataRuleUpdate(row.id)
    }
  }
}
</script>
