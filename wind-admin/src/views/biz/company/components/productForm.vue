<template>
  <el-dialog v-model="dialogFormVisible" custom-class="dialog-title" :title="title" :close-on-click-modal="false">
    <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="80px">
      <el-row :gutter="40">
        <el-col :span="24">
          <el-form-item label="名称" prop="name">
            <el-input v-model="temp.name" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="价格" prop="price">
            <el-input v-model="temp.price" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="数量" prop="count">
            <el-input v-model="temp.count" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="图片" prop="image">
            <upload-image v-model="temp.image" />
          </el-form-item>
        </el-col>
      </el-row>
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
import { createProduct, updateProduct, getProduct } from '@/api/biz/company/product'
import uploadImage from '@/components/Upload/uploadImage'

export default {
  name: 'ProductView',
  components: { uploadImage },
  emits: ['refreshList'],
  data() {
    return {
      rules: {
        name: [{ required: true, message: '名称为必填项', trigger: 'blur' }],
        price: [{ required: true, message: '价格为必填项', trigger: 'blur' }, { validator: this.formValidate.validateNumberGTZRule, tigger: 'blur' }],
        count: [{ required: true, message: '数量为必填项', trigger: 'blur' }, { validator: this.formValidate.validateNumberGTZRule, tigger: 'blur' }]
      },
      temp: {},
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
        tenant: undefined,
        name: undefined,
        price: undefined,
        count: undefined,
        image: undefined,
        companyId: undefined
      }
    },
    handleCreate(id) {
      this.resetTemp()
      this.temp.companyId = id
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
          createProduct(this.temp).then(response => {
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
      getProduct(id).then(response => {
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
          updateProduct(this.temp).then(response => {
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
