<template>
  <el-dialog v-model="dialogFormVisible" draggable class="dialog-title" :title="title">
    <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="100px" style="width: 400px; margin-left:50px;">
      <el-form-item label="汽车品牌" prop="name">
        <el-input v-model="temp.name" />
      </el-form-item>
      <el-form-item label="品牌编码" prop="code">
        <el-input v-model="temp.code" />
      </el-form-item>
      <el-form-item label="品牌备注">
        <el-input v-model="temp.remarks" :autosize="{ minRows: 2, maxRows: 4}" type="textarea" placeholder="Please input" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogFormVisible = false">取消</el-button>
      <el-button type="primary" :loading="loading" @click="title==='新增'?createData():updateData()">
        确定
      </el-button>
    </template>
  </el-dialog>

</template>

<script>
import { createCar, updateCar, getCar } from '@/api/demo/twoTable/car'

export default {
  name: 'CarForm',
  emits: ['getList'],
  data() {
    return {
      temp: {
        id: undefined,
        name: '',
        code: '',
        remarks: ''
      },
      loading: false,
      dialogFormVisible: false,
      title: undefined,
      rules: {
        name: [{ required: true, message: '品牌名称必填', trigger: 'change' }],
        code: [{ required: true, message: '品牌编码必填', trigger: 'blur' }]
      }

    }
  },
  methods: {
    getList() {
      this.$emit('getList')
    },

    resetTemp() {
      this.loading = false
      this.temp = {
        id: undefined,
        name: '',
        code: '',
        remarks: ''
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
          createCar(this.temp).then((response) => {
            this.loading = false
            const data = response.data
            if (data.code === 0) {
              this.dialogFormVisible = false
              this.$message.success('创建成功')
              this.getList()
            } else {
              this.$message.error(data.msg)
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
      getCar(id).then(response => {
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
          updateCar(tempData).then((response) => {
            this.loading = false
            const data = response.data
            if (data.code === 0) {
              this.dialogFormVisible = false
              this.$message.success('更新成功')
              this.getList()
            } else {
              this.$message.error(data.msg)
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
