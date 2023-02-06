<template>
  <el-dialog v-model="dialogFormVisible" :title="title" :close-on-click-modal="false">
    <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="80px">
      <el-row :gutter="40">
        <el-col :span="24">
          <el-form-item label="内容标题" prop="newsContentTitle">
            <el-input v-model="temp.newsContentTitle" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发布时间" prop="newsReleaseTime">
            <el-date-picker v-model="temp.newsReleaseTime" type="datetime" value-format="yyyy-MM-dd HH:mm" format="yyyy-MM-dd HH:mm" style="width: 100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="新闻类型" prop="newsTypeId">
            <el-select v-model="temp.newsTypeId" placeholder="请选择" style="width: 100%" @change="test"> <!--select选择器的根部-->
              <el-option v-for="item in options" :key="item.value" :label="item.value" :value="item.label"> <!--每一条的option、就是选项内容，用遍历 的形式出现多个-->
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="相关图片" prop="img">
            <upload-image v-model="temp.img" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="内容描述" prop="newsContentDescribe">
            <Tinymce ref="editor" v-model="temp.newsContentDescribe" type="textarea" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button @click="dialogFormVisible = false">
        取消
      </el-button>
      <el-button type="primary" @click="title==='新增'?createData():updateData()">
        确定
      </el-button>
    </template>
  </el-dialog>
</template>

<script>
import {
  createNewsContent,
  updateNewsContent,
  getNewsContent,
  getNewsContentType
} from '@/api/biz/newsContent/newsContent'
import Tinymce from '@/components/Tinymce/index'
import uploadImage from '@/components/Upload/uploadImage'

export default {
  name: 'NewsContentForm',
  components: { Tinymce, uploadImage },
  emits: ['refreshList'],
  data() {
    return {
      dialogVisible: false,
      options: [],
      rules: {
        newsContentTitle: [{ required: true, message: '内容标题为必填项', trigger: 'blur' }],
        newsContentDescribe: [{ required: true, message: '内容描述为必填项', trigger: 'blur' }],
        newsReleaseTime: [{ required: true, message: '发布时间为必填项', trigger: 'blur' }],
        newsTypeId: [{ required: true, message: '新闻类型id为必填项', trigger: 'blur' }]
      },
      temp: {},
      title: undefined,
      dialogFormVisible: false
    }
  },

  methods: {
    test() {
      console.log(this.temp.newsTypeId)
    },
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
        newsContentTitle: undefined,
        newsContentDescribe: undefined,
        newsReleaseTime: undefined,
        img: undefined,
        readingsNumber: undefined,
        newsTypeId: undefined
      }
    },
    handleCreate() {
      this.resetTemp()
      this.title = '新增'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        // console.log("ddddddddddddddddddd")
        this.$refs.dataForm.clearValidate()
      })
      this.options = []
      getNewsContentType().then(response => {
        response.data.data.forEach(res => {
          this.options.push({ value: res.newsTypeName, label: res.id })
          console.log(this.options)
        })
      })
    },
    createData() {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          createNewsContent(this.temp).then(response => {
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

      this.options = []
      getNewsContentType().then(response => {
        response.data.data.forEach(res => {
          this.options.push({ value: res.newsTypeName, label: res.id })
        })
      })
      getNewsContent(id).then(response => {
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
          const tempData = Object.assign({}, this.temp)
          updateNewsContent(tempData).then(response => {
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
