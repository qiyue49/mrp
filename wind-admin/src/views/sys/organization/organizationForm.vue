<template>
  <el-dialog v-model="dialogFormVisible" draggable class="dialog-title" :title="title">
    <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="100px" style="margin-left:50px;margin-right:50px;">
      <el-form-item label="上级部门" prop="parentIds">
        <el-cascader
          v-model="temp.parentIds"
          :options="list"
          :props="treeProps"
          :show-all-levels="false"
          label="name"
          change-on-select
          clearable
        />
      </el-form-item>
      <el-form-item label="部门名称" prop="name">
        <el-input v-model="temp.name" />
      </el-form-item>
      <el-form-item label="备注">
        <el-input v-model="temp.remarks" :autosize="{ minRows: 2, maxRows: 4}" type="textarea" placeholder="Please input" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogFormVisible = false">取消</el-button>
      <el-button v-permission="['sys:organization:update']" type="primary" :loading="loading" @click="title==='新增'?createData():updateData()">
        确定
      </el-button>
    </template>
  </el-dialog>
</template>

<script>
import { fetchOrganization, createOrganization, updateOrganization } from '@/api/sys/organization'

export default {
  name: 'OrganizationForm',
  emits: ['refreshList'],
  data() {
    return {
      rules: {
        name: [{ required: true, message: '名称为必填项', trigger: 'blur' }]
      },
      list: [],
      temp: {},
      title: undefined,
      treeProps: {
        value: 'id',
        label: 'name',
        checkStrictly: true
      },
      loading: false,
      dialogFormVisible: false
    }
  },
  methods: {
    getList() {
      this.$emit('refreshList')
    },
    setList(list) {
      this.list = list
    },
    resetTemp() {
      this.loading = false
      this.temp = {
        id: undefined,
        name: undefined,
        parentIds: undefined,
        remark: undefined
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
      // 预处理提交的数据
      const parentIds = this.temp.parentIds
      if (parentIds !== undefined && parentIds !== '') {
        if (parentIds instanceof Array && parentIds.length > 0) {
          const parentId = parentIds[parentIds.length - 1]
          this.temp.parentId = parentId
        }
      }
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.loading = true
          if (this.temp.parentIds !== undefined && this.temp.parentIds !== '') {
            this.temp.parentIds.length = 0
          }
          createOrganization(this.temp).then((response) => {
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
    handleUpdate(id) {
      this.resetTemp()
      this.title = '编辑'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs.dataForm.clearValidate()
      })
      fetchOrganization(id).then(response => {
        if (response.data.code === 0) {
          this.temp = response.data.data // copy obj
          if (this.temp.children !== undefined) {
            this.temp.children.length = 0
          }
          if (this.temp.parentIds !== undefined) {
            let parentIds = this.temp.parentIds.trim()
            if (parentIds.length > 0) {
              parentIds = parentIds.substr(0, parentIds.length - 1)
            }
            this.temp.parentIds = parentIds.split('\/')
          }
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
          const parentIds = this.temp.parentIds
          if (parentIds !== undefined && parentIds !== '') {
            if (parentIds instanceof Array && parentIds.length > 0) {
              const parentId = parentIds[parentIds.length - 1]
              this.temp.parentId = parentId
            }
          }
          if (this.temp.parentIds !== undefined && this.temp.parentIds !== '') {
            this.temp.parentIds = undefined
          }
          if (this.temp.parent !== undefined) {
            this.temp.parent = undefined
          }
          const tempData = Object.assign({}, this.temp)
          console.log(tempData)
          updateOrganization(tempData).then((response) => {
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
