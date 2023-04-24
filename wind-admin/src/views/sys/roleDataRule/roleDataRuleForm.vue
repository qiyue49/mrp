<template>
  <el-dialog v-model="dialogFormVisible" draggable class="dialog-title" :title="title" :close-on-click-modal="false">
    <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="70px">
      <el-form-item label="主键" prop="id">
        <el-input v-model="temp.id" />
      </el-form-item>
      <el-form-item label="权限类型(1:数据权限、2:接口权限)" prop="scopeCategory">
        <el-input v-model="temp.scopeCategory" />
      </el-form-item>
      <el-form-item label="权限id" prop="scopeId">
        <el-input v-model="temp.scopeId" />
      </el-form-item>
      <el-form-item label="角色id" prop="roleId">
        <el-input v-model="temp.roleId" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogFormVisible = false">
        取消
      </el-button>
      <el-button type="primary" :loading="loading" @click="title==='新增'?createData():updateData()">
        确定
      </el-button>
    </template>
  </el-dialog>
</template>

<script>
import { createRoleDataRule, updateRoleDataRule, getRoleDataRule } from '@/api/sys/roleDataRule/roleDataRule'

export default {
  name: 'RoleDataRuleForm',
  emits: ['refreshList'],
  data() {
    return {
      rules: {
      },
      temp: {
        id: undefined,
        scopeCategory: undefined,
        scopeId: undefined,
        roleId: undefined,
        remark: ''
      },
      title: undefined,
      loading: false,
      dialogFormVisible: false
    }
  },
  methods: {
    getList() {
      this.$emit('refreshList')
    },
    resetTemp() {
      this.loading = false
      this.temp = {
        id: undefined,
        scopeCategory: undefined,
        scopeId: undefined,
        roleId: undefined,
        remark: ''
      }
    },
    handleCreate() {
      this.resetTemp()
      this.title = '新增'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs.dataForm.clearValidate()
      })
    },
    createData() {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.loading = true
          createRoleDataRule(this.temp).then(response => {
            this.loading = false
            if (response.data.code === 0) {
              this.getList()
              this.dialogFormVisible = false
              this.$message.success(response.data.msg)
            } else {
              this.$message.error(response.data.msg)
            }
          })
        }
      })
    },
    handleUpdate(id) {
      this.resetTemp()
      this.title = '编辑'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs.dataForm.clearValidate()
      })
      getRoleDataRule(id).then(response => {
        if (response.data.code === 0) {
          this.temp = response.data.data
        } else {
          this.dialogFormVisible = false
          this.$message.error(response.data.msg)
        }
      })
    },
    updateData() {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.loading = true
          const tempData = Object.assign({}, this.temp)
          updateRoleDataRule(tempData).then(response => {
            this.loading = false
            if (response.data.code === 0) {
              this.dialogFormVisible = false
              this.getList()
              this.$message.success(response.data.msg)
            } else {
              this.$message.error(response.data.msg)
            }
          })
        }
      })
    }
  }
}
</script>
