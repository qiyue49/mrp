<template>
  <el-dialog v-model="dialogFormVisible" draggable class="dialog-title" :title="title" :close-on-click-modal="false">
    <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="70px">
      <el-form-item label="上级" prop="parentIds">
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
      <el-form-item label="机构名称" prop="name">
        <el-input v-model="temp.name" />
      </el-form-item>
      <el-form-item label="地理编码" prop="geocoding">
        <el-input v-model="temp.geocoding" />
      </el-form-item>
      <el-form-item label="邮政编码" prop="postalCode">
        <el-input v-model="temp.postalCode" />
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
import { createTreeTable, updateTreeTable, getTreeTable } from '@/api/demo/treeTable/treeTable'

export default {
  name: 'TreeTableForm',
  emits: ['refreshList'],
  data() {
    return {
      rules: {
      },
      temp: {
        id: undefined,
        name: undefined,
        geocoding: undefined,
        postalCode: undefined,
        parentId: undefined,
        parentIds: undefined,
        createBy: undefined,
        createDate: undefined,
        updateBy: undefined,
        updateDate: undefined,
        delFlag: undefined,
        remarks: undefined,
        remark: ''
      },
      list: [],
      treeProps: {
        value: 'id',
        label: 'name',
        expandTrigger: 'hover'
      },
      title: undefined,
      loading: false,
      dialogFormVisible: false
    }
  },
  methods: {
    setList(list) {
      this.list = list
    },
    getList() {
      this.$emit('refreshList')
    },
    resetTemp() {
      this.loading = false
      this.temp = {
        id: undefined,
        name: undefined,
        geocoding: undefined,
        postalCode: undefined,
        parentId: undefined,
        parentIds: undefined,
        createBy: undefined,
        createDate: undefined,
        updateBy: undefined,
        updateDate: undefined,
        delFlag: undefined,
        remarks: undefined,
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
          // 预处理提交的数据
          const parentIds = this.temp.parentIds
          if (parentIds !== undefined && parentIds !== '') {
            if (parentIds instanceof Array && parentIds.length > 0) {
              const parentId = parentIds[parentIds.length - 1]
              this.temp.parentId = parentId
            }
          }

          if (this.temp.parentIds !== undefined && this.temp.parentIds !== '') {
            this.temp.parentIds.length = 0
          }
          this.loading = true
          createTreeTable(this.temp).then(response => {
            this.loading = false
            if (response.data.code === 0) {
              this.getList()
              this.dialogFormVisible = false
              this.$message.success('创建成功')
            } else {
              this.dialogFormVisible = false
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
      getTreeTable(id).then(response => {
        if (response.data.code === 0) {
          this.temp = response.data.data
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
          this.loading = true
          updateTreeTable(tempData).then((response) => {
            this.loading = true
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
