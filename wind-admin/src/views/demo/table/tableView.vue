<template>
  <el-card class="el-card">
    <div>
      <div class="tit">返回上一级</div>
      <div style="color:#1762F2;fontSize:1.2rem">新 增 ：</div>
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="70px">
        <div class="flexdi">
          <el-form-item prop="title">
            <span>标题：</span>
            <el-input v-model="temp.title" />
          </el-form-item>
          <el-form-item prop="type">
            <div>类型：</div>
            <el-select v-model="temp.type" class="filter-item" placeholder="Please select">
              <el-option v-for="item in calendarTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
            </el-select>
          </el-form-item>
        </div>
        <div class="flexdi">
          <el-form-item prop="publishDate">
            <div>发布时间：</div>
            <el-date-picker v-model="temp.publishDate" type="datetime" value-format="yyyy-MM-dd HH:mm" format="yyyy-MM-dd HH:mm" />
          </el-form-item>
          <el-form-item prop="author">
            <div>作者：</div>
            <system-user v-model="temp.author" />
          </el-form-item>
          <el-form-item>
            <div>状态：</div>
            <el-select v-model="temp.status" class="filter-item" placeholder="Please select">
              <el-option v-for="item in statusOptions" :key="item" :label="item" :value="item" />
            </el-select>
          </el-form-item>
          <el-form-item label="重要程度：">
            <!-- <div>重要程度：</div> -->
            <el-rate v-model="temp.level" :colors="['#99A9BF', '#F7BA2A', '#FF9900']" :max="3" style="margin-top:8px;" />
          </el-form-item>
        </div>

        <el-form-item>
          <div>内容：</div>
          <Tinymce ref="editor" v-model="temp.content" type="textarea" placeholder="Please input" />

        </el-form-item>
      </el-form>
      <div>
        <el-button @click="getList">
          取消
        </el-button>
        <el-button type="primary" :loading="loading" @click="title==='新增'?createData():updateData()">
          确定
        </el-button>
      </div>
    </div>
  </el-card>
</template>

<script>
import { createTable, updateTable, getTable } from '@/api/demo/table/table'
import Tinymce from '@/components/Tinymce'
import SystemUser from '@/components/System/systemUser'
import { setTagsViewTitle } from '@/utils/i18n'

const calendarTypeOptions = [
  { key: 'CN', display_name: 'China' },
  { key: 'US', display_name: 'USA' },
  { key: 'JP', display_name: 'Japan' },
  { key: 'EU', display_name: 'Eurozone' }
]

export default {
  name: 'TableForm',
  components: { SystemUser, Tinymce },
  data() {
    return {
      rules: {
        type: [{ required: true, message: 'type is required', trigger: 'change' }],
        timestamp: [{ type: 'date', required: true, message: 'timestamp is required', trigger: 'change' }],
        title: [{ required: true, message: 'title is required', trigger: 'blur' }, { validator: this.formValidate.validateIngeterGTZRule, tigger: 'blur' }]
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
      loading: false
    }
  },
  created() {
    this.temp.id = this.$route.params.id
    this.title = this.$route.params.title
    this.setTagsViewTitle()
    this.handleUpdate(this.temp.id)
  },
  methods: {
    setTagsViewTitle() {
      setTagsViewTitle(this.$route, '综合表单-' + this.title)
    },
    getList() {
      const view = { path: this.$route.path }
      this.$store.tagsViewStore.delView(view).then(({ visitedViews }) => {
        this.$router.go(-1)
      })
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
      this.$nextTick(() => {
        this.$refs.dataForm.clearValidate()
      })
      getTable(id).then(response => {
        if (response.data.code === 0) {
          this.temp = response.data.data
        } else {
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
.tinymce-container{
  width: 100% !important;
}
.flexdi{
  display: flex;
}
</style>

