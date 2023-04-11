<template>
  <el-dialog v-model="dialogFormVisible" draggable custom-class="dialog-title" :title="title" :close-on-click-modal="false">
    <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="80px">
      <el-row :gutter="40">
        <el-col :span="24">
          <el-form-item label="标题" prop="title">
            <el-input v-model="temp.title" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="用户" prop="userId">
            <system-user v-model="temp.userId" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="部门" prop="organizationId">
            <system-organization v-model="temp.organizationId" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="日期" prop="date">
            <el-date-picker v-model="temp.date" type="datetime" value-format="yyyy-MM-dd HH:mm" format="yyyy-MM-dd HH:mm" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="类型" prop="type">
            <el-select v-model="temp.type" class="filter-item" placeholder="请选择类型">
              <el-option
                v-for="item in dictList('event_type')"
                :key="'type' + item.label"
                :label="item.label"
                :value="item.value" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="内容" prop="content">
            <el-input v-model="temp.content" type="textarea" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="位置" prop="location">
            <baidu-map-line v-model="temp.location" />
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
      <el-button type="primary" @click="title==='新增'?createData():updateData()">
        确定
      </el-button>
    </template>
  </el-dialog>
</template>

<script>
import { createEvent, updateEvent, getEvent } from '@/api/biz/event/event'
import systemUser from '@/components/System/systemUser'
import systemOrganization from '@/components/System/systemOrganization'
import BaiduMapPoint from '@/components/BaiduMap/baiduMapPoint'
import uploadImage from '@/components/Upload/uploadImage'
import BaiduMapRect from '@/components/BaiduMap/baiduMapRect.vue'
import BaiduMapLine from '@/components/BaiduMap/baiduMapLine.vue'

export default {
  name: 'EventForm',
  components: { BaiduMapLine, BaiduMapRect, systemUser, systemOrganization, BaiduMapPoint, uploadImage },
  emits: ['refreshList'],
  data() {
    return {
      rules: {
        title: [{ required: true, message: '标题为必填项', trigger: 'blur' }],
        userId: [{ required: true, message: '用户为必填项', trigger: 'blur' }],
        organizationId: [{ required: true, message: '部门为必填项', trigger: 'blur' }],
        content: [{ required: true, message: '内容为必填项', trigger: 'blur' }]
      },
      temp: {},
      title: undefined,
      dialogFormVisible: false
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
        title: undefined,
        userId: undefined,
        organizationId: undefined,
        date: undefined,
        type: undefined,
        content: undefined,
        location: undefined,
        image: undefined
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
          createEvent(this.temp).then(response => {
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
      getEvent(id).then(response => {
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
          updateEvent(tempData).then(response => {
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
