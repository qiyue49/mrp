<template>
  <el-dialog v-model="dialogFormVisible" draggable class="dialog-title" :title="title" :close-on-click-modal="false">
    <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="70px">
      <el-form-item label="商品名称" prop="name">
        <el-input v-model="temp.name" />
      </el-form-item>
      <el-form-item label="所属店铺" prop="shop">
        <el-input v-model="temp.shop" />
      </el-form-item>
      <el-form-item label="商品分类" prop="category">
        <el-input v-model="temp.category" />
      </el-form-item>
      <el-form-item label="店铺地址" prop="address">
        <el-input v-model="temp.address" />
      </el-form-item>
      <el-form-item label="商品描述" prop="description">
        <el-input v-model="temp.description" />
      </el-form-item>
      <el-form-item label="标签" prop="tag">
        <el-input v-model="temp.tag" />
      </el-form-item>
      <el-form-item label="图片" prop="image">
        <upload-image v-model="temp.image" />
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
import { createExpandTable, updateExpandTable, getExpandTable } from '@/api/demo/expandTable/expandTable'
import UploadImage from '@/components/Upload/uploadImage'

export default {
  name: 'ExpandTableForm',
  components: { UploadImage },
  emits: ['refreshList'],
  data() {
    return {
      rules: {
      },
      temp: {
        id: undefined,
        name: undefined,
        shop: undefined,
        category: undefined,
        address: undefined,
        description: undefined,
        tag: undefined,
        image: undefined,
        createDate: undefined,
        updateBy: undefined,
        updateDate: undefined,
        remarks: undefined,
        delFlag: undefined,
        createBy: undefined,
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
        name: undefined,
        shop: undefined,
        category: undefined,
        address: undefined,
        description: undefined,
        tag: undefined,
        image: undefined,
        createDate: undefined,
        updateBy: undefined,
        updateDate: undefined,
        remarks: undefined,
        delFlag: undefined,
        createBy: undefined,
        remark: ''
      }
      const fileList = [
        {
          name: 'food.jpeg',
          url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'
        },

        {
          name: 'food.jpeg',
          url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'
        },
        {
          name: 'food.jpeg',
          url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'
        },
        {
          name: 'food.jpeg',
          url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'
        }
      ]
      this.temp.image = JSON.stringify(fileList)
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
          createExpandTable(this.temp).then(response => {
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
      getExpandTable(id).then(response => {
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
          updateExpandTable(tempData).then(response => {
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
