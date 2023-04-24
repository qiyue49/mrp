<template>
  <el-dialog v-model="dialogFormVisible" draggable class="dialog-title" :title="title">
    <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="80px" style="width: 400px; margin-left:50px;">
      <el-form-item label="汽车型号" prop="label">
        <el-input v-model="temp.name" />
      </el-form-item>
      <el-form-item label="型号值" prop="value">
        <el-input v-model="temp.value" />
      </el-form-item>
      <el-form-item label="排序" prop="sort">
        <el-input v-model="temp.sort" />
      </el-form-item>
      <el-form-item label="备注">
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
import { createCarModel, updateCarModel, getCarModel } from '@/api/demo/twoTable/carModel'

export default {
  name: 'CarModelForm',
  emits: ['getList'],
  data() {
    return {
      car: {},
      temp: {
        id: undefined,
        name: '',
        value: '',
        sort: 1,
        remarks: '',
        catId: undefined
      },
      loading: false,
      dialogFormVisible: false,
      title: undefined,
      rules: {
        name: [{ required: true, message: '汽车型号标签不能为空', trigger: 'change' }],
        value: [{ required: true, message: '汽车型号值不能为空', trigger: 'change' }],
        sort: [{ required: true, message: '排序不能为空', trigger: 'change' }]
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
        value: '',
        sort: 1,
        remarks: '',
        carId: this.car.id
      }
    },
    handleCreate(data) {
      this.car = data
      if (this.car.id === undefined || this.car.id === '') {
        this.$message.success('请选择汽车型号分组')
      } else {
        this.resetTemp()
        this.title = '新增'
        this.dialogFormVisible = true
        this.$nextTick(() => {
          this.$refs.dataForm.clearValidate()
        })
      }
    },
    createData() {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.loading = true
          createCarModel(this.temp).then((response) => {
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
      getCarModel(id).then(response => {
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
          updateCarModel(tempData).then((response) => {
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
