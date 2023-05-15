<template>
  <el-dialog v-model="dialogFormVisible" draggable class="dialog-title" :title="title" :close-on-click-modal="false">
    <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px">
      <el-form-item label="租户名称" prop="name">
        <el-input v-model="temp.name" />
      </el-form-item>
      <el-form-item label="联系人" prop="contact">
        <el-input v-model="temp.contact" />
      </el-form-item>
      <el-form-item label="电话" prop="phone">
        <el-input v-model="temp.phone" />
      </el-form-item>
      <el-form-item label="用户名" prop="userName">
        <el-input v-if="title==='新增'" v-model="temp.userName" />
        <span v-else> {{ temp.userName }}</span>
        默认密码为该租户创建后的租户标识
      </el-form-item>
      <el-form-item label="备注" prop="remarks">
        <el-input v-model="temp.remarks" type="textarea" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogFormVisible = false">
        取消
      </el-button>
      <el-button v-permission="['sys:tenant:update']" type="primary" :loading="loading" @click="title==='新增'?createData():updateData()">
        确定
      </el-button>
    </template>
  </el-dialog>
</template>

<script>
import { createTenant, updateTenant, getTenant } from '@/api/sys/tenant/tenant'

export default {
  name: 'TenantForm',
  emits: ['refreshList'],
  data() {
    return {
      rules: {
        contact: [{ required: true, message: '联系人为必填项', trigger: 'blur' }],
        phone: [{ required: true, message: '电话为必填项', trigger: 'blur' }, { validator: this.formValidate.validateTelOrMobileRule, tigger: 'blur' }],
        name: [{ required: true, message: '租户名称为必填项', trigger: 'blur' }],
        userName: [{ required: true, message: '用户名为必填项', trigger: 'blur' }]
      },
      temp: {
        id: undefined,
        createBy: undefined,
        createDate: undefined,
        updateBy: undefined,
        updateDate: undefined,
        delFlag: undefined,
        remarks: undefined,
        tenantId: undefined,
        contact: undefined,
        phone: undefined,
        name: undefined,
        userName: undefined,
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
        createBy: undefined,
        createDate: undefined,
        updateBy: undefined,
        updateDate: undefined,
        delFlag: undefined,
        remarks: undefined,
        tenantId: undefined,
        contact: undefined,
        phone: undefined,
        name: undefined,
        userName: undefined,
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
          createTenant(this.temp).then(response => {
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
      getTenant(id).then(response => {
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
          updateTenant(tempData).then(response => {
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
