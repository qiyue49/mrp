<template>
  <el-dialog v-model="dialogFormVisible" draggable class="dialog-title" :title="title" :close-on-click-modal="false">
    <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="70px">
      <el-form-item label="区域" prop="areaId">
        <el-cascader
          v-model="temp.areaId"
          :options="treeList"
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
      <el-form-item label="类型" prop="type">
        <el-input v-model="temp.type" />
      </el-form-item>
      <el-form-item label="标签" prop="tag">
        <el-input v-model="temp.tag" />
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
import { createTreeAndTable, updateTreeAndTable, getTreeAndTable } from '@/api/demo/treeAndTable/treeAndTable'

export default {
  name: 'TreeAndTableForm',
  emits: ['refreshList'],
  data() {
    return {
      rules: {
      },
      temp: {
        id: undefined,
        name: undefined,
        type: undefined,
        tag: undefined,
        areaId: undefined,
        createDate: undefined,
        updateBy: undefined,
        updateDate: undefined,
        remarks: undefined,
        delFlag: undefined,
        createBy: undefined,
        remark: ''
      },
      treeList: [],
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
    setList(treeList) {
      this.treeList = treeList
    },
    getList() {
      this.$emit('refreshList')
    },
    resetTemp() {
      this.loading = false
      this.temp = {
        id: undefined,
        name: undefined,
        type: undefined,
        tag: undefined,
        areaId: undefined,
        createDate: undefined,
        updateBy: undefined,
        updateDate: undefined,
        remarks: undefined,
        delFlag: undefined,
        createBy: undefined,
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
          createTreeAndTable(this.temp).then(response => {
            this.loading = false
            if (response.data.code === 0) {
              this.getList()
              this.dialogFormVisible = false
              this.$message.success('创建成功')
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
      getTreeAndTable(id).then(response => {
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
          updateTreeAndTable(tempData).then(response => {
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
