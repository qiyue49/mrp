<template>
  <el-card class="el-card">
    <el-row :gutter="40">
      <el-col :span="6">
        <div>
          <div class="filter-container left-tree-list">
            <el-table :data="treeList" row-key="id" highlight-current-row header-cell-class-name="header-cell" @row-click="submitForm">
              <el-table-column prop="name" label="名称" />
            </el-table>
          </div>
        </div>
      </el-col>
      <el-col :span="18">
        <div>
          <div class="filter-container">
            <div class="filter-item">
              <span>姓名:</span>
              <el-input v-model="listQuery.realname" placeholder="请输入姓名" @keyup.enter="handleFilter" />
            </div>
            <div class="filter-item">
              <span>用户名:</span>
              <el-input v-model="listQuery.username" placeholder="请输入用户名" @keyup.enter="handleFilter" />
            </div>
            <div class="filter-item">
              <span>手机号码:</span>
              <el-input v-model="listQuery.phone" placeholder="请输入手机号码" @keyup.enter="handleFilter" />
            </div>
            <el-button v-permission="['sys:user:list']" v-waves class="filter-item" type="primary" icon="Search" @click="handleFilter">搜索</el-button>
            <el-button v-permission="['sys:user:add']" v-waves class="filter-item" type="primary" icon="Plus" @click="handleCreate">新增</el-button>
            <el-button v-permission="['sys:user:export']" v-waves class="filter-item" :loading="downloadLoading" type="primary" icon="Download" @click="handleDownload">导出</el-button>
          </div>

          <el-table
            :key="tableKey"
            v-loading="listLoading"
            :data="list"
            fit
            highlight-current-row
            style="width: 100%"
            header-cell-class-name="header-cell"
          >
            <el-table-column min-width="150" label="姓名">
              <template #default="scope">
                <span>{{ scope.row.realname }}</span>
              </template>
            </el-table-column>
            <el-table-column min-width="120" label="用户名">
              <template #default="scope">
                <span>{{ scope.row.username }}</span>
              </template>
            </el-table-column>
            <el-table-column min-width="120" label="联系电话">
              <template #default="scope">
                <span>{{ scope.row.phone }}</span>
              </template>
            </el-table-column>
            <el-table-column min-width="160" label="可否登录">
              <template #default="scope">
                <el-tag effect="plain" :type="statusFilter(scope.row.status)">
                  {{ dictLabel(scope.row.status, 'sf') }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column min-width="160" label="部门">
              <template #default="scope">
                <span>{{ scope.row.organization.name }}</span>
              </template>
            </el-table-column>
            <el-table-column min-width="350" label="操作">
              <template #default="scope">
                <el-button v-permission="['sys:user:update']" size="small" type="primary" plain icon="EditPen" @click="handleUpdate(scope.row)">编辑</el-button>
                <el-button v-permission="['sys:user:delete']" size="small" plain type="danger" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
                <el-button v-permission="['sys:user:role']" size="small" type="primary" plain icon="User" @click="toAssignRoles(scope.row)">设置角色</el-button>
                <el-button v-permission="['sys:user:change:password']" size="small" type="primary" plain icon="Refresh" @click="handleModifyPassword(scope.row)">重置密码</el-button>
              </template>
            </el-table-column>
          </el-table>

          <pagination v-show="total>0" v-model:page="listQuery.page" v-model:limit="listQuery.limit" :total="total" :page-sizes="pageArray" @pagination="getList" />

          <user-form ref="form" @refresh-list="getList" />

          <el-dialog v-model="dialogFormPasswordVisible" custom-class="dialog-title" title="修改密码">
            <el-form ref="dataModifyForm" :rules="modifyPasswordRules" :model="modifyPassword" label-position="left" label-width="100px" style="width: 400px; margin-left:50px;">
              <el-form-item label="初始密码" prop="password">
                <el-input v-model="modifyPassword.password" type="password" />
              </el-form-item>
            </el-form>
            <template #footer>
              <el-button @click="dialogFormPasswordVisible = false">取消</el-button>
              <el-button type="primary" :loading="loading" @click="postModifyPassword">确定</el-button>
            </template>
          </el-dialog>

          <el-dialog v-model="dialogFormRolesVisible" custom-class="dialog-title" class="setname" title="设置角色">
            <el-transfer
              v-model="userRoleIds"
              :filterable="true"
              :props="{key: 'id',label: 'name'}"
              :titles="['全选', '全选']"
              :format="{noChecked: '${total}', hasChecked: '${checked}/${total}'}"
              :data="roleList"
              @change="handleChangeRoles"
            />
          </el-dialog>
        </div>
      </el-col>
    </el-row>
  </el-card>

</template>

<script>
import { fetchList, deleteUser, modifyPassword, exportUser } from '@/api/sys/user'
import { fetchOrganizationList } from '@/api/sys/organization'
import { fetchUsableRoleList } from '@/api/sys/role'
import { fetchUserRoleIds, insertByUserId, deleteByUserId } from '@/api/sys/userRole'
import permission from '@/directive/permission/permission'
import waves from '@/directive/waves' // 水波纹指令
import userForm from './userForm'
import Pagination from '@/components/Pagination/index.vue'
export default {
  name: 'UserList',
  components: { Pagination, userForm },
  directives: {
    waves, permission
  },
  data() {
    return {
      treeList: [],
      treeProps: {
        value: 'id',
        label: 'name'
      },
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      pageArray: this.$store.dictStore.pageArray,
      listQuery: {
        page: 1,
        limit: this.$store.dictStore.defaultPageSize,
        importance: undefined,
        title: undefined,
        type: undefined,
        organization: {
          id: undefined
        }
      },
      downloadLoading: false,
      dialogFormRolesVisible: false,
      roleList: [],
      userRoleIds: [],
      selectCurentUserId: null,
      modifyPassword: {
        id: undefined,
        password: undefined
      },
      loading: false,
      dialogFormPasswordVisible: false,
      modifyPasswordRules: {
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getTreeList()
    this.getList()
    this.getUsableRoleList()
  },
  methods: {
    statusFilter(status) {
      const statusMap = {
        1: ' ',
        0: 'error'
      }
      return statusMap[status]
    },
    getTreeList() {
      this.listLoading = true
      fetchOrganizationList(this.listQuery).then(response => {
        this.treeList = response.data.data
        this.listLoading = false
      })
    },
    submitForm(data) {
      console.log(data)
      this.listQuery.organization.id = data.id
      this.getList()
    },
    getList() {
      this.listLoading = true
      fetchList(this.listQuery).then(response => {
        this.list = response.data.data
        this.total = response.data.total
        this.listLoading = false
      })
    },
    getUsableRoleList() {
      fetchUsableRoleList().then(response => {
        this.roleList = response.data.data
      })
    },
    getUserRoleIds(userId) {
      fetchUserRoleIds(userId).then(response => {
        this.userRoleIds = response.data
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
    handleCreate() {
      this.$refs.form.handleCreate()
    },
    handleUpdate(row) {
      this.$refs.form.handleUpdate(row.id)
    },
    handleModifyPassword(row) {
      this.modifyPassword.id = row.id // copy obj
      this.dialogFormPasswordVisible = true
      this.$nextTick(() => {
        this.$refs.dataModifyForm.clearValidate()
      })
    },
    postModifyPassword() {
      this.loading = false
      this.$refs.dataModifyForm.validate((valid) => {
        if (valid) {
          this.loading = true
          const tempData = Object.assign({}, this.modifyPassword)
          modifyPassword(tempData).then(response => {
            this.loading = false
            if (response.data.code === 0) {
              this.dialogFormPasswordVisible = false
              this.$message.success(response.data.msg)
            } else {
              this.$message.error(response.data.msg)
            }
          })
        }
      })
    },
    handleDelete(row) {
      this.$confirm('确定删除该数据吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteUser(row.id).then(response => {
          if (response.data.code === 0) {
            this.getList()
            this.$message.success(response.data.msg)
          } else {
            this.$message.error(response.data.msg)
          }
        })
      })
    },
    handleInsertByUserId(selectCurentUserId, roleIds) {
      const idsStr = roleIds.join(',')
      insertByUserId(selectCurentUserId, idsStr).then((response) => {
        if (response.data.code === 0) {
          this.$message.success('设置成功')
        } else {
          this.$message.error(response.data.msg)
        }
      })
    },
    handleDeleteByUserId(selectCurentUserId, roleIds) {
      const idsStr = roleIds.join(',')
      deleteByUserId(selectCurentUserId, idsStr).then((response) => {
        if (response.data.code === 0) {
          this.$message('撤回成功')
        } else {
          this.$message.error(response.data.msg)
        }
      })
    },
    handleDownload() {
      this.downloadLoading = false
      exportUser(this.listQuery).then(response => {
        this.downloadLoading = true
        if (response.data.code === 0) {
          import('@/vendor/Export2Excel').then(excel => {
            excel.export_byte_to_excel(response.data.data.bytes, response.data.data.title)
            this.downloadLoading = false
          })
        } else {
          this.$message.error(response.data.msg)
        }
      })
    },
    toAssignRoles(row) {
      this.selectCurentUserId = row.id
      this.dialogFormRolesVisible = true
      this.getUserRoleIds(this.selectCurentUserId)
    },
    handleChangeRoles(value, direction, movedKeys) {
      if (direction === 'left') {
        this.handleDeleteByUserId(this.selectCurentUserId, movedKeys)
      } else {
        this.handleInsertByUserId(this.selectCurentUserId, movedKeys)
      }
    }
  }
}
</script>
