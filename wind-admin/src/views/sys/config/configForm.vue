<template>
  <el-dialog v-model="dialogFormVisible" draggable class="dialog-title" :title="title" :close-on-click-modal="false">
    <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="80px">
      <el-row :gutter="40">
        <el-col :span="12">
          <el-form-item label="配置名称" prop="name">
            <el-input v-model="temp.name" :disabled="title!=='新增' && temp.isSys" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="配置编码" prop="code">
            <el-input v-model="temp.code" :disabled="title!=='新增' && temp.isSys" />
          </el-form-item>
        </el-col>
        <el-col v-if="title==='新增'" :span="12">
          <el-form-item label="组件类型" prop="type">
            <el-select v-model="temp.type" placeholder="请选择组件类型">
              <el-option
                v-for="item in dictList('sys_config_component_type')"
                :key="item.value + 'filter_status'"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col v-if="title==='新增' && temp.type === '5'" :span="12">
          <el-form-item label="配置项" prop="setting">
            <el-input v-model="temp.setting" placeholder="请选择配置项"/>
          </el-form-item>
        </el-col>
        <el-col v-if="title==='新增'" :span="12">
          <el-form-item label="系统参数" prop="isSys">
            <el-radio
              v-for="item in dictList('sf_bool')"
              :key="'type' + item.label"
              v-model="temp.isSys"
              :label="item.value"
            >
              {{ item.label }}
            </el-radio>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="配置值" prop="value">
            <el-input v-if="temp.type === '1'" v-model="temp.value" type="textarea" />
            <upload-image v-if="temp.type === '2'" v-model="temp.value" />
            <upload-file v-if="temp.type === '3'" v-model="temp.value" />
            <baidu-map-point v-if="temp.type === '4'" v-model="temp.value" />
            <el-select v-if="temp.type === '5'" v-model="temp.value" placeholder="请选择配置值">
              <el-option
                v-for="item in dictList(temp.setting)"
                :key="item.value + 'filter_status'"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="remarks">
            <el-input v-model="temp.remarks" type="textarea" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button @click="dialogFormVisible = false">
        取消
      </el-button>
      <el-button v-permission="['sys:config:update']" type="primary" :loading="loading" @click="title==='新增'?createData():updateData()">
        确定
      </el-button>
    </template>
  </el-dialog>
</template>

<script>
import { updateConfig, createConfig, getConfig } from '@/api/sys/config'
import UploadFile from '@/components/Upload/uploadFile'
import UploadImage from '@/components/Upload/uploadImage'
import BaiduMapPoint from '@/components/BaiduMap/baiduMapPoint'

export default {
  name: 'ConfigForm',
  components: { BaiduMapPoint, UploadImage, UploadFile },
  emits: ['refreshList'],
  data() {
    return {
      temp: {},
      title: undefined,
      loading: false,
      dialogFormVisible: false,
      rules: {
        name: [{ required: true, message: '配置名称为必填', trigger: 'blur' }],
        code: [{ required: true, message: '配置编码为必填', trigger: 'blur' }],
        setting: [{ required: true, message: '配置项为必填', trigger: 'blur' }],
        type: [{ required: true, message: '组件类型为必填', trigger: 'blur' }],
        isSys: [{ required: true, message: '是否系统参数为必填', trigger: 'blur' }],
        value: [{ required: true, message: '配置值为必填', trigger: 'blur' }]
      }
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
        name: undefined,
        code: undefined,
        setting: undefined,
        value: undefined,
        type: '1',
        isSys: 'false'
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
          createConfig(this.temp).then(response => {
            this.loading = false
            if (response.data.code === 0) {
              this.getList()
              this.$store.sysConfigStore.getConfig()
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
      getConfig(id).then(response => {
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
          updateConfig(tempData).then(response => {
            this.loading = false
            if (response.data.code === 0) {
              this.dialogFormVisible = false
              this.getList()
              this.$store.sysConfigStore.getConfig()
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

