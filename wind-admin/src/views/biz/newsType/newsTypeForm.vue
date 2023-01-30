<template>
  <el-dialog :title="textMap[dialogStatus]" v-model="dialogFormVisible" :close-on-click-modal="false">
    <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="80px" style="width: 80%; margin-left:50px;">
      <el-row :gutter="40">
        <el-col :span="24">
          <el-form-item label="类型名称" prop="newsTypeName">
            <el-input v-model="temp.newsTypeName" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button @click="dialogFormVisible = false">
        取消
      </el-button>
      <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
        确定
      </el-button>
    </template>
  </el-dialog>
</template>

<script>
import { createNewsType, updateNewsType, getNewsType } from '@/api/biz/newsType/newsType'

export default {
  name: 'NewsTypeForm',
  data() {
    return {
      rules: {
        newsTypeName: [{ required: true, message: '新闻类型名称为必填项', trigger: 'blur' }]
      },
      temp: {},
      textMap: {
        update: '编辑',
        create: '新建'
      },
      dialogFormVisible: false,
      dialogStatus: ''
    }
  },
  methods: {
    getList() {
      this.$emit('refreshList')
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        createBy: undefined,
        createDate: undefined,
        updateBy: undefined,
        updateDate: undefined,
        delFlag: undefined,
        remarks: undefined,
        tenant: undefined,
        newsTypeName: undefined
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          createNewsType(this.temp).then(response => {
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
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
      getNewsType(id).then(response => {
        if (response.data.code === 0) {
          this.temp = response.data.data
        } else {
          this.dialogFormVisible = false
          this.$message.error(response.data.msg)
        }
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          updateNewsType(tempData).then(response => {
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
