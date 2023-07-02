<template>
  <el-card class="el-card">
    <div>
      <div class="filter-container">
        <div class="filter-item">
          <span>模版名称:</span>
          <el-input v-model="listQuery.name" placeholder="请输入模版名称" @keyup.enter="handleFilter" />
        </div>
        <div class="filter-item">
          <span>模版编码:</span>
          <el-input v-model="listQuery.code" placeholder="请输入模版编码" @keyup.enter="handleFilter" />
        </div>
        <el-button class="filter-item" type="primary" icon="Search" @click="handleFilter">搜索</el-button>
        <el-button class="filter-item" type="primary" icon="Plus" @click="handleCreate">新增</el-button>
      </div>
      <el-table
        :key="tableKey"
        v-loading="listLoading"
        :data="list"
        fit
        highlight-current-row
        style="width: 100%"
        header-cell-class-name="header-cell"
      >
        <el-table-column label="模版名称">
          <template #default="scope">
            <span>{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column min-width="180" label="模版编码">
          <template #default="scope">
            <span>{{ scope.row.code }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160">
          <template #default="scope">
            <el-button v-permission="['email:template:update']" size="small" plain type="primary" icon="EditPen" @click="handleUpdate(scope.row)">编辑</el-button>
            <el-button v-permission="['email:template:delete']" size="small" icon="Delete" plain type="danger" @click="handleDelete(scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" v-model:page="listQuery.page" v-model:limit="listQuery.limit" :total="total" :page-sizes="pageArray" @pagination="getList" />

      <el-dialog v-model="dialogFormVisible" draggable class="dialog-title" :title="title" width="80%">
        <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="120px">
          <el-form-item label="模版名称" prop="name">
            <el-input v-model="temp.name" />
          </el-form-item>
          <el-form-item label="模版主题" prop="templateSubject">
            <el-input v-model="temp.templateSubject" />
          </el-form-item>
          <el-form-item label="模版内容" prop="templateContent">
            <el-input id="templateContentTinymce" v-model="temp.templateContent" :height="500" type="textarea" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="dialogFormVisible = false">取消</el-button>
          <el-button type="primary" :loading="loading" @click="title==='新增'?createData():updateData()">
            确定
          </el-button>
        </template>
      </el-dialog>

    </div>
  </el-card>
</template>
<script>
import { fetchTemplateList, createTemplate, deleteTemplate, updateTemplate } from '@/api/email/template'
import { getDictList } from '@/utils/dict'
import Pagination from '@/components/Pagination/index.vue'
export default {
  name: 'TemplateList',
  components: { Pagination },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      pageArray: this.$store.dictStore.pageArray,
      listQuery: {
        page: 1,
        limit: this.$store.dictStore.defaultPageSize,
        importance: undefined,
        title: undefined,
        type: undefined
      },
      businessTypeOptions: getDictList('business_type'),
      showReviewer: false,
      temp: {
        id: undefined,
        name: '',
        code: '',
        businessType: '',
        templateSubject: '',
        templateContent: '',
        configId: '',
        appId: ''

      },
      loading: false,
      dialogFormVisible: false,
      title: undefined,
      dialogPvVisible: false,
      pvData: [],
      rules: {
        appId: [{ required: true, message: '请选择所属应用', trigger: 'blur' }],
        configId: [{ required: true, message: '请选择签名配置', trigger: 'blur' }],
        businessType: [{ required: true, message: '请选择业务类型', trigger: 'blur' }],
        name: [{ required: true, message: '模版名称必填', trigger: 'change' }],
        templateSubject: [{ required: true, message: '模版ID必填', trigger: 'change' }],
        templateContent: [{ required: true, message: '模版内容必填', trigger: 'change' }]
      },
      appList: null,
      configList: null
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      fetchTemplateList(this.listQuery).then(response => {
        this.list = response.data.data
        this.total = response.data.total
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleModifyStatus(row, status) {
      this.$message({
        message: '操作成功',
        type: 'success'
      })
      row.status = status
    },
    resetTemp() {
      this.loading = false
      this.temp = {
        id: undefined,
        name: '',
        code: '',
        businessType: '',
        templateSubject: '',
        templateContent: '',
        configId: '',
        appId: ''
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
          createTemplate(this.temp).then((response) => {
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
      this.resetTemp()
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
          updateTemplate(tempData).then((response) => {
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
    handleDelete(row) {
      this.$confirm('确定删除该数据吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteTemplate(row.id).then(response => {
          if (response.data.code === 0) {
            this.getList()
            this.$message.success(response.data.msg)
          } else {
            this.$message.error(response.data.msg)
          }
        })
      })
    }
  }
}
</script>
