<template>
  <el-dialog title="导入" :visible.sync="dialogFormVisible" width="30%" :close-on-click-modal="false">
    <el-upload
      v-loading="uploadLoading"
      :data="uploadData"
      :action="uploadImageUrl"
      :show-file-list="false"
      :headers="myHeaders"
      :on-success="handleSuccess"
      :on-error="handleError"
      :before-upload="beforeUpload"
      drag
      :multiple="false">
      <i class="el-icon-upload"></i>
      <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
      <div slot="tip" class="el-upload__tip">请按照模板上传数据</div>
    </el-upload>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogFormVisible = false">
        {{ $t('table.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleTemplate">
        {{ $t('table.template') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getToken } from '@/utils/auth'
import request from '@/utils/request'

export default {
  name: 'Import',
  props: {
    limit: {
      type: Number,
      required: false,
      default: 10
    },
    templateUrl: {
      type: String,
      default: undefined
    },
    importUrl: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      uploadImageUrl: 'http://' + process.env.VUE_APP_BASE_API + this.importUrl,
      uploadData: { 'base_path': this.basePath },
      myHeaders: { access_token: getToken() },
      extensions: ['xls', 'xlsx'],
      dialogFormVisible: false,
      uploadLoading: false,
      waringLabel: '只能上传' + this.extension + '文件，且不超过' + this.limit + 'M'
    }
  },
  methods: {
    show() {
      this.dialogFormVisible = true
    },
    handleTemplate() {
      request({
        url: this.templateUrl,
        method: 'get'
      }).then(response => {
        if (response.data.code === 0) {
          import('@/vendor/Export2Excel').then(excel => {
            excel.export_byte_to_excel(response.data.data.bytes, response.data.data.title)
            this.downloadLoading = false
          })
        } else {
          this.$message.error(response.data.msg)
        }
      })
    },
    handleSuccess(response) {
      if (response.code === 0) {
        this.uploadLoading = false
        this.$message.success(response.msg)
        this.dialogFormVisible = false
      } else {
        this.$message.error(response.msg)
      }
    },
    handleError(response) {
      this.uploadLoading = false
      if (response.msg) {
        this.$message.error(response.msg)
      } else {
        this.$message.error('上传失败')
      }
    },
    beforeUpload(file) {
      // 扩展名判断
      const fileExtension = this.getFileExtension(file.name)
      if (fileExtension === '') {
        this.$message.error('文件必须有扩展名')
        return false
      }

      if (this.extensions !== '') {
        const isFileExtension = this.extensions.includes(fileExtension)
        if (!isFileExtension) {
          this.$message.error('上传文件只能是' + this.extensions.join(',') + '格式!')
          return false
        }
      }
      // 判断文件大小
      const isLimitSize = file.size / 1024 / 1024 < this.limit
      if (!isLimitSize) {
        this.$message.error('上传头像图片大小不能超过 ' + this.limit + 'MB!')
      }
      if (isLimitSize) {
        this.uploadLoading = true
      }
      return isLimitSize
    },
    getFileExtension(fileName) {
      if (fileName.lastIndexOf('.') > 0) {
        return fileName.substring(fileName.lastIndexOf('.') + 1)
      }
      return ''
    }
  }
}
</script>

<style scoped>

</style>
