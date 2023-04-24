<template>
  <el-dialog v-model="dialogFormVisible" draggable width="800px" title="新建" class="dialog-title" destroy-on-close>
    <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="70px">
      <el-form-item label="标题" prop="title">
        <el-input v-model="temp.title" />
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-select v-model="temp.type" class="filter-item" placeholder="Please select">
          <el-option v-for="item in calendarTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
        </el-select>
      </el-form-item>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="发布时间" prop="publishDate">
            <el-date-picker v-model="temp.publishDate" type="datetime" value-format="yyyy-MM-dd HH:mm" format="yyyy-MM-dd HH:mm" />
          </el-form-item>
        </el-col>
        <el-col :span="16">
          <el-form-item label="作者" prop="author">
            <system-user v-model="temp.author" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="状态">
        <el-select v-model="temp.status" class="filter-item" placeholder="Please select">
          <el-option v-for="item in statusOptions" :key="item" :label="item" :value="item" />
        </el-select>
      </el-form-item>
      <el-form-item label="重要程度">
        <el-rate v-model="temp.level" :colors="['#99A9BF', '#F7BA2A', '#FF9900']" :max="3" style="margin-top:8px;" />
      </el-form-item>
      <el-form-item label="内容">
        <Tinymce ref="editor" v-model="temp.content" type="textarea" placeholder="Please input" />

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
import { createTable, updateTable, getTable } from '@/api/demo/table/table'
import Tinymce from '@/components/Tinymce'
import SystemUser from '@/components/System/systemUser'

const calendarTypeOptions = [
  { key: 'CN', display_name: 'China' },
  { key: 'US', display_name: 'USA' },
  { key: 'JP', display_name: 'Japan' },
  { key: 'EU', display_name: 'Eurozone' }
]

export default {
  name: 'TableForm',
  components: { SystemUser, Tinymce },
  emits: ['refreshList'],
  data() {
    return {
      rules: {
        type: [{ required: true, message: 'type is required', trigger: 'change' }],
        timestamp: [{ type: 'date', required: true, message: 'timestamp is required', trigger: 'change' }],
        title: [{ required: true, message: 'title is required', trigger: 'blur' }, { validator: this.formValidate.isIntegerGEZRule, tigger: 'blur' }]
      },
      statusOptions: ['published', 'draft', 'deleted'],
      temp: {
        id: undefined,
        level: 1,
        remark: '',
        publishDate: undefined,
        content: undefined,
        title: '',
        type: undefined,
        status: 'published',
        author: ''
      },
      calendarTypeOptions,
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
        level: 1,
        remark: '',
        publishDate: undefined,
        content: undefined,
        title: '',
        type: 'china',
        status: 'published'
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
          createTable(this.temp).then(response => {
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
      getTable(id).then(response => {
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
          updateTable(tempData).then(response => {
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
<style lang="scss" scoped>
:deep(.el-dialog__header){
background-color: #000 !important;
    display: flex;
    justify-content: center !important;
}
:deep(.el-dialog){
  .el-dialog__header{
    background-color: #000 !important;
    display: flex;
    justify-content: center !important;
  }
  .el-dialog__body{
    .tit{
  display: flex;
  justify-content: center;
}
  }

}
:deep(.el-form-item){
  display: flex;
  flex-direction: column;
}
.formflex{
  display: flex;
}
</style>

