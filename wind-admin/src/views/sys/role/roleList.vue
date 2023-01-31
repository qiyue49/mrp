<template>
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
      <el-button v-permission="['sys:role:list']" v-waves class="filter-item" type="primary" icon="Search" @click="handleFilter">搜索</el-button>
      <el-button v-permission="['sys:role:add']" class="filter-item" type="primary" icon="Plus" @click="handleCreate">新增</el-button>
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
    >
      <el-table-column width="200" align="center" label="角色名称">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column width="150" label="角色编码">
        <template slot-scope="scope">
          <span>{{ scope.row.code }}</span>
        </template>
      </el-table-column>
      <el-table-column width="200" align="center" label="是否可用">
        <template slot-scope="scope">
          <span>{{  dictLabel(scope.row.usable, 'sf') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-if="scope.row.tenantId === tenantId" slot-scope="scope">
          <el-button v-permission="['sys:role:update']" size="small" type="primary" text icon="Setting" @click="toSetMenu(scope.row)">设置菜单</el-button>
          <el-button v-permission="['sys:role:update']" size="small" type="primary" text icon="Operation" @click="toSetPermission(scope.row)">设置权限</el-button>
          <el-button v-permission="['sys:datarule:update']" size="small" type="primary" text icon="Finished" @click="toSetDataRule(scope.row)">数据权限</el-button>
          <el-button v-permission="['sys:role:detail']" size="small" type="primary" text icon="Edit" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button v-permission="['sys:role:delete']" size="small" type="danger" text icon="Delete" @click="handleDelete(scope.row)">删除
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

    <role-form ref="form" @refreshList="getList" />
    <!--设置权限-->
    <role-setting-form ref="settingForm" />

    <role-data-rule-list ref="ruleForm" />
  </div>
</template>

<script>
import { fetchList, deleteRole } from '@/api/sys/role'
import roleDataRuleList from '../roleDataRule/roleDataRuleList'
import waves from '@/directive/waves'
import permission from '@/directive/permission/permission'
import RoleForm from './roleForm'
import RoleSettingForm from './roleSettingForm' // 水波纹指令

export default {
  name: 'RoleList',
  components: { RoleSettingForm, RoleForm, roleDataRuleList },
  directives: {
    waves, permission
  },
  data() {
    return {
      tableKey: 0,
      tenantId: this.$store.userStore.userInfo.tenantId,
      list: null,
      total: null,
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
