<template>
  <el-dialog v-model="dialogFormVisible" draggable class="dialog-title" :title="title" :close-on-click-modal="false">
    <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px" style="width: 400px; margin-left:50px;">
      <el-form-item label="角色名称" prop="name">
        <el-input v-model="temp.name" />
      </el-form-item>
      <el-form-item label="角色编码" prop="code">
        <el-input v-model="temp.code" />
      </el-form-item>
      <el-form-item v-if="isDefaultTenant" label="是否系统数据" prop="isSys">
        <el-radio
          v-for="item in dictList('sf')"
          :key="item.id"
          v-model="temp.isSys"
          :label="item.value"
        >
          {{ item.label }}
        </el-radio>
      </el-form-item>
      <el-form-item label="是否可用" prop="usable">
        <el-radio
          v-for="item in dictList('sf')"
          :key="item.id"
          v-model="temp.usable"
          :label="item.value"
        >
          {{ item.label }}
        </el-radio>
      </el-form-item>
      <el-form-item label="备注">
        <el-input
          v-model="temp.remarks"
          :autosize="{ minRows: 2, maxRows: 4}"
          type="textarea"
          placeholder="请输入内容"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogFormVisible = false">取消</el-button>
      <el-button v-permission="['sys:role:update']" type="primary" :loading="loading" @click="title==='新增'?createData():updateData()">
        确定
      </el-button>
    </template>
  </el-dialog>
</template>

<script>
import { addRole, updateRole } from '@/api/sys/role'

export default {
  name: 'RoleForm',
  emits: ['refreshList'],
  data() {
    return {
      temp: {},
      tenantId: this.$store.userStore.userInfo.id,
      loading: false,
      dialogFormVisible: false,
      title: undefined,
      rules: {
        code: [{ required: true, message: '角色编码必填', trigger: 'change' }],
        name: [{ required: true, message: '角色名称必填', trigger: 'change' }],
        isSys: [{ required: true, message: '是否系统必选', trigger: 'blur' }],
        usable: [{ required: true, message: '是否可用必选', trigger: 'blur' }]
      }

    }
  },
  computed: {
    isDefaultTenant() {
      return this.tenantId === '00000000'
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
        isSys: this.isDefaultTenant ? '1' : '0',
        usable: '1',
        name: '',
        code: '',
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
          addRole(this.temp).then((response) => {
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
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.title = '编辑'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs.dataForm.clearValidate()
      })
    },
    updateData() {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.loading = true
          const tempData = Object.assign({}, this.temp)
          updateRole(tempData).then((response) => {
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

<style scoped>

</style>
